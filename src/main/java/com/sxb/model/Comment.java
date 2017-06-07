package com.sxb.model;

import java.io.Serializable;
import java.util.Date;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * @description 随心播评论
 * @author <a href="mailto:ou8zz@sina.com">OLE</a>
 * @date 2016/06/14
 * @version 1.0
 */
@JsonInclude(Include.NON_NULL)
public class Comment implements Serializable {
	private static final long serialVersionUID = -7614402552482999889L;
	
	private Long id;
	private String article_uuid;
	private String content;
	private String userphone;
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
	private Date create_time;
	
	// 评论 VO
	private String comment_uuid;	// 评论ID
	private String username;		// 用户名称
	private String headimagepath;	// 用户头像
	private String favor_num;		// 评论数量
	private Boolean favor = false;	// 是否点赞true or false
	private Integer limit;
	private Integer offset;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getArticle_uuid() {
		return article_uuid;
	}

	public void setArticle_uuid(String article_uuid) {
		this.article_uuid = article_uuid;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getUserphone() {
		return userphone;
	}

	public void setUserphone(String userphone) {
		this.userphone = userphone;
	}

	public Date getCreate_time() {
		return create_time;
	}

	public void setCreate_time(Date create_time) {
		this.create_time = create_time;
	}

	public String getComment_uuid() {
		return comment_uuid;
	}

	public void setComment_uuid(String comment_uuid) {
		this.comment_uuid = comment_uuid;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getHeadimagepath() {
		return headimagepath;
	}

	public void setHeadimagepath(String headimagepath) {
		this.headimagepath = headimagepath;
	}

	public String getFavor_num() {
		return favor_num;
	}

	public void setFavor_num(String favor_num) {
		this.favor_num = favor_num;
	}

	public Boolean getFavor() {
		return favor;
	}

	public void setFavor(Boolean favor) {
		this.favor = favor;
	}

	public Integer getLimit() {
		return limit;
	}

	public void setLimit(Integer limit) {
		this.limit = limit;
	}

	public Integer getOffset() {
		return offset;
	}

	public void setOffset(Integer offset) {
		this.offset = offset;
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(getUserphone()).append(getId()).toHashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj instanceof Comment != true)
			return false;
		Comment other = (Comment) obj;
		return new EqualsBuilder().append(getUserphone(), other.getUserphone()).append(getId(), other.getId()).isEquals();
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Comment [id=").append(id).append(", article_uuid=").append(article_uuid).append(", content=")
				.append(content).append(", userphone=").append(userphone).append(", create_time=").append(create_time)
				.append("]");
		return builder.toString();
	}
}