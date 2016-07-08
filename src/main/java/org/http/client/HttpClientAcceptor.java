package org.http.client;

import org.http.HttpClientFactory;
import org.http.HttpRequest;
import org.http.HttpResponseMessage;
import org.http.chain.HttpHandler;
import org.http.chain.HttpSession;
import org.http.exception.HttpInvokeException;
import org.http.exception.HttpSessionClosedException;
import org.http.support.BaseHttpAcceptor;

/**
 * 使用线程池来实现httpClient
 * 
 * @author zouziwen
 *
 *         2016年1月19日 下午1:36:21
 */
public class HttpClientAcceptor extends BaseHttpAcceptor<HttpResponseMessage> {

//	protected HttpFilterChain filterChain;
	
	//具体的处理者
	private final HttpClientProccessor processor = new HttpClientProccessor();

	public HttpClientAcceptor() {
		this(null);
	}
	
	public HttpClientAcceptor(HttpClientFactory httpClientFactory) {
		super(httpClientFactory);
//		filterChain = new HttpClientFilterChain();
	}

	@Override
	protected HttpResponseMessage doService(HttpRequest request, HttpHandler handler, HttpSession session)
			 throws HttpSessionClosedException, HttpInvokeException {
		return processor.doWork(request, (HttpClientSession) session, httpClientFactory);
	}

	@Override
	protected HttpClientSession prepareService(HttpRequest request, HttpHandler handler) throws HttpSessionClosedException, HttpInvokeException  {

		// 创建session
		HttpClientSession session = new HttpClientSession(this, request, handler , httpClientFactory , getExecutor());
		
		return session;
	}
	
	@Override
	public void release(){
		super.release();
//		build(false);
	}

}
