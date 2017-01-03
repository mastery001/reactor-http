package org.http.client;

import org.http.HttpClientFactory;
import org.http.HttpRequest;
import org.http.chain.HttpHandler;
import org.http.chain.HttpSession;
import org.http.chain.util.ExceptionMonitor;
import org.http.exception.HttpInvokeException;
import org.http.exception.HttpSessionClosedException;
import org.http.support.BaseHttpAcceptor;

  
/**  
 * 抽象HttpAcceptor ， 使用ThreadLocal对象
 *@Description:  
 *@Author:zouziwen
 *@Since:2017年1月3日  
 *@Version:1.1.0  
 */
public abstract class AbstractHttpClientAcceptor<T> extends BaseHttpAcceptor<T> {

	/**
	 * 使用ThreadLocal防止多次创建HttpClientSession对象
	 */
	private final ThreadLocal<HttpClientSession> sessions;

	public AbstractHttpClientAcceptor() {
		this(null);
	}

	public AbstractHttpClientAcceptor(HttpClientFactory httpClientFactory) {
		super(httpClientFactory);
		sessions = new ThreadLocal<HttpClientSession>();
	}

	@Override
	protected HttpSession prepareService(HttpRequest request, HttpHandler handler)
			throws HttpSessionClosedException, HttpInvokeException {
		HttpClientSession session = sessions.get();
		if(session == null) {
			// 创建session
			session = new HttpClientSession(this , httpClientFactory , getExecutor());
			
			try {
				getFilterChainBuilder().buildFilterChain(session.getFilterChain());
			} catch (Exception e) {
				ExceptionMonitor.getInstance().exceptionCaught(e);
			}
			
			sessions.set(session);
		}
		session.processRequest(request, handler);
		return session;
	}

}
