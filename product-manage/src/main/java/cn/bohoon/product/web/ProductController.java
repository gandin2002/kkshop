package cn.bohoon.product.web;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

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
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.alibaba.fastjson.JSON;

import cn.bohoon.basicSetup.helper.SysParamCache;
import cn.bohoon.basicSetup.helper.SysParamHelper;
import cn.bohoon.excel.util.ProductExcel;
import cn.bohoon.framework.exception.CheckException;
import cn.bohoon.framework.orm.domain.Page;
import cn.bohoon.framework.util.JsonUtil;
import cn.bohoon.interfaces.entity.SyncGroup;
import cn.bohoon.interfaces.service.SyncGroupService;
import cn.bohoon.main.domain.LoginUser;
import cn.bohoon.main.domain.UserSession;
import cn.bohoon.main.system.entity.Operator;
import cn.bohoon.main.system.service.OperatorLogService;
import cn.bohoon.main.system.service.OperatorService;
import cn.bohoon.product.domain.ProdExcelSkuModel;
import cn.bohoon.product.domain.ProdSku;
import cn.bohoon.product.entity.Attr;
import cn.bohoon.product.entity.AttrGroup;
import cn.bohoon.product.entity.AttrValue;
import cn.bohoon.product.entity.Brand;
import cn.bohoon.product.entity.Category;
import cn.bohoon.product.entity.PSpecificationsP;
import cn.bohoon.product.entity.Product;
import cn.bohoon.product.entity.ProductImage;
import cn.bohoon.product.entity.ProductInfo;
import cn.bohoon.product.entity.ProductLabel;
import cn.bohoon.product.entity.ProductParam;
import cn.bohoon.product.entity.Sku;
import cn.bohoon.product.entity.SkuAttr;
import cn.bohoon.product.entity.TaxCode;
import cn.bohoon.product.entity.Unit;
import cn.bohoon.product.service.AttrGroupService;
import cn.bohoon.product.service.AttrService;
import cn.bohoon.product.service.AttrValueService;
import cn.bohoon.product.service.BrandService;
import cn.bohoon.product.service.CategoryService;
import cn.bohoon.product.service.PSpecificationsPService;
import cn.bohoon.product.service.ProductImageService;
import cn.bohoon.product.service.ProductInfoService;
import cn.bohoon.product.service.ProductLabelService;
import cn.bohoon.product.service.ProductParamService;
import cn.bohoon.product.service.ProductService;
import cn.bohoon.product.service.SkuAttrService;
import cn.bohoon.product.service.SkuService;
import cn.bohoon.product.service.SkuSpecificationsSkuService;
import cn.bohoon.product.service.TaxCodeService;
import cn.bohoon.product.service.UnitService;
import cn.bohoon.product.util.DescartesUtil;
import cn.bohoon.serve.service.ServeService;
import cn.bohoon.util.IDUtil;
import cn.bohoon.util.ResultUtil;

@Controller
@RequestMapping(value="product")
public class ProductController {
	
	@Autowired
    AttrService attrService;
    @Autowired
    AttrValueService attrValueService;
    @Autowired
    BrandService brandService;
    @Autowired
    UnitService unitService;
    @Autowired
    CategoryService categoryService;
    @Autowired
    AttrGroupService attrGroupService;
    @Autowired
    ProductLabelService productLabelService ;
    @Autowired
    ProductService productService;
    @Autowired
    SkuService skuService;
    @Autowired
    SkuAttrService skuAttrService;
    @Autowired
    ProductParamService ppmService ;
    @Autowired
    ProductInfoService productInfoService;
    @Autowired
    ProductImageService productImageService;
    @Autowired
	SyncGroupService syncGroupService;
    @Autowired
    TaxCodeService taxCodeService;
    @Autowired
    PSpecificationsPService pSpecificationsPService;
    @Autowired
    OperatorLogService operatorLogService;
    @Autowired
    OperatorService operatorService;
    
