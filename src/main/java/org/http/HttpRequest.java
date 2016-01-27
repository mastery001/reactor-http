package org.http;

import java.io.IOException;
import java.util.Map;

import org.apache.commons.httpclient.HttpException;
import org.http.exception.HttpInvokeException;

/**
 * http请求基类
 * 
 * @author zouziwen
 *
 *         2016年1月18日 下午5:47:06
 */
public interface HttpRequest {

	/**
	 * 默认重试次数 2016年1月25日 下午4:00:53
	 */
	int DEFAULT_RETRY_COUNT = 3;

	/**
	 * 发送http请求
	 * 
	 * @return
	 * @throws IOException
	 * @throws HttpException
	 *             2016年1月18日 下午6:03:33
	 */
	HttpResponseMessage sendRequest(HttpClientFactory httpClientFactory) throws HttpInvokeException;

	/**
	 * 采用重试发送机制.设置重试次数 2016年1月18日 下午6:03:17
	 */
	HttpRequest setRetryCount(int retryCount);

	/**
	 * 添加请求参数
	 * 当添加的value为null时不做添加操作
	 * @param paramName
	 * @param paramValue
	 * @return 2016年1月19日 下午4:54:30
	 */
	HttpRequest addParameter(String paramName, Object paramValue);

	/**
	 * 获得所有的参数
	 * @return
	 * 2016年1月27日 下午3:39:05
	 */
	Map<String, Object> getParameters();

	/**
	 * 获得对应paramName对应的值
	 * @param paramName
	 * @return
	 * 2016年1月27日 下午3:39:12
	 */
	Object getParameter(String paramName);

	/**
	 * 添加请求头参数
	 * 
	 * @param headerName
	 * @param headerValue
	 * @return 2016年1月19日 下午4:54:30
	 */
	HttpRequest addHeader(String headerName, Object headerValue);

	/**
	 * 移除请求头参数
	 * 
	 * @param headerName
	 * @return 2016年1月19日 下午4:54:38
	 */
	HttpRequest removeHeader(String headerName);

	/**
	 * 获得请求信息
	 * 
	 * @return 2016年1月19日 下午3:11:04
	 */
	HttpRequestMessage getRequestMessage();

}
