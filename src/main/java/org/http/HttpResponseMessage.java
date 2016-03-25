package org.http;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.httpclient.Cookie;
import org.apache.commons.httpclient.Header;

/**
 * http返回端
 * @author zouziwen
 *
 * 2016年1月18日 下午5:14:30
 */
public interface HttpResponseMessage {

	/**
	 * 获取返回的内容
	 * @return
	 * 2016年1月18日 下午5:14:12
	 */
	public String getContent();
	
	/**
	 * 获取状态码
	 * @return
	 * 2016年1月18日 下午5:14:21
	 */
	public int getStatusCode();
	
	
	/**
	 * http请求是否成功
	 * @return
	 * 2016年1月21日 下午2:15:16
	 */
	public boolean isSuccess();
	
	/**
	 * 响应头信息
	 * @return
	 * 2016年1月18日 下午5:58:47
	 */
	Header[] getResponseHeaders();
	
	/**
	 * 获取响应头信息
	 * @time 2016年3月25日下午8:52:59
	 * @param headerName
	 * @return
	 */
	Header getResponseHeader(String headerName);

	/**
	 * 响应尾信息
	 * @return
	 * 2016年1月18日 下午5:58:58
	 */
	Header[] getResponseFooters();
	
	/**
	 * 获取响应尾信息
	 * @time 2016年3月25日下午8:52:59
	 * @param footerName
	 * @return
	 */
	Header getResponseFooter(String footerName);

	/**
	 * 获取cookies
	 * @time 2016年3月25日下午8:29:31
	 * @return
	 */
	Cookie[] getCookies();
	
	/**
	 * 获取响应体的内容
	 * @return
	 * @throws IOException
	 * 2016年1月18日 下午5:59:10
	 */
	byte[] getResponseBody() throws IOException;

	/**
	 * 获取响应体的内容（String形式）
	 * @return
	 * @throws IOException
	 * 2016年1月18日 下午5:59:25
	 */
	String getResponseBodyAsString() throws IOException;

	/**
	 * 获取响应体的内容（流形式 ）
	 * @return
	 * @throws IOException
	 * 2016年1月18日 下午5:59:36
	 */
	InputStream getResponseBodyAsStream() throws IOException;
	
}
