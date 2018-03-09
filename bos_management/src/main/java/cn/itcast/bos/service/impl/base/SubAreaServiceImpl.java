package cn.itcast.bos.service.impl.base;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.itcast.bos.dao.base.SubAreaRepository;
import cn.itcast.bos.domain.base.SubArea;
import cn.itcast.bos.service.base.SubAreaService;

@Service
@Transactional
public class SubAreaServiceImpl implements SubAreaService {
	
	@Autowired
	private SubAreaRepository subAreaRepository;
	

	@Override
	public void saveSubArea(ArrayList<SubArea> list) {
		subAreaRepository.save(list);
		
	}


	@Override
	public Page<SubArea> findAreaListPage(PageRequest pageRequest) {
		return subAreaRepository.findAll(pageRequest);
		
	}

}
