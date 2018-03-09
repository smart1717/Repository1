package cn.itcast.bos.service.take_delivery;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import cn.itcast.bos.domain.take_delivery.WayBill;

public interface WayBillService {
	/**
	 * 
	 * 说明：快速保存一个运单
	 * 
	 * @param wayBill
	 * @author 传智.BoBo老师
	 * @time：2017年12月10日 下午5:14:11
	 */
	void saveWayBillQuick(WayBill wayBill);

	/**
	 * 
	 * 说明：分页列表查询运单
	 * @param pageable
	 * @return
	 * @author 传智.BoBo老师
	 * @time：2017年12月10日 下午5:48:58
	 */
	Page<WayBill> findWayBillListPage(Pageable pageable);

	/**
	 * 
	 * 说明：基于es搜索运单
	 * @param pageable
	 * @param fieldName
	 * @param fieldValue
	 * @return
	 * @author 传智.BoBo老师
	 * @time：2017年12月14日 下午5:56:26
	 */
	Page<WayBill> findWayBillListPage(Pageable pageable, String fieldName, String fieldValue);

}
