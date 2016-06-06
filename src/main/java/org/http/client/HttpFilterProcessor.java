package org.http.client;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.SocketTimeoutException;
import java.util.Locale;

import org.apache.http.Header;
import org.apache.http.HeaderIterator;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NoHttpResponseException;
import org.apache.http.ProtocolVersion;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.http.HttpClientFactory;
import org.http.HttpRequest;
import org.http.HttpRequestRetryHandler;
import org.http.HttpResponseMessage;
import org.http.exception.HttpInvokeException;
import org.http.exception.HttpInvokeException.InvokeErrorCode;
import org.http.exception.HttpResponseProcessException;
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
@SuppressWarnings("deprecation")
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
			responseMessage = process(request, httpClientFactory);
			if (responseMessage.getStatusLine().getStatusCode() == 200) {
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

	private HttpResponseMessage process(HttpRequest request, HttpClientFactory httpClientFactory)
			throws HttpInvokeException {
		try {
			HttpUriRequest concreteRequest = request.concreteRequest();
			/**
			 * 执行方法
			 */
			if(request.isRetry()) {
				return retryProcess(concreteRequest , httpClientFactory);
			}
			return new HttpResponseImpl(httpClientFactory.getConnection().execute(concreteRequest));
			
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

	private HttpResponseMessage retryProcess(HttpUriRequest concreteRequest, HttpClientFactory httpClientFactory) throws IOException {
		HttpRequestRetryHandler retryHandler = new HttpRetryHandler();
		final Header[] origheaders = concreteRequest.getAllHeaders();
		HttpResponse reponse = null;
		for (int execCount = 1;; execCount++) {
			try {
				reponse = httpClientFactory.getConnection().execute(concreteRequest);
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
			return new HttpResponseImpl(reponse);
		}
		
	}

	static class HttpResponseImpl implements HttpResponseMessage {

		private final HttpResponse response;

		HttpResponseImpl(HttpResponse response) {
			this.response = response;
		}

		@Override
		public StatusLine getStatusLine() {
			return response.getStatusLine();
		}

		@Override
		public void setStatusLine(StatusLine statusline) {
			response.setStatusLine(statusline);
		}

		@Override
		public void setStatusLine(ProtocolVersion ver, int code) {
			response.setStatusLine(ver, code);
		}

		@Override
		public void setStatusLine(ProtocolVersion ver, int code, String reason) {
			response.setStatusLine(ver, code, reason);
		}

		@Override
		public void setStatusCode(int code) throws IllegalStateException {
			response.setStatusCode(code);
		}

		@Override
		public void setReasonPhrase(String reason) throws IllegalStateException {
			response.setReasonPhrase(reason);
		}

		@Override
		public HttpEntity getEntity() {
			return response.getEntity();
		}

		@Override
		public void setEntity(HttpEntity entity) {
			response.setEntity(entity);
		}

		@Override
		public Locale getLocale() {
			return response.getLocale();
		}

		@Override
		public void setLocale(Locale loc) {
			response.setLocale(loc);
		}

		@Override
		public ProtocolVersion getProtocolVersion() {
			return response.getProtocolVersion();
		}

		@Override
		public boolean containsHeader(String name) {
			return response.containsHeader(name);
		}

		@Override
		public Header[] getHeaders(String name) {
			return response.getHeaders(name);
		}

		@Override
		public Header getFirstHeader(String name) {
			return response.getFirstHeader(name);
		}

		@Override
		public Header getLastHeader(String name) {
			return response.getLastHeader(name);
		}

		@Override
		public Header[] getAllHeaders() {
			return response.getAllHeaders();
		}

		@Override
		public void addHeader(Header header) {
			response.addHeader(header);
		}

		@Override
		public void addHeader(String name, String value) {
			response.addHeader(name, value);
		}

		@Override
		public void setHeader(Header header) {
			response.setHeader(header);
		}

		@Override
		public void setHeader(String name, String value) {
			response.setHeader(name, value);
		}

		@Override
		public void setHeaders(Header[] headers) {
			response.setHeaders(headers);
		}

		@Override
		public void removeHeader(Header header) {
			response.removeHeader(header);
		}

		@Override
		public void removeHeaders(String name) {
			response.removeHeaders(name);
		}

		@Override
		public HeaderIterator headerIterator() {
			return response.headerIterator();
		}

		@Override
		public HeaderIterator headerIterator(String name) {
			return response.headerIterator(name);
		}

		@Override
		public HttpParams getParams() {
			return response.getParams();
		}

		@Override
		public void setParams(HttpParams params) {
			response.setParams(params);
		}

		@Override
		public boolean isSuccess() {
			return this.getStatusLine().getStatusCode() == SUCCESS_CODE;
		}

		@Override
		public String getContent() throws HttpResponseProcessException {
			try {
				return new String(getResponseBody() , "UTF-8");
			} catch (UnsupportedEncodingException e) {
				// never happen
			}
			return null;
		}

		@Override
		public byte[] getResponseBody() throws HttpResponseProcessException {
			if(byteContents == null) {
				synchronized (lock) {
					try {
						byteContents =  EntityUtils.toByteArray(this.getEntity());
					}catch (IOException e) {
						throw new HttpResponseProcessException(e);
					}
				}
			}
			return byteContents;
		}
		
		private byte[] byteContents;
		
		private final byte[] lock = new byte[0];

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
