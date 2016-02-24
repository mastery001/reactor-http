package org.http.filter;

import org.http.HttpResponseMessage;
import org.http.chain.HttpFilterAdapter;
import org.http.chain.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 日志过滤器，只负责记录日志
 * 
 * @author zouziwen
 *
 *         2016年2月24日 下午1:53:00
 */
public class LoggerFilter extends HttpFilterAdapter {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	@Override
	public void requestFailed(NextHttpFilter nextFilter, HttpSession session, HttpResponseMessage responseMessage)
			throws Exception {
		logger.error("调用http请求失败: " + session.getRequestMessage().getCompleteUrl() + ",耗时：" + session.getSurvivalTime() + "ms, 响应码: "
				+ responseMessage.getStatusCode());
		nextFilter.requestFailed(session, responseMessage);
	}

	@Override
	public void requestSuccessed(NextHttpFilter nextFilter, HttpSession session, HttpResponseMessage responseMessage)
			throws Exception {
		logger.info("调用http请求成功: " + session.getRequestMessage().getCompleteUrl()  + ",耗时：" + session.getSurvivalTime() + "ms, 响应码: "
				+ responseMessage.getStatusCode());
		nextFilter.requestSuccessed(session, responseMessage);
	}

	@Override
	public void exceptionCaught(NextHttpFilter nextFilter, HttpSession session, Throwable cause) throws Exception {
		logger.info("调用http请求异常: " + session.getRequestMessage().getCompleteUrl()  + ",耗时：" + session.getSurvivalTime() + "ms, exception:"
				+ cause.getMessage());
		nextFilter.exceptionCaught(session, cause);
	}

}
