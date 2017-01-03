package org.http.client;

import org.http.HttpClientFactory;
import org.http.HttpRequest;
import org.http.chain.HttpFilterChain;
import org.http.chain.HttpHandler;
import org.http.chain.HttpService;
import org.http.chain.support.BaseHttpSession;
import org.http.executor.HttpExecutor;

class HttpClientSession extends BaseHttpSession {

	private final HttpService service;

	private final HttpFilterChain filterChain;

//	private final HttpClientProccessor filterProcessor;
	
	private final HttpClientFactory httpClientFactory;
	
	private final HttpExecutor httpExecutor;
	
	private HttpRequest httpRequest;
	
	private HttpHandler handler;
	
	private Object attachment;

	public HttpClientSession(HttpService service ,HttpClientFactory httpClientFactory , HttpExecutor httpExecutor) {
		this.service = service;
		this.filterChain = new HttpClientFilterChain();
//		this.filterProcessor = filterProcessor;
		this.httpClientFactory = httpClientFactory;
		this.httpExecutor = httpExecutor;
	}

	HttpClientSession processRequest(HttpRequest httpRequest , HttpHandler handler) {
		this.httpRequest = httpRequest;
		this.handler = handler;
		return this;
	}
	
	@Override
	public HttpRequest getHttpRequest() {
		return httpRequest;
	}

	@Override
	public HttpFilterChain getFilterChain(){
		return filterChain;
	}

	public HttpService getService() {
		return service;
	}

	@Override
	public HttpHandler getHandler() {
		return handler;
	}

//	HttpClientProccessor getProcessor() {
//		return this.filterProcessor;
//	}

	@Override
	protected void close0() {
		filterChain.fireFilterClose(this);
	}

	@Override
	public String getName() {
		return getHttpRequest().getURI().toString();
	}
	
	/**
	 * 每个session可携带一个附件传递
	 * @param attachment
	 * @return
	 * 2016年1月28日 上午9:38:26
	 */
	Object setAttachment(Object attachment){
		Object oldAttachment = null;
		if(this.attachment != null) {
			oldAttachment = this.attachment;
		}
		this.attachment = attachment;
		return oldAttachment;
	}
	
	/**
	 * 得到当前的附件
	 * @return
	 * 2016年1月28日 上午9:38:41
	 */
	Object getAttachment(){
		return attachment;
	}

	@Override
	public HttpClientFactory getHttpClientFactory() {
		return httpClientFactory;
	}

	@Override
	public HttpExecutor getHttpExecutor() {
		return httpExecutor;
	}
}
