package org.http.chain.util;

import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 获得本地ip的工具类
 * @author zouziwen
 *
 * 2015年10月21日 上午10:17:15
 */
public class IPUtil {

	private static final Logger logger = LoggerFactory.getLogger(IPUtil.class);

	public static String getLocalIP() {
		String IpAddress = "";
		String os = System.getProperty("os.name");
		if (os != null) {
			// if current is windows
			if (os.startsWith("Windows")) {
				try {
					IpAddress = InetAddress.getLocalHost().getHostAddress();
				} catch (UnknownHostException e) {
					logger.info("get windows localhost ip fail," + e.getMessage(), e);
				}
			} else {
				try {
					Enumeration<?> e1 = (Enumeration<?>) NetworkInterface.getNetworkInterfaces();
					while (e1.hasMoreElements()) {
						NetworkInterface ni = (NetworkInterface) e1.nextElement();
						if (!ni.getName().equals("eth0")) {
							continue;
						} else {
							Enumeration<?> e2 = ni.getInetAddresses();
							while (e2.hasMoreElements()) {
								InetAddress ia = (InetAddress) e2.nextElement();
								if (ia instanceof Inet6Address)
									continue;
								IpAddress = ia.getHostAddress();
							}
							break;
						}
					}
				} catch (SocketException e) {
					logger.info("get linux localhost ip fail," + e.getMessage(), e);
				}
			}
		}
		return IpAddress;
	}

	public static void main(String[] args) {
		System.out.println(getLocalIP());
	}
}