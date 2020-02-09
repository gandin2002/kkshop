package cn.bohoon.userInfo.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 用户增长率
 * 
 * @author djq
 *
 */
@Entity
@Table(name = "t_user_rate")
public class UserInRate {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Integer id;
	Long users;
	Double rate;
	String datDay;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Long getUsers() {
		return users;
	}

	public void setUsers(Long users) {
		this.users = users;
	}

	public Double getRate() {
		return rate;
	}

	public void setRate(Double rate) {
		this.rate = rate;
	}

	public String getDatDay() {
		return datDay;
	}

	public void setDatDay(String datDay) {
		this.datDay = datDay;
	}

}
