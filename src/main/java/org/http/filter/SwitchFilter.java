package org.http.filter;

import org.cache.DistributedCache;
import org.cache.redis.RedisDistributedCache;
import org.http.HttpResponseMessage;
import org.http.chain.HttpFilterAdapter;
import org.http.chain.HttpSession;
import org.http.exception.HttpInvokeException;

/**
 * 服务开关升降级
 * 
 * @author zouziwen
 *
 *         2016年2月24日 下午5:17:42
 */
public abstract class SwitchFilter extends HttpFilterAdapter {

	private static final DistributedCache<String, Integer> cache = new RedisDistributedCache<String, Integer>();

	@Override
	public void sessionCreated(NextHttpFilter nextFilter, HttpSession session) throws Exception {
		String key = session.getName();
		Integer errorCount = cache.get(key);
		if (errorCount > getMaxErrorSize()) {
			session.close();
			// 如果被拦截则请求断链
		}else {
			nextFilter.sessionCreated(session);
		}
	}

	@Override
	public void exceptionCaught(NextHttpFilter nextFilter, HttpSession session, Throwable cause) throws Exception {
		String url = session.getName();
		// 如果是HttpInvokeException则认为是接口异常
		if (cause instanceof HttpInvokeException && isAddToCache(url , (HttpInvokeException)cause)) {
			addToCache(url);
		}
		nextFilter.exceptionCaught(session, cause);
	}

	@Override
	public void requestFailed(NextHttpFilter nextFilter, HttpSession session, HttpResponseMessage responseMessage)
			throws Exception {
		if(isAddToCache(session.getName(), responseMessage)) {
			addToCache(session.getName());
		}
		nextFilter.requestFailed(session, responseMessage);
	}
	
	/**
	 * 将本地缓存更新
	 * @param url
	 * 2016年2月29日 下午6:04:28
	 */
	protected void addToCache(String url) {
		Integer count = cache.get(url);
		count = (count == null ? 0 : count);
		cache.set(url, count);
	}

	/**
	 * 获取调用接口最大错误次数
	 * 
	 * @return 2016年2月29日 下午4:09:37
	 */
	protected abstract int getMaxErrorSize();

	/**
	 * 是否允许添加至缓存的策略（当产生http异常时）
	 * @param url	请求的接口地址
	 * @param cause	HttpInvokeException异常
	 * 2016年2月29日 下午4:34:57
	 */
	protected abstract boolean isAddToCache(String url, HttpInvokeException cause);
	
	/**
	 * 是否允许添加至缓存的策略(当请求失败)
	 * @param url	请求的接口地址
	 * @param responseMessage	请求失败返回的信息
	 * 2016年2月29日 下午4:36:41
	 */
	protected abstract boolean isAddToCache(String url, HttpResponseMessage responseMessage);
}
