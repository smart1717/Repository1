package cn.itcast.bos.web.action.common;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.springframework.data.domain.Page;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

//通用action
@Results({
	@Result(name=BaseAction.JSON,type=BaseAction.JSON)//全局的json类型结果集
})
@ParentPackage("json-default")//继承json插件的包
@Namespace("/")
public class BaseAction<T> extends  ActionSupport implements ModelDriven<T>{
	
	//声明一个数据模型对象
	protected T model;
	

	@Override
	public T getModel() {
		return model;
	}
	
	//默认构造器
	public BaseAction() {
		//目标：初始化T的具体类型的对象
		//获取BaseAction<T>类型
		Type superType = this.getClass().getGenericSuperclass();
		//强转一般类型为泛型化类型
		ParameterizedType parameterizedType=(ParameterizedType)superType;
		//获取泛型化类型的参数的类型
		Class<T> modelClass =(Class<T>) parameterizedType.getActualTypeArguments()[0];
		//实例化modelClass
		try {
			model=modelClass.newInstance();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}
	
	
	//================常量
	public static final String REDIRECT = "redirect"; //重定向字符串
	public static final String JSON = "json"; //json字符串
	
	//================分页列表相关
	//获取分页参数
	protected int page;
	protected int rows;
	public void setPage(int page) {
		this.page = page;
	}
	public void setRows(int rows) {
		this.rows = rows;
	}
	
	//将分页结果数据重新组装并压入root栈顶
	protected void pushPageDataToValuestackRoot(Page<T> pageResponse) {
		//第四步：重新组装数据
		Map<String, Object> resultMap=new HashMap<>();
		//总记录数
		resultMap.put("total", pageResponse.getTotalElements());
		//当前页的数据列表
		resultMap.put("rows", pageResponse.getContent());
		
		//第五步：将结果压入root栈顶
		ActionContext.getContext().getValueStack().push(resultMap);
	}
	
	

}
