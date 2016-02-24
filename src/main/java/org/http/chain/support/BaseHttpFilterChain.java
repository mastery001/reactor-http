package org.http.chain.support;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.http.HttpResponseMessage;
import org.http.chain.HttpFilter;
import org.http.chain.HttpFilter.NextHttpFilter;
import org.http.chain.HttpFilterAdapter;
import org.http.chain.HttpFilterChain;
import org.http.chain.HttpSession;
import org.http.chain.util.ExceptionMonitor;

/**
 * 基础http处理链实现
 * 
 * @author zouziwen
 *
 *         2016年1月19日 下午1:17:18
 */
public abstract class BaseHttpFilterChain implements HttpFilterChain {

	private final Map<String, Entry> name2entry = new HashMap<String, Entry>();

	private final EntryImpl head;

	private final EntryImpl tail;

	public BaseHttpFilterChain() {

		head = new EntryImpl(null, null, "head", new HeadHttpFilter());
		tail = new EntryImpl(head, null, "tail", new TailHttpFilter());
		head.nextEntry = tail;
	}

	@Override
	public Entry getEntry(String name) {
		Entry e = (Entry) name2entry.get(name);
		if (e == null) {
			return null;
		}
		return e;
	}

	@Override
	public HttpFilter get(String name) {
		Entry e = getEntry(name);
		if (e == null) {
			return null;
		}

		return e.getFilter();
	}

	@Override
	public NextHttpFilter getNextFilter(String name) {
		Entry e = getEntry(name);
		if (e == null) {
			return null;
		}
		return e.getNextFilter();
	}

	@Override
	public List<Entry> getAll() {
		List<Entry> list = new ArrayList<Entry>();
		EntryImpl e = head.nextEntry;
		while (e != tail) {
			list.add(e);
			e = e.nextEntry;
		}

		return list;
	}

	@Override
	public List<Entry> getAllReversed() {
		List<Entry> list = new ArrayList<Entry>();
		EntryImpl e = tail.prevEntry;
		while (e != head) {
			list.add(e);
			e = e.prevEntry;
		}
		return list;
	}

	@Override
	public boolean contains(String name) {
		return getEntry(name) != null;
	}

	@Override
	public boolean contains(HttpFilter httpFilter) {
		EntryImpl e = head.nextEntry;
		while (e != tail) {
			if (e.getFilter() == httpFilter) {
				return true;
			}
			e = e.nextEntry;
		}
		return false;
	}

	@Override
	public boolean contains(Class<?> httpFilterType) {
		EntryImpl e = head.nextEntry;
		while (e != tail) {
			if (httpFilterType.isAssignableFrom(e.getFilter().getClass())) {
				return true;
			}
			e = e.nextEntry;
		}
		return false;
	}

	@Override
	public void addFirst(String name, HttpFilter httpFilter) {
		checkAddable(name);
		register(head, name, httpFilter);
	}

	@Override
	public void addLast(String name, HttpFilter httpFilter) {
		checkAddable(name);
		register(tail.prevEntry, name, httpFilter);
	}

	@Override
	public void addBefore(String baseName, String name, HttpFilter httpFilter) {
		EntryImpl baseEntry = checkOldName(baseName);
		checkAddable(name);
		register(baseEntry.prevEntry, name, httpFilter);
	}

	@Override
	public void addAfter(String baseName, String name, HttpFilter httpFilter) {
		EntryImpl baseEntry = checkOldName(baseName);
		checkAddable(name);
		register(baseEntry, name, httpFilter);
	}

	@Override
	public HttpFilter remove(String name) {
		EntryImpl entry = checkOldName(name);
		deregister(entry);
		return entry.getFilter();
	}

	@Override
	public void clear() throws Exception {
		Iterator<String> it = new ArrayList<String>(name2entry.keySet()).iterator();
		while (it.hasNext()) {
			this.remove(it.next());
		}
	}

	private void register(EntryImpl prevEntry, String name, HttpFilter filter) {
		EntryImpl newEntry = new EntryImpl(prevEntry, prevEntry.nextEntry, name, filter);

		prevEntry.nextEntry.prevEntry = newEntry;
		prevEntry.nextEntry = newEntry;
		name2entry.put(name, newEntry);

	}

