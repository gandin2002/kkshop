package cn.bohoon.order.web;

import java.math.BigDecimal;
import java.net.URLEncoder;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.bohoon.company.entity.Company;
import cn.bohoon.company.entity.Invoice;
import cn.bohoon.company.entity.License;
import cn.bohoon.company.service.CompanyService;
import cn.bohoon.company.service.InvoiceService;
import cn.bohoon.company.service.LicenseService;
import cn.bohoon.excel.util.ExcelWrite;
import cn.bohoon.framework.orm.domain.Page;
import cn.bohoon.framework.util.DateUtil;
import cn.bohoon.main.domain.LoginUser;
import cn.bohoon.main.domain.UserSession;
import cn.bohoon.main.system.entity.Operator;
import cn.bohoon.main.system.service.OperatorLogService;
import cn.bohoon.main.system.service.OperatorService;
import cn.bohoon.order.domain.InvoiceType;
import cn.bohoon.order.domain.OrderCheckState;
import cn.bohoon.order.entity.Order;
import cn.bohoon.order.entity.OrderInvoice;
import cn.bohoon.order.service.OrderInvoiceService;
import cn.bohoon.order.service.OrderLogService;
import cn.bohoon.order.service.OrderService;
import cn.bohoon.util.ResultUtil;

@Controller
@RequestMapping(value = "orderInvoice")
public class OrderInvoiceController {

	@Autowired
    OrderService orderService;
	
	@Autowired
	InvoiceService invoiceService ;
	
	@Autowired
    OrderLogService orderLogService;
	
    @Autowired
    OrderInvoiceService orderInvoiceService;
    
    @Autowired
    CompanyService companyService;
    
    @Autowired
    LicenseService licenseService;

    @Autowired
    OperatorLogService operatorLogService;
    @Autowired
    OperatorService operatorService;
    
    @RequestMapping(value = "list", method = RequestMethod.GET)
	public String list(HttpServletRequest request, Model model) throws ParseException {
		Integer pageNo = ServletRequestUtils.getIntParameter(request, "pageNo", 1);
		
		String invoiceType = ServletRequestUtils.getStringParameter(request, "invoiceType","").trim();
        String companyName = ServletRequestUtils.getStringParameter(request, "companyName","").trim();
        String title = ServletRequestUtils.getStringParameter(request, "title", "");
        String payment = ServletRequestUtils.getStringParameter(request, "payment","").trim();
		String state = ServletRequestUtils.getStringParameter(request, "state","").trim();
        String startTime = ServletRequestUtils.getStringParameter(request, "startTime","");
		String endTime = ServletRequestUtils.getStringParameter(request, "endTime","");
		String makeStartTime = ServletRequestUtils.getStringParameter(request, "makeStartTime","");
		String makeEndTime = ServletRequestUtils.getStringParameter(request, "makeEndTime","");

		Page<OrderInvoice> pageOrderInvoice = new Page<OrderInvoice>();
		pageOrderInvoice.setPageNo(pageNo);
		
		StringBuilder hql = new StringBuilder("from OrderInvoice i where 1 = 1");
		List<Object> params = new ArrayList<>();
		if (!StringUtils.isEmpty(invoiceType)) {
			hql.append(" and i.invoiceType =?");
			params.add(Enum.valueOf(InvoiceType.class, invoiceType));
			model.addAttribute("invoiceType", invoiceType);
		}
		if(!StringUtils.isEmpty(companyName)){
        	Company company = companyService.select("from Company c where c.name =?", companyName);
        	if(!StringUtils.isEmpty(company)){
        		hql.append(" and i.companyId =?");
        		params.add(company.getId());
        	}else{
        		hql.append(" and i.companyId =-1");
        	}
        	model.addAttribute("companyName", companyName);
        }
		if (!StringUtils.isEmpty(title)) {
			hql.append(" and i.title like ?");
        	params.add('%' + title + '%');
        	model.addAttribute("title", title);
		}
		if (!StringUtils.isEmpty(payment)) {
			hql.append(" and i.payment =?");
			params.add(new BigDecimal(payment));
			model.addAttribute("payment", payment);
		}
		if (!StringUtils.isEmpty(state)) {
			hql.append(" and i.state =?");
			params.add(Integer.valueOf(state));
			model.addAttribute("state", state);
		}
		if(!StringUtils.isEmpty(startTime)){
        	hql.append(" and i.creatDate >= ?");
        	params.add(DateUtil.switchStringToDate(startTime, "yy-MM-dd"));
        	model.addAttribute("startTime", startTime);
        }
        if(!StringUtils.isEmpty(endTime)){
        	hql.append(" and i.creatDate < ?");
        	params.add(DateUtil.getNDayAfter(endTime, 1));
        	model.addAttribute("endTime", endTime);
        }
        if(!StringUtils.isEmpty(makeStartTime)){
        	hql.append(" and i.makeDate >= ?");
        	params.add(DateUtil.switchStringToDate(makeStartTime, "yy-MM-dd"));
        	model.addAttribute("makeStartTime", makeStartTime);
        }
        if(!StringUtils.isEmpty(makeEndTime)){
        	hql.append(" and i.makeDate < ?");
        	params.add(DateUtil.getNDayAfter(makeEndTime, 1));
        	model.addAttribute("makeEndTime", makeEndTime);
        }
		hql.append(" order by i.id desc");
		
		pageOrderInvoice = orderInvoiceService.page(pageOrderInvoice, hql.toString(),params.toArray());
		
		Map<OrderInvoice, Company> companyMap = new HashMap<>();
		for(OrderInvoice orderInvoice : pageOrderInvoice.getResult()){
			if(StringUtils.isEmpty(orderInvoice.getCompanyId())){
				continue;
			}
			companyMap.put(orderInvoice, companyService.get(orderInvoice.getCompanyId()));
		}
		
		model.addAttribute("pageOrderInvoice", pageOrderInvoice);
		model.addAttribute("companyMap", companyMap);
		return "orderInvoice/orderInvoiceList";
	}
    
