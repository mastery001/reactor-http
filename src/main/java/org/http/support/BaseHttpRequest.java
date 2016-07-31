package org.http.support;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.apache.http.Header;
import org.apache.http.HeaderIterator;
import org.apache.http.NameValuePair;
import org.apache.http.ProtocolVersion;
import org.apache.http.RequestLine;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpParams;
import org.http.HttpParameterOperation;
import org.http.HttpRequest;
import org.http.Releaseable;
import org.http.chain.util.UrlFormatUtil;

@SuppressWarnings("deprecation")
public abstract class BaseHttpRequest implements HttpRequest , Releaseable {

	private final HttpRequestBase request;
	
	protected String baseUrl;
	
	/**
	 * 完整路径
	 */
	private String requestUrl;
	
	/**
	 * 采用重试机制，重试发送 2016年1月18日 下午6:06:15
	 */
	private boolean isRetry;
	
	private boolean logEnabled = true;
	
	/**
	 * 参数构造器 2016年1月19日 下午6:19:51
	 */
//	private final ParamBuilder paramBuilder = new ParamBuilder();
	private List <NameValuePair> params;
	
	/**
	 * 默认采用非重试机制发送请求
	 * 
	 * @param baseUrl
	 *            2016年1月25日 下午4:07:41
	 */
	public BaseHttpRequest(String baseUrl) {
		this(baseUrl, false);
	}
	
	public BaseHttpRequest(String baseUrl , boolean isRetry) {
		this.baseUrl = baseUrl;
		this.isRetry = isRetry;
		request = initRequest(baseUrl);
		request.setHeader("Connection", "close");
		Objects.requireNonNull(request , "request should be init");
	}
	
	protected abstract HttpRequestBase initRequest(String baseUrl);
	
	protected HttpRequestBase getRequest() {
		return request;
	}
	
	@Override
	public boolean isRetry() {
		return isRetry;
	}

	@Override
	public HttpUriRequest concreteRequest() {
		if(requestUrl == null) {
			requestUrl = getCompleteUrl();
			request.setURI(URI.create(requestUrl));
		}
		return request;
	}
	
	protected String getBaseUrl() {
		return this.baseUrl;
	}
	
	String getCompleteUrl() {
		return UrlFormatUtil.formatUrl(getBaseUrl(), parameters());
	}

	protected List <NameValuePair> parameters() {
		if(params == null) {
			params = new ArrayList <NameValuePair>();
		}
		return params;
	}
	

	@Override
	public HttpRequest logEnabled(boolean enable) {
		logEnabled = enable;
		return this;
	}

	@Override
	public boolean logEnabled() {
		return logEnabled;
	}

	@Override
	public String getMethod() {
		return request.getMethod();
	}

	@Override
	public URI getURI() {
		return request.getURI();
	}

	@Override
	public void abort() throws UnsupportedOperationException {
		request.abort();
	}

	@Override
	public boolean isAborted() {
		return request.isAborted();
	}

	@Override
	public RequestLine getRequestLine() {
		return request.getRequestLine();
	}

	@Override
	public ProtocolVersion getProtocolVersion() {
		return request.getProtocolVersion();
	}

	@Override
	public boolean containsHeader(String name) {
		return request.containsHeader(name);
	}

	@Override
	public Header[] getHeaders(String name) {
		return request.getHeaders(name);
	}

	@Override
	public Header getFirstHeader(String name) {
		return request.getFirstHeader(name);
	}

	@Override
	public Header getLastHeader(String name) {
		return request.getLastHeader(name);
	}

	@Override
	public Header[] getAllHeaders() {
		return request.getAllHeaders();
	}

	@Override
	public void addHeader(Header header) {
		request.addHeader(header);
	}

	@Override
	public void addHeader(String name, String value) {
		request.addHeader(name, value);
	}

	@Override
	public void setHeader(Header header) {
		request.setHeader(header);
	}

	@Override
	public void setHeader(String name, String value) {
		request.setHeader(name, value);
	}

	@Override
	public void setHeaders(Header[] headers) {
		request.setHeaders(headers);
	}

	@Override
	public void removeHeader(Header header) {
		request.removeHeader(header);
	}

	@Override
	public void removeHeaders(String name) {
		request.removeHeaders(name);
	}

	@Override
	public HeaderIterator headerIterator() {
		return request.headerIterator();
	}

	@Override
	public HeaderIterator headerIterator(String name) {
		return request.headerIterator(name);
	}

	@Override
	public HttpParams getParams() {
		return request.getParams();
	}

	@Override
	public void setParams(HttpParams params) {
		request.setParams(params);
	}

	@Override
	public HttpParameterOperation addParameter(String paramName, Object paramValue) {
		//paramBuilder.addParameter(paramName, paramValue);
		// 判断null减少内存使用
		if(paramName != null) {
			String value = "";
			if(paramValue != null ){
				value = String.valueOf(paramValue);
			}
			parameters().add(new BasicNameValuePair(paramName, value));
		}
		return this;
	}

	@Override
	public void release() {
		// 释放内存
		params = null;
		request.releaseConnection();
	}
	

	
	/**
	 * 参数构造器
	 * 
	 * @author zouziwen
	 *
	 *         2016年1月19日 下午5:58:13
	 */
//	private class ParamBuilder {
//		private final Map<String, Object> params = new HashMap<String, Object>(8);
//
//		public ParamBuilder addParameter(String paramName, Object paramValue) {
//			params.put(paramName, paramValue);
//			return this;
//		}
//
//		public Map<String, Object> getParameters() {
//			return params;
//		}
//
//		public ParamBuilder clear() {
//			params.clear();
//			return this;
//		}
//		
//		public boolean isEmpty() {
//			return params.isEmpty();
//		}
//	}
	
}
