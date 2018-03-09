package cn.itcast.fore.springdataredis.test;

import java.util.concurrent.TimeUnit;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:applicationContext-cache.xml"})
public class SpringDataRedisTest {

	//注入模版对象
	@Autowired
	private RedisTemplate<String, String> redisTemplate;
	
	@Test
	public void test(){
		//目标：操作redis：存+取
		//基本存值：
//		redisTemplate.opsForValue().set("username2", "小红");
		
		//基本取值
//		System.out.println(redisTemplate.opsForValue().get("username2"));
		
		//存放有时效的值：
		//参数3：超时的时间的数字
		//参数4：超时的时间的单位
		redisTemplate.opsForValue().set("username3", "小绿", 10, TimeUnit.SECONDS);
		
	}
}
