package org.http.support;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.URI;
import org.apache.commons.httpclient.URIException;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.http.HttpClientFactory;
import org.http.HttpRequest;
import org.http.HttpRequestMessage;
import org.http.HttpResponseMessage;
import org.http.chain.support.HttpRetryHandler;

public abstract class BaseHttpRequest implements HttpRequest {

	protected final String baseUrl;

	/**
	 * 默认空请求 2016年1月18日 下午5:56:02
	 */
	private final HttpRequestMessageImpl requestMessage;

	/**
	 * 采用重试机制，重试发送 2016年1月18日 下午6:06:15
	 */
	private int retryCount;

	private HttpMethod method;

	/**
	 * 参数构造器 2016年1月19日 下午6:19:51
	 */
	protected final ParamBuilder paramBuilder = new ParamBuilder();

	public BaseHttpRequest(String baseUrl) {
		this.baseUrl = baseUrl;
		method = innerInitMethod();
		requestMessage = new HttpRequestMessageImpl(method, paramBuilder);
	}

	public HttpRequest setRetryCount(int retryCount) {
		if (retryCount > 0) {
			this.retryCount = retryCount;
			initRetry(method, this.retryCount);
		}
		return this;
	}

	/**
	 * 获得重试次数
	 * 
	 * @return 2016年1月20日 上午9:59:03
	 */
	protected int getRetryCount() {
		return this.retryCount;
	}

	@Override
	public HttpRequest addParameter(String paramName, Object paramValue) {
		paramBuilder.addParameter(paramName, paramValue);
		updateRequestMessage();
		return this;
	}

	@Override
	public Map<String, Object> getParameters() {
		return paramBuilder.getParameters();
	}

	@Override
	public Object getParameter(String paramName) {
		return paramBuilder.getParameter(paramName);
	}

	@Override
	public HttpRequest addHeader(String paramName, Object paramValue) {
		method.addRequestHeader(paramName, String.valueOf(paramValue));
		updateRequestMessage();
		return this;
	}

	@Override
	public HttpRequest removeHeader(String paramName) {
		method.removeRequestHeader(paramName);
		updateRequestMessage();
		return this;
	}

