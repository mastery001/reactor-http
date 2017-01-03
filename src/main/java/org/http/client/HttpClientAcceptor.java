package org.http.client;

import org.http.HttpClientFactory;
import org.http.HttpResponseMessage;
import org.http.chain.HttpSession;
import org.http.exception.HttpInvokeException;
import org.http.exception.HttpSessionClosedException;

/**
 * 使用线程池来实现httpClient
 * 
 * @author zouziwen
 *
 *         2016年1月19日 下午1:36:21
 */
public class HttpClientAcceptor extends AbstractHttpClientAcceptor<HttpResponseMessage> {

	//具体的处理者
	private final HttpClientProccessor processor = new HttpClientProccessor();
	
	public HttpClientAcceptor() {
		this(null);
	}
	
	public HttpClientAcceptor(HttpClientFactory httpClientFactory) {
		super(httpClientFactory);
	}

	@Override
	protected HttpResponseMessage doService(HttpSession session)
			 throws HttpSessionClosedException, HttpInvokeException {
		return processor.doWork(session.getHttpRequest(), (HttpClientSession) session, httpClientFactory);
	}

	@Override
	public void release(){
		super.release();
//		build(false);
	}

}
