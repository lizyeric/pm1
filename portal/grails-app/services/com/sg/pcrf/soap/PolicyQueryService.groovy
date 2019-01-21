package com.sg.pcrf.soap

import entity.PolicyAccount
import grails.transaction.Transactional
import groovy.json.JsonSlurper
import groovy.sql.Sql
import groovy.xml.MarkupBuilder
import soap.BaseSoapService

@Transactional
class PolicyQueryService implements BaseSoapService {
    def dataSource

    @Override
    def retrieveData(Object parsedXml) {
//        println('--' * 10 + 'in retrieve data' + '--' * 10)
        def sql = new Sql(dataSource)
        String startTime = parsedXml.StartTime
        String endTime = parsedXml.EndTime
        String policyCode = parsedXml.PolicyCode
        def responseList = []
        def querySql = makeQueryString(startTime, endTime, policyCode)
        def rows = sql.rows(querySql)
        rows.each { row ->
            PolicyAccount tempRecord = new PolicyAccount()
            def policyAction = parseJson(row?.policy_action)
            def policyCondition = parseJson(row?.policy_condition)
            if (null != row?.subid) {
                tempRecord.subscriber = row.subid
            }
            if (null != row?.policy_start_time){
                tempRecord.time = row.policy_start_time.format("yyyyMMddHHmmss")
            }
            if (0 != policyAction?.grant?.size()) {
                def quota = ""
                policyAction.grant.eachWithIndex { it, index ->
                    if (index < policyAction.grant.size() - 1) {
                        quota += it + ','
                    } else {
                        quota += it
                    }
                }
                tempRecord.quota = quota
            }
            if (null != policyCondition?.subscriber?.usrGrade) {
                tempRecord.usrGrade = policyCondition.subscriber.usrGrade
            }
            if (null != policyCondition?.ECI) {
                tempRecord.location = policyCondition.ECI
            }
            if (null != policyCondition["TDF-Application-Identifier"]) {
                tempRecord.destinationAddress = policyCondition["TDF-Application-Identifier"]
            }
            if (null != policyCondition?.TerminalType) {
                tempRecord.terminalType = policyCondition.TerminalType
            }
            responseList.add(tempRecord)
//            println(tempRecord)
        }
        if (0 == responseList.size()) {
            responseList = null
        }
        return responseList
    }

    def parseJson(json) {
//        parse policy condition and action
        def jsonSluper = new JsonSlurper()
        def parsedJson = jsonSluper.parseText(json)
        return parsedJson
    }

    @Override
    def makeQueryString(String startTime, String endTime, String policyCode) {
        def result = "SELECT pdd.policy_action, pdd.policy_condition, per.subid, per.policy_start_time\n" +
                "FROM policy_detail_data pdd JOIN policy_event_record per on pdd.policy_id = per.policy_id\n" +
                "WHERE pdd.policy_code = '$policyCode'\n" +
                "AND per.policy_start_time >= STR_TO_DATE('$startTime','%Y%m%d%H%i%s')\n" +
                "AND per.policy_start_time <= STR_TO_DATE('$endTime','%Y%m%d%H%i%s')"
        return result
    }

    @Override
    def makeSingleUserXml(Object uselessValue, Object policyRecordList) {
        def xml = ""
        policyRecordList.each{ record ->
            def stringWriter = new StringWriter()
            def recordBuilder = new MarkupBuilder(stringWriter)
            recordBuilder.PolicyRecord {
                attribute{
                    key("Subscriber")
                    value(record.subscriber)
                }
                attribute{
                    key("Time")
                    value(record.time)
                }
                attribute{
                    key("usrGrade")
                    value(record.usrGrade)
                }
                attribute{
                    key("Location")
                    value(record.location)
                }
                paras{
                    key("DestinationAddress")
                    value(record.destinationAddress)
                }
                attribute{
                    key("Quota")
                    value(record.quota)
                }
                attribute{
                    key("TerminalType")
                    value(record.terminalType)
                }
            }
            xml += stringWriter.toString() + '\n'
        }
        return xml
    }

    @Override
    def appendResponse(Object xml) {
        return "<PolicyQueryResponse xmlns=\"pcrf:soap\">\n<result xmlns=\"\">\n" + xml + "</result>\n</PolicyQueryResponse>\n"
    }
}
