package com.sg.db;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.commons.lang3.concurrent.BasicThreadFactory;

import com.sg.client.MinaTimeClient;

public class ExecuteADS2DB {

	private static int DB_THREADS = 200;
	private static ExecuteADS2DB adsExecutor = new ExecuteADS2DB();
	public static List<List<Map<String, String>>> perList = new ArrayList<List<Map<String, String>>>();
	public static List<List<Map<String, String>>> prdList = new ArrayList<List<Map<String, String>>>();
	public static Object object = new Object();
	private ExecutorService service;

	private ExecuteADS2DB() {
		if (MinaTimeClient.properties.containsKey("DB_THREADS")) {
			DB_THREADS = Integer.valueOf(MinaTimeClient.properties.getProperty("DB_THREADS"));
		}
		BasicThreadFactory threadFactory = new BasicThreadFactory.Builder().namingPattern("ADS2DBWorker-%d")
				.daemon(true).priority(Thread.MAX_PRIORITY).build();

		service = Executors.newFixedThreadPool(DB_THREADS, threadFactory);

		// ADSCheck adsCheck = new ADSCheck();
		// adsCheck.start();

	}

	public static ExecuteADS2DB getExecutor() {
		return adsExecutor;
	}

	public synchronized void addToQueue(List<Map<String, String>> list) {

		DBTask task = new DBTask(list);

		service.execute(task);

	}

	class DBTask implements Runnable {

		private List<Map<String, String>> list;

		public DBTask(List<Map<String, String>> list) {
			this.list = list;
		}

		public void run() {
			ads2DB(list);
		}

	}

	public static void ads2DB(List<Map<String, String>> list) {
		try {
			if (null != list && list.size() > 0) {
				synchronized (object) {
					Connection connection = MysqlConnPool.getInstance().getConnection();
					List<Map<String, String>> l = new ArrayList<Map<String, String>>();
					l.addAll(list);
					Map<String, String> map0 = l.remove(0);
					// System.out.println(map0);
					if ("PER".equals(map0.get("TYPE"))) {
						perList.add(l);
						if (perList.size() == Integer.valueOf(MinaTimeClient.properties.getProperty("COMMIT_COUNT"))) {
							MysqlHelper.executeBatchPER(connection, perList);
							perList.clear();
						}
					} else if ("PRD".equals(map0.get("TYPE"))) {
						prdList.add(l);
						MysqlHelper.executeBatchPRD(connection, prdList);
						prdList.clear();
					}
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
