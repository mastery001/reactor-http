package httpx.conn;

import org.apache.http.HeaderElement;
import org.apache.http.HeaderElementIterator;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.conn.ConnectionKeepAliveStrategy;
import org.apache.http.message.BasicHeaderElementIterator;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;

public class DelayConnectionKeepAliveStrategy implements ConnectionKeepAliveStrategy {

	public DelayConnectionKeepAliveStrategy() {

	}

	@Override
	public long getKeepAliveDuration(HttpResponse response, HttpContext context) {
		// Honor 'keep-alive' header
		HeaderElementIterator it = new BasicHeaderElementIterator(response.headerIterator(HTTP.CONN_KEEP_ALIVE));
		while (it.hasNext()) {
			HeaderElement he = it.nextElement();
			String param = he.getName();
			String value = he.getValue();
			if (value != null && param.equalsIgnoreCase("timeout")) {
				try {
					return Long.parseLong(value) * 1000;
				} catch (NumberFormatException ignore) {
				}
			}
		}
		HttpHost target = (HttpHost) context.getAttribute(HttpClientContext.HTTP_TARGET_HOST);
		if ("www.naughty-server.com".equalsIgnoreCase(target.getHostName())) {
			// Keep alive for 5 seconds only
			return 5 * 1000;
		} else {
			// otherwise keep alive for 30 seconds
			return 30 * 1000;
		}
	}

}
