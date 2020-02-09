package cn.bohoon.product.web;

import  java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipOutputStream;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.google.zxing.WriterException;

import cn.bohoon.basicSetup.domain.SysParamType;
import cn.bohoon.basicSetup.helper.SysParamCache;
import cn.bohoon.basicSetup.helper.SysParamHelper;
import cn.bohoon.basicSetup.service.SysParamService;
import cn.bohoon.excel.util.ExcelWrite;
import cn.bohoon.framework.orm.domain.Page;
import cn.bohoon.framework.util.DateUtil;
import cn.bohoon.main.domain.LoginUser;
import cn.bohoon.main.domain.UserSession;
import cn.bohoon.main.system.entity.Operator;
import cn.bohoon.main.system.service.OperatorLogService;
import cn.bohoon.main.system.service.OperatorService;
import cn.bohoon.order.service.OrderService;
import cn.bohoon.product.domain.ProdExcelModel;
import cn.bohoon.product.domain.SkuExcelModel;
import cn.bohoon.product.entity.Brand;
import cn.bohoon.product.entity.Category;
import cn.bohoon.product.entity.Product;
import cn.bohoon.product.entity.ProductCode;
import cn.bohoon.product.entity.ProductLabel;
import cn.bohoon.product.entity.Sku;
import cn.bohoon.product.entity.SkuCode;
import cn.bohoon.product.service.BrandService;
import cn.bohoon.product.service.CategoryService;
import cn.bohoon.product.service.ProductCodeService;
import cn.bohoon.product.service.ProductLabelService;
import cn.bohoon.product.service.ProductService;
import cn.bohoon.product.service.SkuCodeService;
import cn.bohoon.product.service.SkuService;
import cn.bohoon.util.ResultUtil;

@Controller
@RequestMapping(value = "productSale")
public class ProductSaleController {
	

	@Autowired
	SkuService skuService;
 
	@Autowired
	ProductService productService;
	
	@Autowired
	ProductCodeService productCodeService;
	
	@Autowired
	OrderService orderService;
	
	@Autowired
	SkuCodeService skuCodeService;
	
	@Autowired
	BrandService brandService;

	@Autowired
	CategoryService categoryService;
	
	@Autowired
	ProductLabelService productLabelService;
	
    @Autowired
    OperatorLogService operatorLogService;
    
    @Autowired
    OperatorService operatorService;
    @Autowired
    SysParamService sysParamService;
	
