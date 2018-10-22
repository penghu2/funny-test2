package org.clearfuny.funnytest.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Reporter;

public class LogUtil {

	public final static Logger TEST_LOGGER = LoggerFactory.getLogger("test-logger");

			
	public static void info(String message) {
		Reporter.log(message);
		if (TEST_LOGGER.isInfoEnabled()) {
			TEST_LOGGER.info(message);
		}
	}

	public static void info(String format, Object... args) {
		String msg = String.format(format, args);
		Reporter.log(msg);
		if (TEST_LOGGER.isInfoEnabled()) {
			TEST_LOGGER.info(msg);
		}
	}
	
	public static void debug(String message) {
		
		if (TEST_LOGGER.isDebugEnabled()) {
			TEST_LOGGER.debug(message);
		}
	}
	
	public static void error(String message) {
        Reporter.log(message);
		if (TEST_LOGGER.isErrorEnabled()) {
			TEST_LOGGER.error(message);
		}
	}
}
