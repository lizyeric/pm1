package com.sg.pcrf.statistic.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

public class Utils {

	public static void printlnInOut(String name, String text) {
		String date = getDate();
		System.out.println(date + "---" + name + "---" + text);
	}

	public static String getDate() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = new Date();
		return sdf.format(date);
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

	public static void callCmd(String cmd) {
		try {

			Process process = Runtime.getRuntime().exec(cmd);
			int exitCode = process.waitFor();
			InputStream is = process.getInputStream();
			InputStreamReader isr = new InputStreamReader(is);
			BufferedReader br = new BufferedReader(isr);
			String line = null;
			while ((line = br.readLine()) != null) {
				// System.out.println(line);
			}
			is.close();
			isr.close();
			br.close();

			// System.out.println("Execute command end...");

		} catch (Exception e) {

			Utils.printlnInOut("Utils callCmd()", e.getMessage());

		}
	}
}
