package com.sg.log;

import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.commons.lang3.concurrent.BasicThreadFactory;

import com.sg.client.MinaTimeClient;
import com.sg.common.BaseConfig;

public class ExecuteADSLog {

	private static int LOG_THREADS = 200;
	private static ExecuteADSLog adsLogExecutor = new ExecuteADSLog();

	private ExecutorService service;

	private ExecuteADSLog() {
		if (MinaTimeClient.properties.containsKey("LOG_THREADS")) {
			LOG_THREADS = Integer.valueOf(MinaTimeClient.properties.getProperty("LOG_THREADS"));
		}
		BasicThreadFactory threadFactory = new BasicThreadFactory.Builder().namingPattern("ADSLogWorker-%d")
				.daemon(true).priority(Thread.MAX_PRIORITY).build();

		service = Executors.newFixedThreadPool(LOG_THREADS, threadFactory);
	}

	public static ExecuteADSLog getExecutor() {
		return adsLogExecutor;
	}

	public synchronized void addToQueue(List<Map<String, String>> list) {

		LogTask task = new LogTask(list);

		service.execute(task);

	}

	public static String lastReadFile = null;

	class LogTask implements Runnable {

		private List<Map<String, String>> list;

		public LogTask(List<Map<String, String>> list) {
			this.list = list;
		}

		public void run() {
			adsLog(list);
		}

	}

	private synchronized void adsLog(List<Map<String, String>> list) {

		// long startTime = System.currentTimeMillis();
		try {
			StringBuilder sb = new StringBuilder();

			for (Map<String, String> map : list) {
				Set<String> keys = map.keySet();
				Iterator<String> it = keys.iterator();
				while (it.hasNext()) {
					String type = it.next();
					String value = map.get(type);
					sb.append(type).append("=").append(value).append(",");
				}

			}

			Calendar currentTime = Calendar.getInstance();
			String currentReadFile = getCurrent(currentTime);
			lastReadFile = LogFeature.prepareBfReader(lastReadFile, currentReadFile);
			LogFeature.write(sb.toString());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// long endTime = System.currentTimeMillis();
		// System.out.println("write log cost time :" + (endTime - startTime));
	}

	private String getCurrent(Calendar currentTime) {
		// TODO Auto-generated method stub
		Calendar nextTime = Calendar.getInstance();
		int date = currentTime.get(Calendar.DATE);
		nextTime.set(Calendar.DATE, date);
		nextTime.set(Calendar.HOUR, 0);
		nextTime.set(Calendar.MINUTE, 0);
		nextTime.set(Calendar.SECOND, 0);
		nextTime.set(Calendar.MILLISECOND, 0);

		return BaseConfig.sfDate.format(nextTime.getTime());
	}
}