	/**
	 * 商品列表
	 * 
	 * @param model
	 * @param request
	 * @param code
	 * @param name
	 * @param flag
	 * @return
	 */
    @RequestMapping(value = "list")
    public String list(Model model, HttpServletRequest request, String code, String name,Integer flag){
    	Integer pageNo = ServletRequestUtils.getIntParameter(request, "pageNo", 1);
    	Page<Product> pageProduct = new Page<Product>();
    	pageProduct.setPageNo(pageNo);
    	String hql = " from Product p where 1 = 1 ";
    	List<Object> params = new ArrayList<>();
    	if(!StringUtils.isEmpty(code)){
    		hql = hql + " and p.code like ? ";
    		params.add('%'+code+'%');
    		model.addAttribute("code", code);
    	}
    	if(!StringUtils.isEmpty(name)){
    		hql = hql + " and p.name like ? ";
    		params.add('%'+name+'%');
    		model.addAttribute("name", name);
    	}
    	if(!StringUtils.isEmpty(flag)){
    		hql = hql + " and p.flag = ? ";
    		params.add(flag);
    		model.addAttribute("flag", flag);
    	}
    	pageProduct = productService.page(pageProduct, hql, params.toArray());
    	Map<Product, ProductImage> prodImageMap = new HashMap<Product, ProductImage>();
    	for (Product prod : pageProduct.getResult()) {
    		ProductImage pi = prod.getProductImage() ;
    		if(!StringUtils.isEmpty(pi)){
    			prodImageMap.put(prod, pi);
    		}
    	 
		}
    	model.addAttribute("resultUrl", request.getRequestURI());
    	model.addAttribute("prodImageMap", prodImageMap);
    	model.addAttribute("pageProduct", pageProduct);
    	return "product/productList";
    }
    
    
    /**
     * 新增商品 选择分类
     * 
     * @param model
     * @return
     */
    @RequestMapping(value="toAdd/Step1",method=RequestMethod.GET)
    public String addStep1(Model model) {
    	List<Category> categoryList = categoryService.list();
    	model.addAttribute("categoryList", categoryList);
    	return "product/addStep1" ;
    }
    
    
    /**
     * 新增商品页面
     * 
     * @param request
     * @param model
     * @return
     */
    @RequestMapping(value = "add", method = RequestMethod.GET)
    public String addGet(HttpServletRequest request, Model model) throws Exception {
    	String firstCateId = ServletRequestUtils.getStringParameter(request, "firstCate") ;
    	String secondCateId = ServletRequestUtils.getStringParameter(request, "secondCate") ;
    	String thirdCateId = ServletRequestUtils.getStringParameter(request, "thirdCate") ;
    	String fourthCateId = ServletRequestUtils.getStringParameter(request, "fourthCate") ;
    	Category firstCate = categoryService.get(firstCateId);
    	model.addAttribute("firstCate", firstCate);
    	String categoryId = firstCateId ;
    	if(firstCateId != null){
    		List<Category> listc1=categoryService.list(" from Category  where level = 1 ");
    		model.addAttribute("listc1", listc1);
    	}
    	
    	
    	if(!StringUtils.isEmpty(secondCateId)) {
    		categoryId = secondCateId ;
    		Category secondCate = categoryService.get(secondCateId) ;
    		model.addAttribute("secondCate", secondCate) ;
    		
    		List<Category> listc2=categoryService.list(" from Category  where level = 2 and pid = ? ",firstCateId);
    		model.addAttribute("listc2", listc2);
    	}
    	if(!StringUtils.isEmpty(thirdCateId)) {
    		categoryId = thirdCateId ;
    		Category thirdCate = categoryService.get(thirdCateId) ;
    		model.addAttribute("thirdCate", thirdCate) ;
    		
    		List<Category> listc3=categoryService.list(" from Category  where level = 3 and pid = ? ",secondCateId);
    		model.addAttribute("listc3", listc3);
    	}
    	if(!StringUtils.isEmpty(fourthCateId)) {
    		categoryId = fourthCateId ;
    		Category fourthCate = categoryService.get(fourthCateId) ;
    		model.addAttribute("fourthCate", fourthCate) ;
    	}
    	
    	List<TaxCode> taxcodeArray = taxCodeService.list();
    	model.addAttribute("taxcodeArray", taxcodeArray);
    	
    	model.addAttribute("categoryId", categoryId) ;
    	model.addAttribute("serveList", serveService.list(" from Serve where state = 1"));
    	
    	initDate(model,categoryId);
    	String code = IDUtil.getInstance().getIdByDb(productService,Product.class,"SP","code") ;
    	model.addAttribute("code", code) ;
    	return "product/productAdd";
    }

