package org.http.chain;

/**
 * http service
 * 
 * @author zouziwen
 *
 *         2016年1月18日 下午8:02:42
 */
public interface HttpService {

	HttpFilterChainBuilder getFilterChainBuilder();

	DefaultHttpFilterChainBuilder getFilterChain();

	/**
	 * 关闭server
	 * 
	 * @throws Exception
	 *             2016年1月19日 下午3:00:12
	 */
	void close() throws Exception;
}
