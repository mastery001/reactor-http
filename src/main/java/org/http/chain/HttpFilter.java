package org.http.chain;

import org.http.HttpResponseMessage;

/**
 * http的处理过滤
 * 	在0.0.5（包括0.0.5）版本之前调用filter是属于异步调用的，之后的版本将filter的调用修改为同步调用
 * @Notes  HttpFilter的实现最好是线程安全的
 * 
 * @author zouziwen
 *
 *         2016年1月18日 下午4:52:39
 */
public interface HttpFilter {

	/**
	 * http连接开始创建
	 * 
	 * @param nextFilter
	 * @param session
	 * @throws Exception
	 *             2016年1月19日 下午12:36:25
	 */
	void sessionCreated(NextHttpFilter nextFilter, HttpSession session) throws Exception;

	/**
	 * http连接关闭
	 * 
	 * @param nextFilter
	 * @param session
	 * @throws Exception
	 *             2016年1月19日 下午12:36:34
	 */
	void sessionClosed(NextHttpFilter nextFilter, HttpSession session) throws Exception;

	/**
	 * 当http请求调用失败时调用
	 * 
	 * @param nextFilter
	 * @param session
	 * @param responseMessage
	 * @throws Exception
	 *             2016年1月19日 下午12:36:58
	 */
	void requestFailed(NextHttpFilter nextFilter, HttpSession session, HttpResponseMessage responseMessage)
			throws Exception;

	/**
	 * 当http请求调用成功时调用
	 * 
	 * @param nextFilter
	 * @param session
	 * @param responseMessage
	 * @throws Exception
	 *             2016年1月19日 下午12:37:06
	 */
	void requestSuccessed(NextHttpFilter nextFilter, HttpSession session, HttpResponseMessage responseMessage)
			throws Exception;

	/**
	 * 当出现异常时调用
	 * 
	 * @param nextFilter
	 * @param session
	 * @param cause
	 * @throws Exception
	 * 2016年1月19日 下午12:37:14
	 */
	void exceptionCaught(NextHttpFilter nextFilter, HttpSession session, Throwable cause) throws Exception;
	
	/**
	 * 当主动调用HttpSession的close时调用
	 * @throws Exception
	 * 2016年1月20日 上午10:36:20
	 */
	void filterClose(NextHttpFilter nextFilter, HttpSession session) throws Exception;

	public interface NextHttpFilter {
		/**
		 * http连接开始创建
		 * 
		 * @param session
		 * @throws Exception
		 *             2016年1月19日 下午12:36:25
		 */
		void sessionCreated(HttpSession session);

		/**
		 * http连接关闭
		 * 
		 * @param session
		 * @throws Exception
		 *             2016年1月19日 下午12:36:34
		 */
		void sessionClosed(HttpSession session);

		/**
		 * 当http请求调用失败时调用
		 * 
		 * @param session
		 * @param responseMessage
		 * @throws Exception
		 *             2016年1月19日 下午12:36:58
		 */
		void requestFailed(HttpSession session, HttpResponseMessage responseMessage);

		/**
		 * 当http请求调用成功时调用
		 * 
		 * @param session
		 * @param responseMessage
		 * @throws Exception
		 *             2016年1月19日 下午12:37:06
		 */
		void requestSuccessed(HttpSession session, HttpResponseMessage responseMessage);

		/**
		 * 当出现异常时调用
		 * 
		 * @param session
		 * @param cause
		 * @throws Exception
		 *             2016年1月19日 下午12:37:14
		 */
		void exceptionCaught(HttpSession session, Throwable cause);
		
		
		/**
		 * 当主动调用HttpSession的close时调用
		 * @throws Exception
		 * 2016年1月20日 上午10:36:20
		 */
		void filterClose(HttpSession session) throws Exception;
	}
}
