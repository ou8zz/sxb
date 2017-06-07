package com.sxb.controller.php;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.reflect.TypeToken;
import com.sxb.model.LivePHP;
import com.sxb.model.LiveRecord;
import com.sxb.model.RespsonData;
import com.sxb.model.RespsonPHP;
import com.sxb.service.LiveRecordService;
import com.sxb.util.HtmTools;
import com.sxb.util.JsonUtils;
import com.sxb.util.RegexUtil;

/**
 * @description PHP1.7版本接口----视频直播后台管理 RestController
 * @author <a href="mailto:ou8zz@sina.com">OLE</a>
 * @date 2016/05/29
 * @version 1.0
 */
@RestController
@Scope(value="request")
public class LiveCore {
	private Log log = LogFactory.getLog(LiveCore.class);
	
	@Resource(name="liveRecordService")
	private LiveRecordService liveRecordService;
	
	@InitBinder
	protected void initBinder(WebDataBinder binder) {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		binder.registerCustomEditor(Date.class, new CustomDateEditor(df,false));
	}
	
	/**
	 * ## 1 开始直播 http://ole:8080/sxb/live/start
		### 请求包示例
		{
		    "title": "测试直播",
		    "cover": "http://aa.com",
		    "chat_room_id": "1",
		    "av_room_id": 1,
	        "host_uid": "user1000",
	        "host_avatar": "http://r.plures.net/lg/images/star/live/topbar-logo-large.png",
	        "host_username": "用户名",
	        "longitude": 1.2,
	        "latitude": 2.1,
	        "address": "上海"
		}
		### 返回包示例
		{
		    "errorInfo": "",
		    "errorCode": 0
		}
	 */
	@RequestMapping(method=RequestMethod.POST, value="/start.php")
	public Object addLiveRecord(@RequestBody LiveRecord lr) {
		RespsonData rd = new RespsonData("start");
		try {
			lr.setCreate_time(new Date());
			lr.setModify_time(new Date());
			lr.setAdmire_count(0);
			lr.setWatch_count(0);
			lr.setTime_span(0);
			Object i = liveRecordService.save(lr);
			rd.setData(i);
		} catch(Exception e) {
			rd.setErrorCode(560);
			rd.setErrorInfo(e.getMessage());
			log.error("addLiveRecord error", e);
		}
		return rd;
	}
	
	/**
	 * ## 2 直播列表 http://ole:8080/sxb/live_listget.php
	 * 1.7已经禁用直播功能,此处直接返回了一个空的list
	 * @param str
	 * @return
	 */
	@RequestMapping(method=RequestMethod.POST, value="/live_listget.php")
	public Object getLiveRecords(@RequestBody(required=false) String str) {
		RespsonPHP rp = new RespsonPHP();
		try {
			List<LivePHP> php = new ArrayList<LivePHP>();
			rp.setCode(200);
			rp.setData(php);
		} catch (Exception e) {
			rp.setCode(560);
			rp.setData(e.getMessage());
			log.error("getLiveRecords error", e);
		}
		return rp;
	}
	
	/**
	 * ## 观众进入直播房间  http://ole:8080/sxb/enter_room.php
		### 请求包示例
		viewerdata={
		    "userphone": "18621577368"
		    "roomnum": 10005
		}
		### 返回包示例
		{
		  "data": "",	
		  "code": "0",
		}
	 */
	@RequestMapping(method=RequestMethod.POST, value="/enter_room.php")
	public Object enterRoom(@RequestBody(required=false) String viewerdata) {
		RespsonPHP rd = new RespsonPHP();
		try {
			// 处理PHP格式数据，苦逼
			if(RegexUtil.isEmpty(viewerdata)) {
				rd.setCode(561);
				rd.setData("viewerdata is null");
				return rd;
			}
			viewerdata = HtmTools.decode(viewerdata);
			viewerdata = viewerdata.split("=")[1];
			Map<String, String> m = JsonUtils.fromJson(viewerdata, new TypeToken<Map<String, String>>() {}.getType());
			LiveRecord lr = new LiveRecord();
			lr.setHost_uid(m.get("userphone"));
			lr.setModify_time(new Date());
			liveRecordService.updateByHostUid(lr);
			rd.setData("update liveuser success");
		} catch (Exception e) {
			rd.setCode(560);
			rd.setData(e.getMessage());
			log.error("enterRoom error", e);
		}
		return rd;
	}
	
