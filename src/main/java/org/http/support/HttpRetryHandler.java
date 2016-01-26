package org.http.support;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.NoRouteToHostException;
import java.net.UnknownHostException;

import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.HttpMethodRetryHandler;
import org.apache.commons.httpclient.NoHttpResponseException;

/**
 * The default {@link HttpMethodRetryHandler} used by {@link HttpMethod}s.
 * 
 * @author Michael Becke
 * @author <a href="mailto:oleg -at- ural.ru">Oleg Kalnichevski</a>
 */
public class HttpRetryHandler implements HttpMethodRetryHandler {


    private static Class<?> SSL_HANDSHAKE_EXCEPTION = null;
    
    static {
        try {
            SSL_HANDSHAKE_EXCEPTION = Class.forName("javax.net.ssl.SSLHandshakeException");
        } catch (ClassNotFoundException ignore) {           
        }
    }
    /** the number of times a method will be retried */
    private int retryCount;
    
    /** Whether or not methods that have successfully sent their request will be retried */
    private boolean requestSentRetryEnabled;
    
    /**
     * Creates a new DefaultHttpMethodRetryHandler.
     * @param retryCount the number of times a method will be retried
     * @param requestSentRetryEnabled if true, methods that have successfully sent their request will be retried
     */
    public HttpRetryHandler(int retryCount, boolean requestSentRetryEnabled) {
        super();
        this.retryCount = retryCount;
        this.requestSentRetryEnabled = requestSentRetryEnabled;
    }
    
    /**
     * Creates a new DefaultHttpMethodRetryHandler that retries up to 3 times
     * but does not retry methods that have successfully sent their requests.
     */
    public HttpRetryHandler() {
        this(3, false);
    }
    /**
     * Creates a new DefaultHttpMethodRetryHandler that retries up to 3 times
     * but does not retry methods that have successfully sent their requests.
     */
    public HttpRetryHandler(int retryCount) {
        this(retryCount, false);
    }
    /** 
     * Used <code>retryCount</code> and <code>requestSentRetryEnabled</code> to determine
     * if the given method should be retried.
     * 
     * @see HttpMethodRetryHandler#retryMethod(HttpMethod, IOException, int)
     */
    public boolean retryMethod(final HttpMethod method,  final IOException exception, int executionCount) {
        if (method == null) {
            throw new IllegalArgumentException("HTTP method may not be null");
        }
        if (exception == null) {
            throw new IllegalArgumentException("Exception parameter may not be null");
        }
        if (executionCount > this.retryCount) {
            // Do not retry if over max retry count
            return false;
        }
        System.out.println("第"+executionCount+"次重试..........");
        if (exception instanceof NoHttpResponseException) {
            // Retry if the server dropped connection on us
            return true;
        }
        if (exception instanceof InterruptedIOException) {
            // Timeout
            return true;
        }
        if (exception instanceof UnknownHostException) {
            // Unknown host
            return true;
        }
        if (exception instanceof NoRouteToHostException) {
            // Host unreachable
            return true;
        }
        if (SSL_HANDSHAKE_EXCEPTION != null && SSL_HANDSHAKE_EXCEPTION.isInstance(exception)) {
            // SSL handshake exception
            return true;
        }
        if (!method.isRequestSent() || this.requestSentRetryEnabled) {
            // Retry if the request has not been sent fully or
            // if it's OK to retry methods that have been sent
            return true;
        }
        // otherwise do not retry
        return false;
    }
    
    /**
     * @return <code>true</code> if this handler will retry methods that have 
     * successfully sent their request, <code>false</code> otherwise
     */
    public boolean isRequestSentRetryEnabled() {
        return requestSentRetryEnabled;
    }

    /**
     * @return the maximum number of times a method will be retried
     */
    public int getRetryCount() {
        return retryCount;
    }
}
