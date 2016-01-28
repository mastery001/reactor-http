package org.http.client;

import org.http.chain.HttpSession;
import org.http.chain.support.BaseHttpFilterChain;

/**
 * httpclient的具体处理链
 * 
 * @author zouziwen
 *
 *         2016年1月20日 上午11:01:31
 */
class HttpClientFilterChain extends BaseHttpFilterChain {
	
//	public HttpClientFilterChain(HttpSession session) {
//	}

	@Override
	protected void doClose(HttpSession session) {
	}

}
