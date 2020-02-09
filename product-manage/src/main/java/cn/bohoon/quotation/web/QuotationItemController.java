package cn.bohoon.quotation.web;

import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import cn.bohoon.company.entity.Company;
import cn.bohoon.company.service.CompanyService;
import cn.bohoon.excel.util.ExcelRead;
import cn.bohoon.excel.util.ExcelWrite;
import cn.bohoon.framework.exception.CheckException;
import cn.bohoon.framework.orm.domain.Page;
import cn.bohoon.framework.util.JsonUtil;
import cn.bohoon.util.ResultUtil;
import cn.bohoon.product.entity.Product;
import cn.bohoon.product.entity.Sku;
import cn.bohoon.product.service.ProductService;
import cn.bohoon.product.service.SkuService;
import cn.bohoon.quotation.domain.QuotationExcel;
import cn.bohoon.quotation.domain.QuotationState;
import cn.bohoon.quotation.entity.Quotation;
import cn.bohoon.quotation.entity.QuotationItem;
import cn.bohoon.quotation.service.QuotationItemService;
import cn.bohoon.quotation.service.QuotationService;
import cn.bohoon.userInfo.entity.UserInfo;
import cn.bohoon.userInfo.service.UserInfoService;

@Controller
@RequestMapping(value = "quotationItem")
public class QuotationItemController {

	@Autowired
    QuotationItemService quotationItemService;
	
	@Autowired
    QuotationService quotationService;
	
	@Autowired
	SkuService skuService;

	@Autowired
	ProductService productService;
	
	@Autowired
	UserInfoService userInfoService;
	
	@Autowired
	CompanyService companyService;

	@RequestMapping(value = "edit", method = RequestMethod.GET)
    public String editGet(HttpServletRequest request,Model model) {
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
        
        model.addAttribute("item",quotation);
        model.addAttribute("tDifference",Math.abs(tDifference.doubleValue()));
        model.addAttribute("userItem",userInfo);
        
        model.addAttribute("pageQuotationItem",pageQuotationItem);
        model.addAttribute("skuMap",skuMap);
        model.addAttribute("productMap",productMap);
        return "quotation/quotationItemEdit";
    }
	
	@ResponseBody
    @RequestMapping(value = "edit", method = RequestMethod.POST)
    public String editPost(Integer id,BigDecimal quotationPrice) {
		
		QuotationItem quotationItem = quotationItemService.get(id);
		BigDecimal oQuotationPrice = quotationItem.getQuotationPrice();
		Boolean modify = quotationPrice.compareTo(quotationItem.getQuotationSkuPrice())!=0?true:false;
		
		quotationItem.setQuotationPrice(quotationPrice);
		quotationItem.setModify(modify);
		
        quotationItemService.update(quotationItem);
        
        Long num = quotationItemService.select("select count(*) from QuotationItem q where q.quotationId = ? and q.modify = ?", 
        		Long.class,quotationItem.getQuotationId(), true);

    	Quotation quotation = quotationService.get(quotationItem.getQuotationId());
    	
    	BigDecimal tQuotationPrice = quotation.getQuotationPrice();
    	tQuotationPrice = tQuotationPrice.add(quotationPrice).subtract(oQuotationPrice);
    	quotation.setQuotationPrice(tQuotationPrice);
    	quotation.setModifyNum(num.intValue());
    	quotationService.update(quotation);
    	
    	BigDecimal tDifference = quotation.getQuotationPrice().subtract(quotation.getQuotationSkuPrice());
    	
    	Map<String, Object> map = new HashMap<>();
    	map.put("num", num);
    	map.put("tQuotationPrice", tQuotationPrice+"");
    	map.put("tDifference", Math.abs(tDifference.doubleValue())+"");
        
        return ResultUtil.getData(0, "", map);
    }

