package cn.itcast.bos.service.impl.system;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.itcast.bos.dao.system.PermissionRepository;
import cn.itcast.bos.domain.system.Permission;
import cn.itcast.bos.service.system.PermissionService;

@Service
@Transactional
public class PermissionServiceImpl implements PermissionService {
	//注入dao
	@Autowired
	private PermissionRepository permissionRepository;

	@Override
	public List<Permission> findPermissionList() {
		return permissionRepository.findAll();
	}

	@Override
	public void savePermission(Permission permission) {
		permissionRepository.save(permission);
		
	}

}
