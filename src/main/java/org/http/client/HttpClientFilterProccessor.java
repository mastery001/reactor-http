package org.http.client;

import java.util.concurrent.ExecutorService;

import org.http.HttpClientFactory;
import org.http.HttpRequest;
import org.http.HttpResponseMessage;
import org.http.chain.HttpHandler;
import org.http.chain.HttpHandlerAdapter;
import org.http.chain.util.ExceptionMonitor;
import org.http.chain.util.NamePreservingRunnable;
import org.http.chain.util.Queue;
import org.http.exception.HttpInvokeException;
import org.http.exception.HttpSessionClosedException;

/**
 * HttpClientFilter的处理者 使用多线程处理请求
 * 
 * @author zouziwen
 *
 *         2016年1月19日 下午1:26:00
 */
class HttpClientFilterProccessor {

	final String threadName;

	private final ExecutorService executor;

	private Worker worker;

	private Queue createdSession = new Queue();

	private Queue exceptionSession = new Queue();

	private Queue closedSession = new Queue();

	private Queue successedSession = new Queue();

	private Queue failedSession = new Queue();

	public HttpClientFilterProccessor(String threadName, ExecutorService executor) {
		this.threadName = threadName;
		this.executor = executor;
	}

	HttpResponseMessage doWork(final HttpRequest request, final HttpClientSession session,
			final HttpClientFactory httpClientFactory, final HttpHandler handler)
					throws HttpSessionClosedException, HttpInvokeException {
		scheduleCreated(session, handler);
		HttpResponseMessage responseMessage = null;
		try {
			responseMessage = request.sendRequest(httpClientFactory);
			if (responseMessage.isSuccess()) {
				scheduleSuccessed(session, handler, responseMessage);
			} else {
				scheduleFaild(session, handler, responseMessage);
			}
		} catch (HttpInvokeException e) {
			scheduleException(session, handler, e);
			throw e;
		} finally {
			scheduleClosed(session, handler);
		}
		return responseMessage;
	}

	void scheduleCreated(HttpClientSession session, HttpHandler handler) throws HttpInvokeException {
		checkSessionClose(session);
		try {
			if (handlerEmpty(handler)) {
				handler.sessionCreated(session);
			}
		} catch (Exception e) {
			throw new HttpInvokeException(e);
		}
		synchronized (createdSession) {
			createdSession.push(session);
		}
		startupWorker();

	}

	void doSessionCreated() throws HttpSessionClosedException {
		if (createdSession.isEmpty())
			return;
		for (;;) {
			HttpClientSession session;

			synchronized (createdSession) {
				session = (HttpClientSession) createdSession.pop();
			}

			if (session == null) {
				break;
			}

			session.getFilterChain().fireSessionCreated(session);
		}

	}

	void checkSessionClose(HttpClientSession session) throws HttpSessionClosedException {
		if (session.isClose()) {
			throw new HttpSessionClosedException(session.getName());
		}
	}

	void scheduleClosed(HttpClientSession session, HttpHandler handler) throws HttpInvokeException {
		try {
			if (handlerEmpty(handler)) {
				handler.sessionClosed(session);
			}
		} catch (Exception e) {
			throw new HttpInvokeException(e);
		}
		checkSessionClose(session);

		synchronized (closedSession) {
			closedSession.push(session);
		}
		startupWorker();

	}

	void doSessionClosed() throws HttpSessionClosedException {

		if (closedSession.isEmpty())
			return;
		for (;;) {
			HttpClientSession session;

			synchronized (closedSession) {
				session = (HttpClientSession) closedSession.pop();
			}

			if (session == null)
				break;

			session.getFilterChain().fireSessionClosed(session);
		}

	}

	void scheduleSuccessed(HttpClientSession session, HttpHandler handler, HttpResponseMessage responseMessage)
			throws HttpInvokeException {
		try {
			if (handlerEmpty(handler)) {
				handler.requestSuccessed(session, responseMessage);
			}
		} catch (Exception e) {
			throw new HttpInvokeException(e);
		}
		checkSessionClose(session);

		synchronized (successedSession) {
			session.setAttachment(responseMessage);
			successedSession.push(session);
		}
		startupWorker();
	}

	void doRequestSuccessed() throws HttpSessionClosedException {
		if (successedSession.isEmpty())
			return;

		for (;;) {
			HttpClientSession session;

			synchronized (successedSession) {
				session = (HttpClientSession) successedSession.pop();
			}

			if (session == null)
				break;

			HttpResponseMessage responseMessage = (HttpResponseMessage) session.getAttachment();
			session.getFilterChain().fireRequestSuccessed(session, responseMessage);
		}
	}

	void scheduleFaild(HttpClientSession session, HttpHandler handler, HttpResponseMessage responseMessage)
			throws HttpInvokeException {
		checkSessionClose(session);
		try {
			if (handlerEmpty(handler)) {
				handler.requestFailed(session, responseMessage);
			}
		} catch (Exception e) {
			throw new HttpInvokeException(e);
		}
		synchronized (failedSession) {
			session.setAttachment(responseMessage);
			failedSession.push(session);
		}
		startupWorker();
	}

	void doRequestFaild() throws HttpSessionClosedException {
		if (failedSession.isEmpty())
			return;

		for (;;) {
			HttpClientSession session;

			synchronized (failedSession) {
				session = (HttpClientSession) failedSession.pop();
			}

			if (session == null)
				break;

			HttpResponseMessage responseMessage = (HttpResponseMessage) session.getAttachment();
			session.getFilterChain().fireRequestFailed(session, responseMessage);
		}

	}

	void scheduleException(HttpClientSession session, HttpHandler handler, Throwable cause) throws HttpInvokeException {
		checkSessionClose(session);
		try {
			if (handlerEmpty(handler)) {
				handler.exceptionCaught(session, cause);
			}
		} catch (Exception e) {
			throw new HttpInvokeException(e);
		}
		synchronized (exceptionSession) {
			session.setAttachment(cause);
			exceptionSession.push(session);
		}
		startupWorker();
	}

	void doExceptionCaught() throws HttpSessionClosedException {
		if (exceptionSession.isEmpty())
			return;

		for (;;) {
			HttpClientSession session;

			synchronized (exceptionSession) {
				session = (HttpClientSession) exceptionSession.pop();
			}

			if (session == null)
				break;

			Throwable cause = (Throwable) session.getAttachment();
			session.getFilterChain().fireExceptionCaught(session, cause);
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
	 * 工作线程，专门负责执行这5个步骤
	 * 
	 * @author zouziwen
	 *
	 *         2016年1月28日 上午10:55:20
	 */
	private class Worker implements Runnable {

		public Worker() {
		}

		@Override
		public void run() {
//			while (true) {

				try {
					doSessionCreated();

					doRequestSuccessed();

					doRequestFaild();

					doExceptionCaught();

					doSessionClosed();
				} catch (Exception e) {
					ExceptionMonitor.getInstance().exceptionCaught(e);
				}
			}
//		}
	}

	/**
	 * 开启工作线程
	 * 
	 * @param runnable
	 *            2016年1月20日 下午1:42:13
	 */
	void startupWorker() {
		// 设置当前session工作线程的名称
		worker = new Worker();
		executor.execute(new NamePreservingRunnable(worker, threadName));
	}

}
