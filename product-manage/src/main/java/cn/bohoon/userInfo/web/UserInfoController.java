package cn.bohoon.userInfo.web;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.bohoon.basicSetup.domain.SysParamType;
import cn.bohoon.basicSetup.entity.SysParam;
import cn.bohoon.basicSetup.helper.SysParamHelper;
import cn.bohoon.basicSetup.service.SysParamService;
import cn.bohoon.framework.orm.domain.Page;
import cn.bohoon.framework.util.DateUtil;
import cn.bohoon.framework.util.JsonUtil;
import cn.bohoon.main.domain.LoginUser;
import cn.bohoon.main.domain.UserSession;
import cn.bohoon.main.system.entity.Operator;
import cn.bohoon.main.system.service.OperatorLogService;
import cn.bohoon.main.system.service.OperatorService;
import cn.bohoon.order.domain.InvoiceType;
import cn.bohoon.order.entity.AfterMarketOrder;
import cn.bohoon.order.entity.Order;
import cn.bohoon.order.entity.OrderInvoice;
import cn.bohoon.order.service.AfterMarketOrderService;
import cn.bohoon.order.service.OrderInvoiceService;
import cn.bohoon.order.service.OrderService;
import cn.bohoon.userInfo.domain.CreditLogType;
import cn.bohoon.userInfo.domain.ExpType;
import cn.bohoon.userInfo.domain.Member;
import cn.bohoon.userInfo.domain.PictureDefault;
import cn.bohoon.userInfo.domain.ScoreType;
import cn.bohoon.userInfo.domain.UserSex;
import cn.bohoon.userInfo.entity.CreditLog;
import cn.bohoon.userInfo.entity.ExpLog;
import cn.bohoon.userInfo.entity.ScoreLog;
import cn.bohoon.userInfo.entity.ShippingInfo;
import cn.bohoon.userInfo.entity.User;
import cn.bohoon.userInfo.entity.UserInfo;
import cn.bohoon.userInfo.entity.UserLevel;
import cn.bohoon.userInfo.service.CreditLogService;
import cn.bohoon.userInfo.service.ExpLogService;
import cn.bohoon.userInfo.service.ScoreLogService;
import cn.bohoon.userInfo.service.ShippingInfoService;
import cn.bohoon.userInfo.service.UserInfoService;
import cn.bohoon.userInfo.service.UserLevelService;
import cn.bohoon.userInfo.service.UserService;
import cn.bohoon.util.ResultUtil;

@Controller
@RequestMapping(value = "userInfo")
public class UserInfoController {

	
	@Autowired
	private UserService userService;
	
	@Autowired
	private UserInfoService userInfoService;

	@Autowired
	private ShippingInfoService shippingInfoService;

	@Autowired
	private ExpLogService expLogService;

	@Autowired
	private ScoreLogService scoreLogService;

	@Autowired
	private UserLevelService memberLevelService;
	
	@Autowired
	private OrderService orderService;
	
	@Autowired
	private CreditLogService creditLogService;
	
	@Autowired
	AfterMarketOrderService afterMarketOrderService ;
	
	@Autowired
	private OrderInvoiceService orderInvoiceService;

	@Autowired
	private SysParamService sysParamService;
	@Autowired
	OperatorLogService operatorLogService;
	@Autowired
	OperatorService operatorService;
	@Autowired
	JdbcTemplate jdbcTemplate;


	
	
