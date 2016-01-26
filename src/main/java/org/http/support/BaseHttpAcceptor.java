package org.http.support;

import org.apache.commons.httpclient.HttpClient;
import org.http.HttpAcceptor;
import org.http.HttpClientFactory;
import org.http.HttpRequest;
import org.http.HttpResponseMessage;
import org.http.chain.HttpHandler;
import org.http.chain.HttpSession;
import org.http.chain.support.BaseHttpService;
import org.http.exception.HttpInvokeException;
import org.http.exception.HttpSessionClosedException;

public abstract class BaseHttpAcceptor extends BaseHttpService implements HttpAcceptor {

	/**
	 * http客户端 2016年1月18日 下午6:00:01
	 */
	protected final HttpClient httpClient;

	public BaseHttpAcceptor() {
		this(HttpClientFactory.DEFAULT_FACTORY);
	}

	public BaseHttpAcceptor(HttpClientFactory httpClientFactory) {
		this.httpClient = httpClientFactory.getConnection();
	}

	@Override
	public HttpResponseMessage service(HttpRequest request) throws HttpSessionClosedException, HttpInvokeException {
		return this.service(request, null);
	}

	@Override
	public HttpResponseMessage service(HttpRequest request, HttpHandler handler) throws HttpSessionClosedException, HttpInvokeException  {
		// 准备service
		HttpSession session = prepareService(request, handler);

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
	protected abstract HttpResponseMessage doService(HttpRequest request, HttpHandler handler, HttpSession session)
			 throws HttpSessionClosedException, HttpInvokeException ;

}
