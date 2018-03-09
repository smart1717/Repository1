package cn.itcast.bos.domain.take_delivery;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

import cn.itcast.utils.Constants;

/**
 * @description:促销信息实体类
 */
@Entity
@Table(name = "T_PROMOTION")
@XmlRootElement//REST资源标识
public class Promotion implements Serializable {
	private static final long serialVersionUID = 8624929100821623835L;
	@Id
	@GeneratedValue
	@Column(name = "C_ID")
	private Integer id;
	@Column(name = "C_TITLE")
	private String title; // 宣传概要(标题)
	@Column(name = "C_TITLE_IMG")
	private String titleImg; // 宣传图片的地址
	@Column(name = "C_ACTIVE_SCOPE")
	private String activeScope;// 活动范围
	@Column(name = "C_START_DATE")
	private Date startDate; // 发布时间
	@Column(name = "C_END_DATE")
	private Date endDate; // 失效时间
	@Column(name = "C_UPDATE_TIME")
	private Date updateTime; // 更新时间
	@Column(name = "C_UPDATE_UNIT")
	private String updateUnit; // 更新单位
	@Column(name = "C_UPDATE_USER")
	private String updateUser;// 更新人 后续与后台用户关联
	@Column(name = "C_STATUS")
	private String status; // 状态 可取值：1.进行中 2. 已结束
	@Column(name = "C_DESCRIPTION")
	private String description; // 宣传内容(活动描述信息)

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getTitleImg() {
		//url路径修复
		if(titleImg.startsWith("http")){
			//如果是绝对路径，则直接返回
			return titleImg;
		}
		//如果是相对路径，则修复为绝对路径
		return Constants.BOS_MANAGEMENT_HOST+titleImg;
	}

	public void setTitleImg(String titleImg) {
		this.titleImg = titleImg;
	}

	public String getActiveScope() {
		return activeScope;
	}

	public void setActiveScope(String activeScope) {
		this.activeScope = activeScope;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public String getUpdateUnit() {
		return updateUnit;
	}

	public void setUpdateUnit(String updateUnit) {
		this.updateUnit = updateUnit;
	}

	public String getUpdateUser() {
		return updateUser;
	}

	public void setUpdateUser(String updateUser) {
		this.updateUser = updateUser;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getDescription() {
		//修复url
		//如果描述是空的，或者里面包含的是绝对路径，则不替换，直接返回
		if(description==null||description.contains( Constants.BOS_MANAGEMENT_URL)){
			return description;
		}
		return description.replace(Constants.BOS_MANAGEMENT_CONTEXT, Constants.BOS_MANAGEMENT_URL);
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
