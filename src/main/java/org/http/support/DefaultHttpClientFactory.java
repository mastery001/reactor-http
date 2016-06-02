package org.http.support;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.http.HttpClientFactory;

/**
 * 默认的httpClient配置 2016年1月18日 下午4:14:52
 */
public class DefaultHttpClientFactory extends HttpClientFactory {

	@Override
	protected HttpClientBuilder build(HttpClientBuilder builder) {
		RequestConfig config = RequestConfig.custom().setConnectTimeout(1000).setSocketTimeout(4000).build();
		PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();
		// Increase max total connection to 200
		cm.setMaxTotal(200);
		// Increase default max connection per route to 20
		cm.setDefaultMaxPerRoute(20);
		builder.setDefaultRequestConfig(config).setConnectionManager(cm);
		return builder;
	}

}
