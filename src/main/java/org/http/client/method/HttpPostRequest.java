package org.http.client.method;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.message.BasicNameValuePair;
import org.http.HttpRequest;
import org.http.support.BaseHttpRequest;

public class HttpPostRequest extends BaseHttpRequest{

	private final List <NameValuePair> nvps = new ArrayList <NameValuePair>();
	
	public HttpPostRequest(String baseUrl) {
		super(baseUrl);
	}

	public HttpPostRequest(String baseUrl, boolean isRetry) {
		super(baseUrl, isRetry);
	}

	@Override
	protected HttpRequestBase initRequest(String baseUrl) {
		return new HttpPost(baseUrl);
	}

	@Override
	public HttpRequest addParameter(String paramName, Object paramValue) {
		nvps.add(new BasicNameValuePair(paramName, String.valueOf(paramValue)));
		return this;
	}

	@Override
	public HttpRequest prepare() {
		HttpPost post = (HttpPost) getRequest();
		try {
			post.setEntity(new UrlEncodedFormEntity(nvps));
		} catch (UnsupportedEncodingException e) {
			// 防止setEntity报错时,调用父类的方法,容错
			for(NameValuePair pair : nvps) {
				super.addParameter(pair.getName(), pair.getValue());
			}
			super.prepare();
		}
		return this;
	}
	
	

}
