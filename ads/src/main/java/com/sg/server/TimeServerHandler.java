package com.sg.server;

import java.util.Date;

import org.apache.mina.core.service.IoHandler;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;

public class TimeServerHandler implements IoHandler {

	public void exceptionCaught(IoSession arg0, Throwable arg1) throws Exception {
		arg1.printStackTrace();

	}

	public void messageReceived(IoSession session, Object message) throws Exception {

		String str = message.toString();


		if (str.trim().equalsIgnoreCase("quit")) {
			session.write("quit");
			session.close(true);
			return;
		}
		Date date = new Date();
		session.write(date.toString());
		System.out.println("Message written...");
	}

	public void messageSent(IoSession arg0, Object arg1) throws Exception {
		// TODO Auto-generated method stub
		System.out.println("发送信息:" + arg1.toString());
	}

	public void sessionClosed(IoSession session) throws Exception {
		// TODO Auto-generated method stub
		System.out.println("IP:" + session.getRemoteAddress().toString() + "断开连接");
	}

	public void sessionCreated(IoSession session) throws Exception {
		// TODO Auto-generated method stub
		System.out.println("IP:" + session.getRemoteAddress().toString());
	}

	public void sessionIdle(IoSession session, IdleStatus status) throws Exception {
		// TODO Auto-generated method stub
		System.out.println("IDLE " + session.getIdleCount(status));

	}

	public void sessionOpened(IoSession arg0) throws Exception {
		// TODO Auto-generated method stub

	}

	public void inputClosed(IoSession arg0) throws Exception {
		// TODO Auto-generated method stub

	}

}
