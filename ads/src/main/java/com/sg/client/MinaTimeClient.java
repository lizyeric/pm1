package com.sg.client;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import com.sg.common.BaseConfig;
import com.sg.common.Utils;
import com.sg.log.CoreLogger;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class MinaTimeClient {
	public static String separator = System.getProperty("file.separator");
	public static String popCfg = System.getProperty("user.dir") + separator + "pop.cfg";
	public static Properties properties = null;
	public static Map<String, String> status = new HashMap<String, String>();
	public static boolean isClose = false;
	public static String failover_ip = "";
	public static String pcrfid = "";
	public static CoreLogger log;

	public static void main(String[] args) {

		log = new CoreLogger();
		Utils.createDirs(BaseConfig.logPath);
		properties = Utils.loadProperties(popCfg);
		String master = properties.getProperty("MASTER");
		int heartBeat_port = Integer.valueOf(properties.getProperty("HEART_BEAT_PORT"));
		long interval = Long.valueOf(properties.getProperty("HEART_BEAT_INTERVAL"));
		int attempt = Integer.valueOf(properties.getProperty("HEART_BEAT_ATTEMPT"));

		if ("true".equalsIgnoreCase(master)) {

			// start heartbeat server
			pcrfid = properties.getProperty("PCRF_ID");
		} else {
			// start heartbeat client
			String heartBeatMpe = properties.getProperty("HEART_BEAT_MPE");
			JSONObject jsonObj = JSONObject.fromObject(heartBeatMpe);
			Iterator it = jsonObj.keys();
			log.getLogger().info(failover_ip + " server failed.");
		}
		// start ads thread.
		String ip = properties.getProperty(BaseConfig.IP);
		String[] ips = ip.split(",");
		if (!"true".equalsIgnoreCase(master)) {
			// start heartbeat server
			String heartBeatMpe = properties.getProperty("HEART_BEAT_MPE");
			JSONObject jsonObj = JSONObject.fromObject(heartBeatMpe);
			JSONObject pcrf = jsonObj.getJSONObject(failover_ip);
			pcrfid = pcrf.getString("PCRF_ID");
			JSONArray arr = pcrf.getJSONArray("MPE_IP");
			ips = new String[arr.size()];
			for (int i = 0; i < arr.size(); i++) {
				ips[i] = (String) arr.get(i);
			}
			// Utils.printlnInOut("MinaTimeClient", "Failover server connect
			// with mpe server.");
			log.getLogger().info("Failover server connect with mpe server.");
		}

		String ads_port = properties.getProperty(BaseConfig.PORT);
		for (int i = 0; i < ips.length; i++) {
			try {
				Thread t = new Thread(new ClientThread(ips[i], ads_port));
				t.start();
				status.put(ips[i], BaseConfig.RUNNING);
			} catch (Exception e) {
				// Utils.printlnInOut("Start TCP Client Thread", ips[i] + "
				// Connection timed out");
				log.getLogger().info("Start TCP Client Thread " + ips[i] + " Connection timed out");
			}
		}
		boolean quit = false;
		Properties prop = null;
		while (!quit) {
			prop = Utils.loadProperties(popCfg);

			if ("2".equalsIgnoreCase(prop.getProperty(BaseConfig.QUIT))) {
				quit = true;
				Set<String> keySet = status.keySet();
				Iterator<String> it = keySet.iterator();
				while (it.hasNext()) {
					String key = it.next();
					status.put(key, BaseConfig.STOP);
				}
				isClose = true;
			}

			// close all tcp connect.
			if ("1".equalsIgnoreCase(prop.getProperty(BaseConfig.QUIT))) {
				Set<String> keySet = status.keySet();
				Iterator<String> it = keySet.iterator();
				while (it.hasNext()) {
					String key = it.next();
					status.put(key, BaseConfig.CLOSE);
				}
			}

			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

}
