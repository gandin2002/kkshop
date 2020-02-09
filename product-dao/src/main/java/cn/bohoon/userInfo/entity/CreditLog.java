package cn.bohoon.userInfo.entity;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import cn.bohoon.userInfo.domain.CreditLogType;
/**
 * 信用日志
 * @author hj
 * 2017年11月9日,上午10:23:59
 */
@Entity
@Table(name="t_credit_log")
public class CreditLog {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	Integer id;
	
	@Column(length=64)
	String userId;
	Date createDate;
	String detail;
	@Enumerated(EnumType.STRING)
	CreditLogType creditLogType;
	BigDecimal money;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public String getDetail() {
		return detail;
	}
	public void setDetail(String detail) {
		this.detail = detail;
	}
	public CreditLogType getCreditLogType() {
		return creditLogType;
	}
	public void setCreditLogType(CreditLogType creditLogType) {
		this.creditLogType = creditLogType;
	}
	public BigDecimal getMoney() {
		return money;
	}
	public void setMoney(BigDecimal money) {
		this.money = money;
	}
}
