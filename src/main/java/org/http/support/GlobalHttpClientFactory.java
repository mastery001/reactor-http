package org.http.support;

public abstract class GlobalHttpClientFactory {

	private static DefaultHttpClientFactory instance = new DefaultHttpClientFactory();

	public static DefaultHttpClientFactory getInstance() {
		return instance;
	}

	public static void reset() {
		instance = new DefaultHttpClientFactory();
	}
	
}
