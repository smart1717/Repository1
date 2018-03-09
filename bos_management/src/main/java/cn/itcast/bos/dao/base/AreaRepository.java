package cn.itcast.bos.dao.base;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import cn.itcast.bos.domain.base.Area;

public interface AreaRepository extends JpaRepository<Area, String>, JpaSpecificationExecutor<Area>{

	
	//根据省市区完全匹配查询一个区域
	Area findByProvinceAndCityAndDistrict(String province,String city,String district);

	Area findById(String area);

	Area findByPostcode(String area);
	
	//根据id删除
	void deleteById(String id);
	
}