	/**
	 * 商品列表
	 * 
	 * @param model
	 * @param request
	 * @param code
	 * @param name
	 * @param flag
	 * @return
	 * @throws IOException 
	 * @throws WriterException 
	 * @throws ParseException 
	 */
	@RequestMapping(value = "list")
	public String list(Model model, HttpServletRequest request, String product, String sku, String categoryId,
			Integer brandId, String label, Integer sales, Integer sales2, BigDecimal price, BigDecimal price2,
			Integer inventory, Integer inventory2, String startTime, String endTime)
			throws WriterException, IOException, ParseException {
		Integer pageNo = ServletRequestUtils.getIntParameter(request, "pageNo", 1);
		Page<Product> pageProduct = new Page<Product>();
		pageProduct.setPageNo(pageNo);
		String hql = "  from Product as prt where 1 = 1 and flag = 1 ";
		List<Object> params = new ArrayList<Object>();
		if (!StringUtils.isEmpty(product)) {
			
			hql += " and (prt.name like '%" + product + "%' or prt.code like '%" + product + "%' )";
			model.addAttribute("product", product);
		}
		if (!StringUtils.isEmpty(categoryId)) {
			hql += " and prt.categoryId like  '%" + categoryId + "%'";
			model.addAttribute("categoryId", categoryId);
		}
		if (!StringUtils.isEmpty(brandId)) {
			hql += " and prt.brandId =" +brandId;
			model.addAttribute("brandId", brandId);
		}
		if (!StringUtils.isEmpty(label)) {
			hql += " and prt.lables like '%" + label + "%'";
			model.addAttribute("labels", label);
		}
		if (!StringUtils.isEmpty(sales)) {
			hql += " and prt.salesNum >= " + sales;
			model.addAttribute("sales", sales);
		}
		if (!StringUtils.isEmpty(sales2)) {
			hql += " and prt.salesNum <=" + sales2;
			model.addAttribute("sales2", sales2);
		}
		if (!StringUtils.isEmpty(price)) {
			hql += " and prt.salesPrice >=" + price;
			model.addAttribute("price", price);
		}
		if (!StringUtils.isEmpty(price2)) {
			hql += " and prt.salesPrice <= " + price2;
			model.addAttribute("price2", price2);
		}
		if (!StringUtils.isEmpty(startTime)) {
			hql += " and prt.createDate >=  ?   ";
			params.add(DateUtil.switchStringToDate(startTime, "yy-MM-dd"));
			model.addAttribute("startTime", startTime);
		}
		if (!StringUtils.isEmpty(endTime)) {
			hql += " and prt.createDate <  ? ";
			params.add(DateUtil.getNDayAfter(endTime, 1));
			model.addAttribute("endTime", endTime);
		}
		if (!StringUtils.isEmpty(inventory) || !StringUtils.isEmpty(inventory2) || !StringUtils.isEmpty(sku)) {
			String hql2 = "select sku.productId from Sku as sku ";
			hql2 += !StringUtils.isEmpty(sku) ? " where sku.code like '%" + sku + "%'": " " + " group by sku.productId having 1 = 1 ";
			model.addAttribute("sku", sku);//$!prod.code
			if (!StringUtils.isEmpty(inventory)) {
				hql2 += "  and sum(sku.inventory) >= " + inventory;
				model.addAttribute("inventory", inventory);
			}
			if (!StringUtils.isEmpty(inventory2)) {
				hql2 += "  and sum(sku.inventory) <= " + inventory2;
				model.addAttribute("inventory2", inventory2);
			}
			hql += " and  prt.id in ("+hql2+")";
			//hql += " and  prt.id in (%s)";
			//select sku.productId from Sku as sku  where sku.code like '%SP1710260004%'
			//  from Product as prt where 1 = 1 and flag = 1  and prt.categoryId like  '%001%' and prt.brandId = 1 and  prt.id in (%s)
			//from Product as prt where 1 = 1 and flag = 1  and  prt.id in (select sku.productId from Sku as sku  where sku.code like '%SP1710260004%')
			//hql = String.format(hql, hql2);
			//hql=hql+hql2;
			
		}
		pageProduct = productService.page(pageProduct, hql, params.toArray());

		Map<Product, Object> inventoryMap = new HashMap<Product, Object>();
		String inventoryHql = "select sum(inventory) from Sku where productId = ? and status = 1 ";
		for (Product prod : pageProduct.getResult()) {
			Long inventory1 = skuService.select(inventoryHql, Long.class, prod.getId());
			inventoryMap.put(prod, inventory1);
		}
		List<Brand> brandList = brandService.list();
		List<Category> categoryList = categoryService.list(" from Category where  level  = 1");
		List<ProductLabel> pLabelList = productLabelService.list(" from ProductLabel where status= 1");
		model.addAttribute("pLabelList", pLabelList);
		model.addAttribute("categoryList", categoryList);
		model.addAttribute("brandList", brandList);

		model.addAttribute("flag", 1);
		model.addAttribute("resultUrl", request.getRequestURI());
		
		
		model.addAttribute("pageProduct", pageProduct);
		model.addAttribute("inventoryMap", inventoryMap);
		String pcSite = sysParamService.findParam("PC_SITE", SysParamType.PLATFORM_PARAM).getValue();
		model.addAttribute("portalHostDomain", pcSite);
		return "productSale/productSaleList";
	}
	
	
	
	
	
	
	/**
	 * 商品列表
	 * 
	 * @param model
	 * @param request
	 * @param code
	 * @param name
	 * @param flag
	 * @return
	 * @throws ParseException 
	 */
	@RequestMapping(value = "goods")
	public String goods(Model model, HttpServletRequest request , String product, String sku,
			String categoryId, Integer brandId, String label,Integer sales,Integer sales2,BigDecimal price,BigDecimal price2,Integer inventory,Integer inventory2,String startTime, String endTime ,
			@RequestParam(name = "flag", defaultValue = "1") Integer flag) throws ParseException {
		Integer pageNo = ServletRequestUtils.getIntParameter(request, "pageNo", 1);
		Page<Sku> pageSku = new Page<Sku>();
		pageSku.setPageNo(pageNo);
		String hql = " select sku from  Product as prt , Sku as sku where prt.id = sku.productId  and sku.status = 1 and sku.flag = " + flag;
		List<Object> params = new ArrayList<Object>();
		if (!StringUtils.isEmpty(product)) {
			hql += " and (prt.name like '%"+product+"%' or prt.code like '%"+product+"%' )";
			model.addAttribute("product", product);
		}
		if (!StringUtils.isEmpty(sku)) {
			hql += " and sku.code like '%"+sku+"%'";
			model.addAttribute("sku", sku);
		}
		if (!StringUtils.isEmpty(categoryId)) {
			hql +=" and prt.categoryId like  '"+categoryId+"%'";
			model.addAttribute("categoryId", categoryId);
		}
		if (!StringUtils.isEmpty(brandId)) {
			hql +=" and prt.brandId =  "+brandId;
			model.addAttribute("brandId", brandId);
		}
		if (!StringUtils.isEmpty(label)) {
			hql +=" and prt.lables like '%"+label+"%'";
			model.addAttribute("labels", label);
		}
		if (!StringUtils.isEmpty(sales)) {
			hql +=" and sku.salesNum >= "+sales;
			model.addAttribute("sales", sales);
		}
		if (!StringUtils.isEmpty(sales2)) {
			hql +=" and sku.salesNum <="+sales2;
			model.addAttribute("sales2", sales2);
		}
		if (!StringUtils.isEmpty(price)) {
			hql +=" and sku.skuPrice >="+price;
			model.addAttribute("price", price);
		}
		if (!StringUtils.isEmpty(price2)) {
			hql +=" and sku.skuPrice <= "+price2;
			model.addAttribute("price2", price2);
		}
		if (!StringUtils.isEmpty(inventory)) {
 		 	hql +=" and sku.inventory >= "+inventory;
 		 	model.addAttribute("inventory", inventory);
		}
		if (!StringUtils.isEmpty(inventory2)) {
			hql +=" and sku.inventory <= "+inventory2;
			model.addAttribute("inventory2", inventory2);
		}
    	if (!StringUtils.isEmpty(startTime)){
    		hql += " and sku.createDate >=  ?   ";
    		params.add(DateUtil.switchStringToDate(startTime, "yy-MM-dd"));
    		model.addAttribute("startTime", startTime);
        }
        if (!StringUtils.isEmpty(endTime)){
        	hql += " and sku.createDate <  ? ";
        	params.add(DateUtil.getNDayAfter(endTime, 1));
        	model.addAttribute("endTime", endTime);
        }
		pageSku = skuService.page(pageSku, hql,params.toArray());
		List<Brand> brandList= brandService.list();
		List<Category> categoryList = categoryService.list(" from Category where  level  = 1");
    	List<ProductLabel> pLabelList = productLabelService.list(" from ProductLabel where status= 1") ;
    	model.addAttribute("pLabelList", pLabelList) ;
		model.addAttribute("categoryList", categoryList);
		model.addAttribute("brandList", brandList);
		model.addAttribute("flag", flag);
		model.addAttribute("pageSku", pageSku);
		return "productSale/productSaleGoodList";
	}

