package org.http;

public interface HttpParameterOperation {

	/**
	 * 添加请求参数
	 * 当添加的value为null时不做添加操作
	 * @param paramName
	 * @param paramValue
	 * @return 2016年1月19日 下午4:54:30
	 */
	HttpParameterOperation addParameter(String paramName, Object paramValue);
	
	
	/**
	 * Note：必须调用，否则请求的将是空地址
	 * @return
	 * 2016年6月1日 下午8:42:21
	 */
//	HttpRequest prepare();
}