	/**
	 * 会员列表
	 * 
	 * @param request
	 * @param model
	 * @param id
	 * @param phone
	 * @param email
	 * @param userLevel
	 * @param endTime
	 * @param startTime
	 * @return
	 * @throws ParseException
	 */
	@RequestMapping(value = "list", method = RequestMethod.GET)
	public String list(HttpServletRequest request, Model model) throws ParseException {
		String id = ServletRequestUtils.getStringParameter(request, "id", "") ;
		String phone = ServletRequestUtils.getStringParameter(request, "phone", "") ;
		String email = ServletRequestUtils.getStringParameter(request, "email", "") ;
		String userLevel = ServletRequestUtils.getStringParameter(request, "userLevel", "") ;
		String endTime = ServletRequestUtils.getStringParameter(request, "endTime", "") ;
		String startTime = ServletRequestUtils.getStringParameter(request, "startTime", "") ;
		Integer pageNo = ServletRequestUtils.getIntParameter(request, "pageNo", 1);
		Page<UserInfo> userInfoPage = new Page<UserInfo>();
		userInfoPage.setPageNo(pageNo);

		String hql = " from UserInfo where 1=1";
		List<Object> param = new ArrayList<>();

		if (!StringUtils.isEmpty(id)) {
			hql += " and id like '%"+id+"%' ";
			model.addAttribute("id", id);
		}
		if (!StringUtils.isEmpty(phone)) {
			hql += " and phone like ? ";
			param.add("%"+phone+"%");
			model.addAttribute("phone", phone);
		}
		if (!StringUtils.isEmpty(email)) {
			hql += " and email like ? ";
			param.add("%"+email+"%");
			model.addAttribute("email", email);
		}
		if(!StringUtils.isEmpty(userLevel)){
			hql +=" and userlevel = ?";
			param.add(userLevel);
			model.addAttribute("userLevel", userLevel);
		}
    	if (!StringUtils.isEmpty(startTime)){
    		hql += " and createTime >= ? ";
    		param.add(DateUtil.switchStringToDate(startTime, "yy-MM-dd"));
    		model.addAttribute("startTime", startTime);
        }
        if (!StringUtils.isEmpty(endTime)){
        	hql += " and createTime < ? ";
        	param.add(DateUtil.getNDayAfter(endTime, 1));
        	model.addAttribute("endTime", endTime);
        }
        hql += "order by createTime desc";

		userInfoPage = userInfoService.page(userInfoPage, hql, param.toArray());

		Map<UserInfo, UserLevel> memberLevelMap = new HashMap<UserInfo, UserLevel>();
		for (UserInfo userInfo : userInfoPage.getResult()) {
			UserLevel memberLevel = memberLevelService.getUserLevel(userInfo.getExp());
			memberLevelMap.put(userInfo, memberLevel);
		}	

		List<UserLevel> memberLevelList = memberLevelService.listUserLevelCache();
		
		SysParam sysParam = sysParamService.findParam(SysParamHelper.USER_DEFAULT_PASSWORD, SysParamType.PLATFORM_PARAM);
		model.addAttribute("defaultpwd", sysParam.getValue());
		//推荐人数
		Map<UserInfo,Integer> ContMap = new HashMap<UserInfo,Integer>();
		List<UserInfo> userInfoList=userInfoPage.getResult();
		
		for (UserInfo userInfo : userInfoList) {
			String sql="select count(1) from t_user_info where commendFriendId="+userInfo.getId()+"";
			int Cont=(Integer)jdbcTemplate.queryForObject(sql, java.lang.Integer.class);
			ContMap.put(userInfo,Cont);
		}
		
		
		model.addAttribute("memberLevelList", memberLevelList);
		model.addAttribute("memberLevelMap", memberLevelMap);
		model.addAttribute("userInfoPage", userInfoPage);
		model.addAttribute("ContMap", ContMap);
		return "userInfo/userInfoList";
	}

	/**
	 * 新增会员
	 * 
	 * @return
	 */
	@RequestMapping(value = "add", method = RequestMethod.GET)
	public String addGet(Model model) {
		SysParam sysParam = sysParamService.findParam(SysParamHelper.USER_DEFAULT_PASSWORD, SysParamType.PLATFORM_PARAM);
		
		model.addAttribute("defaultpwd", sysParam.getValue());
		
		return "userInfo/userInfoAdd";
	}