	private void deregister(EntryImpl entry) {
		deregister0(entry);
	}

	private void deregister0(EntryImpl entry) {
		EntryImpl prevEntry = entry.prevEntry;
		EntryImpl nextEntry = entry.nextEntry;
		prevEntry.nextEntry = nextEntry;
		nextEntry.prevEntry = prevEntry;

		name2entry.remove(entry.name);
	}

	/**
	 * Throws an exception when the specified filter name is not registered in
	 * this chain.
	 *
	 * @return An filter entry with the specified name.
	 */
	private EntryImpl checkOldName(String baseName) {
		EntryImpl e = (EntryImpl) name2entry.get(baseName);
		if (e == null) {
			throw new IllegalArgumentException("Unknown filter name:" + baseName);
		}
		return e;
	}

	/**
	 * Checks the specified filter name is already taken and throws an exception
	 * if already taken.
	 */
	private void checkAddable(String name) {
		if (name2entry.containsKey(name)) {
			throw new IllegalArgumentException("Other filter is using the same name '" + name + "'");
		}
	}

	@Override
	public void fireSessionCreated(HttpSession session) {
		Entry head = this.head;
		callNextSessionCreated(head, session);
	}

	public void callNextSessionCreated(Entry entry, HttpSession session) {
		try {
			entry.getFilter().sessionCreated(entry.getNextFilter(), session);
		} catch (Throwable e) {
			fireExceptionCaught(session, e);
		}
	}

	@Override
	public void fireSessionClosed(HttpSession session) {

		// And start the chain.
		Entry head = this.head;
		callNextSessionClosed(head, session);
	}

	private void callNextSessionClosed(Entry entry, HttpSession session) {
		try {
			entry.getFilter().sessionClosed(entry.getNextFilter(), session);
		} catch (Throwable e) {
			fireExceptionCaught(session, e);
		}
	}

	@Override
	public void fireRequestFailed(HttpSession session, HttpResponseMessage responseMessage) {
		Entry head = this.head;
		callNextRequestFailed(head, session, responseMessage);
	}

	private void callNextRequestFailed(Entry entry, HttpSession session, HttpResponseMessage responseMessage) {
		try {
			entry.getFilter().requestFailed(entry.getNextFilter(), session, responseMessage);

		} catch (Throwable e) {
			fireExceptionCaught(session, e);
		}
	}

	@Override
	public void fireRequestSuccessed(HttpSession session, HttpResponseMessage responseMessage) {
		Entry head = this.head;
		callNextRequestSuccessed(head, session, responseMessage);
	}

	private void callNextRequestSuccessed(Entry entry, HttpSession session, HttpResponseMessage responseMessage) {
		try {
			entry.getFilter().requestSuccessed(entry.getNextFilter(), session, responseMessage);
		} catch (Throwable e) {
			fireExceptionCaught(session, e);
		}
	}

	@Override
	public void fireExceptionCaught(HttpSession session, Throwable cause) {
		Entry head = this.head;
		callNextExceptionCaught(head, session, cause);
	}

	private void callNextExceptionCaught(Entry entry, HttpSession session, Throwable cause) {
		try {
			entry.getFilter().exceptionCaught(entry.getNextFilter(), session, cause);
		} catch (Throwable e) {
			ExceptionMonitor.getInstance().exceptionCaught(e);
		}
	}

	@Override
	public void fireFilterClose(HttpSession session) {
		Entry tail = this.tail;
		callPreviousFilterClose(tail, session);
	}

	private void callPreviousFilterClose(Entry entry, HttpSession session) {
		try {
			entry.getFilter().filterClose(entry.getNextFilter(), session);
		} catch (Throwable e) {
			fireExceptionCaught(session, e);
		}
	}

	protected abstract void doClose(HttpSession session);

	private class HeadHttpFilter extends HttpFilterAdapter {
		public void sessionCreated(NextHttpFilter nextHttpFilter, HttpSession session) {
			nextHttpFilter.sessionCreated(session);
		}

		public void sessionClosed(NextHttpFilter nextHttpFilter, HttpSession session) {
			nextHttpFilter.sessionClosed(session);
		}

		public void exceptionCaught(NextHttpFilter nextHttpFilter, HttpSession session, Throwable cause) {
			nextHttpFilter.exceptionCaught(session, cause);
		}

