package org.http.client.method;

import java.io.UnsupportedEncodingException;

import org.apache.http.HttpEntity;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.methods.HttpUriRequest;
import org.http.chain.util.ExceptionMonitor;
import org.http.support.BaseHttpEntityRequest;

public class HttpPostRequest extends BaseHttpEntityRequest {

	public HttpPostRequest(String baseUrl) {
		super(baseUrl);
	}

	public HttpPostRequest(String baseUrl, HttpEntity entity) {
		this(baseUrl , entity , false);
	}

	public HttpPostRequest(String baseUrl, HttpEntity entity, boolean isRetry) {
		super(baseUrl, isRetry);
		setEntity(entity);
	}

	@Override
	public HttpUriRequest concreteRequest() {
		if (getEntity() != null) {
			return super.concreteRequest();
		}
		try {
			getRequest().setEntity(new UrlEncodedFormEntity(parameters()));
		} catch (UnsupportedEncodingException e) {
			ExceptionMonitor.getInstance().ignoreCaught(e);
		}
		return getRequest();
	}

	@Override
	protected HttpRequestBase initRequest(String baseUrl) {
		return new HttpPost(baseUrl);
	}
}
