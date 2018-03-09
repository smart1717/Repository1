package cn.itcast.bos.service.impl.take_delivery;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.itcast.bos.dao.take_delivery.PromotionRepository;
import cn.itcast.bos.domain.page.PageBean;
import cn.itcast.bos.domain.take_delivery.Promotion;
import cn.itcast.bos.service.take_delivery.PromotionService;
@Service("promotionService")
@Transactional
public class PromotionServiceImpl implements PromotionService {
	//注入dao
	@Autowired
	private PromotionRepository promotionRepository;

	@Override
	public void savePromotion(Promotion promotion) {
		promotionRepository.save(promotion);

	}

	@Override
	public Page<Promotion> findPromotionListPage(Pageable pageable) {
		return promotionRepository.findAll(pageable);
	}

	@Override
	public PageBean<Promotion> findPromotionListPage(int page, int rows) {
		Pageable pageable=new PageRequest(page-1, rows);
		Page<Promotion> pageResponse = promotionRepository.findAll(pageable);
		//重新组装数据
		PageBean<Promotion> pageBean=new PageBean<>();
		pageBean.setTotalCount(pageResponse.getTotalElements());//总记录数
		pageBean.setPageData(pageResponse.getContent());//当前页的数据列表
		return pageBean;
	}

	@Override
	public Promotion findPromotionById(Integer id) {
		return promotionRepository.findOne(id);
	}

	@Override
	public void updateStatus() {
		promotionRepository.updateStatus1("1");
		promotionRepository.updateStatus0("0");
		
	}

}
