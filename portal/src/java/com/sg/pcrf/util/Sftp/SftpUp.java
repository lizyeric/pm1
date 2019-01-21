package com.sg.pcrf.util.Sftp;

/**
 * Created by Administrator on 17-8-25.
 */

import com.jcraft.jsch.ChannelSftp;
import jline.internal.Log;

import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

public class SftpUp {

    public SFTPChannel getSFTPChannel() {
        return new SFTPChannel();
    }

    /**
     * @throws Exception
     */
    public String stfUp() throws Exception {
        SftpUp test = new SftpUp();

        Map<String, String> sftpDetails = new HashMap<String, String>();
        // 设置主机ip，端口，用户名，密码
        sftpDetails.put(SFTPConstants.SFTP_REQ_HOST, "192.168.99.205");
        sftpDetails.put(SFTPConstants.SFTP_REQ_USERNAME, "root");
        sftpDetails.put(SFTPConstants.SFTP_REQ_PASSWORD, "Password1");
        sftpDetails.put(SFTPConstants.SFTP_REQ_PORT, "22");

        String src = "D:/download/temporaryLog2.txt"; // 本地文件名
        String dst = "/usr/smf/Testlog/temporaryLog2.txt"; // 目标文件名

        SFTPChannel channel = test.getSFTPChannel();
        ChannelSftp chSftp = channel.getChannel(sftpDetails, 60000);

        /**
         * 代码段1
         **/
        try {
            OutputStream out = chSftp.put(dst, ChannelSftp.OVERWRITE); // 使用OVERWRITE模式
            byte[] buff = new byte[1024 * 256]; // 设定每次传输的数据块大小为256KB
            int readStr;
            System.out.println("Start to read input stream");
            InputStream is = null;
            try {
                is = new FileInputStream(src);
                do {
                    readStr = is.read(buff, 0, buff.length);
                    if (readStr > 0) {
                        out.write(buff, 0, readStr);
                    }
                    out.flush();
                } while (readStr >= 0);
                System.out.println("input stream read done.");
            } catch (Exception e) {
                Log.error(e);
            } finally {
                if (is != null) is.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.error(e);
        }
        // chSftp.put(src, dst, ChannelSftp.OVERWRITE); // 代码段2
        // chSftp.put(new FileInputStream(src), dst, ChannelSftp.OVERWRITE); // 代码段3

        chSftp.quit();
        channel.closeChannel();
        return null;
    }
}
