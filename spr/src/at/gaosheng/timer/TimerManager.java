package at.gaosheng.timer;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;
import java.util.Timer;
import java.util.TimerTask;

import at.gaosheng.process.ProcessThread;
import at.gaosheng.sftp.DownloadFileThread;
import at.gaosheng.util.Utils;

public class TimerManager {

	public static Properties prop = new Properties();

	public TimerManager() {
		prop = Utils.loadPropeties(System.getProperty("user.dir") + Utils.sf + "spr.cfg");

		// Create directory
		String DOWNLOAD_DIR = prop.getProperty("DOWNLOAD_DIR").trim();
		Utils.createDir(DOWNLOAD_DIR);

		// mod for create backup dir automaticly
		String BACKUP_DIR = prop.getProperty("BACKUP_DIR").trim();
		Utils.createDir(BACKUP_DIR);

		String stepMaxDay = prop.getProperty("STEP_MAX_DAY").trim();
		int step_max_day = 1;
		if (null != stepMaxDay) {
			step_max_day = Integer.valueOf(prop.getProperty("STEP_MAX_DAY").trim());
			if (step_max_day <= 0) {
				step_max_day = 1;
			}
		}

		Calendar calendar = Calendar.getInstance();
		// calendar.set(Calendar.DATE,calendar.get(Calendar.DATE) +
		// steep_max_day);
		calendar.set(Calendar.HOUR_OF_DAY, Integer.valueOf(prop.getProperty("HOUR_OF_DAY").trim()));
		calendar.set(Calendar.MINUTE, Integer.valueOf(prop.getProperty("MINUTE_OF_DAY").trim()));
		calendar.set(Calendar.SECOND, Integer.valueOf(prop.getProperty("SECOND_OF_DAY").trim()));

		Date date = calendar.getTime();

		if (date.before(new Date())) {
			date = this.addDay(date, step_max_day);
		}

		int steep = 24 * 60 * 60 * 1000;
		if (step_max_day > 0) {
			steep = step_max_day * 24 * 60 * 60 * 1000;
		}

		// steep = 10 * 60 * 1000;
		Utils.printlnInOut("TimerManager", "First times Execute :" + date);
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
		String dateString = formatter.format(new Date());
		Utils.printlnInOut("TimerManager", "NOW :" + dateString);
		Timer timer = new Timer();
		TimerTask task = new TimerTask() {
			@Override
			public void run() {
				Utils.printlnInOut("TimerManager", "Start Download Thread--");
				DownloadFileThread downloadFileThread = new DownloadFileThread(prop);
				downloadFileThread.start();

				Utils.printlnInOut("TimerManager", "Start Process Thread--");
				ProcessThread processThread = new ProcessThread(prop);
				processThread.start();
			}

		};
		timer.schedule(task, date, steep);
		;
	}

	public Date addDay(Date date, int num) {
		Calendar startDT = Calendar.getInstance();
		startDT.setTime(date);
		startDT.add(Calendar.DAY_OF_MONTH, num);
		return startDT.getTime();
	}
}
