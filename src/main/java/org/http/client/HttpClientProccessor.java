package org.http.client;

import org.http.HttpClientFactory;
import org.http.HttpRequest;
import org.http.HttpResponseMessage;
import org.http.exception.HttpInvokeException;
import org.http.exception.HttpSessionClosedException;

/**
 * HttpClientFilter的处理者
 * 
 * @author zouziwen
 *
 *         2016年1月19日 下午1:26:00
 */
class HttpClientProccessor extends HttpFilterProcessor<HttpResponseMessage>{

	public HttpClientProccessor() {
	}

	HttpResponseMessage doWork(final HttpRequest request, final HttpClientSession session,
			final HttpClientFactory httpClientFactory)
					throws HttpSessionClosedException, HttpInvokeException {
		return doWork0(request, session, httpClientFactory);
	}

}
