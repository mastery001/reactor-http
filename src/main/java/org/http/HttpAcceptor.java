package org.http;

import org.http.chain.HttpHandler;

/**
 * http访问者
 * @author zouziwen
 *
 * 2016年1月20日 下午2:05:22
 */
public interface HttpAcceptor {

	/**
	 * 处理请求的根方法
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 *             2016年1月18日 下午8:02:53
	 */
	HttpResponseMessage service(HttpRequest request) throws Exception;

	/**
	 * 针对单个http请求的个性化操作
	 * 
	 * @param request
	 * @param handler
	 *            个性化操作
	 * @return
	 * @throws Exception
	 *             2016年1月19日 下午5:03:23
	 */
	HttpResponseMessage service(HttpRequest request, HttpHandler handler) throws Exception;
}
