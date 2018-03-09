package cn.itcast.bos.service.system;

import java.util.List;

import cn.itcast.bos.domain.system.Menu;
import cn.itcast.bos.domain.system.User;

//菜单业务层接口
public interface MenuService {

	/**
	 * 
	 * 说明：查询所有的菜单
	 * @return
	 * @author 传智.BoBo老师
	 * @time：2017年12月17日 下午4:39:41
	 */
	List<Menu> findMenuList();

	/**
	 * 
	 * 说明：保存菜单
	 * @param menu
	 * @author 传智.BoBo老师
	 * @time：2017年12月17日 下午5:01:23
	 */
	void saveMenu(Menu menu);

	/**
	 * 
	 * 说明：根据用户查询菜单列表
	 * @param user
	 * @return
	 * @author 传智.BoBo老师
	 * @time：2017年12月19日 下午5:21:09
	 */
	List<Menu> findMenuListByUser(User user);

}
