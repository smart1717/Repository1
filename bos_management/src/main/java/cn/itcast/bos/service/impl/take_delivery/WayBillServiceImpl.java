package cn.itcast.bos.service.impl.take_delivery;

import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryStringQueryBuilder;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.index.query.WildcardQueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.itcast.bos.dao.take_delivery.WayBillRepository;
import cn.itcast.bos.domain.take_delivery.WayBill;
import cn.itcast.bos.indexdao.WayBillEsRepository;
import cn.itcast.bos.service.take_delivery.WayBillService;

@Service
@Transactional
public class WayBillServiceImpl implements WayBillService {
	//注入sdj的dao
	@Autowired
	private WayBillRepository wayBillRepository;
	
	//注入sde的dao
	@Autowired
	private WayBillEsRepository wayBillEsRepository;

	@Override
	public void saveWayBillQuick(WayBill wayBill) {
		//注意顺序问题
		//向数据库保存更新数据
		wayBillRepository.save(wayBill);
		//向es索引中保存更新数据
		wayBillEsRepository.save(wayBill);
	}

	@Override
	public Page<WayBill> findWayBillListPage(Pageable pageable) {
		return wayBillRepository.findAll(pageable);
	}

	@Override
	public Page<WayBill> findWayBillListPage(Pageable pageable, String fieldName, String fieldValue) {
		//基于SpringDataElasticSearch来搜索
		//查询条件对象
//		QueryBuilder query=null;
		//先正bool条件
		BoolQueryBuilder query=new BoolQueryBuilder();
		//1)词条精确匹配
		TermQueryBuilder query1=new TermQueryBuilder(fieldName, fieldValue);
		//2)通配符词条匹配
		WildcardQueryBuilder query2=new WildcardQueryBuilder(fieldName, "*"+fieldValue+"*");
		
		//3)全字段匹配-自动先分词
		QueryStringQueryBuilder query3=new QueryStringQueryBuilder(fieldValue);
		
		//组装条件
		query.should(query1);
		query.should(query2);
//		query.should(query3);
		
		return wayBillEsRepository.search(query, pageable);
	}

}
