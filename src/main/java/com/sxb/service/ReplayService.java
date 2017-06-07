package com.sxb.service;

import com.sxb.model.Replay;

/**
 * @description 视频回放服务接口
 * @author <a href="mailto:ou8zz@sina.com">OLE</a>
 * @date 2016/06/14
 * @version 1.0
 */
public interface ReplayService {
	
	/**
	 * 保存·直播视频addReplay
	 * @param Replay 视频对象
	 * @return 成功success或者抛出异常
	 */
	Object addReplay(Replay r);
	
	/**
	 * 获取直播视频列表
	 * @param Replay 视频对象
	 * @return 返回视频直播信息对象
	 */
	Object getReplay(Replay r);
}
