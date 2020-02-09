package cn.bohoon.payment.web;



import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.bohoon.framework.orm.domain.Page;
import cn.bohoon.framework.util.JsonUtil;
import cn.bohoon.main.domain.LoginUser;
import cn.bohoon.main.domain.UserSession;
import cn.bohoon.main.system.entity.Operator;
import cn.bohoon.main.system.service.OperatorLogService;
import cn.bohoon.main.system.service.OperatorService;
import cn.bohoon.payment.domain.AlipayVo;
import cn.bohoon.payment.domain.WechatPayVo;
import cn.bohoon.payment.entity.PaymentType;
import cn.bohoon.payment.service.PaymentTypeService;
import cn.bohoon.util.ResultUtil;

@Controller
@RequestMapping(value = "paymentType")
public class PaymentTypeController {

    @Autowired
    PaymentTypeService paymentTypeService;


    @Autowired
    OperatorLogService operatorLogService;
    @Autowired
    OperatorService operatorService;

    @RequestMapping(value = "list", method = RequestMethod.GET)
    public String list(HttpServletRequest request,Model model)  {
        Integer pageNo=ServletRequestUtils.getIntParameter(request, "pageNo",1);

        Page<PaymentType> pagePaymentType=new Page<PaymentType>(5);
        pagePaymentType.setPageNo(pageNo);


        pagePaymentType=paymentTypeService.page(pagePaymentType, "from PaymentType");

        model.addAttribute("pagePaymentType", pagePaymentType);
        return "paymentType/paymentTypeList";
    }
    
    @RequestMapping(value = "add", method = RequestMethod.GET)
    public String addGet(HttpServletRequest request,Model model)  {
        return "paymentType/paymentTypeAdd";
    }
    
    @ResponseBody
    @RequestMapping(value = "add", method = RequestMethod.POST)
    public String addPost(HttpServletRequest request,PaymentType paymentType) throws Exception  {
        paymentTypeService.save(paymentType);

 		//保存日志,HttpServletRequest request
       	LoginUser userif= UserSession.getLoginUser(request);
		Operator operator = operatorService.findUserByUsername(userif.getUsername());
		operatorLogService.addUserLog(operator, request, "新增支付类型:"+paymentType.getName());
		return ResultUtil.getSuccessMsg();
    }
    
    @RequestMapping(value = "edit", method = RequestMethod.GET)
    public String editGet(HttpServletRequest request,Model model) {
        Integer id=ServletRequestUtils.getIntParameter(request, "id",-1);
        PaymentType paymentType=paymentTypeService.get(id);
        model.addAttribute("item",paymentType);
        return "paymentType/paymentTypeEdit";
    }
    
    /**
     * 设置支付密码
     * 
     * @param request
     * @param model
     * @return
     */
    @RequestMapping(value = "setPass", method = RequestMethod.GET)
    public String setPassGet(HttpServletRequest request,Model model) {
        Integer id=ServletRequestUtils.getIntParameter(request, "id",-1);
        PaymentType paymentType=paymentTypeService.get(id);
        model.addAttribute("item",paymentType);
        return "paymentType/setPass";
    }
    
    @ResponseBody
    @RequestMapping(value = "setPass", method = RequestMethod.POST)
    public String setPassPost(HttpServletRequest request,PaymentType model) throws Exception {
    	PaymentType entity = paymentTypeService.get(model.getId()) ;
    	String oldPassword = ServletRequestUtils.getStringParameter(request, "oldPassword","");
    	String sureReFundPwd = ServletRequestUtils.getStringParameter(request, "sureReFundPwd","");
    	String newPassword = model.getReFundPwd() ;
    	boolean flag = true ;
    	if(!StringUtils.isEmpty(entity.getReFundPwd())) {
    		if(!oldPassword.equals(entity.getReFundPwd())) {
    			flag = false ;
    		}
    	}
    	if(!sureReFundPwd.equals(newPassword)) {
    		flag = false ;
    	}
    	if(!flag) {
    		return ResultUtil.getError("密码输入有误！") ;
    	}
    	entity.setReFundPwd(newPassword);
    	paymentTypeService.save(entity);
    	//保存日志,HttpServletRequest request
       	LoginUser userif= UserSession.getLoginUser(request);
		Operator operator = operatorService.findUserByUsername(userif.getUsername());
		operatorLogService.addUserLog(operator, request, "设置支付类型退款密码");
        return ResultUtil.getSuccessMsg();
    }
    
    
	@ResponseBody
    @RequestMapping(value = "edit", method = RequestMethod.POST)
    public String editPost(PaymentType paymentType,HttpServletRequest request) throws Exception {
		PaymentType py = paymentTypeService.get(paymentType.getId()) ;
		py.setName(paymentType.getName());
		py.setDisplay(paymentType.getDisplay());
		py.setIntro(paymentType.getIntro());
		
        paymentTypeService.save(py);
        //保存日志,HttpServletRequest request
       	LoginUser userif= UserSession.getLoginUser(request);
		Operator operator = operatorService.findUserByUsername(userif.getUsername());
		operatorLogService.addUserLog(operator, request, "修改支付类型:"+paymentType.getName());
        return ResultUtil.getSuccessMsg();
    }

