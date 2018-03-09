package cn.itcast.bos.dao.system;

import org.springframework.data.jpa.repository.JpaRepository;

import cn.itcast.bos.domain.system.Permission;
//功能权限dao
public interface PermissionRepository extends JpaRepository<Permission, Integer>{

}
