package org.http;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;

/**
 * HttpClient的工厂
 * @author zouziwen
 *
 * 2016年1月18日 下午4:15:26
 */
public interface HttpClientFactory {

	/**
	 * 默认的httpClient配置
	 * 2016年1月18日 下午4:14:52
	 */
	HttpClientFactory DEFAULT_FACTORY = new HttpClientFactory() {
		final HttpClient httpClient;
		
		{
			MultiThreadedHttpConnectionManager connectionManager = new MultiThreadedHttpConnectionManager();
			connectionManager.getParams().setConnectionTimeout(1000);
			connectionManager.getParams().setSoTimeout(4000);
			connectionManager.getParams().setDefaultMaxConnectionsPerHost(5);
			httpClient = new HttpClient(connectionManager);
		}
		
		@Override
		public HttpClient getConnection() {
			return httpClient;
		}

	};

	/**
	 * 获取HttpClient的连接
	 * @return
	 * 2016年1月18日 下午4:15:02
	 */
	HttpClient getConnection();
}
