package org.http.client;

import java.util.concurrent.ExecutorService;

import org.http.HttpResponseMessage;
import org.http.chain.HttpHandler;
import org.http.chain.HttpHandlerAdapter;
import org.http.chain.HttpSession;
import org.http.chain.support.BaseHttpFilterChain;
import org.http.chain.util.NamePreservingRunnable;

/**
 * httpclient的具体处理链
 * 
 * @author zouziwen
 *
 *         2016年1月20日 上午11:01:31
 */
class HttpClientFilterChain extends BaseHttpFilterChain {

	static final HttpHandler DefaultHandler = new HttpHandlerAdapter();

	private final ExecutorService executor;

	private final String threadName = HttpClientFilterChain.class.getName() + ".thread";

	public HttpClientFilterChain(ExecutorService executor) {
		this.executor = executor;
	}

	@Override
	protected void doClose(HttpSession session) {
	}

	@Override
	public void fireSessionCreated(final HttpSession session) {
		executeHandler(session, new Runner() {

			@Override
			public void run() throws Exception {
				session.getHandler().sessionCreated(session);
			}

		});
		startAsyncWork(session, new Runnable() {

			public void run() {
				HttpClientFilterChain.super.fireSessionCreated(session);
			}
		});
	}

	@Override
	public void fireSessionClosed(final HttpSession session) {
		executeHandler(session, new Runner() {

			@Override
			public void run() throws Exception {
				session.getHandler().sessionClosed(session);
			}

		});
		startAsyncWork(session, new Runnable() {

			public void run() {
				synchronized (session) {
					HttpClientFilterChain.super.fireSessionClosed(session);
				}
			}
		});

	}

	@Override
	public void fireRequestFailed(final HttpSession session, final HttpResponseMessage responseMessage) {
		executeHandler(session, new Runner() {

			@Override
			public void run() throws Exception {
				session.getHandler().requestFailed(session, responseMessage);
			}

		});
		startAsyncWork(session, new Runnable() {

			public void run() {
				HttpClientFilterChain.super.fireRequestFailed(session, responseMessage);

			}
		});

	}

	@Override
	public void fireRequestSuccessed(final HttpSession session, final HttpResponseMessage responseMessage) {
		executeHandler(session, new Runner() {

			@Override
			public void run() throws Exception {
				session.getHandler().requestSuccessed(session, responseMessage);
			}

		});
		startAsyncWork(session, new Runnable() {

			public void run() {
				HttpClientFilterChain.super.fireRequestSuccessed(session, responseMessage);

			}
		});
	}

	@Override
	public void fireExceptionCaught(final HttpSession session, final Throwable cause) {
		executeHandler(session, new Runner() {

			@Override
			public void run() throws Exception {
				session.getHandler().exceptionCaught(session, cause);

			}

		});
		startAsyncWork(session, new Runnable() {

			public void run() {
				HttpClientFilterChain.super.fireExceptionCaught(session, cause);
			}
		});
	}

	@Override
	public void fireFilterClose(final HttpSession session) {

		super.fireFilterClose(session);
	}

	/**
	 * 开启工作线程
	 * 
	 * @param runnable
	 *            2016年1月20日 下午1:42:13
	 */
	private void startAsyncWork(HttpSession session, Runnable runnable) {
		synchronized (session) {
			executor.execute(new NamePreservingRunnable(runnable, threadName));
		}
	}

	/**
	 * 判断handler是否为空
	 * 
	 * @param session
	 * @return 2016年1月20日 下午1:42:02
	 */
	private boolean handlerEmpty(HttpHandler handler) {
		if (handler == null
				|| handler.getClass().getSimpleName().equalsIgnoreCase(HttpHandlerAdapter.class.getSimpleName())) {
			return false;
		}
		return true;
	}

	/**
	 * 同步执行handler
	 * 
	 * @param session
	 * @param run
	 *            2016年1月21日 上午10:54:24
	 */
	public void executeHandler(final HttpSession session, Runner run) {
		if (handlerEmpty(session.getHandler())) {
			try {
				run.run();
			} catch (final Exception e) {
				startAsyncWork(session, new Runnable() {

					public void run() {
						HttpClientFilterChain.super.fireExceptionCaught(session, e);
					}
				});
			}
		}
	}

	interface Runner {

		void run() throws Exception;

	}

}
