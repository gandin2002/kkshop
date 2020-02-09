package cn.bohoon.company.entity;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.springframework.format.annotation.DateTimeFormat;

import com.alibaba.fastjson.annotation.JSONField;

import cn.bohoon.company.domain.AmountPurchased;
import cn.bohoon.company.domain.BillTime;
import cn.bohoon.company.domain.CompanyConcatState;
import cn.bohoon.company.domain.CompanySateEnum;
import cn.bohoon.company.domain.CompanyScaleEnum;
import cn.bohoon.company.domain.InvoiceType;
import cn.bohoon.company.service.CompanyCataService;
import cn.bohoon.company.service.CompanyDepartmentService;
import cn.bohoon.company.service.CompanyIndustryService;
import cn.bohoon.company.service.CompanyLevelService;
import cn.bohoon.company.service.LicenseService;
import cn.bohoon.framework.SpringContextHolder;
import cn.bohoon.userInfo.entity.UserInfo;
import cn.bohoon.userInfo.service.UserInfoService;

/**
 * 企业基本信息数据模型
 * 
 * @author Administrator
 *
 */
@Entity
@Table(name = "t_company")
public class Company {

	@Id
	@Column(length=64)
	String id;// 企业ID
	String code;// 企业code
	String name;// 企业名称
	Integer cataId = 0; // 企业分类
	Integer industryId = 0;// 行业
	Integer level = 1; // 企业等级
	BigDecimal credits = new BigDecimal(0); // 企业信用额度
	BigDecimal creditOver = new BigDecimal(0); // 企业信用余额
	BigDecimal applyCredists = new BigDecimal(0); // 申请信用金额
	
	

	@Column(length=64)
	String userId; // 负责人ID
	String licenseId; // 营业执照ID
	String province; // 省名称
	Integer provinceCode;//省份Code
	String city; // 市名称
	Integer cityCode;//市Code
	String county; // 区名称
	Integer countyCode;//区Code
	@Column(columnDefinition = "datetime")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	Date regtime; // 注册日期
	String nature; // 企业性质
	
	@Enumerated(EnumType.STRING)
	CompanyScaleEnum scale =  CompanyScaleEnum.LESS1 ; // 企业规模
	String address; // 详细地址
	String postcode; // 地区邮编
	String phone; // 企业电话  --> 座机
	String email; // 企业邮箱
	String fax; // 企业传真
	Integer period = 5 ; // 还款周期/记账周期
	String operatorId  = "1"; // 专管员
	String operatorName; // 专管员姓名
	String  operatorPhone;  //专管员号码
	@Enumerated(EnumType.STRING)
	CompanySateEnum companySate = CompanySateEnum.INIT;  // 审核状态
	String checkWarn;// 审核提示
	String licenseImage; // 营业执照图片
	String companyLogo;//公司背景图
	


	@Column(columnDefinition = "datetime")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	Date applyTime = new Date();//申请时间
	
	@Column(columnDefinition = "datetime")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	Date passTime;//通过时间

	String extensionWorker;//推广人员 operatorId
	String webSite;//公司网址
	
	boolean audit; // 免审核:  0: 审核,  1,免审核，用于订单中的未确认，如果为1免审核，提交订单后，直接到付款状态
	
  /*  BigDecimal companyOrderMoney=new BigDecimal(0.0);  //企业订单总额 除去已取消的
    BigDecimal  remainingMoney=new BigDecimal(0.0);  //余下账单额度
*/
	//维保状态
	Integer   state=0;  //0 未申请     1申请
	
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    Date   serviceTiem=new Date();  //企业审请维保日期
	String  validityTime;  //维保有效期至
	String   PassShenHeTime;   //通过维保审核日期
	String   technicaladvisers;//技术顾问
	Integer   appliState;  //0 维保审核中，1通过
	String  time;  //天数
	Integer  submitCount=0; //提交服务次数
    Integer   finishCount=0;//完成服务 次数
    String  protectPhone;//维保联系人号码
    
    // 合约企业注册新增的信息    
    BillTime applyListTime;  // 申请账期时间
    
    String applyListTimeName;//账期名称  
    String applyListTimeId;//账期ID ->GatheringCondition
    String contacts;   //   企业联系人
    String purchasingCategorys; // 采购品类
    String dailyExpenditures;   // 日常支出方式 
    InvoiceType ticket;              // 开票类型
    AmountPurchased  amount;       // 月采购量
    String contacts_phone; // 企业联系人电话
    String company_password;  // 企业初始化密码
    String 	nowApplyCredists; // 实际申请信用金额
    String nowApplyListTime;  // 实际申请账期时间
//    String pass_remark;      // 通过备注
    CompanyConcatState concatState = CompanyConcatState.NOTAPLLY;  // 合约审核状态  默认为未申请
    
    
    
	public String getCompanyLogo() {
		return companyLogo;
	}

	public void setCompanyLogo(String companyLogo) {
		this.companyLogo = companyLogo;
	}

