package org.http.client;

import org.http.HttpClientFactory;
import org.http.HttpRequest;
import org.http.HttpResponseMessage;
import org.http.exception.HttpInvokeException;
import org.http.exception.HttpSessionClosedException;

/**
 * 具体的http请求者，包含对请求处理以及其请求事件链处理
 * @author zouziwen
 *
 * @param <T>
 * 2016年2月24日 下午1:22:55
 */
abstract class HttpFilterProcessor<T> {

	abstract T doWork(HttpRequest request, HttpClientSession session,
			HttpClientFactory httpClientFactory) throws HttpSessionClosedException, HttpInvokeException;
	
	/**
	 * HttpFilter的具体实现
	 * @param request
	 * @param session
	 * @param httpClientFactory
	 * @return
	 * @throws Exception
	 * 2016年2月24日 下午1:20:46
	 */
	protected HttpResponseMessage doWork0(HttpRequest request, HttpClientSession session,
			HttpClientFactory httpClientFactory) throws HttpSessionClosedException, HttpInvokeException {
		session.getFilterChain().fireSessionCreated(session);
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
			session.updateLastAccessedTime();
		}
		return responseMessage;
	}
}