    @RequestMapping(value = "invoice", method = RequestMethod.GET)
    public String invoiceGet(HttpServletRequest request,Model model) throws Exception {
        String id=ServletRequestUtils.getStringParameter(request, "id") ;
        Order order = orderService.get(id) ;
        Integer invoiceId = order.getInvoiceId() ;
        String invHql = " from Invoice where companyId=? " ;
        if(invoiceId != null) {
        	 OrderInvoice invoice=orderInvoiceService.get(invoiceId);
        	 model.addAttribute("flag","edit");
        	 model.addAttribute("item",invoice);
             
        } else {
        	String hql = " from Invoice where companyId=? and isDefault=true" ;
        	Invoice invoice = invoiceService.select(hql, order.getMemberId()) ;
        	model.addAttribute("flag","add");
        	model.addAttribute("item",invoice);
        	
        }
        List<Invoice> invs = invoiceService.list(invHql, order.getMemberId()) ;
        model.addAttribute("invs",invs);
        model.addAttribute("order",order);
        return "orderInvoice/orderInvoice";
    }

	@ResponseBody
    @RequestMapping(value = "invoice", method = RequestMethod.POST)
    public String invoicePost(HttpServletRequest request,OrderInvoice invoice) throws Exception {
		String flag = ServletRequestUtils.getStringParameter(request, "flag", "") ;
		String orderId = ServletRequestUtils.getStringParameter(request, "orderId", "") ;
		Order order = orderService.get(orderId) ;
		if(flag.equals("add")) {
			invoiceService.save(invoice);
		}
		invoice.setCreatDate(new Date());
		orderInvoiceService.save(invoice);
		order.setInvoiceId(invoice.getId());
		orderService.save(order);
		
		LoginUser user = UserSession.getLoginUser(request) ;
		String note = user.getUsername()+"执行订单开票！" ;
		OrderCheckState ocs = OrderCheckState.CONFIRM_PASS ;
		orderLogService.save(user.getUsername(),order,ocs,note);
		//保存日志,HttpServletRequest request
   		LoginUser userif= UserSession.getLoginUser(request);
		Operator operator = operatorService.findUserByUsername(userif.getUsername());
		operatorLogService.addUserLog(operator, request, "新增订单开发票:id"+order.getId());
        return ResultUtil.getSuccessMsg();
    }
	
