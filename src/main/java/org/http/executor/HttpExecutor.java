package org.http.executor;

import java.io.IOException;

import org.http.HttpClientFactory;
import org.http.HttpRequest;
import org.http.HttpResponseMessage;

/**
 * http执行器,对应了对应的API
 * @author zouziwen
 *
 * 2016年7月6日 下午2:19:15
 */
public interface HttpExecutor {
	
	public HttpResponseMessage execute(HttpClientFactory clientFactory , HttpRequest request) throws IOException ;
}
