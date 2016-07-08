package org.http.executor;

public abstract class GlobalHttpExecutor {
	
	/**
	 * Keep track of a single instance so we can return it to classes that request it.
	 */
	private static HttpExecutor instance = new DefaultHttpExecutor();

	/**
	 * Return the singleton {@link DefaultHttpExecutor} instance.
	 */
	public static HttpExecutor getInstance() {
		return instance;
	}
}
