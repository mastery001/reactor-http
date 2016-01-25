package org.http.client.method;

import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.http.support.BaseHttpRequest;

/**
 * http get请求
 * @author zouziwen
 *
 * 2016年1月19日 下午6:47:16
 */
public class HttpGetRequest extends BaseHttpRequest{
	
	/**
	 * @see {@link BaseHttpRequest } constructor
	 * @param baseUrl
	 * 2016年1月25日 下午4:08:55
	 */
	public HttpGetRequest(String baseUrl) {
		super(baseUrl);
	}

	/**
	 * @see {@link BaseHttpRequest } constructor
	 * @param baseUrl
	 * @param isRetry
	 * 2016年1月25日 下午4:12:45
	 */
	public HttpGetRequest(String baseUrl, boolean isRetry) {
		super(baseUrl, isRetry);
	}

	@Override
	protected HttpMethod innerInitMethod() {
		GetMethod method = new GetMethod(baseUrl);
		method.setRequestHeader("Connection", "close");
		return method;
	}

}
