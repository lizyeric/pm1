package at.gaosheng.sftp;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Vector;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;

import at.gaosheng.util.Utils;

public class SFTP {

	private Session sshSession;
	ChannelExec ssh = null;
	private Session sftpSession;
	private ChannelSftp sftp = null;
	/**
	 * 连接sftp服务器
	 * 
	 * @param host
	 *            主机
	 * @param port
	 * 
	 *            端口
	 * @param username
	 *            用户名
	 * @param password
	 * 
	 *            密码
	 * @return
	 */

	public ChannelSftp connect(String host, int port, String username,
			String password) {
		
		try {
			JSch jsch = new JSch();
//			jsch.getSession(username, host, port);
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

	public void closeSftpConnect(){
		if(null != sftp){
			sftp.quit();
			sftp.disconnect();
			sftp = null;
		}
		if(null!=sftpSession){
			sftpSession.disconnect();
			sftpSession = null;
		}
	}
	
	public void closeSshConnect(){
		if(null != ssh){
			ssh.disconnect();
			ssh = null;
		}
		if(null!=sshSession){
			sshSession.disconnect();
			sshSession = null;
		}
	}
	/**
	 * 上传文件
	 * 
	 * @param directory
	 *            上传的目录
	 * @param uploadFile
	 *            要上传的文件
	 * @param sftp
	 */
	public void upload(String directory, String uploadFile, ChannelSftp sftp,boolean delete) {
		try {
			sftp.cd(directory);
			File file = new File(uploadFile);
			FileInputStream in = new FileInputStream(file);
			sftp.put(in, file.getName());
			in.close();
			if(delete){
				file.delete();
			}
			Utils.printlnInOut("SFTP", uploadFile+" file upLoad success.");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 下载文件
	 * 
	 * @param directory
	 *            下载目录
	 * @param downloadFile
	 *            下载的文件
	 * @param saveFile
	 *            存在本地的路径
	 * @param sftp
	 */
	public void download(String directory, String downloadFile,
			String saveFile, ChannelSftp sftp) {
		try {

			sftp.cd(directory);
			sftp.get(downloadFile, saveFile);
			Utils.printlnInOut("SFTP", "File downLoad success."+saveFile);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public List downloadAllFiles(String directory, String localPath,
			ChannelSftp sftp) {

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
				String saveFile = localPath + Utils.sf + fileName;
				download(directory, fileName, saveFile, sftp);
			}
		} catch (SftpException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return listFilesName;
	}

	/**
	 * 删除文件
	 * 
	 * @param directory
	 *            要删除文件所在目录
	 * @param deleteFile
	 *            要删除的文件
	 * @param sftp
	 */
	public void delete(String directory, String deleteFile) {
		try {
			File file = new File(directory + deleteFile);
			file.delete();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 删除文件
	 * 
	 * @param directory
	 *            要删除文件所在目录
	 * @param deleteFile
	 *            要删除的文件
	 * @param sftp
	 */
	public void delete(String directory, String deleteFile, ChannelSftp sftp) {
		try {
			sftp.cd(directory);
			sftp.rm(deleteFile);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 列出目录下的文件
	 * 
	 * @param directory
	 *            要列出的目录
	 * @param sftp
	 * @return
	 * @throws SftpException
	 */
	public Vector listFiles(String directory, ChannelSftp sftp)
			throws SftpException {

		return sftp.ls(directory);
	}

	public List listFilesName(String directory, ChannelSftp sftp) {
		List fileNames = null;
		List listFilesName = null;
		try {
			listFilesName = listFiles(directory, sftp);
			//listFilesName.remove(0);
			//listFilesName.remove(0);
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

	public ChannelExec connect_ssh(String host, int port, String username,
			String password) {
		
		try {
			JSch jsch = new JSch();
//			jsch.getSession(username, host, port);
			sshSession = jsch.getSession(username, host,port);
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

	public static void zipFile(String directory, String fileName,
			String zipName, ChannelExec ssh) {
		try {
			String dir = "cd "+directory+";";
			String tar = "tar -zcvf "+zipName+" "+fileName+" & \n";
			String command = dir+tar;
			ssh.setCommand(command);
			ssh.setInputStream(null);  
			ssh.setErrStream(System.err);  
              
            ssh.connect();  
            InputStream in = ssh.getInputStream();  
            BufferedReader reader = new BufferedReader(new InputStreamReader(in, Charset.forName("UTF-8")));  
            String buf = null;  
            //ArrayList<String> usageList = new ArrayList<String>();
            while ((buf = reader.readLine()) != null){  
                //usageList.add(buf);
            }  
            reader.close();  
            in.close();
            //System.out.println("SSH COMMAND RESPONSE: "+usageList);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
public List<String> getFileSize(String directory, String fileName,
		String zipName, ChannelExec ssh,String cmd){
	 ArrayList<String> usageList = new ArrayList<String>();
	 try {
		System.out.println("COMMAND: "+cmd);
		ssh.setCommand(cmd);
		
		ssh.setInputStream(null);  
		ssh.setErrStream(System.err);  
          
        ssh.connect();  
        InputStream in = ssh.getInputStream();  
        BufferedReader reader = new BufferedReader(new InputStreamReader(in, Charset.forName("UTF-8")));  
        String buf = null;  
       
        while ((buf = reader.readLine()) != null){  
            usageList.add(buf);
        }  
        reader.close();  
        in.close();
        System.out.println("SSH COMMAND RESPONSE: "+usageList);
       
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}finally{
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
			InputStream in = ssh.getInputStream();
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					in, Charset.forName("UTF-8")));
			String buf = null;
			// ArrayList<String> usageList = new ArrayList<String>();
			while ((buf = reader.readLine()) != null) {
				// usageList.add(buf);
			}
			reader.close();
			in.close();
			// System.out.println("SSH COMMAND RESPONSE: "+usageList);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			Utils.printlnInOut("SFTP", e + "");
		}
	}

	public static void main(String[] args) {
	}
}
