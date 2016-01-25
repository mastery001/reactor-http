package org.http.client;

import java.util.concurrent.ExecutorService;

import org.http.HttpClientFactory;
import org.http.HttpRequest;
import org.http.HttpResponseMessage;

/**
 * HttpClientFilter的处理者 使用多线程处理请求
 * 
 * @author zouziwen
 *
 *         2016年1月19日 下午1:26:00
 */
class HttpClientFilterProccessor {

	final String threadName;

	final ExecutorService executor;

	public HttpClientFilterProccessor(String threadName, ExecutorService executor) {
		this.threadName = threadName;
		this.executor = executor;
	}

	public HttpResponseMessage doWork(final HttpRequest request, final HttpClientSession session,
			final HttpClientFactory httpClientFactory) throws Exception {
		HttpResponseMessage responseMessage = null;
		try {
			responseMessage = request.sendRequest(httpClientFactory);
			if (responseMessage.isSuccess()) {
				session.getFilterChain().fireRequestSuccessed(session, responseMessage);
			} else {
				session.getFilterChain().fireRequestFailed(session, responseMessage);
			}
		} catch (Exception e) {
			session.getFilterChain().fireExceptionCaught(session, e);
			throw e;
		} finally {
			session.getFilterChain().fireSessionClosed(session);
		}
		return responseMessage;
//		Future<HttpResponseMessage> result = executor.submit(new NamePreservingCallable<HttpResponseMessage>(
//				new Worker(request, session, httpClientFactory), threadName));
//		return result.get();
	}

//	class Worker implements Callable<HttpResponseMessage> {
//		HttpRequest request;
//		HttpClientSession session;
//		HttpClientFactory httpClientFactory;
//
//		public Worker(HttpRequest request, HttpClientSession session, HttpClientFactory httpClientFactory) {
//			this.request = request;
//			this.session = session;
//			this.httpClientFactory = httpClientFactory;
//		}
//
//		@Override
//		public HttpResponseMessage call() throws Exception {
//			HttpResponseMessage responseMessage = null;
//			try {
//				responseMessage = request.sendRequest(httpClientFactory);
//				if (responseMessage.isSuccess()) {
//					session.getFilterChain().fireRequestSuccessed(session, responseMessage);
//				} else {
//					session.getFilterChain().fireRequestFailed(session, responseMessage);
//				}
//			} catch (Exception e) {
//				session.getFilterChain().fireExceptionCaught(session, e);
//				throw e;
//			} finally {
//				session.getFilterChain().fireSessionClosed(session);
//			}
//			return responseMessage;
//		}
//	}
	
}
