package cn.bohoon.company.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSONObject;

import cn.bohoon.basicSetup.service.SysParamService;
import cn.bohoon.company.dao.CompanyDao;
import cn.bohoon.company.domain.CompanyConcatState;
import cn.bohoon.company.domain.CompanyModel;
import cn.bohoon.company.domain.CompanySateEnum;
import cn.bohoon.company.domain.CreditType;
import cn.bohoon.company.entity.Company;
import cn.bohoon.company.entity.CompanyMemberLog;
import cn.bohoon.company.entity.Credit;
import cn.bohoon.company.entity.License;
import cn.bohoon.domain.ChiSyncBo;
import cn.bohoon.framework.service.BaseService;
import cn.bohoon.framework.service.UniversalService;
import cn.bohoon.framework.util.DateUtil;
import cn.bohoon.framework.util.JsonUtil;
import cn.bohoon.userInfo.entity.User;
import cn.bohoon.userInfo.entity.UserInfo;
import cn.bohoon.userInfo.service.UserInfoService;
import cn.bohoon.userInfo.service.UserService;
import cn.bohoon.util.IDUtil;

@Service
@Transactional
public class CompanyService extends BaseService<Company, String> {
	
	@Autowired
	CompanyDao companyDao;
	@Autowired
	UserService userService;
	@Autowired
	SysParamService paramService;
	@Autowired
	LicenseService licenseService;
	@Autowired
	UserInfoService userInfoService;
	@Autowired
	UniversalService universalService;
	@Autowired
	CompanyT8Service companyT8Service;
	@Autowired
	CompanyMemberLogService companyMemberLogService;
	@Autowired
	CreditService creditService;
	
	@Autowired
	CompanyDepartmentService companyDepartmentService;
	
	Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	CompanyService(CompanyDao companyDao) {
		super(companyDao);
	}

	
	public void saveComapny(Company company,User user,String commendFriendId){
		UserInfo userInfo = new UserInfo();
		userInfo.setId(IDUtil.getMemberId());
		userInfo.setPhone(user.getMobile());
		userInfo.setNickname(user.getMobile());
		userInfo.setCompanyId(company.getId());
		if(!StringUtils.isEmpty(commendFriendId)){
			int cfId=Integer.parseInt(commendFriendId);
			userInfo.setCommendFriendId(cfId);
		}
		userInfoService.save(userInfo);
		user.setUserInfoId(userInfo.getId());
		user.setCompanyId(company.getId());
		userService.save(user);
		company.setUserId(userInfo.getId());
		this.save(company);
	}
	
	
	public void saveCompany(CompanyModel model){
    	Company company= model.getCompany();
    	License license = model.getLicense();
    	UserInfo userinfo = model.getUserinfo();
    	
    	company.setUserId(userinfo.getId());
    	company.setLicenseId(license.getRegistration());
    	company.setRegtime(new Date());
		company.setId(IDUtil.getMemberId());
  		userinfo.setCompanyId(company.getId());
 
  		save(company);
  		
  		license.setId(license.getRegistration());
  		license.setCompanyid(company.getId());
  		licenseService.save(license);
  		
  		userinfo.setCompanyId(company.getId());
  		userInfoService.update(userinfo);
    }
	
	
	/**
	 * 企业的重复性判断
	 */
	public Company getIsFlag(String column,String value){
		
		Company company = this.select("from Company where "+column+"=? and (companySate=? or companySate=?)", 
    			value,CompanySateEnum.INIT,CompanySateEnum.PASS);
		
		return company;
	}
	
	
	/**
	 * 
	 * 判断用户是否企业员
	 * 
	 * @param userId
	 * @return
	 */
	public boolean isEmpUser(String userId) {
		Company company = getCompanyByUserId(userId) ;
		if (null != company ) {
			return true;
		}
		return false;
	}
	/**
	 * 
	 * 判断用户是否企业员(企业认证前)
	 * 
	 * @param userId
	 * @return
	 */
	public boolean isEmpUsers(String userId) {
		Company company = getCompanyByUserIds(userId) ;
		if (null != company ) {
			return true;
		}
		return false;
	}
	/***
	 *企业注册
	 * 
	 * 
	 */
	public void savaRegCompany(Company company){
		//String hql="insert into t_company(id,name,province,city,address,email,phone) values("+IDUtil.getMemberId()+",?,?,?,?,?,?)";
		
		companyDao.save(company);
		
		
	}
	

