package cn.itcast.fore.web.action;

import javax.ws.rs.core.MediaType;

import org.apache.commons.lang3.StringUtils;
import org.apache.cxf.jaxrs.client.WebClient;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import cn.itcast.bos.domain.base.Area;
import cn.itcast.bos.domain.take_delivery.Order;
import cn.itcast.bos.web.action.common.BaseAction;
import cn.itcast.utils.Constants;
//订单action
@Controller
@Scope("prototype")
public class OrderAction extends BaseAction<Order>{

	//属性驱动获取区域信息
	//寄件人的
	private String sendAreaInfo;
	//收件人的
	private String recAreaInfo;
	public void setSendAreaInfo(String sendAreaInfo) {
		this.sendAreaInfo = sendAreaInfo;
	}
	public void setRecAreaInfo(String recAreaInfo) {
		this.recAreaInfo = recAreaInfo;
	}

	//保存新订单
	@Action(value="order_add",results={
			@Result(type=REDIRECT,location="/index.html")
	})
	public String add(){
		//区域数据截取
		String[] sendAreaInfoArray = StringUtils.split(sendAreaInfo,"/");
		String[] recAreaInfoArray = StringUtils.split(recAreaInfo,"/");
		
		//对省市区重新封装数据
		//寄件人
		Area sendArea=new Area();
		sendArea.setProvince(sendAreaInfoArray[0]);
		sendArea.setCity(sendAreaInfoArray[1]);
		sendArea.setDistrict(sendAreaInfoArray[2]);
		//收件人
		Area recArea=new Area();
		recArea.setProvince(recAreaInfoArray[0]);
		recArea.setCity(recAreaInfoArray[1]);
		recArea.setDistrict(recAreaInfoArray[2]);
		
		model.setSendArea(sendArea);
		model.setRecArea(recArea);
		
		//调用接口Webservice保存
		WebClient.create(Constants.BOS_MANAGEMENT_URL+"/services/orderservice/orders")
		.type(MediaType.APPLICATION_JSON)
		.post(model);
		
		return SUCCESS;
	}
}
