package cn.bohoon.userInfo.entity;

import java.math.BigDecimal;
import java.util.Date;

import cn.bohoon.excel.util.ExportConfig;

/**
 * 用户Excel导出模板
 * @author Administrator
 *
 */

public class UserInfoExcelModel {
	@ExportConfig(name = "真实姓名")
	String realname = ""; // 真实姓名
	@ExportConfig(name = "会员号")
	String id;  
	@ExportConfig(name = "部门名称")
	String departmentName;// 部门名称
	@ExportConfig(name = "信用限额")
	BigDecimal creaditAmount = new BigDecimal(0);   // 信用限额
	@ExportConfig(name = "部门负责人")
	Integer personInCharge = 0 ; // 部门负责人 (true)或禁用(false)
	@ExportConfig(name = "免审状态")
	Integer reviewStatus = 0 ; // 免审状态 (true)或禁用(false)
	@ExportConfig(name = "启用")
	Integer status = 0; // 启用(true)或禁用(false)
	@ExportConfig(name = "完成时间")
	Date bindingDate;// 绑定时间
	
	public void setParam(UserInfo userInfo,String departmentName){
		
		this.id = userInfo.id;
		this.realname = userInfo.realname;
		this.departmentName = departmentName;
		this.creaditAmount = userInfo.getCreaditAmount();
		this.personInCharge = userInfo.getPersonInCharge() == true ? 1 : 0;
		this.reviewStatus = userInfo.getReviewStatus() == true ? 1 : 0;
		this.status = userInfo.getStatus() == true ? 1 : 0;
		this.bindingDate = userInfo.getBindingDate();
	}

	public String getRealname() {
		return realname;
	}
	public void setRealname(String realname) {
		this.realname = realname;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	public String getDepartmentName() {
		return departmentName;
	}

	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}

	public BigDecimal getCreaditAmount() {
		return creaditAmount;
	}
	public void setCreaditAmount(BigDecimal creaditAmount) {
		this.creaditAmount = creaditAmount;
	}

	public Integer getPersonInCharge() {
		return personInCharge;
	}


	public void setPersonInCharge(Integer personInCharge) {
		this.personInCharge = personInCharge;
	}


	public Integer getReviewStatus() {
		return reviewStatus;
	}


	public void setReviewStatus(Integer reviewStatus) {
		this.reviewStatus = reviewStatus;
	}


	public Integer getStatus() {
		return status;
	}


	public void setStatus(Integer status) {
		this.status = status;
	}


	public Date getBindingDate() {
		return bindingDate;
	}
	public void setBindingDate(Date bindingDate) {
		this.bindingDate = bindingDate;
	}
	
	
	
}
