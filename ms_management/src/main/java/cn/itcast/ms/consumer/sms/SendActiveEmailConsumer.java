/**
 * 
 */
package cn.itcast.ms.consumer.sms;

import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageListener;

import org.springframework.stereotype.Component;

import cn.itcast.utils.MailUtils;



/**
 * @author ZSZ
 * @version 1.0
 * @date 2017年12月4日上午9:26:08
 */
// 邮件处理的消费者
@Component("sendActiveEmailConsumer")
public class SendActiveEmailConsumer implements MessageListener {

	@Override
	public void onMessage(Message message) {
		MapMessage mapMessage = (MapMessage) message;
		try {
			// 取值
			String subject = mapMessage.getString("subject");// 标题
			String content = mapMessage.getString("content");// 内容
			String to = mapMessage.getString("to");// 收件人
			// 发送邮件
			MailUtils.sendMail(subject, content, to);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
