package org.http;

/**
 * http重试
 * @author zouziwen
 *
 */
public interface HttpRetryAware {
	
	/**
	 * 是否重试
	 * @return
	 */
	boolean isRetry();
	
	/**
	 * 重试的具体实施策略
	 * @param handler
	 */
	HttpRequestRetryHandler retryHandler();
}
