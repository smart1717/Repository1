package cn.itcast.bos.service.base;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import cn.itcast.bos.domain.base.Area;

//区域业务层
public interface AreaService {

	/**
	 * 
	 * 说明：批量保存
	 * @param areaList
	 * @author 传智.BoBo老师
	 * @time：2017年11月29日 下午5:03:02
	 */
	void saveArea(List<Area> areaList);
	
	/**
	 * 带条件的分页查询
	 * @param specification
	 * @param pageable
	 * @return
	 * @author ZSZ
	 * @time：2017年12月24日 下午6:52:18
	 */
	Page<Area> findPageData(Specification<Area> specification, Pageable pageable);
	
	/**
	 * 查询所有区域
	 * @return
	 * @author ZSZ
	 * @time：2017年12月24日 下午7:33:49
	 */
	List<Area> findAreaList();
	
	/**
	 * 添加区域
	 * @param model
	 * @author ZSZ
	 * @time：2017年12月24日 下午8:14:14
	 */
	void save(Area area);
	
	/**
	 * 批量删除区域
	 * @param ids
	 * @author ZSZ
	 * @time：2017年12月24日 下午9:06:35
	 */
	void delBatch(String ids);
	
	Page<Area> findAreaListPage(PageRequest pageRequest);

	Area findById(String area);

	Area findByPostcode(String area);
	
}
