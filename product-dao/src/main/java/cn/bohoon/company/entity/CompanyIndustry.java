package cn.bohoon.company.entity;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * 企业行业
 * 
 * @author Administrator
 */
@Entity
@Table(name = "t_company_industry")
public class CompanyIndustry {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Integer id ;//企业行业ID
	private String name ; //企业行业名称
	private Integer pid ; //行业父ID
	@Column(columnDefinition="tinyint(2)")
	private Boolean status ; //启用状态
	private Integer sort ; //行业排序
	@Column(columnDefinition = "datetime(0)")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date createTime; // 创建时间
	
	
	
	public Integer getId() {
		return id;
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
	public Integer getPid() {
		return pid;
	}
	public void setPid(Integer pid) {
		this.pid = pid;
	}
	public Boolean getStatus() {
		return status;
	}
	public void setStatus(Boolean status) {
		this.status = status;
	}
	public Integer getSort() {
		return sort;
	}
	public void setSort(Integer sort) {
		this.sort = sort;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
}