    /**
     * 保存新增商品信息
     * 
     * @param request
     * @param model
     * @param prodSku
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "add", method = RequestMethod.POST)
    public String addPost(HttpServletRequest request, Model model, ProdSku prodSku ) throws Exception {
    	try {
    		
			productService.saveProdSku(prodSku);
		} catch (CheckException e) {
			return ResultUtil.getError(e.getMessage());
		}
		//保存日志,HttpServletRequest request
  		LoginUser userif= UserSession.getLoginUser(request);
		Operator operator = operatorService.findUserByUsername(userif.getUsername());
		operatorLogService.addUserLog(operator, request, "新增商品编码:"+prodSku.getProd().getCode());
    	return ResultUtil.getSuccessMsg();
    }
    
    @Autowired
    ServeService serveService;
    
    /**
     * 去编辑页面
     * 
     * @param request
     * @param model
     * @param id
     * @return
     */
    /*
     *  String product, String sku, String categoryId,
			Integer brandId, String label, Integer sales, Integer sales2, BigDecimal price, BigDecimal price2,
			Integer inventory, Integer inventory2, String startTime, String endTime
     */
    @RequestMapping(value = "edit",method=RequestMethod.GET)
    public String editGet(HttpServletRequest request,Model model,Integer id,String resultUrl, String product, String sku, String categoryId,
			Integer brandId, String label, Integer sales, Integer sales2, BigDecimal price, BigDecimal price2,
			Integer inventory, Integer inventory2, String startTime, String endTime) {
      if(!StringUtils.isEmpty(product)){
    		System.out.println("query查询条件"+product);
    		model.addAttribute("product",product);
    	}
    	if(!StringUtils.isEmpty(sku)){
    
    		model.addAttribute("sku",sku);
    	}
    	if(!StringUtils.isEmpty( categoryId)){
    		
    		model.addAttribute("categoryId", categoryId);
    	}
    	if(!StringUtils.isEmpty(brandId)){
    		System.out.println("分页条件"+brandId);
    		model.addAttribute("brandId",brandId);
    	}
    	if(!StringUtils.isEmpty(label)){
    		
    		model.addAttribute("label", label);
    	}
    	
       if(!StringUtils.isEmpty(sales)){
    		
    		model.addAttribute("sales", sales);
    	}
       if(!StringUtils.isEmpty(sales2)){
   		
   		model.addAttribute("sales2", sales2);
   	}
       if(!StringUtils.isEmpty(price)){
   		
   		model.addAttribute("price", price);
   	}
       if(!StringUtils.isEmpty(price2)){
   		
   		model.addAttribute("price2",price2);
   	}
       if(!StringUtils.isEmpty(inventory)){
   		
   		model.addAttribute("inventory", inventory);
   	}
       if(!StringUtils.isEmpty(inventory2)){
   		
   		model.addAttribute("inventory2", inventory2);
   	}
       if(!StringUtils.isEmpty(startTime)){
    	   model.addAttribute("startTime",startTime);
       }
       if(!StringUtils.isEmpty(endTime)){
    	   model.addAttribute("endTime", endTime);
       }
		Product prod = productService.get(id);
		
    	Category firstCate = categoryService.get(prod.getCategoryId()); 
    	model.addAttribute("firstCate", firstCate); //获取父分类
    	
    	List<Category> categ3= categoryService.list(" from Category where pid = ? ",firstCate.getPid());
    	model.addAttribute("categ3", categ3);//三级目录
    	
    	if(!StringUtils.isEmpty(firstCate.getPid())){ //如何pid 不为空
    		
    		Category secondCate=categoryService.get(firstCate.getPid());//拿二级目录
    		model.addAttribute("secondCate", secondCate) ;
    		List<Category> categ2 = categoryService.list(" from Category where( id = ? and level = ?)",firstCate.getPid(),secondCate.getLevel()); //二级全部目录
    		model.addAttribute("categ2", categ2);
    		
    		if(!StringUtils.isEmpty(secondCate.getPid())&& firstCate.getLevel() == 3){
    			
    			Category thirdCate=categoryService.get(firstCate.getPid());
    			model.addAttribute("thirdCate", thirdCate);
 
    		} 
    		
    	} 
		List<Category> categ1 = categoryService.list(" from Category where level = 1   ");
		model.addAttribute("categ1", categ1);
		initDate(model,prod.getCategoryId());
		
		List<Category> categoryList = categoryService.list();
    	model.addAttribute("categoryList", categoryList);
    	
		if(!StringUtils.isEmpty(prod.getLables())){
			List<String> lsLables = Arrays.asList(prod.getLables().split(","));
			model.addAttribute("lsLables",lsLables);
		}
		ProductInfo prodInfo = productInfoService.get(id);
		List<ProductImage> lsProdImages = productImageService.list("from ProductImage where productId = ?", id);
		String hql = "select sat from SkuAttr sat , Sku sku where sku.status = 1 and sku.productId = ? and sat.skuId = sku.id " ;
		Set<String> setAttrValue = new HashSet<String>();
		List<SkuAttr> selectedValues = skuAttrService.list(hql, id)  ;
		
		Map<Integer,String> setAttrImageMap = new HashMap<Integer,String> ();
		for(SkuAttr skuAttr : selectedValues) {
			setAttrValue.add(String.valueOf(skuAttr.getAttrValueId()));
			setAttrImageMap.put(skuAttr.getAttrValueId(), skuAttr.getAttrImage());
		}
    	
		if(!model.containsAttribute("attrList")) {
			
    		Map<Attr, List<AttrValue>>  attrValueMap = new HashMap<>();
    		
    		String aql = " select distinct a from Attr a ,SkuAttr sa ,Sku s  where s.status=1 and s.flag=1 and a.id=sa.attrId and sa.skuId = s.id and s.productId="+id ;
			List<Attr> attrList = attrService.list(aql) ;
    		for(Attr attr : attrList) {
        		List<AttrValue> attrValueList = attrValueService.list("from AttrValue where status = 1 and attrId = ?", attr.getId());
        		attrValueMap.put(attr, attrValueList);
        	}

        	model.addAttribute("attrList", attrList);
        	
        	model.addAttribute("attrValueMap", attrValueMap);
	    
		}
    	List<TaxCode> taxcodeArray = taxCodeService.list();
    	model.addAttribute("taxcodeArray", taxcodeArray);
    	
		List<ProductParam> ppms = ppmService.list(" from ProductParam where productId=?", id) ;
		String[] res=resultUrl.split(",");
		if(res.length>=2){
			if(res[0].toString().equals(res[1].toString())){
			   resultUrl=res[0].toString();
			}
		}
		
		
		String  serveId =  prod.getServeId();
		if(!StringUtils.isEmpty(serveId)){
			String[] servIdArray = serveId.split(",");
			model.addAttribute("servIdArray",servIdArray);
		}
		
		model.addAttribute("serveList", serveService.list(" from Serve where state = 1"));
		model.addAttribute("resultUrl", resultUrl);
    	model.addAttribute("prod",prod);
    	model.addAttribute("ppms",ppms);
    	model.addAttribute("prodInfo",prodInfo);
    	model.addAttribute("lsProdImages", lsProdImages);
    	model.addAttribute("setAttrImageMap", setAttrImageMap);
    	model.addAttribute("selectedValues", new ArrayList<String>(setAttrValue));
		return "product/productEdit";
    }
    
