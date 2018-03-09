package cn.itcast.fore.web.action;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.core.MediaType;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.output.FileWriterWithEncoding;
import org.apache.cxf.jaxrs.client.WebClient;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.opensymphony.xwork2.ActionContext;

import cn.itcast.bos.domain.page.PageBean;
import cn.itcast.bos.domain.take_delivery.Promotion;
import cn.itcast.bos.web.action.common.BaseAction;
import cn.itcast.utils.Constants;
import freemarker.template.Configuration;
import freemarker.template.Template;
//宣传活动action
@Controller
@Scope("prototype")
public class PromotionAction extends BaseAction<Promotion>{

	//分页列表查询(远程数据请求)
	@Action("promotion_listPage")
	public String listPage(){
		//调用WebService查询列表数据
		PageBean pageBean = WebClient.create(Constants.BOS_MANAGEMENT_URL
				+"/services/promotionservice/promotions/listpage?page="+page+"&rows="+rows)
		.accept(MediaType.APPLICATION_JSON)
		.get(PageBean.class);
		//压入root栈顶
		ActionContext.getContext().getValueStack().push(pageBean);
		
		return JSON;
	}
	
	//根据id和模版生成静态页面
	@Action("promotion_showDetail")
	public String showDetail() throws Exception{
		//1.获取存放html静态页面的路径
		String htmlRealPath=ServletActionContext.getServletContext().getRealPath("/")+"freemarker/promotion/";
		//创建文件对象
		File dirFile=new File(htmlRealPath);
		//判断
		if(!dirFile.exists()){
			//如果文件夹不存在，则手动创建
			dirFile.mkdirs();//自动创建父目录和子目录。
		}
		
		//获取静态文件,规则：id+.html
		File htmlFile=new File(htmlRealPath+model.getId()+".html");
		
		
		//看静态文件在不在里面
//		if(!htmlFile.exists()){
			//通过freemark生成该文件
			//第一步：创建一个配置对象
			Configuration configuration=new Configuration(Configuration.VERSION_2_3_22);
			//在配置对象中指定模版存放的路径
			configuration.setDirectoryForTemplateLoading(
					new File("src/main/webapp/WEB-INF/templates"));
			//第二步：读取模版文件
			Template template = configuration.getTemplate("promotion.ftl");
			
			//第三步：创建java对象，用来填充模版
			//远程调用WebService
			Promotion promotion = WebClient.create(Constants.BOS_MANAGEMENT_URL
					+"/services/promotionservice/promotions")
			.path("/"+model.getId())
			.accept(MediaType.APPLICATION_JSON)
			.get(Promotion.class);
			
			
			Map<String, Object> parameterMap=new HashMap<>();
			parameterMap.put("promotion", promotion);
			
			//第四步：将模版和填充对象进行合并，为填充后的文本
//			template.process(parameterMap, new PrintWriter(new BufferedWriter(new FileWriter(htmlFile))));//写到文件中
			template.process(parameterMap, new PrintWriter(new BufferedWriter(new FileWriterWithEncoding(htmlFile,"utf-8"))));//写到文件中使用utf-8
//		}
		
		//将html文件写入响应即可
		//指定输出的文件内容的编码：（响应的的编码）
		ServletActionContext.getResponse().setContentType("text/html;charset=utf-8");//U8编码
		FileUtils.copyFile(htmlFile, ServletActionContext.getResponse().getOutputStream());
		
		return NONE;
	}
}
