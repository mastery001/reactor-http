package org.http.client.method;

import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.RequestEntity;

/**
 * request请求体发送逻辑
 * @author zouziwen
 *
 * 2016年1月19日 下午6:46:40
 */
public class HttpPostEntityRequest extends HttpPostRequest{

	private RequestEntity requestEntity;
	
	public HttpPostEntityRequest(String baseUrl , RequestEntity requestEntity) {
		this(baseUrl , requestEntity , false);
	}
	
	public HttpPostEntityRequest(String baseUrl , RequestEntity requestEntity , boolean isRetry) {
		super(baseUrl,isRetry);
		this.requestEntity = requestEntity;
	}

	@Override
	protected void prepareRequest(HttpMethod method ,NameValuePair[] nameValuePairs) {
		getMethod().setQueryString(nameValuePairs);
		getMethod().setRequestEntity(requestEntity);
	}
	
	
}
