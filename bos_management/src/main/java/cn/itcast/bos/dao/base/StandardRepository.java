package cn.itcast.bos.dao.base;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import cn.itcast.bos.domain.base.Standard;

//取派标准的持久层接口
//必须继承JpaRepository，
//参数1：实体类型
//参数2：主键的实体类属性类型
public interface StandardRepository extends JpaRepository<Standard, Integer>{

	
	//======属性表达式
	//需求：根据name字段值进行精确匹配
	/*
	 * 1）方法名必须满足属性表达式的规则！
	 * 2）c参数必须和属性表达式的属性对应：个数、类型
	 * 3）返回类型：可以是实体类型，也可以是集合类型
	 * 如果返回最多一个，则可以使用实体类型；
	 * 否则，如果返回可能有多于一个，则必须使用集合list类型
	 * 默认接口是public，可以省略
	 */
	Standard findByName(String name);
	
	//需求：根据name字段模糊匹配
	List<Standard> findByNameLike(String name);
	
	
	//===========Query注解
	//需求：要根据id查询name的值
	//JPQL
	@Query("select name from Standard where id =?")//JPQL和hql差不多
	//SQL
//	@Query(value="select name from t_standard where id =?",nativeQuery=true)
	String findNameById(Integer id);
	
	
	//---------参数占位符
	//1)匿名参数占位符:顺序问题
	@Query("from Standard where id =? and name =?")
	Standard findByIdAndName1(Integer id,String name);
	//2)命名参数占位符:没有顺序问题
	@Query("from Standard where id =:id and name =:name")
	Standard findByIdAndName2(@Param("name")String name,@Param("id")Integer id);
	//2)JPA参数占位符:没有顺序问题,索引从1开始
	@Query("from Standard where id =?2 and name =?1")
	Standard findByIdAndName3(String name,Integer id);
	
	@Query("update Standard set name =?2 where id =?1")
	@Modifying
	void updateNameById(Integer id,String name);

	
}
