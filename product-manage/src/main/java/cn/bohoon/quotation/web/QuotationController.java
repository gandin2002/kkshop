package cn.bohoon.quotation.web;



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
import org.apache.commons.lang.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.bohoon.company.entity.Company;
import cn.bohoon.company.service.CompanyService;
import cn.bohoon.excel.util.ExcelWrite;
import cn.bohoon.framework.exception.CheckException;
import cn.bohoon.framework.orm.domain.Page;
import cn.bohoon.framework.util.DateUtil;
import cn.bohoon.main.domain.LoginUser;
import cn.bohoon.main.domain.UserSession;
import cn.bohoon.main.system.entity.Operator;
import cn.bohoon.main.system.service.OperatorLogService;
import cn.bohoon.main.system.service.OperatorService;
import cn.bohoon.product.entity.Product;
import cn.bohoon.product.entity.Sku;
import cn.bohoon.product.service.ProductService;
import cn.bohoon.product.service.SkuService;
import cn.bohoon.quotation.domain.QuotationExcelModer;
import cn.bohoon.quotation.domain.QuotationState;
import cn.bohoon.quotation.entity.Quotation;
import cn.bohoon.quotation.entity.QuotationItem;
import cn.bohoon.quotation.service.QuotationItemService;
import cn.bohoon.quotation.service.QuotationService;
import cn.bohoon.userInfo.entity.UserInfo;
import cn.bohoon.userInfo.service.UserInfoService;
import cn.bohoon.util.ResultUtil;

@Controller
@RequestMapping(value = "quotation")
public class QuotationController {

    @Autowired
    QuotationService quotationService;
    
    @Autowired
    QuotationItemService quotationItemService;
    
    @Autowired
    UserInfoService userInfoService;
    
    @Autowired
    CompanyService companyService;
    
    @Autowired
    SkuService skuService;
    
    @Autowired
    ProductService productService;
    
    @Autowired
    OperatorService operatorService;
    
    @Autowired
    OperatorLogService operatorLogService;
    
   
    
    /**
     * 报价单列表
     * 
     * @param request
     * @param model
     * @return
     * @throws ParseException
     */
    @RequestMapping(value = "list", method = RequestMethod.GET)
    public String list(HttpServletRequest request,Model model) throws ParseException  {
        Integer pageNo=ServletRequestUtils.getIntParameter(request, "pageNo",1);
        String quotationState = ServletRequestUtils.getStringParameter(request, "quotationState", "");
        String id = ServletRequestUtils.getStringParameter(request, "id","").trim();
        String companyName = ServletRequestUtils.getStringParameter(request, "companyName","").trim();
		String userInfoId = ServletRequestUtils.getStringParameter(request, "userInfoId","").trim();
        String startTime = ServletRequestUtils.getStringParameter(request, "startTime","");
		String endTime = ServletRequestUtils.getStringParameter(request, "endTime","");
        
        Page<Quotation> pageQuotation=new Page<Quotation>();
        pageQuotation.setPageNo(pageNo);

        StringBuilder hql = new StringBuilder("from Quotation q where 1 = 1");
        List<Object> params = new ArrayList<>();
        if(!StringUtils.isEmpty(quotationState)){
        	hql.append(" and q.quotationState = ?");
        	params.add(Enum.valueOf(QuotationState.class, quotationState));
        	model.addAttribute("quotationState", quotationState);
        }
        if(!StringUtils.isEmpty(id)){
        	hql.append(" and q.id = ?");
        	params.add(id);
        	model.addAttribute("id", id);
        }
        if(!StringUtils.isEmpty(companyName)){
        	hql.append(" and q.companyName like ?" );
			params.add('%' + companyName + '%');
        	model.addAttribute("companyName", companyName);
        }
        if(!StringUtils.isEmpty(userInfoId)){
        	hql.append(" and q.userInfoId = ?");
        	params.add(userInfoId);
        	model.addAttribute("userInfoId", userInfoId);
        }
        if(!StringUtils.isEmpty(startTime)){
        	hql.append(" and q.createTime >= ?");
        	params.add(DateUtil.switchStringToDate(startTime, "yy-MM-dd"));
        	model.addAttribute("startTime", startTime);
        }
        if(!StringUtils.isEmpty(endTime)){
        	hql.append(" and q.createTime < ?");
        	params.add(DateUtil.getNDayAfter(endTime, 1));
        	model.addAttribute("endTime", endTime);
        }
        
        hql.append(" order by q.createTime desc");

        pageQuotation=quotationService.page(pageQuotation, hql.toString(),params.toArray());
        

        model.addAttribute("pageQuotation", pageQuotation);
        return "quotation/quotationList";
    }

