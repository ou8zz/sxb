package com.sxb.util;

import java.io.Serializable;

import com.github.pagehelper.Page;

/**
 * @description 分页参数对象，用于前台传递和后端返回分页HTML标签
 * @author <a href="mailto:ou8zz@sina.com">OLE</a>
 * @date 2016/05/29
 * @version 1.0
 */
public class Paramer implements Serializable {
	private static final long serialVersionUID = 7954521521071993612L;
	private int pageNum;	 	// 开始条数 默认第一页
	private int pageSize;		// 到N条数
	private String func;		// Ajax分页传递函数名称
	private String option;		// 动态加成显示数据大小
	private String pageHtml;	// 分页的HTML标签
	private Object ob;			// 存储对象使用

	public Paramer() {}
	
	public Paramer(int pageNum, int pageSize) {
		// check:
		if (pageNum < 1) pageNum = 1;
		if (pageSize < 1) pageSize = 1;
		this.pageNum = pageNum;
		this.pageSize = pageSize;
	}

	public int getPageNum() {
		return pageNum;
	}

	public void setPageNum(int pageNum) {
		this.pageNum = pageNum;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public String getFunc() {
		return func;
	}

	public void setFunc(String func) {
		this.func = func;
	}

	public String getOption() {
		return option;
	}

	public void setOption(String option) {
		this.option = option;
	}

	public String getPageHtml() {
		return pageHtml;
	}

	public void setPageHtml(String pageHtml) {
		this.pageHtml = pageHtml;
	}

	public Object getOb() {
		return ob;
	}

	public void setOb(Object ob) {
		this.ob = ob;
	}
	
	/**
	 * 分页HTML处理
	 * @param p					分页对象
	 * @return					分页字符串
	 */
	public String getPageTag(Page<Object> page) {
		setOb(page);									// 分页后的集合放到page对象里面方便传递
		String funcName = getFunc();					// 前台调用ajax分页的函数
		StringBuilder buf = new StringBuilder();
		buf.append("<ul data-am-widget='pagination' class='am-pagination am-pagination-default'><li> 共" + page.getTotal() + " 条</li><li>第 " + page.getPageNum());
		buf.append(" 页</li><li>共 " + page.getPages() + " 页</li>");
		
		String option = "<option value='10'>10</option><option value='20'>20</option><option value='40'>40</option><option value='80'>80</option><option value='200'>200</option>";
		if(getOption() != null) option = getOption()+option;
		String val = "value='"+getPageSize()+"'";
		option = option.replace(val, val+" selected");

		buf.append("<li><select class='carr' onchange=\"javascript:"+funcName+"(this, {'func':'"+funcName+"', 'pageNum':" + page.getPageNum() + ", 'pageSize':this.value});\">" + option + "</select></li>");
		
		if (page.getPages() == 0)
			buf.append("<li>没有可显示的条目</li></ul>");
		else {
			// first page tag
			buf.append(getPreviousTag(page.getPageNum(), page.getPages(), page.getPageSize(), funcName));
			
			// last page tag and HTML end of page tag
			buf.append(getNextTag(page.getPageNum(), page.getPages(), page.getPageSize(), funcName));
		}
		
		buf.append("<li>跳转到<input id='igov' class='igov' style='width:24px;' onkeyup='this.value=this.value.replace(/[^0-9.]/g,\"\")'/> ");
		buf.append("<a class='am-btn am-btn-default' href='javascript:void(0)' onclick=\"javascript:").append(funcName);
		buf.append("(this, {'func':'"+funcName+"', 'pageNum':this.parentNode.getElementsByTagName('input')['igov'].value, 'pageSize':"+getPageSize()+"})\"><span class='am-icon-check'></span></a></li></ul>");
		//buf.append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;");
		
		setPageHtml(buf.toString());					// 返回前台的分页HTML标签
		return getPageHtml();
	}

	/**
	 * get previous HTML TAG
	 * @params currentPage	当前分页数
	 * @params totalPage 	分页总页数
	 * @params limit		分页大小
	 * @params func			前台js函数名
	 * @return previous HTML TAG
	 */
	private String getPreviousTag(long currentPage, long totalPage, long limit, String func) {
		if (currentPage == 1)
			return "<li class='am-pagination-first'>首页</li><li class='am-pagination-prev'>上一页</li>";
		else {
			long i = currentPage - 1;
			String link = func + "(this, {'func':'"+func+"', 'pageNum':1, 'pageSize':"+limit+"}";
			String previous = "<li><a href='javascript:void(0)' onclick=\"javascript:" + link + ");\" class='prev_page' rel='prev'>首页</a></li>";
			link = func + "(this, {'func':'"+func+"', 'pageNum':"+i+", 'pageSize':"+limit+"}";
			previous += "<li><a href='javascript:void(0)' onclick=\"javascript:" + link + ");\" class='prev_page' rel='prev'>上一页</a></li>";
			return previous;
		}
	}

	/**
	 * get next HTML TAG
	 * @params currentPage	当前分页数
	 * @params totalPage 	分页总页数
	 * @params limit		分页大小
	 * @params func			前台js函数名
	 * @return next HTML TAG
	 */
	private String getNextTag(long currentPage, long totalPage, long limit, String func) {
		if(currentPage == 1 && totalPage < 2) 
			return "<li class='am-pagination-next'>下一页</li><li class='am-pagination-last'>尾页</li>";
		else if(currentPage == totalPage && currentPage > 1)
			return "<li class='am-pagination-next'>下一页</li><li class='am-pagination-last'>尾页</li>";
		else{
			long i = currentPage + 1;
			StringBuilder str = new StringBuilder();
			str.append("<li><a href='javascript:void(0)' onclick=\"javascript:").append(func);
			str.append("(this, {'func':'"+func+"', 'pageNum':"+i+", 'pageSize':"+limit+"});\"");
			str.append("class='am-pagination-next' rel='next'>下一页</a></li>");
			str.append("<li><a href='javascript:void(0)' onclick=\"javascript:").append(func);
			str.append("(this, {'func':'"+func+"', 'pageNum':"+totalPage+", 'pageSize':"+limit+"});\"");
			str.append("class='am-pagination-last' rel='next'>尾页</a></li>");
			return str.toString();
		}
	}
}
