package org.http.chain;

import java.util.List;

import org.http.HttpResponseMessage;
import org.http.chain.HttpFilter.NextHttpFilter;

/**
 * http请求的处理链
 * 
 * @author zouziwen
 *
 *         2016年1月18日 下午4:25:29
 */
public interface HttpFilterChain {

	/**
	 * Returns the {@link Entry} with the specified <tt>name</tt> in this chain.
	 * 
	 * @return <tt>null</tt> if there's no such name in this chain
	 */
	Entry getEntry(String name);

	/**
	 * Returns the {@link HttpFilter} with the specified <tt>name</tt> in this
	 * chain.
	 * 
	 * @return <tt>null</tt> if there's no such name in this chain
	 */
	HttpFilter get(String name);

	/**
	 * Returns the {@link NextHttpFilter} of the {@link HttpFilter} with the
	 * specified <tt>name</tt> in this chain.
	 * 
	 * @return <tt>null</tt> if there's no such name in this chain
	 */
	NextHttpFilter getNextFilter(String name);

	/**
	 * Returns the list of all {@link Entry}s this chain contains.
	 */
	List<Entry> getAll();

	/**
	 * Returns the reversed list of all {@link Entry}s this chain contains.
	 */
	List<Entry> getAllReversed();

	/**
	 * Returns <tt>true</tt> if this chain contains an {@link HttpFilter} with
	 * the specified <tt>name</tt>.
	 */
	boolean contains(String name);

	/**
	 * Returns <tt>true</tt> if this chain contains the specified
	 * <tt>filter</tt>.
	 */
	boolean contains(HttpFilter filter);

	/**
	 * Returns <tt>true</tt> if this chain contains an {@link HttpFilter} of the
	 * specified <tt>filterType</tt>.
	 */
	boolean contains(Class<?> filterType);

	/**
	 * Adds the specified filter with the specified name at the beginning of
	 * this chain.
	 * 
	 * @throws HttpFilterLifeCycleException
	 *             if
	 *             {@link HttpFilter#onPostAdd(HttpFilterChain, String, NextFilter)}
	 *             or {@link HttpFilter#init()} throws an exception.
	 */
	void addFirst(String name, HttpFilter filter);

	/**
	 * Adds the specified filter with the specified name at the end of this
	 * chain.
	 * 
	 * @throws HttpFilterLifeCycleException
	 *             if
	 *             {@link HttpFilter#onPostAdd(HttpFilterChain, String, NextFilter)}
	 *             or {@link HttpFilter#init()} throws an exception.
	 */
	void addLast(String name, HttpFilter filter);

	/**
	 * Adds the specified filter with the specified name just before the filter
	 * whose name is <code>baseName</code> in this chain.
	 * 
	 * @throws HttpFilterLifeCycleException
	 *             if
	 *             {@link HttpFilter#onPostAdd(HttpFilterChain, String, NextFilter)}
	 *             or {@link HttpFilter#init()} throws an exception.
	 */
	void addBefore(String baseName, String name, HttpFilter filter);

	/**
	 * Adds the specified filter with the specified name just after the filter
	 * whose name is <code>baseName</code> in this chain.
	 * 
	 * @throws HttpFilterLifeCycleException
	 *             if
	 *             {@link HttpFilter#onPostAdd(HttpFilterChain, String, NextFilter)}
	 *             or {@link HttpFilter#init()} throws an exception.
	 */
	void addAfter(String baseName, String name, HttpFilter filter);

	/**
	 * Removes the filter with the specified name from this chain.
	 * 
	 * @throws HttpFilterLifeCycleException
	 *             if
	 *             {@link HttpFilter#onPostRemove(HttpFilterChain, String, NextFilter)}
	 *             or {@link HttpFilter#destroy()} throws an exception.
	 */
	HttpFilter remove(String name);

	/**
	 * Removes all filters added to this chain.
	 * 
	 * @throws Exception
	 *             if
	 *             {@link HttpFilter#onPostRemove(HttpFilterChain, String, NextFilter)}
	 *             thrown an exception.
	 */
	void clear() throws Exception;

	/**
	 * http连接开始创建
	 * 
	 * @param session
	 *            2016年1月18日 下午4:33:56
	 */
	void fireSessionCreated(HttpSession session);

	/**
	 * http连接关闭
	 * 
	 * @param session
	 *            2016年1月18日 下午4:35:52
	 */
	void fireSessionClosed(HttpSession session);

	/**
	 * 当http请求调用失败时调用
	 * 
	 * @param session
	 *            2016年1月18日 下午4:41:40
	 */
	void fireRequestFailed(HttpSession session, HttpResponseMessage responseMessage);

	/**
	 * 当http请求调用成功时调用
	 * 
	 * @param session
	 *            2016年1月18日 下午4:41:40
	 */
	void fireRequestSuccessed(HttpSession session, HttpResponseMessage responseMessage);

	/**
	 * 当出现异常时调用
	 * 
	 * @param session
	 * @param cause
	 *            2016年1月18日 下午4:39:50
	 */
	void fireExceptionCaught(HttpSession session, Throwable cause);

	/**
	 * 当主动调用HttpSession的close时调用
	 * 
	 * @param session
	 *            2016年1月20日 上午10:40:23
	 */
	void fireFilterClose(HttpSession session);

	public interface Entry {

		String getName();

		HttpFilter getFilter();

		NextHttpFilter getNextFilter();
	}
}
