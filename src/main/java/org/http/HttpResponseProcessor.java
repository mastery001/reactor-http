package org.http;

import org.http.exception.HttpResponseProcessException;

/**
 * HttpResponse的结果处理器
 * 
 * @author zouziwen
 *
 *         2016年1月18日 下午3:49:31
 */
public interface HttpResponseProcessor {

	/**
	 * 处理http返回的response信息
	 * 
	 * @param response
	 * @param clazz
	 *            返回的结果类型
	 * @return
	 * @throws Exception
	 *             2016年1月19日下午10:15:54
	 */
	public <T> T handleHttpResponse(HttpResponseMessage response, Class<T> clazz) throws HttpResponseProcessException;

}
