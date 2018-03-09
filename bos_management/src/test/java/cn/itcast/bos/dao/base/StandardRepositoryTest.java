package cn.itcast.bos.dao.base;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import cn.itcast.bos.domain.base.Standard;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:applicationContext.xml"})
public class StandardRepositoryTest {
	//注入要测试bean
	@Autowired
	private StandardRepository standardRepository;
	//测试保存
	@Test
	public void testSave(){
		Standard standard=new Standard();
		standard.setId(1);
		standard.setName("小件标准-上海");
		standardRepository.save(standard);
	}
	
	//测试查询所有
	@Test
	public void testFindAll(){
		List<Standard> list = standardRepository.findAll();
		System.out.println(list);
	}
	
	//根据名字精确查询
	@Test
	public void testFindByName(){
		Standard standard = standardRepository.findByName("小件标准-上海");
		System.out.println(standard);
	}
	//根据名字模糊查询
	@Test
	public void testFindByNameLike(){
		List<Standard> list = standardRepository.findByNameLike("%标准%");
		System.out.println(list);
	}
	
	//根据id查询名字
	@Test
	public void testFindNameById(){
		String name = standardRepository.findNameById(1);
		System.out.println(name);
	}
	
	//更新操作
	@Test
	public void testUpdateNameById(){
		standardRepository.updateNameById(1, "小件标准-上海2");
	}
		
	
}
