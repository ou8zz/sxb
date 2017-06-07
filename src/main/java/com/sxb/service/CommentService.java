package com.sxb.service;

import com.sxb.model.Comment;

/**
 * @description 评论点赞服务接口
 * @author <a href="mailto:ou8zz@sina.com">OLE</a>
 * @date 2016/06/14
 * @version 1.0
 */
public interface CommentService {

	/**
	 * 创建评论addComment
	 * @param Comment 评论对象
	 * @return 成功success或者抛出异常
	 */
	Object addComment(Comment c);
	
	/**
	 * 获取评论或者评论列表getComment
	 * @param c 查询参数
	 * @return	返回封装MAP返回对象
	 */
	Object getComment(Comment c);
	
	/**
	 * 设置用户点赞，如果该用户已经点赞
	 * @param c	用户信息
	 * @return	返回点赞设置成功或者已经点过赞
	 */
	Object setFavor(Comment c);
	
	
	/**
	 * 收集用户提交的反馈
	 * @param c	用户信息
	 * @return  返回成功或者失败
	 */
	Object feedback(Comment c);
}
