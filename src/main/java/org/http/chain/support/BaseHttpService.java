package org.http.chain.support;

import org.http.chain.DefaultHttpFilterChainBuilder;
import org.http.chain.HttpFilterChainBuilder;
import org.http.chain.HttpService;

public abstract class BaseHttpService implements HttpService {
	private HttpFilterChainBuilder filterChainBuilder = new DefaultHttpFilterChainBuilder();

	@Override
	public HttpFilterChainBuilder getFilterChainBuilder() {
		return filterChainBuilder;
	}

	@Override
	public DefaultHttpFilterChainBuilder getFilterChain() {
		if (filterChainBuilder instanceof DefaultHttpFilterChainBuilder) {
			return (DefaultHttpFilterChainBuilder) filterChainBuilder;
		} else {
			throw new IllegalStateException("Current filter chain builder is not a DefaultHttpFilterChainBuilder.");
		}
	}

	@Override
	public void close() throws Exception {
		getFilterChain().clear();
	}

}
