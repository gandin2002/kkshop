package cn.bohoon.company.domain;

import cn.bohoon.company.entity.Company;
import cn.bohoon.company.entity.License;
import cn.bohoon.userInfo.entity.UserInfo;

/**
 * 添加企业信息
 */
public class CompanyModel {
    
    private UserInfo userinfo;
//    private Invoice invoice;
    private License license;
    private Company company ;
    
	public UserInfo getUserinfo() {
		return userinfo;
	}
	public void setUserinfo(UserInfo userinfo) {
		this.userinfo = userinfo;
	}
//	public Invoice getInvoice() {
//		return invoice;
//	}
//	public void setInvoice(Invoice invoice) {
//		this.invoice = invoice;
//	}
	public License getLicense() {
		return license;
	}
	public void setLicense(License license) {
		this.license = license;
	}
	public Company getCompany() {
		return company;
	}
	public void setCompany(Company company) {
		this.company = company;
	}
    
    
	
}
