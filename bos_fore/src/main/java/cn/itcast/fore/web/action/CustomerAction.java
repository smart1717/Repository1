package cn.itcast.fore.web.action;

import java.util.concurrent.TimeUnit;

import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.Session;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.MediaType;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.cxf.jaxrs.client.WebClient;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;

import cn.itcast.bos.web.action.common.BaseAction;
import cn.itcast.crm.domain.Customer;
import cn.itcast.utils.Constants;



//客户的action
public class CustomerAction extends BaseAction<Customer>{
	private static final long serialVersionUID = 1L;
	//注入消息模版对象
	@Autowired
	private JmsTemplate jmsQueueTemplate;
	//发送短信验证码
	@Action("customer_sendCheckcode")
	public String sendCheckcode(){
		//要获取手机号码（模型驱动搞定）
		//生成随机验证码(约定4个数字)
		final String checkcode = RandomStringUtils.randomNumeric(4);
		//将验证码放入session
		ServletActionContext.getRequest().getSession().setAttribute(model.getTelephone(), checkcode);
		
		//发送短信验证码
//		SmsUtils.sendSmsCheckCode(checkcode, model.getTelephone());
		//将短信消息发送给mq
		jmsQueueTemplate.send("bos.sms.normal.checkcode", new MessageCreator() {
			
			@Override
			public Message createMessage(Session session) throws JMSException {
				//使用map消息
				MapMessage message = session.createMapMessage();
				//手机号码
				message.setString("telephone", model.getTelephone());
				//验证码：
				message.setString("checkcode", checkcode);
				//返回消息给mq
				return message;
			}
		});
		
		
		return NONE;
	}
	
	//获取用户输入的验证码
	private String checkcode;
	public void setCheckcode(String checkcode) {
		this.checkcode = checkcode;
	}
	
	//注入RedisTemplate
	@Autowired
	private RedisTemplate<String, String> redisTemplate;

	//客户注册
	@Action(value="customer_regist",results={
			@Result(type=REDIRECT,location="/signup-success.html")
			,@Result(name=INPUT, type=REDIRECT,location="/signup.html")
	})
	public String regist(){
		System.out.println("客户信息正在注册保存中。。。。");
		//第一步：手机验证码的校验
	
		//获取session中的验证码，根据手机号码
		String checkcodeSession = (String) ServletActionContext.getRequest().getSession()
				.getAttribute(model.getTelephone());
		//清除session中的验证码
		ServletActionContext.getRequest().getSession().removeAttribute(model.getTelephone());
		//验证码对比（用户输入和session中存放的）
		if(StringUtils.isBlank(checkcode)||!checkcode.equals(checkcodeSession)){
			//验证不通过
			return INPUT;
			
		}
		//验证通过
		//第二步：调用Webservice的接口保存客户
//		WebClient.create("http://localhost:9002/crm_management/services/customerservice/customers")
		WebClient.create(Constants.CRM_MANAGEMENT_URL+"/services/customerservice/customers")
		.type(MediaType.APPLICATION_JSON)
		.post(model);
		
		
		//第三步：发送邮件到用户的注册的邮箱中。
		
		//1)
		//邮件主题
		final String subject="速运快递的注册激活邮件";
		//激活地址
//		String activeUrl = "http://localhost:9003/bos_fore/customer_activeMail.action";
		String activeUrl = Constants.BOS_FORE_URL+"/customer_activeMail.action";
		//随机码
		String activecode=RandomStringUtils.randomNumeric(32);
		
		//拼接url地址
		String urlAddress=activeUrl+ "?activecode=" + activecode+"&telephone="+model.getTelephone();
		
		//邮件正文
		final String content="<h3>请点击地址激活:<a href=" + urlAddress + ">" + urlAddress
				+ "</a></h3>";
		//邮件接收地址(根据用户填写的地址)
		final String to=model.getEmail();
		//MailUtils.sendMail(subject, content, to);
		//将发邮件的内容发给mq服务器（把发邮件的任务交给mq）
		 jmsQueueTemplate.send("bos.sms.normal.activeEmail", new MessageCreator() {
				
				@Override
				public Message createMessage(Session session) throws JMSException {
					MapMessage mapMessage = session.createMapMessage();
					mapMessage.setString("subject", subject);
					mapMessage.setString("content", content);
					mapMessage.setString("to", to);
					return mapMessage;
				}
			});
		
		//2)将随机码放入redis缓存起来
		redisTemplate.opsForValue().set(model.getTelephone(), activecode, 24, TimeUnit.HOURS);
		
		
		return SUCCESS;
	}
	
	//属性驱动封装邮件随机激活码
	private String activecode;
	public void setActivecode(String activecode) {
		this.activecode = activecode;
	}

	//邮件激活
	@Action("customer_activeMail")
	public String activeMail() throws Exception{
		//获取响应对象
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("text/html; charset=UTF-8");//响应乱码解决
		//获取Redis中的邮件随机码
		String activecodeRedis = redisTemplate.opsForValue().get(model.getTelephone());
		
		//判断邮件随机码是否有效
		if(StringUtils.isBlank(activecodeRedis)||!activecodeRedis.equals(activecode)){
			//激活码无效，激活失败
			response.getWriter().println("对不起，邮件激活码已经失效，激活失败，");
			return NONE;
		}
		
		//激活码有效，要激活
		//Webservice调用
//		WebClient.create("http://localhost:9002/crm_management/services/customerservice/customers")
		WebClient.create(Constants.CRM_MANAGEMENT_URL+"/services/customerservice/customers")
		.path("/type")
		.path("/"+model.getTelephone())//需要判断
		.path("/1")//1代表激活
		.type(MediaType.APPLICATION_JSON)
		.put(null);
		//激活成功
		response.getWriter().println("恭喜，邮件激活成功，请返回主页");
		
		
		//最后清除redis中的邮件激活码
		redisTemplate.delete(model.getTelephone());
		
		return NONE;
		
	}
}
