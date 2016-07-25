package httpx.conn;

import java.util.concurrent.TimeUnit;

import org.apache.http.conn.HttpClientConnectionManager;

/**
 * 连接回收策略：
 * 经典阻塞I/O模型的一个主要缺点就是只有当组侧I/O时，socket才能对I/O事件做出反应。当连接被管理器收回后，这个连接仍然存活，
 * 但是却无法监控socket的状态，也无法对I/O事件做出反馈。如果连接被服务器端关闭了，客户端监测不到连接的状态变化（也就无法根据连接状态的变化，
 * 关闭本地的socket）。
 * 
 * HttpClient为了缓解这一问题造成的影响，会在使用某个连接前，监测这个连接是否已经过时，如果服务器端关闭了连接，那么连接就会失效。
 * 这种过时检查并不是100%有效，并且会给每个请求增加10到30毫秒额外开销。唯一一个可行的，且does not involve a one thread
 * per socket model for idle connections的解决办法，是建立一个监控线程，来专门回收由于长时间不活动而被判定为失效的连接。
 * 这个监控线程可以周期性的调用ClientConnectionManager类的closeExpiredConnections()方法来关闭过期的连接，
 * 回收连接池中被关闭的连接。它也可以选择性的调用ClientConnectionManager类的closeIdleConnections()
 * 方法来关闭一段时间内不活动的连接。
 * 
 * @author zouziwen
 *
 *         2016年6月2日 下午5:13:53
 */
public class IdleConnectionMonitorThread extends Thread {

	private final HttpClientConnectionManager connMgr;
	private volatile boolean shutdown;

	public IdleConnectionMonitorThread(HttpClientConnectionManager connMgr) {
		super("IdleConnectionMonitorThread");
		this.connMgr = connMgr;
	}

	@Override
	public void run() {
		try {
			while (!shutdown) {
				synchronized (this) {
					wait(5000);
					// Close expired connections
					connMgr.closeExpiredConnections();
					// Optionally, close connections
					// that have been idle longer than 30 sec
					connMgr.closeIdleConnections(30, TimeUnit.SECONDS);
				}
			}
		} catch (InterruptedException ex) {
			// terminate
		}
	}

	public void shutdown() {
		shutdown = true;
		synchronized (this) {
			notifyAll();
		}
	}

}