	/**
	 * 设置方法重试的手柄
	 * 
	 * @param method
	 * @param retryCount
	 *            2016年1月18日 下午4:08:01
	 */
	protected void initRetry(HttpMethod method, int retryCount) {
		if (getRetryCount() > 0) {
			method.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new HttpRetryHandler(retryCount));
			updateRequestMessage();
		}
	}

	/**
	 * 初始化请求
	 * 
	 * @return 2016年1月19日 下午3:03:52
	 */
	private HttpRequest prepareRequest() {
		prepareRequest(method, buildNameValuePair(paramBuilder.getParameters()));
		
		updateRequestMessage();
		
		paramBuilder.clear();
		return this;
	}

	/**
	 * 更新信息
	 * 2016年1月20日 下午4:35:45
	 */
	private void updateRequestMessage() {
		requestMessage.update(method, paramBuilder);
	}

	/**
	 * 准备请求的具体实现
	 * @param method
	 * @param nameValuePairs
	 * 2016年1月20日 下午4:38:56
	 */
	protected void prepareRequest(HttpMethod method, NameValuePair[] nameValuePairs) {
		method.setQueryString(nameValuePairs);
	}

	@Override
	public HttpResponseMessage sendRequest(HttpClientFactory httpClientFactory) throws IOException, HttpException {
		HttpClient httpClient = httpClientFactory.getConnection();
		if (httpClientFactory == null || httpClient == null) {
			throw new IOException("httpClientFacotry is Null");
		}
		
		prepareRequest();

		/**
		 * 执行方法
		 */
		httpClient.executeMethod(method);
		try {
			return new HttpResponseImpl(method);
		} finally {
			method.releaseConnection();
		}
	}

	/**
	 * 构建发送请求参数
	 * 
	 * @param parameters
	 * @return 2016年1月19日 下午4:57:40
	 */
	protected NameValuePair[] buildNameValuePair(Map<String, Object> parameters) {
		if (parameters == null || parameters.isEmpty()) {
			return EMPTY_NAMEVALUE_PAIRS;
		}
		NameValuePair[] nameValuePairs = new NameValuePair[parameters.size()];

		List<NameValuePair> nameValuePairList = new ArrayList<NameValuePair>(parameters.size());
		for (Map.Entry<String, Object> entry : parameters.entrySet()) {
			nameValuePairList.add(new NameValuePair(entry.getKey(), String.valueOf(entry.getValue())));
		}
		nameValuePairList.toArray(nameValuePairs);
		return nameValuePairs;
	}

	protected abstract HttpMethod innerInitMethod();

	@Override
	public HttpRequestMessage getRequestMessage() {
		return requestMessage;
	}

	protected HttpMethod getMethod() {
		return method;
	}

	public String getBaseUrl() {
		return baseUrl;
	}

	/**
	 * 将返回值成string
	 * 
	 * @param method
	 * @return
	 * @throws IOException
	 *             2016年1月18日 下午5:58:23
	 */
	protected String extractContent(HttpMethod method) throws IOException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(method.getResponseBodyAsStream()));
		StringBuffer sb = new StringBuffer();
		String line;
		while ((line = reader.readLine()) != null) {
			sb.append(line);
		}
		return sb.toString();
	}

	protected class HttpResponseImpl implements HttpResponseMessage {
		public HttpMethod method;

		private String content;

		public HttpResponseImpl(HttpMethod method) throws IOException {
			this.method = method;
			content = extractContent(method);
		}

		@Override
		public String getContent() {
			return content;
		}

		@Override
		public int getStatusCode() {
			return method.getStatusCode();
		}

		@Override
		public Header[] getResponseHeaders() {
			return method.getResponseHeaders();
		}

		@Override
		public Header[] getResponseFooters() {
			return method.getResponseFooters();
		}

		@Override
		public byte[] getResponseBody() throws IOException {
			return method.getResponseBody();
		}

		@Override
		public String getResponseBodyAsString() throws IOException {
			return method.getResponseBodyAsString();
		}

		@Override
		public InputStream getResponseBodyAsStream() throws IOException {
			return method.getResponseBodyAsStream();
		}

		@Override
		public boolean isSuccess() {
			return getStatusCode() == HttpStatus.SC_OK;
		}

	}

	protected class HttpRequestMessageImpl implements HttpRequestMessage {

		private HttpMethod method;

		private ParamBuilder paramBuilder;

		void update(HttpMethod method, ParamBuilder paramBuilder) {
			this.method = method;
			this.paramBuilder = paramBuilder;
		}
		

		public HttpRequestMessageImpl(HttpMethod method, ParamBuilder paramBuilder) {
			update(method, paramBuilder);
		}

		@Override
		public String getPath() {
			return method.getPath();
		}

		@Override
		public MethodType getMethod() {
			if (method instanceof PostMethod) {
				return MethodType.POST;
			} else if (method instanceof GetMethod) {
				return MethodType.GET;
			}
			return null;
		}

		@Override
		public URI getURI() throws URIException {
			return method.getURI();
		}

		@Override
		public Header getRequestHeader(String headerName) {
			return method.getRequestHeader(headerName);
		}

		@Override
		public Header[] getRequestHeaders(String headerName) {
			return method.getRequestHeaders();
		}

		@Override
		public String getURL() {
			return getBaseUrl();
		}

		@Override
		public Object getParameter(String name) {
			return paramBuilder.getParameter(name);
		}

		@Override
		public Map<String, Object> getParameters() {
			return paramBuilder.getParameters();
		}

	}

	/**
	 * 参数构造器
	 * 
	 * @author zouziwen
	 *
	 *         2016年1月19日 下午5:58:13
	 */
	private class ParamBuilder {
		private final Map<String, Object> params = new HashMap<String, Object>(8);

		public ParamBuilder addParameter(String paramName, Object paramValue) {
			params.put(paramName, paramValue);
			return this;
		}

		public Object getParameter(String paramName) {
			return params.get(paramName);
		}

		public Map<String, Object> getParameters() {
			return params;
		}

		public ParamBuilder clear() {
			params.clear();
			return this;
		}
	}

}
