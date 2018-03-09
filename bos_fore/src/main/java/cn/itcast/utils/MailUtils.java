package cn.itcast.utils;

import java.util.Date;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMessage.RecipientType;

public class MailUtils {
	private static String SMTP_HOST = "smtp.163.com";//发送邮件服务器地址
	private static String USERNAME = "runjumpfly@163.com";//邮箱帐号
	private static String PASSWORD = "itcast2017";//邮箱密码（对于网易邮箱，这里是授权码，不是真正的密码）
	private static String FROM = "runjumpfly@163.com";//发送的邮箱地址
//	private static String activeUrl = "http://localhost:9003/bos_fore/customer_activeMail";//激活地址

	//发送邮件
	public static void sendMail(String subject, String content, String to) {
		//1.创建参数配置,用于连接邮件服务器的参数配置
		Properties props = new Properties();//参数配置
		props.setProperty("mail.transport.protocol", "smtp");//使用的协议（JavaMail规范要求）
		props.setProperty("mail.smtp.host", SMTP_HOST);//发件人的邮箱的SMTP服务器地址
		props.setProperty("mail.smtp.auth", "true");//需要请求认证
		//开启ssl安全验证
//		props.setProperty("mail.smtp.port",smtpPort);
//		props.setProperty("mail.smtp.socketFactory.class","javax.net.ssl.SSLSocketFactory");
//		props.setProperty("mail.smtp.socketFactory.fallback","false");
//		props.setProperty("mail.smtp.socketFactory.port",smtpPort);
		//2.根据配置创建会话对象,用于和邮件服务器交互
//		Session session = Session.getInstance(props);
		Session session = Session.getDefaultInstance(props);
		session.setDebug(true);//设置为debug模式,可以查看详细的发送log
		
		//3.创建一封邮件
		Message message = new MimeMessage(session);

		try {
			
			//paramsendMail发件人邮箱,paramreceiveMail收件人邮箱
//			MimeMessage message=createMimeMessage(session,myEmailAccount,receiveMailAccount);//方法抽取
			//下面的内容可以抽取出一个方法createMimeMessage
			//1.创建一封邮件
//			MimeMessage message=new MimeMessage(session);
//			//2.From:发件人
			message.setFrom(new InternetAddress(FROM));
			//参数1：发件人地址，参数2：发件人称呼，参数3：编码
//			message.setFrom(new InternetAddress(sendMail,"波波老师","UTF-8"));
//			//3.To:收件人（可以增加多个收件人、抄送、密送）
			message.setRecipient(RecipientType.TO, new InternetAddress(to));
//			message.setRecipient(MimeMessage.RecipientType.TO,new InternetAddress(receiveMail,"周小姐","UTF-8"));
//			//4.Subject:邮件主题
			message.setSubject(subject);
//			message.setSubject(subject,"UTF-8");
//			//5.Content:邮件正文（可以使用html标签）
			message.setContent(content,"text/html;charset=UTF-8");
//			//6.设置发件时间（可选）
			message.setSentDate(new Date());
//			//7.保存设置（可选）
//			message.saveChanges();
			
			//准备发送
			Transport transport = session.getTransport();
			transport.connect(SMTP_HOST, USERNAME, PASSWORD);
			//发送邮件
			transport.sendMessage(message, message.getAllRecipients());
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("邮件发送失败...");
		}
	}

	public static void main(String[] args) {
//		sendMailOld("测试邮件", "你好，传智播客", "runjumpfly@163.com", "98765");
		String activeUrl = "http://localhost:9003/bos_fore/customer_activeMail";//激活地址
		String activecode="1234";
		String content="<h3>请点击地址激活:<a href=" + activeUrl
				+ "?activecode=" + activecode + ">" + activeUrl
				+ "</a></h3>";
		String subject="BOS测试邮件";
		String to="runjumpfly@163.com";
		sendMail(subject, content, to);
	}
}
