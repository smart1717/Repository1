package cn.itcast.bos.web.action.system;

import java.util.List;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.opensymphony.xwork2.ActionContext;

import cn.itcast.bos.domain.system.Role;
import cn.itcast.bos.service.system.RoleService;
import cn.itcast.bos.web.action.common.BaseAction;
@Controller
@Scope("prototype")
public class RoleAction extends BaseAction<Role> {
	//Z注入service
	@Autowired
	private RoleService roleService;
	
	//列表查询
	@Action("role_list")
	public String list(){
		List<Role> list=  roleService.findRoleList();
		//压入root栈顶
		ActionContext.getContext().getValueStack().push(list);
		return JSON;
	}
	
	//属性驱动封装功能权限和菜单的id
	//struts2在封装同名参数的时候，如果使用字符串接收，自动将数组转换为字符串，中间用逗号空格分割
//	private String permissionIds;
//	private String[] permissionIds;
	private Integer[] permissionIds;
	private String menuIds;

	public void setPermissionIds(Integer[] permissionIds) {
		this.permissionIds = permissionIds;
	}

	public void setMenuIds(String menuIds) {
		this.menuIds = menuIds;
	}

	//添加角色
	@Action(value="role_add",results={
			@Result(type=REDIRECT,location="/pages/system/role.html")
	})
	public String add(){
		roleService.saveRole(model,permissionIds,menuIds);
		return SUCCESS;
	}
}
