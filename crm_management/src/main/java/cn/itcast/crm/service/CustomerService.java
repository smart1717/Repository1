package cn.itcast.crm.service;
//SEI接口

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import cn.itcast.crm.domain.Customer;
import cn.itcast.crm.domain.PageBean;

@Path("/customers")
@Consumes({MediaType.APPLICATION_XML,MediaType.APPLICATION_JSON})//消费者类型
@Produces({MediaType.APPLICATION_XML,MediaType.APPLICATION_JSON})//生产者类型
public interface CustomerService {
	
	/**
	 * 
	 * 说明：查询没有关联定区的客户列表
	 * @return
	 * @author 传智.BoBo老师
	 * @time：2017年12月2日 下午4:00:55
	 */
	@Path("/nofixedareaid")
	@GET
	public List<Customer> findCustomerListNoFixedAreaId();
	
	/**
	 * 
	 * 说明：根据定区编号查询客户列表
	 * @param fixedAreaId
	 * @return
	 * @author 传智.BoBo老师
	 * @time：2017年12月2日 下午4:01:26
	 */
	@Path("/fixedareaid/{fixedAreaId}")
	@GET
	public List<Customer> findCustomerListByFixedAreaId(@PathParam("fixedAreaId")String fixedAreaId);
	
	/**
	 * 
	 * 说明：根据多个客户id，更新定区编号
	 * @param fixedAreaId
	 * @param customerIds  多个客户使用英文逗号分割，如1,2
	 * @author 传智.BoBo老师
	 * @time：2017年12月2日 下午4:02:32
	 */
	@Path("/fixedareaid/{fixedAreaId}/{customerIds}")
	@PUT
	public void updateFixedAreaIdByCustomerIds(@PathParam("fixedAreaId")String fixedAreaId
			,@PathParam("customerIds")String customerIds);
	
	/**
	 * 
	 * 说明：保存一个客户
	 * @param customer
	 * @author 传智.BoBo老师
	 * @time：2017年12月5日 下午3:12:29
	 */
	@POST//保存
	public void saveCustomer(Customer customer);
	
	/**
	 * 
	 * 说明：根据手机号码更新状态的值
	 * @param telephone
	 * @param type
	 * @author 传智.BoBo老师
	 * @time：2017年12月5日 下午5:59:15
	 */
	@Path("/type/{telephone}/{type}")
	@PUT
	public void updateTypeByTelephone(@PathParam("telephone")String telephone,@PathParam("type")Integer type);
	
	/**
	 * 
	 * 说明：根据地址完全匹配查询定区编号
	 * @param address
	 * @return
	 * @author 传智.BoBo老师
	 * @time：2017年12月7日 下午5:59:22
	 */
	@Path("/fixedareaid/address/{address}")
	@GET
	public Customer findFixedAreaIdByAddress(@PathParam("address")String address);
	
	PageBean<Customer> findCustomerListPage(int page, int rows);
}
