package org.http;

import org.apache.http.HttpResponse;
import org.http.exception.HttpResponseProcessException;

public interface HttpResponseMessage extends HttpResponse{

	int SUCCESS_CODE = 200;
	
	boolean isSuccess();
	
	/**
	 * 返回默认为UTF-8的字符
	 * @return
	 * @throws HttpResponseProcessException
	 * 2016年6月4日 下午4:35:36
	 */
	String getContent() throws HttpResponseProcessException;
	
	/**
	 * 获得二进制的内容
	 * @return
	 * 2016年6月4日 下午4:35:59
	 * @throws HttpResponseProcessException 
	 */
	byte[] getResponseBody() throws HttpResponseProcessException;
}
