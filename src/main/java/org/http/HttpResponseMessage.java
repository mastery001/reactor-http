package org.http;

import org.apache.http.HttpResponse;
import org.http.exception.HttpResponseProcessException;

public interface HttpResponseMessage extends HttpResponse{

	int SUCCESS_CODE = 200;
	
	boolean isSuccess();
	
	String getContent() throws HttpResponseProcessException;
}
