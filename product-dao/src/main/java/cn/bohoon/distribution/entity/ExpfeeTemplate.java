package cn.bohoon.distribution.entity;

import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import cn.bohoon.distribution.domain.ValuationEnum;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * 快递费用模板
 *
 */
@Entity
@Table(name = "t_expfee_template")
public class ExpfeeTemplate {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	Integer id; // 主键
	String name; // 模板名称
	Integer calcTye;// 0 按订单金额计算  1 按城市运送模板 计算运费 快递费计算方式
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	Date createTime; // 创建时间
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	Date updateTime; // 修改
	@Column(columnDefinition="tinyint(2)")
	Boolean isDefault; // 是否默认模板
	@Column(columnDefinition="tinyint(2)")
	Boolean isOpen ; //是否开启默认
	@Enumerated(EnumType.STRING)
	ValuationEnum valuationEnum;// 计价方式
	BigDecimal coefficient =new BigDecimal(0);//体积体重系数
	BigDecimal defaultFee = new BigDecimal(15) ; //默认费用
	BigDecimal fquality= new BigDecimal(0); // 首重
	BigDecimal fqFee = new BigDecimal(0); // 首重费用
	BigDecimal conHeavy= new BigDecimal(0); // 续重
	BigDecimal conFee = new BigDecimal(0); // 续重费用

	public Integer getId() {
		return id;
	}

	public BigDecimal getCoefficient() {
		return coefficient;
	}

	public void setCoefficient(BigDecimal coefficient) {
		this.coefficient = coefficient;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getCalcTye() {
		return calcTye;
	}

	public void setCalcTye(Integer calcTye) {
		this.calcTye = calcTye;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public Boolean getIsDefault() {
		return isDefault;
	}

	public void setIsDefault(Boolean isDefault) {
		this.isDefault = isDefault;
	}

	public Boolean getIsOpen() {
		return isOpen;
	}

	public void setIsOpen(Boolean isOpen) {
		this.isOpen = isOpen;
	}

	public ValuationEnum getValuationEnum() {
		return valuationEnum;
	}

	public void setValuationEnum(ValuationEnum valuationEnum) {
		this.valuationEnum = valuationEnum;
	}

	public BigDecimal getFquality() {
		return fquality;
	}

	public void setFquality(BigDecimal fquality) {
		this.fquality = fquality;
	}
	
	public BigDecimal getDefaultFee() {
		return defaultFee;
	}

	public void setDefaultFee(BigDecimal defaultFee) {
		this.defaultFee = defaultFee;
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
