package org.http.exception;

/**
 * 当视图转换http返回的结果发生错误时抛出的异常
 * 		其对应的httpInvoke的ErrorCode是{@link InvokeErrorCode}.HTTP_RESPONSE_CONVERT
 * @author zouziwen
 *
 * 2016年1月26日 下午2:47:10
 */
public class HttpResponseProcessException extends HttpInvokeException {

	/**
	 * 2016年1月26日 下午2:37:44
	 */
	private static final long serialVersionUID = 553965611660221706L;

	public HttpResponseProcessException(String message) {
		super(message);
		setErrorCode(InvokeErrorCode.HTTP_RESPONSE_PROCESS);
	}

	public HttpResponseProcessException(Throwable cause) {
		super(InvokeErrorCode.HTTP_RESPONSE_PROCESS, cause);
	}

}
