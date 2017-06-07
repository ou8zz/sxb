package com.sxb.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
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

import com.sxb.model.LiveRecord;
import com.sxb.model.RespsonData;
import com.sxb.service.LiveRecordService;
import com.sxb.util.CosSign;
import com.sxb.util.ResourceUtil;

/**
 * @description 1.8视频直播后台管理 RestController
 * @author <a href="mailto:ou8zz@sina.com">OLE</a>
 * @date 2016/05/29
 * @version 1.0
 */
@RestController
@RequestMapping("/live")
@Scope(value="request")
public class LiveController {
	private Log log = LogFactory.getLog(LiveController.class);
	
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
	@RequestMapping(method=RequestMethod.POST, value="/start")
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
	 * ## 2 直播列表 http://ole:8080/sxb/live/list
	   ## 请求包示例
		{
		    "pageIndex": 1,
		    "pageSize": 10
		}
		### 返回报示例
		{"data": [{
		      "id": 100016,
		      "title": "直播01",
		      "cover": "cover",
		      "host_uid": "user1003",
		      "host_avatar": "1",
		      "host_username": "王大锤",
		      "longitude": 0,
		      "latitude": 0,
		      "address": "浦东南路109号",
		      "av_room_id": 0,
		      "chat_room_id": "1006",
		      "admire_count": 0,
		      "watch_count": 0,
		      "time_span": 0,
		      "create_time": "2016-06-21 13:47:34",
		      "modify_time": "2016-06-21 13:47:34"
		    }
		  ],
		  "totalItems": "5",
		  "errorCode": "0",
		  "errorInfo": ""
		}
	 */
	@RequestMapping(method=RequestMethod.POST, value="/list")
	public Object getLiveRecords(@RequestBody Map<String, String> m) {
		RespsonData rd = null;
		try {
			Integer offset = Integer.valueOf(m.get("pageIndex"));
			Integer limit = Integer.valueOf(m.get("pageSize"));
			rd = liveRecordService.getList(offset, limit);
			rd.setUrl("list");
		} catch (Exception e) {
			rd.setErrorCode(560);
			rd.setErrorInfo(e.getMessage());
			log.error("getLiveRecords error", e);
		}
		return rd;
	}
	
	/**
	 * ## 3 主播心跳包 http://ole:8080/sxb/live/host_heartbeat
		### 请求包示例
		{
		    "host_uid": "user1002",
		    "watch_count": 100,
		    "admire_count": 10,
			"time_span": 100
		}
		### 返回包示例
		data 返回大于0表示成功
		{
		  "data": 1,	
		  "errorCode": "0",
		  "errorInfo": ""
		}
	 */
	@RequestMapping(method=RequestMethod.POST, value="/host_heartbeat")
	public Object heartBeat(@RequestBody LiveRecord lr) {
		RespsonData rd = new RespsonData("host_heartbeat");
		try {
			lr.setModify_time(new Date());
			Integer i = liveRecordService.updateByHostUid(lr);
			rd.setData(i);
		} catch (Exception e) {
			rd.setErrorCode(560);
			rd.setErrorInfo(e.getMessage());
			log.error("heartBeat error", e);
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
	@RequestMapping(method=RequestMethod.POST, value="/end")
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
	
	/**
	 * ## 5 获取av room ID  http://ole:8080/sxb/live/get_user_room
		###请求包示例
		{
		    "uid": "user1000"
		}
		###返回包示例
		{
		    "data": 20003,
		    "errorCode": 0, 
		    "errorInfo": ""
		}
	 */
	@RequestMapping(method=RequestMethod.POST, value="/get_user_room")
	public Object getUserAvRoom(@RequestBody Map<String, String> m) {
		RespsonData rd = new RespsonData("get_user_room");
		try {
			Integer roomId = liveRecordService.load(m.get("uid"));
			rd.setData(roomId);
		} catch(Exception e) {
			rd.setErrorCode(560);
			rd.setErrorInfo(e.getMessage());
			log.error("endLiveRecord error", e);
		}
		return rd;
	}

	/**
	 * ## 6 获取COS签名 http://ole:8080/sxb/live/get_sign
		### 6.3 请求包示例
		{}
		### 6.6 返回包示例
		{
		    "data": {
		        "sign": "cos sign return here"
		    }, 
		    "errorCode": 0, 
		    "errorInfo": ""
		}
		### 6.6 返回包专有字段说明
		| 字段|类型 |说明 |
		|---------|---------|---------|
		| sign | String | 签名。 |
	 */
	@RequestMapping(method=RequestMethod.POST, value="/get_sign")
	public Object getSign() {
		RespsonData rd = new RespsonData("get_sign");
		try {
			String APPID = ResourceUtil.getConf("cos.sign.appId");
			String SECRET_ID = ResourceUtil.getConf("cos.sign.secretId");
			String SECRET_KEY = ResourceUtil.getConf("cos.sign.secretKey");
			long expired = System.currentTimeMillis() + 30 * 24 * 60 * 60;
			
			StringBuffer sb = new StringBuffer();
			int appSign = CosSign.appSign(APPID, SECRET_ID, SECRET_KEY, expired, null, sb);
			if(appSign==0) {
				rd.setData(sb.toString());
			} else {
				rd.setData(appSign);
				rd.setErrorCode(560);
				rd.setErrorInfo("get cos sign error");
			}
		} catch(Exception e) {
			rd.setErrorCode(560);
			rd.setErrorInfo(e.getMessage());
			log.error("endLiveRecord error", e);
		}
		return rd;
	}
}
