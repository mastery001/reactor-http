package org.http.chain;

/**
 * Http处理链的构造
 * @author zouziwen
 *
 * 2016年1月18日 下午7:53:46
 */
public interface HttpFilterChainBuilder {
	
	 /**
     * An implementation which does nothing.
     */
	HttpFilterChainBuilder NOOP = new HttpFilterChainBuilder() {
        public void buildFilterChain(HttpFilterChain chain) throws Exception {
        }

        public String toString() {
            return "NOOP";
        }
    };

    /**
     * Modifies the specified <tt>chain</tt>.
     */
    void buildFilterChain(HttpFilterChain chain) throws Exception;
}