    /**
     * 编辑保存商品信息
     * 
     * @param request
     * @param prodSku
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "edit",method=RequestMethod.POST)
    @ResponseBody
    public String editPost(HttpServletRequest request, ProdSku prodSku) throws Exception {
    	Product prod = prodSku.getProd();
    	if(null!=prod){
    		if(!StringUtils.isEmpty(prod.getShowSort())){
    			
    			prod.setShowSort(null);
                 			
    		}
    	}
		productService.saveProdSku(prodSku);
		//保存日志,HttpServletRequest request
  		LoginUser userif= UserSession.getLoginUser(request);
		Operator operator = operatorService.findUserByUsername(userif.getUsername());
		operatorLogService.addUserLog(operator, request, "修改商品编码:"+prodSku.getProd().getCode());
    	return ResultUtil.getSuccessMsg();
    }
    
    /**
     * 新增&编辑页面初始信息
     * 
     * @param model
     */
    private void initDate(Model model,String categoryId) {
    	
    	String hql = "select g from AttrGroup g , Category c where g.id = c.attrGoupId and c.id = ?" ;
    	List<AttrGroup> groupList = attrGroupService.list(hql,categoryId) ;
    	System.out.println("参数组"+groupList);
    	if(groupList.size() >0 ) {
    		Map<Attr, List<AttrValue>>  attrValueMap = new HashMap<>();
    		AttrGroup attrGroup = groupList.get(0) ;
    		List<Attr> attrList = attrService.list(" from Attr where id in ("+attrGroup.getSpecsIds()+")") ;
    		for(Attr attr : attrList) {
        		List<AttrValue> attrValueList = attrValueService.list("from AttrValue where status = 1 and attrId = ?", attr.getId());
        		attrValueMap.put(attr, attrValueList);
        	}

        	model.addAttribute("attrList", attrList);
        	model.addAttribute("attrGroup", attrGroup);
//        	System.out.println("长度："+attrGroup.getParams().size());
        	model.addAttribute("attrValueMap", attrValueMap);
    	}
    	
		List<Brand> brandList = brandService.list();
    	
    	// 通过分类id获取商品对应的品牌
    	Category cg = categoryService.select("from Category where id=?", categoryId);
    	
    	List<Brand> bran = new ArrayList<>();
    	
    	if (!StringUtils.isEmpty( cg.getBrandIds() )){
    		
    		String[] brands = cg.getBrandIds().split(",");
    		
    		for (String brand : brands){
    			
    			
    			Brand br = brandService.get(new Integer(brand));
    			brandList.removeIf((x) -> x.getId().equals( br.getId() ));
    			bran.add(br);
    		}
    		
    		
    	}
    	
    	
    	model.addAttribute("bran",bran);
		model.addAttribute("brandList", brandList);
    	List<Unit> unitList = unitService.list();
    	model.addAttribute("unitList", unitList);
    	 
    	List<ProductLabel> pLabelList = productLabelService.list(" from ProductLabel where status= 1 and name not in('预售','预定','定制')") ;
    	model.addAttribute("pLabelList", pLabelList) ;
    	
    	
	}
    
