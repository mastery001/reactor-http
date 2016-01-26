package org.http;

import org.http.exception.HttpResponseProcessException;

/**
 * HttpResponse的结果处理器
 * 
 * @author zouziwen
 *
 *         2016年1月18日 下午3:49:31
 */
public interface HttpResponseProcessor<T> {

	/**
	 * 处理http返回的response信息
	 * 
	 * @param response
	 * @return
	 * @throws Exception
	 *             2016年1月19日下午10:15:54
	 */
	public T handleHttpResponse(HttpResponseMessage response) throws HttpResponseProcessException;

}
