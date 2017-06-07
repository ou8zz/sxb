package com.sxb.model;

import java.io.Serializable;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * 七牛云上传文件对象
 * @author <a href="mailto:ou8zz@sina.com">OLE</a>
 * @date 2016/07/05
 * @version 1.0
 */
@JsonInclude(Include.NON_NULL)
public class Qupload implements Serializable {
	private static final long serialVersionUID = 7496588965350017634L;
	private String name;			// 上传的文件名称
	private String prefix;			// 文件访问路径前缀，为七牛云前缀，这个地址目前是测试，后期可能会更换
	private String uploadToken;		// 上传文件token
	private String imagetype;		// 上传文件类型
	private String userphone;		// 上传用户手机
	
	public String getUploadToken() {
		return uploadToken;
	}
	public void setUploadToken(String uploadToken) {
		this.uploadToken = uploadToken;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPrefix() {
		return prefix;
	}
	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}
	public String getImagetype() {
		return imagetype;
	}
	public void setImagetype(String imagetype) {
		this.imagetype = imagetype;
	}
	public String getUserphone() {
		return userphone;
	}
	public void setUserphone(String userphone) {
		this.userphone = userphone;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((imagetype == null) ? 0 : imagetype.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((prefix == null) ? 0 : prefix.hashCode());
		result = prime * result + ((uploadToken == null) ? 0 : uploadToken.hashCode());
		result = prime * result + ((userphone == null) ? 0 : userphone.hashCode());
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
		Qupload other = (Qupload) obj;
		if (imagetype == null) {
			if (other.imagetype != null)
				return false;
		} else if (!imagetype.equals(other.imagetype))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (prefix == null) {
			if (other.prefix != null)
				return false;
		} else if (!prefix.equals(other.prefix))
			return false;
		if (uploadToken == null) {
			if (other.uploadToken != null)
				return false;
		} else if (!uploadToken.equals(other.uploadToken))
			return false;
		if (userphone == null) {
			if (other.userphone != null)
				return false;
		} else if (!userphone.equals(other.userphone))
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Qupload [name=").append(name).append(", prefix=").append(prefix).append(", uploadToken=")
				.append(uploadToken).append(", imagetype=").append(imagetype).append(", userphone=").append(userphone)
				.append("]");
		return builder.toString();
	}
}

