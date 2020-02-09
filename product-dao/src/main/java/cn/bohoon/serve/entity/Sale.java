package cn.bohoon.serve.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import cn.bohoon.serve.domain.SaleType;

/*
 * 售后原因
 */
@Entity
@Table(name = "t_sale")
public class Sale {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	Integer id;                  //主键
	@Enumerated(EnumType.STRING)
	SaleType saleType;           //售后类型
	String saleCause;            //售后原因
	@Column(columnDefinition="tinyint(2)")
	Boolean state= false;        //启用状态     0 false未启用   1 true启用
	Date createTime;             //创建时间
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public SaleType getSaleType() {
		return saleType;
	}
	public void setSaleType(SaleType saleType) {
		this.saleType = saleType;
	}
	public String getSaleCause() {
		return saleCause;
	}
	public void setSaleCause(String saleCause) {
		this.saleCause = saleCause;
	}
	public Boolean getState() {
		return state;
	}
	public void setState(Boolean state) {
		this.state = state;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
}
