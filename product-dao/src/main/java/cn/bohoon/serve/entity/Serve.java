package cn.bohoon.serve.entity;
/*
 * 售后服务说明
 */

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
@Entity
@Table(name = "t_serve")
public class Serve {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	Integer id;               //id主键
	String saleType;          //服务类型
	String saleExplain;       //服务说明
	Integer state=0;          //启用状态       0 未启用   1启用
 	Date createTime;          //创建时间
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getSaleType() {
		return saleType;
	}
	public void setSaleType(String saleType) {
		this.saleType = saleType;
	}
	public String getSaleExplain() {
		return saleExplain;
	}
	public void setSaleExplain(String saleExplain) {
		this.saleExplain = saleExplain;
	}
	
	public Integer getState() {
		return state;
	}
	public void setState(Integer state) {
		this.state = state;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	
}
