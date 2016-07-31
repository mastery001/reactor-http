package org.http.chain.support;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Locale;

import org.apache.http.Header;
import org.apache.http.HeaderIterator;
import org.apache.http.HttpEntity;
import org.apache.http.ProtocolVersion;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.http.HttpResponseMessage;
import org.http.chain.util.ExceptionMonitor;
import org.http.exception.HttpResponseProcessException;

@SuppressWarnings("deprecation")
public class DefaultHttpResponseMessage implements HttpResponseMessage {

	private final CloseableHttpResponse response;

	public DefaultHttpResponseMessage(CloseableHttpResponse response) {
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
	public String getContent() {
		try {
			return content();
		} catch (HttpResponseProcessException e) {
			ExceptionMonitor.getInstance().ignoreCaught(e);
		}
		return null;
	}

	@Override
	public byte[] getResponseBody() throws HttpResponseProcessException {
		if (byteContents == null) {
			synchronized (lock) {
				try {
					byteContents = EntityUtils.toByteArray(this.getEntity());
				} catch (IOException e) {
					throw new HttpResponseProcessException(e);
				}finally {
					// 消费实体，
					try {
						EntityUtils.consume(getEntity());
					} catch (IOException e) {
						ExceptionMonitor.getInstance().ignoreCaught(e);
					}
				}
			}
		}
		return byteContents;
	}

	private byte[] byteContents;

	private final byte[] lock = new byte[0];

	@Override
	public String content() throws HttpResponseProcessException {
		try {
			return new String(getResponseBody(), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// never happen
		}
		return null;
	}

	@Override
	public void close() throws IOException {
		response.close();
	}

	@Override
	public void closeQuietly() {
		// 释放内存,保证都关闭
		try {
			close();
		} catch (IOException e) {
			ExceptionMonitor.getInstance().ignoreCaught(e);
		}
	}

}