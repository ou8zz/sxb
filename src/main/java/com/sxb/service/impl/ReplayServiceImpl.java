package com.sxb.service.impl;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.sxb.model.Replay;
import com.sxb.service.ReplayService;

/**
 * @description 视频回放服务接口
 * @author <a href="mailto:ou8zz@sina.com">OLE</a>
 * @date 2016/06/14
 * @version 1.0
 */
@Repository(value="replayService")
@Scope(value="prototype")
@Transactional(propagation=Propagation.REQUIRED)
public class ReplayServiceImpl implements ReplayService {

	@Autowired
	private SqlSession sqlSession;
	
	/**
	 * 保存·直播视频addReplay
	 * @param Replay 视频对象
	 * @return 成功success或者抛出异常
	 */
	@Override
	public Object addReplay(Replay r) {
		int insert = sqlSession.insert("replay.addReplay", r);
		return insert;
	}
	
	/**
	 * 获取直播视频列表
	 * @param Replay 视频对象
	 * @return 返回视频直播信息对象
	 */
	@Override
	public Object getReplay(Replay r) {
		List<Object> selectList = sqlSession.selectList("replay.getReplay", r);
		return selectList;
	}
}
