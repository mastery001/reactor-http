package org.http;

import static org.junit.Assert.fail;

import java.math.BigDecimal;
import java.util.concurrent.CountDownLatch;

import org.http.chain.HttpFilterAdapter;
import org.http.chain.HttpSession;
import org.http.client.HttpClientAcceptor;
import org.http.client.method.HttpGetRequest;
import org.http.exception.HttpInvokeException;
import org.http.filter.LoggerFilter;
import org.junit.Test;

public class HttpAcceptorTest {

	public static final HttpClientAcceptor client = new HttpClientAcceptor();
	
	public static void main(String[] args) throws InterruptedException {
		HttpAcceptorTest test = new HttpAcceptorTest();
		client.getFilterChain().addLast("loggerFilter", new LoggerFilter());
		client.getFilterChain().addLast("test", new HttpFilterAdapter() {

			@Override
			public void sessionCreated(NextHttpFilter nextFilter, HttpSession session) throws Exception {
				BigDecimal sum = new BigDecimal(1);
				for (int i = 1; i < 20; i++) {
					sum = sum.multiply(BigDecimal.valueOf(i));
				}
				//TimeUnit.SECONDS.sleep(2);
				session.setAttribute("11", 1);
			}

			@Override
			public void requestSuccessed(NextHttpFilter nextFilter, HttpSession session,
					HttpResponseMessage responseMessage) throws Exception {
				System.out.println(session.getHttpRequest().getURI() + "====" + session.getAttribute("11") + "---"
						+ responseMessage.getContent().length());
				//System.out.println(Arrays.toString(responseMessage.getResponseHeaders()));
			}

		});
		long start = System.currentTimeMillis();
		int count = 100;
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
			HttpGetRequest request = new HttpGetRequest("http://www.baidu.com/s");
			request.addParameter("wd", "ceshi" + i);
			request.addParameter("ie", "utf-8");
			try {
				client.service(request);
			} catch (HttpInvokeException e) {
				
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