	@RequestMapping(value = "detail", method = RequestMethod.GET)
	public String addDetail(HttpServletRequest request, Model model,Integer id) {	
		OrderInvoice orderInvoice = orderInvoiceService.get(id);
//		企业查询营业执照地址
		if(!StringUtils.isEmpty(orderInvoice.getCompanyId())){
			List<License> list = licenseService.list("from License l where l.companyid =?", orderInvoice.getCompanyId());
			if(list != null && list.size()>0){
				model.addAttribute("licenseItem", list.get(0));
			}
		}
		
		//发票信息(增值税)查询纳税人资格证、其它证地址
			
		model.addAttribute("item", orderInvoice);
		return "orderInvoice/orderInvoiceDetail";
	}
	
	@RequestMapping(value = "detailInvoice", method = RequestMethod.GET)
	public String examineInvoice(HttpServletRequest request, Model model,Integer id) {	
		Invoice invoice  = 	invoiceService.get(id);
		Company	company  =  companyService.get(invoice.getCompanyId());
		License	license	 =	licenseService.get(company.getId());
		model.addAttribute("license",license);
		model.addAttribute("invoice",invoice);
		model.addAttribute("company",company);
		return "orderInvoice/orderInvoiceDetail";
	}
		
	
	@ResponseBody
	@RequestMapping(value = "checkBatchPass", method = RequestMethod.POST)
    public String checkBatchPassPost(HttpServletRequest request,Model model,String checkboxes) throws Exception {
		LoginUser user = UserSession.getLoginUser(request) ;
		String [] checkBoxes = checkboxes.split(",");
		
		List<OrderInvoice> orderInvoiceList = new ArrayList<>();
		for(int i=0,j=checkBoxes.length;i<j;i++){
			OrderInvoice orderInvoice = orderInvoiceService.get(Integer.parseInt(checkBoxes[i]));
			if(StringUtils.isEmpty(orderInvoice)){
				continue;
			}
			if(orderInvoice.getState()==1){
				continue;
			}
			orderInvoice.setName(user.getUsername());
			orderInvoice.setMakeDate(new Date());
			orderInvoice.setState(1);
			orderInvoiceList.add(orderInvoice);
		}
		if(orderInvoiceList.size()>0){
			orderInvoiceService.saveBatch(orderInvoiceList, orderInvoiceList.size());
		}
		//保存日志,HttpServletRequest request
   		LoginUser userif= UserSession.getLoginUser(request);
		Operator operator = operatorService.findUserByUsername(userif.getUsername());
		operatorLogService.addUserLog(operator, request, "新增订单开发票信息");
        return ResultUtil.getSuccessMsg();
    }
	
	
	/**
	 * 导出发货单列表 
	 * @param rId
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value = "exportExcel", method = RequestMethod.POST)
	public @ResponseBody void exportExcel(Integer[] sId, HttpServletResponse response,HttpServletRequest request) throws Exception {
		
		if(ArrayUtils.isEmpty(sId)){
			return ;
		}
		List<OrderInvoice> list = new ArrayList<>();
		for (Integer item : sId) {
			OrderInvoice orderInvoice = orderInvoiceService.get(item);
			list.add(orderInvoice);
		}
	    String filename = URLEncoder.encode("发货单", "UTF-8"); // 设置字符编码为UTF-8
		response.setHeader("Content-Disposition", "attachment; filename="+filename+System.currentTimeMillis()+".xlsx"); //名字加时间戳
		ServletOutputStream sos =  response.getOutputStream();
		ExcelWrite.writeExcel(sos, list); //写入 Servlet 输出流中 
		sos.close();
		//保存日志,HttpServletRequest request
   		LoginUser userif= UserSession.getLoginUser(request);
		Operator operator = operatorService.findUserByUsername(userif.getUsername());
		operatorLogService.addUserLog(operator, request, "导出发货单列表");
	}
	
 
}