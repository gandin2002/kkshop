package cn.bohoon.order.entity;

import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "t_order_rate")
public class OrderRate {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Integer id;
	Long transNum = 0l;
	BigDecimal amount = new BigDecimal(0);
	String dat = "";

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Long getTransNum() {
		return transNum;
	}

	public void setTransNum(Long transNum) {
		this.transNum = transNum;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public String getDat() {
		return dat;
	}

	public void setDat(String dat) {
		this.dat = dat;
	}

}
