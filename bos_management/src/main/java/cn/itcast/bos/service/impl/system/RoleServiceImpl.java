package cn.itcast.bos.service.impl.system;

import java.util.List;

import javax.persistence.GeneratedValue;
import javax.persistence.ManyToMany;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.itcast.bos.dao.system.MenuRepository;
import cn.itcast.bos.dao.system.PermissionRepository;
import cn.itcast.bos.dao.system.RoleRepository;
import cn.itcast.bos.domain.system.Menu;
import cn.itcast.bos.domain.system.Permission;
import cn.itcast.bos.domain.system.Role;
import cn.itcast.bos.service.system.RoleService;

@Service
@Transactional
public class RoleServiceImpl implements RoleService {
	//注入角色dao
	@Autowired
	private RoleRepository roleRepository;
	//注入功能权限dao
	@Autowired
	private PermissionRepository permissionRepository;
	//注入菜单的dao
	@Autowired
	private MenuRepository menuRepository;
	@Override
	public List<Role> findRoleList() {
		return roleRepository.findAll();
	}
	@Override
	public void saveRole(Role role, Integer[] permissionIds, String menuIds) {
		//1）保存角色
		roleRepository.save(role);//持久态
		/*
		 * SpringDataJPA的save方法后的对象不一定是持久态
		 * 如果需要抢占主键，必须调用hibernate的话，则是持久态
		 * 如果无需抢占主键，则方法调用后不会立即通知hibernate，则不是持久态。
		 * 这里是需要抢占主键，@GeneratedValue-主键是由数据库序列生成，必须通过hibernate去抢占，hibernate立马被通知到了（会将该对象放入一级缓存）
		 * 如果role的主键是手动主键，则save方法后role不是持久态对象，但需要持久态对象怎么办？
		 * 可以执行flush方法
		 */
//		roleRepository.flush();//立即通知hibernate，发出sql
//		roleRepository.saveAndFlush(role);//二合一的api
		
		//2)角色和功能权限的关联（多对多的中间表的插入）-快照
		//hibernate的多对多使用持久态对象的关系来维护中间表的数据
		//查询功能权限（持久态）
		/*if(StringUtils.isNotBlank(permissionIds)){
			//struts2在封装同名参数的时候，如果使用字符串接收，自动将数组转换为字符串，中间用逗号空格分割
			String[] split = permissionIds.split(", ");
		}*/
		if(permissionIds!=null){
			for (Integer permissionId : permissionIds) {
				Permission permission = permissionRepository.findOne(permissionId);
				//关联
//				permission.getRoles().add(role);//权限关联到角色，不行，因为权限放弃了外键维护权@ManyToMany(mappedBy = "permissions")
				role.getPermissions().add(permission);//角色关联权限
				//等flush
			}
		}
		
		//3)角色和菜单的关联-快照
		if(StringUtils.isNotBlank(menuIds)){
			String[] menuIdArray = menuIds.split(",");
			for (String menuId : menuIdArray) {
				Menu menu = menuRepository.findOne(Integer.parseInt(menuId));
				//关联
				role.getMenus().add(menu);//角色关联菜单
				//等flush
			}
		}
		
		
	}

}
