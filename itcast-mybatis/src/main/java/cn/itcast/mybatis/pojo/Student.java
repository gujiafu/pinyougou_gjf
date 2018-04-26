package cn.itcast.mybatis.pojo;

import java.io.Serializable;
import java.util.Date;

public class Student implements Serializable {

	private static final long serialVersionUID = -7644967201570973657L;
	private Long id;
	private String account;
	private String password;
	private String name;
	private Integer gender;
	private Integer age;
	private Date birthday;
	private Date createTime;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getAccount() {
		return account;
	}
	public void setAccount(String account) {
		this.account = account;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getGender() {
		return gender;
	}
	public void setGender(Integer gender) {
		this.gender = gender;
	}
	public Integer getAge() {
		return age;
	}
	public void setAge(Integer age) {
		this.age = age;
	}
	public Date getBirthday() {
		return birthday;
	}
	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	@Override
	public String toString() {
		return "Student [id=" + id + ", account=" + account + ", password=" + password + ", name=" + name + ", gender=" + gender + ", age=" + age + ", birthday=" + birthday + ", createTime="
				+ createTime + "]";
	}
	
}
