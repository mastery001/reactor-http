package org.http.chain;

import org.http.HttpResponseMessage;
import org.http.chain.util.SessionLog;

/**
 * HttpHandler的适配器
 * 
 * @author zouziwen
 *
 *         2016年1月19日 下午5:14:31
 */
public class HttpHandlerAdapter implements HttpHandler {

	@Override
	public void sessionCreated(HttpSession session) throws Exception {

	}

	@Override
	public void sessionClosed(HttpSession session) throws Exception {

	}

	@Override
	public void requestFailed(HttpSession session, HttpResponseMessage responseMessage) throws Exception {

	}

	@Override
	public void requestSuccessed(HttpSession session, HttpResponseMessage responseMessage) throws Exception {

	}

	@Override
	public void exceptionCaught(HttpSession session, Throwable cause) throws Exception {
		if (SessionLog.isWarnEnabled(session)) {
			SessionLog.warn(session,
					"EXCEPTION, please implement " + getClass().getName() + ".exceptionCaught() for proper handling:",
					cause);
		}
	}

	@Override
	public void filterClose(HttpSession session) throws Exception {

	}

}