	@ResponseBody
    @RequestMapping(value = "/delete")
    public String delete(HttpServletRequest request,Integer id) throws Exception {
        paymentTypeService.delete(id);
        //保存日志,HttpServletRequest request
       	LoginUser userif= UserSession.getLoginUser(request);
		Operator operator = operatorService.findUserByUsername(userif.getUsername());
		operatorLogService.addUserLog(operator, request, "删除支付类型:id"+id.toString());
		return ResultUtil.getSuccessMsg();
    }
	
	@RequestMapping(value = "config", method = RequestMethod.GET)
    public String config(HttpServletRequest request,Model model) {
        Integer id=ServletRequestUtils.getIntParameter(request, "id",-1);
        PaymentType paymentType=paymentTypeService.get(id);
        model.addAttribute("item",paymentType);
        return "paymentType/"+paymentType.getCode();
    }
	
	@ResponseBody
	@RequestMapping(value = "alipay", method = RequestMethod.POST)
    public String alipay(AlipayVo alipayConfig,Integer id,HttpServletRequest request) throws Exception  {
		PaymentType paymentType=paymentTypeService.get(id);
		paymentType.setConfigJson(JsonUtil.toJson(alipayConfig));
		paymentTypeService.save(paymentType);
		 //保存日志,HttpServletRequest request
       	LoginUser userif= UserSession.getLoginUser(request);
		Operator operator = operatorService.findUserByUsername(userif.getUsername());
		operatorLogService.addUserLog(operator, request, "更改支付类型参数:id"+id.toString());
		return ResultUtil.getSuccessMsg();
    }

	@ResponseBody
	@RequestMapping(value = "weixin", method = RequestMethod.POST)
    public String weixin(WechatPayVo weChatPayVo,Integer id,HttpServletRequest request) throws Exception  {
		PaymentType paymentType=paymentTypeService.get(id);
		paymentType.setConfigJson(JsonUtil.toJson(weChatPayVo));
		paymentTypeService.save(paymentType);
		 //保存日志,HttpServletRequest request
       	LoginUser userif= UserSession.getLoginUser(request);
		Operator operator = operatorService.findUserByUsername(userif.getUsername());
		operatorLogService.addUserLog(operator, request, "更改支付类型参数:id"+id.toString());
		return ResultUtil.getSuccessMsg();
    }
	
	@ResponseBody
	@RequestMapping(value = "wxminiapp", method = RequestMethod.POST)
    public String wxminiapp(WechatPayVo weChatPayVo,Integer id,HttpServletRequest request) throws Exception  {
		PaymentType paymentType=paymentTypeService.get(id);
		paymentType.setConfigJson(JsonUtil.toJson(weChatPayVo));
		paymentTypeService.save(paymentType);
		 //保存日志,HttpServletRequest request
       	LoginUser userif= UserSession.getLoginUser(request);
		Operator operator = operatorService.findUserByUsername(userif.getUsername());
		operatorLogService.addUserLog(operator, request, "更改支付类型参数:id"+id.toString());
		return ResultUtil.getSuccessMsg();
    }
	
	/**
	 * 状态切换
	 * 
	 * @param request
	 * @param id
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value = "/wapSwitchStatus")
	@ResponseBody
	public String switchStatus(HttpServletRequest request, Integer id) throws Exception {
		PaymentType paymentType=paymentTypeService.get(id);
		paymentType.setIsWap(!paymentType.getIsWap());
		paymentTypeService.save(paymentType);
		 //保存日志,HttpServletRequest request
       	LoginUser userif= UserSession.getLoginUser(request);
		Operator operator = operatorService.findUserByUsername(userif.getUsername());
		operatorLogService.addUserLog(operator, request, "切换支付类型状态:id"+id.toString());
		return ResultUtil.getSuccessMsg();
	}
	
	/**
	 * 状态切换
	 * 
	 * @param request
	 * @param id
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value = "/pcSwitchStatus")
	@ResponseBody
	public String pcSwitchStatus(HttpServletRequest request, Integer id) throws Exception {
		PaymentType paymentType=paymentTypeService.get(id);
		paymentType.setIsPC(!paymentType.getIsPC());
		paymentTypeService.save(paymentType);
		 //保存日志,HttpServletRequest request
       	LoginUser userif= UserSession.getLoginUser(request);
		Operator operator = operatorService.findUserByUsername(userif.getUsername());
		operatorLogService.addUserLog(operator, request, "切换支付类型状态:id"+id.toString());
		return ResultUtil.getSuccessMsg();
	}
}