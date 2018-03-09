package cn.itcast.bos.quartz.job;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;

import cn.itcast.bos.service.system.UserService;

//用户job对象
public class UserJob implements Job{
	//注入service'
	@Autowired
	private UserService userService;

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		//目标：根据激活时间判断过期的用户，将状态改为0过期
		//最好调用业务层
		userService.updateStatusForOutDatetime();
		System.out.println("用户过期任务执行了一次。。。。");
	}

}
