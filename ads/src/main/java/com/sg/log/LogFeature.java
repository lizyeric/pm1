package com.sg.log;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import com.sg.common.BaseConfig;

public class LogFeature {

	public static Object write = new Object();

	public static BufferedWriter bfWriter = null;

	public static String prepareBfReader(String lastReadFile, String currentReadFile) {
		try {
			synchronized (write) {
				if (null == bfWriter || !currentReadFile.equals(lastReadFile)) {
					if (null != bfWriter) {
						bfWriter.flush();
						bfWriter.close();
						bfWriter = null;
					}
					if (null != currentReadFile && !"".equals(currentReadFile)) {
						bfWriter = new BufferedWriter(new FileWriter(BaseConfig.logPath + currentReadFile, true));
						lastReadFile = currentReadFile;
					}
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return lastReadFile;
	}

	public static void write(String info) {
		try {
			synchronized (write) {
				bfWriter.write(info);
				bfWriter.write("\r\n");
				bfWriter.flush();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
		}
	}
}
