package cn.itcast.bos.web.action.base;

import java.util.Collection;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.ws.rs.core.MediaType;

import org.apache.commons.lang3.StringUtils;
import org.apache.cxf.jaxrs.client.WebClient;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Controller;

import com.opensymphony.xwork2.ActionContext;

import cn.itcast.bos.domain.base.FixedArea;
import cn.itcast.bos.service.base.FixedAreaService;
import cn.itcast.bos.web.action.common.BaseAction;
import cn.itcast.crm.domain.Customer;
import cn.itcast.utils.Constants;

@Controller
@Scope("prototype")
public class FixedAreaAction extends BaseAction<FixedArea>{
	//注入service
	@Autowired
	private FixedAreaService fixedAreaService;
	
	//添加
	@Action(value="fixedArea_add",results={
			@Result(type=REDIRECT,location="/pages/base/fixed_area.html")
	})
	public String add(){
		fixedAreaService.saveFixedArea(model);
		return SUCCESS;
	}
	
	//组合条件分页查询
	@Action("fixedArea_listPage")
	public String listPage(){
		//第一步：获取参数（略-父类、模型驱动）
		//第二步：封装参数
		//请求分页bean
		Pageable pageable=new PageRequest(page-1, rows);
		//业务条件bean
		Specification<FixedArea> spec=new Specification<FixedArea>() {
			
			@Override
			public Predicate toPredicate(Root<FixedArea> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				
				//创建条件对象
				Predicate conjunction = cb.conjunction();//天生就是and的逻辑的条件
				Predicate disjunction = cb.disjunction();//or的逻辑的条件
				
				//============单表
				//定区编码
				if(StringUtils.isNotBlank(model.getId())){
					conjunction.getExpressions().add(
					cb.equal(root.get("id").as(String.class), model.getId())
					);
				}
				//所属单位
				if(StringUtils.isNotBlank(model.getCompany())){
					conjunction.getExpressions().add(
					cb.like(root.get("company").as(String.class), "%"+model.getCompany()+"%")
					);
					
				}
				//==============多表(略)
				
				return conjunction;
			}
		};
		
		//第三步：调用业务层查询
		Page<FixedArea> pageResponse= fixedAreaService.findFixedAreaListPage(spec,pageable);
		//第四步：将分页的结果对象重新组装数据压入root栈顶
		pushPageDataToValuestackRoot(pageResponse);
		
		return JSON;
	}
	
	//未关联任何定区的客户列表
	@Action("fixedArea_listCustomerListNoFixedAreaId")
	public String listCustomerListNoFixedAreaId(){
		//Webservice调用
//		Collection<? extends Customer> collection = WebClient.create("http://localhost:9002/crm_management/services/customerservice/customers")
		Collection<? extends Customer> collection = WebClient.create(Constants.CRM_MANAGEMENT_URL+"/services/customerservice/customers")
		.path("/nofixedareaid")//uri拼接
		.accept(MediaType.APPLICATION_JSON)
		.getCollection(Customer.class);
		//将集合压入root栈顶
		ActionContext.getContext().getValueStack().push(collection);
		return JSON;
	}
	//关联指定定区的客户列表
	@Action("fixedArea_listCustomerListByFixedAreaId")
	public String listCustomerListByFixedAreaId(){
		//Webservice调用
//		Collection<? extends Customer> collection = WebClient.create("http://localhost:9002/crm_management/services/customerservice/customers")
		Collection<? extends Customer> collection = WebClient.create(Constants.CRM_MANAGEMENT_URL+"/services/customerservice/customers")
				.path("/fixedareaid")//uri拼接
				.path("/"+model.getId())//定区编号
				.accept(MediaType.APPLICATION_JSON)
				.getCollection(Customer.class);
		//将集合压入root栈顶
		ActionContext.getContext().getValueStack().push(collection);
		return JSON;
	}
	
	//属性驱动封装subAreaIds
	private String[] customerIds;
	public void setCustomerIds(String[] customerIds) {
		this.customerIds = customerIds;
	}

	@Action(value="fixedArea_associationCustomersToFixedArea",results={
			@Result(type=REDIRECT,location="/pages/base/fixed_area.html")
	})
	public String associationCustomersToFixedArea(){
		//将数组拼接为逗号分割的字符串
		String cIds = StringUtils.join(customerIds, ",");
		
		//Webservice调用
//		WebClient.create("http://localhost:9002/crm_management/services/customerservice/customers")
		WebClient.create(Constants.CRM_MANAGEMENT_URL+"/services/customerservice/customers")
		.path("/fixedareaid")//uri拼接
		.path("/"+model.getId())//定区编号
		.path("/"+cIds)
		.type(MediaType.APPLICATION_JSON)
		.put(null);
		
		return SUCCESS;
	}
	
	//属性驱动封装快递员和时间的id
	private Integer courierId;
	private Integer takeTimeId;
	public void setCourierId(Integer courierId) {
		this.courierId = courierId;
	}
	public void setTakeTimeId(Integer takeTimeId) {
		this.takeTimeId = takeTimeId;
	}

	//定区关联快递员
	@Action(value="fixedArea_associationCourierToFixedArea",results={
			@Result(type=REDIRECT,location="/pages/base/fixed_area.html")
	})
	public String associationCourierToFixedArea(){
		
		fixedAreaService.associationCourierToFixedArea(model,courierId,takeTimeId);
		
		return SUCCESS;
	}
}
