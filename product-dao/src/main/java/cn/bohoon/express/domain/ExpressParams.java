package cn.bohoon.express.domain;
/**
 * 2017年11月7日13:56:22
 * @author Administrator
 * Express模板
 */
public class ExpressParams {
	
	private String senderUnitName;  //寄件人单位名称
	private String senderName;      //寄件人姓名
	private String senderAddress;   // 寄件人地址
	private String senderPhone;     //寄件人电话
	private String senderLandline;  //寄件人座机
	private String senderCode;      //寄件人邮编
	private String originatingSite; //始发地
	private String issueTime;         //发件时间
	private String consigneeCompany;  //收件人单位
	private String consigneeName;   // 收件人姓名
	private String consigneeAdress; //收件人地址
	private String consigneePhone;  //收件人电话
	private String consigneeLandline; //收件人固话
	private String landlineCode;    //收件人邮编
	private String destination;     //目的地
	private String remarks;         //备注
	private String commodityName;   //货品名称
	private String commodityCode;   //货品编码
	private String count;           //总数
	private String orderNumber;     //订单号
	private String barCode ;//条形码
	private String userDefined1; //用户自定义1
	private String userDefined2; //用户自定义2
	private String userDefined3; //用户自定义3
	private String userDefined4; //用户自定义4
	
	public ExpressParams() {
	}
	public ExpressParams(String senderUnitName, String senderName, String senderAddress, String senderPhone,
			String senderLandline, String senderCode, String originatingSite, String issueTime, String consigneeCompany,
			String consigneeName, String consigneeAdress, String consigneePhone, String consigneeLandline,
			String landlineCode, String destination, String remarks, String commodityName, String commodityCode,
			String count, String orderNumber, String barCode, String userDefined1, String userDefined2,
			String userDefined3, String userDefined4) {
		this.senderUnitName = senderUnitName;
		this.senderName = senderName;
		this.senderAddress = senderAddress;
		this.senderPhone = senderPhone;
		this.senderLandline = senderLandline;
		this.senderCode = senderCode;
		this.originatingSite = originatingSite;
		this.issueTime = issueTime;
		this.consigneeCompany = consigneeCompany;
		this.consigneeName = consigneeName;
		this.consigneeAdress = consigneeAdress;
		this.consigneePhone = consigneePhone;
		this.consigneeLandline = consigneeLandline;
		this.landlineCode = landlineCode;
		this.destination = destination;
		this.remarks = remarks;
		this.commodityName = commodityName;
		this.commodityCode = commodityCode;
		this.count = count;
		this.orderNumber = orderNumber;
		this.barCode = barCode;
		this.userDefined1 = userDefined1;
		this.userDefined2 = userDefined2;
		this.userDefined3 = userDefined3;
		this.userDefined4 = userDefined4;
	}
	public String getSenderUnitName() {
		return senderUnitName;
	}
	public void setSenderUnitName(String senderUnitName) {
		this.senderUnitName = senderUnitName;
	}
	public String getSenderName() {
		return senderName;
	}
	public void setSenderName(String senderName) {
		this.senderName = senderName;
	}
	public String getSenderAddress() {
		return senderAddress;
	}
	public void setSenderAddress(String senderAddress) {
		this.senderAddress = senderAddress;
	}
	public String getSenderPhone() {
		return senderPhone;
	}
	public void setSenderPhone(String senderPhone) {
		this.senderPhone = senderPhone;
	}
	public String getSenderLandline() {
		return senderLandline;
	}
	public void setSenderLandline(String senderLandline) {
		this.senderLandline = senderLandline;
	}
	public String getSenderCode() {
		return senderCode;
	}
	public void setSenderCode(String senderCode) {
		this.senderCode = senderCode;
	}
	public String getOriginatingSite() {
		return originatingSite;
	}
	public void setOriginatingSite(String originatingSite) {
		this.originatingSite = originatingSite;
	}
	public String getIssueTime() {
		return issueTime;
	}
	public void setIssueTime(String issueTime) {
		this.issueTime = issueTime;
	}
	public String getConsigneeCompany() {
		return consigneeCompany;
	}
	public void setConsigneeCompany(String consigneeCompany) {
		this.consigneeCompany = consigneeCompany;
	}
	public String getConsigneeName() {
		return consigneeName;
	}
	public void setConsigneeName(String consigneeName) {
		this.consigneeName = consigneeName;
	}
	public String getConsigneeAdress() {
		return consigneeAdress;
	}
	public void setConsigneeAdress(String consigneeAdress) {
		this.consigneeAdress = consigneeAdress;
	}
	public String getConsigneePhone() {
		return consigneePhone;
	}
	public void setConsigneePhone(String consigneePhone) {
		this.consigneePhone = consigneePhone;
	}
	public String getConsigneeLandline() {
		return consigneeLandline;
	}
	public void setConsigneeLandline(String consigneeLandline) {
		this.consigneeLandline = consigneeLandline;
	}
	public String getLandlineCode() {
		return landlineCode;
	}
	public void setLandlineCode(String landlineCode) {
		this.landlineCode = landlineCode;
	}
	public String getDestination() {
		return destination;
	}
	public void setDestination(String destination) {
		this.destination = destination;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	public String getCommodityName() {
		return commodityName;
	}
	public void setCommodityName(String commodityName) {
		this.commodityName = commodityName;
	}
	public String getCommodityCode() {
		return commodityCode;
	}
	public void setCommodityCode(String commodityCode) {
		this.commodityCode = commodityCode;
	}
	public String getCount() {
		return count;
	}
	public void setCount(String count) {
		this.count = count;
	}
	public String getOrderNumber() {
		return orderNumber;
	}
	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}
	public String getBarCode() {
		return barCode;
	}
	public void setBarCode(String barCode) {
		this.barCode = barCode;
	}
	public String getUserDefined1() {
		return userDefined1;
	}
	public void setUserDefined1(String userDefined1) {
		this.userDefined1 = userDefined1;
	}
	public String getUserDefined2() {
		return userDefined2;
	}
	public void setUserDefined2(String userDefined2) {
		this.userDefined2 = userDefined2;
	}
	public String getUserDefined3() {
		return userDefined3;
	}
	public void setUserDefined3(String userDefined3) {
		this.userDefined3 = userDefined3;
	}
	public String getUserDefined4() {
		return userDefined4;
	}
	public void setUserDefined4(String userDefined4) {
		this.userDefined4 = userDefined4;
	}
}
