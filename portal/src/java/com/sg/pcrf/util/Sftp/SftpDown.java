package com.sg.pcrf.util.Sftp;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;


import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.SftpATTRS;
import com.jcraft.jsch.SftpException;

public class SftpDown {

    public SFTPChannel getSFTPChannel() {
        return new SFTPChannel();
    }

    /**
     * @param filePath ַ
     * @param downPath
     * @return
     */
    public void sftpDownAndUp(String filePath, String downPath, String host, String port, String username, String password, Integer flag) throws Exception {//flag:0是下载，1是上传
        SftpDown test = new SftpDown();
        Map<String, String> sftpDetails = new HashMap<String, String>();
        // 设置主机ip，端口，用户名，密码
        sftpDetails.put(SFTPConstants.SFTP_REQ_HOST, host);
        sftpDetails.put(SFTPConstants.SFTP_REQ_USERNAME, username);
        sftpDetails.put(SFTPConstants.SFTP_REQ_PASSWORD, password);
        sftpDetails.put(SFTPConstants.SFTP_REQ_PORT, port);

        SFTPChannel channel = test.getSFTPChannel();
        ChannelSftp chSftp = channel.getChannel(sftpDetails, 60000);

        try {
            if (flag == 0) {
                SftpATTRS attr = chSftp.stat(filePath);
                long fileSize = attr.getSize();
                OutputStream out = new FileOutputStream(downPath);
                InputStream is = chSftp.get(filePath, new ProgressMonitor());
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
                out.close();
                is.close();
            } else {
                //上传流程
                OutputStream out = chSftp.put(filePath, ChannelSftp.OVERWRITE); // 使用OVERWRITE模式
                byte[] buff = new byte[1024 * 256]; // 设定每次传输的数据块大小为256KB
                int read;
                if (out != null) {
                    System.out.println("Start to read input stream");
                    InputStream is = new FileInputStream(downPath);
                    do {
                        read = is.read(buff, 0, buff.length);
                        if (read > 0) {
                            out.write(buff, 0, read);
                        }
                        out.flush();
                    } while (read >= 0);
                    System.out.println("input stream read done.");
                    is.close();
                }
                out.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            chSftp.quit();
            channel.closeChannel();
        }
    }
}
