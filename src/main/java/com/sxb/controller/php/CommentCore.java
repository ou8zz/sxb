package com.sxb.controller.php;

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
import com.sxb.model.RespsonPHP;
import com.sxb.service.CommentService;

/**
 * @description PHP1.7版本接口----评论点赞RestController
 * @author <a href="mailto:ou8zz@sina.com">OLE</a>
 * @date 2016/06/14
 * @version 1.0
 */
@RestController
@Scope(value="request")
public class CommentCore {
	private Log log = LogFactory.getLog(CommentCore.class);
	
	@Resource(name="commentService")
	private CommentService commentService;

	/**
	 * 创建评论addComment http://localhost/comment_create.php
	 * @param Comment 评论对象 
	 * 请求示例： {"userphone":"1555223554", "article_uuid":"auid100", "content":"neirong"}
	 * @return RespsonData 成功1或者0失败
	 * {
		  "code": 200,
		  "data": "save comment success"
		}
	 */
	@RequestMapping(method=RequestMethod.POST, value="/comment_create.php")
	public Object addComment(@RequestBody Comment c) {
		RespsonPHP rd = new RespsonPHP();
		try {
			c.setCreate_time(new Date());
			commentService.addComment(c);
			rd.setData("save comment success");
		} catch (Exception e) {
			rd.setCode(560);
			rd.setData(e.getMessage());
			log.error("addComment error", e);
		}
		return rd;
	}

	/**
	 * 获取评论或者评论列表getComment  http://localhost/comment_listget.php
	 * @param Comment 评论对象 
	 * 请求示例：{"article_uuid":100, "userphone":"15966636322", "limit":10, "offset":0}
	 * @return RespsonData 封装MAP返回对象
	 */
	@RequestMapping(method=RequestMethod.POST, value="/comment_listget.php")
	public Object getComment(@RequestBody Comment c) {
		RespsonPHP rd = new RespsonPHP();
		try {
			c.setOffset(c.getOffset()+1);
			Object m = commentService.getComment(c);
			rd.setData(m);
			return rd;
		} catch (Exception e) {
			rd.setCode(560);
			rd.setData(e.getMessage());
			log.error("getComment error", e);
		}
		return rd;
	}
	
	/**
	 * 设置用户点赞，如果该用户已经点赞  http://localhost/comment_favor_set.php
	 * @param c	用户信息
	 * 请求示例： {"userphone":"18625155246", "comment_uuid":15500, "favor":true}
	 * @return	RespsonData 返回点赞设置成功或者已经点过赞 
	 */
	@RequestMapping(method=RequestMethod.POST, value="/comment_favor_set.php")
	public Object setFavor(@RequestBody Comment c) {
		RespsonPHP rd = new RespsonPHP();
		try {
			commentService.setFavor(c);
			rd.setData("favor success");
		} catch (Exception e) {
			rd.setCode(560);
			rd.setData(e.getMessage());
			log.error("setFavor error", e);
		}
		return rd;
	}
	
}
