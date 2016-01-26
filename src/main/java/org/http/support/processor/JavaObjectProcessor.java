package org.http.support.processor;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

/**
 * 转换json成java对象的处理器
 * @author zouziwen
 *
 * @param <T>
 * 2016年1月26日 下午6:27:41
 */
public class JavaObjectProcessor<T> extends HttpJsonProcessor<T>{

	private Class<T> clazz;
	
	public JavaObjectProcessor(Class<T> clazz) {
		this.clazz = clazz;
	}
	
	@Override
	protected T innerHandleResponse(JSONObject resultJsonObject) throws Exception {
		return JSON.toJavaObject(resultJsonObject, clazz);
	}

}
