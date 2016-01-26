package org.http.exception;

/**
 * http连接强制关闭异常
 * @author zouziwen
 *
 * 2016年1月20日 下午5:54:07
 */
@SuppressWarnings("serial")
public class HttpSessionClosedException extends HttpInvokeException{

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

	@Override
	public synchronized Throwable getCause() {
		return this;
	}


	public String getAddress() {
		return address;
	}
}
