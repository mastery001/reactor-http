package org.http.chain.util;

/**
 * http连接关闭异常
 * @author zouziwen
 *
 * 2016年1月20日 下午5:54:07
 */
@SuppressWarnings("serial")
public class HttpSessionClosedException extends RuntimeException{

	/**
	 * 关闭的url地址
	 * 2016年1月20日 下午5:54:34
	 */
	private String address;
	
	public HttpSessionClosedException(String address) {
		this.address = address;
	}

	@Override
	public String getMessage() {
		return "Http Request is Closed , current request address is " + address;
	}

	public String getAddress() {
		return address;
	}
}
