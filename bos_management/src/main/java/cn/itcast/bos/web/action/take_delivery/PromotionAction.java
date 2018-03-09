package cn.itcast.bos.web.action.take_delivery;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import org.apache.commons.io.FileUtils;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;

import cn.itcast.bos.domain.take_delivery.Promotion;
import cn.itcast.bos.service.take_delivery.PromotionService;
import cn.itcast.bos.web.action.common.BaseAction;

//宣传广告的action
@Controller
@Scope("prototype")
public class PromotionAction extends BaseAction<Promotion>{

	//struts2的FileUpload拦截器获取宣传图片（缩略图）
	private File titleImgFile;//文件对象
	private String titleImgFileContentType;//mime类型
	private String titleImgFileFileName;//文件名
	public void setTitleImgFile(File titleImgFile) {
		this.titleImgFile = titleImgFile;
	}
	public void setTitleImgFileContentType(String titleImgFileContentType) {
		this.titleImgFileContentType = titleImgFileContentType;
	}
	public void setTitleImgFileFileName(String titleImgFileFileName) {
		this.titleImgFileFileName = titleImgFileFileName;
	}
	
	//注入service
	@Autowired
	private PromotionService promotionService;
	
	//保存
	@Action(value="promotion_add",results={
			@Result(type=REDIRECT,location="/pages/take_delivery/promotion.html")
	})
	public String add() throws Exception{
		
		//处理宣传图片
		//两个动作
		//1)保存文件到服务器
		//文件保存目录路径
		String savePath = ServletActionContext.getServletContext().getRealPath("/") + "upload/";//绝对文件路径
		//文件保存目录URL
		String saveUrl  = ServletActionContext.getRequest().getContextPath() + "/upload/";//相对url
		//获取文件的扩展名
		String fileExt = titleImgFileFileName.substring(titleImgFileFileName.lastIndexOf(".") + 1).toLowerCase();
		
		//生成随机的文件名（用来保存到服务器上）
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
		String newFileName = df.format(new Date()) + "_" + new Random().nextInt(1000) + "." + fileExt;
		//将文件保存到服务器上，使用新名字
		FileUtils.copyFile(titleImgFile, new File(savePath+newFileName));
		//2)将文件路径保存到数据库,相对url路径-将来要访问
		model.setTitleImg(saveUrl+newFileName);
		
		//调用业务层保存
		promotionService.savePromotion(model);
		
		return SUCCESS;
	}
	
	//分页列表查询
	@Action("promotion_listPage")
	public String listPage(){
		//封装请求的条件的bean
		Pageable pageable=new PageRequest(page-1, rows);
		//调用业务层查询
		Page<Promotion> pageResponse=promotionService.findPromotionListPage(pageable);
		//将结果压入root栈顶
		pushPageDataToValuestackRoot(pageResponse);
		
		return JSON;
	}
}
