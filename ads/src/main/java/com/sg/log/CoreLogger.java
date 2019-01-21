package com.sg.log;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sg.common.Utils;

public class CoreLogger {

	public static final String LOGBACK_MARK = "LOG_CONFIG_";

	private Logger logger = null;
	private Logger errLogger = null;

	public CoreLogger() {
		super();
		// TODO Auto-generated constructor stub
		initLogger();
	}

	public void initLogger() {
		setLogger(LoggerFactory.getLogger("adsInfo"));
		setErrLogger(LoggerFactory.getLogger("adsErr"));
	}

	public Logger getLogger() {
		return logger;
	}

	public void setLogger(Logger logger) {
		this.logger = logger;
	}

	/**
	 * System and business error logger.
	 * 
	 * @return
	 */
	public Logger getErrLogger() {
		return errLogger;
	}

	public void setErrLogger(Logger errLogger) {
		this.errLogger = errLogger;
	}

	/**
	 * Error log.
	 */
	public void logErr(String message) {
		getErrLogger().error(message);
	}

	/**
	 * Error log.
	 */
	public void logErr(Exception e) {
		getErrLogger().error(Utils.printExceptionToString(e));
	}

	public static void main(String[] args) {
		CoreLogger log = new CoreLogger();
		log.getLogger().info("a");
	}
}