	/**
	 * 下架SKu
	 * @param sIdArray 
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value="soldOutSku",method= RequestMethod.POST)
	public @ResponseBody String soldOutSku(@RequestParam(name="sIdArray[]")Integer[] sIdArray,HttpServletRequest request) throws Exception{
		for (int i = 0; i < sIdArray.length; i++) {
			Integer sKuId = sIdArray[i];
			skuService.solout(sKuId);
		}
		//保存日志,HttpServletRequest request
   		LoginUser userif= UserSession.getLoginUser(request);
		Operator operator = operatorService.findUserByUsername(userif.getUsername());
		operatorLogService.addUserLog(operator, request, "下架销售中的SKUid:"+org.apache.commons.lang.StringUtils.join(sIdArray,","));
		return ResultUtil.getSuccessMsg();
	}
	
	
	/**
	 * 下架商品
	 * @param pIdArray 商品ID 集合
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value="solout",method=RequestMethod.POST)
	public @ResponseBody String soldOut(@RequestParam(name="pIdArray[]")Integer[] pIdArray,HttpServletRequest request) throws Exception{
		if(ArrayUtils.isEmpty(pIdArray)){
			return ResultUtil.getError("请选择要下架的商品!");
		}
		for (Integer pid : pIdArray) {
			productService.solout(pid);
		}
		//保存日志,HttpServletRequest request
   		LoginUser userif= UserSession.getLoginUser(request);
		Operator operator = operatorService.findUserByUsername(userif.getUsername());
		operatorLogService.addUserLog(operator, request, "下架销售中的商品id:"+org.apache.commons.lang.StringUtils.join(pIdArray,","));
		return ResultUtil.getSuccessMsg();

	}
	
	/**
	 * 获取Product二维码图片
	 * @param ptId
	 * @return base64 字符串
	 * @throws IOException 
	 * @throws WriterException 
	 */
	@RequestMapping(value = "getQrCodeImg", method = RequestMethod.GET)
	public @ResponseBody String getQrCodeImg(@RequestParam(name = "ptId", required = true) Integer ptId) throws WriterException, IOException {
		productCodeService.createQrCode(ptId);
		ProductCode productCode = productCodeService.select(" from ProductCode where productId = " + ptId);
		return productCode.getQrCode();
	}
	
