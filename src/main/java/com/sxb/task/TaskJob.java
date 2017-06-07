package com.sxb.task;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.sxb.service.LiveRecordService;

/**
 * @description spring定时调度执行处理类，根据注解@Scheduled来定义需要定时调度的任务
 * @author <a href="mailto:ou8zz@sina.com">OLE</a>
 * @date 2016/05/29
 * @version 1.0
 */
@Component("taskJob")
public class TaskJob {
private Log log = LogFactory.getLog(TaskJob.class);
	
	@Resource(name="liveRecordService")
	private LiveRecordService liveRecordService;
	
	/**
	 * <ul>
	 * <li> 字段   允许值   允许的特殊字符</li>
	 * <li> 秒    0-59    , - * /</li>
	 * <li> 分    0-59    , - * /</li>
	 * <li> 小时    0-23    , - * /</li>
	 * <li> 日期    1-31    , - * ? / L W C</li>
	 * <li> 月份    1-12 或者 JAN-DEC    , - * /</li>
	 * <li> 星期    1-7 或者 SUN-SAT    , - * ? / L C #</li>
	 * <li> 年（可选）    留空, 1970-2099    , - * /</li> 
	 * <li> 减号(-) 区间  </li>
	 * <li> 星号(*)字符是通配字符,表示该字段可以接受任何可能的值、表达式例子。 </li>
	 * <li> 问号(?)字符和字母L字符只有在月内日期和周内日期字段中可用。问号表示这个字段不包含具体值。</li>
	 * <li> 斜线(/)字符表示增量值。例如，在秒字段中"5/15"代表从第5秒开始，每15秒一次。</li>
	 * <ul>
	 */
//	@Scheduled(cron="0/5 * * * * ?")   //每5秒执行一次
	@Scheduled(cron="0 0/1 * * * ?")
    public void job() {
        try {
			System.out.println("task runing..........");
			liveRecordService.deleteInactiveRecord(60);
		} catch (Exception e) {
			log.error("定时执行任务错误", e);
		}
    }
}