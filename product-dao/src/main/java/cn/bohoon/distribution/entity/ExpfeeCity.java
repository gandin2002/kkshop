package cn.bohoon.distribution.entity;

import java.math.BigDecimal;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 按区域/货品计算运费模板明细信息
 *
 */
@Entity
@Table(name = "t_expfee_city")
public class ExpfeeCity {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	Integer id; // 主键
	Integer efId; // 模板ID
	String cIds; // 区域ID

	BigDecimal fquality = new BigDecimal(0); // 首重
	BigDecimal fqFee = new BigDecimal(0); // 首重费用
	BigDecimal conHeavy = new BigDecimal(0); // 续重
	BigDecimal conFee = new BigDecimal(0); // 续重费用

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

	public String getcIds() {
		return cIds;
	}

	public void setcIds(String cIds) {
		this.cIds = cIds;
	}

	public BigDecimal getFquality() {
		return fquality;
	}

	public void setFquality(BigDecimal fquality) {
		this.fquality = fquality;
	}

	public BigDecimal getFqFee() {
		return fqFee;
	}

	public void setFqFee(BigDecimal fqFee) {
		this.fqFee = fqFee;
	}

	public BigDecimal getConHeavy() {
		return conHeavy;
	}

	public void setConHeavy(BigDecimal conHeavy) {
		this.conHeavy = conHeavy;
	}

	public BigDecimal getConFee() {
		return conFee;
	}

	public void setConFee(BigDecimal conFee) {
		this.conFee = conFee;
	}

}
