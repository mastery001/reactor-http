package org.http.client;

import org.http.HttpRequest;
import org.http.chain.HttpFilterChain;
import org.http.chain.HttpHandler;
import org.http.chain.HttpService;
import org.http.chain.support.BaseHttpSession;
import org.http.chain.util.HttpSessionClosedException;

class HttpClientSession extends BaseHttpSession {

	private final HttpService service;

	private final HttpRequest httpRequest;

	private final HttpFilterChain filterChain;

	private final HttpHandler handler;

	private final HttpClientFilterProccessor filterProcessor;

	public HttpClientSession(HttpService service, HttpRequest httpRequest,
			HttpClientFilterProccessor filterProcessor, HttpHandler handler, HttpFilterChain filterChain) {
		this.service = service;
		this.httpRequest = httpRequest;
		this.filterChain = filterChain;
		this.filterProcessor = filterProcessor;
		if(handler == null) {
			handler = HttpClientFilterChain.DefaultHandler;
		}
		this.handler = handler;
	}

	@Override
	public HttpRequest getRequest() {
		return httpRequest;
	}

	@Override
	public HttpFilterChain getFilterChain() {
		if(isClose()) {
			throw new HttpSessionClosedException(getName());
		}
		return filterChain;
	}

	@Override
	public HttpService getService() {
		return service;
	}

	@Override
	public HttpHandler getHandler() {
		return handler;
	}

	HttpClientFilterProccessor getProcessor() {
		return this.filterProcessor;
	}

	@Override
	protected void close0() {
		filterChain.fireFilterClose(this);
	}

	@Override
	public String getName() {
		return getRequest().getRequestMessage().getURL();
	}
}
