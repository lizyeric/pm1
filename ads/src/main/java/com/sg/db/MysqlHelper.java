package com.sg.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

import com.sg.client.MinaTimeClient;
import com.sg.common.MSG_TYPE;

public class MysqlHelper {

	public static int executeUpdate(Connection connection, String sql) {
		int resCount = 0;
		if (StringUtils.isBlank(sql)) {
			System.out.println("sql is null.");
			return resCount;
		}
		PreparedStatement ps = null;
		try {
			ps = connection.prepareStatement(sql);
			resCount = ps.executeUpdate();
			connection.commit();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (null != ps) {
				try {
					ps.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return resCount;

	}

	public static List<String> executeQuery(Connection connection, String sql) {

		List<String> result = new ArrayList<String>();
		ResultSet rs = null;
		PreparedStatement ps = null;
		try {
			ps = connection.prepareStatement(sql);
			rs = ps.executeQuery();
			while (rs.next()) {
				String policy_id = rs.getString(1);
				result.add(policy_id);
			}
		} catch (SQLException e) {
			MinaTimeClient.log.logErr(e);
		} finally {
			if (null != ps) {
				try {
					ps.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					MinaTimeClient.log.logErr(e);
				}
			}
		}
		return result;
	}

	public static int executeBatchPRD(Connection connection, List<List<Map<String, String>>> listAll) {
		int resCount = 0;
		String sql = "INSERT INTO policy_record_data (policy_id, policy_name, ref_version,is_delete,pcrf_id) VALUES";
		String selectSql = "SELECT policy_id FROM policy_record_data";
		StringBuilder sb = new StringBuilder();
		PreparedStatement ps = null;
		try {
			// execute select
			List<String> list = executeQuery(connection, selectSql);
			int count = 0;
			String sqlValue = "('#policy_id#','#policy_name#','#ref_version#','0',#pcrf_id#)";
			if (null != MinaTimeClient.pcrfid && !"".equals(MinaTimeClient.pcrfid)) {
				sqlValue = sqlValue.replace("#pcrf_id#", "'" + MinaTimeClient.pcrfid + "'");
			} else {
				sqlValue = sqlValue.replace("#pcrf_id#", "null");
			}
			for (int i = 0; i < listAll.size(); i++) {
				count++;
				String policy_id = "";
				String policy_name = "";
				String ref_version = "";
				boolean policy_id_flag = false, policy_name_flag = false, ref_version_flag = false;
				for (Map<String, String> map : listAll.get(i)) {
					Set<String> keys = map.keySet();
					Iterator<String> it = keys.iterator();
					while (it.hasNext()) {
						String type = it.next();
						String value = map.get(type);

						switch (type) {
						case MSG_TYPE.Policy_ID:
							sqlValue = sqlValue.replace("#policy_id#", value);
							policy_id = value;
							policy_id_flag = true;
							break;
						case MSG_TYPE.Policy_Name:
							 sqlValue = sqlValue.replace("#policy_name#",
							 value);
							policy_name = value;
							policy_name_flag = true;
							break;
						case MSG_TYPE.Reference_Data_Version:
							 sqlValue = sqlValue.replace("#ref_version#",
							 value);
							ref_version = value;
							ref_version_flag = true;
							break;
						default:

						}
					}
					if (policy_id_flag && policy_name_flag && ref_version_flag) {

						if (null != list && list.size() > 0) {
							// update prd_status to 1
							if (list.contains(policy_id)) {
								String update_policyId = "UPDATE policy_record_data SET is_delete=0,policy_name='"
										+ policy_name + "' WHERE policy_id='" + policy_id + "'";
								// update prd_stastus to 0
								executeUpdate(connection, update_policyId);
							} else {
								String sqlValueTmp = sqlValue.replace("#policy_id#", policy_id)
										.replace("#policy_name#", policy_name).replace("#ref_version#", ref_version);
								sb.append(sqlValueTmp);
								sb.append(",");
							}
						} else {
							String sqlValueTmp = sqlValue.replace("#policy_id#", policy_id)
									.replace("#policy_name#", policy_name).replace("#ref_version#", ref_version);
							sb.append(sqlValueTmp);
							sb.append(",");
						}
						policy_id_flag = false;
						policy_name_flag = false;
						continue;
					}
				}

			}
			String sqlBody = sb.toString();
			if (!"".equals(sqlBody)) {
				ps = connection.prepareStatement("");
				ps.addBatch(sql + sqlBody.substring(0, sqlBody.length() - 1));
				int[] results = ps.executeBatch();
				connection.commit();
			}
		} catch (SQLException e) {
			MinaTimeClient.log.logErr(e);
		} finally {
			if (null != ps) {
				try {
					ps.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return resCount;
	}

	public static int executeBatchPER(Connection connection, List<List<Map<String, String>>> listAll) {
		int resCount = 0;
		String sql = "INSERT INTO policy_event_record (ads_version,bill_day,called_station_id,cell_id,event_trigger,event_type,framed_ip_addr,ip_can_type,location_area_code,mccmnc,mobility_protocol,orig_host,peer_id,policy_end_time,policy_id,policy_start_time,rat_type,ref_version,routing_area_code,serial_number,session_id,sgsn_ipaddr,sgsn_mccmnc,subid,tier,user_equip_info,pcrf_id) VALUES";
		StringBuilder sb = new StringBuilder();
		PreparedStatement ps = null;
		try {
			ps = connection.prepareStatement("");
			int count = 0;
			if (null != listAll && listAll.size() > 0) {

				for (int i = 0; i < listAll.size(); i++) {
					String sqlValue = "(#ads_version#,#bill_day#,#called_station_id#,#cell_id#,#event_trigger#,#event_type#,#framed_ip_addr#,#ip_can_type#,#location_area_code#,#mccmnc#,#mobility_protocol#,#orig_host#,#peer_id#,#policy_end_time#,#policy_id#,#policy_start_time#,#rat_type#,#ref_version#,#routing_area_code#,#serial_number#,#session_id#,#sgsn_ip_addr#,#sgsn_mccmnc#,#sub_id#,#tier#,#user_equip_info#,#pcrf_id#)";
					count++;
					for (Map<String, String> map : listAll.get(i)) {
						Set<String> keys = map.keySet();
						Iterator<String> it = keys.iterator();
						while (it.hasNext()) {
							String type = it.next();
							String value = map.get(type);
							value = "'" + value + "'";
							switch (type) {
							case MSG_TYPE.Version:
								sqlValue = sqlValue.replace("#ads_version#", value);
								break;
							case MSG_TYPE.Policy_ID:
								sqlValue = sqlValue.replace("#policy_id#", value);
								break;
							case MSG_TYPE.Reference_Data_Version:
								sqlValue = sqlValue.replace("#ref_version#", value);
								break;
							case MSG_TYPE.Policy_Entry_Timestamp:
								sqlValue = sqlValue.replace("#policy_start_time#", value);
								break;
							case MSG_TYPE.Policy_Exit_Timestamp:
								sqlValue = sqlValue.replace("#policy_end_time#", value);
								break;
							case MSG_TYPE.Policy_Event_Serial_Number:
								sqlValue = sqlValue.replace("#serial_number#", value);
								break;
							case MSG_TYPE.Session_Id:
								sqlValue = sqlValue.replace("#session_id#", value);
								break;
							case MSG_TYPE.RAT_Type:
								sqlValue = sqlValue.replace("#rat_type#", value);
								break;
							case MSG_TYPE.Event_Type:
								sqlValue = sqlValue.replace("#event_type#", value);
								break;
							case MSG_TYPE.IP_CAN_Type:
								sqlValue = sqlValue.replace("#ip_can_type#", value);
								break;
							case MSG_TYPE.Application_Id:
								sqlValue = sqlValue.replace("#app_id#", value);
								break;
							case MSG_TYPE.Called_Station_Identifier:
								sqlValue = sqlValue.replace("#called_station_id#", value);
								break;
							case MSG_TYPE.Mobility_Protocol:
								sqlValue = sqlValue.replace("#mobility_protocol#", value);
								break;
							case MSG_TYPE.User_Equipment_Information:
								sqlValue = sqlValue.replace("#user_equip_info#", value);
								break;
							case MSG_TYPE.Peer_Identity:
								sqlValue = sqlValue.replace("#peer_id#", value);
								break;
							case MSG_TYPE.Origin_Host:
								sqlValue = sqlValue.replace("#orig_host#", value);
								break;
							case MSG_TYPE.Framed_IP_Address:
								sqlValue = sqlValue.replace("#framed_ip_addr#", value);
								break;
							case MSG_TYPE.SGSN_MCC_MNC:
								sqlValue = sqlValue.replace("#sgsn_mccmnc#", value);
								break;
							case MSG_TYPE.SGSN_IP_Address:
								sqlValue = sqlValue.replace("#sgsn_ip_addr#", value);
								break;
							case MSG_TYPE.Event_triggers_mask:
								sqlValue = sqlValue.replace("#event_trigger#", value);
								break;
							case MSG_TYPE.MCCMNC:
								sqlValue = sqlValue.replace("#mccmnc#", value);
								break;
							case MSG_TYPE.Location_Area_Code:
								sqlValue = sqlValue.replace("#location_area_code#", value);
								break;
							case MSG_TYPE.Cell_Identifier:
								sqlValue = sqlValue.replace("#cell_id#", value);
								break;
							case MSG_TYPE.Subscriber_ID:
								if (value.toUpperCase().contains("E164")) {
									int index = value.indexOf(":");
									if (index > 0)
										sqlValue = sqlValue.replace("#sub_id#", "'" + value.substring(index + 1));
								}
								break;
							case MSG_TYPE.Billing_Day:
								sqlValue = sqlValue.replace("#bill_day#", value);
								break;
							case MSG_TYPE.Routing_Area_Code:
								sqlValue = sqlValue.replace("#routing_area_code#", value);
								break;
							case MSG_TYPE.Tier:
								sqlValue = sqlValue.replace("#tier#", value);
								break;
							default:

							}
						}
					}
					if (null == MinaTimeClient.pcrfid || "".equals(MinaTimeClient.pcrfid)) {
						sqlValue = sqlValue.replace("#pcrf_id#", "null");
					} else {
						sqlValue = sqlValue.replace("#pcrf_id#", "'" + MinaTimeClient.pcrfid + "'");
					}
					sqlValue = sqlValue.replace("#ads_version#", "null").replace("#ref_version#", "null")
							.replace("#policy_start_time#", "null").replace("#policy_end_time#", "null")
							.replaceAll("#policy_id#", "null").replace("#serial_number#", "null")
							.replace("#session_id#", "null").replace("#rat_type#", "null")
							.replace("#event_type#", "null").replace("#ip_can_type#", "null")
							.replace("#app_id#", "null").replace("#called_station_id#", "null")
							.replace("#mobility_protocol#", "null").replace("#user_equip_info#", "null")
							.replace("#peer_id#", "null").replace("#orig_host#", "null")
							.replace("#framed_ip_addr#", "null").replace("#sgsn_mccmnc#", "null")
							.replace("#sgsn_ip_addr#", "null").replace("#event_trigger#", "null")
							.replace("#mccmnc#", "null").replace("#location_area_code#", "null")
							.replace("#cell_id#", "null").replace("#sub_id#", "null").replace("#bill_day#", "null")
							.replace("#routing_area_code#", "null").replace("#tier#", "null");
					sb.append(sqlValue);
					if (count < listAll.size()) {
						sb.append(",");
					}
				}
				if (!"".equals(sb.toString())) {
					ps.addBatch(sql + sb.toString());
					int[] results = ps.executeBatch();
					connection.commit();
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {

			if (null != ps) {
				try {
					ps.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		}
		return resCount;

	}
}
