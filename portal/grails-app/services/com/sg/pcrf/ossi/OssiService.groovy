package com.sg.pcrf.ossi

import com.sg.pcrf.exceptions.OssiException
import com.sg.pcrf.rcmgr.PolicyDetailData
import grails.transaction.Transactional
import groovy.json.JsonSlurper
import org.dom4j.Document
import org.dom4j.Node
import org.dom4j.io.SAXReader
import org.springframework.core.io.FileSystemResource
import org.springframework.core.io.support.PropertiesLoaderUtils

@Transactional
class OssiService {
    def grailsApplication

    def getConfigProperties() {
        def path = grailsApplication.config.basepath
        def resource = new FileSystemResource("${path}${File.separator}config${File.separator}ossi.properties")
        def properties = PropertiesLoaderUtils.loadProperties(resource)
        return properties
    }

    def saveRuleInstallRecord(Date entryTime, String rirStartTime, String rirEndTime) {
//        save rule install record to database
        def properties = getConfigProperties()
        def RIR_TEMPLATE_PATH = properties.ruleInstallTemplate
        def rirUrl = properties.ruleInstallUrl
        try {
            def template = renderRuleInstallTemplate(RIR_TEMPLATE_PATH, rirStartTime, rirEndTime, rirUrl)
            def xmlString = RunCommand.runCommand(template)
//            log.info("--" * 20)
//            println("length:" + xmlString.length() + "content:" + xmlString)
//            String xmlString = new File("D:\\workFile\\commandFile\\ossiReturn.xml").text
            def recordList = parseRuleInstallXml(xmlString)
            for (record in recordList) {
//                def newRecord = RuleInstallRecord.findOrCreateByStartTime(record.startTime)
//                newRecord.name = record.name
//                newRecord.endTime = record.endTime
//                newRecord.policyServer = record.policyServer
//                newRecord.isComplete = record.isComplete
//                newRecord.numberOfTimesInstalledAttemps = record.numberOfTimesInstalledAttemps
//                newRecord.numberOfTimesRemovedByPCRF = record.numberOfTimesRemovedByPCRF
//                newRecord.numberOfTimesFailedOrRemovedByGateway = record.numberOfTimesFailedOrRemovedByGateway
//                newRecord.numberOfTimesTotalRetryAttempts = record.numberOfTimesTotalRetryAttempts
//                newRecord.save()
                record.entryTime = entryTime
                record.save()
                if (record.hasErrors()) {
                    throw OssiException("sth error in rule_install_record save:" + record.errors)
                }
            }
        } catch (Exception e) {
            e.printStackTrace()
            throw new OssiException("error in saveRuleInstallRecord")
        }
    }

    def saveSpecificQuota() {
//        println('--' * 10 + 'in save specific quota' + '--' * 10)
        def pddList = PolicyDetailData.findAll()
        pddList.each { pdd ->
            def parsedJson = parseJson(pdd.policyAction)
            parsedJson?.grant?.each { quota ->
                saveSingleSpecificQuota(quota)
            }
        }
    }

    def parseJson(json) {
//        parse condition json
        def jsonSluper = new JsonSlurper()
        def parsedJson = jsonSluper.parseText(json)
        return parsedJson
    }

    def saveSingleSpecificQuota(String quotaName) {
        def properties = getConfigProperties()
        def SQ_TEMPLATE_PATH = properties.quotaTemplate
        def sqUrl = properties.quotaUrl
        try {
            def template = renderSpecificQuotaTemplate(SQ_TEMPLATE_PATH, quotaName, sqUrl)
            String quotaXml = RunCommand.runCommand(template)
//            for dev test
//            def quotaXml = new File("D:\\workFile\\commandFile\\specificQuota.xml").text
            def quotaList = parseSpecificQuotaXml(quotaXml)
            for (quota in quotaList) {
                def newQuota = SpecificQuota.findByName(quota.name)
                if (!newQuota) {
//                    create
                    newQuota = new SpecificQuota(name: quota.name)
                    newQuota.dateCreated = new Date()
                }
                newQuota.totalVolumeLimit = quota.totalVolumeLimit
                newQuota.lastUpdated = new Date()
                newQuota.save()
                if (newQuota.hasErrors()) {
                    throw OssiException("sth error in specific_quota save:" + newQuota.errors)
                }
            }
        } catch (Exception e) {
            e.printStackTrace()
            throw new OssiException("error in saveSingleSpecificQuota")
        }
    }

