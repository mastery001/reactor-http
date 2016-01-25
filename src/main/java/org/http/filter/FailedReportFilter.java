package org.http.filter;

import org.http.HttpResponseMessage;
import org.http.chain.HttpFilterAdapter;
import org.http.chain.HttpSession;

/**
 * 错误上报过滤器
 * 
 * @author zouziwen
 *
 *         2016年1月25日 上午11:42:35
 */
public abstract class FailedReportFilter extends HttpFilterAdapter {

	public FailedReportFilter() {
	}

	@Override
	public void exceptionCaught(NextHttpFilter nextFilter, HttpSession session, Throwable cause) throws Exception {
		report(session.getName(), cause);
		nextFilter.exceptionCaught(session, cause);
	}

	@Override
	public void requestFailed(NextHttpFilter nextFilter, HttpSession session, HttpResponseMessage responseMessage)
			throws Exception {
		report(session.getName(), responseMessage);
		nextFilter.requestFailed(session, responseMessage);
	}

	/**
	 * 具体的上报实现
	 * 
	 * @param url	concrete request's url
	 * @param message
	 *            message there are two possible ，one is {@link Throwable }，
	 *            other is {@link HttpResponseMessage} 2016年1月25日 下午12:52:11
	 */
	protected abstract void report(String url, Object message);

}
