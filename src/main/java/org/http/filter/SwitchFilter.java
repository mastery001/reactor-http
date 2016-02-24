package org.http.filter;

import org.http.chain.HttpFilterAdapter;
import org.http.chain.HttpSession;

/**
 * 服务开关升降级
 * @author zouziwen
 *
 * 2016年2月24日 下午5:17:42
 */
public class SwitchFilter extends HttpFilterAdapter{

	@Override
	public void sessionCreated(NextHttpFilter nextFilter, HttpSession session) throws Exception {
		// TODO Auto-generated method stub
		super.sessionCreated(nextFilter, session);
	}

	@Override
	public void exceptionCaught(NextHttpFilter nextFilter, HttpSession session, Throwable cause) throws Exception {
		// TODO Auto-generated method stub
		super.exceptionCaught(nextFilter, session, cause);
	}

	
}
