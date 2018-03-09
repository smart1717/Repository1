package cn.itcast.bos.web.action.system;

import java.util.List;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.opensymphony.xwork2.ActionContext;

import cn.itcast.bos.domain.system.Permission;
import cn.itcast.bos.service.system.PermissionService;
import cn.itcast.bos.web.action.common.BaseAction;

//功能权限的action
@Controller
@Scope("prototype")
public class PermissionAction extends BaseAction<Permission>{
	//注入service
	@Autowired
	private PermissionService permissionService;

	//列表查询
	@Action("permission_list")
	public String list(){
		List<Permission> list= permissionService.findPermissionList();
		//压入root栈顶
		ActionContext.getContext().getValueStack().push(list);
		return JSON;
	}
	
	//添加功能权限
	@Action(value="permission_add",results={
			@Result(type=REDIRECT,location="/pages/system/permission.html")
	})
	public String add(){
		permissionService.savePermission(model);
		
		return SUCCESS;
	}
}
