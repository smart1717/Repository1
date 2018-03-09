package cn.itcast.bos.service.base;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import cn.itcast.bos.domain.base.FixedArea;

public interface FixedAreaService {

	/**
	 * 
	 * 说明：添加一个定区
	 * @param fixedArea
	 * @author 传智.BoBo老师
	 * @time：2017年11月29日 下午6:05:21
	 */
	void saveFixedArea(FixedArea fixedArea);

	/**
	 * 
	 * 说明：组合条件分页查询
	 * @param spec
	 * @param pageable
	 * @return
	 * @author 传智.BoBo老师
	 * @time：2017年11月29日 下午6:25:09
	 */
	Page<FixedArea> findFixedAreaListPage(Specification<FixedArea> spec, Pageable pageable);

	/**
	 * 
	 * 说明：定区关联快递员
	 * @param fixedArea
	 * @param courierId
	 * @param takeTimeId
	 * @author 传智.BoBo老师
	 * @time：2017年12月7日 下午3:21:08
	 */
	void associationCourierToFixedArea(FixedArea fixedArea, Integer courierId, Integer takeTimeId);

	FixedArea findById(String fixedArea);

}
