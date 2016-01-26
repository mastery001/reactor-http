package org.http.client;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.http.HttpClientFactory;
import org.http.HttpRequest;
import org.http.HttpResponseMessage;
import org.http.chain.HttpFilterChain;
import org.http.chain.HttpHandler;
import org.http.chain.HttpSession;
import org.http.exception.HttpInvokeException;
import org.http.exception.HttpSessionClosedException;
import org.http.support.BaseHttpAcceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 使用线程池来实现httpClient
 * 
 * @author zouziwen
 *
 *         2016年1月19日 下午1:36:21
 */
public class HttpClientAcceptor extends BaseHttpAcceptor {

	private final Logger logger = LoggerFactory.getLogger(HttpClientAcceptor.class);

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

	private final HttpClientFilterProccessor httpProcessor[];

	/**
	 * 判断filterChain是否build 2016年1月20日 下午1:25:48
	 */
	private boolean isBuildChain;

	/**
	 * http客户端 2016年1月18日 下午6:00:01
	 */
	protected final HttpClientFactory httpClientFactory;
	
	private HttpFilterChain filterChain;

	public HttpClientAcceptor() {
		this(null, 5, Executors.newCachedThreadPool());
	}

	public HttpClientAcceptor(HttpClientFactory httpClientFactory, int processorCount, ExecutorService executor) {
		if (processorCount < 1) {
			throw new IllegalArgumentException("Must have at least one processor");
		}
		if (httpClientFactory == null) {
			httpClientFactory = HttpClientFactory.DEFAULT_FACTORY;
		}
		this.httpClientFactory = httpClientFactory;
		filterChain = new HttpClientFilterChain(executor);
		this.executor = executor;
		this.processorCount = processorCount;
		httpProcessor = new HttpClientFilterProccessor[this.processorCount];

		for (int i = 0; i < processorCount; i++) {
			httpProcessor[i] = new HttpClientFilterProccessor("HttpClientFilterProccessor-" + id + "." + i, executor);
		}
	}

	@Override
	protected HttpResponseMessage doService(HttpRequest request, HttpHandler handler, HttpSession session)
			 throws HttpSessionClosedException, HttpInvokeException {
		HttpClientSession clientSession = (HttpClientSession) session;
		return clientSession.getProcessor().doWork(request, clientSession, httpClientFactory);
	}

	@Override
	protected HttpClientSession prepareService(HttpRequest request, HttpHandler handler) throws HttpSessionClosedException, HttpInvokeException  {

		// 获取具体的处理者
		HttpClientFilterProccessor processor = getProcessor();

		// 创建session
		HttpClientSession session = new HttpClientSession(this, request, processor, handler,filterChain);
		
		try {
			// 没有build链则build
			if (!isBuild()) {
				
				getFilterChainBuilder().buildFilterChain(session.getFilterChain());
				buildOk();
			}
		} catch (Exception e) {
			logger.warn("Unexpected exception.", e);
		}
		session.getFilterChain().fireSessionCreated(session);
		return session;
	}

	protected boolean isBuild() {
		return isBuildChain;
	}

	protected void buildOk() {
		isBuildChain = true;
	}

	/**
	 * 得到处理器
	 * 
	 * @return 2016年1月19日 下午1:54:10
	 */
	private HttpClientFilterProccessor getProcessor() {
		if (this.processorDistributor == Integer.MAX_VALUE) {
			this.processorDistributor = Integer.MAX_VALUE % this.processorCount;
		}

		return httpProcessor[processorDistributor++ % processorCount];
	}

	@Override
	public void close() throws Exception {
		super.close();
		executor.shutdown();
	}

}
