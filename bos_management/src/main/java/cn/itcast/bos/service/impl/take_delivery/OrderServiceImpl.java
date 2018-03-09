package cn.itcast.bos.service.impl.take_delivery;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import javax.ws.rs.core.MediaType;

import org.apache.commons.lang3.StringUtils;
import org.apache.cxf.jaxrs.client.WebClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.itcast.bos.dao.base.AreaRepository;
import cn.itcast.bos.dao.base.FixedAreaRepository;
import cn.itcast.bos.dao.take_delivery.OrderRepository;
import cn.itcast.bos.dao.take_delivery.WorkBillRepository;
import cn.itcast.bos.domain.base.Area;
import cn.itcast.bos.domain.base.Courier;
import cn.itcast.bos.domain.base.FixedArea;
import cn.itcast.bos.domain.base.SubArea;
import cn.itcast.bos.domain.take_delivery.Order;
import cn.itcast.bos.domain.take_delivery.WorkBill;
import cn.itcast.bos.service.take_delivery.OrderService;
import cn.itcast.crm.domain.Customer;

@Service("orderService")
@Transactional
public class OrderServiceImpl implements OrderService {
	//注入dao
	@Autowired
	private OrderRepository orderRepository;
	//注入区域dao
	@Autowired
	private AreaRepository areaRepository;
	
	//注入定区dao
	@Autowired
	private FixedAreaRepository fixedAreaRepository;
	
	//注入工单的dao
	@Autowired
	private WorkBillRepository workBillRepository;