	public String getOperatorPhone() {
		return operatorPhone;
	}

	public void setOperatorPhone(String operatorPhone) {
		this.operatorPhone = operatorPhone;
	}

	public CompanyConcatState getConcatState() {
		return concatState;
	}

	public void setConcatState(CompanyConcatState concatState) {
		this.concatState = concatState;
	}

	public String getNowApplyCredists() {
		return nowApplyCredists;
	}

	public void setNowApplyCredists(String nowApplyCredists) {
		this.nowApplyCredists = nowApplyCredists;
	}

	public String getNowApplyListTime() {
		return nowApplyListTime;
	}

	public void setNowApplyListTime(String nowApplyListTime) {
		this.nowApplyListTime = nowApplyListTime;
	}

/*	public String getPass_remark() {
		return pass_remark;
	}

	public void setPass_remark(String pass_remark) {
		this.pass_remark = pass_remark;
	}*/

	public String getCompany_password() {
		return company_password;
	}

	public void setCompany_password(String company_password) {
		this.company_password = company_password;
	}

	public String getContacts() {
		return contacts;
	}

	public void setContacts(String contacts) {
		this.contacts = contacts;
	}

	public BigDecimal getApplyCredists() {
		return applyCredists;
	}
	
	public void setApplyCredists(BigDecimal applyCredists) {
		this.applyCredists = applyCredists;
	}
	public String getContacts_phone() {
		return contacts_phone;
	}

	public void setContacts_phone(String contacts_phone) {
		this.contacts_phone = contacts_phone;
	}

	public AmountPurchased getAmount() {
		return amount;
	}

	public void setAmount(AmountPurchased amount) {
		this.amount = amount;
	}

	public BillTime getApplyListTime() {
		return applyListTime;
	}

	public void setApplyListTime(BillTime applyListTime) {
		this.applyListTime = applyListTime;
	}




	public String getPurchasingCategorys() {
		return purchasingCategorys;
	}

	public void setPurchasingCategorys(String purchasingCategorys) {
		this.purchasingCategorys = purchasingCategorys;
	}

	public String getDailyExpenditures() {
		return dailyExpenditures;
	}

	public void setDailyExpenditures(String dailyExpenditures) {
		this.dailyExpenditures = dailyExpenditures;
	}

	public InvoiceType getTicket() {
		return ticket;
	}

	public void setTicket(InvoiceType ticket) {
		this.ticket = ticket;
	}

	public String getProtectPhone() {
	
		return protectPhone;
	}

