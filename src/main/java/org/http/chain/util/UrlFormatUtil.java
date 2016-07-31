package org.http.chain.util;

import java.util.List;

import org.apache.http.NameValuePair;

public class UrlFormatUtil {

	public static String formatUrl(String baseUrl, List<NameValuePair> params) {
		final StringBuilder urlBuilder = new StringBuilder();
		urlBuilder.append(baseUrl);
		if(params != null && !params.isEmpty()) {
			String join = Constant.INTERROGATION;
			// if have ?
			if (urlBuilder.indexOf(Constant.INTERROGATION) == -1) {
				urlBuilder.append(join);
				join = Constant.AND;
			}
			String value = null;
			for (NameValuePair nv : params) {
				value = nv.getValue();
				if (value == null) {
					value = "";
				}
				urlBuilder.append(join).append(nv.getName()).append(Constant.EQUAL).append(value);
			}
		}
		return urlBuilder.toString();
	}
}
