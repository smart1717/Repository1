package cn.itcast.bos.service.system;

import java.util.List;

import cn.itcast.bos.domain.system.User;

public interface UserService {

	/**
	 * 
	 * 说明：保存用户并关联角色
	 * @param user
	 * @param roleIds
	 * @author 传智.BoBo老师
	 * @time：2017年12月19日 下午5:01:31
	 */
	void saveUser(User user, Integer[] roleIds);

	/**
	 * 
	 * 说明：查询所有用户列表
	 * @return
	 * @author 传智.BoBo老师
	 * @time：2017年12月19日 下午5:08:37
	 */
	List<User> findUserList();

	/**
	 * 
	 * 说明：为过期的用户更新状态
	 * @author 传智.BoBo老师
	 * @time：2017年12月21日 下午2:57:17
	 */
	void updateStatusForOutDatetime();

}
