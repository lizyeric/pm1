package com.sg.pcrf.statistic.db

import com.sg.pcrf.statistic.file.StatisticFile
import groovy.sql.Sql
import net.sf.json.JSONObject

public class Services {

    public static List<List<String>> searchPolicyRuleMapping(Sql sql, StatisticFile sFile) {
        String search = "SELECT pdd.policy_id,pdd.policy_name,pdd.policy_action,pdd.policy_condition,pdd.policy_code,pdd.service_code FROM policy_detail_data pdd WHERE pdd.is_delete=0 AND pdd.policy_code!=0;";
        // Search data from DB
        List<List<String>> mappingList = MysqlHelper.executeQueryMapping(sql, search);
        return mappingList;
    }

    public static void executeMapping(List<String> list, StatisticFile sFile) {
        // TODO Auto-generated method stub
        String policyId = list.get(0);
        String policyName = list.get(1);
        String policy_action = list.get(2);
        String policy_condition = list.get(3);
        String policy_code = list.get(4);
        String rules = "";
        if (null != policy_action && !"".equals(policy_action)) {
            JSONObject json = JSONObject.fromObject(policy_action);
            if (json.containsKey("install")) {
                rules = json.getString("install");
            }
        }
        String line = policy_code + "," + rules + "\r\n";
        sFile.writeMappingFile(line);
    }

    public static void executePolicy(List<String> list, Sql sql, StatisticFile sFile, String startTime,
                                     String endTime) {
        // TODO Auto-generated method stub
        // PolicyID
        String policy_id = list.get(0);
        String policy_name = list.get(1);
        String policy_action = list.get(2);
        String policy_condition = list.get(3);
        String policy_code = list.get(4);
        StringBuilder sb = new StringBuilder();
        sb.append(policy_code).append(",");

        // NSubscriber
        int NSubscriber = 0;
        if (!"".equals(policy_code)) {
            NSubscriber = searchNSubscriber(sql, policy_code, startTime, endTime);
            sb.append(NSubscriber).append(",");
        }

        // NUsrExausted
        int NUsrExausted = 0;
        if (!"".equals(policy_code)) {
//            NUsrExausted = searchNUsrExausted(sql, policy_code);
            sb.append(NUsrExausted).append(",");
        }
        // NUserRecharge
        int NUserRecharge = 0;
        if (!"".equals(policy_code)) {
//            NUserRecharge = searchNUserRecharge(sql, policy_code);
            sb.append(NUserRecharge).append(",");
        }
        // NServiceExausted
        int NServiceExausted = 0;
        if (!"".equals(policy_code)) {
//            NServiceExausted = searchNServiceExausted(sql, policy_code);
            sb.append(NServiceExausted).append(",");
        }
        // NServiceRecharge
        int NServiceRecharge = 0;
        if (!"".equals(policy_code)) {
//            NServiceRecharge = searchNServiceRecharge(sql, policy_code);
            sb.append(NServiceRecharge).append(",");
        }

        // TPolicy
        int TPolicy = searchTPolicy(sql, policy_id, startTime, endTime);
        sb.append(TPolicy).append(",");
        // TMessage
        int TMessage = searchTMessage(sql, policy_name, startTime, endTime);
        sb.append(TMessage).append(",");
        // NMessage
        int NMessage = searchNMessage(sql, policy_name, startTime, endTime);
        sb.append(NMessage).append(",");
        // NPolicy
        int NPolicy = searchNPolicy(sql, policy_name, startTime, endTime);
        sb.append(NPolicy);
        sb.append("\r\n");
        sFile.writePolicyFile(sb.toString());
    }

    public static void executeRule(List<String> list, Sql sql, StatisticFile sFile, String startTime,
                                   String endTime) {
        String policy_id = list.get(0);
        String policy_name = list.get(1);
        String policy_action = list.get(2);
        String policy_condition = list.get(3);

        if (null != policy_action && !"".equals(policy_action)) {
            JSONObject json = JSONObject.fromObject(policy_action);
            if (json.containsKey("install")) {
                String install = json.getString("install");
                String[] rules = install.split(",");
                if (rules.length > 0) {
                    StringBuilder sb = new StringBuilder();
                    int TPolicy = searchTPolicy(sql, policy_id, startTime, endTime);
                    int NPolicy = searchNPolicy(sql, policy_name, startTime, endTime);
                    for (int i = 0; i < rules.length; i++) {
                        String rule = rules[i];
                        sb.append(rule).append(",").append(TPolicy).append(",").append(NPolicy).append("\r\n");
                    }
                    if (!"".equals(sb.toString())) {
                        sFile.writeRuleFile(sb.toString());
                    }
                }

            }
        }

    }

    private static int searchNPolicy(Sql sql, String policy_id, String startTime, String endTime) {
        // TODO Auto-generated method stub
        String search = "SELECT COUNT(DISTINCT per.subid) AS c FROM policy_event_record per WHERE per.policy_id ='" + policy_id + "' AND STR_TO_DATE('" + startTime + "','%Y-%m-%d %H:%i:%s')>=policy_start_time AND STR_TO_DATE('" + endTime + "','%Y-%m-%d %H:%i:%s')<=policy_end_time;";
        int count = MysqlHelper.searchCount(sql, search);
        return count;
    }

