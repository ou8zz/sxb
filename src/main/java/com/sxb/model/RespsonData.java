package com.sxb.model;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * @description 返回json结果通用类 
 * <ul>
 *   <li>1. 默认errorCode为0</li>
 *   <li>2. 正常不等于0并且小于200。大于200表示错误</li>
 *   <li>3. errorInfo只有在大于200时有错误信息，正常为空</li>
 *   <li>4. data为返回的Json对象</li>
 *   <li>5. totalItems用于返回有data集合的总分页条数</li>
 *   <li>6. 如果有对象为空则在返回JSON时不序列化该对象属性</li>
 * </ul>
 * @author <a href="mailto:ou8zz@sina.com">OLE</a>
 * @date 2016/06/08
 * @version 1.0
 */
@JsonInclude(Include.NON_NULL)
public class RespsonData implements Serializable {
	private static final long serialVersionUID = 6399276587714654607L;

	private Object data = "";
	private String totalItems;
	private String url;
	private Integer errorCode = 0;
	private String errorInfo = "";
	
	public RespsonData() {
		
	}
	
	public RespsonData(String url) {
		this.url = url;
	}
	
	public Object getData() {
		return data;
	}
	public void setData(Object data) {
		this.data = data;
	}
	public Integer getErrorCode() {
		return errorCode;
	}
	public void setErrorCode(Integer errorCode) {
		this.errorCode = errorCode;
	}
	public String getErrorInfo() {
		return errorInfo;
	}
	public void setErrorInfo(String errorInfo) {
		this.errorInfo = errorInfo;
	}
	public String getTotalItems() {
		return totalItems;
	}
	public void setTotalItems(String totalItems) {
		this.totalItems = totalItems;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((data == null) ? 0 : data.hashCode());
		result = prime * result + ((errorCode == null) ? 0 : errorCode.hashCode());
		result = prime * result + ((errorInfo == null) ? 0 : errorInfo.hashCode());
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
		RespsonData other = (RespsonData) obj;
		if (data == null) {
			if (other.data != null)
				return false;
		} else if (!data.equals(other.data))
			return false;
		if (errorCode == null) {
			if (other.errorCode != null)
				return false;
		} else if (!errorCode.equals(other.errorCode))
			return false;
		if (errorInfo == null) {
			if (other.errorInfo != null)
				return false;
		} else if (!errorInfo.equals(other.errorInfo))
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("RespsonData [data=").append(data).append(", errorCode=").append(errorCode)
				.append(", errorInfo=").append(errorInfo).append("]");
		return builder.toString();
	}
}
