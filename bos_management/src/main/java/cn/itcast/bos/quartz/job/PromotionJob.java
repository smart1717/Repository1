package cn.itcast.bos.quartz.job;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;

import cn.itcast.bos.service.take_delivery.PromotionService;

public class PromotionJob implements Job{

	@Autowired
	private PromotionService promotionService;
	
	//作废过期宣传活动
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		promotionService.updateStatus();
		System.out.println("执行一次作废(开始)宣传任务任务");
		
	}

}
