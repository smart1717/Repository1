package cn.itcast.bos.domain.page;

import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;

import org.apache.poi.ss.formula.functions.T;

import cn.itcast.bos.domain.take_delivery.Promotion;

//分页的DTO对象，用来封装分页结果和WebService传递的对象的类型
@XmlRootElement(name="pageBean")//REST资源标识
@XmlSeeAlso({Promotion.class})//泛型类型如果找不到，则到这里找类型
public class PageBean<T> {
	private long totalCount;// 总记录数
	private List<T> pageData;// 当前页的数据

	public long getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(long totalCount) {
		this.totalCount = totalCount;
	}

	public List<T> getPageData() {
		return pageData;
	}

	public void setPageData(List<T> pageData) {
		this.pageData = pageData;
	}

}
