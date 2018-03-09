package cn.itcast.bos.service.impl.base;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.itcast.bos.dao.base.CourierRepository;
import cn.itcast.bos.domain.base.Courier;
import cn.itcast.bos.service.base.CourierService;
//快递员业务层实现
@Service
@Transactional
public class CourierServiceImpl implements CourierService {
	//注入dao
	@Autowired
	private CourierRepository courierRepository;

	@Override
	public void saveCourier(Courier courier) {
		courierRepository.save(courier);
	}

	@Override
	public Page<Courier> findCourierListPage(Specification<Courier> spec, Pageable pageable) {
		return courierRepository.findAll(spec, pageable);
	}

	@Override
	public void deleteBatch(String ids) {
		//逻辑删除：更新操作
		if(StringUtils.isNotBlank(ids)){
			String[] idArray = ids.split(",");
			for (String id : idArray) {
				courierRepository.updateDeltagById(Integer.parseInt(id), '1');
			}
		}
		
	}

	@Override
	public List<Courier> findCourierListNoDeltag() {
		return courierRepository.findByDeltag('0');
	}
	
	/**批量还原快递员*/
	@Override
	public void resBatch(String ids) {
		//逻辑删除：更新操作
		if(StringUtils.isNotBlank(ids)){
			String[] idArray = ids.split(",");
			for (String id : idArray) {
				courierRepository.updateDeltagById(Integer.parseInt(id), '0');
			}
		}
	}
	
}