	/**
	 * 新增会员保存信息
	 * 
	 * @param request
	 * @param member
	 * @param birthday
	 * @return
	 * @throws Exception 
	 */
	@ResponseBody
	@RequestMapping(value = "add", method = RequestMethod.POST)
	public String addPost(HttpServletRequest request, Member member) throws Exception {
		UserInfo userinfo= member.getUserInfo();
		String phone = userinfo.getPhone();
		User user = userService.findUserByLoginName(phone) ;
		if(user!=null){
			return ResultUtil.getError("该手机号码已经绑定用户！");
		}
		if(userInfoService.isIdcard(userinfo.getIdcard())){
			return ResultUtil.getError("该身份证号已经存在！");
		}
		
		member.getUserInfo().setImageUrl(PictureDefault.DEFAULT_MAN);
		if(member.getUserInfo().getSex().equals(UserSex.WOMAN)){
			member.getUserInfo().setImageUrl(PictureDefault.DEFAULT_WOMAN);
		}
		userInfoService.save(member);
		
		LoginUser userif= UserSession.getLoginUser(request);
		Operator operator = operatorService.findUserByUsername(userif.getUsername());
		operatorLogService.addUserLog(operator, request, "新增会员:手机号"+phone);
		return ResultUtil.getSuccessMsg();
	}

	/**
	 * 编辑会员
	 * 
	 * @param id
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "edit", method = RequestMethod.GET)
	public String editGet(String id, Model model) {
		UserInfo userInfo = userInfoService.get(id);
		ShippingInfo shippingInfo = shippingInfoService.queryFirst(id) ;
		model.addAttribute("shippingInfo", shippingInfo);
		model.addAttribute("userInfo", userInfo);
		return "userInfo/userInfoEdit";
	}

	/**
	 * 编辑会员保存信息
	 * 
	 * @param request
	 * @param member
	 * @param birthday
	 * @return
	 * @throws Exception 
	 */
	@ResponseBody
	@RequestMapping(value = "edit", method = RequestMethod.POST)
	public String editPost(Member member,HttpServletRequest request) throws Exception {

		ShippingInfo shippinginfo = member.getShippingInfo();
		UserInfo userInfo = member.getUserInfo();

		UserInfo target = userInfoService.get(userInfo.getId());
		if(userInfoService.isIdcard(userInfo.getId())){
			target.setNickname(userInfo.getNickname());
		}
		target.setRealname(userInfo.getNickname());
		target.setSex(userInfo.getSex());
//		target.setQualityRate(userInfo.getQualityRate());
		if(!StringUtils.isEmpty(userInfo.getBirthday())) {
			target.setBirthday(userInfo.getBirthday());
		}
		
		target.setCommendFriendId(userInfo.getCommendFriendId());
		
		
		if(!StringUtils.isEmpty(userInfo.getIdcard())){
			target.setIdcard(userInfo.getIdcard());
		}

		userInfoService.save(target);
		shippingInfoService.execute(" update ShippingInfo set first = 0 where  userId = ? ", target.getId()); 
		
		
		shippinginfo.setFirst(true);
		shippinginfo.setUserId(target.getId());
		shippingInfoService.saveInfo(shippinginfo);
		//存储日志
		LoginUser userif= UserSession.getLoginUser(request);
		Operator operator = operatorService.findUserByUsername(userif.getUsername());
		operatorLogService.addUserLog(operator, request, "修改会员:手机号"+userInfo.getPhone());

		return ResultUtil.getSuccessMsg();

	}

	/**
	 * 编辑会员经验值
	 * 
	 * @param id
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "expEdit", method = RequestMethod.GET)
	public String editExp(@RequestParam(name="idArry[]",required=true)String idArry[], Model model) {
		model.addAttribute("idArry", org.apache.commons.lang.StringUtils.join(idArry, ','));
		return "userInfo/userInfoExpEdit";
	}

	/**
	 * 编辑保存会员经验值
	 * 
	 * @param type
	 * @param exp
	 * @param id
	 * @return
	 * @throws Exception 
	 */
	@ResponseBody
	@RequestMapping(value = "expEdit", method = RequestMethod.POST)
	public String editExpPost(ExpType type, Integer exp, String idArry[],HttpServletRequest request) throws Exception {
		for (String id : idArry) {
			expLogService.updateExp(id, type, exp);
		}
		LoginUser userif= UserSession.getLoginUser(request);
		Operator operator = operatorService.findUserByUsername(userif.getUsername());
		operatorLogService.addUserLog(operator, request, "修改会员id:"+org.apache.commons.lang.StringUtils.join(idArry,",")+"经验值");
		return ResultUtil.getSuccessMsg();
	}

