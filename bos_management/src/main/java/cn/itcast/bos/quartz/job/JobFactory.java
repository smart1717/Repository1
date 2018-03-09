package cn.itcast.bos.quartz.job;

import org.quartz.spi.TriggerFiredBundle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.scheduling.quartz.AdaptableJobFactory;
import org.springframework.stereotype.Service;
//job工厂对象：该工厂用来生产job对象的
//为什么要自定义一个呢？因为默认情况下，该工厂生产的job是quartz来提供的，不是springnew出来的，spring只是调用
@Service("jobFactory")
public class JobFactory extends AdaptableJobFactory {

	@Autowired
	//将普通的java对象放入spring容器中变成bean
	private AutowireCapableBeanFactory capableBeanFactory;

	@Override
	protected Object createJobInstance(TriggerFiredBundle bundle) throws Exception {
		// 调用父类的方法：quartz基于配置job类型生产了一个job对象（没有在spring容器中）
		Object jobInstance = super.createJobInstance(bundle);
		// 将Quartz生产出来的job对象，注入到spring容器，该job对象就变成了bean
		capableBeanFactory.autowireBean(jobInstance);
		return jobInstance;
	}

}
