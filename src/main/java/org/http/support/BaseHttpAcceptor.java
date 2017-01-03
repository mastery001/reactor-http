package org.http.support;

import org.http.HttpAcceptor;
import org.http.HttpClientFactory;
import org.http.HttpRequest;
import org.http.chain.HttpHandler;
import org.http.chain.HttpSession;
import org.http.chain.support.BaseHttpService;
import org.http.exception.HttpInvokeException;
import org.http.exception.HttpSessionClosedException;
import org.http.executor.GlobalHttpExecutor;
import org.http.executor.HttpExecutor;

public abstract class BaseHttpAcceptor<T> extends BaseHttpService implements HttpAcceptor<T> {
	
	/**
	 * http客户端 2016年1月18日 下午6:00:01
	 */
	protected final HttpClientFactory httpClientFactory;
	
	private HttpExecutor executor;

	public BaseHttpAcceptor() {
		this(null);
	}

	public BaseHttpAcceptor(HttpClientFactory httpClientFactory) {
		if (httpClientFactory == null) {
			httpClientFactory = GlobalHttpClientFactory.getInstance();
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

		return doService(session);
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
	protected abstract T doService(HttpSession session)
			 throws HttpSessionClosedException, HttpInvokeException ;

	public HttpClientFactory getHttpClientFactory() {
		return this.httpClientFactory;
	}

	protected HttpExecutor getExecutor() {
		if(executor == null) 
			setExecutor(GlobalHttpExecutor.getInstance());
		return executor;
	}

	protected void setExecutor(HttpExecutor executor) {
		this.executor = executor;
	}
	
}
