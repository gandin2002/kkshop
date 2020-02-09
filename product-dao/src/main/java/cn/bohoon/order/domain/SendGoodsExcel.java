package cn.bohoon.order.domain;

import cn.bohoon.excel.util.ExportConfig;
/**
 * 导出EXCEl 实体
 * @author HJ
 * 2017年12月26日,上午9:43:46
 */
public class SendGoodsExcel {
	
	@ExportConfig(name="发货单号")
	String sendGoodsId;
	
	@ExportConfig(name="订单号")
	String orderId;
	
	@ExportConfig(name="购买会员")
	String userInfoId;
	
	@ExportConfig(name="收件人")
	String consignee;
	
	@ExportConfig(name="收件人电话")
	String consigneePhone;
	
	@ExportConfig(name="物流公司")
	String expressCompany;
	
	@ExportConfig(name="公司名称")
	String companyName;
	
	@ExportConfig(name="发货时间")
	String sendGoodsTime;
	
	@ExportConfig(name="生成时间")
	String createGoodsTime;

	public String getSendGoodsId() {
		return sendGoodsId;
	}

	public void setSendGoodsId(String sendGoodsId) {
		this.sendGoodsId = sendGoodsId;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getUserInfoId() {
		return userInfoId;
	}

	public void setUserInfoId(String userInfoId) {
		this.userInfoId = userInfoId;
	}

	public String getConsignee() {
		return consignee;
	}

	public void setConsignee(String consignee) {
		this.consignee = consignee;
	}

	public String getConsigneePhone() {
		return consigneePhone;
	}

	public void setConsigneePhone(String consigneePhone) {
		this.consigneePhone = consigneePhone;
	}

	public String getExpressCompany() {
		return expressCompany;
	}

	public void setExpressCompany(String expressCompany) {
		this.expressCompany = expressCompany;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getSendGoodsTime() {
		return sendGoodsTime;
	}

	public void setSendGoodsTime(String sendGoodsTime) {
		this.sendGoodsTime = sendGoodsTime;
	}

	public String getCreateGoodsTime() {
		return createGoodsTime;
	}

	public void setCreateGoodsTime(String createGoodsTime) {
		this.createGoodsTime = createGoodsTime;
	}
}
