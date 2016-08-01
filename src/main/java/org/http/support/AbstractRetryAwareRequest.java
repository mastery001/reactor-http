package org.http.support;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import org.http.HttpRequestRetryHandler;
import org.http.HttpRetryAware;

public abstract class AbstractRetryAwareRequest implements HttpRetryAware {

	private final AtomicBoolean retryed;
	private final AtomicReference<HttpRequestRetryHandler> handlerRef;

	public AbstractRetryAwareRequest() {
		retryed = new AtomicBoolean(false);
		this.handlerRef = new AtomicReference<HttpRequestRetryHandler>(new HttpRetryHandler());
	}

	@Override
	public boolean isRetry() {
		return this.retryed.get();
	}
	
	protected boolean retry() { 
		return this.retryed.compareAndSet(false, true);
	}
	
	public void setRetryHandler(final HttpRequestRetryHandler handler) {
		if (this.retryed.get()) {
			this.handlerRef.set(handler);
		}
	}
	
	@Override
	public HttpRequestRetryHandler retryHandler() {
		return this.handlerRef.get();
	}
}
