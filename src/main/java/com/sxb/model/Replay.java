package com.sxb.model;

import java.io.Serializable;
import java.util.Date;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 * @description 随心播视频保存 
 * @author <a href="mailto:ou8zz@sina.com">OLE</a>
 * @date 2016/06/14
 * @version 1.0
 */
public class Replay implements Serializable {
	private static final long serialVersionUID = 3027052564659821285L;
	
	private Integer programid;					// 房间号
	private String userphone;					// 用户手机
	private String subject;					
	private Integer praisenum = 0;				
	private String coverimagepath;  			
	private Integer state = 0; 		
	private String groupid; 					
	private Date begin_time; 					
	private String url; 	
	private String addr; 	
	private String duration;
	private String replayid;
	
	public Integer getProgramid() {
		return programid;
	}

	public void setProgramid(Integer programid) {
		this.programid = programid;
	}

	public String getUserphone() {
		return userphone;
	}

	public void setUserphone(String userphone) {
		this.userphone = userphone;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public Integer getPraisenum() {
		return praisenum;
	}

	public void setPraisenum(Integer praisenum) {
		this.praisenum = praisenum;
	}

	public String getCoverimagepath() {
		return coverimagepath;
	}

	public void setCoverimagepath(String coverimagepath) {
		this.coverimagepath = coverimagepath;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public String getGroupid() {
		return groupid;
	}

	public void setGroupid(String groupid) {
		this.groupid = groupid;
	}

	public Date getBegin_time() {
		return begin_time;
	}

	public void setBegin_time(Date begin_time) {
		this.begin_time = begin_time;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getAddr() {
		return addr;
	}

	public void setAddr(String addr) {
		this.addr = addr;
	}

	public String getDuration() {
		return duration;
	}

	public void setDuration(String duration) {
		this.duration = duration;
	}

	public String getReplayid() {
		return replayid;
	}

	public void setReplayid(String replayid) {
		this.replayid = replayid;
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(getUserphone()).append(getProgramid()).toHashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj instanceof Replay != true)
			return false;
		Replay other = (Replay) obj;
		return new EqualsBuilder().append(getUserphone(), other.getUserphone()).append(getProgramid(), other.getProgramid()).isEquals();
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Replay [programid=").append(programid).append(", userphone=").append(userphone)
				.append(", subject=").append(subject).append(", praisenum=").append(praisenum)
				.append(", coverimagepath=").append(coverimagepath).append(", state=").append(state)
				.append(", groupid=").append(groupid).append(", begin_time=").append(begin_time).append(", url=")
				.append(url).append(", addr=").append(addr).append(", duration=").append(duration).append(", replayid=")
				.append(replayid).append("]");
		return builder.toString();
	}
	
}