	/**
	 * 编辑会员积分
	 * 
	 * @param id
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "scoreEdit", method = RequestMethod.GET)
	public String scoreEdit(@RequestParam(name="idArry[]",required=true)String idArry[], Model model) {
		model.addAttribute("idArry", org.apache.commons.lang.StringUtils.join(idArry, ','));
		return "userInfo/userInfoScoreEdit";
	}

	/**
	 * 编辑保存会员积分
	 * 
	 * @param type
	 * @param score
	 * @param id
	 * @return
	 * @throws Exception 
	 */
	@ResponseBody
	@RequestMapping(value = "scoreEdit", method = RequestMethod.POST)
	public String scoreEditPost(ScoreType type, Integer score, String idArray[],HttpServletRequest request) throws Exception {
		for (String id : idArray) {
			scoreLogService.updateScore(id, score, type);
		}
		LoginUser userif= UserSession.getLoginUser(request);
		Operator operator = operatorService.findUserByUsername(userif.getUsername());
		operatorLogService.addUserLog(operator, request, "修改会员id:"+org.apache.commons.lang.StringUtils.join(idArray,",")+"积分");
		
		return ResultUtil.getSuccessMsg();
	}
	
	/**
	 * 查看会员详情
	 * 
	 * @param requset
	 * @param model
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "detail", method = RequestMethod.GET)
	public String list(HttpServletRequest requset, Model model, String id) {

		UserInfo userInfo = userInfoService.get(id);

		UserLevel memberLevel =  memberLevelService.getUserLevel(userInfo.getExp());

		List<ShippingInfo> shippingInfoList = shippingInfoService.list(" from ShippingInfo  where userId = ? ",userInfo.getId());

		
		List<OrderInvoice> invoceList = orderInvoiceService.list(" from OrderInvoice where memberId = ? ",id);
		
		model.addAttribute("id", id);
		model.addAttribute("userInfo", userInfo);
		model.addAttribute("memberLevel", memberLevel);
		model.addAttribute("shippingInfoList", shippingInfoList);
		model.addAttribute("invoceList",invoceList);

		return "userInfo/Detail/userInfoDetail";
	}

	/**
	 * 用户积分明细
	 * 
	 * @param requset
	 * @param model
	 * @param scoreType
	 * @return
	 * @throws ParseException
	 */
	@RequestMapping(value = "scoreDetail", method = RequestMethod.GET)
	public String scoreLog(HttpServletRequest requset, Model model,ScoreType scoreType) throws ParseException {

		String id = ServletRequestUtils.getStringParameter(requset, "id", "");
		Integer pageNo = ServletRequestUtils.getIntParameter(requset, "pageNo", 1);
		String detail = ServletRequestUtils.getStringParameter(requset, "detail","") ;
		String startTime = ServletRequestUtils.getStringParameter(requset, "startTime","") ;
		String endTime = ServletRequestUtils.getStringParameter(requset, "endTime","") ;
		Page<ScoreLog> scoreLogPage = new Page<>();
		scoreLogPage.setPageNo(pageNo);
		
		String hql = " from ScoreLog where memberId = ? ";
		List<Object> params = new ArrayList<>();
		params.add(id);
		if(!StringUtils.isEmpty(scoreType)){
			hql += " and scoreType = ? ";
			params.add(scoreType);
			model.addAttribute("scoreType",scoreType);
		}
		if(!StringUtils.isEmpty(detail)){
			hql +=" and note like ? ";
			params.add("%"+detail+"%");
			model.addAttribute("detail", detail);
		}
    	if (!StringUtils.isEmpty(startTime)){
    		hql = hql + " and  createDate >= ?   ";
    		params.add(DateUtil.switchStringToDate(startTime, "yy-MM-dd"));
    		model.addAttribute("startTime", startTime);
        }
        if (!StringUtils.isEmpty(endTime)){
        	hql = hql + " and createDate < ?  ";
        	params.add(DateUtil.getNDayAfter(endTime, 1));
        	model.addAttribute("endTime", endTime);
        }
		
		
		scoreLogPage = scoreLogService.page(scoreLogPage,hql,params.toArray());
		model.addAttribute("scoreLogPage", scoreLogPage);

		UserInfo user = userInfoService.get(id);
		model.addAttribute("user", user);
		model.addAttribute("id", id);
		return "userInfo/Detail/userInfoScoreDetail";
	}

