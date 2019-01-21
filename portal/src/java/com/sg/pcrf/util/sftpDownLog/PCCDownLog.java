package com.sg.pcrf.util.sftpDownLog;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.Vector;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.SftpATTRS;
import com.sg.pcrf.LangUtils;
import com.sg.pcrf.util.Sftp.ProgressMonitor;
import com.sg.pcrf.util.Sftp.SFTPChannel;
import com.sg.pcrf.util.Sftp.SFTPConstants;
import com.sg.pcrf.util.Sftp.SftpDown;

/**
 * Created by Administrator on 17-8-28.
 */
public class PCCDownLog {
    public String downPCCLog(String downPath,String host,String username,String password,String port) throws Exception {
        SftpDown test = new SftpDown();
        Map<String, String> sftpDetails = new HashMap<String, String>();
        // 设置主机ip，端口，用户名，密码
        sftpDetails.put(SFTPConstants.SFTP_REQ_HOST, host);
        sftpDetails.put(SFTPConstants.SFTP_REQ_USERNAME, username);
        sftpDetails.put(SFTPConstants.SFTP_REQ_PASSWORD, password);
        sftpDetails.put(SFTPConstants.SFTP_REQ_PORT, port);

        SFTPChannel channel = test.getSFTPChannel();
        ChannelSftp chSftp = channel.getChannel(sftpDetails, 60000);
        String pathCh="./";
        Vector ar=chSftp.ls(pathCh);
        SftpATTRS al=chSftp.lstat(pathCh);
        Map<String, Long> getTime = new HashMap<String, Long>();
        ValueComparator bvc = new ValueComparator(getTime);
        TreeMap<String, Long> sorted_map = new TreeMap<String, Long>(bvc);
        SimpleDateFormat sdf1= new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy", Locale.ENGLISH);
//        SimpleDateFormat sdf2= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        for(int ft=0;ft<ar.size();ft++){
            String af=ar.get(ft).toString();
            boolean tea = af.contains("CMPP.log");
            if(tea){
                String subFileName=af.substring(af.lastIndexOf(" ")+1,af.length());
                SftpATTRS attr = chSftp.stat(pathCh+"/"+subFileName);
                String fileCretearTime=attr.getMtimeString();//获取sftp文件创建时间
                Date dates=sdf1.parse(fileCretearTime);
                long longFileTime=dates.getTime();//文件时间
                getTime.put(subFileName, longFileTime);
            }
        }
        sorted_map.putAll(getTime);//获取根据文件时间排序后的log文件

        //以下循环列表下载log文件
        Set set=sorted_map.keySet();
        Iterator<String> it = set.iterator();
        Object[] fileName=(Object[])set.toArray();
        int index=0;
        // Get file content读取存储位置的文件的内容（包括文件名，行数）
        File fileDir = new File(downPath);
        if(!fileDir.exists()){
            fileDir.mkdirs();
        }
        //String ftpLog=downPath.substring(0,downPath.lastIndexOf("\\"));
        String ftpLog=downPath.substring(0,downPath.lastIndexOf("/"));
        File ftplogRecord = new File(ftpLog+"/ftplogRecord");
        if(!ftplogRecord.exists()){
            ftplogRecord.mkdirs();
        }
        File ftplogRecordTxt = new File(ftplogRecord+"/ftpLogRecord.txt");
        if(!ftplogRecordTxt.exists()){
            ftplogRecordTxt.createNewFile();// 创建txt文件
        }
        java.io.BufferedReader reader = new java.io.BufferedReader(
                new java.io.FileReader(ftplogRecordTxt));
        String s = reader.readLine();// 读取日志文件的一行记录
        String logFileName = null;
        if (s != null) {// 判断是否记录了节点
            logFileName = s.substring(0, s.indexOf(":"));
        }
        if (LangUtils.isNotEmpty(logFileName)) {
            for (int i = 0; i < fileName.length; i++) {
                if (fileName[i].equals(logFileName)) {
                    index = i + 1;
                    continue;
                }
            }
        }

        try {
            for(int f=index;f<fileName.length;f++){
                String downFileName = pathCh+"/"+fileName[f].toString();
                String dst=fileDir+"/"+fileName[f].toString();
                OutputStream out = new FileOutputStream(dst);
                InputStream is = chSftp.get(downFileName, new ProgressMonitor());
                byte[] buff = new byte[1024 * 2];
                int read;
                if (is != null) {
                    System.out.println("Start to read input stream");
                    do {
                        read = is.read(buff, 0, buff.length);
                        if (read > 0) {
                            out.write(buff, 0, read);
                        }
                        out.flush();
                    } while (read >= 0);
                    System.out.println("input stream read done.");
                }
                PrintWriter pw = new PrintWriter(new FileWriter(ftplogRecordTxt));
                pw.print(fileName[f] + ":");
                pw.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            chSftp.quit();
            channel.closeChannel();
        }
        return fileDir.toString();
    }

}

