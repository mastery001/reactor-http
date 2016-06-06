package org.http.filter;

import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;

import org.apache.http.HttpResponse;
import org.cache.DistributedCache;
import org.cache.redis.RedisDistributedCache;
import org.http.HttpRequest;
import org.http.HttpResponseMessage;
import org.http.MethodType;
import org.http.chain.HttpFilterAdapter;
import org.http.chain.HttpSession;
import org.http.chain.util.Constant;
import org.http.chain.util.NamePreservingRunnable;
import org.http.client.method.HttpGetRequest;
import org.http.client.method.HttpPostRequest;
import org.http.exception.HttpInvokeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 服务开关升降级
 * 
 * @author zouziwen
 *
 *         2016年2月24日 下午5:17:42
 */
/**
 * @author zouziwen
 *
 *         2016年3月2日 下午2:02:45
 */
public abstract class SwitchFilter extends HttpFilterAdapter {

	private class FatalCount {

		int count;

		/**
		 * 是否已经启动后台进行去访问 2016年3月2日 下午3:06:19
		 */
		boolean runRequest;

		public FatalCount() {
			super();
		}

		void run(final HttpSession session) {
			if (!runRequest) {
				final String url = session.getHttpRequest().getURI().toString();
				executor.execute(new NamePreservingRunnable(new Runnable() {

					@Override
					public void run() {
						asyncFatalRequest(session);
					}

				}, Constant.LEFT_SQUARE + url + Constant.RIGHT_SQUARE + "-thread"));
				runRequest = true;
			}
		}
	}

	private static final DistributedCache<String, FatalCount> cache = new RedisDistributedCache<String, FatalCount>();

	private final Executor executor;

	private final Logger logger = LoggerFactory.getLogger(getClass());

	/**
	 * 执行异步自动请求接口是否
	 * 
	 * @param executor
	 *            2016年3月2日 下午2:02:56
	 */
	public SwitchFilter(Executor executor) {
		super();
		this.executor = executor;
	}

	/**
	 * 异步去请求出现错误的请求
	 * 
	 * @param session
	 *            2016年3月2日 下午3:13:56
	 */
	protected void asyncFatalRequest(HttpSession session) {
		String url = session.getName();
		MethodType type = MethodType.valueOf(session.getHttpRequest().getMethod());
		HttpRequest request = null;
		switch (type) {
		case GET:
			request = new HttpGetRequest(url);
			break;
		default:
			request = new HttpPostRequest(url);
		}
		long sleepTime = 1;
		while (true) {
			if (retryRequest(request, sleepTime, session)) {
				return;
			} else {
				if (sleepTime < 10) {
					sleepTime += ((sleepTime + 1) / 2);
				}

			}
		}
	}

	/**
	 * 请求错误的接口
	 * 
	 * @param session
	 * @param type
	 * @return 2016年3月2日 下午3:50:43
	 */
	private boolean retryRequest(HttpRequest request, long sleepTime, HttpSession session) {
		try {
			TimeUnit.SECONDS.sleep(sleepTime);
			HttpResponse response = session.getHttpClientFactory().getConnection().execute(request.concreteRequest());
			if (response.getStatusLine().getStatusCode() == 200) {
				cache.set(session.getName(), new FatalCount());
				return true;
			}
		} catch (InterruptedException e) {
			logger.info("retry request error ; exception is {}", e);
		} catch (Exception e) {
		}
		return false;
	}

	@Override
	public void sessionCreated(NextHttpFilter nextFilter, HttpSession session) throws Exception {
		String key = session.getName();
		FatalCount fatalCount = cache.get(key);
		if (fatalCount.count > getMaxErrorSize()) {
			session.close();
			fatalCount.run(session);
			// 如果被拦截则请求断链
		} else {
			nextFilter.sessionCreated(session);
		}
	}

	@Override
	public void exceptionCaught(NextHttpFilter nextFilter, HttpSession session, Throwable cause) throws Exception {
		String url = session.getName();
		// 如果是HttpInvokeException则认为是接口异常
		if (cause instanceof HttpInvokeException && isAddToCache(url, (HttpInvokeException) cause)) {
			addToCache(url);
		}
		nextFilter.exceptionCaught(session, cause);
	}

	@Override
	public void requestFailed(NextHttpFilter nextFilter, HttpSession session, HttpResponseMessage responseMessage)
			throws Exception {
		if (isAddToCache(session.getName(), responseMessage)) {
			addToCache(session.getName());
		}
		nextFilter.requestFailed(session, responseMessage);
	}

	/**
	 * 将本地缓存更新
	 * 
	 * @param url
	 *            2016年2月29日 下午6:04:28
	 */
	protected void addToCache(String url) {
		FatalCount fatalCount = cache.get(url);
		fatalCount = (fatalCount == null ? new FatalCount() : fatalCount);
		cache.set(url, fatalCount);
	}

	/**
	 * 获取调用接口最大错误次数
	 * 
	 * @return 2016年2月29日 下午4:09:37
	 */
	protected abstract int getMaxErrorSize();

	/**
	 * 是否允许添加至缓存的策略（当产生http异常时）
	 * 
	 * @param url
	 *            请求的接口地址
	 * @param cause
	 *            HttpInvokeException异常 2016年2月29日 下午4:34:57
	 */
	protected abstract boolean isAddToCache(String url, HttpInvokeException cause);

	/**
	 * 是否允许添加至缓存的策略(当请求失败)
	 * 
	 * @param url
	 *            请求的接口地址
	 * @param responseMessage
	 *            请求失败返回的信息 2016年2月29日 下午4:36:41
	 */
	protected abstract boolean isAddToCache(String url, HttpResponseMessage responseMessage);
}
