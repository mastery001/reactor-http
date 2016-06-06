package org.http;

import java.io.IOException;

import org.apache.http.client.methods.HttpUriRequest;

public interface HttpRequestRetryHandler {

	boolean retryRequest(IOException exception, int executionCount , HttpUriRequest httpRequest);
	
}