	@RequestMapping(value = "commendfriendDetail", method = RequestMethod.GET)
	public String shareDateil(HttpServletRequest requset, Model model, Integer id,Boolean concern,String nickName , Integer userId , String startTime,String endTime) throws ParseException {
		Integer pageNo = ServletRequestUtils.getIntParameter(requset, "pageNo", 1);
		Page<UserInfo> userInfoPage = new Page<>();
		userInfoPage.setPageNo(pageNo);
		String hql =" from UserInfo where 1 = 1 and  commendFriendId = ? ";
		List<Object> params = new ArrayList<>();
		params.add(id);
		if(!StringUtils.isEmpty(nickName)){
			hql += " and nickName like ? ";
			params.add("%"+nickName+"%");
			model.addAttribute("nickName",nickName);
		}
		if(!StringUtils.isEmpty(userId)){
			hql += " and id = ?";
			params.add(userId);
			model.addAttribute("userId",userId);
		}
		if(concern!=null){
			hql +=" and concern = ?";
			params.add(concern);
			model.addAttribute("concern", concern);
		}
    	if (!StringUtils.isEmpty(startTime)){
    		hql = hql + " and createTime >= ? ";
    		params.add(DateUtil.switchStringToDate(startTime, "yy-MM-dd"));
    		model.addAttribute("startTime", startTime);
        }
        if (!StringUtils.isEmpty(endTime)){
        	hql = hql + " and createTime < ?  ";
        	params.add(DateUtil.getNDayAfter(endTime, 1));
        	model.addAttribute("endTime", endTime);
        }
		
		
		userInfoPage = userInfoService.page(userInfoPage, hql,params.toArray());
		model.addAttribute("userInfoPage", userInfoPage);
		model.addAttribute("id", id);
		return "userInfo/Detail/userInfoCommendfriendDetail";
	}

	@RequestMapping(value = "expDetail", method = RequestMethod.GET)
	public String experienceDetail(HttpServletRequest requset, Model model, String id , ExpType expType,String orderId,String startTime,String endTime) throws ParseException {
		Integer pageNo = ServletRequestUtils.getIntParameter(requset, "pageNo", 1);
		Page<ExpLog> expLogPage = new Page<>();
		expLogPage.setPageNo(pageNo);
		
		String hql = " from ExpLog where memberId = ? ";
		List<Object> params = new ArrayList<>();
		params.add(id);
		if(!StringUtils.isEmpty(expType)){
			hql += " and expType = ? ";
			params.add(expType);
			model.addAttribute("expType",expType);
		}
		if(!StringUtils.isEmpty(orderId)){
			hql +=" and orderId = ? ";
			params.add(orderId);
			model.addAttribute("orderId", orderId);
		}
    	if (!StringUtils.isEmpty(startTime)){
    		hql = hql + " and createDate >= ? ";
    		params.add(DateUtil.switchStringToDate(startTime, "yy-MM-dd"));
    		model.addAttribute("startTime", startTime);
        }
        if (!StringUtils.isEmpty(endTime)){
        	hql = hql + " and createDate < ?  ";
        	params.add(DateUtil.getNDayAfter(endTime, 1));
        	model.addAttribute("endTime", endTime);
        }
		
		
		hql +=" ORDER BY id DESC";
		expLogPage = expLogService.page(expLogPage, hql, params.toArray());
		model.addAttribute("expLogPage", expLogPage);
		model.addAttribute("id", id);
		return "userInfo/Detail/userInfoExpDetail";
	}
	
