package org.http;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.http.exception.HttpResponseProcessException;

public interface HttpResponseMessage extends CloseableHttpResponse{

	int SUCCESS_CODE = 200;
	
	boolean isSuccess();
	
	/**
	 * 返回默认为UTF-8的字符
	 * @see HttpResponseMessage#content
	 * @return
	 * 2016年6月4日 下午4:35:36
	 */
	String getContent();
	
	/**
	 * 得到内容，忽略HttpResponseProcessException异常
	 * 不建议使用#getEntity方法，因为该方法得到是null
	 * @throws HttpResponseProcessException
	 * @return
	 * 2016年7月6日 下午2:53:59
	 */
	String content() throws HttpResponseProcessException;
	
	/**
	 * 获得二进制的内容
	 * @return
	 * 2016年6月4日 下午4:35:59
	 * @throws HttpResponseProcessException 
	 */
	byte[] getResponseBody() throws HttpResponseProcessException;
	
	/**
	 * 关闭连接和消费实体，不抛出异常
	 */
	void closeQuietly();
}
