package cn.itcast.bos.service.base;

import java.util.List;

import cn.itcast.bos.domain.base.TakeTime;

public interface TakeTimeService {

	/**
	 * 
	 * 说明：查询没有删除的时间列表
	 * @return
	 * @author 传智.BoBo老师
	 * @time：2017年12月7日 下午3:06:57
	 */
	List<TakeTime> findTakeTimeListNoDel();

}
