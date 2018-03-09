package cn.itcast.crm.service.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.itcast.crm.dao.CustomerRepository;
import cn.itcast.crm.domain.Customer;
import cn.itcast.crm.domain.PageBean;
import cn.itcast.crm.service.CustomerService;
//sei:实现
@Service("customerService")
@Transactional
public class CustomerServiceImpl implements CustomerService {
	//注入dao
	@Autowired
	private CustomerRepository customerRepository;

	@Override
	public List<Customer> findCustomerListNoFixedAreaId() {
		return customerRepository.findByFixedAreaIdIsNull();
	}

	@Override
	public List<Customer> findCustomerListByFixedAreaId(String fixedAreaId) {
		return customerRepository.findByFixedAreaId(fixedAreaId);
	}

	@Override
	public void updateFixedAreaIdByCustomerIds(String fixedAreaId, String customerIds) {
		//强烈推荐hibernate的快照更新！
		//两个逻辑：
		//第一个：先清掉要关联的定区的所有的之前的客户的记录中的字段
		//查询关联某定区原有的客户列表
		List<Customer> oldCustomerList = customerRepository.findByFixedAreaId(fixedAreaId);
		for (Customer customer : oldCustomerList) {
			//将定区编号字段置空
			customer.setFixedAreaId(null);
			//等flush
		}
		
		
		//第二步：拿最终（最新的）客户ids列表，关联到定区上（给定区编号赋值）
		if(StringUtils.isNotBlank(customerIds)&&!"null".equals(customerIds)){
			String[] idArray = customerIds.split(",");
			for (String id : idArray) {
				customerRepository.findOne(Integer.parseInt(id))
				.setFixedAreaId(fixedAreaId);
				//等flush
			}
		}

	}

	@Override
	public void saveCustomer(Customer customer) {
		customerRepository.save(customer);
	}

	@Override
	public void updateTypeByTelephone(String telephone, Integer type) {
		//更新
		customerRepository.updateTypeByTelephone(telephone, type);
		
	}

	@Override
	public Customer findFixedAreaIdByAddress(String address) {
		List<Customer> customerList = customerRepository.findByAddress(address);
		//重新组装数据
		if(customerList!=null&&!customerList.isEmpty()){
			Customer customer=new Customer();
			customer.setFixedAreaId(customerList.get(0).getFixedAreaId());
			
			return customer;
		}
		
		/*String fixedAreaId = customerRepository.findFixedAreaIdByAddress(address);
		//重新组装数据
		if(StringUtils.isNotBlank(fixedAreaId)){
			Customer customer=new Customer();
			customer.setFixedAreaId(fixedAreaId);
			
			return customer;
		}*/
				
		return null;
	}

	@Override
	public PageBean<Customer> findCustomerListPage(int page, int rows) {
		//请求的分页bean对象
		Pageable pageable=new PageRequest(page, rows);
		//查询分页数据
		Page<Customer> pageResponse = customerRepository.findAll(pageable);
		//重新封装到DTO中
		PageBean<Customer> pageBean=new PageBean<>();
		pageBean.setTotalCount(pageResponse.getTotalElements());
		pageBean.setPageData(pageResponse.getContent());
		return pageBean;

	}

}
