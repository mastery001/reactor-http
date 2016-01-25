package org.http.chain.support;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.http.chain.HttpSession;

public abstract class BaseHttpSession implements HttpSession{

	private final ConcurrentMap<String, Object> attributes = new ConcurrentHashMap<String, Object>(8);
	
	private final long createTime;
	
	private long lastAccessedTime;
	
	private boolean close;
	
	public BaseHttpSession() {
		createTime = lastAccessedTime= System.currentTimeMillis();
	}
	
	@Override
	public long getCreateTime() {
		return createTime;
	}

	@Override
	public long getLastAccessedTime() {
		return lastAccessedTime;
	}
	
	public void setLastAccessedTime(long lastAccessedTime) {
		this.lastAccessedTime = lastAccessedTime;
	}

	@Override
	public long getSurvivalTime() {
		return getLastAccessedTime() - getCreateTime();
	}

	@Override
	public Object setAttribute(String key, Object value) {
		return attributes.putIfAbsent(key, value);
	}

	@Override
	public Object getAttribute(String key) {
		return attributes.get(key);
	}

	@Override
	public Object removeAttribute(String key) {
		return attributes.remove(key);
	}

	@Override
	public boolean containsAttribute(String key) {
		return attributes.containsKey(key);
	}

	@Override
	public void close() {
		if(!isClose()) {
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
