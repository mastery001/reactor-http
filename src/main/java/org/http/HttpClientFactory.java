package org.http;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;

/**
 * HttpClient的工厂
 * @author zouziwen
 *
 * 2016年1月18日 下午4:15:26
 */
public abstract class HttpClientFactory {

	private final HttpClientBuilder builder = HttpClients.custom();
	
	private final HttpClient httpClient;
	
	public HttpClientFactory() {
		// 默认取消重试
		httpClient = build(builder).disableAutomaticRetries().build();
	}
	
	protected abstract HttpClientBuilder build(HttpClientBuilder builder);
	
	/**
	 * 获取HttpClient的连接
	 * @return
	 * 2016年1月18日 下午4:15:02
	 */
	public HttpClient getConnection() {
		return httpClient;
	}
}
