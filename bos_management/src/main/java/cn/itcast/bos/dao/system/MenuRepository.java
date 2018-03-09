package cn.itcast.bos.dao.system;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import cn.itcast.bos.domain.system.Menu;
import cn.itcast.bos.domain.system.User;

public interface MenuRepository extends JpaRepository<Menu, Integer>{

	//超管：查询所有的菜单，且正序排序
	List<Menu> findByOrderByPriority();
	//根据用户查询菜单，且正序排序
	//JPQL语句-面向对象
	@Query("from Menu m inner join fetch m.roles r inner join fetch r.users u where u=? order by m.priority")//迫切连接查询
//	@Query("from Menu m inner join m.roles r inner join r.users u where u=? order by m.priority")//普通连接查询
	List<Menu> findByUser(User user);
	//SQL
	@Query(value="SELECT * FROM t_menu t1 INNER JOIN t_role_menu t2 ON t1.c_id=t2.c_menu_id INNER JOIN t_user_role t3 ON t2.c_role_id =t3.c_role_id WHERE t3.c_user_id=? ORDER BY t1.c_priority ASC",
			nativeQuery=true)
	List<Menu> findByUserId(Integer userId);
}
