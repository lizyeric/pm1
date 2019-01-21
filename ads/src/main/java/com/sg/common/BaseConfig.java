package com.sg.common;

import java.text.SimpleDateFormat;

public class BaseConfig {
	public static String PORT = "PORT";
	public static String IP = "IP";
	public static String RUNNING = "RUNNING";
	public static String STOP = "STOP";
	public static String CLOSE = "CLOSE";
	public static String QUIT = "QUIT";
	public static String separator = System.getProperty("file.separator");
	public static String logPath = System.getProperty("user.dir") + separator + "log" + separator;
	public static SimpleDateFormat sfDate = new SimpleDateFormat("yyyyMMdd");
	public static SimpleDateFormat sfMills = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss SSS");
	public static SimpleDateFormat sfSecond = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
}
