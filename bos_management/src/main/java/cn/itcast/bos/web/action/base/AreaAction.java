package cn.itcast.bos.web.action.base;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.servlet.ServletOutputStream;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Controller;

import com.github.stuxuhai.jpinyin.PinyinFormat;
import com.github.stuxuhai.jpinyin.PinyinHelper;
import com.opensymphony.xwork2.ActionContext;

import cn.itcast.bos.domain.base.Area;
import cn.itcast.bos.service.base.AreaService;
import cn.itcast.bos.web.action.common.BaseAction;
import cn.itcast.utils.FileUtils;

//区域action
@Controller
@Scope("prototype")
public class AreaAction extends BaseAction<Area>{
	//上传文件的相关接收
	private File upload;//文件对象
	private String uploadFileName;//文件名
	private String uploadContentType;//文件类型
	public void setUpload(File upload) {
		this.upload = upload;
	}
	public void setUploadFileName(String uploadFileName) {
		this.uploadFileName = uploadFileName;
	}
	public void setUploadContentType(String uploadContentType) {
		this.uploadContentType = uploadContentType;
	}
	
	//注入service
	@Autowired
	private AreaService areaService;
	
	/**
	 * 添加区域
	 * @return
	 * @author ZSZ
	 * @time：2017年12月24日 下午8:13:30
	 */
	@Action(value="area_save", results={@Result(type=REDIRECT,location="./pages/base/area.html")})
	public String SavedRequest(){
		if (StringUtils.isNoneBlank(model.getId())) {
			model.setId(model.getId());
		}else {
			//手动分配区域的id
			model.setId(model.getPostcode()+new Random().nextInt(100));
		}
		areaService.save(model);
		return SUCCESS;
	}
	

	//导入excel数据
	@Action("area_importData")
	public String importData(){
		//第一步：获取客户端上传的文件对象(struts2的fileupload拦截器)
		//第二步：解析excel，入库
		//声明一个集合临时保存区域对象
		List<Area> areaList=new ArrayList<>();
		
		//结果map
		Map<String, Object> resultMap=new HashMap<>();
		
		try {
			//技巧：平时怎么读excel，现在就怎么写代码
			//1)打开工作簿97格式
			HSSFWorkbook workbook=new HSSFWorkbook(new FileInputStream(upload));
			//2)读取工作表Iterable<Row>
//			workbook.getSheet(name);//根据名字读取
			HSSFSheet sheet = workbook.getSheetAt(0);//根据索引读取，从0开始
			//3)遍历表中的行
			for (Row row : sheet) {
				//先判断是否是第一行，如果是，则跳过
				if(row.getRowNum()==0){
					continue;
				}
				
				//读取正式数据
				String id = row.getCell(0).getStringCellValue();
				String province = row.getCell(1).getStringCellValue();
				String city = row.getCell(2).getStringCellValue();
				String district = row.getCell(3).getStringCellValue();
				String postcode = row.getCell(4).getStringCellValue();
				
				//创建一个对象
				Area area=new Area();
				area.setId(id);
				area.setProvince(province);
				area.setCity(city);
				area.setDistrict(district);
				area.setPostcode(postcode);
				
				//汉字转拼音
				//处理截取需要中文
				String provinceStr = StringUtils.substring(province, 0, -1);
				String cityStr = StringUtils.substring(city, 0, -1);
				String districtStr = StringUtils.substring(district, 0, -1);
				//区域简码
				String shortcode = PinyinHelper.getShortPinyin(provinceStr+cityStr+districtStr).toUpperCase();
				area.setShortcode(shortcode);
				//城市编码
				/*
				 * str 需要转换的字符串
					separator 拼音分隔符
					pinyinFormat 拼音格式：WITH_TONE_NUMBER--数字代表声调，WITHOUT_TONE--不带声调，WITH_TONE_MARK--带声调
				 */
				String citycode = PinyinHelper.convertToPinyinString(cityStr, "", PinyinFormat.WITHOUT_TONE);
				area.setCitycode(citycode);
				//添加到集合中
				areaList.add(area);
			}
			//保存
			areaService.saveArea(areaList);
			//成功
			resultMap.put("result", true);
		} catch (Exception e) {
			e.printStackTrace();
			//成功
			resultMap.put("result", false);
		}
		
		//给客户友好提示
		//结果压入root栈顶
		ActionContext.getContext().getValueStack().push(resultMap);
		
		return JSON;
	}
	
