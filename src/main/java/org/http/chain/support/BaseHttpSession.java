package org.http.chain.support;

import java.util.HashMap;
import java.util.Map;

import org.http.chain.HttpSession;

public abstract class BaseHttpSession implements HttpSession {

	private final Map<String, Object> attributes = new HashMap<String, Object>(8);
	
	private final long createTime;

	private long lastAccessedTime;

	private volatile boolean close;

	public BaseHttpSession() {
		createTime = lastAccessedTime = System.currentTimeMillis();
	}

	@Override
	public long getCreateTime() {
		return createTime;
	}

	@Override
	public long getLastAccessedTime() {
		updateLastAccessedTime();
		return lastAccessedTime;
	}

	public void updateLastAccessedTime() {
		this.lastAccessedTime = System.currentTimeMillis();
	}

	@Override
	public long getSurvivalTime() {
		return getLastAccessedTime() - getCreateTime();
	}

	@Override
	public Object setAttribute(String key, Object value) {
		synchronized (attributes) {
			return attributes.put(key, value);
		}

	}

	@Override
	public Object getAttribute(String key) {
		synchronized (attributes) {
			return attributes.get(key);
		}

	}

	@Override
	public Object removeAttribute(String key) {
		synchronized (attributes) {
			return attributes.remove(key);
		}

	}

	@Override
	public boolean containsAttribute(String key) {
		return getAttribute(key) != null;

	}

	@Override
	public void close() {
		if (!isClose()) {
			close = true;
		}
		close0();
	}

	protected abstract void close0();

	@Override
	public boolean isClose() {
		return close;
	}

}
