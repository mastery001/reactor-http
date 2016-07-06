package org.http.client;

import java.io.IOException;
import java.net.SocketTimeoutException;

import org.apache.http.Header;
import org.apache.http.NoHttpResponseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.conn.ConnectTimeoutException;
import org.http.HttpClientFactory;
import org.http.HttpRequest;
import org.http.HttpRequestRetryHandler;
import org.http.HttpResponseMessage;
import org.http.chain.HttpSession;
import org.http.exception.HttpInvokeException;
import org.http.exception.HttpInvokeException.InvokeErrorCode;
import org.http.exception.HttpSessionClosedException;
import org.http.support.HttpRetryHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 具体的http请求者，包含对请求处理以及其请求事件链处理
 * 
 * @author zouziwen
 *
 * @param <T>
 *            2016年2月24日 下午1:22:55
 */
abstract class HttpFilterProcessor<T> {

	private final Logger log = LoggerFactory.getLogger(getClass());

	abstract T doWork(HttpRequest request, HttpClientSession session, HttpClientFactory httpClientFactory)
			throws HttpSessionClosedException, HttpInvokeException;

	/**
	 * HttpFilter的具体实现
	 * 
	 * @param request
	 * @param session
	 * @param httpClientFactory
	 * @param executor 
	 * @return
	 * @throws Exception
	 *             2016年2月24日 下午1:20:46
	 */
	protected HttpResponseMessage doWork0(HttpRequest request, HttpClientSession session,
			HttpClientFactory httpClientFactory) throws HttpSessionClosedException, HttpInvokeException {
		session.getFilterChain().fireSessionCreated(session);
		checkSessionClose(session);
		HttpResponseMessage responseMessage = null;
		try {
			responseMessage = process(request, httpClientFactory , session);
			if (responseMessage.isSuccess()) {
				session.getFilterChain().fireRequestSuccessed(session, responseMessage);
			} else {
				session.getFilterChain().fireRequestFailed(session, responseMessage);
			}
		} catch (HttpInvokeException e) {
			session.getFilterChain().fireExceptionCaught(session, e);
			throw e;
		} finally {
			session.getFilterChain().fireSessionClosed(session);
			session.updateLastAccessedTime();
		}
		return responseMessage;
	}

	private HttpResponseMessage process(HttpRequest request, HttpClientFactory httpClientFactory, HttpSession session)
			throws HttpInvokeException {
		try {
			//HttpUriRequest concreteRequest = request.concreteRequest();
			/**
			 * 执行方法
			 */
			if(request.isRetry()) {
				return retryProcess(request , httpClientFactory , session);
			}
			return session.getHttpExecutor().execute(httpClientFactory, request);
			
		} catch (IOException e) {
			if (e instanceof ClientProtocolException) {
				throw new HttpInvokeException(InvokeErrorCode.HTTP_PROTOCOL_INVAILD, e);
			} else if (e instanceof SocketTimeoutException) {
				throw new HttpInvokeException(InvokeErrorCode.SOCKET_CONNECT_TIME_OUT, e);
			} else if (e instanceof ConnectTimeoutException) {
				throw new HttpInvokeException(InvokeErrorCode.CONNECT_TIME_OUT, e);
			} else if (e instanceof NoHttpResponseException) {
				throw new HttpInvokeException(InvokeErrorCode.NO_HTTP_RESPONSE, e);
			}
			throw new HttpInvokeException(InvokeErrorCode.UNKONW_IO_EXCEPTION, e);
		}
	}

	private HttpResponseMessage retryProcess(HttpRequest concreteRequest, HttpClientFactory httpClientFactory, HttpSession session) throws IOException {
		HttpRequestRetryHandler retryHandler = new HttpRetryHandler();
		final Header[] origheaders = concreteRequest.getAllHeaders();
		HttpResponseMessage reponse = null;
		for (int execCount = 1;; execCount++) {
			try {
				reponse = session.getHttpExecutor().execute(httpClientFactory, concreteRequest);
			} catch (final IOException ex) {
				if (retryHandler.retryRequest(ex, execCount, concreteRequest)) {
					if (this.log.isInfoEnabled()) {
						this.log.info(
								"I/O exception (" + ex.getClass().getName() + ") caught when processing request to "
										+ concreteRequest.getURI() + ": " + ex.getMessage());
					}
					if (this.log.isDebugEnabled()) {
						this.log.debug(ex.getMessage(), ex);
					}
					concreteRequest.setHeaders(origheaders);
					if (this.log.isInfoEnabled()) {
						this.log.info("Retrying request to " + concreteRequest.getURI());
					}
				} else {
                    if (ex instanceof NoHttpResponseException) {
                        final NoHttpResponseException updatedex = new NoHttpResponseException(
                        		concreteRequest.getURI() + " failed to respond");
                        updatedex.setStackTrace(ex.getStackTrace());
                        throw updatedex;
                    } else {
                        throw ex;
                    }
                }
				continue;
			}
			return reponse;
		}
		
	}

	/**
	 * 检查请求是否关闭
	 * 
	 * @param session
	 * @throws HttpSessionClosedException
	 *             2016年2月24日 下午5:34:16
	 */
	void checkSessionClose(HttpClientSession session) throws HttpSessionClosedException {
		if (session.isClose()) {
			throw new HttpSessionClosedException(session.getHttpRequest().getURI().toString());
		}
	}

}