    /**
     * sku 属性组合
     * 
     * @param model
     * @param selectedValues 规格ID[]
     * @param productId 产品ID
     * @param salesPrice 销售价格
     * @param volume 体积
     * @param weight 重量
     * @return
     */
    @RequestMapping(value = "sku",method=RequestMethod.GET)
    public String sku(Model model,String selectedValues,Integer productId,BigDecimal salesPrice,BigDecimal volume,BigDecimal weight,String selectedImage,String categoryId) {
    	if(StringUtils.isEmpty(selectedValues)) {
    		return null;
    	}
    	String[] ids = selectedValues.split(",");
    	List<Attr> attrList = attrService.list() ;
    	List<List<String>> dimValue = new ArrayList<List<String>>();
    	Map<Attr, List<String>> attrValueMap = new HashMap<>();
    	for(Attr attr : attrList) {
    		List<String> list = new ArrayList<>();
    		
    		for (int i = 0; i < ids.length; i++) {
				String id = ids[i];
				AttrValue attrValue = attrValueService.get(Integer.parseInt(id));
    			if(attrValue.getAttrId().equals(attr.getId())) {//比对
    				list.add(String.valueOf(attrValue.getId())); //存入属性值 ID
    			}
			}
    		
    		attrValueMap.put(attr, list);
    		if(list.size()>0){
    			dimValue.add(list);
    		}
    	}
    	List<AttrValue> attrValueList = attrValueService.list("from AttrValue where status =1 order by attrId") ;
    	List<String> result = DescartesUtil.descartes(dimValue);
    	List<Sku> skus = new ArrayList<Sku>();
    	for (int i=0;i<result.size();i++) {
    		String attrValues = result.get(i);
    		Sku sku = skuService.select("from Sku where productId = ? and attrValues = ?", productId, attrValues);
    	
    		if(null == sku) {
    			sku = new Sku();
    			sku.setAttrValues(attrValues);
    			sku.setSkuPrice(salesPrice);
    			sku.setVolume(volume);
    			sku.setTranslateRate(1);
    			sku.setWeight(weight);
    			Category category = categoryService.get(categoryId);
    			sku.setScore(category.getScorePercentage());
    		}
    		Sku target = skuService.select("from Sku where productId = ? and attrValues  = 0", productId);
    		if(target!= null){
    			sku.setErpCode(target.getErpCode());
    			sku.setTax(target.getTax());
    			sku.setTaxId(target.getTaxId());
    			sku.setTaxCode(target.getTaxCode());
    			if(!StringUtils.isEmpty(target.getErpUnitCode()) && StringUtils.isEmpty(sku.getAuxUnitId())){
    				sku.setErpUnitCode(target.getErpUnitCode());
    				Unit unit = unitService.select(" from Unit where code = ? ", target.getErpUnitCode());
    				sku.setErpUnitCode(target.getErpUnitCode());
    				sku.setAuxUnitId(unit.getId());
    				sku.setAuxUnitName(unit.getName());
    			}
    			
    		}
    		skus.add(sku);
    	}
    	
    	
    	// 判断商品表的是否开启
    	boolean isFlag = false;
    	// select * from t_sync_group where (mallTable='t_company' or mallTable='t_License') and status = 1;
    	String hql = "from SyncGroup where mallTable=? or mallTable=?";
    	List<String> params = new ArrayList<String>();
    	params.add("t_sku");
    	params.add("t_product");
    	List<SyncGroup> list = syncGroupService.list(hql, params.toArray());
    	
    	if (null != list && list.size() > 0 ){
    		
    		isFlag = true;
    	}
    	
    	model.addAttribute("isFlag",isFlag);
    	
    	
    	List<Unit> unitList = unitService.list();
    	
    	model.addAttribute("jsonImageArray",  JSON.parseArray(selectedImage));
    	model.addAttribute("unitList", unitList);
    	model.addAttribute("attrList", attrList);
    	model.addAttribute("attrValueList", attrValueList);
    	model.addAttribute("attrValueMap", attrValueMap);
    	model.addAttribute("skus", skus);
    	return "product/productSku";
    }
    /**
     * 获取SKU的erpcode
     */    
    @ResponseBody
    @RequestMapping(value="/getErpcode",method = RequestMethod.GET)
    public String getAttr(HttpServletRequest request){
    	Integer pageNo = ServletRequestUtils.getIntParameter(request, "pageNo", 1);
    	String erpcode = ServletRequestUtils.getStringParameter(request, "str", "") ;
    	Page<Sku> skus = new Page<Sku>();
    	skus.setPageNo(pageNo);
    	String hql ="from Sku where 1 = 1";
    	List<Object> params = new ArrayList<>();
    	
    	if(!StringUtils.isEmpty(erpcode)){
    		hql +=" and erpCode like  ? ";
    		params.add("%"+erpcode+"%");
    	}
    	skus = skuService.page(skus, hql,params.toArray());
    	System.out.println(JsonUtil.toJson(skus));
    	
    	List hashList = new ArrayList();
    	
    	for (Sku  s : skus.getResult()){
    		
    		HashMap<Object,Object> hash = new HashMap<Object,Object> ();
    		hash.put("id", s.getId());
    		
    		String st = s.getProduct().getName();
    		// 商品名
    		if (st.length() > 10){
    			
    			st = st.substring(0, 10) + "...";
    		}
    		hash.put("name", st+s.getAttrValueNames()+"</div>");
    		hash.put("ids",s.getErpCode() );
    		
    		// 规格名
//    		hash.put("attr",s.getAttrValueNames());
    		hashList.add(hash);
    	}
 	
    	skus.setResult(hashList);
	
    	return JsonUtil.toJson(skus);
    }
    
    
    /**
     * 通过skuId获取sku信息
     */
    @ResponseBody
    @RequestMapping(value="/getSkuData",method = RequestMethod.POST)
    public String getSku(String id){
    	
    
    	Sku sku = skuService.get(new Integer(id));
    	
    	if (null == sku){
    		return "1";
    	}
    	
    	return JsonUtil.toJson(sku);
    }
    
    
    
    
    
