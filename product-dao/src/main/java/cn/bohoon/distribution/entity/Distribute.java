package cn.bohoon.distribution.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
/*
 * 配送方式
 */
@Entity
@Table(name = "t_distribute")
public class Distribute {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	Integer id;               //主键
	String typeName;         //配送类型名称
	String deliveryWay;       //配送方式
	String description;       //说明描述
	@Column(columnDefinition="tinyint(2)")
	Boolean isPC;             //是否启用PC
	@Column(columnDefinition="tinyint(2)")
	Boolean isWap;            //是否启用微信 
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getTypeName() {
		return typeName;
	}
	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}
	public String getDeliveryWay() {
		return deliveryWay;
	}
	public void setDeliveryWay(String deliveryWay) {
		this.deliveryWay = deliveryWay;
	}
	
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Boolean getIsPC() {
		return isPC;
	}
	public void setIsPC(Boolean isPC) {
		this.isPC = isPC;
	}
	public Boolean getIsWap() {
		return isWap;
	}
	public void setIsWap(Boolean isWap) {
		this.isWap = isWap;
	}
}
