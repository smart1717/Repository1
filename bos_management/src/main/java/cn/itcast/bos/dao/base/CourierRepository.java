package cn.itcast.bos.dao.base;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import cn.itcast.bos.domain.base.Courier;
//组合条件分页查询：JpaRepository负责分页，JpaSpecificationExecutor扩展了业务条件
public interface CourierRepository extends JpaRepository<Courier, Integer>
,JpaSpecificationExecutor<Courier>{
	
	//根据id更新作废标记
	@Query("update Courier set deltag =?2 where id =?1")
	@Modifying
	void updateDeltagById(Integer id,Character deltag);
	
	//根据删除标记查询快递员
	List<Courier> findByDeltag(Character deltag);

}
