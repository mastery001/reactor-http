package org.http.chain.util;

import java.util.concurrent.Callable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NamePreservingCallable<V> implements Callable<V> {
	private final Logger logger = LoggerFactory.getLogger(NamePreservingCallable.class);

	private final String newName;
	private final Callable<V> callable;

	public NamePreservingCallable(Callable<V> callable, String newName) {
	        this.callable = callable;
	        this.newName = newName;
	    }

	@Override
	public V call() throws Exception {
		 Thread currentThread = Thread.currentThread();
	        if(!currentThread.isAlive()) {
	        	currentThread.setDaemon(true);
	        }
	        String oldName = currentThread.getName();
	        
	        if (newName != null) {
	            setName(currentThread, newName);
	        }

	        try {
	        	return callable.call();
	        } finally {
	            setName(currentThread, oldName);
	        }
	}

	 private void setName(Thread thread, String name) {
	        try {
	            thread.setName(name);
	        } catch (Exception e) {
	            // Probably SecurityException.
	            if (logger.isWarnEnabled()) {
	                logger.warn("Failed to set the thread name.", e);
	            }
	        }
	    }
}