	@ResponseBody
    @RequestMapping(value = "edit", method = RequestMethod.POST)
    public String editPost(HttpServletRequest request,String id,String ValidDate) throws Exception {
		LoginUser loginUser = UserSession.getLoginUser(request);
		Operator operator = operatorService.findUserByUsername(loginUser.getUsername());
		
		Quotation quotation = quotationService.get(id);
		
		quotation.setQuotationState(QuotationState.BE_ON_QUOTATION);
		quotation.setOperatorId(operator.getId());
		quotation.setQuotationTime(new Date());
		quotation.setValidDate(DateUtil.switchStringToDate(ValidDate, "yyyy-MM-dd HH:mm:ss"));
		
        quotationService.update(quotation);
        String hql = "update QuotationItem set quotationState=? ,ValidDate=? where quotationId=?" ;
        quotationItemService.execute(hql, quotation.getQuotationState(),quotation.getValidDate(),quotation.getId()) ;
      //保存日志,HttpServletRequest request
  		LoginUser userif= UserSession.getLoginUser(request);
		Operator operator1 = operatorService.findUserByUsername(userif.getUsername());
		operatorLogService.addUserLog(operator1, request, "拒绝议价申请单:单号"+quotation.getId());
        return ResultUtil.getSuccessMsg();
    }
	
