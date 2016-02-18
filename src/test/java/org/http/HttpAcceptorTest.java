package org.http;

import static org.junit.Assert.fail;

import java.util.Arrays;
import java.util.concurrent.CountDownLatch;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.http.chain.HttpFilterAdapter;
import org.http.chain.HttpSession;
import org.http.client.HttpClientAcceptor;
import org.http.client.method.HttpGetRequest;
import org.http.exception.HttpInvokeException;
import org.junit.Test;

public class HttpAcceptorTest {

	final static HttpClientAcceptor client = new HttpClientAcceptor(new HttpClientFactory() {

		@Override
		public HttpClient getConnection() {
			MultiThreadedHttpConnectionManager connectionManager = new MultiThreadedHttpConnectionManager();
			connectionManager.getParams().setConnectionTimeout(1500);
			connectionManager.getParams().setSoTimeout(4000);
			connectionManager.getParams().setDefaultMaxConnectionsPerHost(5);
			HttpClient httpClient = new HttpClient(connectionManager);
			httpClient.getHostConfiguration().setProxy("123.125.65.82", 80);
			return httpClient;
		}
		
	});
	
	public static void main(String[] args) throws InterruptedException {
		HttpAcceptorTest test = new HttpAcceptorTest();
		client.getFilterChain().addLast("test", new HttpFilterAdapter() {

			@Override
			public void sessionCreated(NextHttpFilter nextFilter, HttpSession session) throws Exception {
//				BigDecimal sum = new BigDecimal(1);
//				for (int i = 1; i < 20; i++) {
//					sum = sum.multiply(BigDecimal.valueOf(i));
//				}
				session.setAttribute("11", 1);
			}

			@Override
			public void requestSuccessed(NextHttpFilter nextFilter, HttpSession session,
					HttpResponseMessage responseMessage) throws Exception {
				System.out.println(session.getName() + "====" + session.getAttribute("11") + "---"
						+ responseMessage.getContent().length());
				//System.out.println(Arrays.toString(responseMessage.getResponseHeaders()));
			}

		});
		long start = System.currentTimeMillis();
		int count = 10000000;
		CountDownLatch c = new CountDownLatch(count);
		for (int i = 0; i < count; i++) {
			new Thread(test.new Request(c,i)).start();
		}
		c.await();
		long end =  System.currentTimeMillis();
		System.out.println(end - start);
	}
	
	class Request implements Runnable {

		private CountDownLatch c;
		
		private int i;
		
		public Request(CountDownLatch c , int i) {
			this.c = c;
			this.i = i;
		}
		
		@Override
		public void run() {
			HttpGetRequest request = new HttpGetRequest("http://www.baidu.com/s?wd=" + i , true);
			try {
				client.service(request);
			} catch (HttpInvokeException e) {
				//e.printStackTrace();
			}
			c.countDown();
		}
		
	}

	@Test
	public void testServiceHttpRequest() {
	}

	@Test
	public void testServiceHttpRequestHttpHandler() {
		fail("Not yet implemented");
	}

}
