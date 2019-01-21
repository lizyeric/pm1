package com.sg.pcrf.rcmgr

import com.sg.pcrf.ads.PolicyRecordData
import com.sg.pcrf.exceptions.RcmgrException

//create by jyx
//2017/08/07

import grails.transaction.Transactional
import org.json.JSONArray
import org.json.JSONObject
import org.springframework.core.io.FileSystemResource
import org.springframework.core.io.support.PropertiesLoaderUtils

import java.util.regex.Matcher
import java.util.regex.Pattern

@Transactional
class PolicyHandleService {
    def grailsApplication

    public static final String command = "sudo /opt/camiant/rc/bin/rcmgr \"show policy library -v\"";

    def getConfigProperties() {
        def path = grailsApplication.config.basepath
        def resource = new FileSystemResource("${path}${File.separator}config${File.separator}rcmgr.properties")
        def properties = PropertiesLoaderUtils.loadProperties(resource)
        return properties
    }

    def loadAndSavePolicyData() {
        def properties = getConfigProperties()
        def userName = properties.userName
        def password = properties.password
        def connectionIP = properties.connectionIP
        try {
//            request Policy data via ssh-rcmgr command
            def rawPolicy = requestPolicyData(userName, password, connectionIP)
//            split all results into single policy
//            dev test
//            def policyList = splitRawPolicy("")
            def policyList = splitRawPolicy(rawPolicy)
//            parse data and get a list which contains policy_detail_data objects.
            def pddList = []
            policyList.each { singlePolicy ->
                def pdd = parseSinglePolicy(singlePolicy)
                if (pdd != null) {
                    pddList.add(pdd)
                }
            }
//            save/update policy data and modify isDelete flag
            pddList.each { pdd ->
                def newPdd = PolicyDetailData.findOrCreateByPolicyName(pdd.policyName)
                newPdd.policyId = pdd.policyId
                newPdd.policyCode = pdd.policyCode
                newPdd.serviceCode = pdd.serviceCode
                newPdd.activateReason = pdd.activateReason
                newPdd.policyCondition = pdd.policyCondition
                newPdd.policyAction = pdd.policyAction
                newPdd.isDelete = pdd.isDelete
                newPdd.save()
                if (newPdd.hasErrors()) {
                    throw new RcmgrException("sth error in save or update data:" + newPdd.errors)
                }
            }
            def oldNameSet = PolicyDetailData.findAll()*.policyName.toSet()
            def newNameSet = pddList*.policyName.toSet()
            Set deleteSet = oldNameSet - newNameSet
            deleteSet.each { name ->
                def deletedPolicy = PolicyDetailData.findByPolicyName(name)
                deletedPolicy.isDelete = true
                deletedPolicy.save()
                if (deletedPolicy.hasErrors()) {
                    throw new RcmgrException("sth error in change delete flag:" + deletedPolicy.errors)
                }
            }
        } catch (Exception e) {
            e.printStackTrace()
            throw new RcmgrException("error in loadAndSavePolicyData func")
        }
    }


    def requestPolicyData(String userName, String password, String connectionIP) {
//        may use this: return a hashMap{result_code(0or-1), result_desc:0=>result/-1=>error_data}
//        println("--" * 10 + "request policy data" + "--" * 10)
        SSHManger instance = new SSHManger(userName, password, connectionIP, "");
        String errorMessage = instance.connect();
        if (errorMessage != null) {
//            connect error!
            System.out.println(errorMessage);
            throw new RcmgrException("sth error in ssh connect:" + errorMessage)
        }
        try {
            String result = instance.sendCommand(command);
            instance.close();
//        write to file, dev test.
//            File file = new File("/usr/AshanTest/newPolicyInfo.txt")
//            file.write(result)
//            remove xxx.jar at the first line.
            return result.substring(result.indexOf('\n') + 1)
        } catch (Exception e) {
            e.printStackTrace()
            throw new RcmgrException("sth error in sendCommand:")
        }

    }

    def splitRawPolicy(String rawPolicyData) {
//        split all results into single policy
//        dev test.
//        File file = new File("D:\\workFile\\commandFile\\PolicyInfo.txt")
//        rawPolicyData = file.text
        String tempStr = ""
        List policyList = []
        rawPolicyData.eachLine { line ->
            if (line.trim() == "accept message" || line.trim() == "continue processing message"
                    || line.trim() == "reject message") {
                tempStr += line.trim() + '\n'
                policyList.add(tempStr)
                tempStr = ""
            } else if (!(line.trim() == "")) {
                tempStr += line.trim() + '\n'
            }
            return
        }
//        println(policyList)
        return policyList
    }

