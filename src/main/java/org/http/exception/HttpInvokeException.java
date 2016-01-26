package org.http.exception;

/**
 * http调用异常
 * 
 * @author zouziwen
 *
 *         2016年1月25日 下午5:26:14
 */
public class HttpInvokeException extends Exception {

	/**
	 * 2016年1月25日 下午5:26:41
	 */
	private static final long serialVersionUID = -536087228129046605L;

	private InvokeErrorCode errorCode;

	protected HttpInvokeException() {
	}

	public HttpInvokeException(Throwable cause) {
		this(InvokeErrorCode.CUSTOM_EXCEPTION, cause);
	}

	public HttpInvokeException(InvokeErrorCode errorCode, Throwable cause) {
		super(cause);
		this.errorCode = errorCode;
	}

	public void setErrorCode(InvokeErrorCode errorCode) {
		this.errorCode = errorCode;
	}

	public InvokeErrorCode getErrorCode() {
		return errorCode;
	}

	@Override
	public String toString() {
		String s = getClass().getName();
		String message = getLocalizedMessage();
		String join = "errorCode is :" + errorCode.mesage + ";and concrete exception is :";
		return (message != null) ? (s + join + message) : s;
	}

	/**
	 * http调用时出现的错误代码
	 * 
	 * @author zouziwen
	 *
	 *         2016年1月25日 下午6:48:04
	 */
	public enum InvokeErrorCode {

		/**
		 * http连接回话关闭 2016年1月25日 下午7:39:56
		 */
		SESSION_CLOSE("http session closed"),

		/**
		 * http连接超时 2016年1月25日 下午7:40:14
		 */
		CONNECT_TIME_OUT("session connect time out"),

		/**
		 * socket连接超时 2016年1月25日 下午7:42:25
		 */
		SOCKET_CONNECT_TIME_OUT("socket session connect time out"),

		/**
		 * 当请求来不及处理报出异常 2016年1月25日 下午7:40:23
		 */
		NO_HTTP_RESPONSE(" a valid HTTP response"),

		/**
		 * 当抛出ProtocolException异常时的code 2016年1月25日 下午7:54:17
		 */
		HTTP_PROTOCOL_INVAILD("http protocol exception"),

		/**
		 * 未知类型的IOException 2016年1月25日 下午7:55:25
		 */
		UNKONW_IO_EXCEPTION("unkonw IOException was happened"),

		/**
		 * 未知类型的HttpException 2016年1月25日 下午7:55:37
		 */
		UNKONW_HTTP_EXCEPTION("unkonw HttpException was happened"),

		/**
		 * 自定义的code异常 2016年1月25日 下午7:55:56
		 */
		CUSTOM_EXCEPTION("custom's exception code");

		String mesage;

		InvokeErrorCode(String message) {
			this.mesage = message;
		}
	}

}
