package cn.bohoon.page.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 页面模板
 * @author djq
 *
 */
@Entity
@Table(name = "t_define_template")
public class DefineTemplate {
	@Id
	String id; // 模板ID
	String name; // 模板名称
	String companyId;// 所属企业
	String companyName; // 所属企业名称
	String mainUrl; // 模板首页url
	Boolean isDefault = false;// 是否默认模板
	Boolean isSystem = false; // 是否系统模板

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCompanyId() {
		return companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getMainUrl() {
		return mainUrl;
	}

	public void setMainUrl(String mainUrl) {
		this.mainUrl = mainUrl;
	}

	public Boolean getIsDefault() {
		return isDefault;
	}

	public void setIsDefault(Boolean isDefault) {
		this.isDefault = isDefault;
	}

	public Boolean getIsSystem() {
		return isSystem;
	}

	public void setIsSystem(Boolean isSystem) {
		this.isSystem = isSystem;
	}

}
