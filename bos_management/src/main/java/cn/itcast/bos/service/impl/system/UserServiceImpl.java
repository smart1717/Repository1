package cn.itcast.bos.service.impl.system;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.itcast.bos.dao.system.RoleRepository;
import cn.itcast.bos.dao.system.UserRepository;
import cn.itcast.bos.domain.system.Role;
import cn.itcast.bos.domain.system.User;
import cn.itcast.bos.service.system.UserService;

@Service
@Transactional
public class UserServiceImpl implements UserService {
	//注入dao
	@Autowired
	private UserRepository userRepository;
	//注入角色dao
	@Autowired
	private RoleRepository roleRepository;

	@Override
	public void saveUser(User user, Integer[] roleIds) {
		//1.保存用户
		userRepository.save(user);//持久态
		//2.通过持久态对象之间关系关联，在关系表中添加用户和角色的关系
		if(roleIds!=null){
			for (Integer roleId : roleIds) {
				Role role = roleRepository.findOne(roleId);
				//关联：用户关联角色
				user.getRoles().add(role);
				//等flush
			}
		}
	}

	@Override
	public List<User> findUserList() {
		return userRepository.findAll();
	}

	@Override
	public void updateStatusForOutDatetime() {
		//用户激活超过三个月则过期，禁用
		userRepository.updateStatusByActivetime("0");
	}

}