	@RequestMapping(value = "checkList", method = RequestMethod.GET)
    public String checkList(HttpServletRequest request,Model model) throws ParseException  {
        Integer pageNo=ServletRequestUtils.getIntParameter(request, "pageNo",1);
        String quotationState = ServletRequestUtils.getStringParameter(request, "quotationState", "");
        
        String companyName = ServletRequestUtils.getStringParameter(request, "companyName","").trim();
        String userInfoId = ServletRequestUtils.getStringParameter(request, "userInfoId","").trim();
		String operatorName = ServletRequestUtils.getStringParameter(request, "operatorName","").trim();
        
        String startTime = ServletRequestUtils.getStringParameter(request, "startTime","");
		String endTime = ServletRequestUtils.getStringParameter(request, "endTime","");
        
        Page<Quotation> pageQuotation=new Page<Quotation>();
        pageQuotation.setPageNo(pageNo);

        StringBuilder hql = new StringBuilder("from Quotation q where 1 = 1");
        List<Object> params = new ArrayList<>();
        if(!StringUtils.isEmpty(quotationState)){
        	hql.append(" and q.quotationState = ? ");
        	params.add(Enum.valueOf(QuotationState.class, quotationState));
        	model.addAttribute("quotationState", quotationState);
        }else{
        	hql.append(" and q.quotationState != ? ");
        	params.add(QuotationState.WATI_QUOTATION);
        }
        if(!StringUtils.isEmpty(companyName)){
        	Company company = companyService.select("from Company c where c.name =?", companyName);
        	if(!StringUtils.isEmpty(company)){
        		List<UserInfo> userInfoL = userInfoService.list("from UserInfo u where u.companyId =?", company.getId());
            	if(userInfoL != null && userInfoL.size()>0){
            		for(int i= 0,j=userInfoL.size();i<j;i++){
            			if(i==0){
            				hql.append(" and (");
            			}else{
            				hql.append(" or");
            			}
            			hql.append(" q.userInfoId =?");
            			if(i==j-1){
            				hql.append(")");
            			}
            			params.add(userInfoL.get(i).getId());
            		}
            	}
        	}else{
        		hql.append(" and q.userInfoId =-1");
        	}
        	model.addAttribute("companyName", companyName);
        }
        if(!StringUtils.isEmpty(userInfoId)){
        	Integer userId = -1;
        	try{
        		userId = Integer.valueOf(userInfoId);
        	}catch(NumberFormatException e){
        		userId = -1;
        	}
        	hql.append(" and q.userInfoId = ?");
        	params.add(userId);
        	model.addAttribute("userInfoId", userInfoId);
        }
        if(!StringUtils.isEmpty(operatorName)){
        	Operator operator = operatorService.findUserByUsername(operatorName);
        	if(!StringUtils.isEmpty(operator)){
        		hql.append(" and q.operatorId = ?");
        		params.add(operator.getId());
        	}else{
        		hql.append(" and q.operatorId = -1");
        	}
        	model.addAttribute("operatorName", operatorName);
        }
        if(!StringUtils.isEmpty(startTime)){
        	hql.append(" and q.quotationTime >= ?");
        	params.add(DateUtil.switchStringToDate(startTime, "yy-MM-dd"));
        	model.addAttribute("startTime", startTime);
        }
        if(!StringUtils.isEmpty(endTime)){
        	hql.append(" and q.quotationTime < ?");
        	params.add(DateUtil.getNDayAfter(endTime, 1));
        	model.addAttribute("endTime", endTime);
        }
        
        hql.append(" order by q.createTime desc");
        
        
        pageQuotation=quotationService.page(pageQuotation, hql.toString(),params.toArray());
        
        Map<Quotation, UserInfo> userInfoMap = new HashMap<>();
        Map<Quotation, Company> companyMap = new HashMap<>();
        Map<Quotation, Operator> operatorMap = new HashMap<>();
        for(Quotation quotation : pageQuotation.getResult()){
        	if(StringUtils.isEmpty(quotation.getUserInfoId())){
        		continue;
        	}
        	UserInfo userInfo = userInfoService.get(quotation.getUserInfoId());
        	userInfoMap.put(quotation, userInfo);
        	if(StringUtils.isEmpty(userInfo)||StringUtils.isEmpty(userInfo.getCompanyId())){
        		continue;
        	}
        	companyMap.put(quotation, companyService.get(userInfo.getCompanyId()));
        	if(StringUtils.isEmpty(quotation.getOperatorId())){
        		continue;
        	}
        	operatorMap.put(quotation, operatorService.get(quotation.getOperatorId()));
        }

        model.addAttribute("pageQuotation", pageQuotation);
        model.addAttribute("userInfoMap", userInfoMap);
        model.addAttribute("companyMap", companyMap);
        model.addAttribute("operatorMap", operatorMap);
        return "quotation/quotationCheckList";
    }
	