	/**
	 * 售后明细
	 * 
	 * @param requset
	 * @param model
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "afterSaleDetail", method = RequestMethod.GET)
	public String afterSaleDetail(HttpServletRequest requset, Model model, String id) {
		model.addAttribute("id", id);
		Page<AfterMarketOrder> page =new Page<AfterMarketOrder>();
		String hql =  " from AfterMarketOrder where 1 = 1 and userId = ? ";
		List<Object> params = new ArrayList<>();
		params.add(id);
		
		page = afterMarketOrderService.page(page,hql, params.toArray()) ;
		model.addAttribute("page", page);
		return "userInfo/Detail/userInfoAfterSaleDetail";
	}
	
	/**
	 * 用户订单
	 * 
	 * @param requset
	 * @param model
	 * @param id
	 * @param orderId
	 * @param orderMoney
	 * @param startTime
	 * @param endTime
	 * @return
	 * @throws ParseException
	 */
	@RequestMapping(value = "orderDetail", method = RequestMethod.GET)
	public String orderDetail(HttpServletRequest requset, Model model, String id,String orderId , BigDecimal orderMoney,String startTime,String endTime) throws ParseException {
		Integer pageNo = ServletRequestUtils.getIntParameter(requset, "pageNo", 1);
		
		Page<Order> page =new Page<>();
		page.setPageNo(pageNo);
		String hql =  " from Order where 1 = 1 and userId = ? ";
		List<Object> params = new ArrayList<>();
		params.add(id);
		if(!StringUtils.isEmpty(orderId)){
			hql += " and id = ?";
			params.add(orderId);
			model.addAttribute("orderId",orderId);
		}
		if(!StringUtils.isEmpty(orderMoney)){
			hql +=" and total = ? ";
			params.add(orderMoney);
			model.addAttribute("orderMoney", orderMoney);
		}
    	if (!StringUtils.isEmpty(startTime)){
    		hql = hql + " and payDate >= ? ";
    		params.add(DateUtil.switchStringToDate(startTime, "yy-MM-dd"));
    		model.addAttribute("startTime", startTime);
        }
        if (!StringUtils.isEmpty(endTime)){
        	hql = hql + " and payDate < ?  ";
        	params.add(DateUtil.getNDayAfter(endTime, 1));
        	model.addAttribute("endTime", endTime);
        }
		
		
		page = orderService.page(page,hql,params.toArray());
		model.addAttribute("orderPage",page);
		model.addAttribute("id", id);
		return "userInfo/Detail/userInfoOrderDetail";
	}
	@RequestMapping(value = "creditDetail", method = RequestMethod.GET)
	public String creditDetail(HttpServletRequest requset, Model model, String id,CreditLogType creditLogType,String detail,String startTime,String endTime) throws ParseException {
		Integer pageNo= ServletRequestUtils.getIntParameter(requset, "pageNo",1);
		Page<CreditLog> page = new Page<>();
		page.setPageNo(pageNo);
		String hql = " from CreditLog where  1 = 1 and userId = ? ";
		List<Object> params =new ArrayList<>();
		params.add(id);
		model.addAttribute("id",id);
		if(!StringUtils.isEmpty(creditLogType)){
			hql += " and creditLogType = ?";
			params.add(creditLogType);
			model.addAttribute("creditLogType", creditLogType);
		}
		if(!StringUtils.isEmpty(detail)){
			hql +=" and detail like ? ";
			params.add("%"+detail+"%");
			model.addAttribute("detail", detail);
		}
    	if (!StringUtils.isEmpty(startTime)){
    		hql = hql + " and createDate >= ? ";
    		params.add(DateUtil.switchStringToDate(startTime, "yy-MM-dd"));
    		model.addAttribute("startTime", startTime);
        }
        if (!StringUtils.isEmpty(endTime)){
        	hql = hql + " and createDate < ?  ";
        	params.add(DateUtil.getNDayAfter(endTime, 1));
        	model.addAttribute("endTime", endTime);
        }
		
		page = creditLogService.page(page,hql,params.toArray());
		model.addAttribute("creditPage", page);
		return "userInfo/Detail/userInfoCreditDetail";
	}
	
