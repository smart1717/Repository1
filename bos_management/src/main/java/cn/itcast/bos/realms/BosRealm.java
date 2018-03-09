package cn.itcast.bos.realms;

import java.util.List;
import java.util.Set;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import cn.itcast.bos.dao.system.PermissionRepository;
import cn.itcast.bos.dao.system.RoleRepository;
import cn.itcast.bos.dao.system.UserRepository;
import cn.itcast.bos.domain.system.Permission;
import cn.itcast.bos.domain.system.Role;
import cn.itcast.bos.domain.system.User;

//realm安全数据
@Component("bosRealm")
public class BosRealm extends AuthorizingRealm{
	
	//注入认证的缓存区域的名字
	@Value("bos_realm_authentication_cache")
	//注入的方法名：随便写，但必须是setXxx,方法上写注入注解，根据方法的参数注入
	public void setSuperAuthenticationCacheName(String authenticationCacheName){
		//向父类中注入认证的缓存区域的名字
		super.setAuthenticationCacheName(authenticationCacheName);
	}
	
	//注入授权的缓存区域的名字
	@Value("bos_realm_authorization_cache")
	//注入的方法名：随便写，但必须是setXxx,方法上写注入注解，根据方法的参数注入
	public void setSuperAuthorizationCacheName(String authorizationCacheName){
		//向父类中注入认证的缓存区域的名字
		super.setAuthorizationCacheName(authorizationCacheName);
	}
	
	//注入用户dao
	@Autowired
	private UserRepository userRepository;
	//注入角色dao
	@Autowired
	private RoleRepository roleRepository;
	//注入功能权限dao
	@Autowired
	private PermissionRepository permissionRepository;

	//用来提供授权的安全数据
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		
		System.out.println("授权的安全数据获取中...");
		//==========1. 写死授权数据
		SimpleAuthorizationInfo authorizationInfo=new SimpleAuthorizationInfo();
		//1)功能权限:权限底层就是字符串的对比，-- perms[courier:list]
//		authorizationInfo.addStringPermission("courier:list");
		//2)角色权限--roles[base]
//		authorizationInfo.addRole("base");
		
		//==========2. 根据当前登录用户，到数据库中动态查询授权数据
		//获取当前登录的用户
		//方式1：工具类
//		User user = (User) SecurityUtils.getSubject().getPrincipal();
		//方法2：参数
		User user = (User)principals.getPrimaryPrincipal();
		//判断
		if("admin".equals(user.getUsername())){
			//超管：拥有所有的角色和功能权限
			//角色
			List<Role> roleList = roleRepository.findAll();
			for (Role role : roleList) {
				//添加角色字符串
				authorizationInfo.addRole(role.getKeyword());
			}
			//功能权限
			List<Permission> permissionList = permissionRepository.findAll();
			for (Permission permission : permissionList) {
				//添加功能权限字符串
				authorizationInfo.addStringPermission(permission.getKeyword());
			}
		}else{
			//普通用户：根据登录用户的名字和表关联关系，来查询角色和功能权限
			//角色
			List<Role> roleSet = roleRepository.findByUsers(user);
			for (Role role : roleSet) {
				//添加角色字符串
				authorizationInfo.addRole(role.getKeyword());
				//通过角色     导航查询    功能权限
				Set<Permission> permissionSet = role.getPermissions();
				for (Permission permission : permissionSet) {
					//添加功能权限字符串
					authorizationInfo.addStringPermission(permission.getKeyword());
				}
			}
			
		}
		
		return authorizationInfo;
	}

	//用来提供认证的安全数据（AuthenticationInfo）
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
		System.out.println("认证的安全数据获取中...");
		//目标：根据用户名查询用户，判断用户是否存在，如果存在，重新封装到AuthenticationInfo中，，交给（返回）安全管理器
		//1.获取用户输入的用户名
		String username = ((UsernamePasswordToken)token).getUsername();
		//2.根据用户名到数据库查询数据
		User user = userRepository.findByUsername(username);
		//3.判断用户是否存在
		if(null==user){
			//用户名根本不存在
			return null;//代表用户不存在
		}else{
			
			//如果用户名存在，则判断是否被锁定（过期）
			if("0".equals(user.getStatus())){
				//用户过期锁定
				throw new LockedAccountException("用户"+user.getUsername()+"被锁定了，请及时充值！");
			}
			
			
			//用户存在，要将用户信息包装为AuthenticationInfo交给安全管理器
			AuthenticationInfo authenticationInfo=new SimpleAuthenticationInfo
			/*
			 * 参数1：principal首长负责人--真实的用户对象本身user,将来安全管理器会将其放入“session”
			 * 参数2：credentials 凭证，证件--这里就是真实原来的密码，安全管理器自动和用户输入的令牌中的密码对比
			 * 参数3：realm对象唯一的名字,值就是类名+随机增长数-固定写法
			 */
					(user, user.getPassword(), super.getName());
			
			return authenticationInfo;
		}
	}

}
