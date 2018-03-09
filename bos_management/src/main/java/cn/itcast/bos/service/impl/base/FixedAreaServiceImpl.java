package cn.itcast.bos.service.impl.base;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.itcast.bos.dao.base.CourierRepository;
import cn.itcast.bos.dao.base.FixedAreaRepository;
import cn.itcast.bos.dao.base.TakeTimeRepository;
import cn.itcast.bos.domain.base.Courier;
import cn.itcast.bos.domain.base.FixedArea;
import cn.itcast.bos.domain.base.TakeTime;
import cn.itcast.bos.service.base.FixedAreaService;

@Service
@Transactional
public class FixedAreaServiceImpl implements FixedAreaService {
	//注入dao
	@Autowired
	private FixedAreaRepository fiexedAreaRepository;
	//注入快递员的dao
	@Autowired
	private CourierRepository courierRepository;
	//注入时间的dao
	@Autowired
	private TakeTimeRepository takeTimeRepository;

	@Override
	public void saveFixedArea(FixedArea fixedArea) {
		fiexedAreaRepository.save(fixedArea);

	}

	@Override
	public Page<FixedArea> findFixedAreaListPage(Specification<FixedArea> spec, Pageable pageable) {
		return fiexedAreaRepository.findAll(spec, pageable);
	}

	@Override
	public void associationCourierToFixedArea(FixedArea fixedArea, Integer courierId, Integer takeTimeId) {
		//涉及到多表操作，使用hibernate快照操作-持久态的操作
		//先查询，再修改
		FixedArea fa = fiexedAreaRepository.findOne(fixedArea.getId());
		Courier courier = courierRepository.findOne(courierId);
		TakeTime takeTime = takeTimeRepository.findOne(takeTimeId);
		
		//持久态的关联
		fa.getCouriers().add(courier);//多对多中间表的维护，插入数据
		courier.setTakeTime(takeTime);//多对一的外键维护
		
		//等flush
		
	}

	@Override
	@Cacheable("bos_SubAreaUpload")
	public FixedArea findById(String fixedArea) {
		return fiexedAreaRepository.findById(fixedArea);
		
	}

}
