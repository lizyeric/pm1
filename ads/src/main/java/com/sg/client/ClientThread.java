package com.sg.client;

import java.net.InetSocketAddress;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.service.IoConnector;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.logging.LoggingFilter;

import com.sg.common.BaseConfig;
import com.sg.common.Utils;

public class ClientThread implements Runnable {

	private String ip;
	private String port;

	public ClientThread(String ip, String port) {
		this.ip = ip;
		this.port = port;
	}

	public void run() {
		long attempt_interval = Long.valueOf(MinaTimeClient.properties.getProperty("ATTEMPT_INTERVAL").trim());
		while (true && !MinaTimeClient.isClose) {
			boolean flag = true;
			IoConnector connector = new NioSocketConnector();
			connector.getFilterChain().addLast("logger", new LoggingFilter());
			connector.setHandler(new TimeClientHander());
			connector.getSessionConfig().setIdleTime(IdleStatus.BOTH_IDLE,
					Integer.valueOf(MinaTimeClient.properties.getProperty("IDLE_TIME").trim()));

			int count = 0;
			while (!BaseConfig.RUNNING.equals(MinaTimeClient.status.get(ip))) {
				for (int i = 1; i <= Integer
						.valueOf(MinaTimeClient.properties.getProperty("CONNECT_ATTEMPT").trim()); i++) {
					count++;
					boolean connectFlag = true;
					try {
						ConnectFuture connectFuture = connector
								.connect(new InetSocketAddress(ip, Integer.valueOf(port)));
						connectFuture.awaitUninterruptibly();
						IoSession session = connectFuture.getSession();
						session.write(IoBuffer.wrap(Utils.str2byte(MinaTimeClient.properties.getProperty("VR"))));
						MinaTimeClient.status.put(ip, BaseConfig.RUNNING);
					} catch (Exception e) {
						MinaTimeClient.log.getErrLogger()
								.error("Server: " + ip + ":" + port + " Attempt " + count + ":" + e);
						MinaTimeClient.log.logErr(e);
						connectFlag = false;
						if (count == Integer.valueOf(MinaTimeClient.properties.getProperty("CONNECT_ATTEMPT").trim())) {
							attempt_interval = Long
									.valueOf(MinaTimeClient.properties.getProperty("ATTEMPT_MAX_INTERVAL").trim());
							flag = false;
							MinaTimeClient.status.put(ip, BaseConfig.STOP);
							break;
						}
					}
					if (connectFlag) {
						break;
					} else {
						try {
							Thread.sleep(attempt_interval);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					if (count == Integer.MAX_VALUE) {
						count = 0;
					}
				}
			}

			while (BaseConfig.RUNNING.equals(MinaTimeClient.status.get(ip))) {
				try {
					Thread.sleep(Long.valueOf(MinaTimeClient.properties.getProperty("ATTEMPT_INTERVAL").trim()));
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (null != session) {
				session.write(IoBuffer.wrap(Utils.str2byte(MinaTimeClient.properties.getProperty("CLOSE"))));
			}
			while (MinaTimeClient.status.get(ip).equals(BaseConfig.CLOSE)) {
				try {
					Thread.sleep(Long.valueOf(MinaTimeClient.properties.getProperty("ATTEMPT_INTERVAL").trim()));
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (null != session) {
				// Utils.printlnInOut("ClientThread", ip + " session closed.");
				MinaTimeClient.log.getLogger().info(ip + " ClientThread session closed.");
				session.closeOnFlush();
				connector.dispose(true);
			}
			if (!flag) {
				// Utils.printlnInOut("ClientThread", ip + " Stoped.");
				MinaTimeClient.log.getLogger().info(ip + " ClientThread stoped.");
				break;
			}
		}
	}
}
