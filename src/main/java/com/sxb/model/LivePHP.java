package com.sxb.model;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * @description LiveRecord 
 * @author <a href="mailto:ou8zz@sina.com">OLE</a>
 * @date 2016/06/08
 * @version 1.0
 * 
 * //	"userphone": "15150516695",
//    "username": "路",
//    "headimagepath": "15150516695/head-1465695686.jpg",
//    "programid": "12190",
//    "subject": "e",
//    "praisenum": "0",
//    "viewernum": "0",
//    "totalnum": "1",
//    "begin_time": "2016-05-09 10:43:53",
//    "groupid": "@TGS#3CHNXUAEN",
//    "url": "",
//    "coverimagepath": "15150516695/coverimage-12190.jpg",
//    "addr": ""
 */
@JsonInclude(Include.NON_NULL)
public class LivePHP implements Serializable {
	private static final long serialVersionUID = 4370898824573709255L;
	private Integer id;						// 主键ID
	private String subject;					// '标题
	private String coverimagepath;			// '封面URL'
	private String userphone;				// '主播UID' 
	private String headimagepath;			// '主播头像'
	private String username;				// '主播用户名
	private String url;
	private String addr; 					// '地址'
	private String groupid; 				// 'av房间ID'		
	private String programid; 				// '聊天室ID'		
	private Integer praisenum = 0; 			// '点赞人数'
	private Integer viewernum = 0;			// '观看人数'
	private Integer totalnum = 0;			// '人数'
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
	private Date begin_time;				// '创建日期'
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
	private Date modify_time; 				// '更新时间'
	
	public LivePHP() {
	}
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getCoverimagepath() {
		return coverimagepath;
	}

	public void setCoverimagepath(String coverimagepath) {
		this.coverimagepath = coverimagepath;
	}

	public String getUserphone() {
		return userphone;
	}

	public void setUserphone(String userphone) {
		this.userphone = userphone;
	}

	public String getHeadimagepath() {
		return headimagepath;
	}

	public void setHeadimagepath(String headimagepath) {
		this.headimagepath = headimagepath;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
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

	public String getGroupid() {
		return groupid;
	}

	public void setGroupid(String groupid) {
		this.groupid = groupid;
	}

	public String getProgramid() {
		return programid;
	}

	public void setProgramid(String programid) {
		this.programid = programid;
	}

	public Integer getPraisenum() {
		return praisenum;
	}

	public void setPraisenum(Integer praisenum) {
		this.praisenum = praisenum;
	}

	public Integer getViewernum() {
		return viewernum;
	}

	public void setViewernum(Integer viewernum) {
		this.viewernum = viewernum;
	}

	public Integer getTotalnum() {
		return totalnum;
	}

	public void setTotalnum(Integer totalnum) {
		this.totalnum = totalnum;
	}

	public Date getBegin_time() {
		return begin_time;
	}

	public void setBegin_time(Date begin_time) {
		this.begin_time = begin_time;
	}

	public Date getModify_time() {
		return modify_time;
	}

	public void setModify_time(Date modify_time) {
		this.modify_time = modify_time;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((addr == null) ? 0 : addr.hashCode());
		result = prime * result + ((begin_time == null) ? 0 : begin_time.hashCode());
		result = prime * result + ((coverimagepath == null) ? 0 : coverimagepath.hashCode());
		result = prime * result + ((groupid == null) ? 0 : groupid.hashCode());
		result = prime * result + ((headimagepath == null) ? 0 : headimagepath.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((modify_time == null) ? 0 : modify_time.hashCode());
		result = prime * result + ((praisenum == null) ? 0 : praisenum.hashCode());
		result = prime * result + ((programid == null) ? 0 : programid.hashCode());
		result = prime * result + ((subject == null) ? 0 : subject.hashCode());
		result = prime * result + ((totalnum == null) ? 0 : totalnum.hashCode());
		result = prime * result + ((url == null) ? 0 : url.hashCode());
		result = prime * result + ((username == null) ? 0 : username.hashCode());
		result = prime * result + ((userphone == null) ? 0 : userphone.hashCode());
		result = prime * result + ((viewernum == null) ? 0 : viewernum.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		LivePHP other = (LivePHP) obj;
		if (addr == null) {
			if (other.addr != null)
				return false;
		} else if (!addr.equals(other.addr))
			return false;
		if (begin_time == null) {
			if (other.begin_time != null)
				return false;
		} else if (!begin_time.equals(other.begin_time))
			return false;
		if (coverimagepath == null) {
			if (other.coverimagepath != null)
				return false;
		} else if (!coverimagepath.equals(other.coverimagepath))
			return false;
		if (groupid == null) {
			if (other.groupid != null)
				return false;
		} else if (!groupid.equals(other.groupid))
			return false;
		if (headimagepath == null) {
			if (other.headimagepath != null)
				return false;
		} else if (!headimagepath.equals(other.headimagepath))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (modify_time == null) {
			if (other.modify_time != null)
				return false;
		} else if (!modify_time.equals(other.modify_time))
			return false;
		if (praisenum == null) {
			if (other.praisenum != null)
				return false;
		} else if (!praisenum.equals(other.praisenum))
			return false;
		if (programid == null) {
			if (other.programid != null)
				return false;
		} else if (!programid.equals(other.programid))
			return false;
		if (subject == null) {
			if (other.subject != null)
				return false;
		} else if (!subject.equals(other.subject))
			return false;
		if (totalnum == null) {
			if (other.totalnum != null)
				return false;
		} else if (!totalnum.equals(other.totalnum))
			return false;
		if (url == null) {
			if (other.url != null)
				return false;
		} else if (!url.equals(other.url))
			return false;
		if (username == null) {
			if (other.username != null)
				return false;
		} else if (!username.equals(other.username))
			return false;
		if (userphone == null) {
			if (other.userphone != null)
				return false;
		} else if (!userphone.equals(other.userphone))
			return false;
		if (viewernum == null) {
			if (other.viewernum != null)
				return false;
		} else if (!viewernum.equals(other.viewernum))
			return false;
		return true;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("LivePHP [id=").append(id).append(", subject=").append(subject).append(", coverimagepath=")
				.append(coverimagepath).append(", userphone=").append(userphone).append(", headimagepath=")
				.append(headimagepath).append(", username=").append(username).append(", url=").append(url)
				.append(", addr=").append(addr).append(", groupid=").append(groupid).append(", programid=")
				.append(programid).append(", praisenum=").append(praisenum).append(", viewernum=").append(viewernum)
				.append(", totalnum=").append(totalnum).append(", begin_time=").append(begin_time)
				.append(", modify_time=").append(modify_time).append("]");
		return builder.toString();
	}

}