package cn.bohoon.company.entity;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import cn.bohoon.company.domain.CreditType;
/*
 * 信用记录
 */
@Entity
@Table(name = "t_credit")
public class Credit {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Integer id;                                    //主键
	private String companyId;                             //企业id 
	@Enumerated(EnumType.STRING)
	private CreditType creditType;                         //类型
	private BigDecimal creditOver = new BigDecimal(0) ;    //信用余额
	private BigDecimal userCredit = new BigDecimal(0);     //信用操作记录
	public BigDecimal getUserCredit() {
		return userCredit;
	}
	public void setUserCredit(BigDecimal userCredit) {
		this.userCredit = userCredit;
	}
	private Date correlationTime;                          //相关时间
	private String particular;                             //详细说明
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getCompanyId() {
		return companyId;
	}
	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}
	public CreditType getCreditType() {
		return creditType;
	}
	public void setCreditType(CreditType creditType) {
		this.creditType = creditType;
	}
	public BigDecimal getCreditOver() {
		return creditOver;
	}
	public void setCreditOver(BigDecimal creditOver) {
		this.creditOver = creditOver;
	}
	public Date getCorrelationTime() {
		return correlationTime;
	}
	public void setCorrelationTime(Date correlationTime) {
		this.correlationTime = correlationTime;
	}
	public String getParticular() {
		return particular;
	}
	public void setParticular(String particular) {
		this.particular = particular;
	}
}