	/**
	 * 通过用户ID 获取企业信息（认证后的）
	 * 
	 * @param userId
	 * @return
	 */
	public Company getCompanyByUserId(String userId) {
		String hql = " select c from Company c ,UserInfo u where u.companyId=c.id and u.id=? and c.companySate= ? ";
		List<Company> list = list(hql, userId, CompanySateEnum.PASS);
		if (!list.isEmpty()) {
			return list.get(0);
		}
		return null ;
	}
	/**
	 * 通过用户ID 获取企业信息（认证前的）
	 * 
	 * @param userId
	 * @return
	 */
	public Company getCompanyByUserIds(String userId) {
		String hql = " select c from Company c ,UserInfo u where u.companyId=c.id and u.id=? ";
		List<Company> list = list(hql, userId);
		if (!list.isEmpty()) {
			return list.get(0);
		}
		return null ;
	}
	/**
	/**
	 * 查询当前用户是否是负责人
	 * 
	 * @param userId
	 * @return 
	 */
	public boolean isResp(String userId) {
		String hql = "select c from Company c ,UserInfo u where u.companyId=c.id and c.userId=u.id and u.id=? and c.companySate=?";
		List<Company> list = list(hql, userId, CompanySateEnum.PASS);
		
		if (!list.isEmpty()) {
			return true;
		}
		return false;
	}
	/**
	/**
	 * 查询当前用户是否是负责人（认证前的）
	 * 
	 * @param userId
	 * @return 
	 */
	public boolean isResps(String userId) {
		String hql = "select c from Company c ,UserInfo u where u.companyId=c.id and c.userId=u.id and u.id=?";
		List<Company> list = list(hql, userId);
		
		if (!list.isEmpty()) {
			return true;
		}
		return false;
	}
	
