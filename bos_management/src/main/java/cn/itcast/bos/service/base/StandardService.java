package cn.itcast.bos.service.base;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import cn.itcast.bos.domain.base.Standard;

public interface StandardService {

	/**
	 * 
	 * 说明：保存收派标准
	 * @param standard
	 * @author 传智.BoBo老师
	 * @time：2017年11月28日 下午2:46:47
	 */
	void saveStandard(Standard standard);
	
	/**
	 * 
	 * 说明：分页列表查询
	 * @param pageable
	 * @return
	 * @author 传智.BoBo老师
	 * @time：2017年11月28日 下午3:11:35
	 */
	Page<Standard> findStandardListPage(Pageable pageable);

	List<Standard> findStandardList();

}
