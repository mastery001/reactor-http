package org.http;

import org.apache.http.HttpMessage;
import org.apache.http.client.methods.HttpUriRequest;

/**
 * http请求
 * Apache HttpClient Proxy
 * @author zouziwen
 *
 */
public interface HttpRequest extends HttpUriRequest , HttpMessage , HttpParameterOperation{
	
	/**
	 * 获取真实的request请求
	 * @return
	 * 2016年6月2日 下午9:47:38
	 */
	HttpUriRequest concreteRequest();
	
}
