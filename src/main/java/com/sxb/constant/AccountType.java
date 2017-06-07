package com.sxb.constant;

/**
 * @description 随心播用户信息账号类型枚举
 * @author <a href="mailto:ou8zz@sina.com">OLE</a>
 * @date 2016/07/14
 * @version 1.0
 */
public enum AccountType {
	PHONE("手机"),QQ("腾讯QQ"),WX("微信");
	
	private String des;

	AccountType(String des) {
		this.des = des;
	}

	public String getDes() {
		return des;
	}
}
