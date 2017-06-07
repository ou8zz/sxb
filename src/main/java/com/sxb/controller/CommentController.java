package com.sxb.controller;

import java.util.Date;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.sxb.model.Comment;
import com.sxb.model.RespsonData;
import com.sxb.service.CommentService;

/**
 * @description 评论点赞RestController
 * @author <a href="mailto:ou8zz@sina.com">OLE</a>
 * @date 2016/06/14
 * @version 1.0
 */
@RestController
@RequestMapping("/comment")
@Scope(value="request")
public class CommentController {
	private Log log = LogFactory.getLog(CommentController.class);
	
	@Resource(name="commentService")
	private CommentService commentService;

	/**
	 * 创建评论addComment http://localhost/sxb/comment/comment_create
	 * @param Comment 评论对象 
	 * 请求示例： {"userphone":"1555223554", "article_uuid":"auid100", "content":"neirong"}
	 * @return RespsonData 成功1或者0失败
	 */
	@RequestMapping(method=RequestMethod.POST, value="/comment_create")
	public Object addComment(@RequestBody Comment c) {
		RespsonData rd = new RespsonData("comment_create");
		try {
			c.setCreate_time(new Date());
			Object i = commentService.addComment(c);
			rd.setData(i);
		} catch (Exception e) {
			rd.setErrorCode(560);
			rd.setErrorInfo(e.getMessage());
			log.error("addComment error", e);
		}
		return rd;
	}

	/**
	 * 获取评论或者评论列表getComment  http://localhost/sxb/comment/comment_listget
	 * @param Comment 评论对象 
	 * 请求示例：{"article_uuid":100, "userphone":"15966636322", "limit":10, "offset":0}
	 * @return RespsonData 封装MAP返回对象
	 */
	@RequestMapping(method=RequestMethod.POST, value="/comment_listget")
	public Object getComment(@RequestBody Comment c) {
		RespsonData rd = new RespsonData("comment_listget");
		try {
			Object m = commentService.getComment(c);
			rd.setData(m);
			return rd;
		} catch (Exception e) {
			rd.setErrorCode(560);
			rd.setErrorInfo(e.getMessage());
			log.error("getComment error", e);
		}
		return rd;
	}
	
	/**
	 * 设置用户点赞，如果该用户已经点赞  http://localhost/sxb/comment/comment_favor_set
	 * @param c	用户信息
	 * 请求示例： {"userphone":"18625155246", "comment_uuid":15500, "favor":true}
	 * @return	RespsonData 返回点赞设置成功或者已经点过赞 
	 */
	@RequestMapping(method=RequestMethod.POST, value="/comment_favor_set")
	public Object setFavor(@RequestBody Comment c) {
		RespsonData rd = new RespsonData("comment_favor_set");
		try {
			Object m = commentService.setFavor(c);
			rd.setData(m);
		} catch (Exception e) {
			rd.setErrorCode(560);
			rd.setErrorInfo(e.getMessage());
			log.error("setFavor error", e);
		}
		return rd;
	}
	
	
	/**
	 * 收集用户提交的反馈  http://localhost/sxb/comment/feedback
	 * @param c	用户提交的信息
	 * 请求示例： {"userphone":"18625155246", "content":"这个APP还有XXX需要改进的地方"}
	 * @return	RespsonData 
	 */
	@RequestMapping(method=RequestMethod.POST, value="/feedback")
	public Object feedback(@RequestBody Comment c) {
		RespsonData rd = new RespsonData("feedback");
		try {
			Object m = commentService.feedback(c);
			rd.setData(m);
		} catch (Exception e) {
			rd.setErrorCode(560);
			rd.setErrorInfo(e.getMessage());
			log.error("feedback error", e);
		}
		return rd;
	}
	
}
