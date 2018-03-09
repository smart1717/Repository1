package cn.itcast.bos.web.action.base;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;
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

import cn.itcast.bos.domain.base.Courier;
import cn.itcast.bos.domain.base.Standard;
import cn.itcast.bos.service.base.CourierService;
import cn.itcast.bos.web.action.common.BaseAction;
//快递员的action
@Controller
@Scope("prototype")
public class CourierAction extends BaseAction<Courier>{
	//注入service
	@Autowired
	private CourierService courierService;

	//添加一个快递员
	@Action(value="courier_add",results={
			@Result(type=REDIRECT,location="/pages/base/courier.html")
	})
	public String add(){
		courierService.saveCourier(model);
		return SUCCESS;
	}
	
	
	//获取参数
//	private int page;
//	private int rows;
//	public void setPage(int page) {
//		this.page = page;
//	}
//	public void setRows(int rows) {
//		this.rows = rows;
//	}
	
	//组合条件分页查询
	@Action("courier_listPage")
	public String listPage(){
		//第一步：获取参数（分页-属性驱动+业务-模型驱动）略
		//第二步：封装参数
		//请求的分页条件对象
		Pageable pageable=new PageRequest(page-1, rows);
		//请求的业务条件对象:封装jpa的Critieral
		Specification<Courier> spec=new Specification<Courier>() {

			@Override
			//参数1：根查询对象（主查询对象--from roottable）
			//参数2：简单查询拼接条件对象，用来拼接条件语句
			//参数3：复杂条件构建对象
			//返回：条件的结果对象
			public Predicate toPredicate(Root<Courier> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				//定义集合
				List<Predicate> andPredicateList=new ArrayList<>();//放and拼接
				List<Predicate> orPredicateList=new ArrayList<>();//放or拼接
				
				
				//======单表、单对象普通属性
				//工号
				if(StringUtils.isNotBlank(model.getCourierNum())){
					
					//hibernate :Restrictions.eq("属性名","属性值")
					//参数1：属性 （路径） 
					//参数2：属性的值
					//相当courierNum=?
					Predicate p = cb.equal(root.get("courierNum").as(String.class), model.getCourierNum());
					andPredicateList.add(p);
				}
				//所属单位
				if(StringUtils.isNotBlank(model.getCompany())){
					//模糊匹配
					Predicate p = cb.like(root.get("company").as(String.class), "%"+model.getCompany()+ "%");
					andPredicateList.add(p);
				}
				//类型
				if(StringUtils.isNotBlank(model.getType())){
					//模糊匹配
					Predicate p = cb.like(root.get("type").as(String.class), "%"+model.getType()+ "%");
					andPredicateList.add(p);
				}
				
				//======多表、关联对象属性
				//判断要不要拼接!
				if(model.getStandard()!=null){
					//连接
					//复杂写法
//					Join<Courier, Standard> standardJoin = root.join(root.getModel().getSingularAttribute("standard", Standard.class), JoinType.INNER);
					//简单写法
//					Join<Object, Object> standardJoin = root.join("standard",JoinType.INNER);
					//更简单写法，默认内连接
					Join<Object, Object> standardJoin = root.join("standard");
					
					//判断拼接条件
					//收派标准
					if(StringUtils.isNotBlank(model.getStandard().getName())){
						Predicate p = cb.like(standardJoin.get("name").as(String.class), "%"+model.getStandard().getName()+"%");
						andPredicateList.add(p);
					}
				
				}
				
				//最终要将集合中p对象合并为一个p
				Predicate andP = cb.and(andPredicateList.toArray(new Predicate[0]) );
				
				//积木编程：先造积木，再拼接积木
				/*
				 * pAnd=cb.and (p1,p2);
				 * pOr= cb.or(p3,p4...);
				 * cb.and(pAnd,pOr)
				 */
				
				return andP;
			}
			
		};
		//第三步：调用service获取响应分页数据
		Page<Courier> pageResponse = courierService.findCourierListPage(spec, pageable);
		//将分页结果数据压入root栈顶
		pushPageDataToValuestackRoot(pageResponse);
		
		return JSON;
	}
	
	//属性驱动获取ids
	private String ids;
	public void setIds(String ids) {
		this.ids = ids;
	}


	//作废
	@Action("courier_deleteBatch")
	public String deleteBatch(){
		//定义map：存放结果
		Map<String, Object> resultMap=new HashMap<>();
		try {
			courierService.deleteBatch(ids);
			//成功
			resultMap.put("result", true);
		} catch (Exception e) {
			e.printStackTrace();
			//失败
			resultMap.put("result", false);
		}
		//将结果压入root栈顶
		ActionContext.getContext().getValueStack().push(resultMap);
		
		return JSON;
	}
	
	/**
	 * 还原快递员
	 * @return
	 * @author ZSZ
	 * @time：2017年12月24日 下午3:37:49
	 */
	@Action("courier_restoreCourier")
	public String restoreCourier(){
		//定义一个结果集
		Map<String, Object> result = new HashMap<>();
		try {
			
			courierService.resBatch(ids);;
			result.put("result", true);
		} catch (Exception e) {
			e.printStackTrace();
			result.put("result", false);
		}
		//将结果集压入值栈
		ActionContext.getContext().getValueStack().push(result);
		return JSON;
	}
	
	//列出没有作废的快递员
	@Action("courier_listNoDeltag")
	public String listNoDeltag(){
		List<Courier> courierList= courierService.findCourierListNoDeltag();
		ActionContext.getContext().getValueStack().push(courierList);
		return JSON;
	}

}