	@RequestMapping(value = "check", method = RequestMethod.GET)
    public String checkGet(HttpServletRequest request,Model model) {
		Integer pageNo=ServletRequestUtils.getIntParameter(request, "pageNo",1);
        String id=ServletRequestUtils.getStringParameter(request, "id","");
        Quotation quotation=quotationService.get(id);
        BigDecimal tDifference = quotation.getQuotationPrice().subtract(quotation.getQuotationSkuPrice());
        UserInfo userInfo = userInfoService.get(quotation.getUserInfoId());
        if(!StringUtils.isEmpty(userInfo.getCompanyId())){
        	Company company = companyService.get(userInfo.getCompanyId());
        	model.addAttribute("companyItem",company);
        }
        
        Page<QuotationItem> pageQuotationItem=new Page<QuotationItem>(30);
        pageQuotationItem.setPageNo(pageNo);

        StringBuilder hql = new StringBuilder("from QuotationItem q where q.quotationId = ? order by q.id asc");
        List<Object> params = new ArrayList<>();
        	params.add(id);

        pageQuotationItem=quotationItemService.page(pageQuotationItem, hql.toString(), params.toArray());
        List<QuotationItem> itemList = pageQuotationItem.getResult();
        Map<QuotationItem, Sku> skuMap = new HashMap<>();
        Map<QuotationItem, Product> productMap = new HashMap<>();
        for(QuotationItem quotationItem : itemList){
        	Sku sku = skuService.get(quotationItem.getSkuId());
        	skuMap.put(quotationItem, sku);
        	Product product = productService.get(sku.getProductId());
        	productMap.put(quotationItem, product);
        }
        
        if(!StringUtils.isEmpty(quotation.getOperatorId())){
        	Operator operator = operatorService.get(quotation.getOperatorId());
        	String operatorUserName = operator.getUsername();
        	model.addAttribute("operatorUserName",operatorUserName);
        }
        
        model.addAttribute("item",quotation);
        model.addAttribute("tDifference",Math.abs(tDifference.doubleValue()));
        model.addAttribute("userItem",userInfo);
        
        model.addAttribute("pageQuotationItem",pageQuotationItem);
        model.addAttribute("skuMap",skuMap);
        model.addAttribute("productMap",productMap);

        return "quotation/quotationCheck";
    }
	
	@RequestMapping(value = "checkDeny", method = RequestMethod.GET)
    public String checkDenyGet(HttpServletRequest request,Model model) {
        String id=ServletRequestUtils.getStringParameter(request, "id","");
        Quotation quotation=quotationService.get(id);
        
        model.addAttribute("item",quotation);
        return "quotation/quotationCheckDeny";
    }
	
	@ResponseBody
	@RequestMapping(value = "check", method = RequestMethod.POST)
    public String checkPost(HttpServletRequest request,Model model) throws Exception {
        String id = ServletRequestUtils.getStringParameter(request, "id","");
        String quotationState = ServletRequestUtils.getStringParameter(request, "quotationState","");
        String denyDetail = ServletRequestUtils.getStringParameter(request, "denyDetail","");
        String ValidDate = ServletRequestUtils.getStringParameter(request, "ValidDate","");
        
        Quotation quotation=quotationService.get(id);
        quotation.setQuotationState(Enum.valueOf(QuotationState.class, quotationState));
        quotation.setDenyDetail(denyDetail);
        quotation.setCheckTime(new Date());
        if(!StringUtils.isEmpty(ValidDate)){
        	quotation.setValidDate(DateUtil.switchStringToDate(ValidDate, "yyyy-MM-dd HH:mm:ss"));
        }
        
        quotationService.update(quotation);
        quotationItemService.execute("update QuotationItem set quotationState=? where quotationId=?",quotation.getQuotationState(),id) ;
        //保存日志,HttpServletRequest request
  		LoginUser userif= UserSession.getLoginUser(request);
		Operator operator1 = operatorService.findUserByUsername(userif.getUsername());
		operatorLogService.addUserLog(operator1, request, "通过议价申请单:单号"+quotation.getId());
        return ResultUtil.getSuccessMsg();
    }
	
	@ResponseBody
	@RequestMapping(value = "checkBatchPass", method = RequestMethod.POST)
    public String checkBatchPassPost(HttpServletRequest request,Model model,String checkboxes) throws Exception {
		String [] checkBoxes = checkboxes.split(",");
		
		for(int i=0,j=checkBoxes.length;i<j;i++){
			quotationService.editState(QuotationState.PASS_QUOTATION,checkBoxes[i],"PASS") ;
		}
		//保存日志,HttpServletRequest request
  		LoginUser userif= UserSession.getLoginUser(request);
		Operator operator1 = operatorService.findUserByUsername(userif.getUsername());
		operatorLogService.addUserLog(operator1, request, "批量通议价申请单:单号"+org.apache.commons.lang.StringUtils.join(checkBoxes,","));
        return ResultUtil.getSuccessMsg();
    }
	
