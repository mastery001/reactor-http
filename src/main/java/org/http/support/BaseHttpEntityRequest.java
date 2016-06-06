package org.http.support;

import org.apache.http.HttpEntity;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;

public abstract class BaseHttpEntityRequest extends BaseHttpRequest implements HttpEntityEnclosingRequest{

	public BaseHttpEntityRequest(String baseUrl) {
		super(baseUrl);
	}
	
	public BaseHttpEntityRequest(String baseUrl, boolean isRetry) {
		super(baseUrl, isRetry);
	}

	@Override
	protected HttpEntityEnclosingRequestBase getRequest() {
		return (HttpEntityEnclosingRequestBase) super.getRequest();
	}

	@Override
	public boolean expectContinue() {
		return getRequest().expectContinue();
	}

	@Override
	public void setEntity(HttpEntity entity) {
		getRequest().setEntity(entity);
	}

	@Override
	public HttpEntity getEntity() {
		return getRequest().getEntity();
	}

}
