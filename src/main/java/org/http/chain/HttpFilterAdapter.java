package org.http.chain;

import org.http.HttpResponseMessage;

/**
 * http的处理过滤的适配器
 * @Notes 由于调用HttpFilter是异步调用HttpFilter的实现最好是线程安全的
 * @author zouziwen
 *
 * 2016年1月20日 下午7:53:16
 */
public class HttpFilterAdapter implements HttpFilter{

	@Override
	public void sessionCreated(NextHttpFilter nextFilter, HttpSession session) throws Exception {
		nextFilter.sessionCreated(session);
	}

	@Override
	public void sessionClosed(NextHttpFilter nextFilter, HttpSession session) throws Exception {
		nextFilter.sessionClosed(session);
	}

	@Override
	public void requestFailed(NextHttpFilter nextFilter, HttpSession session, HttpResponseMessage responseMessage)
			throws Exception {
		nextFilter.requestFailed(session, responseMessage);
	}

	@Override
	public void requestSuccessed(NextHttpFilter nextFilter, HttpSession session, HttpResponseMessage responseMessage)
			throws Exception {
		nextFilter.requestSuccessed(session, responseMessage);
	}

	@Override
	public void exceptionCaught(NextHttpFilter nextFilter, HttpSession session, Throwable cause) throws Exception {
		nextFilter.exceptionCaught(session, cause);
	}

	@Override
	public void filterClose(NextHttpFilter nextFilter, HttpSession session) throws Exception {
		
	}

}
