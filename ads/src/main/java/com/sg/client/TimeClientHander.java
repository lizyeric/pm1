package com.sg.client;

import java.util.Iterator;
import java.util.Set;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.service.IoHandler;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;

import com.sg.common.BaseConfig;
import com.sg.common.MSG_TYPE;
import com.sg.common.Utils;

public class TimeClientHander implements IoHandler {

	public StringBuilder tmpMsgBuffer = new StringBuilder();
	public String tmpMsg = "";
	public String type = "";
	public int length = 0;

	public void exceptionCaught(IoSession arg0, Throwable arg1) throws Exception {
		// TODO Auto-generated method stub
		arg1.printStackTrace();
	}

	public void messageReceived(IoSession session, Object message) throws Exception {
		// TODO Auto-generated method stub
		try {
			IoBuffer ioBuffer = (IoBuffer) message;
			String hexDump = ioBuffer.getHexDump();
			hexDump = hexDump.replaceAll(" ", "");
			if ("".equals(tmpMsgBuffer.toString())) {
				if (!"".equals(tmpMsg)) {
					hexDump = tmpMsg + hexDump;
				}

				type = hexDump.substring(0, 4);
				length = Utils.hexStr2Int(hexDump.substring(4, 12));
				tmpMsg += hexDump.substring(12);
				tmpMsgBuffer.append(hexDump.substring(12));
				tmpMsg = "";

			} else {
				tmpMsgBuffer.append(hexDump);
				tmpMsg += hexDump;
			}
			parseMsg(session);
		} catch (Exception e) {
			if (null != session) {
				session.closeOnFlush();
			}
			e.printStackTrace();
		}

	}

	private void parseMsg(IoSession session) {
		if (tmpMsgBuffer.toString().length() == length * 2) {
			execMsg(session, tmpMsgBuffer.toString());
		} else if (tmpMsgBuffer.toString().length() > (length * 2)) {
			// System.out.println("length out of range:" +
			// tmpMsgBuffer.toString().length());
			String secTmp = tmpMsgBuffer.toString();
			String secHexDump = secTmp.substring(length * 2);
			execMsg(session, secTmp.substring(0, length * 2));

			if (secHexDump.length() > 12) {
				tmpMsgBuffer.append(secHexDump.substring(12));
				type = secHexDump.substring(0, 4);
				length = Utils.hexStr2Int(secHexDump.substring(4, 12));
				tmpMsg += secHexDump.substring(12);

				parseMsg(session);
			} else {
				tmpMsg = secHexDump;
			}
		}
	}

	private void execMsg(IoSession session, String tmpMsg) {
		// TODO Auto-generated method stub
		if (type.equalsIgnoreCase(MSG_TYPE.Stop_Acknowledgement)) {
			session.closeOnFlush();
		} else if (type.equalsIgnoreCase(MSG_TYPE.Keep_Alive)) {
			// session.closeOnFlush();
		} else if (type.equalsIgnoreCase(MSG_TYPE.Version_Acknowledgement)) {
			// sent 0007
			session.write(IoBuffer.wrap(Utils.str2byte(MinaTimeClient.properties.getProperty("IR"))));
		} else if (type.equalsIgnoreCase(MSG_TYPE.Error)) {
			session.closeOnFlush();
		} else if (type.equalsIgnoreCase(MSG_TYPE.Policy_Event_Record)
				|| type.equalsIgnoreCase(MSG_TYPE.Policy_Reference_Data)) {
			Utils.parseADS(tmpMsg, type, length);
		}
		tmpMsgBuffer = new StringBuilder();
		length = 0;
		type = "";
	}

	public void messageSent(IoSession arg0, Object message) throws Exception {
		// TODO Auto-generated method stub
		IoBuffer ioBuffer = (IoBuffer) message;
		// Utils.printlnInOut("messageSent", "client send message:" +
		// ioBuffer.getHexDump());
		MinaTimeClient.log.getLogger().info("client send message:" + ioBuffer.getHexDump());

	}

	public void sessionClosed(IoSession session) throws Exception {
		// TODO Auto-generated method stub
		// Utils.printlnInOut("sessionClosed", "client disconnect with:" +
		// session.getRemoteAddress().toString());
		MinaTimeClient.log.getLogger().info("client disconnect with:" + session.getRemoteAddress().toString());
	}

	public void sessionCreated(IoSession session) throws Exception {
		// TODO Auto-generated method stub
		// Utils.printlnInOut("sessionCreated", "client connect with:" +
		// session.getRemoteAddress().toString());
		MinaTimeClient.log.getLogger().info("client connect with:" + session.getRemoteAddress().toString());
	}

	public void sessionIdle(IoSession session, IdleStatus status) throws Exception {
		// TODO Auto-generated method stub
		if (null != session) {
			String addr = session.getRemoteAddress().toString();
			Set<String> keySet = MinaTimeClient.status.keySet();
			Iterator<String> it = keySet.iterator();
			while (it.hasNext()) {
				String key = it.next();
				if (addr.contains(key)) {
					MinaTimeClient.status.put(key, BaseConfig.STOP);
				}
			}
		}
	}

	public void sessionOpened(IoSession arg0) throws Exception {
		// TODO Auto-generated method stub
		// System.out.println("Open connector");
		// Utils.printlnInOut("sessionOpened", "Open connector");
	}

	public void inputClosed(IoSession arg0) throws Exception {
		// TODO Auto-generated method stub

	}

}