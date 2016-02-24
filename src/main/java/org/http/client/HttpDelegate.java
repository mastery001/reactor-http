package org.http.client;

import java.util.concurrent.atomic.AtomicInteger;

import org.http.HttpAcceptor;
import org.http.HttpRequest;
import org.http.HttpResponseMessage;
import org.http.chain.DefaultHttpFilterChainBuilder;
import org.http.chain.HttpFilter;
import org.http.chain.HttpHandler;
import org.http.exception.HttpInvokeException;
import org.http.exception.HttpSessionClosedException;

/**
 * http代理请求发送者
 * @author zouziwen
 *
 * 2016年1月20日 上午11:24:51
 */
public class HttpDelegate implements HttpAcceptor<HttpResponseMessage>{

	public static final HttpDelegate Proxy = new HttpDelegate();
	
	private final HttpClientAcceptor server;
	
	private final DefaultHttpFilterChainBuilder filterChain;

	private static final String FILTER_PREFIX = "FILTER_";
	
	private AtomicInteger index = new AtomicInteger();
	
	private HttpDelegate(){
		server = new HttpClientAcceptor();
		filterChain = server.getFilterChain();
	}

	public HttpDelegate prepareFilter(HttpFilter filter) {
		filterChain.addLast(FILTER_PREFIX + index.incrementAndGet(), filter);
		return this;
	}

	@Override
	public HttpResponseMessage service(HttpRequest request)  throws HttpSessionClosedException, HttpInvokeException  {
		return server.service(request);
	}

	@Override
	public HttpResponseMessage service(HttpRequest request, HttpHandler handler) throws HttpSessionClosedException, HttpInvokeException  {
		return server.service(request,handler);
	}
	
}
