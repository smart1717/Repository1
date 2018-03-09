package cn.itcast.bos.service.take_delivery;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import cn.itcast.bos.domain.page.PageBean;
import cn.itcast.bos.domain.take_delivery.Promotion;

//宣传service
public interface PromotionService {

	/**
	 * 
	 * 说明：保存广告
	 * @param promotion
	 * @author 传智.BoBo老师
	 * @time：2017年12月21日 下午6:26:13
	 */
	void savePromotion(Promotion promotion);

	/**
	 * 
	 * 说明：分页列表查询
	 * @param pageable
	 * @return
	 * @author 传智.BoBo老师
	 * @time：2017年12月23日 下午3:02:35
	 */
	Page<Promotion> findPromotionListPage(Pageable pageable);
	
	/**
	 * 
	 * 说明：根据当前页码和每页最大记录数返回分页结果封装对象
	 * @param page
	 * @param rows
	 * @return
	 * @author 传智.BoBo老师
	 * @time：2017年12月23日 下午5:09:56
	 */
	@Path("/promotions/listpage")//访问：/promotions/listpage?page=?&rows=?
	@GET//代表查询
	@Produces({MediaType.APPLICATION_XML,MediaType.APPLICATION_JSON})//无需消费者类型
	PageBean<Promotion> findPromotionListPage(
			@QueryParam("page")int page//查询参数
			,@QueryParam("rows")int rows);
	
	@Path("/promotions/{id}")
	@GET
	@Consumes({MediaType.APPLICATION_XML,MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_XML,MediaType.APPLICATION_JSON})
	Promotion findPromotionById(@PathParam("id")Integer id);

	void updateStatus();

}
