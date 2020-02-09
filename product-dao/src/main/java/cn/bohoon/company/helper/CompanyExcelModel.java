package cn.bohoon.company.helper;

import cn.bohoon.excel.util.ExportConfig;

/**
 * 企业导入导出模板
 * @author Administrator
 *
 */

public class CompanyExcelModel {
	
	@ExportConfig(name = "企业名称")
	private String name;// 商品编码
	@ExportConfig(name = "负责人手机号码")
	private String phone;// 负责人手机号码
	@ExportConfig(name = "负责人真实姓名")
	private String realName;// 负责人真实姓名
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getRealName() {
		return realName;
	}
	public void setRealName(String realName) {
		this.realName = realName;
	}
	
	
	

}