		@Override
		public void requestFailed(NextHttpFilter nextFilter, HttpSession session, HttpResponseMessage responseMessage)
				throws Exception {
			nextFilter.requestFailed(session, responseMessage);
		}

		@Override
		public void requestSuccessed(NextHttpFilter nextFilter, HttpSession session,
				HttpResponseMessage responseMessage) throws Exception {
			nextFilter.requestSuccessed(session, responseMessage);
		}

		@Override
		public void filterClose(NextHttpFilter nextFilter, HttpSession session) throws Exception {
			doClose(session);
		}

	}

	private static class TailHttpFilter extends HttpFilterAdapter {
		public void sessionCreated(NextHttpFilter nextHttpFilter, HttpSession session) throws Exception {
			if (haveHandler(session)) {
				session.getHandler().sessionCreated(session);
			}

		}

		public void sessionClosed(NextHttpFilter nextHttpFilter, HttpSession session) throws Exception {
			if (haveHandler(session)) {
				session.getHandler().sessionClosed(session);
			}

		}

		public void exceptionCaught(NextHttpFilter nextHttpFilter, HttpSession session, Throwable cause)
				throws Exception {
			if (haveHandler(session)) {
				session.getHandler().exceptionCaught(session, cause);
			}

		}

		@Override
		public void requestFailed(NextHttpFilter nextFilter, HttpSession session, HttpResponseMessage responseMessage)
				throws Exception {
			if (haveHandler(session)) {
				session.getHandler().requestFailed(session, responseMessage);
			}

		}

		@Override
		public void requestSuccessed(NextHttpFilter nextFilter, HttpSession session,
				HttpResponseMessage responseMessage) throws Exception {
			if (haveHandler(session)) {
				session.getHandler().requestSuccessed(session, responseMessage);
			}

		}

		/**
		 * 当不为空返回true
		 * @param session
		 * @return
		 * 2016年2月24日 下午4:49:39
		 */
		private boolean haveHandler(HttpSession session) {
			return session.getHandler() != null;
		}

	}

	private class EntryImpl implements Entry {
		private EntryImpl prevEntry;

		private EntryImpl nextEntry;

		private final String name;

		private final HttpFilter httpFilter;

		private final NextHttpFilter nextHttpFilter;

		private EntryImpl(EntryImpl prevEntry, EntryImpl nextEntry, String name, HttpFilter httpFilter) {
			if (httpFilter == null) {
				throw new NullPointerException("HttpFilter");
			}
			if (name == null) {
				throw new NullPointerException("name");
			}

			this.prevEntry = prevEntry;
			this.nextEntry = nextEntry;
			this.name = name;
			this.httpFilter = httpFilter;
			this.nextHttpFilter = new NextHttpFilter() {
				public void sessionCreated(HttpSession session) {
					Entry nextEntry = EntryImpl.this.nextEntry;
					callNextSessionCreated(nextEntry, session);
				}

				public void sessionClosed(HttpSession session) {
					Entry nextEntry = EntryImpl.this.nextEntry;
					callNextSessionClosed(nextEntry, session);
				}

				public void exceptionCaught(HttpSession session, Throwable cause) {
					Entry nextEntry = EntryImpl.this.nextEntry;
					callNextExceptionCaught(nextEntry, session, cause);
				}

				@Override
				public void requestFailed(HttpSession session, HttpResponseMessage responseMessage) {
					Entry nextEntry = EntryImpl.this.nextEntry;
					callNextRequestFailed(nextEntry, session, responseMessage);
				}

				@Override
				public void requestSuccessed(HttpSession session, HttpResponseMessage responseMessage) {
					Entry nextEntry = EntryImpl.this.nextEntry;
					callNextRequestSuccessed(nextEntry, session, responseMessage);
				}

				@Override
				public void filterClose(HttpSession session) throws Exception {
					Entry nextEntry = EntryImpl.this.prevEntry;
					callPreviousFilterClose(nextEntry, session);
				}

			};
		}

		public String getName() {
			return name;
		}

		public HttpFilter getFilter() {
			return httpFilter;
		}

		public NextHttpFilter getNextFilter() {
			return nextHttpFilter;
		}

		public String toString() {
			return "(" + getName() + ':' + httpFilter + ')';
		}
	}

}
