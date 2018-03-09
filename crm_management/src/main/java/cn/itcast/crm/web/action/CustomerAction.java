package cn.itcast.crm.web.action;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.opensymphony.xwork2.ActionContext;

import cn.itcast.bos.web.action.common.BaseAction;
import cn.itcast.crm.domain.Customer;
import cn.itcast.crm.domain.PageBean;
import cn.itcast.crm.service.CustomerService;

@Controller
@Scope("prototype")
public class CustomerAction extends BaseAction<Customer>{

	private static final long serialVersionUID = 1L;
	
	@Autowired
	private CustomerService customerService;


	@Action(value="customer_findCustomerPage")
	public String findListPage(){
		PageBean<Customer> pageResponse = customerService.findCustomerListPage(page, rows);
		ActionContext.getContext().getValueStack().push(pageResponse);
		return JSON;
	}
	
	@Action(value="customer")
	public String find(){
		System.out.println(1);
		PageBean<Customer> pageResponse = customerService.findCustomerListPage(page, rows);
		ActionContext.getContext().getValueStack().push(pageResponse);
		return JSON;
	}
	
}
