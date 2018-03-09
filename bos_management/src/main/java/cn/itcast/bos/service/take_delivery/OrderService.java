package cn.itcast.bos.service.take_delivery;

import java.util.HashMap;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import cn.itcast.bos.domain.take_delivery.Order;

@Path("/orders")
@Consumes({MediaType.APPLICATION_XML,MediaType.APPLICATION_JSON})//消费者类型
@Produces({MediaType.APPLICATION_XML,MediaType.APPLICATION_JSON})//生产者类型
public interface OrderService {

	/**
	 * 
	 * 保存新订单
	 * 说明：
	 * @param order
	 * @author 传智.BoBo老师
	 * @time：2017年12月7日 下午5:07:33
	 */
	@POST//保存
	void saveOrder(Order order);

	List<Order> findOrder();

	List findOrderGroupBySendArea();

}
