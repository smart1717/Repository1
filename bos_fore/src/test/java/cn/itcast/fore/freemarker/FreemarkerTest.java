package cn.itcast.fore.freemarker;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import freemarker.template.Configuration;
import freemarker.template.Template;

public class FreemarkerTest {

	@Test
	public void testHello() throws Exception{
		//第一步：创建一个配置对象
		Configuration configuration=new Configuration(Configuration.VERSION_2_3_22);
		//在配置对象中指定模版存放的路径
		configuration.setDirectoryForTemplateLoading(
				new File("src/main/webapp/WEB-INF/templates"));
		//第二步：读取模版文件
		Template template = configuration.getTemplate("hello.ftl");
		
		//第三步：创建java对象，用来填充模版
		Map<String, Object> parameterMap=new HashMap<>();
		parameterMap.put("title", "我是标题");
		parameterMap.put("msg", "我是内容，哈哈。。。222222");
		
		//第四步：将模版和填充对象进行合并，为填充后的文本
		//参数1：要填充map对象
		//参数2：你要输出的文本的地方
		template.process(parameterMap, new PrintWriter(System.out));
		template.process(parameterMap, new PrintWriter(new BufferedWriter(new FileWriter("z:/freemarkder.html"))));//写到文件中
	}
}
