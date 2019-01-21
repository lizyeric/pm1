package com.sg.pcrf.statistic.sftp

import com.jcraft.jsch.ChannelSftp
import com.sg.pcrf.statistic.util.Constants
import com.sg.pcrf.statistic.util.Utils;


public class UploadStatisticFile {
	private def prop;

	public UploadStatisticFile(Properties prop) {
		this.prop = prop;
	}

	public List<String> uploadFile() {

		// TODO Auto-generated method stub
		List<String> list = new ArrayList<String>();
		SFTP sf = new SFTP();
		ChannelSftp sftp = null;
		for (int loop = 0; loop < Integer.valueOf(prop.getProperty(Constants.MAX_CON_ATTEMPT)); loop++) {
			// create sftp connect.
			try {
				sftp = sf.connect(prop.getProperty(Constants.IP).trim(),
						Integer.valueOf(prop.getProperty(Constants.PORT).trim()),
						prop.getProperty(Constants.USERNAME).trim(), prop.getProperty(Constants.PASSWORD).trim());
				// upload bulk request file to sftp server.
			} catch (Exception e) {
				Utils.printlnInOut("uploadFile", "Can not connect sftp server." + e.getMessage());

			}
			if (sftp != null) {

				File folder = new File(Constants.localDir);
				File[] files = folder.listFiles();
				List fileNames = new ArrayList();

				boolean flag = true;
				for (File file : files) {
					if (file.isFile()) {
						fileNames.add(file.getName());
					}
				}
				if (null != fileNames && !fileNames.isEmpty()) {
					String remotePath = prop.getProperty(Constants.PATH).trim();
					for (int i = 0; i < fileNames.size(); i++) {
						String fileName = fileNames.get(i).toString();
						if (fileName.endsWith(Constants.fileType)) {

							boolean upload_status = true;
							String uploadFile = Constants.localDir + Constants.separator + fileName;

							try {
								long putStart = System.currentTimeMillis();

								sf.upload(remotePath, uploadFile, sftp, false);
								long putEnd = System.currentTimeMillis();
								Utils.printlnInOut("UploadFile",
										"Upload " + fileName + " success. Execute time :" + (putEnd - putStart));
							} catch (Exception e) {
								flag = false;
								upload_status = false;
								list.add(fileName);
								/*
								 * 4. if put files failed, send soap request to
								 * OMP
								 */

								for (int uploadLoop = 1; uploadLoop <= Integer
										.valueOf(prop.getProperty(Constants.MAX_UPLOAD_ATTEMPT)); uploadLoop++) {
									try {
										sf.upload(remotePath, uploadFile, sftp, false);
										Utils.printlnInOut("UploadFile", "Upload " + fileName + " success.");
										upload_status = true;
										list.remove(fileName);
										break;
									} catch (Exception err) {
										Utils.printlnInOut("UploadFile",
												"Upload " + fileName + " faild. Attempt: " + uploadLoop);
									}
									try {
										Thread.sleep(100);
									} catch (InterruptedException e1) {
										// TODO Auto-generated catch block
									}
								}

							}
							try {
								if (upload_status) {
									String catCommand = "mv " + Constants.localDir + Constants.separator + fileName
											+ " " + Constants.localBackupDir + Constants.separator;
									Utils.callCmd(catCommand);
								}
							} catch (Exception e) {
							}

						}

					}

				}
			} else {
				try {
					Thread.sleep(60000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return list;
	}
}
