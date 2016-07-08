package org.http.client.method;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.http.support.BaseHttpEntityRequest;

public class HttpPostRequest extends BaseHttpEntityRequest{

	public HttpPostRequest(String baseUrl) {
		super(baseUrl);
	}
	
	public HttpPostRequest(String baseUrl , HttpEntity entity) {
		this(baseUrl);
		setEntity(entity);
	}

	public HttpPostRequest(String baseUrl, boolean isRetry) {
		super(baseUrl, isRetry);
	}

	@Override
	protected HttpRequestBase initRequest(String baseUrl) {
		return new HttpPost(baseUrl);
	}

}