    /**
     * SKU列表
     * @param model
     * @param productId
     * @return
     */
    @RequestMapping(value = "skuList",method=RequestMethod.GET)
    public String skuList(Model model,Integer productId) {
    	List<Attr> attrList = attrService.list() ;
    	Product prod = productService.get(productId);
    	List<Sku> skus = skuService.list("from Sku where productId = ?  and status = 1 ", productId);
    	List<AttrValue> attrValueList = attrValueService.list("from AttrValue where status =1 order by attrId") ;
    	
    	model.addAttribute("skus", skus);
    	model.addAttribute("prod", prod);
    	model.addAttribute("attrList", attrList);
    	model.addAttribute("attrValueList", attrValueList);
    	return "product/skuList";
    }
    
    
    
    @RequestMapping(value = "noSku",method=RequestMethod.GET)
    public String noSku(Model model,Integer productId,BigDecimal salesPrice,BigDecimal volume,BigDecimal weight,String categoryId) {
    	 
    	List<Sku> skus = new ArrayList<>();
		Sku sku = skuService.select("from Sku where productId = ? and attrValues = ?", productId, "0");
		if(null == sku) {
			sku = new Sku();
			sku.setAttrValues("0");
			sku.setSkuPrice(salesPrice);
			sku.setVolume(volume);
			sku.setWeight(weight);
			sku.setTranslateRate(1);
			Category category = categoryService.get(categoryId);
			sku.setScore(category.getScorePercentage());
		}
		
		// 判断商品表的是否开启
    	boolean isFlag = false;
    	// select * from t_sync_group where (mallTable='t_company' or mallTable='t_License') and status = 1;
    	String hql = "from SyncGroup where (mallTable=? or mallTable=?) and status = 1";
    	List<String> params = new ArrayList<String>();
    	params.add("t_sku");
    	params.add("t_product");
    	List<SyncGroup> list = syncGroupService.list(hql, params.toArray());
    	
    	if (null != list && list.size() > 0 ){
    		
    		isFlag = true;
    	}
    	
    	model.addAttribute("isFlag",isFlag);
		
		
		skus.add(sku);
		List<Unit> unitList = unitService.list();
    	model.addAttribute("unitList", unitList);
    	model.addAttribute("skus", skus);
    	return "product/productNoSku";
    }
    
    
	/**
	 * SKU上下架切换
	 * 
	 * @param request
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/switchSkuFlag")
	@ResponseBody
	public String switchSkuFlag(HttpServletRequest request, Integer id) {
		Sku sku = skuService.get(id);
		sku.setFlag(!sku.getFlag());
		skuService.save(sku);
		return ResultUtil.getSuccessMsg();
	}
    
    
    
    /**
     * 去新增商品关联 
     * 
     * @param request
     * @param model
     * @param mainProdId
     * @return
     */
    @RequestMapping(value="relatedAdd",method=RequestMethod.GET)
    public String relatedAddGet(HttpServletRequest request,Model model,Integer mainProdId){
    	Integer pageNo = ServletRequestUtils.getIntParameter(request, "pageNo", 1);
    	Page<Product> pageProduct = new Page<Product>();
    	pageProduct.setPageNo(pageNo);
    	pageProduct = productService.page(pageProduct, "from Product");
    	Product mainProd = productService.get(mainProdId);
    	model.addAttribute("pageProduct", pageProduct);
    	model.addAttribute("mainProd", mainProd);
		return "product/productRelatedAdd";
	}
    
