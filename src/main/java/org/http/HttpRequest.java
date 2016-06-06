package org.http;

import org.apache.http.HttpMessage;
import org.apache.http.client.methods.HttpUriRequest;

public interface HttpRequest extends HttpUriRequest , HttpMessage{
	
	
	/**
	 * 是否重试
	 * @return
	 * 2016年6月2日 下午3:29:03
	 */
	boolean isRetry();
	
	/**
	 * 获取真实的request请求
	 * @return
	 * 2016年6月2日 下午9:47:38
	 */
	HttpUriRequest concreteRequest();
	
}
