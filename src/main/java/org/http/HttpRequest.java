package org.http;

import java.io.IOException;
import java.util.Map;

import org.apache.commons.httpclient.HttpException;

/**
 * http请求基类
 * 
 * @author zouziwen
 *
 *         2016年1月18日 下午5:47:06
 */
public interface HttpRequest {

	/**
	 * 默认重试次数
	 * 2016年1月25日 下午4:00:53
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
	 HttpResponseMessage sendRequest(HttpClientFactory httpClientFactory) throws IOException, HttpException;

	/**
	 * 采用重试发送机制.设置重试次数 2016年1月18日 下午6:03:17
	 */
	 HttpRequest setRetryCount(int retryCount);

	/**
	 * 添加请求参数，亦或是body的参数
	 * 
	 * @param paramName
	 * @param paramValue
	 * @return 2016年1月19日 下午4:54:30
	 */
	 HttpRequest addParameter(String paramName, Object paramValue);
	 
	 
	 Map<String , Object> getParameters();
	 
	 Object getParameter(String paramName);

	/**
	 * 添加请求头参数
	 * 
	 * @param paramName
	 * @param paramValue
	 * @return 2016年1月19日 下午4:54:30
	 */
	 HttpRequest addHeader(String paramName, Object paramValue);

	/**
	 * 移除请求头参数
	 * 
	 * @param paramName
	 * @return 2016年1月19日 下午4:54:38
	 */
	 HttpRequest removeHeader(String paramName);
	
	/**
	 * 获得请求信息
	 * 
	 * @return 2016年1月19日 下午3:11:04
	 */
	 HttpRequestMessage getRequestMessage();
	
}
