package org.http;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.URI;
import org.apache.commons.httpclient.URIException;

/**
 * http请求的信息
 * @author zouziwen
 *
 * 2016年1月18日 下午5:39:49
 */
public interface HttpRequestMessage {

	/**
	 * 空请求
	 * 2016年1月18日 下午5:58:14
	 */
	HttpRequestMessage NullHttpRequestMessage = new HttpRequestMessage() {
		
		private final Map<String , Object> map = new HashMap<String, Object>();
		
		@Override
		public String getPath() {
			return null;
		}

		@Override
		public MethodType getMethod() {
			return MethodType.NULL;
		}

		@Override
		public URI getURI() throws URIException {
			return null;
		}

		@Override
		public Header getRequestHeader(String headerName) {
			return null;
		}

		@Override
		public Header[] getRequestHeaders(String headerName) {
			return null;
		}

		@Override
		public String getURL() {
			return "";
		}

		@Override
		public Object getParameter(String name) {
			return null;
		}

		@Override
		public Map<String, Object> getParameters() {
			return map;
		}
		
	};
	
	/**
	 * 获取请求的真实路径
	 * @return
	 * 2016年1月18日 下午5:57:06
	 */
	String getPath();

	/**
	 * 获取请求的方法，get还是post
	 * @return
	 * 2016年1月18日 下午5:57:10
	 */
	MethodType getMethod();
	
	/**
	 * 获取请求的uri
	 * @return
	 * @throws URIException
	 * 2016年1月18日 下午5:57:46
	 */
	URI getURI() throws URIException;
	
	/**
	 * 获得请求的url
	 * @return
	 * 2016年1月19日 下午5:09:39
	 */
	String getURL();

	/**
	 * 对应的请求头
	 * @param headerName
	 * @return
	 * 2016年1月18日 下午5:57:52
	 */
	Header getRequestHeader(String headerName);

	/**
	 * 所有请求头
	 * @param headerName
	 * @return
	 * 2016年1月18日 下午5:57:59
	 */
	Header[] getRequestHeaders(String headerName);

	/**
	 * 获得查询参数
	 * @param name
	 * @return
	 * 2016年1月20日 下午4:23:14
	 */
	Object getParameter(String name);
	
	/**
	 * 获得所有查询参数
	 * @return
	 * 2016年1月20日 下午4:23:20
	 */
	Map<String , Object> getParameters();
	
	public enum MethodType {
		POST , GET , NULL 
	}

}
