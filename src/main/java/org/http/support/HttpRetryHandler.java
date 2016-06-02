package org.http.support;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.UnknownHostException;

import javax.net.ssl.SSLException;

import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.conn.ConnectTimeoutException;
import org.http.HttpRequestRetryHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpRetryHandler implements HttpRequestRetryHandler {

	private final Logger logger = LoggerFactory.getLogger(getClass());
	
	/** the number of times a method will be retried */
	private int retryCount;

	/**
	 * Creates a new DefaultHttpMethodRetryHandler that retries up to 3 times
	 * but does not retry methods that have successfully sent their requests.
	 */
	public HttpRetryHandler() {
		this(3);
	}

	/**
	 * Creates a new DefaultHttpMethodRetryHandler that retries up to 3 times
	 * but does not retry methods that have successfully sent their requests.
	 */
	public HttpRetryHandler(int retryCount) {
		this.retryCount = retryCount;
	}
	
	  /**
     * @return the maximum number of times a method will be retried
     */
    public int getRetryCount() {
        return retryCount;
    }

	@Override
	public boolean retryRequest(IOException exception, int executionCount, org.http.HttpRequest httpRequest) {
		if (executionCount > this.retryCount) {
			// Do not retry if over max retry count
			return false;
		}
		logger.info("第"+executionCount+"次重试..........");
		if (exception instanceof InterruptedIOException) {
			// Timeout
			return true;
		}
		if (exception instanceof UnknownHostException) {
			// Unknown host
			return true;
		}
		if (exception instanceof ConnectTimeoutException) {
			// Connection refused
			return true;
		}
		if (exception instanceof SSLException) {
			// SSL handshake exception
			return true;
		}
		boolean idempotent = !(httpRequest instanceof HttpEntityEnclosingRequest);
		if (idempotent) {
			// Retry if the request is considered idempotent
			return true;
		}
		return false;
	}

}