    def parseSinglePolicy(String singlePolicy) {
//        println("--" * 10 + "parse single policy" + "--" * 10)
        PolicyDetailData pdd = new PolicyDetailData()
        JSONObject policyCondition = new JSONObject()
        JSONObject policyAction = new JSONObject()
        JSONObject subscriberJson = new JSONObject()
        JSONArray grantArray = new JSONArray()
        singlePolicy.split("\n").eachWithIndex { line, count ->
            if (count == 0) {
//                println("name_line:" + line)
                pdd.policyName = line
            }
            if (line.matches("^And +where the request.*")) {
                policyCondition.put("request",
                        line.substring(line.indexOf("is") + 3))
            }
            if (line.matches("^And +where the enforcement session.*")) {
                policyCondition.put("enforcement session",
                        line.substring(line.indexOf("is") + 3))
            }
            if (line.matches("^And +where the current time.*")) {
                pdd.activateReason += "B"
                policyCondition.put("current time",
                        line.substring(line.indexOf("within the") + 11).split(" ")[0])
            }
            if (line.matches("^And +where the TDF-Application-Identifier.*")) {
                pdd.activateReason += "D"
                policyCondition.put("TDF-Application-Identifier",
                        line.substring(line.indexOf("one of") + 7))
            }
            if (line.matches("^And +where the UserEquipmentInfo.*")) {
                pdd.activateReason += "H" //这个是Terminate type
                policyCondition.put("USER_EQUIPMENT_IDENTITY",
                        line.substring(line.indexOf("List(s)") + 8))
            }
            if (line.matches("^And +where the Eutran_Cell_Identifier.*")) {
                pdd.activateReason += "C" //这个是位置信息
                policyCondition.put("ECI",
                        line.substring(line.indexOf("List(s)") + 8))
            }
            if (line.matches("^And +where the user.*")) {
                def regex = /the user (\d+) (\d+)/
                Matcher matcher = Pattern.compile(regex).matcher(line)
                if (matcher.find()) {
//                    println("user_id:" + matcher.group(1))
                    def userId = matcher.group(1)
//                    println("corresponding_num:" + matcher.group(2))
                    if (userId == "1") {
//                  user1和user2的写入内容虽然会被覆盖，但他们的数据是一定相同的，直接覆盖
////                user 1 => Service
                        policyCondition.put("Service", matcher.group(2))
                        pdd.serviceCode = matcher.group(2)
                    }
                    if (userId == "2") {
//                user 2 => User Session Policy
                        policyCondition.put("User Session Policy", matcher.group(2))
                        pdd.policyCode = matcher.group(2)
                    }
                }

                if (line.find("TerminalType")) {
                    policyCondition.put("TerminalType", line.substring(line.indexOf("true") + 5))
                }

                if (line.find("ServiceUsageState")) {
                    pdd.activateReason += "F"
                    policyCondition.put("ServiceUsageState", line.substring(line.indexOf("true") + 5))
                }
            }
            if (line.matches("And +where the \\d+ state variable.*")) {
                def identifyNum = -1
                def keyWord = ""
                def correspondingNum = ""
                def regex = /the (\d+) state variable (\w+) is numerically == (\d+)/
                Matcher matcher = Pattern.compile(regex).matcher(line)
                if (matcher.find()) {
                    identifyNum = matcher.group(1).toInteger()
                    keyWord = matcher.group(2)
                    correspondingNum = matcher.group(3)
                }
                if (identifyNum == 0) {
//                    state 0 => subscriber
                    if (keyWord == "usrGrade") {
                        pdd.activateReason += "A"
                        subscriberJson.put(keyWord, correspondingNum)
                    }

                    if (keyWord == "usrStatus") {
                        pdd.activateReason += "E"
                        subscriberJson.put(keyWord, correspondingNum)
                    }

//                    按照新文档上的usageState存储，这是旧的
//                    if (keyWord == "servicUsageState") {
//                    }

                    if (keyWord == "pcrfUsageState") {
                        pdd.activateReason += "G"
                        subscriberJson.put(keyWord, correspondingNum)
                    }
                }
            }
//            policy action
            if (line.matches("^install.*")) {
                policyAction.put("install", line.split(" ")[1])
            }
            if (line.matches("^grant.*")) {
                String tempLine
                if (line.matches(".*for BEST OF.*")) {
//                System.out.println("--best of--");
                    tempLine = line.substring(line.indexOf("BEST OF"))
                    if (tempLine.matches(".* using .*")) {
                        tempLine = tempLine.substring(0, tempLine.indexOf(" using"));
                    }
                    tempLine = tempLine.substring(8)
                    tempLine = oactFilter(tempLine)
                    grantArray.put(tempLine)
                } else if (line.matches("^grant session time limit to.*")) {
//                System.out.println("--percent of--");
                    tempLine = line.substring(line.indexOf("percent of"))
                    tempLine = tempLine.substring(11)
                    tempLine = oactFilter(tempLine)
//                System.out.println(tempLine);
                    grantArray.put(tempLine)
                } else {
//                System.out.println("--for--");
                    tempLine = line.substring(line.indexOf("for"))
                    if (tempLine.matches(".* using .*")) {
                        tempLine = tempLine.substring(0, tempLine.indexOf(" using"))
                    }
                    tempLine = tempLine.substring(4)
                    tempLine = oactFilter(tempLine)
                    grantArray.put(tempLine)
                }
            }
        }
        pdd.activateReason = pdd.activateReason.toList().sort().join("")
        policyCondition.put("subscriber", subscriberJson)
        policyAction.put("grant", grantArray)
        pdd.policyCondition = policyCondition.toString()
        if(!policyAction.keySet().contains("install")){
            policyAction.put('install', "")
        }
        pdd.policyAction = policyAction.toString()
        try {
            def prd = PolicyRecordData.findByPolicyName(pdd.policyName)
            pdd.policyId = prd.policyId
        } catch (Exception ignored) { // null id
//            println("null id occured!")
            log.info("the following is the policy which isn't in the PRD table:\n" + pdd.toString())
            pdd = null
        }
        return pdd
    }

    def oactFilter(String templine) {
//        remove the OACT.
        String regex = ".*(OACT\\w*)";
        Matcher matcher = Pattern.compile(regex).matcher(templine);
        if (matcher.find()) {
            String removeOact = matcher.group(1) + " ";
            return templine.replace(removeOact, "");
        }
        return templine;
    }
}