	@ResponseBody
	@RequestMapping(value="userInfoList",method=RequestMethod.GET)
	public String getUserInfoList(){
		 List<UserInfo> list= userInfoService.list();
		return JsonUtil.toJson(list);
	}
	
	@RequestMapping(value="invoiceDetail" ,method = RequestMethod.GET)
	public String invoiceList(HttpServletRequest requset,String id,Model model , InvoiceType invoiceType ,String orderId, String title){
		Integer pageNo= ServletRequestUtils.getIntParameter(requset, "pageNo",1);
		
		Page<Order> orderPage = new Page<>();
		orderPage.setPageNo(pageNo);
		
		List<Object> params = new ArrayList<>();
		String hql = "select o from Order as  o , OrderInvoice as i where o.invoiceId=i.id and invoiceId is not null and userId = ?  ";
		params.add(id);
		if(!StringUtils.isEmpty(invoiceType)){
			hql += " and  i.invoiceType = ? ";
			params.add(invoiceType);
			model.addAttribute("invoiceType", invoiceType);
		}
		if(!StringUtils.isEmpty(orderId)){
			hql += " and  o.id = ? ";
			params.add(orderId);
			model.addAttribute("orderId", orderId);
		}
		if(!StringUtils.isEmpty(title)){
			hql += " and  i.title like  ? ";
			params.add("%"+title+"%");
			model.addAttribute("title", title);
		}
		Map<Order,OrderInvoice> orderInvoiceMap  = new HashMap<>();
		orderPage = orderService.page(orderPage,hql,params.toArray());
		
		for (Order order : orderPage.getResult()) {
			OrderInvoice orderInvoice = orderInvoiceService.get(order.getInvoiceId());
			orderInvoiceMap.put(order,orderInvoice);
		}
		
		model.addAttribute("orderInvoiceMap", orderInvoiceMap);
		model.addAttribute("orderPage", orderPage);
		model.addAttribute("id",id);
		return "userInfo/Detail/userInfoInvoiceDetail";
	}
	
	@ResponseBody
	@RequestMapping(value="/getUser",method = RequestMethod.GET)
	public String getUser(String id , String phone , String str ,
			@RequestParam(name="pageNo",defaultValue="1")Integer pageNo,String companyId){
		Page<UserInfo> pageUserinfo = new Page<>();
		pageUserinfo.setPageNo(pageNo);
		String hql =" from UserInfo where  ((companyId = ? and id != (select userId from Company where id=?))  ";
		hql += " or companyId not in (select id from Company)) ";
		
		List<Object> params = new ArrayList<>();
		params.add(companyId);
		params.add(companyId);
		if(!StringUtils.isEmpty(id)){
			hql +=" and id like ?";
			params.add(id+"%");
		}
		if(!StringUtils.isEmpty(str)){
			hql +=" and realname like  ? ";
			params.add(str+"%");
		}
		pageUserinfo = userInfoService.page(pageUserinfo, hql,params.toArray());
		return JsonUtil.toJson(pageUserinfo);
	}
	
	
	@ResponseBody
	@RequestMapping(value="/getUserInfo",method = RequestMethod.GET)
	public String getUser_info(String phone,
			@RequestParam(name="pageNo",defaultValue="1")Integer pageNo){
		Page<UserInfo> pageUserinfo = new Page<>();
		pageUserinfo.setPageNo(pageNo);
		
		if (!StringUtils.isEmpty(phone)){
			
			
			String hql = "from UserInfo where realname like ? or nickname like ? or phone like ?";
			
			
			List<Object> params = new ArrayList<>();
			params.add("%"+phone+"%");
			params.add("%"+phone+"%");
			params.add("%"+phone+"%");
			pageUserinfo = userInfoService.page(pageUserinfo, hql,params.toArray());
		}
		
		
		/*
		if(!StringUtils.isEmpty(id)){
			hql +=" and id like ?";
			params.add(id+"%");
		}
		if(!StringUtils.isEmpty(str)){
			hql +=" and realname like  ? ";
			params.add(str+"%");
		}*/
		return JsonUtil.toJson(pageUserinfo);
	}
	
	
	
