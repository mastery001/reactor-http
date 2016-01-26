package org.http.exception;

import org.http.exception.HttpInvokeException.InvokeErrorCode;

/**
 * http连接强制关闭异常
 * 		其对应的httpInvoke的ErrorCode是{@link InvokeErrorCode}.SESSION_CLOSE
 * @author zouziwen
 *
 * 2016年1月20日 下午5:54:07
 */
public class HttpSessionClosedException extends HttpInvokeException{

	/**
	 * 2016年1月26日 下午2:52:31
	 */
	private static final long serialVersionUID = -6722906166497125388L;
	
	/**
	 * 关闭的url地址
	 * 2016年1月20日 下午5:54:34
	 */
	private String address;
	
	public HttpSessionClosedException(String address) {
		this.address = address;
		setErrorCode(InvokeErrorCode.SESSION_CLOSE);
	}

	@Override
	public String getMessage() {
		return "Http Request is Closed , current request address is " + address;
	}

	public String getAddress() {
		return address;
	}
}
