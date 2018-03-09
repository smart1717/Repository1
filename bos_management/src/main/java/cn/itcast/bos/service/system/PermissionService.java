package cn.itcast.bos.service.system;

import java.util.List;

import cn.itcast.bos.domain.system.Permission;

public interface PermissionService {

	/**
	 * 查询所有的功能权限
	 * 说明：
	 * @return
	 * @author 传智.BoBo老师
	 * @time：2017年12月17日 下午5:37:21
	 */
	List<Permission> findPermissionList();

	/**
	 * 
	 * 说明：保存数据
	 * @param permission
	 * @author 传智.BoBo老师
	 * @time：2017年12月17日 下午5:43:35
	 */
	void savePermission(Permission permission);

}
