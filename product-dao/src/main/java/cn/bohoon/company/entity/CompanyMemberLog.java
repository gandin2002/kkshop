package cn.bohoon.company.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name = "t_company_member_log")
public class CompanyMemberLog {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;
	private String note; //记录
	private String userId; //用户id ;
	private String companyId; //企业id 
	@Column(columnDefinition="tinyint(2)")
	private Boolean binding;//绑定 true 绑定 ，false 解绑
	private Date createDate; //创建时间
	
 
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getCompanyId() {
		return companyId;
	}
	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public Boolean getBinding() {
		return binding;
	}
	public void setBinding(Boolean binding) {
		this.binding = binding;
	}
}
