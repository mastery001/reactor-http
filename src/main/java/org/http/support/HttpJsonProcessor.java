package org.http.support;

import org.http.HttpResponseMessage;
import org.http.HttpResponseProcessor;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

/**
 * 抽象http返回jsop数据的处理器
 * 
 * @author mastery
 * @time 2016年1月19日下午10:22:21
 */
public abstract class HttpJsonProcessor implements HttpResponseProcessor {

	@Override
	public <T> T handleHttpResponse(HttpResponseMessage response, Class<T> clazz) throws Exception {
		if (response != null) {
			JSONObject resultJsonObject = JSON.parseObject(response.getContent());
			return innerHandleResponse(resultJsonObject, clazz);
		}
		return null;
	}

	protected abstract <T> T innerHandleResponse(JSONObject resultJsonObject, Class<T> clazz);

}
