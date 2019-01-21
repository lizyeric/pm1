package at.gaosheng.sftp;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.ChannelSftp;

import at.gaosheng.util.Utils;

public class DownloadFileThread extends Thread {

	private Properties prop;
	private boolean isConnector = false;
	public static boolean unzipFlag = false;

	public DownloadFileThread(Properties prop) {
		this.prop = prop;
	}

	@Override
	public void run() {
		unzipFlag = false;
		SFTP sf = new SFTP();
		ChannelSftp sftp = null;
		ChannelExec ssh = null;

		int count = 0;
		int timeOut = 0;
		while (true) {
			String saveFilePath = prop.getProperty("DOWNLOAD_DIR").trim() + Utils.sf;
			boolean flag = false;
			if (!isConnector) {
				try {
					// create sftp connect.
					sftp = sf.connect(prop.getProperty("IP_SFTP").trim(),
							Integer.valueOf(prop.getProperty("PORT_SFTP").trim()),
							prop.getProperty("USERNAME_SFTP").trim(), prop.getProperty("PASSWORD_SFTP").trim());
					ssh = sf.connect_ssh(prop.getProperty("IP_SFTP").trim(),
							Integer.valueOf(prop.getProperty("PORT_SFTP").trim()),
							prop.getProperty("USERNAME_SSH").trim(), prop.getProperty("PASSWORD_SSH").trim());

					count++;
				} catch (Exception e) {
					Utils.printlnInOut("DownloadFileThread", "Can not connect sftp server." + e.getMessage());
				}
			}

			if (null != sftp && sftp.isConnected() && null != ssh && !ssh.isClosed()) {
				count = 0;
				isConnector = true;
				// change DOWNLOAD_FILE SFTP TO UPLOAD_FILE_SFTP
				SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
				String dateString = formatter.format(new Date());
				String download_file_prefix = prop.getProperty("FILE_PREFIX").trim() + dateString;
				Utils.printlnInOut("DownloadFileThread", "Download file prefix: " + download_file_prefix + "  |||");

				List fileNames = sf.listFilesName(
						prop.getProperty("DOWNLOAD_FILE_PATH_SFTP").trim() + Utils.sf + download_file_prefix + "*",
						sftp);

				if (null != fileNames && !fileNames.isEmpty()) {
					String downloadFileName = (String) fileNames.get(fileNames.size() - 1);
					// if (fileNames.get(i).toString()
					// .equals(prop.getProperty("FILE_PREFIX").trim() +
					// dateString + Utils.exml)) {

					// execute zip command
					Utils.printlnInOut("DownloadFileThread", "Start zipFile " + downloadFileName + " on server.");

					long zipFileStart = System.currentTimeMillis();
					sf.zipFile(prop.getProperty("DOWNLOAD_FILE_PATH_SFTP").trim(), downloadFileName,
							downloadFileName + Utils.tar, ssh);

					long zipFileEnd = System.currentTimeMillis();

					try {
						if (null != prop.getProperty("ZIP_MAX_TIME")
								&& !"".equals(prop.getProperty("ZIP_MAX_TIME").trim())) {
							Thread.sleep(Long.valueOf(prop.getProperty("ZIP_MAX_TIME")) * 1000);
						} else {
							Thread.sleep(600000);
						}
						if (null == ssh || !ssh.isConnected()) {
							try {
								// create sftp connect.
								sftp = sf.connect(prop.getProperty("IP_SFTP").trim(),
										Integer.valueOf(prop.getProperty("PORT_SFTP").trim()),
										prop.getProperty("USERNAME_SFTP").trim(),
										prop.getProperty("PASSWORD_SFTP").trim());
								ssh = sf.connect_ssh(prop.getProperty("IP_SFTP").trim(),
										Integer.valueOf(prop.getProperty("PORT_SFTP").trim()),
										prop.getProperty("USERNAME_SSH").trim(),
										prop.getProperty("PASSWORD_SSH").trim());

								count++;
							} catch (Exception e) {
								Utils.printlnInOut("DownloadFileThread",
										"Can not connect sftp server." + e.getMessage());
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
					}

					Utils.printlnInOut("DownloadFileThread",
							"Completed zip file onserver. Execute time :" + (zipFileEnd - zipFileStart));
					// execute download
					long downloadStart = System.currentTimeMillis();
					Utils.printlnInOut("DownloadFileThread File Name", downloadFileName + Utils.tar);
					sf.download(prop.getProperty("DOWNLOAD_FILE_PATH_SFTP").trim(), downloadFileName + Utils.tar,
							saveFilePath + downloadFileName + Utils.tar, sftp);
					sf.delete(prop.getProperty("DOWNLOAD_FILE_PATH_SFTP".trim()), downloadFileName + Utils.tar, sftp);
					sf.delete(prop.getProperty("DOWNLOAD_FILE_PATH_SFTP".trim()), downloadFileName, sftp);
					long downloadEnd = System.currentTimeMillis();
					Utils.printlnInOut("DownloadFileThread",
							"Download File from success. Execute time :" + (downloadEnd - downloadStart));
					timeOut = 0;
					flag = true;

					// unzip spml file in local.
					saveFilePath = saveFilePath + downloadFileName + Utils.tar;

					long uzipFileStart = System.currentTimeMillis();
					String tarUzip = "tar -zxvf " + prop.getProperty("DOWNLOAD_DIR").trim() + Utils.sf
							+ downloadFileName + Utils.tar + " -C " + prop.getProperty("DOWNLOAD_DIR").trim();
					Utils.printlnInOut("DownloadFileThread", "Start tar unzip command: " + tarUzip);
					Utils.callCmd(tarUzip);
					try {
						sleep(30000);
					} catch (InterruptedException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					long uzipFileEnd = System.currentTimeMillis();
					Utils.printlnInOut("DownloadFileThread",
							"Completed unzip command in local dir. Execute time :" + (uzipFileEnd - uzipFileStart));

					// mv spml zip file to result directory.
					String mvCommand = "mv " + prop.getProperty("DOWNLOAD_DIR").trim() + Utils.sf + downloadFileName
							+ Utils.tar + " "
							// change backup dir for pgw file
							+ prop.getProperty("BACKUP_DIR").trim() + Utils.sf;

					Utils.callCmd(mvCommand);

					try {
						sf.closeSshConnect();
						sf.closeSftpConnect();
						sftp.quit();
						sftp.disconnect();
						ssh.disconnect();
						sftp = null;
						ssh = null;
						isConnector = false;
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					break;

				} else {

					Utils.printlnInOut("DownloadFileThread", "Remote file don't exist.");

					try {
						sf.closeSshConnect();
						sf.closeSftpConnect();
						sftp.quit();
						sftp.disconnect();
						ssh.disconnect();
						sftp = null;
						ssh = null;
						isConnector = false;
						if ((Float.valueOf(timeOut) / 3600) > (Integer
								.valueOf(prop.getProperty("CHECK_FILE_TIMEOUT").trim()))) {
							Utils.printlnInOut("DownloadFileThread",
									"Check file Timeout, DownloadFileThread close success.");
							break;
						}
						sleep(Integer.valueOf(prop.getProperty("CHECK_FILE_SLEEP").trim()) * 1000);
						timeOut += Integer.valueOf(prop.getProperty("CHECK_FILE_SLEEP").trim());
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

			} else {

				if (count == Integer.valueOf(prop.getProperty("RECONMAXTIMES").trim())) {
					Utils.printlnInOut("DownloadFileThread", "Connector times is " + count
							+ " greater than RECONMAXTIMES; DownloadFileThread close success.");

					break;
				} else {
					try {
						isConnector = false;
						Utils.printlnInOut("DownloadFileThread", "sftp or ssh is disconnect");
						sleep(60000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

			}
		}
		Utils.printlnInOut("DownloadFileThread", "Download File End.");
	}


}
