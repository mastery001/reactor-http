package org.http.chain;

import org.http.HttpResponseMessage;

/**
 * 针对某个请求而个性化的实现
 * 	handler的调用是同步的
 * @author zouziwen
 *
 * 2016年1月19日 下午5:00:32
 */
public interface HttpHandler {
	
	/**
	 * http连接开始创建
	 * 
	 * @param session
	 * @throws Exception
	 *             2016年1月19日 下午12:36:25
	 */
	void sessionCreated(HttpSession session) throws Exception;

	/**
	 * http连接关闭
	 * 
	 * @param session
	 * @throws Exception
	 *             2016年1月19日 下午12:36:34
	 */
	void sessionClosed(HttpSession session) throws Exception;

	/**
	 * 当http请求调用失败时调用
	 * 
	 * @param session
	 * @param responseMessage
	 * @throws Exception
	 *             2016年1月19日 下午12:36:58
	 */
	void requestFailed(HttpSession session, HttpResponseMessage responseMessage) throws Exception;

	/**
	 * 当http请求调用成功时调用
	 * 
	 * @param session
	 * @param responseMessage
	 * @throws Exception
	 *             2016年1月19日 下午12:37:06
	 */
	void requestSuccessed(HttpSession session, HttpResponseMessage responseMessage) throws Exception;

	/**
	 * 当出现异常时调用
	 * 
	 * @param session
	 * @param cause
	 * @throws Exception
	 *             2016年1月19日 下午12:37:14
	 */
	void exceptionCaught(HttpSession session, Throwable cause) throws Exception;
	
	/**
	 * 当主动调用HttpSession的close时调用
	 * @throws Exception
	 * 2016年1月20日 上午10:36:20
	 */
	void filterClose(HttpSession session) throws Exception;
}
