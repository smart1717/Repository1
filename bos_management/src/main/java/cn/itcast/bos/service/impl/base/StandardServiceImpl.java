package cn.itcast.bos.service.impl.base;

import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.itcast.bos.dao.base.StandardRepository;
import cn.itcast.bos.domain.base.Standard;
import cn.itcast.bos.service.base.StandardService;

@Service
@Transactional
public class StandardServiceImpl implements StandardService {
	//注入dao
	@Autowired
	private StandardRepository standardRepository;

	@Override
	@RequiresRoles("base1")//该方法要被调用，当前用户必须具备xxx（base）的角色权限
	public void saveStandard(Standard standard) {
		standardRepository.save(standard);
		
		//代码级别的权限控制的伪代码演示
		System.out.println("我是代码。。。");
		
		Subject subject = SecurityUtils.getSubject();
		
		//认证
		if(subject.isAuthenticated()){
			System.out.println("我是代码。。。必须认证后");
		}
		
		//授权
		//角色权限
		if(subject.hasRole("base")){
			System.out.println("我是代码。。。");
		}
		
		try {
			subject.checkRole("base");
			System.out.println("我是代码。。。");
		} catch (AuthorizationException e) {
			e.printStackTrace();
		}
		
		//功能权限
		if(subject.isPermitted("courier:add")){
			System.out.println("我是代码。。。");
		}
		try {
			subject.checkPermission("courier:add");;
			System.out.println("我是代码。。。");
		} catch (AuthorizationException e) {
			e.printStackTrace();
		}
		
	}

	@Override
	public Page<Standard> findStandardListPage(Pageable pageable) {
		return standardRepository.findAll(pageable);
	}

	@Override
	public List<Standard> findStandardList() {
		return standardRepository.findAll();
	}

}
