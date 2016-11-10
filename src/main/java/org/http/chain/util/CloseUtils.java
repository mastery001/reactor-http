package org.http.chain.util;

import java.io.IOException;

import org.http.HttpRequest;
import org.http.HttpResponseMessage;
import org.http.Releaseable;

public class CloseUtils {

	/**
	 * 关闭请求
	 * 
	 * @param request
	 */
	public static void closeRequest(HttpRequest request) {
		if (request instanceof Releaseable) {
			((Releaseable) request).release();
		}
	}

	/**
	 * 关闭请求，ignore exception
	 * @param request
	 */
	public static void closeRequestQuietly(HttpRequest request) {
		try {
			closeRequest(request);
		} catch (Exception e) {
			ExceptionMonitor.getInstance().exceptionCaught(e);
		}
	}

	/**
	 * 关闭response
	 * @param response
	 * @throws IOException
	 */
	public static void closeResponse(HttpResponseMessage response) throws IOException {
		if (response != null) {
			response.close();
		}
	}

	/**
	 * 关闭response，ignore exception
	 * @param response
	 */
	public static void closeResponseQuietly(HttpResponseMessage response) {
		if (response != null) {
			response.closeQuietly();
		}
	}

	/**
	 * 关闭request和response
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	public static void closeAll(HttpRequest request, HttpResponseMessage response) throws IOException {
		closeResponse(response);
		closeRequest(request);
	}

	/**
	 * 关闭request和response，ignore exception
	 * @param request
	 * @param response
	 */
	public static void closeAllQuietly(HttpRequest request, HttpResponseMessage response) {
		closeResponseQuietly(response);
		closeRequestQuietly(request);
	}
}
