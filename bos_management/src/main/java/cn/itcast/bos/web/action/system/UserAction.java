package cn.itcast.bos.web.action.system;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.opensymphony.xwork2.ActionContext;

import cn.itcast.bos.domain.system.User;
import cn.itcast.bos.service.system.UserService;
import cn.itcast.bos.web.action.common.BaseAction;
//用户的action
@Controller
@Scope("prototype")
public class UserAction extends BaseAction<User>{
	
	/*//登录
	@Action(value="user_login",results={
			@Result(type=REDIRECT,location="/index.html")//主页
			,@Result(name=INPUT,type=REDIRECT,location="/login.html")//登录页面
	})
	public String login(){
		//使用shiro进行认证操作
		//获取subject对象、
		Subject subject = SecurityUtils.getSubject();
		
		//创建令牌对象,参数就是用户输入用户名和密码
		AuthenticationToken token=new UsernamePasswordToken
				(model.getUsername(), model.getPassword());
		//认证登录操作
		//参数：令牌：存放用户名和密码的一个对象
		//异常判断
		try {
			subject.login(token);
			//认证登录成功
			return SUCCESS;
		
		} catch (UnknownAccountException e) {
			e.printStackTrace();
			//认证登录失败:用户名帐号不存在
			return INPUT;
		}catch (LockedAccountException e) {
			e.printStackTrace();
			//认证登录失败:帐号被锁定
			return INPUT;
		} catch (IncorrectCredentialsException e) {
			e.printStackTrace();
			//认证登录失败:密码不正确
			return INPUT;
		} catch (AuthenticationException e) {
			e.printStackTrace();
			//认证登录失败
			return INPUT;
		}
		
	}*/
	//登录
	@Action("user_login")
	public String login(){
		//使用shiro进行认证操作
		//获取subject对象、
		Subject subject = SecurityUtils.getSubject();
		
		//创建令牌对象,参数就是用户输入用户名和密码
		AuthenticationToken token=new UsernamePasswordToken
				(model.getUsername(), model.getPassword());
		
		
		
		//结果map
		Map<String, Object> resultMap=new HashMap<>();
		
		
		try {
			//认证登录操作
			//参数：令牌：存放用户名和密码的一个对象
			subject.login(token);
			//认证登录成功
			resultMap.put("result", true);
		} catch (UnknownAccountException e) {
			e.printStackTrace();
			//认证登录失败:用户名帐号不存在
			resultMap.put("result", false);
			resultMap.put("message", "用户名帐号不存在");
		}catch (LockedAccountException e) {
			e.printStackTrace();
			//认证登录失败:帐号被锁定
			resultMap.put("result", false);
			resultMap.put("message", "帐号被锁定,请联系管理员！");
		} catch (IncorrectCredentialsException e) {
			e.printStackTrace();
			//认证登录失败:密码不正确
			resultMap.put("result", false);
			resultMap.put("message", "密码不正确");
		} catch (AuthenticationException e) {
			e.printStackTrace();
			//认证登录失败
			resultMap.put("result", false);
			resultMap.put("message", "登录失败！");
		}
		
		//将map压入root栈顶
		ActionContext.getContext().getValueStack().push(resultMap);
		
		return JSON;
		
	}
	
	//用户注销-退出
	@Action(value="user_logout",results={
			@Result(type=REDIRECT,location="/login.html")
	})
	public String logout(){
		//清空“session”，释放资源
		/*
		 * Shiro的session，不一定是httpsession，shiro可以用在任意的框架环境下。
		 * 如果不是web应用，则session不是httpSEssion，底层是个map
		 * 如果是个web应用，则Session可以等同于httpSession（shiro的session封装了httpsession）
		 */
		SecurityUtils.getSubject().logout();
		return SUCCESS;
	}
	
	//注入业务层
	@Autowired
	private UserService userService;
	
	//s属性驱动获取角色
	private Integer[] roleIds;
	public void setRoleIds(Integer[] roleIds) {
		this.roleIds = roleIds;
	}

	//添加用户
	@Action(value="user_add",results={
			@Result(type=REDIRECT,location="/pages/system/userlist.html")
	})
	public String add(){
		userService.saveUser(model,roleIds);
		return SUCCESS;
	}
	
	//用户列表查询
	@Action("user_list")
	public String list(){
		List<User> list= userService.findUserList();
		ActionContext.getContext().getValueStack().push(list);
		return JSON;
	}
}
