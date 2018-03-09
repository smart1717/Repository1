package cn.itcast.bos.service.system;

import java.util.List;

import cn.itcast.bos.domain.system.Role;

public interface RoleService {

	/**
	 * 
	 * 说明：查询所有角色
	 * @return
	 * @author 传智.BoBo老师
	 * @time：2017年12月17日 下午5:50:08
	 */
	List<Role> findRoleList();

	/**
	 * 
	 * 说明：保存角色（并关联功能权限和菜单）
	 * @param role
	 * @param permissionIds
	 * @param menuIds
	 * @author 传智.BoBo老师
	 * @time：2017年12月19日 下午3:37:41
	 */
	void saveRole(Role role, Integer[] permissionIds, String menuIds);

}
