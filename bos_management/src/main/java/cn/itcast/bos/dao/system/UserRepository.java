package cn.itcast.bos.dao.system;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import cn.itcast.bos.domain.system.User;

//用户的dao接口
public interface UserRepository extends JpaRepository<User, Integer>{

	//根据用户名查询一个用户对象、
	User findByUsername(String username);

	//根据激活时间更新状态为某值
	@Query(value="UPDATE t_user SET status=? WHERE months_between(SYSDATE,activetime)>3 AND status='1'",nativeQuery=true)
	@Modifying
	void updateStatusByActivetime(String status);
}
