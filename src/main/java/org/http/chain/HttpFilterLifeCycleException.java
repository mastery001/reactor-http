package org.http.chain;

public class HttpFilterLifeCycleException extends RuntimeException {
	private static final long serialVersionUID = -5542098881633506449L;

	public HttpFilterLifeCycleException() {
	}

	public HttpFilterLifeCycleException(String message) {
		super(message);
	}

	public HttpFilterLifeCycleException(String message, Throwable cause) {
		super(message, cause);
	}

	public HttpFilterLifeCycleException(Throwable cause) {
		super(cause);
	}
}
