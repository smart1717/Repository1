package cn.itcast.ms.consumer.sms;

import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageListener;

import org.springframework.stereotype.Component;

import cn.itcast.utils.SmsUtils;
//短消息-验证码的消费者
@Component("smsCheckcodeConsumer")
public class SmsCheckcodeConsumer implements MessageListener{

	@Override
	public void onMessage(Message message) {
		//目标：获取消息内容，调用第三方短信平台发出短信
		MapMessage mapMessage=(MapMessage)message;
		try {
			//手机号码
			String telephone = mapMessage.getString("telephone");
			//验证码
			String checkcode = mapMessage.getString("checkcode");
			
			//调用第三方短信平台发短信
			SmsUtils.sendSmsCheckCode(checkcode, telephone);
		} catch (JMSException e) {
			e.printStackTrace();
		}
		
	}

}