	public void setProtectPhone(String protectPhone) {
		this.protectPhone = protectPhone;
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

	public String getPassShenHeTime() {
		return PassShenHeTime;
	}

	public void setPassShenHeTime(String passShenHeTime) {
		PassShenHeTime = passShenHeTime;
	}

	public boolean isAudit() {
		return audit;
	}

	public void setAudit(boolean audit) {
		this.audit = audit;
	}

	@Transient
	@JSONField(serialize = false)
	public CompanyLevel getCompanyLevel() {
		CompanyLevelService service = SpringContextHolder.getBean(CompanyLevelService.class);
		if (null != level) {
			return service.get(level);
		}
		return null;
	}
	//行业
	@Transient
	@JSONField(serialize = false)
	public CompanyIndustry getCompanyIndustry() {
		CompanyIndustryService service = SpringContextHolder.getBean(CompanyIndustryService.class);
		if (null != industryId) {
			return service.get(industryId);
		}
		return null;
	}
    //部門
	@Transient
	@JSONField(serialize = false)
	public List<CompanyDepartment> getCompanyDepartment() {
		CompanyDepartmentService service = SpringContextHolder.getBean(CompanyDepartmentService.class);
		if (null != id) {
			return service.list(" from CompanyDepartment where  companyId= ? ",id);
		}
		return null;
	}
	
	@Transient
	@JSONField(serialize = false)
	public CompanyCata getCompanyCata() {
		CompanyCataService service = SpringContextHolder.getBean(CompanyCataService.class);
		if (null != cataId) {
			return service.get(cataId);
		}
		return null;
	}
	
	//负责人
	@Transient
	@JSONField(serialize = false)
	public UserInfo getUserInfo() {
		UserInfoService service = SpringContextHolder.getBean(UserInfoService.class);
		if (null != userId) {
			return service.get(userId);
		}
		return null;
	}
	@Transient
	@JSONField(serialize = false)
	public String getPeriodCountDate(){ // 计算账单日
		if(this.period != null){
			Calendar calendar = Calendar.getInstance();
			if(calendar.get(Calendar.DAY_OF_MONTH)>this.period){ //当前日 大于还款日  那么计算下一个月的还款日
				calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH)+1);
			}
			calendar.set(Calendar.DAY_OF_MONTH, this.period);
			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");  
			return dateFormat.format(calendar.getTime());
		}
		return null;
	}
	
	//企业资质
	@Transient
	@JSONField(serialize = false)
	public License getLicense(){ 
		LicenseService licenseService = SpringContextHolder.getBean(LicenseService.class);
		if (null != this.licenseId) {
			return  licenseService.get(licenseId);  
		}
		return null;
	}
	

	public Company() {
		super();
	}
	

	public String getLicenseId() {
		return licenseId;
	}
	//, CompanyScaleEnum valueOf
	//name, province, city, county, address, email,smsPhone

	public Company(String name, String province, String city, String county, String address, String phone,
			String email,Integer CompanyIndustryId ,CompanyScaleEnum scale) {
		super();
		this.name = name;
		this.province = province;
		this.city = city;
		this.county = county;
		this.address = address;
		this.phone = phone;
		this.email = email;
		this.industryId =CompanyIndustryId;
		this.scale=scale;
	}

	
	public void setLicenseId(String licenseId) {
		this.licenseId = licenseId;
	}
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getCataId() {
		return cataId;
	}

	public void setCataId(Integer cataId) {
		this.cataId = cataId;
	}

	public Integer getIndustryId() {
		return industryId;
	}

	public void setIndustryId(Integer industryId) {
		this.industryId = industryId;
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

	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}

	public BigDecimal getCredits() {
		return credits;
	}

	public void setCredits(BigDecimal credits) {
		this.credits = credits;
	}

	public BigDecimal getCreditOver() {
		return creditOver;
	}

	public void setCreditOver(BigDecimal creditOver) {
		this.creditOver = creditOver;
	}

	public Date getRegtime() {
		return regtime;
	}

	public void setRegtime(Date regtime) {
		this.regtime = regtime;
	}

	public String getNature() {
		return nature;
	}

	public void setNature(String nature) {
		this.nature = nature;
	}

	public CompanyScaleEnum getScale() {
		return scale;
	}

	public void setScale(CompanyScaleEnum scale) {
		this.scale = scale;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPostcode() {
		return postcode;
	}

	public void setPostcode(String postcode) {
		this.postcode = postcode;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getFax() {
		return fax;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}

	public Integer getPeriod() {
		return period;
	}

	public void setPeriod(Integer period) {
		this.period = period;
	}

	public String getOperatorId() {
		return operatorId;
	}

	public void setOperatorId(String operatorId) {
		this.operatorId = operatorId;
	}

	public String getOperatorName() {
		return operatorName;
	}

	public void setOperatorName(String operatorName) {
		this.operatorName = operatorName;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public CompanySateEnum getCompanySate() {
		return companySate;
	}

	public void setCompanySate(CompanySateEnum companySate) {
		this.companySate = companySate;
	}

	public String getCheckWarn() {
		return checkWarn;
	}

	public void setCheckWarn(String checkWarn) {
		this.checkWarn = checkWarn;
	}

	public String getLicenseImage() {
		return licenseImage;
	}

	public void setLicenseImage(String licenseImage) {
		this.licenseImage = licenseImage;
	}

	public Date getApplyTime() {
		return applyTime;
	}

	public void setApplyTime(Date applyTime) {
		this.applyTime = applyTime;
	}

	public Date getPassTime() {
		return passTime;
	}

	public void setPassTime(Date passTime) {
		this.passTime = passTime;
	}

	public Integer getProvinceCode() {
		return provinceCode;
	}

	public String getExtensionWorker() {
		return extensionWorker;
	}

	public void setProvinceCode(Integer provinceCode) {
		this.provinceCode = provinceCode;
	}

	public Integer getCityCode() {
		return cityCode;
	}

	public void setCityCode(Integer cityCode) {
		this.cityCode = cityCode;
	}

	public Integer getCountyCode() {
		return countyCode;
	}

	public void setCountyCode(Integer countyCode) {
		this.countyCode = countyCode;
	}

	public String getWebSite() {
		return webSite;
	}

	public void setExtensionWorker(String extensionWorker) {
		this.extensionWorker = extensionWorker;
	}

	public void setWebSite(String webSite) {
		this.webSite = webSite;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public Date getServiceTiem() {
		return serviceTiem;
	}

	public void setServiceTiem(Date serviceTiem) {
		this.serviceTiem = serviceTiem;
	}

	public String getValidityTime() {
		return validityTime;
	}

	public void setValidityTime(String validityTime) {
		this.validityTime = validityTime;
	}

	public String getTechnicaladvisers() {
		return technicaladvisers;
	}

	public void setTechnicaladvisers(String technicaladvisers) {
		this.technicaladvisers = technicaladvisers;
	}

	public Integer getAppliState() {
		return appliState;
	}

	public void setAppliState(Integer appliState) {
		this.appliState = appliState;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getApplyListTimeName() {
		return applyListTimeName;
	}

	public void setApplyListTimeName(String applyListTimeName) {
		this.applyListTimeName = applyListTimeName;
	}

	public String getApplyListTimeId() {
		return applyListTimeId;
	}

	public void setApplyListTimeId(String applyListTimeId) {
		this.applyListTimeId = applyListTimeId;
	}
 
}
