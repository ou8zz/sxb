package com.sxb.service.impl;

import java.util.Calendar;
import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sxb.model.LiveRecord;
import com.sxb.model.RespsonData;
import com.sxb.service.LiveRecordService;
import com.sxb.util.RegexUtil;

/**
 * @description 直播操作
 * @author <a href="mailto:ou8zz@sina.com">OLE</a>
 * @date 2016/06/08
 * @version 1.0
 */
@Repository(value="liveRecordService")
@Scope(value="prototype")
@Transactional(propagation=Propagation.REQUIRED)
public class LiveRecordServiceImpl implements LiveRecordService {
    
	@Autowired
	private SqlSession sqlSession;
	
	/**
	 * 创建一个直播，如果这个用户已经有一个直播了，则不用创建，直接返回即可
	 * @param lr
	 */
	@Override
    public Object save(LiveRecord lr) {
		int i = sqlSession.selectOne("live.getLiveRecordCount", lr);
		if(i==0) {
			i = sqlSession.insert("live.addLiveRecord", lr);
		}
    	return i;
    }

	/**
     * 直播结束
     * @return 直播信息
     */
	@Override
    public Object endLiveRecord(LiveRecord lr) {
		LiveRecord ob = sqlSession.selectOne("live.getLiveRecords", lr);
    	if(RegexUtil.isEmpty(ob)) {
    		return ob;
    	}
    	sqlSession.delete("live.delLiveRecord", lr);
    	
    	// 获取直播时长,毫秒转成秒
    	long i = System.currentTimeMillis()-ob.getCreate_time().getTime();
    	if(i>0) {
    		int span = (int) (i/1000);
        	ob.setTime_span(span);
    	}
    	return ob;
    }
    
    /**
     * 删除不活跃的记录
     * @param  int $inactiveSeconds 多久没更新
     * @return bool 成功：true; 失败：false.
     */
    @Override
    public void deleteInactiveRecord(Integer inactiveSeconds) {
    	Calendar cal = Calendar.getInstance();
    	cal.add(Calendar.SECOND, -inactiveSeconds);
    	LiveRecord lr = new LiveRecord();
    	lr.setModify_time(cal.getTime());
    	int live = sqlSession.selectOne("live.getLiveRecordCount", lr);
    	if(live > 0) {
    		sqlSession.delete("live.deleteInactiveRecord", lr.getModify_time());
    	}
    }

    /**
     * 根据主播Uid从数据库加载数据
     * @return 
     */
    @Override
    public Object loadByHostUid(LiveRecord lr) {
    	return sqlSession.selectOne("live.getLiveRecords", lr);
    }

    /**
     * 获取直播列表
     * @param  integer $offset = 0
     * @param  integer $limit = 50
     * @return array  LiveRecord对象数组,出错返回null
     */
    @Override
    public RespsonData getList(Integer offset, Integer limit) {
    	RespsonData rd = new RespsonData();
    	Page<Object> startPage = PageHelper.startPage(offset, limit);
    	List<LiveRecord> selectList = sqlSession.selectList("live.getLiveRecords");
    	rd.setData(selectList);
    	rd.setTotalItems(String.valueOf(startPage.getTotal()));
    	return rd;
    }
    
    /**
     * 根据主播Uid更新数据
     * @param  string $hostUid 主播Uid
     * @param  LiveDynamicData $data   直播动态数据
     * @return int  成功：更新记录数;出错：-1      
     */
    @Override
    public int updateByHostUid(LiveRecord data) {
//    	LiveRecord lr = sqlSession.selectOne("live.getLiveRecords", data);
    	int fak = RegexUtil.watchCount(data.getWatch_count());
    	data.setWatch_count(data.getWatch_count()+fak);
    	data.setReal_watch(fak);
    	int i = sqlSession.update("live.updateLiveRecord", data);
    	if(i==0) return i;
    	return data.getWatch_count();
    }
    
    /**
     * 创建 AvRoomId
     * @param  string $uid 
     * @return int      成功：true, 出错：false
     */
    @Override
    public boolean create(String uid) {
    	int i = sqlSession.insert("live.createAvRoom", uid);
    	return (i==1) ? true : false;
    }
    
    /**
     * 从数据库加载 AvRoomId如果没有
     * 创建 AvRoomId
     * @return int roomId
     */
    @Override
    public Integer load(String uid) {
    	Integer i = sqlSession.selectOne("live.getAvRoomId", uid);
    	if(i==null) {
    		// 创建 AvRoomId
    		i = sqlSession.insert("live.createAvRoom", uid);
    		i = sqlSession.selectOne("live.getAvRoomId", uid);
    	}
        return i;
    }
}


