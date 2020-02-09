package cn.bohoon.userInfo.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import chi.data.container.Row;
import chi.data.container.Table;
import chi.data.service.CAPInteropServiceExSoapStub;
import cn.bohoon.basicSetup.domain.SysParamType;
import cn.bohoon.basicSetup.entity.SysParam;
import cn.bohoon.basicSetup.helper.SysParamHelper;
import cn.bohoon.basicSetup.service.SysParamService;
import cn.bohoon.company.entity.Company;
import cn.bohoon.company.service.CompanyService;
import cn.bohoon.framework.service.BaseService;
import cn.bohoon.framework.service.UniversalService;
import cn.bohoon.framework.util.CipherUtil;
import cn.bohoon.order.entity.Order;
import cn.bohoon.quotation.entity.Quotation;
import cn.bohoon.syn.dao.SynUserInfoDao;
import cn.bohoon.syn.entity.SynUserInfo;
import cn.bohoon.userInfo.dao.ShippingInfoDao;
import cn.bohoon.userInfo.dao.UserDao;
import cn.bohoon.userInfo.dao.UserInfoDao;
import cn.bohoon.userInfo.domain.Member;
import cn.bohoon.userInfo.domain.UserSex;
import cn.bohoon.userInfo.entity.ShippingInfo;
import cn.bohoon.userInfo.entity.User;
import cn.bohoon.userInfo.entity.UserInfo;
import cn.bohoon.userInfo.entity.UserLevel;
import cn.bohoon.util.IDUtil;

@Service
@Transactional
public class UserInfoService extends BaseService<UserInfo, String> {

	@Autowired
	UserDao userDao;
	@Autowired
	UserInfoDao userInfoDao;
	@Autowired
	SysParamService paramService;
	@Autowired
	ShippingInfoDao shippingInfoDao;
	@Autowired
	UserLevelService userLevelService;
	
	@Autowired
	UniversalService universalService;
	
	@Autowired
	SysParamService sysParamService;
	
	@Autowired
	JdbcTemplate jdbcTemplate;
	Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	UserInfoService(UserInfoDao userInfoDao) {
		super(userInfoDao);
	}
	
	
	
	// 通过企业id获取当前企业的所有员工
	public List<UserInfo> getUserInfos(String companyId){
		List<UserInfo> list = this.list("from UserInfo where companyId=? order by bindingDate",companyId);
		
		return list;
		
		
		
	}
	
 
	public String encodePwd(){
		SysParam sysParam = sysParamService.findParam(SysParamHelper.USER_DEFAULT_PASSWORD, SysParamType.PLATFORM_PARAM);
		if(!StringUtils.isEmpty(sysParam.getValue())){
			return CipherUtil.md5(sysParam.getValue());
		}
		return CipherUtil.md5("123456");
	}
	
	//判断用户是否为企业用户
	public String IsCompany(UserInfo userInfo){
		if(userInfo == null ){
			return "0";
		}
		String CompangId=userInfo.getCompanyId();
		if(CompangId!=null){
			return "1";
		}else{
			return "0";
		}
	}
	
	public void save(UserInfo entity) {
		UserLevel userLevel = userLevelService.getUserLevel(entity.getExp());
		entity.setUserLevel(userLevel.getLevel());
		
		if (StringUtils.isEmpty(entity.getId())) {
			String userId = IDUtil.getInstance().getCommonId(this,UserInfo.class);
			entity.setId(userId);
		}
		
		
		// 首先先判断新增的账户是否为部门负责人，
		if (entity.getPersonInCharge()){
			
			// 如果为部门负责人，则需要把先前的部门负责人设置为false
			String hql = "from UserInfo where companyId=? and departmentId=? and personInCharge=?";
			List<Object> list = new ArrayList<Object>();
			list.add(entity.getCompanyId());
			list.add(entity.getDepartmentId());
			list.add(entity.getPersonInCharge());
			List<UserInfo> findAll = userInfoDao.findAll(hql, list.toArray());
			
			if (findAll.size()>0){
				
				for (UserInfo userInfo : findAll){
					
					userInfo.setPersonInCharge(false);
				}
					}
		}
		
		
		userInfoDao.save(entity);
	}

		public UserInfo getUserInfoByNickname(String Nickname){
			String hql="from UserInfo where nickname=?";
			return userInfoDao.select(hql, Nickname);
			 
		}
	
	public void save(Member member) {
		User user = new User();
		ShippingInfo shippingInfo = member.getShippingInfo();
		UserInfo userInfo = member.getUserInfo();
		
		String userId = IDUtil.getInstance().getCommonId(this,UserInfo.class);
		
		userInfo.setId(userId);
		userInfo = userInfoDao.save(userInfo);
		
		shippingInfo.setId(IDUtil.getMemberId());
		shippingInfo.setUserId(userInfo.getId());
		shippingInfo.setFirst(true);
		shippingInfoDao.save(shippingInfo);

		user.setUserInfoId(userInfo.getId());
		user.setMobile(userInfo.getPhone());
		user.setPassword(encodePwd()); //默认密码为111111
		userDao.save(user);

	}
		

