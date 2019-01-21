package com.sg.common;

import java.util.HashMap;
import java.util.Map;

public class MSG_TYPE {

	public static final String STRING = "String";
	public static final String LONG = "Long";
	public static final String BYTE = "byte";
	public static final String SHORT = "Short";
	public static final String INTEGER = "Integer";
	public static final String GROUP = "group";

	public static final String Keep_Alive = "0003";
	public static final String Error = "0004";
	public static final String Version_Request = "0005";
	public static final String Version_Acknowledgement = "0006";
	public static final String Initiate_Request = "0007";
	public static final String Stop_Request = "0008";
	public static final String Stop_Acknowledgement = "0009";
	public static final String Reference_Data_Version_Request = "000A";
	public static final String Policy_Event_Record = "000B";
	public static final String Policy_Reference_Data = "000C";

	public static final String Version = "1";
	public static final String Event_Type = "2";
	public static final String Reference_Data_Version = "3";
	public static final String Policy_Entry_Timestamp = "4";
	public static final String Policy_Exit_Timestamp = "5";
	public static final String Policy_Event_Serial_Number = "6";
	public static final String Error_Message = "9";
	public static final String Error_Code = "10";
	public static final String Analytics_Client_ID = "11";
	public static final String Channel_Type = "12";
	public static final String Request_PRD = "13";
	public static final String Keep_Alive_Interval = "14";
	public static final String RAT_Type = "101";
	public static final String IP_CAN_Type = "102";
	public static final String Session_Id = "105";
	public static final String Application_Id = "106";
	public static final String Peer_Identity = "107";
	public static final String SGSN_IP_Address = "108";
	public static final String SGSN_MCC_MNC = "109";
	public static final String AF_Application_ID = "110";
	public static final String Subscriber_ID = "111";

	public static final String User_Equipment_Information = "112";
	public static final String Called_Station_Identifier = "118";
	public static final String Mobility_Protocol = "119";
	public static final String Billing_Day = "120";
	public static final String Entitlement = "121";
	public static final String Tier = "122";
	public static final String Event_triggers_mask = "123";
	public static final String Location_Area_Code = "124";
	public static final String Cell_Identifier = "125";
	public static final String Cell_Global_Identifier = "126";
	public static final String EUTRAN_Cell_Identifier = "127";
	public static final String MCCMNC = "128";
	public static final String Service_Area_Code = "129";
	public static final String Routing_Area_Code = "130";
	public static final String Tracking_Area_Code = "131";
	public static final String Access_Network_Charging_Address = "132";

	public static final String Access_Network_Charging_Address_ID_Gx = "133";
	public static final String Access_Network_Charging_Identifier_Value = "134";
	public static final String Charging_Rule_Name = "135";
	public static final String Charging_Rule_Base_Name = "136";
	public static final String Origin_Host = "137";
	public static final String Framed_IP_Address = "138";
	public static final String PGW_IP_address = "139";
	public static final String Quota_Usage = "140";
	public static final String Usage_Update = "141";
	public static final String Quota_Name = "142";
	public static final String Monitoring_Key = "143";
	public static final String Usage_Value = "144";
	public static final String Quota_Limit_Type = "145";

	public static final String Quota_Limit_Value = "146";
	public static final String QoS_Information_of_current_Gx = "150";
	public static final String QoS_Class_Identifier = "151";
	public static final String Max_Requested_Bandwidth_UL = "152";
	public static final String Max_Requested_Bandwidth_DL = "153";
	public static final String Guaranteed_Bitrate_UL = "154";
	public static final String Guaranteed_Bitrate_DL = "155";
	public static final String Bearer_ID_value = "156";
	public static final String Allocation_Retention_Priority = "157";
	public static final String APR_Pre_Emption_Capability_value = "158";
	public static final String APR_Pre_Emption_Vulnerability_value = "159";
	public static final String APN_Aggregate_Max_Bitrate_UL_value = "160";
	public static final String APN_Aggregate_Max_Bitrate_DL_value = "161";

	public static final String Default_EPS_Bearer_QoS = "162";
	public static final String SOC = "163";
	public static final String Policy = "500";
	public static final String Policy_Name = "501";
	public static final String Policy_ID = "502";
	public static final String Policy_Mandatory_Action = "503";
	public static final String Policy_Optional_Action = "504";
	public static final String Policy_Action_ID = "505";
	public static final String Policy_Action_Replacement = "506";
	public static final String Policy_Action_Replacement_Sequence = "507";
	public static final String Policy_Action_Replacement_Value = "508";
	public static final String Policy_Table_ID = "509";
	public static final String Policy_Table_Association = "510";
	public static final String Policy_Table_Alias = "511";
	public static final String Policy_Table = "512";
	public static final String Policy_Table_Row = "513";
	public static final String Policy_Table_Row_Number = "514";
	public static final String Policy_Table_Cell = "515";
	public static final String Policy_Table_Column_Name = "516";
	public static final String Polic_Table_Cell_Value = "517";
	public static final String Matched_Table_Row = "518";
	public static final String Matched_Table_Driven_Policy = "519";

