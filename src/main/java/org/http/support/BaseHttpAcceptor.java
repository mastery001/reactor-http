package org.http.support;

import org.http.HttpAcceptor;
import org.http.HttpClientFactory;
import org.http.HttpRequest;
import org.http.chain.HttpHandler;
import org.http.chain.HttpSession;
import org.http.chain.support.BaseHttpService;
import org.http.chain.util.ExceptionMonitor;
import org.http.exception.HttpInvokeException;
import org.http.exception.HttpSessionClosedException;

public abstract class BaseHttpAcceptor<T> extends BaseHttpService implements HttpAcceptor<T> {

	
	/**
	 * http客户端 2016年1月18日 下午6:00:01
	 */
	protected final HttpClientFactory httpClientFactory;

	/**
	 * 判断filterChain是否build 2016年1月20日 下午1:25:48
	 */
	private volatile boolean isBuildChain;

	public BaseHttpAcceptor() {
		this(null);
	}

	public BaseHttpAcceptor(HttpClientFactory httpClientFactory) {
		if (httpClientFactory == null) {
			httpClientFactory = HttpClientFactory.DEFAULT_FACTORY;
		}
		this.httpClientFactory = httpClientFactory;
	}

	@Override
	public T service(HttpRequest request) throws HttpSessionClosedException, HttpInvokeException {
		return this.service(request, null);
	}

	@Override
	public T service(HttpRequest request, HttpHandler handler) throws HttpSessionClosedException, HttpInvokeException  {
		// 准备service
		HttpSession session = prepareService(request, handler);
		
		try {
			getFilterChainBuilder().buildFilterChain(session.getFilterChain());
			// 没有build链则build
//			if (!isBuild()) {
//				
//				getFilterChainBuilder().buildFilterChain(session.getFilterChain());
//				build(true);
//			}
		} catch (Exception e) {
			ExceptionMonitor.getInstance().exceptionCaught(e);
		}

		return doService(request, handler, session);
	}

	/**
	 * service的准备工作 2016年1月20日 下午1:09:57
	 */
	protected abstract HttpSession prepareService(HttpRequest request, HttpHandler handler)  throws HttpSessionClosedException, HttpInvokeException ;

	/**
	 * 具体的service的实现者
	 * 
	 * @param request
	 * @param handler
	 * @param session
	 * @return 2016年1月20日 下午1:45:35
	 * @throws Exception
	 */
	protected abstract T doService(HttpRequest request, HttpHandler handler, HttpSession session)
			 throws HttpSessionClosedException, HttpInvokeException ;


	@Deprecated
	public boolean isBuild() {
		return isBuildChain;
	}

	@Deprecated
	public void build(boolean build) {
		isBuildChain = build;
	}

	@Override
	public HttpClientFactory getHttpClientFactory() {
		return this.httpClientFactory;
	}
	
}