	/**
	 * 根据用户ID 判断协议客户是否通过审核
	 * @param userId
	 * @return
	 */
	public boolean isconcatState(String userId){
		if(!StringUtils.isEmpty(userId)){
			String hql = "select c from Company c ,UserInfo u where u.companyId=c.id and c.userId=u.id and u.id=? and c.concatState = ?";
			List<Company> list = list(hql, userId,CompanyConcatState.PASS);
			if (!list.isEmpty()) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 获取往来对象
	 * 
	 * @return
	 * @throws Exception
	 */
	public void getPartnerFromChi()  {
		Partner() ;
		customer() ;
	}

 
	/**
	 * 企业资料
	 * 
	 */
	private void customer() {
		List<Map<String,Object>> listMap = new ArrayList<Map<String,Object>>() ;
		String progId = "comCustomer" ; //表名
		logger.info("start sync company-comCustomer from chi ----------------");
		ChiSyncBo bo = new ChiSyncBo(progId);
		listMap = companyT8Service.getQueryData(bo) ;
		int len = null != listMap ? listMap.size() : 0 ;
		for(int i=0 ;i<len;i++) {
			String jsonStr = JsonUtil.toJson(listMap.get(i)) ;
			JSONObject json =  JSONObject.parseObject(jsonStr);
			String BizPartnerId = json.getString("BizPartnerId") ;
			Company company = companyDao.get(BizPartnerId) ;
			if(null == company ) {
				continue ;
			}
			 company.setPeriod(json.getIntValue("CloseOffDate")) ;
			 
		}
		logger.info("end sync company-comCustomer from chi ----------------");
	}

	/**
	 * 同步企业  往来客户
	 */
	private void Partner() {
		List<Map<String,Object>> listMap = new ArrayList<Map<String,Object>>() ;
		String progId = "comBusinessPartner" ; //表名
		logger.info("start sync company from chi ----------------");
		ChiSyncBo bo = new ChiSyncBo(progId);
		listMap = companyT8Service.getQueryData(bo) ;
		int len = null != listMap ? listMap.size() : 0 ;
		for(int i=0 ;i<len;i++) {
			String jsonStr = JsonUtil.toJson(listMap.get(i)) ;
			JSONObject json =  JSONObject.parseObject(jsonStr);
			String BizPartnerId = json.getString("BizPartnerId") ;
			Company company = companyDao.get(BizPartnerId) ;
			if(null == company ) {
				company = new Company() ;
				company.setId(BizPartnerId);
				company.setCompanySate(CompanySateEnum.INIT);
			}
			Long CreateTime = json.getLong("CreateTime") ;
			if(null !=CreateTime) {
				String ctime = CreateTime+"" ;
				Date regtime = DateUtil.getDateLong(ctime);
				company.setRegtime(regtime);
			}
			company.setCode(BizPartnerId);
			company.setName(json.getString("BizPartnerName"));
			company.setProvinceCode(json.getInteger("ProvinceId"));
			company.setCityCode(json.getInteger("CityId"));
			int state = json.getIntValue("PermitState") ;
			if(state == 2) {
				company.setCompanySate(CompanySateEnum.PASS);
			}
			company.setPhone(json.getString("WorkTelNo"));
			company.setFax(json.getString("FaxNo"));
			company.setPostcode(json.getString("Postalcode"));
			company.setEmail(json.getString("EMail"));
			company.setLicenseId(json.getString("BusinessNo"));
			company.setNature(json.getString("EnterpriseType"));
			company.setAddress(json.getString("ContactAddress"));
			companyDao.save(company) ;
		}
		logger.info("end sync company from chi ----------------");
	}

	/**
	 * 删除企业所有信息
	 * @param companyId
	 */
	public void deleteAll(String companyId) {
		logger.info("----------开始删除企业：{}----------",companyId);
		//--------------------------------- 删除企业信息 ------------------------
		Company company = this.get(companyId);
		if(company != null){
			String companyName = company.getName();
		
		String hql1 = "delete from CompanyBank where companyId = ? ";//公司银行
		universalService.execute(hql1, companyId);
		
		String hql2 = "delete from CompanyBill where companyId = ?  ";//账单记录
		universalService.execute(hql2, companyId);
		
		String hql3 = "delete from CompanyDepartment where companyId = ? ";//企业部门
		universalService.execute(hql3,companyId);
		
		String hql4 = "delete from CompanyMemberLog where companyId = ? ";//公司成员记录
		universalService.execute(hql4, companyId);
		
		String hql5 = "delete from Credit where companyId = ? ";//信用记录
		universalService.execute(hql5, companyId);
		
		String hql6 = "delete from CreditFlow where companyId = ?";//流水
		universalService.execute(hql6, companyId);
		
		String hql7 = "delete from Invoice where companyId = ? ";// 发票信息 
		universalService.execute(hql7, companyId);
		
		String hql8= "delete from License where companyid = ? ";//营业执照
		universalService.execute(hql8, companyId);
		
		String hql9= "delete from Company where id = ? "; //删除公司
		universalService.execute(hql9 , companyId);
		
		String hql19 = "update UserInfo set companyId = '', deptCode = '' ,entryTime = null  where companyId = ? ";
		universalService.execute(hql19 , companyId);
		logger.info("----------移除企业订单信息 :{} --------",companyId);
		
		String hql10 = "update Order set company = '' where company = ? ";
		universalService.execute(hql10, companyName);
		
		String hql11 = "update AfterMarketOrder set company = '' where company = ? ";
		universalService.execute(hql11, companyName);
		
		String hql12 = "update OrderRepaire set company = '' where company = ?";
		universalService.execute(hql12, companyName);
		
		String hql13 = "update OrderInvoice  set companyId = '' , companyName = '' where companyId = ? ";
		universalService.execute(hql13, companyId);
		
		String hql14 = "update OrderCancel set company = '' where company = ? ";
		universalService.execute(hql14, companyName);
		
		String hql15 = "update OrderBarter set company = '' where company = ? ";
		universalService.execute(hql15, companyName);
		
		String hql16 = "update OrderRefund set company = '' where company = ? ";
		universalService.execute(hql16, companyName);
		
	    String hql17 = "update OrderReceipt  set companyId = ''  where companyId = ? ";
	    universalService.execute(hql17, companyId);
		
		String hql18 = "update Quotation set companyId = '' ,companyName = '' where companyId = ?  ";
		universalService.execute(hql18, companyId);
	    
		}
		logger.info("----------删除企业结束----------");
	}

	/**
	 * 修改企业信用额度
	 * @param companyId 企业ID
	 * @param credit 信用额
	 * @param string 
	 */
	public String updateCredit(String companyId, BigDecimal credit,CreditType creditType, String operaTor) {
		
		
		 
		
		Credit credit2  = new Credit();
		Date dateDate = new Date();
		credit2.setCompanyId(companyId);
		
		
		Company company = get(companyId);
		BigDecimal max  =  new BigDecimal("10000000");
		BigDecimal min = new BigDecimal("0");
		if(company == null){
			return "操作失败";
		}
		
		if(CreditType.ADD.equals(creditType)){ //手动增加
			
			//增加的信用额度不能大于10000000
			
			// //信用额
			if (max.compareTo(credit.add(company.getCredits()))> 0) {
				company.setCreditOver(company.getCreditOver().add(credit)); //信用余额
				credit2.setCorrelationTime(dateDate);
				credit2.setCreditType(creditType.ADD);
				credit2.setCreditOver(credit);
				credit2.setParticular(operaTor + "手动增加额度");
				creditService.save(credit2);
				
			}else{
				
			//	System.out.println("信用额度不能大于10000000");
				return "信用额度不能大于一千万";
			}
			
			company.setCredits(company.getCredits().add(credit));//增加信用额度
		}
		else if(CreditType.REDUCE.equals(creditType)){//手动减少
			BigDecimal credits = company.getCredits(); //当前信用额
			
			
			if(credits.compareTo(credit) == -1){ //小于 就是修改后的信用额度不能不负数
			//	company.setCredits(new BigDecimal(0));
				//	System.out.println("信用额度不能小于0");
				return "信用额度不能小于0";
			}else{			//不为-1的话 就是修改后的金额大于等于0 ， 可以进行修改 ， 进行数据操作
				company.setCredits(credits.subtract(credit));
				credit2.setCorrelationTime(dateDate);
				credit2.setCreditType(creditType.REDUCE);
				credit2.setCreditOver(credit);
				credit2.setParticular(operaTor + "手动减少额度");
				creditService.save(credit2);
			}

			
			
			
			company.setCreditOver(company.getCreditOver().subtract(credit));
		}else if(CreditType.SETTING.equals(creditType)){//重新设置
			BigDecimal credits = company.getCredits();
			if(credits.compareTo(credit) == -1){ //增加
				if (credit.compareTo(max) > 0) {
					//	System.out.println("设置的信额度不能大于10000000");
					return "设置的信额度不能大于一千万";
				}
						
				BigDecimal addnew = credit.subtract(credits) ;
				company.setCredits(credit); //信用额
				company.setCreditOver(company.getCreditOver().add(addnew)); //信用余额
				
			} else {
				company.setCredits(credit); //信用额
				BigDecimal subnew = credits.subtract(credit) ;
				if (subnew.compareTo(min) >=0) {
					company.setCreditOver(company.getCreditOver().subtract(subnew));
				}else{
					//	System.out.println("信用额度不能小于0");
					return "信用额度不能小于0";
				}
				
				
			}
			credit2.setCorrelationTime(dateDate);
			credit2.setCreditType(creditType.SETTING);
			credit2.setCreditOver(credit);
			credit2.setParticular("重置额度");
			creditService.save(credit2);
		}
		update(company);
		logger.info("-------------成功 修改企业:{}信用额:{}----------",companyId,credit);
		return "";
	}
	
	/**
	 * 与公司解除关系
	 * @param userId
	 * @param company
	 */
	public void terminate(String userId,Company company ){
		
		String companyId = company.getId();
		if(StringUtils.isEmpty(companyId)){
			return ;
		}
		
		userInfoService.execute(" update UserInfo set companyState = 0 , companyId= null, bindingDate = null, departmentId = null, entryTime  = null, personInCharge = 0 where id = ? and companyId = ? ",userId,companyId);
		companyDepartmentService.execute(" update CompanyDepartment set responsibleId = null , responsiblePerson = null where responsibleId = ?  and companyId = ? ",userId,companyId);
		
		CompanyMemberLog companyMemberLog = new CompanyMemberLog();
		companyMemberLog.setUserId(userId);
		companyMemberLog.setCompanyId(companyId);
		companyMemberLog.setCreateDate(new Date());
		companyMemberLog.setNote(String.format("用户Id:%s与公司:%s 解绑", userId,company.getName()));
		companyMemberLog.setBinding(false);
		companyMemberLogService.save(companyMemberLog);
		
		logger.info("----------------用户Id:{}与公司:{}解绑 成功----------------",userId,company.getName());
		
	}


	
	
}
