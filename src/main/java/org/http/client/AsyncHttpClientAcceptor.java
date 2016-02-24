package org.http.client;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.http.HttpClientFactory;
import org.http.HttpRequest;
import org.http.HttpResponseMessage;
import org.http.chain.HttpFilterChain;
import org.http.chain.HttpHandler;
import org.http.chain.HttpSession;
import org.http.exception.HttpInvokeException;
import org.http.exception.HttpSessionClosedException;
import org.http.support.BaseHttpAcceptor;

/**
 * 暂时未开发完
 * 
 * @author zouziwen
 *
 *         2016年2月24日 上午11:36:13
 */
public class AsyncHttpClientAcceptor extends BaseHttpAcceptor<Future<HttpResponseMessage>> {

	@SuppressWarnings("unused")
	private final ExecutorService executor;

	/**
	 * 处理器个数，最大线程数, 2016年1月19日 下午1:36:56
	 */
	private final int processorCount;

	private int processorDistributor = 0;

	/**
	 * @noinspection StaticNonFinalField
	 */
	private static volatile int nextId = 0;

	private final int id = nextId++;

	protected HttpFilterChain filterChain;

	/**
	 * 预留http处理者的数组，可能以后会修改成异步调用http的方式 2016年1月26日 下午5:11:39
	 */
	private final AyncHttpClientProccessor httpProcessor[];

	public AsyncHttpClientAcceptor() {
		this(null);
	}

	public AsyncHttpClientAcceptor(HttpClientFactory httpClientFactory) {
		this(null, 5, Executors.newCachedThreadPool());
	}

	public AsyncHttpClientAcceptor(HttpClientFactory httpClientFactory, int processorCount, ExecutorService executor) {
		super(httpClientFactory);
		if (processorCount < 1) {
			throw new IllegalArgumentException("Must have at least one processor");
		}
		if (executor == null) {
			executor = Executors.newCachedThreadPool();
		}
		this.executor = executor;
		this.processorCount = processorCount;
		filterChain = new HttpClientFilterChain();
		httpProcessor = new AyncHttpClientProccessor[this.processorCount];
		for (int i = 0; i < processorCount; i++) {
			httpProcessor[i] = new AyncHttpClientProccessor("AyncHttpClientFilterProccessor-" + id + "." + i, executor);
		}
	}

	/**
	 * 得到处理器
	 * 
	 * @return 2016年1月19日 下午1:54:10
	 */
	private AyncHttpClientProccessor getProcessor() {
		if (this.processorDistributor == Integer.MAX_VALUE) {
			this.processorDistributor = Integer.MAX_VALUE % this.processorCount;
		}

		return httpProcessor[processorDistributor++ % processorCount];
	}

	@Override
	protected HttpSession prepareService(HttpRequest request, HttpHandler handler)
			throws HttpSessionClosedException, HttpInvokeException {
		// 创建session
		HttpClientSession session = new HttpClientSession(this, request.getRequestMessage(), handler, filterChain);
		return session;
	}

	@Override
	protected Future<HttpResponseMessage> doService(HttpRequest request, HttpHandler handler, HttpSession session)
			throws HttpSessionClosedException, HttpInvokeException {
		AyncHttpClientProccessor processor = getProcessor();
		return processor.doWork(request, (HttpClientSession) session, httpClientFactory);
	}

}
