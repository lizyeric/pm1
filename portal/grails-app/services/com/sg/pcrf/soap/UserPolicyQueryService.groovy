package com.sg.pcrf.soap

import entity.PolicyRecord
import grails.transaction.Transactional
import groovy.json.JsonSlurper
import groovy.sql.Sql
import groovy.xml.MarkupBuilder
import soap.BaseSoapService

@Transactional
class UserPolicyQueryService implements BaseSoapService {
    def dataSource

    @Override
    def retrieveData(Object parsedXml) {
//        println('--' * 10 + 'in retrieve data' + '--' * 10)
        def sql = new Sql(dataSource)
        String startTime = parsedXml.StartTime
        String endTime = parsedXml.EndTime
        List userList = parsedXml.userList
//        println(startTime + endTime + userList)
        def responseMap = [:]
        userList.each { subid ->
            def querySql = makeQueryString(startTime, endTime, subid)
            def rows = sql.rows(querySql)
            def rowList = [] // save policyRecord list
            rows.each { row ->
                PolicyRecord tempRecord = new PolicyRecord()
                def parsedJson = parseJson(row?.policy_condition)
                if (null != parsedJson["User Session Policy"]) {
                    tempRecord.policyCode = parsedJson["User Session Policy"]
                }
                if (null != parsedJson["current time"]) {
//                    println("have current time")
                    tempRecord.time = row?.policy_start_time?.format("yyyyMMddHHmmss")
                }
                if (null != parsedJson?.subscriber?.usrGrade) {
                    tempRecord.usrGrade = parsedJson.subscriber.usrGrade
                }
                if (null != parsedJson?.ECI) {
                    tempRecord.location = parsedJson.ECI
                }
                if (null != parsedJson["TDF-Application-Identifier"]) {
                    tempRecord.destinationAddress = parsedJson["TDF-Application-Identifier"]
                }
                if (null != parsedJson?.subscriber?.usrStatus) {
                    tempRecord.bossUsrQuotaStatus = parsedJson.subscriber.usrStatus
                }
                if (null != parsedJson?.ServiceUsageState) {
                    tempRecord.bossServiceQuotaStatus = parsedJson.ServiceUsageState
                }
                if (null != parsedJson?.TerminalType) {
                    tempRecord.terminalType = parsedJson.TerminalType
                }
                rowList.add(tempRecord)
//                println(tempRecord)
            }
            responseMap[subid] = rowList
        }

        def nullFlag = true
        for (value in responseMap.values()) {
            if (value.size() != 0) {
                nullFlag = false
                break
            }
        }
        if (nullFlag) {
            responseMap = null
        }
        return responseMap
    }

    def parseJson(json) {
//        parse policy condition
        def jsonSluper = new JsonSlurper()
        def parsedJson = jsonSluper.parseText(json)
        return parsedJson
    }

    @Override
    def makeQueryString(String startTime, String endTime, String subId) {
        def result = "SELECT\n" +
                "per.policy_start_time,\n" +
                "pdd.policy_condition\n" +
                "FROM\n" +
                "policy_event_record per\n" +
                "JOIN policy_detail_data pdd ON per.policy_id = pdd.policy_id\n" +
                "WHERE per.subid = '$subId'\n" +
                "AND per.policy_start_time >= STR_TO_DATE('$startTime','%Y%m%d%H%i%s')\n" +
                "AND per.policy_start_time <= STR_TO_DATE('$endTime','%Y%m%d%H%i%s')"
        return result
    }

    @Override
    def makeSingleUserXml(Object subId, Object policyRecordList) {
        def xml = ''
//        println("--" * 10 + "in make single xml" + "--" * 10)
//        println("subId:" + subId)
//        println("policy record list:" + policyRecordList)
        if (policyRecordList?.size() == 0) {
            def stringWriter = new StringWriter()
            def recordBuilder = new MarkupBuilder(stringWriter)
            recordBuilder.PolicyRecord {
                attribute {
                    key('PolicyCode')
                    value(0)
                }
                attribute {
                    key('Time')
                    value(0)
                }
                attribute {
                    key('usrGrade')
                    value(0)
                }
                attribute {
                    key('Location')
                    value(0)
                }
                attribute {
                    key('DestinationAddress')
                    value(0)
                }
                attribute {
                    key('BossUsrQuotaStatus')
                    value(0)
                }
                attribute {
                    key('BossServiceQuotaStatus')
                    value(0)
                }
                attribute {
                    key('PCRFQuota')
                    value(0)
                }
                attribute {
                    key('TerminalType')
                    value(0)
                }
            }
            xml += stringWriter.toString()
        } else {
            policyRecordList.each { record ->
                def stringWriter = new StringWriter()
                def recordBuilder = new MarkupBuilder(stringWriter)
                recordBuilder.PolicyRecord {
                    attribute {
                        key('PolicyCode')
                        value(record.policyCode)
                    }
                    attribute {
                        key('Time')
                        value(record.time)
                    }
                    attribute {
                        key('usrGrade')
                        value(record.usrGrade)
                    }
                    attribute {
                        key('Location')
                        value(record.location)
                    }
                    attribute {
                        key('DestinationAddress')
                        value(record.destinationAddress)
                    }
                    attribute {
                        key('BossUsrQuotaStatus')
                        value(record.bossUsrQuotaStatus)
                    }
                    attribute {
                        key('BossServiceQuotaStatus')
                        value(record.bossServiceQuotaStatus)
                    }
                    attribute {
                        key('PCRFQuota')
                        value(record.pcrfQuota)
                    }
                    attribute {
                        key('TerminalType')
                        value(record.terminalType)
                    }
                }
                xml += stringWriter.toString() + '\n'
            }
        }
        xml = "<User>\n<attribute>\n<key>Subscriber</key>\n<value>$subId</value>\n</attribute>\n" + xml + "</User>\n"
        return xml
    }

    @Override
    def appendResponse(Object xml) {
        return "<UserPolicyQueryResponse xmlns=\"pcrf:soap\">\n<result xmlns=\"\">\n" + xml + "</result>\n</UserPolicyQueryResponse>\n"
    }
}
