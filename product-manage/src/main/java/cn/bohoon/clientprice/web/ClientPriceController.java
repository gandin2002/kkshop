package cn.bohoon.clientprice.web;

import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.bohoon.basicSetup.helper.SysParamCache;
import cn.bohoon.basicSetup.helper.SysParamHelper;
import cn.bohoon.company.domain.CompanySateEnum;
import cn.bohoon.company.entity.Company;
import cn.bohoon.company.service.CompanyService;
import cn.bohoon.excel.util.ExcelWrite;
import cn.bohoon.framework.orm.domain.Page;
import cn.bohoon.main.domain.LoginUser;
import cn.bohoon.main.domain.UserSession;
import cn.bohoon.main.system.entity.Operator;
import cn.bohoon.main.system.service.OperatorLogService;
import cn.bohoon.main.system.service.OperatorService;
import cn.bohoon.product.entity.Brand;
import cn.bohoon.product.entity.Category;
import cn.bohoon.product.entity.Product;
import cn.bohoon.product.entity.Sku;
import cn.bohoon.product.service.BrandService;
import cn.bohoon.product.service.CategoryService;
import cn.bohoon.product.service.ProductService;
import cn.bohoon.product.service.SkuService;
import cn.bohoon.quotation.domain.QuotationExcel;
import cn.bohoon.quotation.entity.QuotationItem;
import cn.bohoon.quotation.entity.TieredPrice;
import cn.bohoon.quotation.entity.TieredPriceList;
import cn.bohoon.quotation.service.QuotationItemService;
import cn.bohoon.quotation.service.QuotationService;
import cn.bohoon.quotation.service.TieredPriceService;
import cn.bohoon.util.ResultUtil;

/**
 * 客户价格表
 * 
 * @author HJ 2018年1月26日,上午9:34:41
 */
@Controller
@RequestMapping(value = "clientprice")
public class ClientPriceController {

	@Autowired
	QuotationService quotationService;

	@Autowired
	QuotationItemService quotationItemservice;
	
	@Autowired
	ProductService productService;
	
	@Autowired
	SkuService skuService;
	
	@Autowired
	CompanyService companyService;
	

	@Autowired
	CategoryService categoryService;
	
	@Autowired
	BrandService brandService;
	
	@Autowired
	TieredPriceService   tieredPriceService;
	
    @Autowired
    OperatorLogService operatorLogService;
    
    
    @Autowired
    OperatorService operatorService;

	@RequestMapping(value = "list")
	public String list(@RequestParam(name = "pageNo", required = true, defaultValue = "1") Integer pageNo,Model model,String companyId,String productName,
		String skuCode,String categoryId,Integer brandId,String quotatiou) {
		Page<Sku> skuPage = new Page<Sku>();
		skuPage.setPageNo(pageNo);
		List<Object> params = new ArrayList<Object>();
		if(!StringUtils.isEmpty(companyId)){
			
		String hql =" select s  from Sku as s , Product as pt ";
		if (!StringUtils.isEmpty(quotatiou) && !"全部".equals(quotatiou)) {
			 hql +=" ,QuotationItem as qit where qit.quotationId in  ( SELECT  qt.id FROM Quotation qt  where  qt.companyId = ?   and qt.ValidDate > now()  )  ";
			 if ("yes_quotatiou".equals( quotatiou )){
					hql +=" and qit.skuId = s.id and s.productId = pt.id  ";
			 }else{
				    hql +=" and  qit.skuId != s.id and s.productId = pt.id  ";
			 }
			 params.add(companyId);
		}else{
			hql +=" where  s.productId = pt.id ";
		}
		if(!StringUtils.isEmpty(productName)){
			hql+=" and ( pt.name like '%"+productName+"%' or pt.code like '%"+productName+"%' )";
		}
		if(!StringUtils.isEmpty(categoryId)){
			hql+=" and pt.categoryId like '%"+categoryId+"%' ";
		}
		if(!StringUtils.isEmpty(brandId)){
			hql+=" and pt.brandId ="+brandId;
		}
			
		if(!StringUtils.isEmpty(skuCode)){
			hql +=" and pt.code like '%"+skuCode+"%'";
		}
		
		skuPage = skuService.page(skuPage,hql,params.toArray());
		
		
		String inventoryHql = "select sum(inventory) from Sku where productId = ? and status = 1 " ; //计算库存
		
		Map<Sku,Long> inventorymap = new HashMap<Sku,Long>();
		
		
		HashMap<Sku,Object> priceMap = new HashMap<Sku,Object>();
		for (Sku sku : skuPage.getResult()) {
			Product product = sku.getProduct();
        	Long inventory = skuService.select(inventoryHql, Long.class,product.getId());
        	
        	BigDecimal companyVipPrice = sku.getCompanyVipPrice(companyId);
        	priceMap.put(sku, companyVipPrice);
        	
        	inventorymap.put(sku, inventory);
		}
		
		model.addAttribute("priceMap", priceMap);
		model.addAttribute("productName", productName);
		model.addAttribute("skuCode", skuCode);
		model.addAttribute("companyId",companyId);
		model.addAttribute("inventorymap", inventorymap);
		model.addAttribute("quotatiou", quotatiou);
		
		}
		List<Company> companyList= companyService.list(" from Company where companySate= ? ",CompanySateEnum.PASS);
		List<Category> categoryList = categoryService.list(" from Category where  level  = 1");
		List<Brand> brandList = brandService.list(" from Brand  order by sort ASC ");
		String pcSite = SysParamCache.getCache().getSysParamValue(SysParamHelper.PC_SITE) ;
		model.addAttribute("portalHostDomain", pcSite);
		
		
		
		model.addAttribute("skuPage", skuPage);
		model.addAttribute("brandList", brandList);
		model.addAttribute("categoryList",  categoryList);
		model.addAttribute("companyList",companyList);
		model.addAttribute("brandId", brandId);
		model.addAttribute("categoryId", categoryId);
		
		
		return "clientprice/clientpriceList";
	}
	
