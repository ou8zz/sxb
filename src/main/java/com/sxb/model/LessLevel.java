package com.sxb.model;

import java.io.Serializable;

/**
 * @description 随机数量实体bean
 * @author <a href="mailto:ou8zz@sina.com">OLE</a>
 * @date 2016/07/22
 * @version 1.0
 */
public class LessLevel implements Serializable {
	private static final long serialVersionUID = -3361698824929674289L;
	
	private Integer less;	// 随机数量最大上限
	private Integer min;	// 每次随机累加最小数量
	private Integer max;	// 每次随机累加最大数量
	
	public Integer getLess() {
		return less;
	}
	public void setLess(Integer less) {
		this.less = less;
	}
	public Integer getMin() {
		return min;
	}
	public void setMin(Integer min) {
		this.min = min;
	}
	public Integer getMax() {
		return max;
	}
	public void setMax(Integer max) {
		this.max = max;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((less == null) ? 0 : less.hashCode());
		result = prime * result + ((max == null) ? 0 : max.hashCode());
		result = prime * result + ((min == null) ? 0 : min.hashCode());
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
		LessLevel other = (LessLevel) obj;
		if (less == null) {
			if (other.less != null)
				return false;
		} else if (!less.equals(other.less))
			return false;
		if (max == null) {
			if (other.max != null)
				return false;
		} else if (!max.equals(other.max))
			return false;
		if (min == null) {
			if (other.min != null)
				return false;
		} else if (!min.equals(other.min))
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("LessLevel [less=").append(less).append(", min=").append(min).append(", max=").append(max)
				.append("]");
		return builder.toString();
	}
}