package org.http.chain;

import org.http.HttpRequest;

/**
 * 每个请求会有一个独立的session
 * 
 * @author zouziwen
 *
 *         2016年1月18日 下午4:45:50
 */
public interface HttpSession {

	/**
	 * 具体请求的回调者
	 * 
	 * @return 2016年1月20日 上午10:08:34
	 */
	HttpHandler getHandler();

	/**
	 * 获取server
	 * 
	 * @return 2016年1月19日 上午11:33:11
	 */
	HttpService getService();
	
	
	/**
	 * 获取请求
	 * @return
	 * 2016年1月20日 下午5:18:17
	 */
	HttpRequest getRequest();

	/**
	 * 主动关闭session的连接，即停止http请求 2016年1月20日 上午10:52:54
	 */
	void close();

	/**
	 * 是否关闭连接
	 * 
	 * @return 2016年1月20日 上午10:55:40
	 */
	boolean isClose();

	HttpFilterChain getFilterChain();

	/**
	 * 获得session名称，其实是请求的url
	 * 
	 * @return 2016年1月19日 下午5:11:07
	 */
	String getName();

	/**
	 * 获取创建时间
	 * 
	 * @return 2016年1月18日 下午4:30:11
	 */
	long getCreateTime();

	/**
	 * 获取最后访问时间
	 * 
	 * @return 2016年1月18日 下午4:30:18
	 */
	long getLastAccessedTime();

	/**
	 * 获取生存时间
	 * 
	 * @return 2016年1月18日 下午4:31:04
	 */
	long getSurvivalTime();

	Object setAttribute(String key, Object value);

	Object getAttribute(String key);

	Object removeAttribute(String key);

	boolean containsAttribute(String key);

}
