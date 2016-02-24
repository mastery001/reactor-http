package org.http.client;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import org.http.HttpClientFactory;
import org.http.HttpRequest;
import org.http.HttpResponseMessage;
import org.http.chain.util.NamePreservingCallable;
import org.http.exception.HttpInvokeException;
import org.http.exception.HttpSessionClosedException;

/**
 * 异步调用http
 * @author zouziwen
 *
 * 2016年2月24日 下午1:33:08
 */
class AyncHttpClientProccessor extends HttpFilterProcessor<Future<HttpResponseMessage>>{

	final String threadName;

	private final ExecutorService executor;

	public AyncHttpClientProccessor(String threadName, ExecutorService executor) {
		this.threadName = threadName;
		this.executor = executor;
	}

	Future<HttpResponseMessage> doWork(final HttpRequest request, final HttpClientSession session,
			final HttpClientFactory httpClientFactory)
					throws HttpSessionClosedException, HttpInvokeException {
		Future<HttpResponseMessage> result = executor.submit(new NamePreservingCallable<HttpResponseMessage>(
				new Worker(request, session, httpClientFactory), threadName));
		return result;
	}

	class Worker implements Callable<HttpResponseMessage> {
		HttpRequest request;
		HttpClientSession session;
		HttpClientFactory httpClientFactory;

		public Worker(HttpRequest request, HttpClientSession session, HttpClientFactory httpClientFactory) {
			this.request = request;
			this.session = session;
			this.httpClientFactory = httpClientFactory;
		}

		@Override
		public HttpResponseMessage call() throws Exception {
			return doWork0(request, session, httpClientFactory);
		}
	}

}
