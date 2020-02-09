package cn.bohoon.company.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import cn.bohoon.company.entity.Company;
import cn.bohoon.company.service.CompanyService;
import cn.bohoon.framework.orm.domain.Page;
import cn.bohoon.order.entity.Order;
import cn.bohoon.order.entity.OrderInvoice;
import cn.bohoon.order.service.OrderInvoiceService;
import cn.bohoon.order.service.OrderService;

/*
 * 开票记录
 */
@Controller
@RequestMapping(value = "account")
public class AccountController {
   @Autowired
   CompanyService companyService;
	
   @Autowired
   OrderInvoiceService orderInvoiceService;
   
   @Autowired
   OrderService orderService;
   
   @RequestMapping(value = "list", method = RequestMethod.GET)
   public String list(HttpServletRequest request,Model model,OrderInvoice seach) {
	    Integer pageNo=ServletRequestUtils.getIntParameter(request, "pageNo",1);
	    String companyId=ServletRequestUtils.getStringParameter(request, "companyId","");
		Page<OrderInvoice> pageOrderInvoice=new Page<OrderInvoice>(5);
		pageOrderInvoice.setPageNo(pageNo);
		String hql = " from OrderInvoice o where 1 = 1";
	        List<Object> params = new ArrayList<Object>();
	        if (companyId!=null) {
				hql = hql + "  and o.companyId=?";
				params.add(companyId);	
			}
	        if (!StringUtils.isEmpty(seach.getOrderId())) {
				hql = hql + " and o.orderId like ? ";
				params.add('%' + seach.getOrderId() + '%');
			}
	        if (!StringUtils.isEmpty(seach.getMemberId())) {
				hql = hql + " and o.memberId = ? ";
				params.add(seach.getMemberId());
			}
	        if (!StringUtils.isEmpty(seach.getTitle())) {
				hql = hql + " and o.title like ? ";
				params.add('%' + seach.getTitle() + '%');
			}
	        hql+=" ORDER BY o.creatDate desc";
		    pageOrderInvoice=orderInvoiceService.page(pageOrderInvoice, hql,params.toArray());
		    Map<OrderInvoice,Company> companyMap = new HashMap<OrderInvoice,Company>() ;
		    Map<OrderInvoice,Order> orderMap = new HashMap<OrderInvoice,Order>() ;
		    for(OrderInvoice orderInvoice:pageOrderInvoice.getResult() ) {
			     Company company = companyService.get(orderInvoice.getCompanyId()) ;
			     companyMap.put(orderInvoice, company) ;
			     Order order = orderService.get(orderInvoice.getOrderId());
			     orderMap.put(orderInvoice, order);
	        }
		    model.addAttribute("seach",seach);
		    model.addAttribute("orderMap",orderMap); 
		    model.addAttribute("companyMap",companyMap);
		    model.addAttribute("memberId", companyId);
		    model.addAttribute("pageOrderInvoice", pageOrderInvoice);
	        return "company/accountList";
  }	
}
