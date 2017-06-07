package com.sxb.model;

import java.io.Serializable;
import java.util.Date;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * @description LiveRecord 
 * @author <a href="mailto:ou8zz@sina.com">OLE</a>
 * @date 2016/06/08
 * @version 1.0
 */
public class LiveRecord implements Serializable {
	private static final long serialVersionUID = 6502549952970335947L;

	private Integer id;						// 主键ID
	private String title;					// '标题
	private String cover;					// '封面URL'
	private String host_uid;				// '主播UID' 
	private String host_avatar;				// '主播头像'
	private String host_username;			// '主播用户名
	private Double longitude = 0d;  		// '经度'
	private Double latitude = 0d; 			// '纬度'
	private String address; 				// '地址'
	private Integer av_room_id; 			// 'av房间ID'		
	private String chat_room_id; 			// '聊天室ID'		
	private Integer admire_count = 0; 		// '点赞人数'
	private Integer watch_count = 0;		// '观看人数'
	private Integer real_watch;				// '真实人数'
	private Integer time_span = 0;			// '直播时长'
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
	private Date create_time;				// '创建日期'
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
	private Date modify_time; 				// '更新时间'
	
	public LiveRecord() {
	}
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getCover() {
		return cover;
	}
	public void setCover(String cover) {
		this.cover = cover;
	}
	public String getHost_uid() {
		return host_uid;
	}
	public void setHost_uid(String host_uid) {
		this.host_uid = host_uid;
	}
	public String getHost_avatar() {
		return host_avatar;
	}
	public void setHost_avatar(String host_avatar) {
		this.host_avatar = host_avatar;
	}
	public String getHost_username() {
		return host_username;
	}
	public void setHost_username(String host_username) {
		this.host_username = host_username;
	}
	public Double getLongitude() {
		return longitude;
	}
	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}
	public Double getLatitude() {
		return latitude;
	}
	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public Integer getAv_room_id() {
		return av_room_id;
	}
	public void setAv_room_id(Integer av_room_id) {
		this.av_room_id = av_room_id;
	}
	public String getChat_room_id() {
		return chat_room_id;
	}
	public void setChat_room_id(String chat_room_id) {
		this.chat_room_id = chat_room_id;
	}
	public Integer getAdmire_count() {
		return admire_count;
	}
	public void setAdmire_count(Integer admire_count) {
		this.admire_count = admire_count;
	}
	public Integer getWatch_count() {
		return watch_count;
	}
	public Integer getReal_watch() {
		return real_watch;
	}
	public void setReal_watch(Integer real_watch) {
		this.real_watch = real_watch;
	}
	public void setWatch_count(Integer watch_count) {
		this.watch_count = watch_count;
	}
	public Integer getTime_span() {
		return time_span;
	}
	public void setTime_span(Integer time_span) {
		this.time_span = time_span;
	}
	public Date getCreate_time() {
		return create_time;
	}
	public void setCreate_time(Date create_time) {
		this.create_time = create_time;
	}
	public Date getModify_time() {
		return modify_time;
	}
	public void setModify_time(Date modify_time) {
		this.modify_time = modify_time;
	}
	
	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(getId()).append(getCreate_time()).toHashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj instanceof LiveRecord != true)
			return false;
		LiveRecord other = (LiveRecord) obj;
		return new EqualsBuilder().append(getId(), other.getId()).append(getCreate_time(), other.getCreate_time()).isEquals();
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("LiveRecord [id=").append(id).append(", title=").append(title).append(", cover=").append(cover)
		.append(", host_uid=").append(host_uid).append(", host_avatar=").append(host_avatar)
		.append(", host_username=").append(host_username).append(", longitude=").append(longitude)
		.append(", latitude=").append(latitude).append(", address=").append(address).append(", av_room_id=")
		.append(av_room_id).append(", chat_room_id=").append(chat_room_id).append(", admire_count=")
		.append(admire_count).append(", watch_count=").append(watch_count).append(", time_span=")
		.append(time_span).append(", create_time=").append(create_time).append(", modify_time=")
		.append(modify_time).append("]");
		return builder.toString();
	}
}