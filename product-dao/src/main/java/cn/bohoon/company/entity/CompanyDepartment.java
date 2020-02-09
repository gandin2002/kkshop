package cn.bohoon.company.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.springframework.util.StringUtils;

import cn.bohoon.company.service.CompanyDepartmentService;
import cn.bohoon.framework.SpringContextHolder;
import cn.bohoon.userInfo.entity.UserInfo;
import cn.bohoon.userInfo.service.UserInfoService;


/**
 * 企业部门表
 * @author Administrator
 *
 */

@Entity
@Table(name = "t_company_department" )
public class CompanyDepartment {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Integer id;
	String deptCode; // 部门编码
	String title; // 部门名称
	@Column(columnDefinition="tinyint(2)")
	Boolean state; // 部门状态 (true 启用 false 禁用)
	String responsiblePerson; // 部门责任人
	String responsibleId; // 部门负责人ID
	Date createDate = new Date(); // 创建时间
	Integer level; // 部门等级
	Integer pid; // 父id
	String companyId; // 公司Id;

	@Transient
	public Integer getNextLevel() {
		return this.level + 1;
	}

	@Transient
	public String getRespons() {
		UserInfoService service = SpringContextHolder.getBean(UserInfoService.class) ;
		
//		String respons = service.getResponsNames(id) ;
		
		if (StringUtils.isEmpty(responsibleId)){
			
			return "";
		}
		
		//CompanyDepartmentService  departmentService=SpringContextHolder.getBean(CompanyDepartmentService.class);
	    String responsNames = service.getResponsNames(id);
	    if(!StringUtils.isEmpty(responsNames)){
	    	return  responsNames;
	    }
		UserInfo userInfo = service.get(responsibleId);
		if (null == userInfo)
		return null;
		
		return userInfo.getRealname();
	}
	
	
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDeptCode() {
		return deptCode;
	}

	public void setDeptCode(String deptCode) {
		this.deptCode = deptCode;
	}

	public Boolean getState() {
		return state;
	}

	public void setState(Boolean state) {
		this.state = state;
	}

	public String getResponsiblePerson() {
		return responsiblePerson;
	}

	public void setResponsiblePerson(String responsiblePerson) {
		this.responsiblePerson = responsiblePerson;
	}

	public String getResponsibleId() {
		return responsibleId;
	}

	public void setResponsibleId(String responsibleId) {
		this.responsibleId = responsibleId;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}

	public Integer getPid() {
		return pid;
	}

	public void setPid(Integer pid) {
		this.pid = pid;
	}

	public String getCompanyId() {
		return companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

}
