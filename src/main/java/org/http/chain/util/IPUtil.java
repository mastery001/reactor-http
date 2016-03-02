package org.http.chain.util;

import java.net.InetAddress;
import java.net.NetworkInterface;
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
	/**
	 * 获取本机ip地址，并自动区分Windows还是linux操作系统
	 * 
	 * @return String
	 */
	public static String getLocalIP() {
		String sIP = "";
		InetAddress ip = null;
		String os = System.getProperty("os.name");
		try {
			if (os != null) {
				// 如果是Windows操作系统
				if (os.startsWith("Windows")) {
					try {
						sIP = InetAddress.getLocalHost().getHostAddress();
					} catch (UnknownHostException e) {
						logger.info("get windows localhost ip fail," + e.getMessage());
					}
				}
				// 如果是Linux操作系统
				else {
					boolean bFindIP = false;
					Enumeration<NetworkInterface> netInterfaces = (Enumeration<NetworkInterface>) NetworkInterface
							.getNetworkInterfaces();
					while (netInterfaces.hasMoreElements()) {
						if (bFindIP) {
							break;
						}
						NetworkInterface ni = (NetworkInterface) netInterfaces.nextElement();
						// ----------特定情况，可以考虑用ni.getName判断
						// 遍历所有ip
						Enumeration<InetAddress> ips = ni.getInetAddresses();
						while (ips.hasMoreElements()) {
							ip = (InetAddress) ips.nextElement();
							if (ip.isSiteLocalAddress() && !ip.isLoopbackAddress() // 127.开头的都是lookback地址
									&& ip.getHostAddress().indexOf(":") == -1) {
								bFindIP = true;
								break;
							}
						}

					}
				}
			}
		} catch (Exception e) {
			logger.info("get linux localhost ip fail," + e.getMessage(), e);
		}

		if (null != ip) {
			sIP = ip.getHostAddress();
		}
		logger.info("this service localhost is {}" , sIP);
		return sIP;
	}

	public static void main(String[] args) {
		System.out.println(getLocalIP());
	}
}