package cn.itcast.bos.dao.system;

import org.springframework.data.jpa.repository.JpaRepository;

import cn.itcast.bos.domain.system.Role;
import cn.itcast.bos.domain.system.User;

import java.util.Set;
import java.util.List;

//角色dao
public interface RoleRepository extends JpaRepository<Role, Integer>{

	//根据多个用户查询对应的多个角色--不用
	List<Role> findByUsers(Set users);
	//根据一个用户查询对应的多个角色
	List<Role> findByUsers(User user);
}
