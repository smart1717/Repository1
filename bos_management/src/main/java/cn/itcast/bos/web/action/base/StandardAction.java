package cn.itcast.bos.web.action.base;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

import cn.itcast.bos.domain.base.Standard;
import cn.itcast.bos.service.base.StandardService;
import cn.itcast.bos.web.action.common.BaseAction;

//收派标准action
@Controller
@Scope("prototype")
//@ParentPackage("struts-default")
//@ParentPackage("json-default")//继承json插件的包
//@Namespace("/")
//public class StandardAction extends  ActionSupport implements ModelDriven<Standard>{
public class StandardAction extends BaseAction<Standard>{
	//声明数据模型对象
//	private Standard standard=new Standard();
//	@Override
//	public Standard getModel() {
//		return standard;
//	}
	
	//注入service
	@Autowired
	private StandardService standardService;
	
	@Action(value="standard_add",results={
//			@Result(name="success",type="redirect",location="/pages/base/standard.html")
//			@Result(name=SUCCESS,type=REDIRECT,location="/pages/base/standard.html")
			@Result(type=REDIRECT,location="/pages/base/standard.html")
	})
	public String save(){
		//调用业务层
		standardService.saveStandard(model);
		return SUCCESS;
	}
	
	
	//获取参数
	private int page;
	private int rows;
	public void setPage(int page) {
		this.page = page;
	}
	public void setRows(int rows) {
		this.rows = rows;
	}

/*	@Action(value="standard_listPage",results={
//			@Result(name="success",type="json")
			//@Result(name=SUCCESS,type=JSON)
//			@Result(name=JSON,type=JSON)
	})*/
	@Action("standard_listPage")
	public String listPage(){
		//三步
		//第一步：获取参数、封装(属性驱动)
		
		//请求的分页bean对象：封装分页的两个参数
		//参数1：当前页码的索引，从0开始 Pages are zero indexed
		//参数2：每页最大的记录数
		Pageable pageable=new PageRequest(page-1, rows);
		//第二步：调用业务层查询分页数据
		//响应的分页bean对象
		Page<Standard> pageResponse = standardService.findStandardListPage(pageable);
		
		//第三步：重新组装需要的格式的数据
		Map<String, Object> resultMap=new HashMap<>();
		//总记录数
		resultMap.put("total", pageResponse.getTotalElements());
		//当前页的数据列表
		resultMap.put("rows", pageResponse.getContent());
		
		//第四步：将结果对象压入root栈顶	
		ActionContext.getContext().getValueStack().push(resultMap);
		
		return JSON;
	}
	
	//列出所有的收派标准
	@Action("standard_list")
	public String list(){
		//调用业务层查询所有
		List<Standard> standardList= standardService.findStandardList();
		//将数据压入root栈顶
		ActionContext.getContext().getValueStack().push(standardList);
		
		return JSON;
	}

}
