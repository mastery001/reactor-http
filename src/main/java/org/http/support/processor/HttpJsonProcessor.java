package org.http.support.processor;

import org.http.HttpResponseMessage;
import org.http.HttpResponseProcessor;
import org.http.exception.HttpResponseProcessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

/**
 * 默认http返回jsop数据的处理器 返回的是传递参数<T>的对应的类
 * 
 * @author mastery
 * @time 2016年1月19日下午10:22:21
 */
public abstract class HttpJsonProcessor<T> implements HttpResponseProcessor<T> {

	@Override
	public T handleHttpResponse(HttpResponseMessage response) throws HttpResponseProcessException {
		if (response != null) {
			try {
				JSONObject resultJsonObject = JSON.parseObject(response.getContent());
				getLogger().info("return json is : " + resultJsonObject);
				return innerHandleResponse(resultJsonObject);
			} catch (HttpResponseProcessException e) {
				throw e;
			} catch (Exception e) {
				throw new HttpResponseProcessException(e);
			}
		}
		return null;
	}

	/**
	 * 具体的对json对象的处理逻辑
	 * 
	 * @param resultJsonObject
	 * @return
	 * @throws Exception
	 *             2016年1月26日 下午6:28:28
	 */
	protected abstract T innerHandleResponse(JSONObject resultJsonObject) throws Exception;

	private Logger getLogger() {
		return LoggerFactory.getLogger(this.getClass());
	}
}