	@ResponseBody
	@RequestMapping(value="/getRecommendUser",method = RequestMethod.GET)
	public String getRecommendUser(String id , String phone , String str ,
			@RequestParam(name="pageNo",defaultValue="1")Integer pageNo,String userId){
		Page<UserInfo> pageUserinfo = new Page<>();
		pageUserinfo.setPageNo(pageNo);
		String hql ="from UserInfo where id !=?";
		List<Object> params = new ArrayList<>();
		params.add(userId);
		if(!StringUtils.isEmpty(id)){
			hql +=" and id like ?";
			params.add(id+"%");
		}
		if(!StringUtils.isEmpty(phone)){
			hql +=" or phone like ?";
			params.add(phone+ "%");
		}
			
		if(!StringUtils.isEmpty(str)){
			hql +=" and realname like  ? ";
			params.add(str+"%");
		}
		pageUserinfo = userInfoService.page(pageUserinfo, hql,params.toArray());
		return JsonUtil.toJson(pageUserinfo);
	}
	
	
	
	
	/**
	 * 获取全部用户
	 * 
	 * @param id
	 * @param phone
	 * @param str
	 * @param pageNo
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/getAllUser",method = RequestMethod.GET)
	public String getAllUser(String id , String phone , String str ,@RequestParam(name="pageNo",defaultValue="1")Integer pageNo){
		Page<UserInfo> pageUserinfo = new Page<>();
		pageUserinfo.setPageNo(pageNo);
		String hql =" from UserInfo where 1=1 ";
		List<Object> params = new ArrayList<>();
		if(!StringUtils.isEmpty(id) && !StringUtils.isEmpty(phone)){
			hql +=" and ( id like ?  or phone like ? )";
			params.add("%"+id+"%");
			params.add("%"+phone+"%");
		}
		if(!StringUtils.isEmpty(str)){
			hql +=" and realname like  ? ";
			params.add("%"+str+"%");
		}
		pageUserinfo = userInfoService.page(pageUserinfo, hql,params.toArray());
		return JsonUtil.toJson(pageUserinfo);
	}
	
	/**
	 * 删除用户
	 * @param uIArray
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value="/delete",method=RequestMethod.POST)
	public @ResponseBody String delete(@RequestParam(name="uIArray[]")String uIArray[],HttpServletRequest request) throws Exception{
		for (String userId : uIArray) {
			userInfoService.deleteAll(userId);
		}
		
		//存储日志 HttpServletRequest request
		LoginUser userif= UserSession.getLoginUser(request);
		Operator operator = operatorService.findUserByUsername(userif.getUsername());
		operatorLogService.addUserLog(operator, request, "删除id:"+org.apache.commons.lang.StringUtils.join(uIArray,",")+"用户");
		
		return ResultUtil.getSuccessMsg();
	}
	
	/**
	 * 重置密码
	 * @param uIArray
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value="/reinstall",method=RequestMethod.POST)
	public @ResponseBody String reinstall(@RequestParam(name="uIArray[]")String uIArray[],HttpServletRequest request) throws Exception{
		for (String userId : uIArray) {
			userService.reinstall(userId);
		}
		//存储日志 HttpServletRequest request
		LoginUser userif= UserSession.getLoginUser(request);
		Operator operator = operatorService.findUserByUsername(userif.getUsername());
		operatorLogService.addUserLog(operator, request, "重置用户密码id:"+org.apache.commons.lang.StringUtils.join(uIArray,",")+"密码");
		 
		return ResultUtil.getSuccessMsg();
	}
	
	/**
	 * 拉选机构获取收货地址
	 * 
	 * @param companyId
	 * @return
	 */
	@RequestMapping("/getShippingByCompanyId")
	@ResponseBody
	public String getShippingByCompanyId(String companyId){
		String hql = " from ShippingInfo  where companyId = ? " ;
		List<ShippingInfo> list = shippingInfoService.list(hql,companyId) ;
		return ResultUtil.getData(0, "成功", list) ;
	}
	
}
