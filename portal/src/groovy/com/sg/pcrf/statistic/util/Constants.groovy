package com.sg.pcrf.statistic.util;

public class Constants {
	public static final Object MappingTable = "MappingTable";
	public static final Object PolicyStatisticsTable = "PolicyStatisticsTable";
	public static final Object RuleStatisticsTable = "RuleStatisticsTable";
	public static final String separator = System.getProperty("file.separator");
	public static final String statisticFilePath = System.getProperty("user.dir") + separator + "statistic" + separator;
	public static final String fileType = "csv";
	public static final String policy_rule_mappingFile = "Policy&Rule_MapingTable";
	public static final String policy_statisticFile = "Policy_StatisticsTable";
	public static final String rule_statisticFile = "Rule_StatisticsTable";
	public static String pccCfg = System.getProperty("user.dir") + separator + "pcc.cfg";
	public static String localDir = System.getProperty("user.dir") + separator + "statistic";
	public static String localBackupDir = System.getProperty("user.dir") + separator + "backup";
	public static final String IP = "IP";
	public static final String PORT = "PORT";
	public static final String USERNAME = "USERNAME";
	public static final String PASSWORD = "PASSWORD";
	public static final String PATH = "PATH";
	public static final String MAX_CON_ATTEMPT = "MAX_CON_ATTEMPT";
	public static final String MAX_UPLOAD_ATTEMPT = "MAX_UPLOAD_ATTEMPT";
	public static final String INTERVAL = "INTERVAL";
	public static final String MONTH = "MONTH";
	public static final String WEEKLY = "WEEKLY";
	public static final String DAY = "DAY";
	public static final String HOUR = "HOUR";
	public static final String MINUTE = "MINUTE";
	public static final String SECOND = "SECOND";
	
	public static String SOAP_REQ = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:pop=\"POP:soap\"><soapenv:Header/><soapenv:Body><pop:StatisticLogUploadFailed><inPara>#ATTRIBUTE#</inPara></pop:StatisticLogUploadFailed></soapenv:Body></soapenv:Envelope>";
}