	/**
	 * 导入excel
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value = "uploadExcel", method = RequestMethod.POST)
	public @ResponseBody String uploadExcel(@RequestParam("file") MultipartFile multipartFile, String qId)
			throws Exception {
		List<List<String>> list = new ArrayList<>();
		List<String> errorMsgList = new ArrayList<>();
		try {
			list = ExcelRead.readExcel(multipartFile.getInputStream());
		} catch (Exception e) {

			throw new CheckException("解析EXCEL 文件异常！");
		}
		Quotation quotation = quotationService.get(qId);
		for (int i = 0; i < list.size(); i++) {
			List<String> datas = list.get(i);
	 
			if(datas.size() != 5){
				errorMsgList.add("第"+(i+2)+"行出现错误！");
				continue;
			}
			
			String skuCode = datas.get(0); // 货品SKU编号

			Sku sku = skuService.select(" from Sku where code = ? ",skuCode);
			if (sku == null) {
				errorMsgList.add("第"+(i+2)+"行 SKU编码 填写错误！ ");
				continue;
			}
			
			String qprice= datas.get(4);
			if (!NumberUtils.isNumber(qprice)) {
				errorMsgList.add("第"+(i+2)+"行 报价  填写错误！ ");
				   continue;	
			}
			BigDecimal quotationPrice = new BigDecimal(qprice);
			
			QuotationItem quotationItem = quotationItemService
					.select(" from QuotationItem where quotationId = ? and skuId = ? ", qId, sku.getId());
			if (quotationItem != null) { // 已有此 报价项，直接修改
				quotationItem.setQuotationPrice(quotationPrice);
				quotationItem.setModify(true); // 设置 为 改动
				quotationItemService.save(quotationItem);
				continue;
			}

			quotationItem = new QuotationItem();
			quotationItem.setQuotationId(qId);
			quotationItem.setSkuId(sku.getId());
			quotationItem.setProductId(sku.getProductId());
			quotationItem.setQuotationSkuPrice(sku.getSkuPrice());// 商品sku价格
			quotationItem.setQuotationPrice(quotationPrice); // 报价
			quotationItem.setUserInfoId(quotation.getUserInfoId()); // 会员ID
			quotationItem.setQuantity(9999);
			quotationItem.setValidDate(quotation.getValidDate()); // 有效时间
			quotationItem.setQuotationState(QuotationState.WATI_QUOTATION);
			quotationItemService.save(quotationItem);

		}
		if(!errorMsgList.isEmpty()){
			return JsonUtil.toJson(errorMsgList);
		}
		return ResultUtil.getSuccessMsg();
	}
	
	/**
	 *	 导出Excel
	 * @param data
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value="exportExcel",method=RequestMethod.POST)
	public @ResponseBody void exportExcel(String qId,HttpServletResponse response) throws Exception{
		List<QuotationExcel> excelQuotationList = new ArrayList<>();
			List<QuotationItem> quotationItemList = quotationItemService.list(" from QuotationItem where  quotationId = ? ",qId);
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
		response.setHeader("Content-Disposition", "attachment; filename="+filename+qId+".xlsx"); //名字加时间戳
		ServletOutputStream sos =  response.getOutputStream(); //servlet 输出流
		ExcelWrite.writeExcel(sos, excelQuotationList);
		sos.close();
	}
	
	/**
	 * 打印报价单
	 * @param qid
	 * @param model
	 * @return
	 */
	@RequestMapping(value="quotationPrint",method=RequestMethod.GET)
	public String quotationPrint(@RequestParam(name="qId",required=true)String qid,Model model){
		Quotation quotation = quotationService.get(qid);
		List<QuotationItem>  quotationItemList= quotationItemService.list("from QuotationItem q where q.quotationId = ? order by q.id asc",quotation.getId());
		UserInfo userInfo = userInfoService.select(" from UserInfo where id = ? ", quotation.getUserInfoId());
		BigDecimal tDifference = quotation.getQuotationPrice().subtract(quotation.getQuotationSkuPrice());
        Map<QuotationItem, Sku> skuMap = new HashMap<>();
        Map<QuotationItem, Product> productMap = new HashMap<>();
        for(QuotationItem quotationItem : quotationItemList){
        	Sku sku = skuService.get(quotationItem.getSkuId());
        	skuMap.put(quotationItem, sku);
        	Product product = productService.get(sku.getProductId());
        	productMap.put(quotationItem, product);
        }
        if(!StringUtils.isEmpty(userInfo.getCompanyId())){
        	Company company = companyService.get(userInfo.getCompanyId());
        	model.addAttribute("companyItem",company);
        }
        
        model.addAttribute("skuMap",skuMap);
        model.addAttribute("productMap",productMap);
		model.addAttribute("tDifference",Math.abs(tDifference.doubleValue()));
		model.addAttribute("userItem", userInfo);
		model.addAttribute("item", quotation);
		model.addAttribute("quotationItemList", quotationItemList);
		return "/quotation/quotationPrint";
	}
}