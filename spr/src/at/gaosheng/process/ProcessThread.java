package at.gaosheng.process;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import at.gaosheng.util.DBUtil;
import at.gaosheng.util.Utils;

public class ProcessThread extends Thread {

	private Properties prop;

	public ProcessThread(Properties prop) {
		this.prop = prop;
	}

	@Override
	public void run() {

		Utils.printlnInOut("ProcessThread", "Process Start.");
		int timeOut = 0;
		while (true) {
			boolean processFlag = false;
			File folder = new File(prop.getProperty("DOWNLOAD_DIR").trim());
			if (null != folder && folder.listFiles().length > 0) {
				File[] files = folder.listFiles();
				for (File file : files) {
					if (file.isFile() && file.getName().endsWith("exml")) {
						if (checkFileSize(file)) {
							processFlag = true;
						}
					}
				}
			}
			if (!processFlag) {

				if ((Float.valueOf(timeOut) / 3600) > (Integer
						.valueOf(prop.getProperty("CHECK_FILE_TIMEOUT").trim()))) {
					Utils.printlnInOut("ProcessThread", "Check file Timeout, ProcessThread close success.");
					break;
				}
				try {
					Utils.printlnInOut("ProcessThread", "Local file don't exist.");
					sleep(Integer.valueOf(prop.getProperty("CHECK_FILE_SLEEP").trim()) * 1000);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				timeOut += Integer.valueOf(prop.getProperty("CHECK_FILE_SLEEP").trim());
				continue;

			}
			Connection connection = DBUtil.getConnection(prop);
			PreparedStatement insertSubscriberpst = null;
			PreparedStatement insertServicepst = null;
			PreparedStatement insertPolicypst = null;
			PreparedStatement insertLocationpst = null;
			PreparedStatement insertQuotapst = null;
			try {
				connection.setAutoCommit(false);
				insertSubscriberpst = connection.prepareStatement("");
				insertServicepst = connection.prepareStatement("");
				insertPolicypst = connection.prepareStatement("");
				insertLocationpst = connection.prepareStatement("");
				insertQuotapst = connection.prepareStatement("");
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				Utils.printlnInOut("ProcessThread", "Can not connect MySQL server." + e1.getMessage());
				break;
			}
			File[] files = folder.listFiles();
			for (File file : files) {
				if (file.isFile()) {
					long start = System.currentTimeMillis();
					FileReader reader;
					BufferedReader br;
					Utils.printlnInOut("ProcessThread",
							"File Name." + prop.getProperty("DOWNLOAD_DIR").trim() + Utils.sf + file.getName());
					try {
						reader = new FileReader(prop.getProperty("DOWNLOAD_DIR").trim() + Utils.sf + file.getName());

						int fileLineCount = getFileLineCount(
								prop.getProperty("DOWNLOAD_DIR").trim() + Utils.sf + file.getName());
						br = new BufferedReader(reader);
						String str = null;

						int lineIndex = 0;
						String insertCount = prop.getProperty("INSERT_COUNT").trim();
						int count = 5000;
						if (null != insertCount && !"".equals(insertCount)) {
							count = Integer.parseInt(insertCount);
						}

						StringBuilder subscribersb = new StringBuilder();
						StringBuilder servicesb = new StringBuilder();
						StringBuilder policysb = new StringBuilder();
						StringBuilder locationsb = new StringBuilder();
						StringBuilder quotasb = new StringBuilder();
						while ((str = br.readLine()) != null) {
							if (str.startsWith("<subscriberRecord>")) {
								// System.out.println(rootStr);
								lineIndex++;
								Document document = DocumentHelper.parseText(str);
								Element root = document.getRootElement();
								Element subElement = root.element("subscriber");
								Iterator<Element> fieldEles = subElement.elementIterator();
								String IMSI = null;
								String MSISDN = null;
								String billingDay = null;
								String usrBillingType = null;
								String usrNotifyMSISDN = null;
								String operateTime = null;
								String packageType = null;
								while (fieldEles.hasNext()) {
									Element ele = fieldEles.next();
									Attribute attribute = ele.attribute("name");
									String name = attribute.getText();
									String eleText = ele.getText();
									if ("".equals(eleText)) {
										eleText = null;
									}
									switch (name) {
									case "IMSI": {
										IMSI = eleText;
										break;
									}
									case "MSISDN": {
										MSISDN = eleText;
										break;
									}
									case "BillingDay": {
										billingDay = eleText;
										break;
									}
									case "usrBillingType": {
										usrBillingType = eleText;
										break;
									}
									case "usrNotifyMSISDN": {
										usrNotifyMSISDN = eleText;
										break;
									}
									case "operateTime": {
										operateTime = eleText;
										break;
									}
									case "PackageType": {
										packageType = eleText;
										break;
									}
									}
								}

								// state
								Element stateElement = root.element("state");
								String usrStatus = null;
								String lastDeliveryTime = null;
								String usrNextResetTime = null;
								if (null != stateElement) {
									String stateVersion = stateElement.elementTextTrim("version");
									Iterator<Element> propertyElements = stateElement.elementIterator("property");
									while (propertyElements.hasNext()) {
										Element propertyElement = propertyElements.next();
										String name = propertyElement.elementTextTrim("name");
										String value = propertyElement.elementTextTrim("value");
										switch (name) {
										case "usrStatus": {
											usrStatus = value;
											break;
										}

										case "usrNextResetTime": {
											usrNextResetTime = value;
											break;
										}
										}
									}
								}

								if ((lineIndex == 0 || lineIndex % count != 0) && fileLineCount != lineIndex) {
									subscribersb.append("(" + MSISDN + "," + IMSI + "," + billingDay + ","
											+ usrBillingType + "," + "DATE_FORMAT(" + operateTime
											+ ",'%Y-%m-%d %h:%i:%s')" + "," + packageType + "," + usrStatus + ","
											+ "DATE_FORMAT(" + usrNextResetTime + ",'%Y-%m-%d %h:%i:%s')" + "),\r\n");
								} else {
									String insertSubscriberSQL = "INSERT INTO subscriber_data (msisdn, imsi, billing_day, usr_billing_type, operate_time, package_type, usr_status, usr_next_reset_time) VALUES ";
									subscribersb.append("(" + MSISDN + "," + IMSI + "," + billingDay + ","
											+ usrBillingType + "," + "DATE_FORMAT(" + operateTime
											+ ",'%Y-%m-%d %h:%i:%s')" + "," + packageType + "," + usrStatus + ","
											+ "DATE_FORMAT(" + usrNextResetTime + ",'%Y-%m-%d %h:%i:%s')" + ")");
									String sql = insertSubscriberSQL + subscribersb.toString();
									try {
										insertSubscriberpst.addBatch(sql);
										insertSubscriberpst.executeBatch();
										connection.commit();
									} catch (SQLException e) {
										// TODO Auto-generated catch block
										Utils.printlnInOut("ProcessThread", "193" + e.getMessage());
									}

									subscribersb = new StringBuilder();
								}

								// services
								Element servicesElement = root.element("services");
								if (null != servicesElement) {
									String servicesVersion = servicesElement.elementTextTrim("version");
									List nodes = servicesElement.elements("service");
									Iterator<Element> serviceElements = nodes.iterator();
									while (serviceElements.hasNext()) {
										Element serviceElement = serviceElements.next();
										Attribute serviceAttribute = serviceElement.attribute("ServiceCode");
										String serviceCode = serviceAttribute.getText();
										String serviceBillingType = serviceElement
												.elementTextTrim("ServiceBillingType");
										String serviceStartDateTime = serviceElement
												.elementTextTrim("ServiceStartDateTime");
										String serviceEndDateTime = serviceElement
												.elementTextTrim("ServiceEndDateTime");
										String serviceUsageState = serviceElement.elementTextTrim("ServiceUsageState");
										String serviceOperateTime = serviceElement.elementTextTrim("operateTime");

										servicesb.append("(" + IMSI + "," + MSISDN + "," + serviceCode + ","
												+ serviceBillingType + "," + "DATE_FORMAT(" + serviceStartDateTime
												+ ",'%Y-%m-%d %h:%i:%s')" + "," + "DATE_FORMAT(" + serviceEndDateTime
												+ ",'%Y-%m-%d %h:%i:%s')" + "," + "DATE_FORMAT(" + serviceOperateTime
												+ ",'%Y-%m-%d %h:%i:%s')" + "," + serviceUsageState + "),\r\n");
									}
								}

								// insert SubscriberServiceData
								if ((lineIndex != 0 && lineIndex % count == 0) || fileLineCount == lineIndex) {
									String insertServicesSQL = "INSERT INTO subscriber_service_data (imsi, msisdn,service_code,billing_type,start_date,end_date,operate_time,usage_state) VALUES ";
									if (null != servicesb && servicesb.length() > 3) {
										servicesb.delete(servicesb.length() - 3, servicesb.length() - 1);
									}
									String sql = insertServicesSQL + servicesb.toString();
									try {
										insertServicepst.addBatch(sql);
										insertServicepst.executeBatch();
										connection.commit();
									} catch (SQLException e) {
										// TODO Auto-generated catch block
										Utils.printlnInOut("ProcessThread", "233" + e.getMessage());
									}

									servicesb = new StringBuilder();
								}

								// policies
								Element policiesElement = root.element("policies");
								if (null != policiesElement) {
									String policiesVersion = policiesElement.elementTextTrim("version");
									Iterator<Element> policyElements = policiesElement.elementIterator("policy");
									while (policyElements.hasNext()) {
										Element policyElement = policyElements.next();
										Attribute policyAttribute = policyElement.attribute("PolicyCode");
										String policyCode = policyAttribute.getText();
										String notificationCycle = policyElement.elementTextTrim("NotificationCycle");
										String terminalType = policyElement.elementTextTrim("TerminalType");
										String sessionPolicyStartDateTime = policyElement
												.elementTextTrim("SessionPolicyStartDateTime");
										String sessionPolicyEndDateTime = policyElement
												.elementTextTrim("SessionPolicyEndDateTime");
										String policyOperateTime = policyElement.elementTextTrim("operateTime");

										policysb.append("(" + IMSI + "," + MSISDN + "," + policyCode + ","
												+ notificationCycle + "," + terminalType + "," + "DATE_FORMAT("
												+ sessionPolicyStartDateTime + ",'%Y-%m-%d %h:%i:%s')" + ","
												+ "DATE_FORMAT(" + sessionPolicyEndDateTime + ",'%Y-%m-%d %h:%i:%s')"
												+ "," + "DATE_FORMAT(" + policyOperateTime
												+ ",'%Y-%m-%d %h:%i:%s')),\r\n");
									}
								}

								// insert SubscriberSessionPolicyInfo
								if ((lineIndex != 0 && lineIndex % count == 0) || fileLineCount == lineIndex) {
									String insertPoliciesSQL = "INSERT INTO subscriber_session_policy_info (imsi, msisdn,policy_code,notification_cycle,terminate_type,start_time,end_time,operate_time) VALUES ";
									if (null != policysb && policysb.length() > 3) {
										policysb.delete(policysb.length() - 3, policysb.length() - 1);
									}
									String sql = insertPoliciesSQL + policysb.toString();
									try {
										insertPolicypst.addBatch(sql);
										insertPolicypst.executeBatch();
										connection.commit();
									} catch (SQLException e) {
										// TODO Auto-generated catch block
										Utils.printlnInOut("ProcessThread", "273" + e.getMessage());
									}
									policysb = new StringBuilder();
								}

								// locations
								Element locationsElement = root.element("locations");
								if (null != locationsElement) {
									String locationsVersion = locationsElement.elementTextTrim("version");
									Iterator<Element> locationElements = locationsElement.elementIterator("location");
									while (locationElements.hasNext()) {
										Element locationElement = locationElements.next();
										Attribute locationAttribute = locationElement.attribute("Code");
										String code = locationAttribute.getText();
										String codeType = locationElement.elementTextTrim("codeType");
										String usrLocation = locationElement.elementTextTrim("UsrLocation");
										String locationOperateTime = locationElement.elementTextTrim("operateTime");
										locationsb.append("(" + IMSI + "," + MSISDN + "," + code + "," + codeType + ","
												+ usrLocation + "," + "DATE_FORMAT(" + locationOperateTime
												+ ",'%Y-%m-%d %h:%i:%s')),\r\n");
									}
								}

								// insert SubscriberLocationInfo
								if ((lineIndex != 0 && lineIndex % count == 0) || fileLineCount == lineIndex) {
									String insertLocationsSQL = "INSERT INTO subscriber_location_info (imsi, msisdn, code, code_type, usr_location, operate_time) VALUES ";
									if (null != locationsb && locationsb.length() > 3) {
										locationsb.delete(locationsb.length() - 3, locationsb.length() - 1);
									}
									String sql = insertLocationsSQL + locationsb.toString();
									try {
										insertLocationpst.addBatch(sql);
										insertLocationpst.executeBatch();
										connection.commit();
									} catch (SQLException e) {
										// TODO Auto-generated catch block
										Utils.printlnInOut("ProcessThread", "308" + e.getMessage());
									}
									locationsb = new StringBuilder();
								}

								// usage
								Element usageElement = root.element("usage");
								if (null != usageElement) {
									String usageVersion = usageElement.elementTextTrim("version");
									Iterator<Element> quotaElements = usageElement.elementIterator("quota");
									while (quotaElements.hasNext()) {
										Element quotaElement = quotaElements.next();
										Attribute quotaAttribute = quotaElement.attribute("name");
										String name = quotaAttribute.getText();
										String cid = quotaElement.elementTextTrim("cid");
										String type = quotaElement.elementTextTrim("Type");
										String nextResetTime = quotaElement.elementTextTrim("nextResetTime")
												.replaceAll("-", "").replaceAll("T", "").replaceAll(":", "");
										String totalVolume = quotaElement.elementTextTrim("totalVolume");

										quotasb.append("(" + IMSI + "," + MSISDN + ",'" + name + "'," + totalVolume
												+ "," + "DATE_FORMAT(" + nextResetTime + ",'%Y-%m-%d %h:%i:%s')"
												+ "),\r\n");
									}
								}

								// insert SubscriberQuotaData
								if ((lineIndex != 0 && lineIndex % count == 0) || fileLineCount == lineIndex) {
									String insertQuotaSQL = "INSERT INTO subscriber_quota_data (imsi, msisdn, quota_name, total_volume, next_reset_time) VALUES ";
									if (null != quotasb && quotasb.length() > 3) {
										quotasb.delete(quotasb.length() - 3, quotasb.length() - 1);
									}
									String sql = insertQuotaSQL + quotasb.toString();
									try {
										insertQuotapst.addBatch(sql);
										insertQuotapst.executeBatch();
										connection.commit();
									} catch (SQLException e) {
										// TODO Auto-generated catch block
										Utils.printlnInOut("ProcessThread", "345" + e.getMessage());
									}
									quotasb = new StringBuilder();
								}
							}
						}

						br.close();
						reader.close();

						long end = System.currentTimeMillis();
						Utils.printlnInOut("ProcessThread", "Process file execute time:" + (end - start));
					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						Utils.printlnInOut("ProcessThread", "FileNotFoundException." + e.getMessage());
					} catch (IOException e) {
						// TODO Auto-generated catch block
						Utils.printlnInOut("ProcessThread", "IOException." + e.getMessage());
					} catch (DocumentException e) {
						// TODO Auto-generated catch block
						Utils.printlnInOut("ProcessThread", "DocumentException." + e.getMessage());
					}
					file.delete();
				}
			}
			try {
				insertSubscriberpst.close();
				insertServicepst.close();
				insertPolicypst.close();
				insertLocationpst.close();
				insertQuotapst.close();
				connection.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				Utils.printlnInOut("ProcessThread", "383" + e.getMessage());
			}

			break;

		}

		Utils.printlnInOut("ProcessThread", "Process File End.");
		System.exit(0);
	}

	private Connection mysqlConnection(Properties prop) {
		String driver = prop.getProperty("MYSQL_DRIVER").trim();
		String url = prop.getProperty("MYSQL_URL").trim();
		String username = prop.getProperty("MYSQL_USERNAME").trim();
		String password = prop.getProperty("MYSQL_PASSWORD").trim();
		Connection connection = null;
		try {
			Class.forName(driver);// 注册驱动
			connection = DriverManager.getConnection(url, username, password);// 定义连接
		} catch (Exception e) {
			// TODO: handle exception
			Utils.printlnInOut("ProcessThread", "405" + e.getMessage());
		}

		return connection;
	}

	public static int getFileLineCount(String filename) {
		int cnt = 0;
		LineNumberReader reader = null;
		try {
			reader = new LineNumberReader(new FileReader(filename));
			@SuppressWarnings("unused")
			String lineRead = "";
			cnt = reader.getLineNumber();
			while ((lineRead = reader.readLine()) != null) {
				// System.out.println(reader.getLineNumber());
			}
			cnt = reader.getLineNumber();
		} catch (Exception ex) {
			cnt = -1;
			ex.printStackTrace();
			Utils.printlnInOut("ProcessThread", "425" + ex.getMessage());
		} finally {
			try {
				reader.close();
			} catch (Exception ex) {
				Utils.printlnInOut("ProcessThread", "431" + ex.getMessage());
			}
		}
		return cnt;
	}

	public static boolean checkFileSize(File file) {
		// TODO Auto-generated method stub
		try {
			if (file.exists() && file.isFile()) {
				long sizeStart = file.length();
				sleep(10000);
				long sizeCheck = file.length();
				if (sizeStart != sizeCheck) {
					checkFileSize(file);
				}
			} else {
				sleep(10000);
				checkFileSize(file);
			}
			return true;
		} catch (Exception e) {
			return false;
		}
	}

}
