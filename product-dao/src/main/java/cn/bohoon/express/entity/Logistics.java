package cn.bohoon.express.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/*
 * 物流公司
 */
@Entity
@Table(name = "t_logistics")
public class Logistics {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	Integer id;					//主键
	String companyname;         //物流公司名称
	String code;                //物流公司编码
	@Column(columnDefinition=" int default 1")
	Integer status = 1;         //是否启用：1启用0禁用
	@Column(columnDefinition="tinyint(2)")
	Boolean defaultState = false ; 		//默认状态  //true 启用 false 禁用
	String remark;              //备注
	
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getCompanyname() {
		return companyname;
	}
	public void setCompanyname(String companyname) {
		this.companyname = companyname;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public Boolean getDefaultState() {
		return defaultState;
	}
	public void setDefaultState(Boolean defaultState) {
		this.defaultState = defaultState;
	}
}
