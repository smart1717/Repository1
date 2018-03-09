package cn.itcast.bos.web.action.take_delivery;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.opensymphony.xwork2.ActionContext;

import cn.itcast.bos.web.action.common.BaseAction;

//kindeditor：图片上传和管理的action
//没有使用模型驱动封装参数，可以使用Object泛型类型
@Controller
@Scope("prototype")
public class ImageAction extends BaseAction<Object>{

	//获取文件对象-struts2的拦截器FileUpload
	private File imgFile;//文件对象
	private String imgFileContentType;//mime类型
	private String imgFileFileName;//文件名
	public void setImgFile(File imgFile) {
		this.imgFile = imgFile;
	}
	public void setImgFileContentType(String imgFileContentType) {
		this.imgFileContentType = imgFileContentType;
	}
	public void setImgFileFileName(String imgFileFileName) {
		this.imgFileFileName = imgFileFileName;
	}

	@Action("image_upload")
	public String upload(){
		//构建一个map，用来封装响应结果
		Map<String, Object> resultMap=new HashMap<String, Object>();
		
		//定义允许上传的文件扩展名
		HashMap<String, String> extMap = new HashMap<String, String>();
		extMap.put("image", "gif,jpg,jpeg,png,bmp");
		extMap.put("flash", "swf,flv");
		extMap.put("media", "swf,flv,mp3,wav,wma,wmv,mid,avi,mpg,asf,rm,rmvb");
		extMap.put("file", "doc,docx,xls,xlsx,ppt,htm,html,txt,zip,rar,gz,bz2");
		
		//获取请求地址中的参数dir
		String dirName = ServletActionContext.getRequest().getParameter("dir");
		//获取文件的扩展名
		String fileExt = imgFileFileName.substring(imgFileFileName.lastIndexOf(".") + 1).toLowerCase();
		
		//最大文件大小，byte
		long maxSize = 1024*1024*2;
//		long maxSize = 1;
		
		//检查文件大小
		if(FileUtils.sizeOf(imgFile) > maxSize){
			//给用户友好提示
			//上传失败
			resultMap.put("error", 1);
			//错误提示信息
			resultMap.put("message", "上传文件大小超过限制。");
		}else if(StringUtils.isNoneBlank(dirName) &&!Arrays.<String>asList(extMap.get(dirName).split(",")).contains(fileExt)){
			//判断文件扩展名的限制
			//给用户友好提示
			//上传失败
			resultMap.put("error", 1);
			//错误提示信息
			resultMap.put("message", "上传文件扩展名是不允许的扩展名。\n只允许" + extMap.get(dirName) + "格式。");
			
		}else{
			//System.out.println("上传的文件："+imgFile);
			//文件保存目录路径
			String savePath = ServletActionContext.getServletContext().getRealPath("/") + "upload/";//绝对文件路径
			//文件保存目录URL
			String saveUrl  = ServletActionContext.getRequest().getContextPath() + "/upload/";//相对url
			
			//生成随机的文件名（用来保存到服务器上）
			SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
			String newFileName = df.format(new Date()) + "_" + new Random().nextInt(1000) + "." + fileExt;
			
			//日志
			System.out.println("savePath："+savePath);
			System.out.println("saveUrl："+saveUrl);
			System.out.println("newFileName："+newFileName);
			
			try {
				//int i=1/0;
				//将文件保存到服务器上，使用新名字
				FileUtils.copyFile(imgFile, new File(savePath+newFileName));
				
				//上传成功
				resultMap.put("error", 0);
				//将来会自动插入到文档中的链接：可以是绝对路径（http://ip:port/路径/图片），也可以是相对路径（必须在当前项目中）(/路径+图片)
				resultMap.put("url", saveUrl+newFileName);
				
			} catch (Exception e) {
				e.printStackTrace();
				//上传失败
				resultMap.put("error", 1);
				//错误提示信息
				resultMap.put("message", "图片上传失败！请联系管理员！");
			}
		}
		
		//将map压入root栈顶
		ActionContext.getContext().getValueStack().push(resultMap);
		//返回json对象：格式必须满足KindEditor的要求
		return JSON;
	}
	
	
	//浏览远程图片的服务器端方法
	@Action("image_manage")
	public String manage(){
		//根目录路径，可以指定绝对路径，比如 /var/www/attached/
		String rootPath = ServletActionContext.getServletContext().getRealPath("/") + "upload/";//绝对路径
		//根目录URL，可以指定绝对路径，比如 http://www.yoursite.com/attached/
		String rootUrl  = ServletActionContext.getRequest().getContextPath() + "/upload/";//相对路径
		//图片扩展名，用于图片的类型筛选
		String[] fileTypes = new String[]{"gif", "jpg", "jpeg", "png", "bmp"};
		
		//根据path参数，设置各路径和URL
		//path:可以在fileManagerJson:"../../image_manage.action?path=当前业务存放图片的目录/"
		//没有path参数，说明没有业务子目录，这里是空串
		String path =  ServletActionContext.getRequest().getParameter("path") != null ? ServletActionContext.getRequest().getParameter("path") : "";
		String currentPath = rootPath + path;
		String currentUrl = rootUrl + path;
		String currentDirPath = path;
		String moveupDirPath = "";
		//判断子目录是否为空
		if (!"".equals(path)) {
			String str = currentDirPath.substring(0, currentDirPath.length() - 1);//截取最后以为的斜杠
			moveupDirPath = str.lastIndexOf("/") >= 0 ? str.substring(0, str.lastIndexOf("/") + 1) : "";
		}
		
		//获取目录的文件对象
		File currentPathFile = new File(currentPath);
		
		//遍历目录取的文件信息
		List<Hashtable> fileList = new ArrayList<Hashtable>();
		if(currentPathFile.listFiles() != null) {
			for (File file : currentPathFile.listFiles()) {
				Hashtable<String, Object> hash = new Hashtable<String, Object>();
				String fileName = file.getName();
				if(file.isDirectory()) {
					hash.put("is_dir", true);
					hash.put("has_file", (file.listFiles() != null));
					hash.put("filesize", 0L);
					hash.put("is_photo", false);
					hash.put("filetype", "");
				} else if(file.isFile()){
					String fileExt = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
					hash.put("is_dir", false);
					hash.put("has_file", false);
					hash.put("filesize", file.length());
					//删选文件的类型，必须是图片
					hash.put("is_photo", Arrays.<String>asList(fileTypes).contains(fileExt));
					hash.put("filetype", fileExt);
				}
				hash.put("filename", fileName);
				hash.put("datetime", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(file.lastModified()));
				fileList.add(hash);
			}
		}
		
		//构建一个结果的map
		Map<String, Object> resultMap=new HashMap<>();
		resultMap.put("moveup_dir_path", moveupDirPath);
		resultMap.put("current_dir_path", currentDirPath);//当前路径
		resultMap.put("current_url", currentUrl);//相对url会自动插入到文档中，显示图片
		resultMap.put("total_count", fileList.size());//图片总数
		resultMap.put("file_list", fileList);//图片列表
		
		//将map压入root栈顶
		ActionContext.getContext().getValueStack().push(resultMap);
		//返回json对象：格式必须满足KindEditor的要求
		return JSON;
	}
	
}
