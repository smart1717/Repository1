package cn.itcast.bos.web.action.base;

import javax.ws.rs.core.MediaType;

import org.apache.cxf.jaxrs.client.WebClient;
import org.apache.struts2.convention.annotation.Action;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.opensymphony.xwork2.ActionContext;

import cn.itcast.bos.domain.page.PageBean;
import cn.itcast.bos.web.action.common.BaseAction;
import cn.itcast.crm.domain.Customer;
import cn.itcast.utils.Constants;

@Controller
@Scope("prototype")
public class CustomerAction extends BaseAction<Customer>{
	private static final long serialVersionUID = 1L;
	
	@Action(value="customer_listPage")
	public String listPage(){
		PageBean pageBean = WebClient.create(Constants.CRM_MANAGEMENT_URL+"services//customer/listpage?page="+page+"&rows="+rows)
		.accept(MediaType.APPLICATION_JSON)
		.get(PageBean.class);
		ActionContext.getContext().getValueStack().push(pageBean);
		return JSON;
	}

}
