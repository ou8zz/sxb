package com.sxb.model;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * @description 1.7 PHP返回json结果通用类 
 * <ul>
 *   <li>1. 默认code为200</li>
 *   <li>2. errorInfo只有在大于200时有错误信息，正常为空</li>
 *   <li>3. data为返回的Json对象或者提示信息</li>
 * </ul>
 * @author <a href="mailto:ou8zz@sina.com">OLE</a>
 * @date 2016/07/19
 * @version 1.0
 */
@JsonInclude(Include.NON_NULL)
public class RespsonPHP implements Serializable {
	private static final long serialVersionUID = 495721304265531524L;
	private Integer code = 200;
	private Object data = "";
	public RespsonPHP() {
		
	}
	public Object getData() {
		return data;
	}
	public void setData(Object data) {
		this.data = data;
	}
	public Integer getCode() {
		return code;
	}
	public void setCode(Integer code) {
		this.code = code;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((code == null) ? 0 : code.hashCode());
		result = prime * result + ((data == null) ? 0 : data.hashCode());
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
		RespsonPHP other = (RespsonPHP) obj;
		if (code == null) {
			if (other.code != null)
				return false;
		} else if (!code.equals(other.code))
			return false;
		if (data == null) {
			if (other.data != null)
				return false;
		} else if (!data.equals(other.data))
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("RespsonPHP [data=").append(data).append(", code=").append(code).append("]");
		return builder.toString();
	}
}
