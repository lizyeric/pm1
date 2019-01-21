package at.gaosheng.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

public class Utils {
	
	public static String sf = System.getProperty("file.separator");
	public static String exml = ".exml";
	public static String res = ".res";
	public static String tar = ".tar.gz";
	public static String GP = "Group";

	public static void printlnInOut(String name, String text) {
		String date = getDate();
		System.out.println(date + "---" + name + "---" + text);
	}
	
	
	public static String getDate() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = new Date();
		return sdf.format(date);
	}
	
	public static void callCmd(String cmd) {
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
			}

			is.close();

			isr.close();

			br.close();

			// System.out.println("Execute command end...");

		} catch (Exception e) {

			Utils.printlnInOut("Utils callCmd()", e.getMessage());

		}
	}
	
	public static Properties loadPropeties(String propertyName) {
		Properties prop = new Properties();
		try {
			File f = new File(propertyName);
			FileInputStream fis = new FileInputStream(f);
			prop.load(fis);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return prop;
	}
	
	public static void createDir(String DOWNLOAD_DIR) {
		// TODO Auto-generated method stub
		String[] dir = DOWNLOAD_DIR.split("/");
		String path = "/";
		for (int i = 0; i < dir.length; i++) {
			if (null != dir[i] && !"".equals(dir[i])) {
				path += dir[i];
				path += "/";
				File localDownload = new File(path);
				if (!localDownload.exists()) {
					localDownload.mkdir();
				}
			}

		}
	}
}