	/**
	 * 手动添加员工
	 * 
	 * @param entity
	 */
	public void addAccount(UserInfo entity) {
		String userId = IDUtil.getInstance().getCommonId(this,UserInfo.class);
		entity.setId(userId);
		User user = new User();
		userInfoDao.save(entity);
		user.setUserInfoId(entity.getId());
		if(entity.getPhone() != null){
			user.setMobile(entity.getPhone());
		}
		user.setPassword(encodePwd());
		userDao.save(user);
	}

	/**
	 * 通过微信unionID查询用户
	 * 
	 * @param unionId
	 * @return
	 */
	public UserInfo getUserInfoByUnionId(String unionId) {
		String hql = " from UserInfo where wechatUnioid=? ";
		UserInfo userInfo = userInfoDao.select(hql, unionId);
		return userInfo;
	}

	public String getResponsNames(Integer departmentId) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("departmentId", departmentId);
		String hql = "select realname from UserInfo where departmentId=:departmentId and personInCharge=1 ";
		List<String> result = userInfoDao.findAll(hql, String.class, params);
		String name = "";
		for (int i = 0; i < result.size(); i++) {
			String x = result.get(i);
			name += x + ";";
			if (i == 2) {
				break;
			}
		}
		if (!StringUtils.isEmpty(name)) {
			name = name.substring(0, name.length() - 1);
		}
		return name;
	}

	/**
	 * 获取负责人ID
	 * 
	 * @param userId
	 * @return
	 */
	public UserInfo queryRespUser(String userId) {
		String hql = "select u from UserInfo u,UserInfo u2 ,Company c where u.companyId = c.id and u.id = c.userId and u2.id="
				+ userId;
		hql += " and u.companyId = u2.companyId";
		List<UserInfo> userInfos = userInfoDao.findAll(hql);
		if (userInfos.size() > 0) {
			return userInfos.get(0);
		}
		return userInfoDao.get(userId);
	}
	
	
	/**
	 * 正航同步用户相关数据
	 */
	public void syncCustomerFromChi() {
		try {
			Map<String, String> params = paramService.findParamMap(SysParamType.CHI_SYNC_PARAM);
			String syncUrl = params.get(SysParamHelper.SYNC_CHI_URL);
			String groupId = params.get(SysParamHelper.SYNC_CHI_GROUP_ID);
			String language = params.get(SysParamHelper.SYNC_CHI_LANGUAGE);
			String userId = params.get(SysParamHelper.SYNC_CHI_USER_ID);
			String pwd = params.get(SysParamHelper.SYNC_CHI_PWD);
			byte[] password = pwd.getBytes("utf-8");
			String progId = "comCustomer"; // 表名
			int tableIndex = 0;
			String selectedFields = "OrgId,BizPartnerId,BizPartnerTypeId,PersonId,CreditorOrgId,CreateTime,CloseOffDate ";
			String whereClause = " ";// 条件
			String sortFields = ""; // 排序字段
			int fetchCount = 100;
			boolean isDistinct = false;
			int logMode = 4;
			java.net.URL url = new java.net.URL(syncUrl);
			CAPInteropServiceExSoapStub service = new CAPInteropServiceExSoapStub(url, null);
			String result = service.getQueryData(groupId, language, userId, password, language, progId, tableIndex,
					selectedFields, whereClause, sortFields, fetchCount, isDistinct, logMode);
			Table table = Table.loadXml(result);
			int count = null != table ? table.getRowCount() : 0;
			for (int i = 0; i < count; i++) {
				Row row = table.getRow(i);
				System.out.print(row.get(0) + "  ;");
				System.out.print(row.get(1) + "  ;");
				System.out.print(row.get(2) + "  ;");
				System.out.print(row.get(3) + "  ;");
				System.out.print(row.get(4) + "  ;");
				System.out.println(row.get(5));
			}
		} catch (Exception e) {

		}
	}
		
	
	
	public UserInfo geeUserInfoByUsername(String username){
		UserInfo userInfo = userInfoDao.select(" from UserInfo where username=?", username);
		return userInfo;
	}
	
	
	/**
	 * 验证是否存在
	 * 
	 * @param idcard 身份证号
	 * @return
	 */
	public Boolean isIdcard(String idcard) {
		UserInfo userInfo = select(" from UserInfo where idcard = ? ", idcard);
		if (userInfo != null) {
			return true;
		}
		return false;
	}
	
	
	
	/**
	 * 删除用户
	 * @param userInfo
	 * @param user
	 */
	public void deleteAll(String userId){
		
		logger.info("----------开始删除用户:{} --------",userId);
		//--------------------------------- 删除基础用户信息 ------------------------
		String hql1 = "delete from UserInfo where id=?";
		universalService.execute(hql1, userId);

		String hql2 = "delete from User where userInfoId=?";
		universalService.execute(hql2, userId);
		logger.info("----------删除基础用户信息:{} --------",userId);
		// --------------------------------- 删除用户记录信息 ------------------------
		
		String hql3 = "delete from ExpLog where memberId = ? "; // 经验记录
		universalService.execute(hql3, userId);
		String hql4 = "delete from ScoreLog where memberId = ? ";// 积分记录
		universalService.execute(hql4, userId);
		String hql5 = "delete from CreditLog where userId = ? ";// 信用记录
		universalService.execute(hql5, userId);
		String hql6 = "delete from ShippingInfo where userId = ?";// 购物地址
		universalService.execute(hql6, userId);
		logger.info("----------删除用户记录信息 :{} --------",userId);
		// --------------------------------- 删除订单信息 ------------------------
		List<Order> orderlist = universalService.list(Order.class,"  from Order where userId = ? ", userId);
		for (Order order : orderlist) {
			
			String hql21 = "delete from OrderCode where orderId = ? "; // 订单订单二维码
			universalService.execute(hql21, order.getId());

			String hql22 = "delete from SendGoods where orderId = ? ";// 发货单
			universalService.execute(hql22, order.getId());
		}
		

		String hql7 = "delete from Order where userId = ? "; // 删除订单
		universalService.execute(hql7, userId);

		String hql8 = "delete from OrderItem where userId = ? "; // 删除订单详情
		universalService.execute(hql8, userId);

		String hql9 = "delete from Invoice where memberId = ? "; // 发票
		universalService.execute(hql9, userId);

		String hql10 = "delete from OrderRefund where userId = ? ";// 退货
		universalService.execute(hql10, userId);

		String hql11 = "delete from OrderBarter where userId = ? ";// 换货
		universalService.execute(hql11, userId);

		String hql12 = "delete from OrderCancel where userId = ? ";// 取消订单
		universalService.execute(hql12, userId);

		String hql13 = "delete from OrderInvoice where memberId = ?  ";// 发票信息
		universalService.execute(hql13, userId);

		String hql14 = "delete from OrderLog  where orderUserId = ?  ";// 订单记录
		universalService.execute(hql14, userId);

		String hql15 = "delete from OrderReceipt where userInfoId = ? ";// 订单收款信息
		universalService.execute(hql15, userId);

		String hql16 = "delete from OrderRepaire where userId = ? ";// 维修单
		universalService.execute(hql16, userId);

		String hql17 = "delete from ShoppingCart where userId = ? ";// 购物车
		universalService.execute(hql17, userId);

		String hql18 = "delete from AfterMarketOrder where userId = ? ";// 售后订单信息
		universalService.execute(hql18, userId);

		String hql19 = "delete from AmOrderItem where userId = ? ";// 售后订单清单项
		universalService.execute(hql19, userId);

		String hql20 = "delete from AmOrderLog where OrderUserId = ? ";
		universalService.execute(hql20, userId);
		
		List<Quotation> listQuotation = universalService.list(Quotation.class," from Quotation where userInfoId = ? ",userId);
		for (Quotation quotation : listQuotation) {
			universalService.execute(" delete from QuotationItem  where  quotationId = ? ",quotation.getId());
		}
		universalService.execute(" delete from Quotation where userInfoId = ? ", userId);
		
		logger.info("----------删除用户订单信息 :{} --------",userId);
		

		//--------------------------------- 删除公司信息 ------------------------

		Company company = universalService.select(" from Company where userId = ? ", Company.class, userId);
		if(company != null){
			
			String companyId = company.getId();
			
			String hql25 = "delete from CompanyBank where companyId = ? ";//公司银行
			universalService.execute(hql25,companyId);
			
			String hql26 = "delete from CompanyBill where companyId = ?  ";//账单记录
			universalService.execute(hql26,companyId);
			
			String hql27 = "delete from CompanyDepartment where companyId = ? ";//企业部门
			universalService.execute(hql27,companyId);
			
			String hql28 = "delete from CompanyMemberLog where companyId = ? ";//公司成员记录
			universalService.execute(hql28,companyId);
			
			String hql29 = "delete from Credit where companyId = ? ";//信用记录
			universalService.execute(hql29,companyId);
			
			String hql30 = "delete from CreditFlow where companyId = ?";//流水
			universalService.execute(hql30,companyId);
			
			String hql31 = "delete from Invoice where companyId = ? ";// 发票信息 
			universalService.execute(hql31,companyId);
			
			String hql32 = "delete from License where companyid = ? ";//营业执照
			universalService.execute(hql32,companyId);
			
			String hql23 = "delete from Company where userId = ? "; //删除公司
			universalService.execute(hql23, userId);
			
			logger.info("----------删除用户:{}公司信息 :{} --------",userId,companyId);
		}
		
		String hql24 = "update CompanyDepartment set responsiblePerson='' where responsiblePerson = ?"; //清除部门负责人
		universalService.execute(hql24, userId);
		
		
		logger.info("----------删除用户结束 --------");
	}


	//手动添加账号
	public void addAccount(UserInfo entity, User member) {
		
		// 首先先判断新增的账户是否为部门负责人，
		if (entity.getPersonInCharge()){
			
			// 如果为部门负责人，则需要把先前的部门负责人设置为false
			String hql = "from UserInfo where companyId=? and departmentId=? and personInCharge=?";
			List<Object> list = new ArrayList<Object>();
			list.add(entity.getCompanyId());
			list.add(entity.getDepartmentId());
			list.add(entity.getPersonInCharge());
			List<UserInfo> findAll = userInfoDao.findAll(hql, list.toArray());
			
			if (findAll.size()>0){
				
				for (UserInfo userInfo : findAll){
					
					userInfo.setPersonInCharge(false);
				}
			}
			
			
		}
		
		if(null == entity.getId()){ 
			String userId = IDUtil.getInstance().getCommonId(this,UserInfo.class);
			entity.setId(userId);
			entity.setBindingDate(new Date());
			member.setPassword(encodePwd());
			member.setUserInfoId(entity.getId());
		}
	       userInfoDao.save(entity);
	 // System.out.println("userInfo的余额"+userInfo.getRemainingCredit());
		userDao.save(member);
	}
	
	@Autowired
	SynUserInfoDao synUserInfoDao;
	
	@Autowired
	CompanyService companyService;
	
	public void synUserInfo(){
		
		List<SynUserInfo> list= synUserInfoDao.findAll();
		for (SynUserInfo synUserInfo : list) {
			String mobile=synUserInfo.getMobile();
			if(!StringUtils.isEmpty(mobile)){
				
				String companyId = "";
				
				UserInfo userInfo = this.select(" from UserInfo where phone = ?  ", mobile);
				if(userInfo == null){
					userInfo = new UserInfo();
					UserSex sex = synUserInfo.getSex().equals("0") ? UserSex.MAN:UserSex.WOMAN;
					
					String email=synUserInfo.geteMail();
					companyId = synUserInfo.getBizPartnerId();
					
					userInfo.setSex(sex);
					userInfo.setPhone(mobile);
					userInfo.setRealname(synUserInfo.getLinkMan());
					userInfo.setEmail(email);
					userInfo.setCompanyId(companyId);
					userInfo.setNickname(synUserInfo.getLinkMan());
					userInfo.setRowNo(synUserInfo.getRowNo());
					User user = new User();
					user.setEmail(email);
					user.setMobile(mobile);
					addAccount(userInfo, user);
					
				}else{
					
					UserSex sex = synUserInfo.getSex().equals("0") ? UserSex.MAN:UserSex.WOMAN;
					
					String email=synUserInfo.geteMail();
					companyId = synUserInfo.getBizPartnerId();
					
					userInfo.setNickname(synUserInfo.getLinkMan());
					userInfo.setSex(sex);
					userInfo.setRealname(synUserInfo.getLinkMan());
					userInfo.setEmail(email);
					userInfo.setCompanyId(companyId);
					userInfo.setRowNo(synUserInfo.getRowNo());
					save(userInfo);
					
				}  
				
				Boolean blea = new Boolean(synUserInfo.getIsMain());
				if(blea){ //如果为主要公司主要联系人
					 Company company = companyService.get(companyId);
					 if(company == null){
						 continue;
					 }
					 company.setUserId(userInfo.getId());
					 companyService.save(company);
				}
			}
			
		}
		
		
	}
	
	//微信小程序获取专管员手机号
	public String getOperatorPhone(String oppid){
		UserInfo user=userInfoDao.select("from UserInfo where wechatOpenid=?", oppid);
		String result="";
		//判断是否为企业用户
		if(IsCompany(user).equals("1")){
			Company company=user.getCompany();
			if(StringUtils.isEmpty(company.getOperatorId())){
				String operatorId=company.getOperatorId();//获取专管员Id
				String phone  =  (String) jdbcTemplate.queryForObject("SELECT phone FROM t_operator where id='" + operatorId + "'", Object.class);
				result=phone;
			}else{
				String phone  =  (String) jdbcTemplate.queryForObject("SELECT phone FROM t_operator where realname='admin'", Object.class);
				result=phone;
			}
		}else{
			String phone  =  (String) jdbcTemplate.queryForObject("SELECT phone FROM t_operator where realname='admin'", Object.class);
			result=phone;
		}
		return result;
	}
	
	
}
