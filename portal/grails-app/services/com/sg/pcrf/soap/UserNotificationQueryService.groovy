package com.sg.pcrf.soap

import entity.Notification
import grails.transaction.Transactional
import groovy.sql.Sql
import groovy.xml.MarkupBuilder
import soap.BaseSoapService

@Transactional
class UserNotificationQueryService implements BaseSoapService {
    def dataSource

    def retrieveData(parsedXml) {
        String startTime = Date.parse("yyyyMMddHHmmss", parsedXml.StartTime).format("yyyy-MM-dd HH:mm:ss")
        String endTime = Date.parse("yyyyMMddHHmmss", parsedXml.EndTime).format("yyyy-MM-dd HH:mm:ss")
        List userList = parsedXml.userList
        def responseMap = [:]
//        println(startTime + '\n' + endTime + '\n' + userList)
        def sql = new Sql(dataSource)
        userList.each { subid ->
            def querySql = makeQueryString(startTime, endTime, subid)
            def rows = sql.rows(querySql)
            def rowList = [] //save notification object list
            rows.each { row ->
                Notification tempNotify = new Notification()
                tempNotify.time = Date.parse("yyyy-MM-dd HH:mm:ss",
                        row?.notify_date).format("yyyyMMddHHmmss")
                tempNotify.policyId = row?.policy_id
                if (row?.notify_type?.toInteger() == 0) {
                    tempNotify.form = "SMS"
                }
                if (row?.notify_type?.toInteger() == 1) {
                    tempNotify.form = "EMAIL"
                }
                if (row?.notify_type?.toInteger() == 2) {
                    tempNotify.form = "OTHER"
                }
                if (row?.notify_result?.toInteger() == 0) {
                    tempNotify.result = "SUCCESS"
                }
                if (row?.notify_result?.toInteger() == 1) {
                    tempNotify.result = "FAILED"
                }
                rowList.add(tempNotify)
            }
            responseMap[subid] = rowList
        }
//        null response
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

    def makeSingleUserXml(subId, notificationList) {
        def xml = ''
        if (notificationList.size() == 0) {
//            multiple users contain one or more null.
            def stringWriter = new StringWriter()
            def notificationBuilder = new MarkupBuilder(stringWriter)
            notificationBuilder.Notification {
                attribute {
                    key('Time')
                    value(0)
                }
                attribute {
                    key('PolicyID')
                    value(0)
                }
                attribute {
                    key('Form')
                    value(0)
                }
                attribute {
                    key('Result')
                    value(0)
                }
            }
            xml += stringWriter.toString() + '\n'
        } else {
            notificationList.each { obj -> //Notification obj
                def stringWriter = new StringWriter()
                def notificationBuilder = new MarkupBuilder(stringWriter)
                notificationBuilder.Notification {
                    attribute {
                        key('Time')
                        value(obj?.time)
                    }
                    attribute {
                        key('PolicyID')
                        value(obj?.policyId)
                    }
                    attribute {
                        key('Form')
                        value(obj?.form)
                    }
                    attribute {
                        key('Result')
                        value(obj?.result)
                    }
                }
                xml += stringWriter.toString() + '\n'
            }
        }
        xml = "<User>\n<attribute>\n<key>Subscriber</key>\n<value>$subId</value>\n</attribute>\n" + xml + "</User>\n"
        return xml
    }

    def appendResponse(xml) {
        return "<UserNotificationQueryResponse xmlns=\"pcrf:soap\">\n<result xmlns=\"\">\n" + xml + "</result>\n</UserNotificationQueryResponse>\n"
    }

    def makeQueryString(String startTime, String endTime, String subId) {
        def queryStr = "SELECT\n" +
                "\ta.*, b.policy_id\n" +
                "FROM\n" +
                "\tuser_notification_record a\n" +
                "LEFT JOIN policy_record_data b ON a.notify_policy_name = b.policy_name\n" +
                "WHERE\n" +
                "\tSTR_TO_DATE(notify_date, '%Y-%m-%d %H:%i:%s') >= STR_TO_DATE(\n" +
                "\t\t\"$startTime\",\n" +
                "\t\t'%Y-%m-%d %H:%i:%s'\n" +
                "\t)\n" +
                "AND STR_TO_DATE(notify_date, '%Y-%m-%d %H:%i:%s') <= STR_TO_DATE(\n" +
                "\t\"$endTime\",\n" +
                "\t'%Y-%m-%d %H:%i:%s'\n" +
                ")\n" +
                "AND subscriber_id = \"$subId\""
        return queryStr
    }
}