	@Override
	public void saveOrder(Order order) {
		
		//为了测试，打印order
		if(1==1){
			System.out.println(order);
			//return;
		}
		
		
		//-----------------------------------------订单基本保存
		//处理一下区域，将其变成持久态
		//寄件人的
		Area sendAreaPersist = areaRepository.findByProvinceAndCityAndDistrict(
				order.getSendArea().getProvince()
				,order.getSendArea().getCity()
				,order.getSendArea().getDistrict()
				);
		
		//收件人的
		Area recAreaPersist = areaRepository.findByProvinceAndCityAndDistrict(
				order.getRecArea().getProvince()
				,order.getRecArea().getCity()
				,order.getRecArea().getDistrict()
				);
		order.setSendArea(sendAreaPersist);
		order.setRecArea(recAreaPersist);
		
		//订单号(根据公司的业务的规则去生成的号)
		order.setOrderNum(UUID.randomUUID().toString());
		//分单类型：默认是人工分单
		order.setOrderType("人工分单");
		//订单时间(默认当前时间)
		order.setOrderTime(new Date());
		//短信内容
		order.setSendMobileMsg("您的订单已经收到。。。");
		//订单状态
		order.setStatus("待取件");
		
		//调用dao保存
		//Servlet.service() for servlet cxf threw exception
		//java.lang.NullPointerException
		orderRepository.save(order);
		//-----------------------------------------自动分单\
		
		//获取寄件人的地址：区域+详细地址:北京市北京市朝阳区幸福小区
		String address=sendAreaPersist.getProvince()
				+sendAreaPersist.getCity()
				+sendAreaPersist.getDistrict()
				+order.getSendAddress();
		
		
		//+++++++1.客户下单地址完全匹配规则实现自动分单
		
		//调用Webservice接口crm的
		Customer customer=null;
		try {
			customer = WebClient.create("http://localhost:9002/crm_management/services/customerservice/customers")
			.path("/fixedareaid/address")
			.path("/"+address)
			.type(MediaType.APPLICATION_JSON)
			.accept(MediaType.APPLICATION_JSON)
			.get(Customer.class);
		} catch (Exception e) {
			e.printStackTrace();
			//使用log4j的日志记录异常
			System.out.println("bos调用crm接口根据地址查询定区编号异常！");
		}
		
		//判断
		if(null!=customer){
			//获取定区id
			String fixedAreaId = customer.getFixedAreaId();
			//判断定区id是否存在
			if(StringUtils.isNotBlank(fixedAreaId)){
				//存在,根据id查询定区
				FixedArea fixedArea = fixedAreaRepository.findOne(fixedAreaId);
				//判断是否找到定区
				if(null!=fixedArea){
					//找到定区，找到快递员集合
					Set<Courier> courierSet = fixedArea.getCouriers();
					/*
					 * 业务扩展描述：
					 * 找到快递员集合后，根据更多的纬度来寻找最终的那个快递员。
					 * 比如可以根据收派时间（上下班时间）、收派标准（运送能力）、替班
					 */
					//简化业务，让一个定区中就有一个快递员。
					if(null!=courierSet&&!courierSet.isEmpty()){
						//找到快递员了！！
						Courier courier = courierSet.iterator().next();
						
						//订单状态
						order.setOrderType("自动分单");
						//订单关联快递员
						order.setCourier(courier);
						
						//下工单-给快递员看的
						saveWorkBill(order, courier);
						
						//某个规则生效后，不再继续派单
						return;
					}
				}
				
			}
		}
		
		//+++++++2.客户下单地址中的关键字匹配规则
		//获取客户下单的区域：要根据省市区定位一个区域(ok)
		//sendAreaPersist
		if(null!=sendAreaPersist){
			//找到区域后，获取区域下面的所有分区:一对多关系
			Set<SubArea> subareaSet = sendAreaPersist.getSubareas();
			//目标：要通过分区中存放关键字到客户地址中寻找匹配到 客户下单的分区。
			for (SubArea subArea : subareaSet) {
				//比如：某分区的关键字是：幸福大厦,客户地址：幸福大厦204室，这样就可以匹配上。
				if(order.getSendAddress().contains(subArea.getKeyWords())){
					//代表：匹配上了，找到了客户下单的小分区
					//通过分区找关联的定区
					FixedArea fixedArea = subArea.getFixedArea();
					//判断是否找到定区
					if(null!=fixedArea){
						//找到定区，找到快递员集合
						Set<Courier> courierSet = fixedArea.getCouriers();
						/*
						 * 业务扩展描述：
						 * 找到快递员集合后，根据更多的纬度来寻找最终的那个快递员。
						 * 比如可以根据收派时间（上下班时间）、收派标准（运送能力）、替班
						 */
						//简化业务，让一个定区中就有一个快递员。
						if(null!=courierSet&&!courierSet.isEmpty()){
							//找到快递员了！！
							Courier courier = courierSet.iterator().next();
							
							//订单状态
							order.setOrderType("自动分单");
							//订单关联快递员
							order.setCourier(courier);
							//下工单-给快递员看的
							saveWorkBill(order, courier);
							
							//某个规则生效后，不再继续派单
							return;
						}
					}
					break;//停止
				}
			}
			
		}
		
		
	}

	//保存工单：下工单-给快递员看的
	private void saveWorkBill(Order order, Courier courier) {
		WorkBill workBill=new WorkBill();
//						workBill.setId(id);//oid
		workBill.setType("新");//工单类型，默认是新单，如果被追单了就是追，如果要销单了，就是销
		workBill.setAttachbilltimes(0);//追单次数，没追一次+1
		workBill.setPickstate("新单");// 取件状态，该状态根据快递员的取件情况进行变化
		workBill.setBuildtime(new Date());//工单生成时间
		workBill.setOrder(order);//工单关联订单
		workBill.setCourier(courier);//工单关联快递员
		workBill.setRemark(order.getRemark());//备注：给快递员捎话，表的冗余设计。
		workBill.setSmsNumber("1111");//给快递员下发的短信的序号
		
		workBillRepository.save(workBill);
	}

	@Override
	public List<Order> findOrder() {
		return orderRepository.findAll();
		
	}

	@Override
	public List findOrderGroupBySendArea() {
		return orderRepository.findOrderGroupBySendArea();
		
	}
}
