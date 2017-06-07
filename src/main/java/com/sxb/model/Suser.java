package com.sxb.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import com.sxb.constant.AccountType;

/**
 * @description 随心播用户对象 
 * @author <a href="mailto:ou8zz@sina.com">OLE</a>
 * @date 2016/06/14
 * @version 1.0
 */
public class Suser implements Serializable {
	private static final long serialVersionUID = -813462896593233235L;
	
	private String userphone;					// 手机号
	private String username;					// 用户名
	private String password;					// 用户密码
	private String headimagepath;				// 头像路径
	private Integer praisenum = 0;				// 
	private String personal_signature;  			
	private Integer getattentions = 0; 		
	private Integer payattentions = 0; 					
	private String sex; 					
	private String address; 					
	private String constellation; 					
	private Integer online;
	private AccountType atype;					// 账号类型
	private String phoneno;						// 手机号码
	private String unionno;						// 联合账号
	private String remark;						// 备注
	
	// 登录日志VO
	private String deviceinfo;					// 设备信息
	private String ipaddress;					// 登录IP
	private String logininfo;					// 登录成功或异常信息
	
	// 给1.7PHP提供的VO字段，返回Json用
	private String signature;
	
	private static final Suser instance = new Suser();
	
	public static Suser getInstance() {
		return instance;
	}
	
	public Suser() {
			
	}
	
	public Suser(String userphone) {
		this.userphone = userphone;
	}

	public Suser(String userphone, String password) {
		this.userphone = userphone;
		this.password = password;
	}
	
	public String getUserphone() {
		return userphone;
	}
	public void setUserphone(String userphone) {
		this.userphone = userphone;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getHeadimagepath() {
		return headimagepath;
	}
	public void setHeadimagepath(String headimagepath) {
		this.headimagepath = headimagepath;
	}
	public Integer getPraisenum() {
		return praisenum;
	}
	public void setPraisenum(Integer praisenum) {
		this.praisenum = praisenum;
	}
	public String getPersonal_signature() {
		return personal_signature;
	}
	public void setPersonal_signature(String personal_signature) {
		this.personal_signature = personal_signature;
	}
	public Integer getGetattentions() {
		return getattentions;
	}
	public void setGetattentions(Integer getattentions) {
		this.getattentions = getattentions;
	}
	public Integer getPayattentions() {
		return payattentions;
	}
	public void setPayattentions(Integer payattentions) {
		this.payattentions = payattentions;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getConstellation() {
		return constellation;
	}
	public void setConstellation(String constellation) {
		this.constellation = constellation;
	}
	public Integer getOnline() {
		return online;
	}
	public void setOnline(Integer online) {
		this.online = online;
	}
	public AccountType getAtype() {
		return atype;
	}
	public void setAtype(AccountType atype) {
		this.atype = atype;
	}
	public String getPhoneno() {
		return phoneno;
	}
	public void setPhoneno(String phoneno) {
		this.phoneno = phoneno;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getUnionno() {
		return unionno;
	}
	public void setUnionno(String unionno) {
		this.unionno = unionno;
	}
	public String getSignature() {
		return signature;
	}
	public void setSignature(String signature) {
		this.signature = signature;
	}

	public String getDeviceinfo() {
		return deviceinfo;
	}

	public void setDeviceinfo(String deviceinfo) {
		this.deviceinfo = deviceinfo;
	}

	public String getIpaddress() {
		return ipaddress;
	}

	public void setIpaddress(String ipaddress) {
		this.ipaddress = ipaddress;
	}

	public String getLogininfo() {
		return logininfo;
	}

	public void setLogininfo(String logininfo) {
		this.logininfo = logininfo;
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(getUserphone()).append(getUsername()).toHashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj instanceof Suser != true)
			return false;
		Suser other = (Suser) obj;
		return new EqualsBuilder().append(getUserphone(), other.getUserphone()).append(getUsername(), other.getUsername()).isEquals();
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Suser [userphone=").append(userphone).append(", username=").append(username)
				.append(", password=").append(password).append(", headimagepath=").append(headimagepath)
				.append(", praisenum=").append(praisenum).append(", personal_signature=").append(personal_signature)
				.append(", getattentions=").append(getattentions).append(", payattentions=").append(payattentions)
				.append(", sex=").append(sex).append(", address=").append(address).append(", constellation=")
				.append(constellation).append(", online=").append(online).append("]");
		return builder.toString();
	}
}