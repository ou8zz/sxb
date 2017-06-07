package com.sxb.controller.php;

import java.util.Date;
import java.util.Map;
import java.util.TreeMap;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.sxb.model.Replay;
import com.sxb.model.RespsonData;
import com.sxb.model.RespsonPHP;
import com.sxb.qcloud.QcloudApiModuleCenter;
import com.sxb.qcloud.Module.Vod;
import com.sxb.qcloud.Utilities.Json.JSONArray;
import com.sxb.qcloud.Utilities.Json.JSONObject;
import com.sxb.service.ReplayService;
import com.sxb.util.RegexUtil;
import com.sxb.util.ResourceUtil;

/**
 * @description PHP1.7版本接口----视频回放服务RestController（目前没有使用）
 * @author <a href="mailto:ou8zz@sina.com">OLE</a>
 * @date 2016/06/14
 * @version 1.0
 */
@RestController
@RequestMapping("/replay")
@Scope(value="request")
public class ReplayCore {
	private Log log = LogFactory.getLog(ReplayCore.class);
	
	@Resource(name="replayService")
	private ReplayService replayService;

	
	/**
	 * 获取直播视频列表 http://localhost/replay_getbytime.php
	 * @param Replay 视频对象
	 * 请求示例： { "begin_time":"2016-06-29" }
	 * @return RespsonData 返回视频直播信息对象
	 */
	@RequestMapping(method=RequestMethod.POST, value="/replay_getbytime.php")
	public Object getReplayByTime(@RequestBody Replay r) {
		RespsonPHP rd = new RespsonPHP();
		try {
			Object replay = replayService.getReplay(r);
			rd.setData(replay);
		} catch (Exception e) {
			rd.setCode(560);
			rd.setData(e.getMessage());
			log.error("getReplayByTime error", e);
		}
		return rd;
	}
	
	/**
	 * 保存·直播视频addReplay  http://localhost/replay_create.php
	 * @param Replay 视频对象
	 * 请求示例：
	 * {
	 * 	"subject":"title", 
	 * 	"programid":12220, 
	 * 	"userphone":"1523655566", 
	 * 	"groupid":"SE14PE", 
	 * 	"replayid":1005, 
	 * 	"duration":"2016-06-30 00:00:00", 
	 * 	"coverimagepath":"http://o9idyqp20.bkt.clouddn.com/head_image-1467178670840-1111.png?imageView2/2/w/300/h/300/interlace/0/q/100"
	 * }
	 * @return RespsonData成功success或者抛出异常
	 */
	@RequestMapping(method=RequestMethod.POST, value="/replay_create.php")
	public Object addReplay(@RequestBody Replay r) {
//		$livedata = $_POST['replaydata'];
//		$jsondata = json_decode($livedata, true);
//		$livetitle = $jsondata['livetitle'];
//		$programid = $jsondata['roomnum'];
//		$userphone = $jsondata['userphone'];
//		$groupid = $jsondata['groupid'];
//		$replayid = $jsondata['replayid'];
//		$duration = $jsondata['duration'];
		
		RespsonData rd = new RespsonData("replay_create");
		try {
			r.setBegin_time(new Date());
			Object m = replayService.addReplay(r);
			rd.setData(m);
		} catch (Exception e) {
			rd.setErrorCode(560);
			rd.setData(e.getMessage());
			log.error("addReplay error", e);
		}
		return rd;
	}
	
	/**
	 * 根据VID获取视频的详细信息  http://localhost/replay_detail.php
	 * @param vid 视频的唯一ID
	 * 请求示例： { "vid":"200012747_fdb0ed7f3a3c4547a5c3b63435e2f110" }
	 * @return RespsonData 返回视频直播信息对象
	 * {
		  "data": "http:\\/\\/200012747.vod.myqcloud.com\\/200012747_7eea92d4a0ec45dfa2ead0e969d52b2e.f0.mp4",
		  "url": "replay_detail",
		  "errorCode": 0,
		  "errorInfo": ""
		}
	 */
	@RequestMapping(method=RequestMethod.POST, value="/replay_detail.php")
	public Object getReplayDetail(@RequestBody(required=false) Map<String, String> m) {
		RespsonData rd = new RespsonData("replay_detail");
		try {
			String vid = m.get("vid");
			if(RegexUtil.isEmpty(vid)) {
				rd.setErrorCode(561);
				rd.setData("vid is null!");
				return rd;
			}
			
			// 参数列表从配置文件中读取
			TreeMap<String, Object> config = new TreeMap<String, Object>();
			config.put("SecretId", ResourceUtil.getConf("qc.api.secretId"));
			config.put("SecretKey", ResourceUtil.getConf("qc.api.secretKey"));
			config.put("DefaultRegion", ResourceUtil.getConf("qc.api.defaultRegion"));
			config.put("RequestMethod", ResourceUtil.getConf("qc.api.requestMethod"));

			// vid视频类型读取
			QcloudApiModuleCenter module = new QcloudApiModuleCenter(new Vod(), config);
			TreeMap<String, Object> params = new TreeMap<String, Object>();
			params.put("vid", vid);
			
			/* call 方法正式向指定的接口名发送请求，并把请求参数params传入，返回即是接口的请求结果。 */
			String result = module.call("DescribeRecordPlayInfo", params);
			
			// 处理JSON只返回视频url即可
			JSONObject ob = new JSONObject(result);
			JSONArray ja = (JSONArray) ob.get("fileSet");
			ob = (JSONObject) ja.get(0);
			ja = (JSONArray) ob.get("playSet");
			ob = (JSONObject) ja.get(0);
			rd.setData(ob.get("url"));
		} catch (Exception e) {
			rd.setErrorCode(560);
			rd.setData(e.getMessage());
			log.error("getReplayDetail error", e);
		}
		return rd;
	}
}