	/**
	 * 条件分页查询
	 * @return
	 * @author ZSZ
	 * @time：2017年12月24日 下午8:25:59
	 */
	@Action("area_pageQuery")
	public String pageQuery(){
		//构造pageable
		Pageable pageable = new PageRequest(page - 1, rows);
		//构造specification
		Specification<Area> specification = new Specification<Area>() {
			
			@Override
			public Predicate toPredicate(Root<Area> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				Predicate predicate = null;
				
				List<Predicate> list = new ArrayList<>();
				
				if (StringUtils.isNoneBlank(model.getProvince())) {
					Predicate p1 = cb.like(root.get("province").as(String.class), "%" + model.getProvince() + "%");
					list.add(p1);
				}
				if (StringUtils.isNoneBlank(model.getCity())) {
					Predicate p2 = cb.like(root.get("city").as(String.class),"%" + model.getCity() + "%");
					list.add(p2);
				}
				if (StringUtils.isNoneBlank(model.getDistrict())) {
					Predicate p3 = cb.like(root.get("district").as(String.class), "%" + model.getDistrict() + "%");
					list.add(p3);
				}
				if(list!=null && list.size()>0){
					Predicate [] p = new Predicate[list.size()];
					predicate = cb.and(list.toArray(p));
				}
				return predicate;
			}
		
	};
		//带有条件的查询
		Page<Area> pageData = areaService.findPageData(specification,pageable);
		pushPageDataToValuestackRoot(pageData);
		return JSON;
	}
	
	/**
	 * 导出Excel
	 * @return
	 * @throws IOException
	 * @author ZSZ
	 * @time：2017年12月24日 下午7:16:49
	 */
	@Action("report_exportXls")
	public String exportXls() throws IOException {
		// 查询所有区域
		List<Area> areaList = areaService.findAreaList();

		// 生成Excel文件
		HSSFWorkbook hssfWorkbook = new HSSFWorkbook();
		// 创建Sheet
		HSSFSheet sheet = hssfWorkbook.createSheet("区域数据");
		// 表头
		HSSFRow headRow = sheet.createRow(0);

		headRow.createCell(0).setCellValue("省");
		headRow.createCell(1).setCellValue("市");
		headRow.createCell(2).setCellValue("区");
		headRow.createCell(3).setCellValue("邮编");
		headRow.createCell(4).setCellValue("简码");
		headRow.createCell(5).setCellValue("城市编码");
		// 表格数据
		for (Area area : areaList) {
			// 设置Sheet中最后一行的行号+1，或者for循环的时候用索引for(int i=0;i<wayBills.size();i++)，用i的形式创建行号
			HSSFRow dataRow = sheet.createRow(sheet.getLastRowNum() + 1);
			dataRow.createCell(0).setCellValue(area.getProvince());
			dataRow.createCell(1).setCellValue(area.getCity());
			dataRow.createCell(2).setCellValue(area.getDistrict());
			dataRow.createCell(3).setCellValue(area.getPostcode());
			dataRow.createCell(4).setCellValue(area.getShortcode());
			dataRow.createCell(5).setCellValue(area.getCitycode());
		}

		// 下载导出
		// 设置头信息
		ServletActionContext.getResponse().setContentType(
				"application/vnd.ms-excel");
		String filename = "区域数据.xls";
		String agent = ServletActionContext.getRequest()
				.getHeader("user-agent");
		filename = FileUtils.encodeDownloadFilename(filename, agent);
		ServletActionContext.getResponse().setHeader("Content-Disposition",
				"attachment;filename=" + filename);
		
		// 将Excel文档写到输出流中
		ServletOutputStream outputStream = ServletActionContext.getResponse().getOutputStream();
		hssfWorkbook.write(outputStream);

		// 关闭
		hssfWorkbook.close();

		return NONE;
	}
	
	//简单属性驱动，接收ids字符串
	private String ids;

	public void setIds(String ids) {
		this.ids = ids;
	}

	/**
	 * 批量删除区域
	 * @return
	 * @author ZSZ
	 * @time：2017年12月24日 下午9:07:15
	 */
	@Action("area_deleteArea")
	public String deleteCourier(){
		//定义一个结果集
		Map<String, Object> result = new HashMap<>();
		try {
			areaService.delBatch(ids);
			result.put("result", true);
		} catch (Exception e) {
			e.printStackTrace();
			result.put("result", false);
		}
		//将结果集压入值栈
		ActionContext.getContext().getValueStack().push(result);
		
		return JSON;
	}
	
}



