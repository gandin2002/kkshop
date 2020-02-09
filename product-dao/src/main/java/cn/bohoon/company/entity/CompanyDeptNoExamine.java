package cn.bohoon.company.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 企业部门免审
 * @author Administrator
 *
 */

@Entity
@Table(name = "t_company_department_noExamine")
public class CompanyDeptNoExamine {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Integer id;
	Integer departmentId;    // 部门id
	@Column(length=64)	
	String userInfoId;		//  用户id
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getDepartmentId() {
		return departmentId;
	}
	public void setDepartmentId(Integer departmentId) {
		this.departmentId = departmentId;
	}
	public String getUserInfoId() {
		return userInfoId;
	}
	public void setUserInfoId(String userInfoId) {
		this.userInfoId = userInfoId;
	}
	
	
	
	
}
