package com.sg.pcrf.soap

import com.sg.pcrf.statistic.client.Client
import com.sg.pcrf.statistic.db.Services
import com.sg.pcrf.statistic.file.StatisticFile
import com.sg.pcrf.statistic.sftp.UploadStatisticFile
import com.sg.pcrf.statistic.util.Constants
import com.sg.pcrf.statistic.util.Utils
import grails.transaction.Transactional
import groovy.sql.Sql

import java.text.SimpleDateFormat

@Transactional
class PolicyStatisticService {
    def dataSource
    def grailsApplication
    def exectue(String cron) {
/*
		 * 1. Search data from DB. 2. Write Statistic files. 3. Put result files
		 * to FTP server. 4. Sent soap request to OMP if put files failed.
		 */
        try {
            String[] c = cron.split(" ");
            StatisticFile sFile = new StatisticFile();
            SimpleDateFormat sfSecond = new SimpleDateFormat("yyyyMMddHHmmss");
            SimpleDateFormat sfdb = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Calendar startTime = Calendar.getInstance();
            Date nowDate = new Date();
            int currentDay = startTime.get(Calendar.DAY_OF_MONTH);
            int currentMonth = startTime.get(Calendar.MONTH);
            int currentMinute = startTime.get(Calendar.MINUTE)
            if (!"*".equals(c[2])) {
                startTime.set(Calendar.HOUR, Integer.valueOf(c[2]));
            }
            if (!"*".equals(c[1])) {
                String[] min = c[1].split("/")
                startTime.set(Calendar.MINUTE, currentMinute - Integer.valueOf(min[1]));
            }

            startTime.set(Calendar.SECOND, Integer.valueOf(c[0]));
            startTime.set(Calendar.MILLISECOND, 0);
            // day
            if (!"*".equals(c[3]) && !"?".equals(c[3])) {
                startTime.set(Calendar.MONTH, currentMonth - 1);
            }
            // weekly
            if (!"*".equals(c[5]) && !"?".equals(c[5])) {
                startTime.set(Calendar.DAY_OF_MONTH, currentDay - 7);
            }
            Date date = startTime.getTime();

            String start = sfdb.format(date);
            String end = sfdb.format(nowDate);
            sFile.createFile(sfSecond.format(nowDate));

            def sql = new Sql(dataSource)
            List<List<String>> mappingList = Services.searchPolicyRuleMapping(sql, sFile);
            println(mappingList)
            if (null != mappingList && !mappingList.isEmpty()) {
                for (int i = 0; i < mappingList.size(); i++) {
                    List<String> list = mappingList.get(i);
                    // Execute policy rule mapping
                    Services.executeMapping(list, sFile);
                    // Execute policy statistic
                    Services.executePolicy(list, sql, sFile, start, end);
                    // Execute rule statistic
                    Services.executeRule(list, sql, sFile, start, end);
                }
            }
        } catch (Exception e) {
            e.printStackTrace()
        }

        /*
             * 3. Put files to remote server via sftp
             */
        Properties properties = Utils.loadProperties(grailsApplication.config.configPath+"pcc.properties");
        UploadStatisticFile uploadFile = new UploadStatisticFile(properties);
        List<String> list = uploadFile.uploadFile();
        /*
         * 4. if put files failed, send soap request to OMP
         */
        StringBuilder attributes = null;
        if (null != list && list.size() > 0) {
            attributes = new StringBuilder();
            for (String fileName : list) {
                attributes.append("<attribute>");
                if (fileName.startsWith(Constants.policy_rule_mappingFile)) {
                    attributes.append("<key>");
                    attributes.append(Constants.MappingTable);
                    attributes.append("</key>");
                    attributes.append("<value>");
                    attributes.append(fileName);
                    attributes.append("</value>");
                } else if (fileName.startsWith(Constants.policy_statisticFile)) {
                    attributes.append("<key>");
                    attributes.append(Constants.PolicyStatisticsTable);
                    attributes.append("</key>");
                    attributes.append("<value>");
                    attributes.append(fileName);
                    attributes.append("</value>");
                } else if (fileName.startsWith(Constants.rule_statisticFile)) {
                    attributes.append("<key>");
                    attributes.append(Constants.RuleStatisticsTable);
                    attributes.append("</key>");
                    attributes.append("<value>");
                    attributes.append(fileName);
                    attributes.append("</value>");
                }
                attributes.append("</attribute>");
            }
            // sent soap xml
            Client.sentMsg(Constants.SOAP_REQ.replace("#ATTRIBUTE#", attributes.toString()));

        }


    }
}
