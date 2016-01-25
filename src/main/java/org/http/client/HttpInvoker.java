package org.http.client;

import org.http.HttpAcceptor;
import org.http.HttpRequest;
import org.http.HttpResponseMessage;
import org.http.chain.DefaultHttpFilterChainBuilder;
import org.http.chain.HttpHandler;

/**
 * http接口调用者
 * 
 * @author zouziwen
 *
 *         2016年1月20日 下午12:58:44
 */
public abstract class HttpInvoker implements HttpAcceptor {

	private static HttpInvoker instance;

	private final HttpClientAcceptor server;

	private final DefaultHttpFilterChainBuilder filterChain;

	public static HttpInvoker getInstance() {
		return instance;
	}

	public HttpInvoker() {
		server = new HttpClientAcceptor();
		filterChain = server.getFilterChain();

		instance = getHttpInvoker();

		initFilter(filterChain);
	}

	/**
	 * 获取具体的http调用者，一般是具体实现类
	 * @return
	 * 2016年1月25日 上午11:19:02
	 */
	protected abstract HttpInvoker getHttpInvoker();

	/**
	 * 初始化过滤器操作
	 * 
	 * @param filterChain
	 *            2016年1月21日 上午10:26:49
	 */
	protected abstract void initFilter(DefaultHttpFilterChainBuilder filterChain);

	@Override
	public HttpResponseMessage service(HttpRequest request) throws Exception {
		return server.service(request);
	}

	@Override
	public HttpResponseMessage service(HttpRequest request, HttpHandler handler) throws Exception {
		return server.service(request, handler);
	}

}