    /**
     * 搜索商品
     * 
     * @param model
     * @param request
     * @param mainProdId
     * @param codeOrName
     * @return
     */
    @RequestMapping(value = "search")
    public String search(Model model, HttpServletRequest request, Integer mainProdId, String codeOrName){
    	Integer pageNo = ServletRequestUtils.getIntParameter(request, "pageNo", 1);
    	Page<Product> pageProduct = new Page<Product>();
    	pageProduct.setPageNo(pageNo);
    	String hql = " from Product p where 1 = 1 ";
    	List<Object> params = new ArrayList<>();
    	if(!StringUtils.isEmpty(codeOrName)){
    		hql = hql + " and p.searchWords like ? ";
    		params.add('%'+codeOrName+'%');
    	}
    	pageProduct = productService.page(pageProduct, hql, params.toArray());
    	Product mainProd = productService.get(mainProdId);
    	model.addAttribute("pageProduct", pageProduct);
    	model.addAttribute("mainProd", mainProd);
    	return "product/searchResults";
    }
    
     
    
    /**
     * 预览页面
     * 
     * @param id
     * @return
     */
    @RequestMapping(value = "preview", method=RequestMethod.GET)
    public String productPreview(String id){
    	String pcSite = SysParamCache.getCache().getSysParamValue(SysParamHelper.PC_SITE) ;
    	return "redirect:"+pcSite+"/product/"+id;
    }
    
