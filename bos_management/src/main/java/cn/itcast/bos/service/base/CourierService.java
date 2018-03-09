package cn.itcast.bos.service.base;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import cn.itcast.bos.domain.base.Courier;

//快递员业务层接口
public interface CourierService {

	/**
	 * 
	 * 说明：保存快递员
	 * @param courier
	 * @author 传智.BoBo老师
	 * @time：2017年11月28日 下午5:05:39
	 */
	void saveCourier(Courier courier);
	/**
	 * 
	 * 说明：组合条件分页查询
	 * @param spec
	 * @param pageable
	 * @return
	 * @author 传智.BoBo老师
	 * @time：2017年11月28日 下午6:09:09
	 */
	Page<Courier> findCourierListPage(Specification<Courier> spec, Pageable pageable);
	/**
	 * 
	 * 说明：批量作废
	 * @param ids
	 * @author 传智.BoBo老师
	 * @time：2017年11月29日 下午3:17:02
	 */
	void deleteBatch(String ids);
	/**
	 * 
	 * 说明：查询没有作废的快递员
	 * @return
	 * @author 传智.BoBo老师
	 * @time：2017年12月7日 下午2:56:12
	 */
	List<Courier> findCourierListNoDeltag();
	
	/**
	 * 批量还原快递员
	 * @param ids
	 * @author ZSZ
	 * @time：2017年12月24日 下午3:38:38
	 */
	void resBatch(String ids);
	
}
