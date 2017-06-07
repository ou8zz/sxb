package com.sxb.util;

import java.util.Locale;
import java.util.ResourceBundle;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @description 动态读取properties文件
 * @author <a href="mailto:ou8zz@sina.com">OLE</a>
 * @date 2016/06/22
 * @version 1.0
 */
public class ResourceUtil {
	private static Log log = LogFactory.getLog(ResourceUtil.class);
	private final static MyResourceBundleControl ctl = new MyResourceBundleControl();

	/**
	 * @param args
	 * @throws InterruptedException
	 */
	public static void main(String[] args) throws InterruptedException {
		System.out.println(getBundle("conf").getString("jssdk.appId"));
		Thread.sleep(5000);
		System.out.println(getBundle("conf").getString("jssdk.appId"));
	}

	private static ResourceBundle getBundle(String pro) {
		return ResourceBundle.getBundle(pro, Locale.getDefault(), ctl);
	}
	
	/**
	 * 读取conf.properties
	 * @param key
	 * @return value
	 */
	public static String getConf(String key) {
		String string = "";
		try {
			string = getBundle("conf").getString(key);
		} catch (Exception e) {
			log.trace("miss get key " + key);
		}
		return string;
	}
	
	/**
	 * 重载控制器
	 */
	private static class MyResourceBundleControl extends ResourceBundle.Control {

		/**
		 * 如果在加载配置文件中时隔一秒钟文件内容将重新读取
		 */
		@Override
		public long getTimeToLive(String baseName, Locale locale) {
			return 1000;
		}

		@Override
		public boolean needsReload(String baseName, Locale locale, String format, ClassLoader loader, ResourceBundle bundle, long loadTime) {
			return true;
		}
	}
}