	@RequestMapping(value = "checkBatchDeny", method = RequestMethod.GET)
    public String checkBatchDenyGet(HttpServletRequest request,Model model,String checkboxes) {		
        model.addAttribute("checkboxes", checkboxes);
        return "quotation/quotationCheckBatchDeny";
    }
	
	@ResponseBody
	@RequestMapping(value = "checkBatchDeny", method = RequestMethod.POST)
    public String checkBatchDenyPost(HttpServletRequest request,Model model,String checkboxes) throws Exception {
		if(StringUtils.isEmpty(checkboxes)){
			throw new CheckException("批量拒绝报价单选择为空！");
		}
		
		String denyDetail = ServletRequestUtils.getStringParameter(request, "denyDetail","");
		String [] checkBoxes = checkboxes.split(",");
		
		for(int i=0,j=checkBoxes.length;i<j;i++){
			quotationService.execute("Update Quotation q set q.quotationState =?,q.denyDetail =?,q.checkTime =? where q.quotationState =? and q.id =?", 
					QuotationState.DENY_QUOTATION,denyDetail,new Date(),QuotationState.BE_ON_QUOTATION,checkBoxes[i]);
			quotationService.editState(QuotationState.DENY_QUOTATION,checkBoxes[i],denyDetail) ;
		}
		//保存日志,HttpServletRequest request
  		LoginUser userif= UserSession.getLoginUser(request);
		Operator operator1 = operatorService.findUserByUsername(userif.getUsername());
		operatorLogService.addUserLog(operator1, request, "批量拒绝申请单:单号"+org.apache.commons.lang.StringUtils.join(checkBoxes,","));
        
        return ResultUtil.getSuccessMsg();
    }
	/**
	 * 导出议价单excel
	 * @param response
	 * @param pid
	 * @throws Exception
	 */
	@RequestMapping(value="downloadExcel",method=RequestMethod.POST)
	public @ResponseBody void downloadExcel(HttpServletResponse response,String[] pid,String flag,HttpServletRequest request) throws Exception{
		String filename = URLEncoder.encode("议价单表", "UTF-8"); // 设置字符编码为UTF-8
		response.setHeader("Content-Disposition", "attachment; filename=" +filename +System.currentTimeMillis()+ ".xlsx"); // 名字加时间戳
		ServletOutputStream sos = response.getOutputStream();
		List<QuotationExcelModer> pemlist = new ArrayList<QuotationExcelModer>();
		if(ArrayUtils.isEmpty(pid)){ // 写入全部
			List<Quotation> listQuotation;
			if (!StringUtils.isEmpty(flag)) {
				 listQuotation = quotationService.list("from Quotation where quotationState=?",QuotationState.valueOf(flag));
			}else {
				 listQuotation = quotationService.list();
			}
			
			for (Quotation quotation : listQuotation) {
				QuotationExcelModer quotationExcelModer = new QuotationExcelModer();
				quotationExcelModer.setParams(quotation);
				pemlist.add(quotationExcelModer);
			}
		} else {
			for (String string : pid) {
				if (NumberUtils.isNumber(string)) { // 写入指定
					Quotation quotation = quotationService.get(string);
					QuotationExcelModer quotationExcelModer = new QuotationExcelModer();
					quotationExcelModer.setParams(quotation);
					pemlist.add(quotationExcelModer);
				}
			}
		}
		ExcelWrite.writeExcel(sos, pemlist);
		sos.close();
		//保存日志,HttpServletRequest request
  		LoginUser userif= UserSession.getLoginUser(request);
		Operator operator1 = operatorService.findUserByUsername(userif.getUsername());
		operatorLogService.addUserLog(operator1, request, "议价单批量导出");
	}
	
}