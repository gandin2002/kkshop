package cn.bohoon.distribution.entity;

import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "t_expfee_order")
public class ExpfeeOrder {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	Integer id; // 主键
	Integer efId; // 模板ID
	String userLevel; // 用户等级ID
	BigDecimal orderFee = new BigDecimal(0); // 订单费用
	BigDecimal expFee = new BigDecimal(0); // 快递费用

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getEfId() {
		return efId;
	}

	public void setEfId(Integer efId) {
		this.efId = efId;
	}

	public String getUserLevel() {
		return userLevel;
	}

	public void setUserLevel(String userLevel) {
		this.userLevel = userLevel;
	}

	public BigDecimal getOrderFee() {
		return orderFee;
	}

	public void setOrderFee(BigDecimal orderFee) {
		this.orderFee = orderFee;
	}

	public BigDecimal getExpFee() {
		return expFee;
	}

	public void setExpFee(BigDecimal expFee) {
		this.expFee = expFee;
	}

}
