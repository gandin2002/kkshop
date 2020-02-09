package cn.bohoon.syn.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 用户同步表
 * @author HJ
 * 2018年4月8日,上午9:12:50
 */
@Entity
@Table(name = "t_syn_user_info")
public class SynUserInfo implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	String orgId;
	@Id
	String bizPartnerId;
	@Id
	String rowCode;
	
	Integer rowNo;
	
	String isMain;
	String linkMan;
	String sex;
	String eMail;
	String telNo;
	String mobile;

	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	public String getBizPartnerId() {
		return bizPartnerId;
	}

	public void setBizPartnerId(String bizPartnerId) {
		this.bizPartnerId = bizPartnerId;
	}

	public String getRowCode() {
		return rowCode;
	}

	public void setRowCode(String rowCode) {
		this.rowCode = rowCode;
	}

	public String getIsMain() {
		return isMain;
	}

	public void setIsMain(String isMain) {
		this.isMain = isMain;
	}

	public String getLinkMan() {
		return linkMan;
	}

	public void setLinkMan(String linkMan) {
		this.linkMan = linkMan;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String geteMail() {
		return eMail;
	}

	public void seteMail(String eMail) {
		this.eMail = eMail;
	}

	public String getTelNo() {
		return telNo;
	}

	public void setTelNo(String telNo) {
		this.telNo = telNo;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public Integer getRowNo() {
		return rowNo;
	}

	public void setRowNo(Integer rowNo) {
		this.rowNo = rowNo;
	}
}
