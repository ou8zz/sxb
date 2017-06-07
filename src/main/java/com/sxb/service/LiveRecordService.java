package com.sxb.service;

import com.sxb.model.LiveRecord;
import com.sxb.model.RespsonData;

/**
 * @description 直播操作
 * @author <a href="mailto:ou8zz@sina.com">OLE</a>
 * @date 2016/06/08
 * @version 1.0
 */
public interface LiveRecordService {
    
	/**
	 * 创建一个直播，如果这个用户已经有一个直播了，则不用创建，直接返回即可
	 * @param lr
	 */
	Object save(LiveRecord lr);

    /**
     * 直播结束
     * @return 直播信息
     */
    Object endLiveRecord(LiveRecord lr);
    
    /**
     * 从数据库删除直播记录
     * @return bool 成功：true, 失败：false
     
    void delete(LiveRecord lr);*/

    /**
     * 删除不活跃的记录
     * @param  int $inactiveSeconds 多久没更新
     * @return bool 成功：true; 失败：false.
     */
    void deleteInactiveRecord(Integer inactiveSeconds);

    /**
     * 根据主播Uid从数据库加载数据
     * @return int 成功：1，不存在记录: 0, 出错：-1
     */
    Object loadByHostUid(LiveRecord lr);

    /**
     * 获取直播列表
     * @param  integer $offset = 0
     * @param  integer $limit = 50
     * @return array  LiveRecord对象数组,出错返回null
     */
    RespsonData getList(Integer offset, Integer limit);
    
    /**
     * 根据主播Uid更新数据
     * @param  string $hostUid 主播Uid
     * @param  LiveDynamicData $data   直播动态数据
     * @return int  成功：更新记录数;出错：-1      
     */
    int updateByHostUid(LiveRecord data);
    
    /**
     * 创建 AvRoomId
     * @param  string $uid 
     * @return int      成功：true, 出错：false
     */
    boolean create(String uid);
    
    /**
     * 从数据库加载 AvRoomId
     * @return int roomId
     */
    Integer load(String uid);
}


