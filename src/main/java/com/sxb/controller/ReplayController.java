package com.sxb.controller;

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
import com.sxb.qcloud.QcloudApiModuleCenter;
import com.sxb.qcloud.Module.Vod;
import com.sxb.qcloud.Utilities.Json.JSONArray;
import com.sxb.qcloud.Utilities.Json.JSONObject;
import com.sxb.service.ReplayService;
import com.sxb.util.RegexUtil;
import com.sxb.util.ResourceUtil;

/**
 * @description 视频回放服务RestController
 * @author <a href="mailto:ou8zz@sina.com">OLE</a>
 * @date 2016/06/14
 * @version 1.0
 */
@RestController
@RequestMapping("/replay")
@Scope(value="request")
public class ReplayController {
	private Log log = LogFactory.getLog(ReplayController.class);
	
	@Resource(name="replayService")
	private ReplayService replayService;

	
	/**
	 * 获取直播视频列表 http://localhost/sxb/replay/replay_getbytime
	 * @param Replay 视频对象
	 * 请求示例： { "begin_time":"2016-06-29" }
	 * @return RespsonData 返回视频直播信息对象
	 */
	@RequestMapping(method=RequestMethod.POST, value="/replay_getbytime")
	public Object getReplayByTime(@RequestBody Replay r) {
		RespsonData rd = new RespsonData("replay_getbytime");
		try {
			Object replay = replayService.getReplay(r);
			rd.setData(replay);
		} catch (Exception e) {
			rd.setErrorCode(560);
			rd.setErrorInfo(e.getMessage());
			log.error("getReplayByTime error", e);
		}
		return rd;
	}
	
	/**
	 * 保存·直播视频addReplay  http://localhost/sxb/replay/replay_create
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
	@RequestMapping(method=RequestMethod.POST, value="/replay_create")
	public Object addReplay(@RequestBody Replay r) {
		RespsonData rd = new RespsonData("replay_create");
		try {
			r.setBegin_time(new Date());
			Object m = replayService.addReplay(r);
			rd.setData(m);
		} catch (Exception e) {
			rd.setErrorCode(560);
			rd.setErrorInfo(e.getMessage());
			log.error("addReplay error", e);
		}
		return rd;
	}
	
	/**
	 * 根据VID获取视频的详细信息  http://localhost/sxb/replay/replay_detail
	 * 后续因为后台需要上传视频文件而VID取不到上传的视频文件，新增该方法VID传参规则为@file文件为上传文件操作方式，原VID获取逻辑不变。
	 * @param vid 视频的唯一ID
	 * 请求示例： { "vid":"200012747_fdb0ed7f3a3c4547a5c3b63435e2f110" }
	 * 上传文件请求示例：{ "vid":"20160722@file" }
	 * @return RespsonData 返回视频直播信息对象
	 * {
		  "data": {
		    "image_url": "http://p.qpic.cn/videoyun/0/200012747_fdb0ed7f3a3c4547a5c3b63435e2f110_1/640",
		    "url": "http://200012747.vod.myqcloud.com/200012747_fdb0ed7f3a3c4547a5c3b63435e2f110.f0.mp4"
		  },
		  "url": "replay_detail",
		  "errorCode": 0,
		  "errorInfo": ""
		}
	 */
	@RequestMapping(method=RequestMethod.POST, value="/replay_detail")
	public Object getReplayDetail(@RequestBody(required=false) Map<String, String> m) {
		RespsonData rd = new RespsonData("replay_detail");
		try {
			String vid = m.get("vid");
			if(RegexUtil.isEmpty(vid)) {
				rd.setErrorCode(561);
				rd.setErrorInfo("vid is null!");
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
			String result = "";
			
			/* call 方法正式向指定的接口名发送请求，并把请求参数params传入，返回即是接口的请求结果。
			 * DescribeVodPlayInfo 上传文件查找
			 * DescribeRecordPlayInfo VID文件查找 */
			int fn = vid.indexOf("@file");
			if(fn>0) {
				String fileName = vid.split("@file")[0];
				params.put("fileName", fileName);
				result = module.call("DescribeVodPlayInfo", params);
			} else {
				params.put("vid", vid);
				result = module.call("DescribeRecordPlayInfo", params);
			}
			
			// 处理JSON只返回视频url即可
			JSONObject ob = new JSONObject(result);
			JSONArray ja = (JSONArray) ob.get("fileSet");
			JSONObject ob0 = (JSONObject) ja.get(0);
			JSONArray playSet = (JSONArray) ob0.get("playSet");
			JSONObject play = (JSONObject) playSet.get(0);
			params = new TreeMap<String, Object>();
			params.put("image_url", ob0.get("image_url"));
			params.put("url", play.get("url"));
			rd.setData(params);
		} catch (Exception e) {
			rd.setErrorCode(560);
			rd.setErrorInfo(e.getMessage());
			log.error("getReplayDetail error", e);
		}
		return rd;
	}
}
