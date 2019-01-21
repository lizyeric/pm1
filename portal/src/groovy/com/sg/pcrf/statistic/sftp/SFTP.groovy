package com.sg.pcrf.statistic.sftp

import com.sg.pcrf.statistic.util.Utils

import java.nio.charset.Charset

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;


public class SFTP {

	private Session sshSession;
	ChannelExec ssh = null;
	private Session sftpSession;
	private ChannelSftp sftp = null;

	public ChannelSftp connect(String host, int port, String username, String password) {

		try {
			JSch jsch = new JSch();
			// jsch.getSession(username, host, port);
			sftpSession = jsch.getSession(username, host, port);
			Utils.printlnInOut("SFTP", "Session create " + host);
			sftpSession.setPassword(password);
			Properties sshConfig = new Properties();
			sshConfig.put("StrictHostKeyChecking", "no");
			sftpSession.setConfig(sshConfig);
			sftpSession.connect();
			Channel channel = sftpSession.openChannel("sftp");
			channel.connect();
			sftp = (ChannelSftp) channel;
			Utils.printlnInOut("SFTP", "Connected to " + host + ".");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sftp;
	}

	public void closeSftpConnect() {
		if (null != sftp) {
			sftp.quit();
			sftp.disconnect();
			sftp = null;
		}
		if (null != sftpSession) {
			sftpSession.disconnect();
			sftpSession = null;
		}
	}

	public void closeSshConnect() {
		if (null != ssh) {
			ssh.disconnect();
			ssh = null;
		}
		if (null != sshSession) {
			sshSession.disconnect();
			sshSession = null;
		}
	}

	public void upload(String directory, String uploadFile, ChannelSftp sftp, boolean delete) {
		try {
			sftp.cd(directory);
			File file = new File(uploadFile);
			FileInputStream ino = new FileInputStream(file);
			sftp.put(ino, file.getName());
			ino.close();
			if (delete) {
				file.delete();
			}
			Utils.printlnInOut("SFTP", uploadFile + " file upLoad success.");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void download(String directory, String downloadFile, String saveFile, ChannelSftp sftp) {
		try {

			sftp.cd(directory);
			sftp.get(downloadFile, saveFile);
			Utils.printlnInOut("SFTP", "File downLoad success." + saveFile);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public List downloadAllFiles(String directory, String localPath, ChannelSftp sftp) {

		List listFilesName = null;
		try {
			listFilesName = listFiles(directory, sftp);
			listFilesName.remove(0);
			listFilesName.remove(0);
			List fileNames = new ArrayList();
			for (int i = 0; i < listFilesName.size(); i++) {

				String[] f = listFilesName.get(i).toString().split(" ");
				int length = f.length;
				String fileName = f[length - 1];
				fileNames.add(fileName);
				String saveFile = localPath + Constants.separator + fileName;
				download(directory, fileName, saveFile, sftp);
			}
		} catch (SftpException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return listFilesName;
	}

	public void delete(String directory, String deleteFile) {
		try {
			File file = new File(directory + deleteFile);
			file.delete();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void delete(String directory, String deleteFile, ChannelSftp sftp) {
		try {
			sftp.cd(directory);
			sftp.rm(deleteFile);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Vector listFiles(String directory, ChannelSftp sftp) throws SftpException {

		return sftp.ls(directory);
	}

	public List listFilesName(String directory, ChannelSftp sftp) {
		List fileNames = null;
		List listFilesName = null;
		try {
			listFilesName = listFiles(directory, sftp);
			// listFilesName.remove(0);
			// listFilesName.remove(0);
			fileNames = new ArrayList();
			for (int i = 0; i < listFilesName.size(); i++) {
				String[] f = listFilesName.get(i).toString().split(" ");
				int length = f.length;
				String fileName = f[length - 1];
				fileNames.add(fileName);
				// System.out.println("fileName:" + f[length - 1]);
			}
		} catch (Exception e) {

		}
		return fileNames;
	}

	public static Properties loadPropeties(String propertyName) {
		Properties prop = new Properties();
		try {
			File f = new File(propertyName);
			FileInputStream fis = new FileInputStream(f);
			prop.load(fis);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return prop;
	}

	public ChannelExec connect_ssh(String host, int port, String username, String password) {

		try {
			JSch jsch = new JSch();
			// jsch.getSession(username, host, port);
			sshSession = jsch.getSession(username, host, port);
			sshSession.setPassword(password);
			Utils.printlnInOut("SFTP", "SSH Session created." + host);
			Properties sshConfig = new Properties();
			sshConfig.put("StrictHostKeyChecking", "no");
			sshSession.setConfig(sshConfig);
			sshSession.connect();
			Channel channel = sshSession.openChannel("exec");
			ssh = (ChannelExec) channel;
			Utils.printlnInOut("SFTP", "SSH Connected to " + host + " success.");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ssh;
	}

	public static void zipFile(String directory, String fileName, String zipName, ChannelExec ssh) {
		try {
			String dir = "cd " + directory + ";";
			String tar = "tar -zcvf " + zipName + " " + fileName + " & \n";
			String command = dir + tar;
			ssh.setCommand(command);
			ssh.setInputStream(null);
			ssh.setErrStream(System.err);

			ssh.connect();
			InputStream ino = ssh.getInputStream();
			BufferedReader reader = new BufferedReader(new InputStreamReader(ino, Charset.forName("UTF-8")));
			String buf = null;
			// ArrayList<String> usageList = new ArrayList<String>();
			while ((buf = reader.readLine()) != null) {
				// usageList.add(buf);
			}
			reader.close();
			ino.close();
			// System.out.println("SSH COMMAND RESPONSE: "+usageList);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public List<String> getFileSize(String directory, String fileName, String zipName, ChannelExec ssh, String cmd) {
		ArrayList<String> usageList = new ArrayList<String>();
		try {
			System.out.println("COMMAND: " + cmd);
			ssh.setCommand(cmd);

			ssh.setInputStream(null);
			ssh.setErrStream(System.err);

			ssh.connect();
			InputStream ino = ssh.getInputStream();
			BufferedReader reader = new BufferedReader(new InputStreamReader(ino, Charset.forName("UTF-8")));
			String buf = null;

			while ((buf = reader.readLine()) != null) {
				usageList.add(buf);
			}
			reader.close();
			ino.close();
			System.out.println("SSH COMMAND RESPONSE: " + usageList);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			ssh.disconnect();
			sshSession.disconnect();
		}
		return usageList;
	}

	// ssh login the server to execute the cmd
	public static void ssh_Cmd(String cmd, ChannelExec ssh) {
		try {
			ssh.setCommand(cmd);
			ssh.setInputStream(null);
			ssh.setErrStream(System.err);

			ssh.connect();
			InputStream ino = ssh.getInputStream();
			BufferedReader reader = new BufferedReader(new InputStreamReader(ino, Charset.forName("UTF-8")));
			String buf = null;
			// ArrayList<String> usageList = new ArrayList<String>();
			while ((buf = reader.readLine()) != null) {
				// usageList.add(buf);
			}
			reader.close();
			ino.close();
			// System.out.println("SSH COMMAND RESPONSE: "+usageList);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			Utils.printlnInOut("SFTP", e + "");
		}
	}

	public static void main(String[] args) {
	}
}
