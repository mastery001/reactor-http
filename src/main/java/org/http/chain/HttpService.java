package org.http.chain;

/**
 * http service
 * 
 * @author zouziwen
 *
 *         2016年1月18日 下午8:02:42
 */
public interface HttpService {

	
	/**
	 * 抽象httpFilter的构造器
	 * @return
	 * 2016年1月26日 下午4:45:56
	 */
	HttpFilterChainBuilder getFilterChainBuilder();

	/**
	 * httpFilter的的处理链的构造器
	 * 	一般调用该方法来添加filter
	 * @return
	 * 2016年1月26日 下午4:45:26
	 */
	DefaultHttpFilterChainBuilder getFilterChain();

	/**
	 * 释放server的资源
	 * 	该方法需要谨慎使用，会清除所有的filter
	 * 
	 * 2016年1月19日 下午3:00:12
	 */
	void release();
}