	/**
	 * 获取Sku二维码图片
	 * @param ptId
	 * @return base64 字符串
	 * @throws IOException 
	 * @throws WriterException 
	 */
	@RequestMapping(value = "getSkuQrCodeImg", method = RequestMethod.GET)
	public @ResponseBody String getSkuQrCodeImg(@RequestParam(name = "skuId", required = true) Integer skuId) throws WriterException, IOException {
		skuCodeService.createQrCode(skuId);
		SkuCode skuCode = skuCodeService.select(" from SkuCode where skuId = " + skuId);
		return skuCode.getQrcode();
	}
	/**
	 * 删除货品
	 * @param pIdArray
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value="deleteProduct",method=RequestMethod.POST)
	public @ResponseBody String deleteProduct(@RequestParam(name="pIdArray[]",required=true)Integer pIdArray[] ,HttpServletRequest request) throws Exception{
		for (int i = 0; i < pIdArray.length; i++) {
			Integer productId = pIdArray[i];
			Integer num = orderService.list("select tor from OrderItem as toi ,Order as tor where toi.productId = "+productId+" and toi.orderId = tor.id and tor.payState = 1").size();
			if(num==0){ //订单未付款的商品可删除
				productService.deleteAll(productId);	
			}
		}
		//保存日志,HttpServletRequest request
   		LoginUser userif= UserSession.getLoginUser(request);
		Operator operator = operatorService.findUserByUsername(userif.getUsername());
		operatorLogService.addUserLog(operator, request, "删除销售中的商品id:"+org.apache.commons.lang.StringUtils.join(pIdArray,","));
		return ResultUtil.getSuccessMsg();
	}
	
	/**
	 * 删除货品SKu
	 * @param pIdArray
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value="deleteProductSku",method=RequestMethod.POST)
	public @ResponseBody String deleteProductSku(@RequestParam(name="skuArray[]",required=true)Integer skuArray[] ,HttpServletRequest request) throws Exception{
		for (Integer skuId : skuArray) {
			skuService.deleteSku(skuId);
		}
		//保存日志,HttpServletRequest request
   		LoginUser userif= UserSession.getLoginUser(request);
		Operator operator = operatorService.findUserByUsername(userif.getUsername());
		operatorLogService.addUserLog(operator, request, "删除销售中的SKUid:"+org.apache.commons.lang.StringUtils.join(skuArray,","));
		return ResultUtil.getSuccessMsg();
	}
	
	/**
	 * 导出货品excel
	 * @param response
	 * @param pid
	 * @throws Exception
	 */
	@RequestMapping(value="downloadExcel",method=RequestMethod.POST)
	public @ResponseBody void downloadExcel(HttpServletResponse response,String[] pid,Integer flag,HttpServletRequest request) throws Exception{
		String filename = URLEncoder.encode("博宏商城货品", "UTF-8"); // 设置字符编码为UTF-8
		response.setHeader("Content-Disposition", "attachment; filename=" +filename +System.currentTimeMillis()+ ".xlsx"); // 名字加时间戳
		ServletOutputStream sos = response.getOutputStream();
		List<ProdExcelModel> pemlist = new ArrayList<>();
		if(ArrayUtils.isEmpty(pid)){ // 写入全部
			List<Product> listProduct = productService.list(" from Product p where p.flag =  "+flag);
			for (Product product : listProduct) {
				ProdExcelModel prodExcelModel = new ProdExcelModel();
				prodExcelModel.setParams(product);
				pemlist.add(prodExcelModel);
			}
		} else {
			for (String string : pid) {
				if (NumberUtils.isNumber(string)) { // 写入指定
					Product product = productService.get(Integer.parseInt(string));
					ProdExcelModel prodExcelModel = new ProdExcelModel();
					prodExcelModel.setParams(product);
					pemlist.add(prodExcelModel);
				}
			}
		}
		ExcelWrite.writeExcel(sos, pemlist);
		sos.close();
		//保存日志,HttpServletRequest request
   		LoginUser userif= UserSession.getLoginUser(request);
		Operator operator = operatorService.findUserByUsername(userif.getUsername());
		operatorLogService.addUserLog(operator, request, "导出销售中的商品product");

		
	}
	
