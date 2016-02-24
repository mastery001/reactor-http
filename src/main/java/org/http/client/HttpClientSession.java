package org.http.client;

import org.http.HttpRequestMessage;
import org.http.chain.HttpFilterChain;
import org.http.chain.HttpHandler;
import org.http.chain.HttpService;
import org.http.chain.support.BaseHttpSession;

class HttpClientSession extends BaseHttpSession {

	private final HttpService service;

	private final HttpRequestMessage httpRequestMessage;

	private final HttpFilterChain filterChain;

	private final HttpHandler handler;

//	private final HttpClientProccessor filterProcessor;
	
	private Object attachment;

	public HttpClientSession(HttpService service, HttpRequestMessage httpRequestMessage, HttpHandler handler, HttpFilterChain filterChain) {
		this.service = service;
		this.httpRequestMessage = httpRequestMessage;
		this.filterChain = filterChain;
//		this.filterProcessor = filterProcessor;
		this.handler = handler;
	}

	@Override
	public HttpRequestMessage getRequestMessage() {
		return httpRequestMessage;
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
		return getRequestMessage().getURL();
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
}
