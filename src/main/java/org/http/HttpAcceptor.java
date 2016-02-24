package org.http;

import org.http.chain.HttpHandler;
import org.http.exception.HttpInvokeException;
import org.http.exception.HttpSessionClosedException;

/**
 * http访问者
 * 
 * @author zouziwen
 *
 *         2016年1月20日 下午2:05:22
 */
public interface HttpAcceptor<T> {

	/**
	 * 处理请求的根方法
	 * 
	 * @param request
	 * @return
	 * @throws HttpSessionClosedException   可能抛出请求强制关闭的异常	
	 * @throws HttpInvokeException			请求调用时产生的异常
	 *             2016年1月18日 下午8:02:53
	 */
	T service(HttpRequest request) throws HttpSessionClosedException, HttpInvokeException;

	/**
	 * 针对单个http请求的个性化操作
	 * 
	 * @param request
	 * @param handler
	 *            个性化操作
	 * @return
	 * @throws HttpSessionClosedException   请求强制关闭的异常
	 * @throws HttpInvokeException			请求调用时产生的异常
	 *             2016年1月19日 下午5:03:23
	 */
	T service(HttpRequest request, HttpHandler handler)
			throws HttpSessionClosedException, HttpInvokeException;
}