	/**
	 * SKu下载Excel
	 * @param response
	 * @param sid
	 * @param flag
	 * @throws Exception
	 */
	@RequestMapping(value="downloadSkuExcel",method=RequestMethod.POST)
	public @ResponseBody void downloadSkuExcel(HttpServletResponse response,String sid[],Integer flag,HttpServletRequest request) throws Exception{
		String filename = URLEncoder.encode("博宏商城货品SKU", "UTF-8"); // 设置字符编码为UTF-8
		response.setHeader("Content-Disposition", "attachment; filename=" +filename +System.currentTimeMillis()+ ".xlsx"); // 名字加时间戳
		ServletOutputStream sos = response.getOutputStream();
		List<SkuExcelModel> skuList = new ArrayList<SkuExcelModel>();
		if(ArrayUtils.isEmpty(sid)){//为空读出全部
			 List<Sku> list= skuService.list(" from Sku where flag = "+flag);
			 Map<Integer,Product> productMap = new HashMap<Integer,Product>();
			 for (Sku sku : list) {
				Integer productId  = sku.getProductId();
				if(!productMap.containsKey(productId)){
					Product product = productService.select(" from Product where id = "+sku.getProductId());
					productMap.put(productId, product);
				}
				skuList.add(new SkuExcelModel(sku,productMap.get(productId)));
			 }
		}else{
			for (String skuid : sid) {
				Sku sku = skuService.select(" from Sku where id = "+skuid);
				Product product = productService.select(" from Product where id = "+sku.getProductId());
				skuList.add(new SkuExcelModel(sku,product));
			}
		}
		ExcelWrite.writeExcel(sos, skuList);
		sos.close();
		//保存日志,HttpServletRequest request
   		LoginUser userif= UserSession.getLoginUser(request);
		Operator operator = operatorService.findUserByUsername(userif.getUsername());
		operatorLogService.addUserLog(operator, request, "导出销售中的商品SKU");
		
	}
	
