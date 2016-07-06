package org.http.chain.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 异常监控器，用于处理Service的异常，无法被捕获的异常
 * @author zouziwen
 *
 * 2016年1月26日 下午4:42:29
 */
public class ExceptionMonitor {
	
	private final Logger log = LoggerFactory
            .getLogger(ExceptionMonitor.class);

	
	private static ExceptionMonitor instance = new ExceptionMonitor();

	/**
	 * Returns the current exception monitor.
	 */
	public static ExceptionMonitor getInstance() {
		return instance;
	}

	private ExceptionMonitor() {
		
	}
	public static void setInstance(ExceptionMonitor monitor) {
		if (monitor != null) {
			instance = monitor;
		}
	}

	public void exceptionCaught(Throwable cause) {
		if (log.isWarnEnabled()) {
			log.warn("Unexpected exception.", cause);
		}
	}
	
	public void ignoreCaught(Throwable cause) {
		if (log.isWarnEnabled()) {
			log.warn("Ignore exception.", cause);
		}
	}
}
