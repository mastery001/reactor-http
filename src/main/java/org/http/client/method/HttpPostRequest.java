package org.http.client.method;

import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.http.support.BaseHttpRequest;

/**
 * http的post请求
 * @author zouziwen
 *
 * 2016年1月19日 下午6:47:30
 */
public class HttpPostRequest extends BaseHttpRequest{

	/**
	 * @see {@link BaseHttpRequest } constructor
	 * @param baseUrl
	 * 2016年1月25日 下午4:12:57
	 */
	public HttpPostRequest(String baseUrl) {
		super(baseUrl);
	}

	/**
	 * @see {@link BaseHttpRequest } constructor
	 * @param baseUrl
	 * @param isRetry
	 * 2016年1月25日 下午4:13:01
	 */
	public HttpPostRequest(String baseUrl, boolean isRetry) {
		super(baseUrl, isRetry);
	}

	@Override
	protected HttpMethod innerInitMethod() {
		PostMethod method = new PostMethod(baseUrl);
		method.setRequestHeader("Connection", "close");
		return method;
	}

	@Override
	protected void prepareRequest(HttpMethod method ,NameValuePair[] nameValuePairs) {
		PostMethod post = getMethod();
		post.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, DEFAULT_CHARET);
		post.setRequestBody(nameValuePairs);
	}
	
	@Override
	public PostMethod getMethod() {
		return (PostMethod)super.getMethod();
	}
	
}
