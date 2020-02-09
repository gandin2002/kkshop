package cn.bohoon.company.web;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import cn.bohoon.company.entity.Company;
import cn.bohoon.company.entity.CompanyBank;
import cn.bohoon.company.entity.CompanyLevel;
import cn.bohoon.company.entity.Invoice;
import cn.bohoon.company.entity.License;
import cn.bohoon.company.service.CompanyBankService;
import cn.bohoon.company.service.CompanyCataService;
import cn.bohoon.company.service.CompanyIndustryService;
import cn.bohoon.company.service.CompanyLevelService;
import cn.bohoon.company.service.CompanyService;
import cn.bohoon.company.service.InvoiceService;
import cn.bohoon.company.service.LicenseService;
import cn.bohoon.framework.util.CipherUtil;
import cn.bohoon.order.entity.OrderInvoice;
import cn.bohoon.order.service.OrderInvoiceService;
import cn.bohoon.userInfo.entity.ShippingInfo;
import cn.bohoon.userInfo.entity.User;
import cn.bohoon.userInfo.entity.UserInfo;
import cn.bohoon.userInfo.service.ShippingInfoService;
import cn.bohoon.userInfo.service.UserInfoService;
import cn.bohoon.userInfo.service.UserService;

@Controller
@RequestMapping(value = "details")
public class DetailsController {
	@Autowired
	CompanyService companyService;

	@Autowired
	CompanyCataService companyCataService;

	@Autowired
	CompanyLevelService companyLevelService;

	@Autowired
	CompanyIndustryService companyIndustryService;

	@Autowired
	UserInfoService userInfoService;

	@Autowired
	LicenseService licenseService;

	@Autowired
	OrderInvoiceService orderInvoiceService;

	@Autowired
	CompanyBankService companyBankService;

	@Autowired
	ShippingInfoService shippingInfoService;
	
	@Autowired
	UserService userService;
	
	@Autowired
	InvoiceService invoiceService;
 
	
	@RequestMapping(value = "list", method = RequestMethod.GET)
	public String list(HttpServletRequest request, Model model) {
		String id = ServletRequestUtils.getStringParameter(request, "id", "");
		Company company = companyService.get(id);

		// 订单发票信息
//		List<OrderInvoice> invoiceList = orderInvoiceService.list("from OrderInvoice where companyId=?", id);
//		model.addAttribute("invoiceList", invoiceList);
	
		//公司发票信息
		List<Invoice> invoices  = invoiceService.list("from Invoice where companyId = ?",id);
		
		model.addAttribute("invoiceList", invoices);
		
		
		
		
		List<CompanyBank> companyBankList = companyBankService.list(" from CompanyBank where companyId = ? ", id);
		model.addAttribute("companyBankList", companyBankList);

		String userid = company.getUserId();
		if (userid != null) {
			UserInfo user = userInfoService.get(userid);
			model.addAttribute("user", user);
		}

		String licenseid = company.getLicenseId();
		if (!StringUtils.isEmpty(licenseid)) {
			License license = licenseService.get(licenseid);
			model.addAttribute("license", license);
		}

		CompanyLevel companylevel = companyLevelService.select(" from CompanyLevel where id = ? ", company.getLevel());
 
		List<ShippingInfo> shippingInfos= shippingInfoService.getShipByCompanyId(company.getId());
		
		
		if (company.getCataId() == 12){
			String company_password = company.getCompany_password();
			User user = userService.select("from User where companyId=?", company.getId());
			
			if (!StringUtils.isEmpty(company.getCompany_password())){
			
			if (!user.getPassword().equals(CipherUtil.md5(company.getCompany_password()))){
				
				company_password = "<font color='red'>您的初始化密码已被修改!</font>";
			}
			}
			
			
			model.addAttribute("company_password", company_password);
		}
		
		model.addAttribute("shippingInfos", shippingInfos);
		model.addAttribute("companylevel", companylevel);
		model.addAttribute("item", company);
		
		
		return "company/companyDetails";
	}
}
