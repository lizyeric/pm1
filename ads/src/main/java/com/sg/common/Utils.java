package com.sg.common;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import com.sg.client.MinaTimeClient;
import com.sg.db.ExecuteADS2DB;
import com.sg.log.ExecuteADSLog;

public class Utils {

	public static String callCmd(String cmd) {
		StringBuilder result = new StringBuilder();
		try {

			Process process = Runtime.getRuntime().exec(

					new String[] { "/bin/sh", "-c", cmd });

			int exitCode = process.waitFor();

			InputStream is = process.getInputStream();

			InputStreamReader isr = new InputStreamReader(is);

			BufferedReader br = new BufferedReader(isr);

			String line = null;

			while ((line = br.readLine()) != null) {
				// System.out.println(line);
				result.append(line);
			}

			is.close();

			isr.close();

			br.close();

			// System.out.println("Execute command end...");

		} catch (Exception e) {
			// Utils.printlnInOut("Utils callCmd()", e.getMessage());
		}
		return result.toString();
	}

	// public static void printlnInOut(String name, String text) {
	// String date = getDate();
	// System.out.println(date + "---" + name + "---" + text);
	// }

	public static String getDate() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = new Date();
		return sdf.format(date);
	}

	public static void createDirs(String dir) {
		File fileFolder = new File(dir);
		if (!fileFolder.exists()) {
			fileFolder.mkdirs();
		}
	}

	public static Properties loadProperties(String propertyName) {
		Properties prop = new Properties();
		FileInputStream fis = null;
		try {
			File f = new File(propertyName);
			fis = new FileInputStream(f);
			prop.load(fis);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (null != fis) {
				try {
					fis.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return prop;
	}

	public static byte[] str2byte(String str) {
		String[] cmds = str.split(":");
		byte[] b = new byte[cmds.length];
		int i = 0;
		for (String c : cmds) {

			try {
				b[i++] = Byte.parseByte(c, 16);
			} catch (Exception e) {
				// TODO: handle exception
				b[i] = -1;
			}

		}
		return b;
	}

	public static Integer hexStr2Int(String hexStr) {
		return Integer.parseInt(hexStr, 16);
	}

	public static long hexStr2Long(String hexStr) {
		return Long.parseLong(hexStr, 16);
	}

	public static String hexStr2Str(String hexStr) {
		String str = "0123456789ABCDEF";
		char[] hexs = hexStr.toCharArray();
		byte[] bytes = new byte[hexStr.length() / 2];
		int n;

		for (int i = 0; i < bytes.length; i++) {
			n = str.indexOf(hexs[2 * i]) * 16;
			n += str.indexOf(hexs[2 * i + 1]);
			bytes[i] = (byte) (n & 0xff);
		}
		return new String(bytes, Charset.forName("UTF-8"));
	}

	public static void main(String[] args) {

		System.out.println(hexStr2Str("20607B307D60"));
		System.out.println(Integer.parseInt("0000018D", 16));
		System.out.println(Integer.parseInt("000001A2", 16));
		// 01F60000000F020801040704090708010100080007
		// 281474978110807
	}

	public static void parseADS(String hexDump, String type, int length) {
		// TODO Auto-generated method stub
		// String ads = hexDump.substring(12);
		// parse per
		if (type.equalsIgnoreCase(MSG_TYPE.Policy_Event_Record)) {
			parsePER(hexDump);
		} else if (type.equalsIgnoreCase(MSG_TYPE.Policy_Reference_Data)) {
			parsePRD(hexDump);
		}

	}

	private static void parsePRD(String ads) {
		// TODO Auto-generated method stub
		ArrayList<Map<String, String>> list = new ArrayList<Map<String, String>>();
		Map<String, String> map = new HashMap<String, String>();
		map.put("TYPE", "PRD");
		list.add(map);
		parseType_Value(ads, list, "PRD");
		List<Map<String, String>> prd = new ArrayList<Map<String, String>>();
		prd.addAll(list);
		if ("0".equals(MinaTimeClient.properties.getProperty("LOG_VALVE"))) {
			ExecuteADSLog.getExecutor().addToQueue(prd);
		}
		ExecuteADS2DB.getExecutor().addToQueue(list);
		// System.out.println("List:" + list.toString());
	}

	public static void parsePER(String hexDump) {
		// TODO Auto-generated method stub
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		Map<String, String> map = new HashMap<String, String>();
		map.put("TYPE", "PER");
		list.add(map);
		parseType_Value(hexDump, list, "PER");
		// System.out.println(list);
		List<Map<String, String>> per = new ArrayList<Map<String, String>>();
		per.addAll(list);
		if ("0".equals(MinaTimeClient.properties.getProperty("LOG_VALVE"))) {
			ExecuteADSLog.getExecutor().addToQueue(per);
		}
		ExecuteADS2DB.getExecutor().addToQueue(list);
		// System.out.println("List:" + list.toString());
	}

	public static void parseType_Value(String hexDump, List<Map<String, String>> list, String PRD_PER) {
		String type = hexDump.substring(0, 4);
		String length = hexDump.substring(4, 12);
		try {
			Map<String, String> map = new HashMap<String, String>();
			String value = hexDump.substring(12, Integer.parseInt(length, 16) * 2 + 12);
			int typeV = Integer.parseInt(type, 16);
			String typeD = String.valueOf(typeV);
			String value4Type = "";
			if ("Short".equals(MSG_TYPE.ADS_TYPE.get(typeD))) {
				value4Type = String.valueOf(hexStr2Int(value));
			} else if ("Long".equals(MSG_TYPE.ADS_TYPE.get(typeD))) {
				if (typeV == 4 || typeV == 5) {
					value4Type = String.valueOf(BaseConfig.sfSecond.format(new Date(hexStr2Long(value))));
				} else {
					value4Type = String.valueOf(hexStr2Long(value));
				}
			} else if ("Integer".equals(MSG_TYPE.ADS_TYPE.get(typeD))) {
				value4Type = String.valueOf(hexStr2Int(value));
			} else if ("byte".equals(MSG_TYPE.ADS_TYPE.get(typeD))) {
				value4Type = String.valueOf(hexStr2Int(value));
			} else if ("group".equals(MSG_TYPE.ADS_TYPE.get(typeD))) {
				parseType_Value(value, list, PRD_PER);
			} else {
				value4Type = hexStr2Str(value);
			}
			if ("PRD".equals(PRD_PER)) {
				if (typeV == 502) {
					map.put(typeD, value4Type);
					list.add(map);
					String data = hexDump.substring(Integer.parseInt(length, 16) * 2 + 12);
					// System.out.println("type:" + typeV + " value:" +
					// value4Type);
				} else if (typeV < 502) {
					map.put(typeD, value4Type);
					list.add(map);
					String data = hexDump.substring(Integer.parseInt(length, 16) * 2 + 12);
					// System.out.println("type:" + typeV + " value:" +
					// value4Type);
					if (null != data && !"".equals(data)) {
						parseType_Value(data, list, PRD_PER);
					}
				}
			} else {
				map.put(typeD, value4Type);
				list.add(map);
				String data = hexDump.substring(Integer.parseInt(length, 16) * 2 + 12);
				// System.out.println("type:" + typeV + " value:" + value4Type);
				if (null != data && !"".equals(data)) {
					parseType_Value(data, list, PRD_PER);
				}
			}
		} catch (Exception e) {
			// System.out.println("Error: type:" + type + " length:" + length);
			// System.out.println(e);
		}
	}

	public static String printExceptionToString(Exception e) {
		StringBuilder sb = new StringBuilder();
		sb.append(e.toString()).append("\n");
		for (StackTraceElement elem : e.getStackTrace()) {
			sb.append("\tat ").append(elem).append("\n");
		}
		return sb.toString();
	}
}
