package cn.itcast.bos.service.impl.base;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.itcast.bos.dao.base.AreaRepository;
import cn.itcast.bos.domain.base.Area;
import cn.itcast.bos.service.base.AreaService;

@Service
@Transactional
public class AreaServiceImpl implements AreaService {
	//注入dao
	@Autowired
	private AreaRepository areaRepository;

	@Override
	public void saveArea(List<Area> areaList) {
		areaRepository.save(areaList);
	}

	@Override
	public Page<Area> findPageData(Specification<Area> specification, Pageable pageable) {
		
		return areaRepository.findAll(specification, pageable);
	}

	@Override
	public List<Area> findAreaList() {
		
		return areaRepository.findAll();
	}

	@Override
	public void save(Area area) {
		
		areaRepository.save(area);
	}

	@Override
	public void delBatch(String ids) {
		//删除：
		if(StringUtils.isNotBlank(ids)){
			String[] idArray = ids.split(",");
			for (String id : idArray) {
				areaRepository.deleteById(id);
			}
		}
		
	}
	
	@Override
	public Page<Area> findAreaListPage(PageRequest pageRequest) {
		return areaRepository.findAll(pageRequest);
	}

	@Override
	public Area findById(String area) {
		return areaRepository.findById(area);
		
	}

	@Override
	@Cacheable("bos_SubAreaUpload")
	public Area findByPostcode(String area) {
		
		return areaRepository.findByPostcode(area);
	}
	
}
