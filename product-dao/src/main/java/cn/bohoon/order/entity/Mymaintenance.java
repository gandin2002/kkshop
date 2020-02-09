package cn.bohoon.order.entity;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.format.annotation.DateTimeFormat;
@Entity
@Table(name = "t_Mymaintenance")
public class Mymaintenance {
	@Id
	@Column(length=64)
	String id=""; // 维保订单ID
	
	@Column(length=64)
	String orderId="";// 订单ID
	String userId="";  //维保人Id
	String  userName="";  //会员名
	String memberId=""; // 企业ID
	String company=""; // 公司名称
	String  realName=""; //维保联系人姓名
	String ipohe="";  //维保人电话
	/* 省区 */
	String province;
	/* 城市 */
	String city;
	/* 地区 */
	String county;
	String addre;   //地址
	String addreCompany;//地址组合
	String  companyDepartment=""; //部门
	String   maintennanceItem="";  //维保项目

	String  useTime=""; //使用年限
	String  usegoods="" ;//使用号品
	String   useBrand=""; //耗品品牌
	@Column(columnDefinition="text")
	String   maintennanceDesc;  //维保描述
	
	String   startTime=""; //预约时间
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss" )
	Date  submitTime=new Date() ; //提交申请时间
	String image1 = ""; // 图片凭证1
	String image2 = ""; // 图片凭证2
	String image3 = ""; // 图片凭证3
	Integer state=0;  //维保状态  0 为处理  1处理
    String  handlerResult; //处理结果
	
	String   finishTime=""; //完成提交时间
	String   ViliTime="";//维保有效期到
    Integer  submitCount=0; //提交服务次数
    Integer   finishCount=0;//完成服务 次数
    
	String  handlePeople=""; //处理人
	
    Integer   quxiao=1;  //前端取消 1 上加  2  后台确让取消  3 
  
    
   
    
	public String getHandlerResult() {
		return handlerResult;
	}
	public void setHandlerResult(String handlerResult) {
		this.handlerResult = handlerResult;
	}
	public String getViliTime() {
		return ViliTime;
	}
	public void setViliTime(String viliTime) {
		ViliTime = viliTime;
	}
	public Integer getQuxiao() {
		return quxiao;
	}
	public void setQuxiao(Integer quxiao) {
		this.quxiao = quxiao;
	}
	
	public String getFinishTime() {
		return finishTime;
	}
	public void setFinishTime(String finishTime) {
		this.finishTime = finishTime;
	}
	public void setHandlePeople(String handlePeople) {
		this.handlePeople = handlePeople;
	}
	public String getHandlePeople() {
		return handlePeople;
	}
	public void setState(Integer state) {
		this.state = state;
	}
	public Integer getState() {
		return state;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getMemberId() {
		return memberId;
	}
	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}
	public String getCompany() {
		return company;
	}
	public void setCompany(String company) {
		this.company = company;
	}
	public String getRealName() {
		return realName;
	}
	public void setRealName(String realName) {
		this.realName = realName;
	}
	public String getIpohe() {
		return ipohe;
	}
	public void setIpohe(String ipohe) {
		this.ipohe = ipohe;
	}
	public String getProvince() {
		return province;
	}
	public void setProvince(String province) {
		this.province = province;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getCounty() {
		return county;
	}
	public void setCounty(String county) {
		this.county = county;
	}
	public String getAddre() {
		return addre;
	}
	public void setAddre(String addre) {
		this.addre = addre;
	}
	public String getAddreCompany() {
		return addreCompany;
	}
	public void setAddreCompany(String addreCompany) {
		this.addreCompany = addreCompany;
	}
	public String getCompanyDepartment() {
		return companyDepartment;
	}
	public void setCompanyDepartment(String companyDepartment) {
		this.companyDepartment = companyDepartment;
	}
	public String getMaintennanceItem() {
		return maintennanceItem;
	}
	public void setMaintennanceItem(String maintennanceItem) {
		this.maintennanceItem = maintennanceItem;
	}
	public String getUseTime() {
		return useTime;
	}
	public void setUseTime(String useTime) {
		this.useTime = useTime;
	}
	public String getUsegoods() {
		return usegoods;
	}
	public void setUsegoods(String usegoods) {
		this.usegoods = usegoods;
	}
	public String getUseBrand() {
		return useBrand;
	}
	public void setUseBrand(String useBrand) {
		this.useBrand = useBrand;
	}
	public String getMaintennanceDesc() {
		return maintennanceDesc;
	}
	public void setMaintennanceDesc(String maintennanceDesc) {
		this.maintennanceDesc = maintennanceDesc;
	}
	
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}


	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public Date getSubmitTime() {
		return submitTime;
	}
	public void setSubmitTime(Date submitTime) {
		this.submitTime = submitTime;
	}
	public Integer getSubmitCount() {
		return submitCount;
	}
	public void setSubmitCount(Integer submitCount) {
		this.submitCount = submitCount;
	}
	public Integer getFinishCount() {
		return finishCount;
	}
	public void setFinishCount(Integer finishCount) {
		this.finishCount = finishCount;
	}
	public String getImage1() {
		return image1;
	}
	public void setImage1(String image1) {
		this.image1 = image1;
	}
	public String getImage2() {
		return image2;
	}
	public void setImage2(String image2) {
		this.image2 = image2;
	}
	public String getImage3() {
		return image3;
	}
	public void setImage3(String image3) {
		this.image3 = image3;
	}
	
	
	
	
	
}
