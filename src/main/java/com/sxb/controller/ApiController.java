package com.sxb.controller;

import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sxb.util.JsonUtils;
import com.sxb.util.Sign;

/**
 * @description 后台工具和一些特定API接口管理 RestController
 * @author <a href="mailto:ou8zz@sina.com">OLE</a>
 * @date 2016/06/22
 * @version 1.0
 */
@RestController
public class ApiController {
	private Log log = LogFactory.getLog(ApiController.class);
	
	/**
	 * JAVA获取微信签名方法 http://localhost/sxb//tools/reqjssdk?callback=callback&url=http://demo.com
	 * @param url	注意 URL 一定要动态获取，不能 hardcode
	 * 请求示例：GET请求后缀/tools/reqjssdk?callback=callback&url=http://demo.com
	 * @return 跨域请求callback
	 */
	@RequestMapping(method=RequestMethod.GET, value="/tools/reqjssdk")
	public Object getSign(@RequestParam("callback") String callback, @RequestParam("url") String url) {
		try {
			Map<String, String> sign = Sign.getSign(null, null, url);
			String json = JsonUtils.toJson(sign);
			json = callback + "(" + json + ")";	// 跨域请求callback拼接
			return json;
		} catch(Exception e) {
			log.error("getSign error", e);
		}
		return null;
	}
}
