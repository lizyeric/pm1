package com.sg.pcrf.rcmgr;

import com.jcraft.jsch.*;

import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 * Created by jyx on 2017/7/25.
 */

public class SSHManger {
    private static final Logger LOGGER = Logger.getLogger(SSHManger.class.getName());
    private JSch jschSSHChannel;
    private String strUserName;
    private String strConnectionIP;
    private int intConnectionPort;
    private String strPassword;
    private Session sesConnection;
    private int intTimeOut;

    private void doCommonConstructorActions(String userName, String password, String connectionIP, String knownHostsFileName){
//        链接信息初始化总构造函数
        jschSSHChannel = new JSch();

        try {
            jschSSHChannel.setKnownHosts(knownHostsFileName);
        } catch (JSchException jschX) {
            logError(jschX.getMessage());
        }

        strUserName = userName;
        strPassword = password;
        strConnectionIP = connectionIP;
    }

    public SSHManger(String userName, String password, String connectionIP, String knownHostsFileName) {
        doCommonConstructorActions(userName, password, connectionIP, knownHostsFileName);
        intConnectionPort = 22;
        intTimeOut = 60000;
    }

    public SSHManger(String userName, String password, String connectionIP,
                     String knownHostsFileName, int connectionPort) {
        doCommonConstructorActions(userName, password, connectionIP, knownHostsFileName);
        intConnectionPort = connectionPort;
        intTimeOut = 60000;
    }

    public SSHManger(String userName, String password, String connectionIP,
                     String knownHostsFileName, int connectionPort, int timeOutMilliseconds) {
        doCommonConstructorActions(userName, password, connectionIP, knownHostsFileName);
        intConnectionPort = connectionPort;
        intTimeOut = timeOutMilliseconds;
    }

    public String connect() {
        String errorMessage = null;

        try {
            sesConnection = jschSSHChannel.getSession(strUserName, strConnectionIP, intConnectionPort);
            sesConnection.setPassword(strPassword);
//            不对主机的host key进行验证，必要时可关闭以增强安全性。
            sesConnection.setConfig("StrictHostKeyChecking", "no");
            sesConnection.connect(intTimeOut);
        } catch (JSchException jschX) {
            errorMessage = jschX.getMessage();
        }

        return errorMessage;
    }

    public String sendCommand(String command) throws IOException, JSchException {
//        执行指令
        StringBuilder outputBuffer = new StringBuilder();

        try
        {
            Channel channel = sesConnection.openChannel("exec");
            ((ChannelExec)channel).setCommand(command);
//            获取channel的输入流
            InputStream commandOutput = channel.getInputStream();
            channel.connect();
            int readByte = commandOutput.read();
//            循环读取数据
            while(readByte != -1)
            {
                outputBuffer.append((char)readByte);
                readByte = commandOutput.read();
            }

            channel.disconnect();
        } catch(IOException ioX)
        {
            logWarning(ioX.getMessage());
            throw ioX;
        } catch(JSchException ioX)
        {
            logWarning(ioX.getMessage());
            throw ioX;
        }

        return outputBuffer.toString();
    }

    private String logError(String errorMessage) {
        if (errorMessage != null) {
            LOGGER.log(Level.SEVERE, "{0}:{1} - {2}",
                    new Object[]{strConnectionIP, intConnectionPort, errorMessage});
        }
        return errorMessage;
    }

    private String logWarning(String warnMessage) {
        if (warnMessage != null) {
            LOGGER.log(Level.WARNING, "{0}:{1} - {2}",
                    new Object[]{strConnectionIP, intConnectionPort, warnMessage});
        }
        return warnMessage;
    }

    public void close() {
        sesConnection.disconnect();
    }
}
