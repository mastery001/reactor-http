package org.http.client.method;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpRequestBase;
import org.http.support.BaseHttpRequest;

public class HttpGetRequest extends BaseHttpRequest{

	public HttpGetRequest(String baseUrl) {
		super(baseUrl);
	}
	
	public HttpGetRequest(String baseUrl, boolean isRetry) {
		super(baseUrl, isRetry);
	}

	@Override
	protected HttpRequestBase initRequest(String baseUrl) {
		return new HttpGet(baseUrl);
	}

}