	@RequestMapping(value="exportExcel",method=RequestMethod.POST)
	public @ResponseBody void exportExcel(Integer companyId,HttpServletResponse response,HttpServletRequest request) throws Exception{
		
		String hql =" select qim from Quotation as qt, QuotationItem as qim where qt.id = qim.quotationId and qt.ValidDate > now()  ";
		if(!StringUtils.isEmpty(companyId)){
			hql +=" and companyId = "+companyId;
		}else{
			hql +=" and companyId is null ";
		}
		
		 List<QuotationItem> quotationItemList =  quotationItemservice.list(hql);
		 List<QuotationExcel> excelQuotationList = new ArrayList<>();
		
			for (QuotationItem quotationItem : quotationItemList) {
	        	Sku sku = skuService.get(quotationItem.getSkuId());  
	        	Product product = productService.get(sku.getProductId());
	        	QuotationExcel excelQuotation = new QuotationExcel();
	        	excelQuotation.setProductName(product.getName()+"("+sku.getAttrAndAttrValues()+")");
	        	excelQuotation.setQuantity(quotationItem.getQuantity().toString());
	        	excelQuotation.setQuotationSkuPrice(quotationItem.getQuotationSkuPrice().toString());
	        	excelQuotation.setQuotationPrice(quotationItem.getQuotationPrice().toString());
	        	excelQuotation.setSkuId(sku.getCode());
	        	excelQuotationList.add(excelQuotation);
			}
	    String filename = URLEncoder.encode("报价单", "UTF-8"); // 设置字符编码为UTF-8
		response.setHeader("Content-Disposition", "attachment; filename="+filename+companyId+".xlsx"); //名字加时间戳
		ServletOutputStream sos =  response.getOutputStream(); //servlet 输出流
		ExcelWrite.writeExcel(sos, excelQuotationList);
		sos.close();
		//保存日志,HttpServletRequest request
  		 LoginUser userif= UserSession.getLoginUser(request);
		Operator operator = operatorService.findUserByUsername(userif.getUsername());
		operatorLogService.addUserLog(operator, request, "客户价格表导出");
	}
	
	/*
	 * 价格阶梯
	 */
    
	@RequestMapping(value="TieredPrice")
	public String tieredprice(Integer companyId,String productCode,BigDecimal companyVipPrice,Model model,String skuCode) throws Exception{
	
		String hql="from TieredPrice where productCode='"+productCode+"'";
		model.addAttribute("skuCode",skuCode);
	   String hql2=" from Product where code=?";
	   String hql3="from Sku where code=?";
	  //String hql4=" insert into TieredPrice(CompanyId,productCode,startNum,remark) values(?,?,?,?)";
	String startNum=null;
	//insert into t_tieredPrice(CompanyId,productCode,startNum,remark) VALUES(50427147,'SP1712130011','1','双十一') 
	if(!StringUtils.isEmpty(companyId)){
	   hql=hql+"and companyId='"+companyId+"'";
	}
	//先查询数据库是否存在价格阶梯实体类
 
  // Double   comPany=new BigDecimal(vipPrice.toString()).setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();
	/* Double    vipPrice=Double.parseDouble(companyVipPrice)*10.00;
	  DecimalFormat  format=new DecimalFormat("#0.00");
	  vipPrice=Double.parseDouble(format.format(vipPrice));
	  companyVipPrice=vipPrice.toString();*/
	  if(tieredPriceService.list(hql).size()==0 ||tieredPriceService.list(hql)==null){
	
			     for(int i=0;i<10;i++){
			    	
			         TieredPrice tie=new TieredPrice();
					tie.setCompanyId(companyId);
					tie.setProductCode(productCode);
					tie.setRemark("协议价");
			    	 if(i==0){
			    		 startNum="1";
			    		 tie.setStartNum(startNum);
			    	 }
			    	
			    
			    	 tieredPriceService.save(tie);
			    	 
			     }
	     }
     
		Product    product=productService.select(hql2, productCode);
		 if(product.getErpCode()!=null){
		    Sku  sku = skuService.select(hql3,product.getErpCode());
		    model.addAttribute("sku",sku);
		 }
		
		
	
		List<TieredPrice>  list=tieredPriceService.list(hql);
		
		model.addAttribute("product",product);
	    model.addAttribute("list",list);
	    model.addAttribute("companyId",companyId);
	    model.addAttribute("productCode",productCode);
	    model.addAttribute("companyVipPrice",companyVipPrice);
		return "/clientprice/TieredPrice";
	}
	
	//阶梯价格保存
	
	@ResponseBody
	@RequestMapping(value = "/TieredPrice/Save", method =RequestMethod.POST)
	public String modify(TieredPriceList pricelist,HttpServletRequest request) throws Exception {
		List<TieredPrice> list=pricelist.getList();
		
	for(TieredPrice tie:list){
	   
	    	 tieredPriceService.update(tie);
	    
	     
	}
	
		//tieredPriceService.saveBatch(list,list.size());
	//保存日志,HttpServletRequest request
		 LoginUser userif= UserSession.getLoginUser(request);
		Operator operator = operatorService.findUserByUsername(userif.getUsername());
		operatorLogService.addUserLog(operator, request, "阶梯价格保存");
		return ResultUtil.getSuccessMsg();
	}
	
	
}
