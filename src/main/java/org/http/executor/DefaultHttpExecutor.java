package org.http.executor;

import java.io.IOException;

import org.http.HttpClientFactory;
import org.http.HttpRequest;
import org.http.HttpResponseMessage;
import org.http.chain.support.DefaultHttpResponseMessage;

class DefaultHttpExecutor implements HttpExecutor {

	@Override
	public HttpResponseMessage execute(HttpClientFactory clientFactory, HttpRequest request) throws IOException {
		if (clientFactory != null && clientFactory.getConnection() != null && request != null
				&& request.concreteRequest() != null) {
			return new DefaultHttpResponseMessage(clientFactory.getConnection().execute(request.concreteRequest()));
		}
		return null;
	}

}