    /**
     * 批量导入页面
     * 
     * @return
     */
    @RequestMapping(value="betch",method=RequestMethod.GET)
    public String betchGet() {
    	
    	return "product/betch" ;
    }
    
    
    @RequestMapping(value="betch",method=RequestMethod.POST)
    public  String betchPost(HttpServletRequest request,Model model) throws Exception {
    	MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		MultipartFile file = multipartRequest.getFile("file");
		List<ProdExcelSkuModel> result =ProductExcel.getNewExcel(file.getInputStream()) ;
		Map<String,List<ProdExcelSkuModel>> productMap = ProductExcel.NewimportData(result);
		model.addAttribute("productMap", productMap);
		//保存日志,HttpServletRequest request
  		LoginUser userif= UserSession.getLoginUser(request);
		Operator operator = operatorService.findUserByUsername(userif.getUsername());
		operatorLogService.addUserLog(operator, request, "商品批量导入");
    	return "product/productImportTable";
    }
    /**
     * 货品提示
     * @param product
     * @param pageNo
     * @return
     */
    @RequestMapping(value="getProduct")
    public @ResponseBody String getProduct(String product,@RequestParam(name="pageNo",defaultValue="1")Integer pageNo){
		Page<Product> productPage = new Page<>();
		productPage.setPageNo(pageNo);
		productPage = productService.page(productPage," from Product  where (name like ? or code like ?)","%"+product+"%","%"+product+"%");
    	return JSON.toJSONString(productPage);
    }
    
    @RequestMapping(value="deleteAll")
    public @ResponseBody String deleteAll(){
		List<Product> product = productService.list();
		for (Product product2 : product) {
			productService.deleteAll(product2.getId());
		}
		return ResultUtil.getSuccessMsg();
    }
    @Autowired
    SkuSpecificationsSkuService skuSpecificationsSkuService;
    /**
     * 使用规格商品关联商品
     * @param 
     * @param 
     * @return
     * @throws Exception 
     */
	@RequestMapping(value = "pSpecificationsP", method = RequestMethod.POST)
	public @ResponseBody String skuSpecificationsSku(Model model, HttpServletRequest request,
			@RequestParam(name = "name", required = true) String name,
			@RequestParam(name = "mainPId", required = true) Integer mainPId,
			@RequestParam(name = "pIdArray[]") Integer[] pIdArray) throws Exception {
		
		if(StringUtils.isEmpty(name)){
			return ResultUtil.getError("名称不能为空");
		}
		
    	 List<PSpecificationsP> psps =  pSpecificationsPService.list("from PSpecificationsP where mainPId=? ",mainPId);
    	 if (psps.size()>0) {
			for (PSpecificationsP p : psps) {
				pSpecificationsPService.delete(p.getId());
			}
		}
    	for (Integer  st : pIdArray) {
    		PSpecificationsP pp=new PSpecificationsP();
    		pp.setMainPId(mainPId);
        	pp.setVicePId(st);
        	pp.setName(name);
        	pSpecificationsPService.save(pp);
		}
    	//保存日志,HttpServletRequest request
  		LoginUser userif= UserSession.getLoginUser(request);
		Operator operator = operatorService.findUserByUsername(userif.getUsername());
		operatorLogService.addUserLog(operator, request, "规格关联商品");
		return ResultUtil.getSuccessMsg();
	}
    /**
     * 查询 列表
     * @param 
     * @param 
     * @return
     */
    @ResponseBody
    @RequestMapping(value="skuInquire",method=RequestMethod.GET)
    public  String skuInquire(Model model, HttpServletRequest request){ 
		Integer pageNo = ServletRequestUtils.getIntParameter(request, "pageNo", 1);
		String str = ServletRequestUtils.getStringParameter(request, "str", "") ;
		Page<Product> productPage = new Page<Product>();
		productPage.setPageNo(pageNo);
		List<Object> params = new ArrayList<>();
		String strId="%"+str+"%";
		String strId1="%"+str+"%";
		String hql="from Product where ( code like ? or name like ?) and lables is not null";
		params.add(strId);
		
		params.add(strId1);
		System.out.println(params);
		productPage = productService.page(productPage, hql,params.toArray());
		return JsonUtil.toJson(productPage);
    }
    /**
     * 查询当前选中的Sku
     * @param 
     * @param 
     * @return
     */
    @ResponseBody
    @RequestMapping(value="pitchSku",method=RequestMethod.GET)
    public  String pitchSku(Model model, HttpServletRequest request){ 
		Integer id = ServletRequestUtils.getIntParameter(request, "id", 0);
		Product product =productService.select("from Product where id = ?", id);
		return JsonUtil.toJson(product);
    }
    
}
