package org.http.support;

import org.http.HttpResponseMessage;
import org.http.HttpResponseProcessor;
import org.http.exception.HttpResponseProcessException;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

/**
 * 默认http返回jsop数据的处理器 返回的是传递参数Class<T> clazz的对应的类
 * 
 * @author mastery
 * @time 2016年1月19日下午10:22:21
 */
public class HttpJsonProcessor implements HttpResponseProcessor {

	/**
	 * 单实例 2016年1月26日 下午2:35:51
	 */
	private static final HttpJsonProcessor instance = new HttpJsonProcessor();

	public static HttpJsonProcessor getInstance() {
		return instance;
	}

	public HttpJsonProcessor() {
	}

	@Override
	public <T> T handleHttpResponse(HttpResponseMessage response, Class<T> clazz) throws HttpResponseProcessException {
		if (response != null) {
			try {
				JSONObject resultJsonObject = JSON.parseObject(response.getContent());
				return innerHandleResponse(resultJsonObject, clazz);
			} catch (Exception e) {
				throw new HttpResponseProcessException(e);
			}
		}
		return null;
	}

	protected <T> T innerHandleResponse(JSONObject resultJsonObject, Class<T> clazz) throws Exception {
		return JSON.toJavaObject(resultJsonObject, clazz);
	}

}