    private static int searchNMessage(Sql sql, String policy_name, String startTime, String endTime) {
        String search = "SELECT COUNT(DISTINCT unr.subscriber_id) AS c FROM user_notification_record unr WHERE unr.notify_policy_name='" + policy_name + "' AND STR_TO_DATE('" + startTime + "','%Y-%m-%d %H:%i:%s')>=unr.notify_date AND STR_TO_DATE('" + endTime + "','%Y-%m-%d %H:%i:%s')<=unr.notify_date;";
        int count = MysqlHelper.searchCount(sql, search);
        return count;
    }

    private static int searchTMessage(Sql sql, String policy_name, String startTime, String endTime) {
        // TODO Auto-generated method stub
        String search = "SELECT COUNT(*) AS c FROM user_notification_record unr WHERE unr.notify_policy_name='" + policy_name + "' AND STR_TO_DATE('" + startTime + "','%Y-%m-%d %H:%i:%s')>=unr.notify_date AND STR_TO_DATE('" + endTime + "','%Y-%m-%d %H:%i:%s')<=unr.notify_date;";
        int count = MysqlHelper.searchCount(sql, search);
        return count;
    }

    private static int searchTPolicy(Sql sql, String policy_id, String startTime, String endTime) {
        // TODO Auto-generated method stub

        String search = "SELECT COUNT(*) AS c FROM policy_event_record WHERE policy_id ='" + policy_id + "' AND STR_TO_DATE('" + startTime + "','%Y-%m-%d %H:%i:%s')>=policy_start_time AND STR_TO_DATE('" + endTime + "','%Y-%m-%d %H:%i:%s')<=policy_end_time;";
        int count = MysqlHelper.searchCount(sql, search);
        return count;
    }

    private static int searchNServiceRecharge(Sql sql, String policy_code) {
        // TODO Auto-generated method stub
        String search = "SELECT COUNT(*) AS c FROM subscriber_data sd JOIN subscriber_session_policy_info sspi ON sd.msisdn=sspi.msisdn AND sspi.policy_code='"
        +policy_code
        +"' AND sd.billing_day!=0 JOIN subscriber_service_data ssd ON sd.msisdn=ssd.msisdn AND DAY(ssd.operate_time)!=sd.billing_day AND ssd.usage_state=1 AND ssd.service_code IS NOT NULL;";
        int count = MysqlHelper.searchCount(sql, search);
        return count;

    }

    private static int searchNServiceExausted(Sql sql, String policy_code) {
        // TODO Auto-generated method stub
        String search = "SELECT COUNT(*) AS c FROM subscriber_data sd JOIN subscriber_session_policy_info sspi ON sd.msisdn=sspi.msisdn AND sspi.policy_code='"
        +policy_code
        +"' JOIN subscriber_service_data ssd ON sd.msisdn=ssd.msisdn AND ssd.usage_state=2 AND ssd.service_code IS NOT NULL;";
        int count = MysqlHelper.searchCount(sql, search);
        return count;
    }

    private static int searchNUserRecharge(Sql sql, String policy_code) {
        // TODO Auto-generated method stub
        String search = "SELECT COUNT(sd.msisdn) AS c FROM subscriber_data sd JOIN subscriber_session_policy_info sspi ON sd.msisdn=sspi.msisdn AND sd.usr_status='1' AND sspi.policy_code='"
        +policy_code
        +"' AND sd.billing_day!=0 AND DAY(sd.operate_time)!=sd.billing_day JOIN subscriber_service_data ssd ON sd.msisdn=ssd.msisdn AND ssd.service_code IS NOT NULL;";
        // Search data from DB
        int count = MysqlHelper.searchCount(sql, search);
        return count;
    }

    private static int searchNUsrExausted(Sql sql, String policy_code) {
        // TODO Auto-generated method stub
        String search = "SELECT * FROM subscriber_data sd JOIN subscriber_session_policy_info sspi ON sd.msisdn=sspi.msisdn AND sd.usr_status='2' AND sspi.policy_code='"
        +policy_code
        +"' JOIN subscriber_service_data ssd ON sd.msisdn=ssd.msisdn AND ssd.service_code IS NOT NULL;";
        // Search data from DB
        int count = MysqlHelper.searchCount(sql, search);
        return count;
    }

    private static int searchNSubscriber(Sql sql, String policy_code, String startTime, String endTime) {
        // TODO Auto-generated method stub
        String search = "SELECT COUNT(policy_code)FROM subscriber_session_policy_info WHERE policy_code = '" + policy_code
        +"' AND STR_TO_DATE('" + startTime + "','%Y-%m-%d %H:%i:%s')>=start_time;"
        +" AND STR_TO_DATE('" + endTime
        +"','%Y-%m-%d %H:%i:%s')<=end_time;";
        int count = MysqlHelper.searchCount(sql, search.toString());
        return count;
    }
}
