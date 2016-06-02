package org.http.filter;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.http.HttpResponseMessage;
import org.http.chain.HttpFilterAdapter;
import org.http.chain.HttpSession;
import org.http.chain.util.ExceptionMonitor;
import org.http.chain.util.NamePreservingRunnable;

/**
 * 错误上报过滤器
 * 	异步执行
 * @author zouziwen
 *
 *         2016年1月25日 上午11:42:35
 */
public abstract class FailedReportFilter extends HttpFilterAdapter {
	
	private static final String threadName = "FailedReportFilter-1";
	
	private ExecutorService executor;
	
	public FailedReportFilter() {
		executor = initThreadPool();
		if(executor == null) {
			executor = Executors.newCachedThreadPool();
		}
	}

	protected abstract ExecutorService initThreadPool();

	@Override
	public void exceptionCaught(NextHttpFilter nextFilter, HttpSession session, Throwable cause) throws Exception {
		innerReport(session.getName(), cause);
		nextFilter.exceptionCaught(session, cause);
	}

	@Override
	public void requestFailed(NextHttpFilter nextFilter, HttpSession session, HttpResponseMessage responseMessage)
			throws Exception {
		innerReport(session.getName(), responseMessage);
		nextFilter.requestFailed(session, responseMessage);
	}
	
	private void innerReport(final String url, final Object message) {
		executor.execute(new NamePreservingRunnable(new Runnable(){

			@Override
			public void run() {
				try {
					report(url, message);
				} catch (Exception e) {
					ExceptionMonitor.getInstance().exceptionCaught(e);
				}
			}
			
		}, threadName));
	}

	/**	
	 * 具体的上报实现
	 * 
	 * @param url	concrete request's url
	 * @param message
	 *            message there are two possible ，one is {@link Throwable }，
	 *            other is {@link HttpResponseMessage} 2016年1月25日 下午12:52:11
	 */
	protected abstract void report(String url, Object message);

}
