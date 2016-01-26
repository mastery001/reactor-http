package org.http.chain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.concurrent.CopyOnWriteArrayList;

import org.http.chain.HttpFilter.NextHttpFilter;
import org.http.chain.HttpFilterChain.Entry;

@SuppressWarnings({"rawtypes","unchecked"})
public class DefaultHttpFilterChainBuilder implements HttpFilterChainBuilder {

	private final List entries;

	/**
	 * Creates a new instance with an empty filter list.
	 */
	public DefaultHttpFilterChainBuilder() {
		entries = new CopyOnWriteArrayList();
	}
	
	/**
	 * @see HttpFilterChain#getEntry(String)
	 */
	public Entry getEntry(String name) {
		for (Iterator i = entries.iterator(); i.hasNext();) {
			Entry e = (Entry) i.next();
			if (e.getName().equals(name)) {
				return e;
			}
		}

		return null;
	}

	/**
	 * @see HttpFilterChain#get(String)
	 */
	public HttpFilter get(String name) {
		Entry e = getEntry(name);
		if (e == null) {
			return null;
		}

		return e.getFilter();
	}

	/**
	 * @see HttpFilterChain#getAll()
	 */
	public List getAll() {
		return new ArrayList(entries);
	}

	/**
	 * @see HttpFilterChain#getAllReversed()
	 */
	public List getAllReversed() {
		List result = getAll();
		Collections.reverse(result);
		return result;
	}

	/**
	 * @see HttpFilterChain#contains(String)
	 */
	public boolean contains(String name) {
		return getEntry(name) != null;
	}

	/**
	 * @see HttpFilterChain#contains(HttpFilter)
	 */
	public boolean contains(HttpFilter filter) {
		for (Iterator i = entries.iterator(); i.hasNext();) {
			Entry e = (Entry) i.next();
			if (e.getFilter() == filter) {
				return true;
			}
		}

		return false;
	}

	/**
	 * @see HttpFilterChain#contains(Class)
	 */
	public boolean contains(Class filterType) {
		for (Iterator i = entries.iterator(); i.hasNext();) {
			Entry e = (Entry) i.next();
			if (filterType.isAssignableFrom(e.getFilter().getClass())) {
				return true;
			}
		}

		return false;
	}

	/**
	 * @see HttpFilterChain#addFirst(String, HttpFilter)
	 */
	public synchronized void addFirst(String name, HttpFilter filter) {
		register(0, new EntryImpl(name, filter));
	}

	/**
	 * @see HttpFilterChain#addLast(String, HttpFilter)
	 */
	public synchronized void addLast(String name, HttpFilter filter) {
		register(entries.size(), new EntryImpl(name, filter));
	}

	/**
	 * @see HttpFilterChain#addBefore(String, String, HttpFilter)
	 */
	public synchronized void addBefore(String baseName, String name, HttpFilter filter) {
		checkBaseName(baseName);

		for (ListIterator i = entries.listIterator(); i.hasNext();) {
			Entry base = (Entry) i.next();
			if (base.getName().equals(baseName)) {
				register(i.previousIndex(), new EntryImpl(name, filter));
				break;
			}
		}
	}

	/**
	 * @see HttpFilterChain#addAfter(String, String, HttpFilter)
	 */
	public synchronized void addAfter(String baseName, String name, HttpFilter filter) {
		checkBaseName(baseName);

		for (ListIterator i = entries.listIterator(); i.hasNext();) {
			Entry base = (Entry) i.next();
			if (base.getName().equals(baseName)) {
				register(i.nextIndex(), new EntryImpl(name, filter));
				break;
			}
		}
	}

	/**
	 * @see HttpFilterChain#remove(String)
	 */
	public synchronized HttpFilter remove(String name) {
		if (name == null) {
			throw new NullPointerException("name");
		}

		for (ListIterator i = entries.listIterator(); i.hasNext();) {
			Entry e = (Entry) i.next();
			if (e.getName().equals(name)) {
				entries.remove(i.previousIndex());
				return e.getFilter();
			}
		}

		throw new IllegalArgumentException("Unknown filter name: " + name);
	}

	/**
	 * @see HttpFilterChain#clear()
	 */
	public synchronized void clear() throws Exception {
		entries.clear();
	}

	public void buildFilterChain(HttpFilterChain chain) throws Exception {
		for (Iterator i = entries.iterator(); i.hasNext();) {
			Entry e = (Entry) i.next();
			chain.addLast(e.getName(), e.getFilter());
		}
	}

	public String toString() {
		StringBuffer buf = new StringBuffer();
		buf.append("{ ");

		boolean empty = true;

		for (Iterator i = entries.iterator(); i.hasNext();) {
			Entry e = (Entry) i.next();
			if (!empty) {
				buf.append(", ");
			} else {
				empty = false;
			}

			buf.append('(');
			buf.append(e.getName());
			buf.append(':');
			buf.append(e.getFilter());
			buf.append(')');
		}

		if (empty) {
			buf.append("empty");
		}

		buf.append(" }");

		return buf.toString();
	}

	public Object clone() {
		DefaultHttpFilterChainBuilder ret = new DefaultHttpFilterChainBuilder();
		for (Iterator i = entries.iterator(); i.hasNext();) {
			Entry e = (Entry) i.next();
			ret.addLast(e.getName(), e.getFilter());
		}
		return ret;
	}

	private void checkBaseName(String baseName) {
		if (baseName == null) {
			throw new NullPointerException("baseName");
		}

		if (!contains(baseName)) {
			throw new IllegalArgumentException("Unknown filter name: " + baseName);
		}
	}

	private void register(int index, Entry e) {
		if (contains(e.getName())) {
			throw new IllegalArgumentException("Other filter is using the same name: " + e.getName());
		}
		entries.add(index, e);
	}

	private static class EntryImpl implements Entry {
		private final String name;

		private final HttpFilter filter;

		private EntryImpl(String name, HttpFilter filter) {
			if (name == null) {
				throw new NullPointerException("name");
			}
			if (filter == null) {
				throw new NullPointerException("filter");
			}

			this.name = name;
			this.filter = filter;
		}

		public String getName() {
			return name;
		}

		public HttpFilter getFilter() {
			return filter;
		}

		public NextHttpFilter getNextFilter() {
			throw new IllegalStateException();
		}

		public String toString() {
			return "(" + getName() + ':' + filter + ')';
		}
	}
}
