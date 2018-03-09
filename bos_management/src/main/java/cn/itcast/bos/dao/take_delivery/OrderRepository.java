package cn.itcast.bos.dao.take_delivery;

import java.util.HashMap;
import java.util.List;

import javax.print.attribute.HashPrintJobAttributeSet;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import cn.itcast.bos.domain.take_delivery.Order;

public interface OrderRepository extends JpaRepository<Order, Integer>{
	
	//@Query(value="SELECT o.C_SEND_AREA_ID,Count(o.C_ID) FROM BOSS.T_ORDER o GROUP BY o.C_SEND_AREA_ID ORDER BY o.C_SEND_AREA_ID",nativeQuery=true)
	@Query(value="SELECT BOSS.T_AREA.C_DISTRICT, Count(BOSS.T_ORDER.C_ID) FROM BOSS.T_ORDER INNER JOIN BOSS.T_AREA ON BOSS.T_ORDER.C_SEND_AREA_ID = BOSS.T_AREA.C_ID GROUP BY BOSS.T_AREA.C_DISTRICT ORDER BY BOSS.T_AREA.C_DISTRICT ASC",nativeQuery=true)
	List findOrderGroupBySendArea();

}
