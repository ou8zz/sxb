package com.sxb.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sxb.model.Comment;
import com.sxb.service.CommentService;
import com.sxb.util.RegexUtil;

/**
 * @description 评论点赞服务接口实现
 * @author <a href="mailto:ou8zz@sina.com">OLE</a>
 * @date 2016/06/14
 * @version 1.0
 */
@Repository(value="commentService")
@Scope(value="prototype")
@Transactional(propagation=Propagation.REQUIRED)
public class CommentServiceImpl implements CommentService {

	@Autowired
	private SqlSession sqlSession;
	
	/**
	 * 创建评论addComment
	 * @param Comment 评论对象
	 * @return 成功success或者抛出异常
	 */
	@Override
	public Object addComment(Comment c) {
		int i = sqlSession.insert("comment.addComment", c);
		return i;
	}

	/**
	 * 获取评论或者评论列表getComment
	 * @param Comment 评论对象
	 * @return 封装MAP返回对象
	 */
	@Override
	public Map<String, Object> getComment(Comment c) {
		// 获取所有article_uuid的评论的数量
		// Integer commentCount = sqlSession.selectOne("comment.getCommentCount", c);

		// 获取article_uuid文章有多少个赞 
		Integer articleFavorCount = sqlSession.selectOne("comment.getArticleFavorCount", c);
		
		// 查询评论情况
		Page<Object> startPage = PageHelper.startPage(c.getOffset(), c.getLimit());
		List<Object> comments = sqlSession.selectList("comment.getComment", c);
		
		// 封装MAP返回对象
		Map<String, Object> m = new HashMap<String, Object>();
		m.put("total", startPage.getTotal());
		m.put("favorTotal", articleFavorCount);
		m.put("comments", comments);
		return m;
	}
	
	/**
	 * 设置用户点赞，如果该用户已经点赞
	 * @param c	用户信息
	 * @return	返回点赞设置成功或者已经点过赞
	 */
	@Override
	public Object setFavor(Comment c) {
		Comment sc = sqlSession.selectOne("comment.getUserFavor", c);
		int i = 0;
		if(RegexUtil.isEmpty(sc)) {
			i = sqlSession.insert("comment.setUserFavor", c);
		} else {
			i = sqlSession.update("comment.editUserFavor", c);
		}
		return i;
	}
	
	/**
	 * 收集用户提交的反馈
	 * @param c	用户信息
	 * @return  返回成功或者失败
	 */
	@Override
	public Object feedback(Comment c) {
		int i = sqlSession.insert("comment.feedback", c);
		return i;
	}
}
