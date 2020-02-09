package cn.bohoon.company.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import org.springframework.format.annotation.DateTimeFormat;
/*
 * 企业等级
 */
@Entity
@Table(name = "t_company_level")
public class CompanyLevel {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Integer id ;//企业级别ID
	private String name ; //企业级别名称
	private String alias ; //企业等级显示
	private Float discount ;//折扣率
	@Column(columnDefinition="tinyint(2)")
	private Boolean status ; //启用状态
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
	public String getAlias() {
		return alias;
	}
	public void setAlias(String alias) {
		this.alias = alias;
	}
	public Float getDiscount() {
		return discount;
	}
	public void setDiscount(Float discount) {
		this.discount = discount;
	}
	public Boolean getStatus() {
		return status;
	}
	public void setStatus(Boolean status) {
		this.status = status;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
 
	
	
}
