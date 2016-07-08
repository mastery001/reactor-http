package org.http.support;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.http.HttpClientFactory;

import httpx.conn.IdleConnectionMonitorThread;

/**
 * 默认的httpClient配置 2016年1月18日 下午4:14:52
 */
public class DefaultHttpClientFactory extends HttpClientFactory {

	@Override
	protected HttpClientBuilder build(HttpClientBuilder builder) {
		RequestConfig config = initConfig(RequestConfig.custom());
		HttpClientConnectionManager cm = initConnectionManager();
		builder.setDefaultRequestConfig(config).setConnectionManager(cm);
		return builder;
	}

	/**
	 * 初始化连接池管理器
	 * @return
	 * 2016年7月8日 下午1:19:39
	 */
	private HttpClientConnectionManager initConnectionManager() {
		PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();
		// Increase max total connection to 200
		cm.setMaxTotal(200);
		// Increase default max connection per route to 20
		cm.setDefaultMaxPerRoute(20);
		// 开启自动清理无效连接的线程
		new IdleConnectionMonitorThread(cm).start();
		return cm;
	}
	
	/**
	 * 初始化配置
	 * @param configBuilder
	 * @return
	 * 2016年7月8日 下午1:19:33
	 */
	protected RequestConfig initConfig(RequestConfig.Builder configBuilder) {
		return configBuilder.setConnectTimeout(1000).setSocketTimeout(4000).build();
	}

}