	@SuppressWarnings("serial")
	public static Map<String, String> ADS_TYPE = new HashMap<String, String>() {
		{
			put(Version, STRING);
			put(Event_Type, BYTE);
			put(Reference_Data_Version, STRING);
			put(Policy_Entry_Timestamp, LONG);
			put(Policy_Exit_Timestamp, LONG);
			put(Policy_Event_Serial_Number, LONG);
			put(Error_Message, STRING);
			put(Error_Code, SHORT);
			put(Analytics_Client_ID, STRING);
			put(Channel_Type, STRING);
			put(Request_PRD, STRING);
			put(Keep_Alive_Interval, INTEGER);
			put(RAT_Type, SHORT);
			put(IP_CAN_Type, SHORT);
			put(Session_Id, STRING);
			put(Application_Id, INTEGER);
			put(Peer_Identity, STRING);
			put(SGSN_IP_Address, STRING);
			put(SGSN_MCC_MNC, STRING);
			put(AF_Application_ID, STRING);
			put(Subscriber_ID, STRING);
			put(User_Equipment_Information, STRING);
			put(Called_Station_Identifier, STRING);
			put(Mobility_Protocol, BYTE);
			put(Billing_Day, BYTE);
			put(Entitlement, STRING);
			put(Tier, STRING);
			put(Event_triggers_mask, LONG);
			put(Location_Area_Code, STRING);
			put(Cell_Identifier, STRING);
			put(Cell_Global_Identifier, STRING);
			put(EUTRAN_Cell_Identifier, STRING);
			put(MCCMNC, STRING);
			put(Service_Area_Code, STRING);
			put(Routing_Area_Code, STRING);
			put(Tracking_Area_Code, STRING);
			put(Access_Network_Charging_Address, STRING);
			put(Access_Network_Charging_Address_ID_Gx, STRING);
			put(Access_Network_Charging_Identifier_Value, STRING);
			put(Charging_Rule_Name, STRING);
			put(Charging_Rule_Base_Name, STRING);
			put(Origin_Host, STRING);
			put(Framed_IP_Address, STRING);
			put(PGW_IP_address, STRING);
			put(Quota_Usage, GROUP);
			put(Usage_Update, GROUP);
			put(Quota_Name, STRING);
			put(Monitoring_Key, STRING);
			put(Usage_Value, LONG);
			put(Quota_Limit_Type, BYTE);
			put(Quota_Limit_Value, LONG);
			put(QoS_Information_of_current_Gx, GROUP);// group
			put(QoS_Information_of_current_Gx, SHORT);
			put(Max_Requested_Bandwidth_UL, LONG);
			put(Max_Requested_Bandwidth_DL, LONG);
			put(Guaranteed_Bitrate_UL, LONG);
			put(Guaranteed_Bitrate_DL, LONG);
			put(Bearer_ID_value, STRING);
			put(Allocation_Retention_Priority, SHORT);
			put(APR_Pre_Emption_Capability_value, SHORT);
			put(APR_Pre_Emption_Vulnerability_value, SHORT);
			put(APN_Aggregate_Max_Bitrate_UL_value, LONG);
			put(APN_Aggregate_Max_Bitrate_DL_value, LONG);
			put(Default_EPS_Bearer_QoS, GROUP);// group
			put(SOC, STRING);
			put(Policy, GROUP);// group
			put(Policy_Name, STRING);
			put(Policy_ID, LONG);
			put(Policy_Mandatory_Action, GROUP);// group
			put(Policy_Optional_Action, GROUP);// group
			put(Policy_Action_ID, STRING);
			put(Policy_Action_Replacement, GROUP);// group
			put(Policy_Action_Replacement_Sequence, INTEGER);
			put(Policy_Action_Replacement_Value, STRING);
			put(Policy_Table_ID, LONG);
			put(Policy_Table_Association, GROUP);// group
			put(Policy_Table_Association, STRING);
			put(Policy_Table, GROUP);// group
			put(Policy_Table_Row, GROUP);// group
			put(Policy_Table_Row_Number, INTEGER);
			put(Policy_Table_Cell, GROUP);// group
			put(Policy_Table_Column_Name, STRING);
			put(Polic_Table_Cell_Value, STRING);
			put(Matched_Table_Row, GROUP);// group
			put(Matched_Table_Driven_Policy, GROUP);// group
			put("520", GROUP);// group
			put("521", GROUP);// group
		}
	};

}