	/**
	 * ## 观众退出房间 http://ole:8080/sxb/room_withdraw.php
		### 请求包示例
		viewerout={
		    "userphone": "18621577368"
		}
		### 返回包示例
		{
		  "data": "",	
		  "code": "0",
		}
	 */
	@RequestMapping(method=RequestMethod.POST, value="/room_withdraw.php")
	public Object withdrawRoom(@RequestBody(required=false) String viewerout) {
		RespsonPHP rd = new RespsonPHP();
		try {
			// 处理PHP格式数据，苦逼
			if(RegexUtil.isEmpty(viewerout)) {
				rd.setCode(561);
				rd.setData("viewerout is null");
				return rd;
			}
			viewerout = HtmTools.decode(viewerout);
			viewerout = viewerout.split("=")[1];
			Map<String, String> m = JsonUtils.fromJson(viewerout, new TypeToken<Map<String, String>>() {}.getType());
			LiveRecord lr = new LiveRecord();
			lr.setHost_uid(m.get("userphone"));
			lr.setModify_time(new Date());
			liveRecordService.updateByHostUid(lr);
			rd.setData("withdraw from living success");
		} catch (Exception e) {
			rd.setCode(560);
			rd.setData(e.getMessage());
			log.error("withdrawRoom error", e);
		}
		return rd;
	}
	
	/**
	 * ## 3 主播心跳包 http://ole:8080/sxb/update_heart.php
	 */
	@RequestMapping(method=RequestMethod.POST, value="/update_heart.php")
	public Object heartBeat(@RequestBody(required=false) String heartTime) {
		RespsonPHP rd = new RespsonPHP();
		return rd;
	}
	
	/**
	 * ## 给房间增加点赞  http://ole:8080/sxb/room_withdraw.php
		### 请求包示例
		praisedata={
		    "userphone": "18621577368"
		    "roomnum": 10005
		}
		### 返回包示例
		{
		  "data": "",	
		  "code": "0",
		}
	 */
	@RequestMapping(method=RequestMethod.POST, value="/live_addpraise.php")
	public Object addPraise(@RequestBody(required=false) String praisedata) {
		RespsonPHP rd = new RespsonPHP();
		try {
			// 处理PHP格式数据，苦逼
			if(RegexUtil.isEmpty(praisedata)) {
				rd.setCode(561);
				rd.setData("viewerout is null");
				return rd;
			}
			praisedata = HtmTools.decode(praisedata);
			praisedata = praisedata.split("=")[1];
			Map<String, String> m = JsonUtils.fromJson(praisedata, new TypeToken<Map<String, String>>() {}.getType());
			LiveRecord lr = new LiveRecord();
			lr.setChat_room_id(m.get("roomnum"));
			LiveRecord res = (LiveRecord) liveRecordService.loadByHostUid(lr);
			lr.setModify_time(new Date());
			lr.setAdmire_count(res.getAdmire_count()+1);
			liveRecordService.updateByHostUid(lr);
			rd.setData("withdraw from living success");
		} catch (Exception e) {
			rd.setCode(560);
			rd.setData(e.getMessage());
			log.error("addPraise error", e);
		}
		return rd;
	}
	
	/**
	 * ## 4 直播结束 http://ole:8080/sxb/live/end
		### 请求包示例
		{
		    "host_uid": "user1002"
		}
		### 返回包
		{"data": {
		    "id": 100018,
		    "title": "直播01",
		    "cover": "cover",
		    "host_uid": "user1002",
		    "host_avatar": "1",
		    "host_username": "王大锤",
		    "longitude": 0,
		    "latitude": 0,
		    "address": "浦东南路109号",
		    "av_room_id": 0,
		    "chat_room_id": "1006",
		    "admire_count": 20,
		    "watch_count": 20,
		    "time_span": 20,
		    "create_time": "2016-06-21 13:47:40",
		    "modify_time": "2016-06-22 00:00:00"
		  },
		  "totalItems": null,
		  "errorCode": "0",
		  "errorInfo": ""
		}
	 */
	@RequestMapping(method=RequestMethod.POST, value="/end.php")
	public Object endLiveRecord(@RequestBody LiveRecord lr) {
		RespsonData rd = new RespsonData("end");
		try {
			Object i = liveRecordService.endLiveRecord(lr);
			rd.setData(i);
		} catch(Exception e) {
			rd.setErrorCode(560);
			rd.setErrorInfo(e.getMessage());
			log.error("endLiveRecord error", e);
		}
		return rd;
	}
	
}
