package cn.itcast.bos.domain.system;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.apache.struts2.json.annotations.JSON;

/**
 * @description:后台用户
 */
@Entity
@Table(name = "T_USER")
public class User implements Serializable{
	private static final long serialVersionUID = 7833155410575077923L;
	
	@Id
	@GeneratedValue
	@Column(name = "C_ID")
	private Integer id; // 主键
	@Column(name = "C_BIRTHDAY")
	private Date birthday; // 生日
	@Column(name = "C_GENDER")
	private String gender; // 性别
	@Column(name = "C_PASSWORD")
	private String password; // 密码
	@Column(name = "C_REMARK")
	private String remark; // 备注
	@Column(name = "C_STATION")
	private String station; // 状态
	@Column(name = "C_TELEPHONE")
	private String telephone; // 联系电话
	@Column(name = "C_USERNAME", unique = true)
	private String username; // 登陆用户名
	@Column(name = "C_NICKNAME")
	private String nickname; // 真实姓名
	//添加两个属性，并提供setter和getter
	@Column(length=1)
	private String status="1";//用户状态,默认是1，正常，0过期
	//jpa的日期相关张家界
	@Temporal(TemporalType.TIMESTAMP)
	private Date activetime ;//激活时间

	public String getStatus() {
			return status;
		}

		public void setStatus(String status) {
			this.status = status;
		}

		public Date getActivetime() {
			return activetime;
		}

		public void setActivetime(Date activetime) {
			this.activetime = activetime;
		}

	@ManyToMany
	@JoinTable(name = "T_USER_ROLE", joinColumns = {
			@JoinColumn(name = "C_USER_ID", referencedColumnName = "C_ID") }, inverseJoinColumns = {
					@JoinColumn(name = "C_ROLE_ID", referencedColumnName = "C_ID") })
	private Set<Role> roles = new HashSet<Role>(0);

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	//json插件会调用getter方法，将结果转为json
	@JSON(format="yyyy-MM-dd")//指定要格式化日期为字符串的格式
	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getStation() {
		return station;
	}

	public void setStation(String station) {
		this.station = station;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public Set<Role> getRoles() {
		return roles;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}

}
