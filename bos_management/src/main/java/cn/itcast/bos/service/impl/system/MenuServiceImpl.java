package cn.itcast.bos.service.impl.system;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.interceptor.SimpleKey;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.itcast.bos.dao.system.MenuRepository;
import cn.itcast.bos.domain.system.Menu;
import cn.itcast.bos.domain.system.User;
import cn.itcast.bos.service.system.MenuService;

@Service
@Transactional
public class MenuServiceImpl implements MenuService{

	//注入dao
	@Autowired
	private MenuRepository menuRepository;
	@Override
	public List<Menu> findMenuList() {
		return menuRepository.findAll();
	}
	@Override
	//增删改的时候，要清除缓存区域内的所有对象
	@CacheEvict(value="bos_menu_cache",allEntries=true)
	public void saveMenu(Menu menu) {
		//父节点关联对象的处理
		Menu parentMenu = menu.getParentMenu();
		if(parentMenu!=null &&parentMenu.getId()==null){
			menu.setParentMenu(null);
		}
		// 保存菜单
		menuRepository.save(menu);
	}
	@Override
	//查询的时候，缓存查询的结果
//	@Cacheable(value="bos_menu_cache")
	/*
	 * 缓存是map结构，value是list结果，key是谁?
	 * key的策略如下：
	 * 1）如果方法没有参数，则可以是一个固定值，(spring3=0,spring4:simlekey的类对象)，
	 * 2）如果只有一个参数，key是参数的对象的地址
	 * 3）如果有多个参数，key是所有参数对象计算出来的一个值（spring3=所有对象的hash值，spring4=基于哈希值计算后的结果）
	 * 这里使用的是第二种策略，key是用户的地址，
	 * 但如果同一个通用两次登录，那么用户对象的地址不同，不同的话就无法两次使用同一个缓存对象
	 * 解决方案：可以通过key属性，指定缓存的key,key支持SpEL表达式
	 */
	@Cacheable(value="bos_menu_cache",key="#user.id")//使用用户的id当key
	public List<Menu> findMenuListByUser(User user) {
		//业务逻辑：
		if("admin".equals(user.getUsername())){
			//超管：拥有所有菜单
//		menuRepository.findAll()
			return menuRepository.findByOrderByPriority();
		}else{
			//普通用户：必须根据不同用户展示不同菜单
//			return menuRepository.findByUserId(user.getId());//jpql
//			return menuRepository.findByUser(user);//sql
			List<Menu> list = menuRepository.findByUser(user);
			return list;
		}
		
	}

}
