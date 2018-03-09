package cn.itcast.bos.web.action.take_delivery;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.convention.annotation.Action;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Controller;

import com.opensymphony.xwork2.ActionContext;

import cn.itcast.bos.domain.take_delivery.WayBill;
import cn.itcast.bos.service.take_delivery.WayBillService;
import cn.itcast.bos.web.action.common.BaseAction;

//运单action
@Controller
@Scope("prototype")
public class WayBillAction extends BaseAction<WayBill>{
	//注入service
	@Autowired
	private WayBillService wayBillService;
	
	//快速录入运单
	@Action("wayBill_addquick")
	public String addquick(){
		
		Map<String, Object> resultMap=new HashMap<>();
		try {
			//调用业务层保存
			wayBillService.saveWayBillQuick(model);
			//成功
			resultMap.put("result", true);
		} catch (Exception e) {
			e.printStackTrace();
			//失败
			resultMap.put("result", false);
		}
		//压入root栈顶(自动将map转成json写入响应)
		ActionContext.getContext().getValueStack().push(resultMap);
		
		return JSON;
	}
	
	//获取两个参数
	private String fieldName;
	private String fieldValue;
	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}
	public void setFieldValue(String fieldValue) {
		this.fieldValue = fieldValue;
	}

	//运单的分页列表查询
	@Action("waybill_listPage")
	public String listPage(){
		//先获取分页两个条件page，rows（父类）
		//封装条件
//		Pageable pageable=new PageRequest(page-1, rows);
		//排序分页对象
		//sort参数：第一个是排序方式，默认是正序，第二个是排序字段
		Pageable pageable=new PageRequest(page-1, rows, new Sort(Direction.DESC, "id"));
		//分页响应的结果对象
		Page<WayBill> pageResponse=null;
		//判断是否有条件
		if(StringUtils.isBlank(fieldValue)){
			//如果没有输入关键字条件，查询所有-走数据库
			//调用业务层查询		
			pageResponse= wayBillService.findWayBillListPage(pageable);
			
		}else{
			//有输入关键字条件，根据条件搜索--走es索引
			//调用业务层查询		
			pageResponse= wayBillService.findWayBillListPage(pageable,fieldName,fieldValue);
		}
		
		//将分页数据重新组装压入root栈顶
		pushPageDataToValuestackRoot(pageResponse);
		
		return JSON;
	}
}
