package cn.itcast.crm.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import cn.itcast.crm.domain.Customer;

//客户的dao
public interface CustomerRepository extends JpaRepository<Customer, Integer>{

	//查询定区编号为空的客户列表
	List<Customer> findByFixedAreaIdIsNull();
	//根据定区编号查询客户列表
	List<Customer> findByFixedAreaId(String fixedAreaId);
	
	//根据手机号码更新类型状态type
	@Query("update Customer set type =?2 where telephone =?1")
	@Modifying
	void updateTypeByTelephone(String telephone,Integer type);
	
	//根据地址查询定区编号
	@Query("select fixedAreaId from  Customer where address =?")
	String findFixedAreaIdByAddress(String address);
	
	//根据地址查询客户信息 
	List<Customer> findByAddress(String address);

	
	
}
