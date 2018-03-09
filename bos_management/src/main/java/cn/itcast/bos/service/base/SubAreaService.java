package cn.itcast.bos.service.base;

import java.util.ArrayList;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import cn.itcast.bos.domain.base.SubArea;

public interface SubAreaService {

	void saveSubArea(ArrayList<SubArea> list);

	Page<SubArea> findAreaListPage(PageRequest pageRequest);
	
}
