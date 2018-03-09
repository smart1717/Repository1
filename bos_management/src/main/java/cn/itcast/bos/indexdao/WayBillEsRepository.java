package cn.itcast.bos.indexdao;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import cn.itcast.bos.domain.take_delivery.WayBill;

//Spring data Elasticsearch 的dao
public interface WayBillEsRepository extends ElasticsearchRepository<WayBill, Integer>{

}
