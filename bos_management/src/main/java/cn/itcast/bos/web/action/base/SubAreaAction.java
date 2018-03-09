package cn.itcast.bos.web.action.base;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.struts2.convention.annotation.Action;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;

import com.opensymphony.xwork2.ActionContext;

import cn.itcast.bos.domain.base.Area;
import cn.itcast.bos.domain.base.FixedArea;
import cn.itcast.bos.domain.base.SubArea;
import cn.itcast.bos.service.base.AreaService;
import cn.itcast.bos.service.base.FixedAreaService;
import cn.itcast.bos.service.base.SubAreaService;
import cn.itcast.bos.web.action.common.BaseAction;
import oracle.net.aso.s;
@Controller
@Scope("prototype")
public class SubAreaAction extends BaseAction<SubArea>{
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
	
	@Autowired
	private SubAreaService subAreaService;
	@Autowired
	private AreaService areaService;
	@Autowired
	private FixedAreaService fixedAreaService;
	
	@Action("subArea_listPage")
	public String listPage(){
		PageRequest pageRequest = new PageRequest(page-1, rows);
		Page<SubArea> listPage = subAreaService.findAreaListPage(pageRequest);
		pushPageDataToValuestackRoot(listPage);
		return JSON;
	}
	
	@Action("subArea_importData")
	public String importData(){
		
		ArrayList<SubArea> list = new ArrayList<>();
		HashMap<String, Boolean> map = new HashMap<>();
		try {
			HSSFWorkbook Workbook = new HSSFWorkbook(new FileInputStream(upload));
			HSSFSheet sheetAt = Workbook.getSheetAt(0);
			for (Row row : sheetAt) {
				if(row.getRowNum()==0){
					continue;
				}
				String id = row.getCell(0).getStringCellValue();
				String fixedArea = row.getCell(1).getStringCellValue();
				String area = row.getCell(2).getStringCellValue();
				String keyWords = row.getCell(3).getStringCellValue();
				String startNum = row.getCell(4).getStringCellValue();
				String endNum = row.getCell(5).getStringCellValue();
				String single = row.getCell(6).getStringCellValue();
				String assistKeyWords = row.getCell(7).getStringCellValue();
				
				FixedArea findById = fixedAreaService.findById(fixedArea);
				Area findByPostcode = areaService.findByPostcode(area);
				
				SubArea subArea = new SubArea();
				subArea.setId(id);
				subArea.setFixedArea(findById);
				subArea.setArea(findByPostcode);
				subArea.setKeyWords(keyWords);
				subArea.setStartNum(startNum);
				subArea.setEndNum(endNum);
				subArea.setSingle(single.charAt(0));
				subArea.setAssistKeyWords(assistKeyWords);
				list.add(subArea);
			}
			subAreaService.saveSubArea(list);
			map.put("result", true);
		} catch (Exception e) {
			map.put("result", false);
		} 
		ActionContext.getContext().getValueStack().push(map);
		return JSON;		
	}
}
