package org.http.exception;

/**
 * http调用异常
 *  该异常一般都嵌套了一个其代表的真实异常,但是调用者
 *  @Notes: 该异常只是异常的包装器，不接收与Throwable无关的参数（当然InvokeErrorCode除外）
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
		setErrorCode(InvokeErrorCode.CUSTOM_EXCEPTION);
	}
	
	protected HttpInvokeException(String message) {
		super(message);
	}

	public HttpInvokeException(Throwable cause) {
		super(cause);
	}

	public HttpInvokeException(InvokeErrorCode errorCode, Throwable cause) {
		super(cause.getMessage(), cause);
		if(errorCode != null) {
			setErrorCode(errorCode);
		}
	}

	protected void setErrorCode(InvokeErrorCode errorCode) {
		this.errorCode = errorCode;
	}

	public InvokeErrorCode getErrorCode() {
		return errorCode;
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
		 * 当将http返回的数据转换出错时的异常 2016年1月26日 下午2:40:16
		 */
		HTTP_RESPONSE_PROCESS("http response process exception"),

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
