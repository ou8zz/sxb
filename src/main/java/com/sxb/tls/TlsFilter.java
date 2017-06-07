package com.sxb.tls;

import java.io.IOException;
import java.util.Map;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.LogFactory;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;
import org.springframework.web.filter.GenericFilterBean;

import com.google.gson.reflect.TypeToken;
import com.sxb.constant.AccountType;
import com.sxb.model.RespsonData;
import com.sxb.util.JsonUtils;
import com.sxb.util.RegexUtil;
import com.sxb.util.TLSSigature;
import com.sxb.util.TLSSigature.CheckTLSSignatureResult;

/**
 * @description TLS校验签名Filter 拦截所有请求做sig校验
 * @author <a href="mailto:ou8zz@sina.com">OLE</a>
 * @date 2016/07/18
 * @version 1.0
 */
public class TlsFilter extends GenericFilterBean {

	/**
	 * 校验所有请求是否有效token
	 * 通过sig和userphone做校验
	 */
	@Override
	public void doFilter(final ServletRequest req, final ServletResponse res, final FilterChain chain) {
		final HttpServletRequest request = (HttpServletRequest) req;
		final StringBuffer purl = request.getRequestURL();
		final String authHeader = request.getHeader("Authorization");
		try {
			// 是否白名单
			if(isWhiteList(purl)) {
				chain.doFilter(req, res);
				return;
			}
			
			// 是否带有签名头部
			if (RegexUtil.isEmpty(authHeader)) {
				RespsonData rd = new RespsonData("TlsFilter");
				rd.setErrorCode(571);
				rd.setErrorInfo("Authorization is null");
				res.getWriter().write(JsonUtils.toJson(rd));
				return;
			}
			
			// 校验sig是否有效，如果无效直接json返回无效错误信息
			Map<String, String> m = JsonUtils.fromJson(authHeader, new TypeToken<Map<String, String>>() {}.getType());
			CheckTLSSignatureResult checkTLSSignatureEx = TLSSigature.CheckTLSSignatureEx(m.get("sig"), m.get("userphone"), AccountType.valueOf(m.get("atype")));
			if (checkTLSSignatureEx.verifyResult) {
				chain.doFilter(req, res);
			} else {
				RespsonData rd = new RespsonData("TlsFilter");
				rd.setErrorCode(570);
				rd.setErrorInfo("Invalid token or userphone. " + checkTLSSignatureEx.errMessage);
				res.getWriter().write(JsonUtils.toJson(rd));
			}
		} catch (final Exception e) {
			// 在验证过程中出错返回验证错误信息
			RespsonData rd = new RespsonData("TlsFilter");
			rd.setErrorCode(570);
			rd.setErrorInfo(e.getMessage());
			try {
				res.getWriter().write(JsonUtils.toJson(rd));
			} catch (IOException e1) {
			}
			LogFactory.getLog(TlsFilter.class).error("TlsFilter error ", e);
		}
	}
	
	/**
	 * 判断当前URL是否白名单
	 * 白名单在web.xml中filter中配置
	 * @param purl
	 * @return true or false
	 */
	private boolean isWhiteList(StringBuffer purl) {
		int li = purl.lastIndexOf("/");
		String url = purl.substring(li, purl.length());
		PathMatcher pm = new AntPathMatcher();
		FilterConfig filterConfig = getFilterConfig();
		String initParameter = filterConfig.getInitParameter("whiteList");
		for(String si : initParameter.split(",")) {
			String s = si.trim();
			boolean match = pm.match(url, s);
			if(match) {
				return true;
			}
			if(s.indexOf("*.") > 0) {
				int i = s.indexOf("*.");
				String ns = s.substring(++i, s.length());
				boolean b = url.indexOf(ns) > 0;
				if(b) return true;
			}
		}
		return false;
	}
}
