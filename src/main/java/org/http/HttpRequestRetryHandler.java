package org.http;

import java.io.IOException;

public interface HttpRequestRetryHandler {

	boolean retryRequest(IOException exception, int executionCount , HttpRequest httpRequest);
	
}