    String renderRuleInstallTemplate(String templateString, String startTime,
                                     String endTime, String outputUrl) throws OssiException {
//        render wget command template.
//        String strTemplate = Utils.readFile(templateString);
        String resTemplate = templateString.replace("*start_time*", startTime).replace("*end_time*", endTime)
                .replace("*output_url*", outputUrl);
        return resTemplate;
    }

    String renderSpecificQuotaTemplate(String templateString, String quotaName, String outputUrl) throws OssiException {
//        String strTemplate = Utils.readFile(templateFileName);
        String resTemplate = templateString.replace("*quota_name*", quotaName).replace("*output_url*", outputUrl);
        return resTemplate;
    }

    List<RuleInstallRecord> parseRuleInstallXml(String xmlString) throws OssiException {
        List<RuleInstallRecord> recordList = new ArrayList<RuleInstallRecord>();
//        parse the xml file and return a RuleInstallRecord list
        try {
            SAXReader reader = new SAXReader();
            Document document = reader.read(new StringReader(xmlString));

//            System.out.println("in ossi manager");
//            System.out.println(document.selectSingleNode("/Statistics/TrafficProfileStats").getText());

            if (document.selectSingleNode("/Statistics/TrafficProfileStats").getText().matches("^Not enough statistical data available to fulfill request.*")) {
                throw new OssiException("ossi get null data");
            }

            List<Node> nodes = document.selectNodes("/Statistics/TrafficProfileStats/Sample");
            for (Node node : nodes) {
                RuleInstallRecord record = new RuleInstallRecord();
                if (null != node.selectSingleNode("StartTime")) {
                    record.setStartTime(node.selectSingleNode("StartTime").getText());
                }
                if (null != node.selectSingleNode("EndTime")) {
                    record.setEndTime(node.selectSingleNode("EndTime").getText());
                }
                if (null != node.selectSingleNode("PolicyServer")) {
                    record.setPolicyServer(node.selectSingleNode("PolicyServer").getText());
                }
                if (null != node.selectSingleNode("IsComplete")) {
                    record.setIsComplete(node.selectSingleNode("IsComplete").getText());
                }
                if (null != node.selectSingleNode("Name")) {
                    record.setName(node.selectSingleNode("Name").getText());
                }
                if (null != node.selectSingleNode("NumberOfTimesInstalledAttemps")) {
                    record.setNumberOfTimesInstalledAttemps(node.selectSingleNode("NumberOfTimesInstalledAttemps").getText());
                }
                if (null != node.selectSingleNode("NumberOfTimesRemovedByPCRF")) {
                    record.setNumberOfTimesRemovedByPCRF(node.selectSingleNode("NumberOfTimesRemovedByPCRF").getText());
                }
                if (null != node.selectSingleNode("NumberOfTimesFailedOrRemovedByGateway")) {
                    record.setNumberOfTimesFailedOrRemovedByGateway(node.selectSingleNode("NumberOfTimesFailedOrRemovedByGateway").getText());
                }
                if (null != node.selectSingleNode("NumberOfTimesTotalRetryAttempts")) {
                    record.setNumberOfTimesTotalRetryAttempts(node.selectSingleNode("NumberOfTimesTotalRetryAttempts").getText());
                }
                recordList.add(record);
            }
        } catch (OssiException ex) {
            log.info(ex.getMessage())
        } catch (Exception e) {
            e.printStackTrace()
            throw new OssiException("error in parseRuleInstallXml");
        }
        return recordList;
    }

    List<SpecificQuota> parseSpecificQuotaXml(String xmlString) throws OssiException {
        List<SpecificQuota> quotaList = new ArrayList<SpecificQuota>();
        try {
            SAXReader reader = new SAXReader();
            Document document = reader.read(new StringReader(xmlString));
            List<Node> nodes = document.selectNodes("/ConfigurationData/Quota");
            for (Node node : nodes) {
                SpecificQuota quota = new SpecificQuota();
                if (null != node.selectSingleNode("Name")) {
                    quota.setName(node.selectSingleNode("Name").getText());
                }
                if (null != node.selectSingleNode("TotalVolumeLimit")) {
                    quota.setTotalVolumeLimit(node.selectSingleNode("TotalVolumeLimit").getText());
                }
                quotaList.add(quota);
            }
        } catch (Exception e) {
            e.printStackTrace()
            throw new OssiException("error in parseSpecificQuotaXml");
        }
        return quotaList;
    }
}