	/** 
	  * 下载Product二维码
	  * @param pid  商品ID 集合
	  * @param response
	  * @param pid
	  * @throws Exception
	  */
	@RequestMapping(value="downloadQRCode",method=RequestMethod.POST)
	public @ResponseBody void downloadQRCode(HttpServletResponse response,String pid[],Integer flag) throws Exception{
		String filename = URLEncoder.encode("博宏商城货品二维码", "UTF-8"); // 设置字符编码为UTF-8
		response.setHeader("Content-Disposition", "attachment; filename=" +filename +System.currentTimeMillis()+ ".zip"); // 名字加时间戳
		
		ServletOutputStream sos =  response.getOutputStream();
	  	ZipOutputStream zos =new ZipOutputStream(sos);
		zos.setComment("https://wx.bohoon.cn/"); //注释
		if(ArrayUtils.isEmpty(pid)){
			List<Product> product = productService.list(" from Product p where p.flag =  "+flag);
			for (Product product2 : product) { //写入全部
				productCodeService.writeZip(zos, product2);
			}
		}else{
			for (String string : pid) {
				if(NumberUtils.isNumber(string)){ //写入指定
					  Integer productId = Integer.parseInt(string);
					  productCodeService.writeZip(zos, productService.get(productId));
				}
			}
		}
		zos.close();
		sos.close();
	}
	
	 /** 
	  * 下载Sku二维码
	  * @param pid  商品ID 集合
	  * @param response
	  * @param pid
	 * @throws UnsupportedEncodingException 
	  * @throws Exception
	  */
	
	@RequestMapping(value="downloadSkuQRCode",method=RequestMethod.POST)
	public @ResponseBody  void downloadSkuQRCode(HttpServletResponse response,String sid[],Integer flag) throws Exception{
		String filename =URLEncoder.encode("博宏商城货品二维码","UTF-8");
		response.setHeader("Content-Disposition",  "attachment; filename=" +filename +System.currentTimeMillis()+ ".zip");
		ServletOutputStream sos = response.getOutputStream();
		ZipOutputStream zos = new ZipOutputStream(sos);
		if(ArrayUtils.isEmpty(sid)){
			List<Sku> skuList =  skuService.list("from Sku p where p.status = 1 and p.flag = "+flag);
			for (Sku sku : skuList) {
				skuCodeService.writeZip(zos, sku);
			}
		}else{
			for (String skuId : sid) {
				Sku sku = skuService.select(" from Sku where id = "+skuId);
				skuCodeService.writeZip(zos, sku);
			}
		}
		zos.close();
		sos.close();
	}
	
	
	@RequestMapping(value="importExcel",method=RequestMethod.POST)
	public @ResponseBody String importExcel(HttpServletResponse response,@RequestParam(name="file")MultipartFile file,Integer flag) throws IOException, Exception{
//		 List<ProdExcelModel> result= ExcelRead.fastReadExcel(file.getInputStream(),ProdExcelModel.class);
//		 ProductExcel.importData(result);
		return ResultUtil.getSuccessMsg();
	}
	

 
}
