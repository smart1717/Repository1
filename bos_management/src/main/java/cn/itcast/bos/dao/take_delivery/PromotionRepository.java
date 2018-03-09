package cn.itcast.bos.dao.take_delivery;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import cn.itcast.bos.domain.take_delivery.Promotion;

public interface PromotionRepository extends JpaRepository<Promotion, Integer>{

	@Query(value="UPDATE t_promotion SET c_satus=? WHERE c_end_date=SYSDATE AND status='0'",nativeQuery=true)
	void updateStatus1(String string);

	@Query(value="UPDATE t_promotion SET c_status=? WHERE c_start_date=SYSDATE AND status='-1'",nativeQuery=true)
	void updateStatus0(String string);


}
