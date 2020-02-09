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
import cn.bohoon.main.domain.LoginUser;
import cn.bohoon.main.domain.UserSession;
import cn.bohoon.main.system.entity.Operator;
import cn.bohoon.main.system.service.OperatorLogService;
import cn.bohoon.main.system.service.OperatorService;
import cn.bohoon.product.domain.ProdExcelModel;
import cn.bohoon.product.domain.ProdSkuScore;
import cn.bohoon.product.entity.Attr;
import cn.bohoon.product.entity.AttrGroup;
import cn.bohoon.product.entity.AttrValue;
import cn.bohoon.product.entity.Brand;
import cn.bohoon.product.entity.Category;
import cn.bohoon.product.entity.Product;
import cn.bohoon.product.entity.ProductImage;
import cn.bohoon.product.entity.ProductInfo;
import cn.bohoon.product.entity.ProductLabel;
import cn.bohoon.product.entity.ProductParam;
import cn.bohoon.product.entity.ProductScore;
import cn.bohoon.product.entity.Sku;
import cn.bohoon.product.entity.SkuAttr;
import cn.bohoon.product.entity.SkuScore;
import cn.bohoon.product.entity.TaxCode;
import cn.bohoon.product.entity.Unit;
import cn.bohoon.product.service.AttrGroupService;
import cn.bohoon.product.service.AttrService;
import cn.bohoon.product.service.AttrValueService;
import cn.bohoon.product.service.BrandService;
import cn.bohoon.product.service.CategoryService;
import cn.bohoon.product.service.ProductImageService;
import cn.bohoon.product.service.ProductInfoService;
import cn.bohoon.product.service.ProductLabelService;
import cn.bohoon.product.service.ProductParamService;
import cn.bohoon.product.service.ProductScoreService;
import cn.bohoon.product.service.ProductService;
import cn.bohoon.product.service.SkuAttrService;
import cn.bohoon.product.service.SkuScoreService;
import cn.bohoon.product.service.SkuService;
import cn.bohoon.product.service.TaxCodeService;
import cn.bohoon.product.service.UnitService;
import cn.bohoon.product.util.DescartesUtil;
import cn.bohoon.util.IDUtil;
import cn.bohoon.util.ResultUtil;

@Controller
@RequestMapping(value="productScore")
public class ProductScoreController {
	@Autowired
	SkuService skuService ;
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
    ProductScoreService productScoreService;
    @Autowired
    SkuScoreService skuScoreService;
    @Autowired
    SkuAttrService skuAttrService;
    
    @Autowired
    ProductParamService ppmService ;
    
    @Autowired
    ProductInfoService productInfoService;
    @Autowired
    ProductImageService productImageService;
    @Autowired
    ProductService productService;
    
    @Autowired
    TaxCodeService taxCodeService;
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
    public String list(Model model, HttpServletRequest request){
    	Integer pageNo = ServletRequestUtils.getIntParameter(request, "pageNo", 1);
    	String code = ServletRequestUtils.getStringParameter(request, "code", "") ;
    	String name = ServletRequestUtils.getStringParameter(request, "name", "") ;
    	String flag = ServletRequestUtils.getStringParameter(request, "flag", "") ;
    	List<ProductScore> productScoreList = new ArrayList<ProductScore>();
    	String hql1 = "from ProductScore ps where 1=1";
    	List<Object> params = new ArrayList<>();
    	if(!StringUtils.isEmpty(code)){
    		hql1 = hql1 + " and ps.code like ? ";
    		params.add('%'+code+'%');
    		model.addAttribute("code", code);
    	}
    	if(!StringUtils.isEmpty(name)){
    		hql1 = hql1 + " and ps.name like ? ";
    		params.add('%'+name+'%');
    		model.addAttribute("name", name);
    	}
    	if(!StringUtils.isEmpty(flag)){
    		hql1 = hql1+ " and ps.flag = ? ";
    		params.add(flag);
    		model.addAttribute("flag", flag);
    	}
    	
    	Map<ProductScore, ProductImage> prodImageMap = new HashMap<ProductScore, ProductImage>();
    	productScoreList=productScoreService.list(hql1, params.toArray());
    	
    	Page<SkuScore> pageSkuScore = new Page<SkuScore>();
    	pageSkuScore.setPageNo(pageNo);
    	List<SkuScore> skuscore=new ArrayList<SkuScore>();
    	for (ProductScore productScore : productScoreList) {
    		ProductImage pi = productScore.getProductImage();
    		if(!StringUtils.isEmpty(pi)){
    			prodImageMap.put(productScore, pi);
    		}
    		String hql2 = "select ss from ProductScore  ps,SkuScore ss where ps.id = ss.productId and ps.id=?";
        	pageSkuScore = skuScoreService.page(pageSkuScore, hql2, productScore.getId());
        	for (SkuScore skus : pageSkuScore.getResult()) {
        		skus.setProductScore(productScore);
    		}
        	skuscore.addAll(pageSkuScore.getResult());
		}
    	pageSkuScore.setResult(skuscore);
   
    	model.addAttribute("resultUrl", request.getRequestURI());
    	model.addAttribute("prodImageMap", prodImageMap);
    	model.addAttribute("pageProduct", pageSkuScore);
    	return "productScore/productList";
    }
    
    
    /**
     * 新增商品 选择分类
     * 
     * @param model
     * @return
     */
    @RequestMapping(value="addFromPro",method=RequestMethod.GET)
    public String addFromPro(Model model,HttpServletRequest request) {
    	Integer pageNo = ServletRequestUtils.getIntParameter(request, "pageNo", 1);
    	String code = ServletRequestUtils.getStringParameter(request, "code", "") ;
    	String name = ServletRequestUtils.getStringParameter(request, "name", "") ;
    	String flag = ServletRequestUtils.getStringParameter(request, "flag", "") ;
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
    	hql += " and p.id not in (select pId from ProductScore) " ;
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
    	return "productScore/addFromPro" ;
    }
    
    /**
     * 通过选择新增ID 
     * 
     * @param pId
     * @return
     * @throws Exception 
     */
    @RequestMapping(value="addFromProSave",method=RequestMethod.POST)
    @ResponseBody
    public String addFromProSave(Integer []pId,HttpServletRequest request) throws Exception {
    	if(!StringUtils.isEmpty(pId)) {
    		for(Integer id : pId) {
    			Product pro = productService.get(id);
    			ProductScore ps = new ProductScore(pro);
    			productScoreService.save(ps);
    			Integer psId = ps.getId();
    			String hql = " from Sku where productId=?";
    			List<Sku> skus = skuService.list(hql,id);
    			for(Sku sku : skus ) {
    				SkuScore ss = new SkuScore(sku);
    				ss.setFlag(ps.getFlag());
    				ss.setProductId(psId);
    				skuScoreService.save(ss);
    			}
    		}
    	}
    	//保存日志,HttpServletRequest request
   		LoginUser userif= UserSession.getLoginUser(request);
		Operator operator = operatorService.findUserByUsername(userif.getUsername());
		operatorLogService.addUserLog(operator, request, "通过选择新增赠品id:"+org.apache.commons.lang.StringUtils.join(pId,","));
    	return ResultUtil.getSuccessMsg();
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
    	return "productScore/addStep1" ;
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
    	initDate(model,categoryId);
    	String code = IDUtil.getInstance().getIdByDb(productScoreService,ProductScore.class,"SP","code") ;
    	model.addAttribute("code", code) ;
    	return "productScore/productAdd";
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
    public String addPost(HttpServletRequest request, Model model, ProdSkuScore prodSku ) throws Exception {
    	try {
    		productScoreService.saveProdSku(prodSku);
		} catch (CheckException e) {
			return ResultUtil.getError(e.getMessage());
		}
    	//保存日志,HttpServletRequest request
   		LoginUser userif= UserSession.getLoginUser(request);
		Operator operator = operatorService.findUserByUsername(userif.getUsername());
		operatorLogService.addUserLog(operator, request, "新增赠品:"+prodSku.getProd().getName());
    	return ResultUtil.getSuccessMsg();
    }
    
    /**
     * 批量删除赠品信息
     * 
     * @param request
     * @param model
     * @param prodSku
     * @return
     * @throws Exception
     */
    @RequestMapping(value="deleteProductScore",method=RequestMethod.POST)
	public @ResponseBody String deleteProductScore(@RequestParam(name="pIdArray[]",required=true)Integer pIdArray[] ,HttpServletRequest request) throws Exception{
		for (int i = 0; i < pIdArray.length; i++) {
			Integer sId = pIdArray[i];
			SkuScore skus = skuScoreService.select("select ss from SkuScore as ss where ss.id = ?", sId);
			Integer productId=skus.getProductId();
			List<SkuScore> list = skuScoreService.list("select ss from SkuScore as ss where ss.productId = ?", productId);
			String hql="delete SkuScore as ss where ss.id=?";
			
			skuScoreService.execute(hql, sId);//删除货品信息
			skuAttrService.execute("delete SkuAttr where skuId=?",sId);//删除货品规格信息
			if(null!=list && list.size()==1){
				productScoreService.deleteAll(productId);//删除商品信息
			}
		}
		//保存日志,HttpServletRequest request
   		LoginUser userif= UserSession.getLoginUser(request);
		Operator operator = operatorService.findUserByUsername(userif.getUsername());
		operatorLogService.addUserLog(operator, request, "批量删除赠品id:"+org.apache.commons.lang.StringUtils.join(pIdArray,","));
    	return ResultUtil.getSuccessMsg();
		
	}
    
    
    /**
     * 批量上架赠品信息
     * 
     * @param request
     * @param model
     * @param prodSku
     * @return
     * @throws Exception
     */
    @RequestMapping(value="soldUpProductScore",method=RequestMethod.POST)
	public @ResponseBody String soldUpProductScore(@RequestParam(name="pIdArray[]",required=true)Integer pIdArray[],HttpServletRequest request ) throws Exception{
		for (int i = 0; i < pIdArray.length; i++) {
			Integer sId = pIdArray[i];
			String hql="update SkuScore set flag=1 where id=?";
			skuScoreService.execute(hql, sId);
			
			SkuScore skus = skuScoreService.select("select ss from SkuScore as ss where ss.id = ?", sId);
			Integer productId=skus.getProductId();
			String hql2="update ProductScore set flag=1 where id=?";
			productScoreService.execute(hql2, productId);
		}
		//保存日志,HttpServletRequest request
   		LoginUser userif= UserSession.getLoginUser(request);
		Operator operator = operatorService.findUserByUsername(userif.getUsername());
		operatorLogService.addUserLog(operator, request, "批量上架赠品id:"+org.apache.commons.lang.StringUtils.join(pIdArray,","));
		return ResultUtil.getSuccessMsg();
	}
    
    
    /**
     * 批量下架赠品信息
     * 
     * @param request
     * @param model
     * @param prodSku
     * @return
     * @throws Exception
     */
    @RequestMapping(value="soldOutProductScore",method=RequestMethod.POST)
	public @ResponseBody String soldOutProductScore(@RequestParam(name="pIdArray[]",required=true)Integer pIdArray[] ,HttpServletRequest request) throws Exception{
		for (int i = 0; i < pIdArray.length; i++) {
			Integer sId = pIdArray[i];
			String hql="update SkuScore set flag=0 where id=?";
			skuScoreService.execute(hql, sId);
			
			SkuScore skus = skuScoreService.select("select ss from SkuScore as ss where ss.id = ?", sId);
			Integer productId=skus.getProductId();
			List<SkuScore> list = skuScoreService.list("select ss from SkuScore as ss where ss.productId = ? and ss.flag=1", productId);
			if(null!=list && list.size()==0){
				String hql2="update ProductScore set flag=0 where id=?";
				productScoreService.execute(hql2, productId);
			}
		}
		//保存日志,HttpServletRequest request
   		LoginUser userif= UserSession.getLoginUser(request);
		Operator operator = operatorService.findUserByUsername(userif.getUsername());
		operatorLogService.addUserLog(operator, request, "批量下架赠品id:"+org.apache.commons.lang.StringUtils.join(pIdArray,","));
		return ResultUtil.getSuccessMsg();
	}
    
    
    /**
     * 去编辑页面
     * 
     * @param request
     * @param model
     * @param id
     * @return
     */
    @RequestMapping(value = "edit",method=RequestMethod.GET)
    public String editGet(HttpServletRequest request,Model model,Integer psid,Integer ssid,String resultUrl) {
    	
		ProductScore prod = productScoreService.get(psid);		
    	Category firstCate = categoryService.get(prod.getCategoryId()); 
    	model.addAttribute("firstCate", firstCate); //获取父分类
    	
    	List<Category> categ3= categoryService.list(" from Category where pid = ? ",firstCate.getPid());
    	model.addAttribute("categ3", categ3);//三级目录
    	
    	if(!StringUtils.isEmpty(firstCate.getPid())){ //如何pid 不为空
    		
    		Category secondCate=categoryService.get(firstCate.getPid());//拿二级目录
    		model.addAttribute("secondCate", secondCate) ;
    		List<Category> categ2 = categoryService.list(" from Category where( pid = ? or level = ?)",firstCate.getPid(),secondCate.getLevel()); //二级全部目录
    		model.addAttribute("categ2", categ2);
    		
    		if(!StringUtils.isEmpty(secondCate.getPid())&& firstCate.getLevel() == 3){
    			
    			Category thirdCate=categoryService.get(firstCate.getPid());
    			model.addAttribute("thirdCate", thirdCate);
    			
				List<Category> categ1 = categoryService.list(" from Category where level = 1   ");
				model.addAttribute("categ1", categ1);
    		} 
    		
    	}
    	
		initDate(model,prod.getCategoryId());
		
		List<Category> categoryList = categoryService.list();
    	model.addAttribute("categoryList", categoryList);
    	
		if(!StringUtils.isEmpty(prod.getLables())){
			List<String> lsLables = Arrays.asList(prod.getLables().split(","));
			model.addAttribute("lsLables",lsLables);
		}
		ProductInfo prodInfo = productInfoService.get(psid);
		List<ProductImage> lsProdImages = productImageService.list("from ProductImage where productId = ?", psid);
		String hql = "select sat from SkuAttr sat , SkuScore sku where sku.status = 1 and sku.productId = ? and sat.skuId = sku.id " ;
		Set<String> setAttrValue = new HashSet<String>();
		List<SkuAttr> selectedValues = skuAttrService.list(hql, psid)  ;
		
		Map<Integer,String> setAttrImageMap = new HashMap<Integer,String> ();
		for(SkuAttr skuAttr : selectedValues) {
			setAttrValue.add(String.valueOf(skuAttr.getAttrValueId()));
			setAttrImageMap.put(skuAttr.getAttrValueId(), skuAttr.getAttrImage());
		}
    	
		if(!model.containsAttribute("attrList")) {
			
    		Map<Attr, List<AttrValue>>  attrValueMap = new HashMap<>();
    		
    		String aql = " select distinct a from Attr a ,SkuAttr sa ,SkuScore s  where s.status=1 and s.flag=1 and a.id=sa.attrId and sa.skuId = s.id and s.productId="+psid ;
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
    	
		List<ProductParam> ppms = ppmService.list(" from ProductParam where productId=?", psid) ;
		model.addAttribute("resultUrl", resultUrl);
    	model.addAttribute("prod",prod);
    	model.addAttribute("ppms",ppms);
    	model.addAttribute("prodInfo",prodInfo);
    	model.addAttribute("lsProdImages", lsProdImages);
    	model.addAttribute("setAttrImageMap", setAttrImageMap);
    	model.addAttribute("skuScoreId", ssid);
		return "productScore/productEdit";
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
    public String editPost(HttpServletRequest request, ProdSkuScore prodSku) throws Exception {
    	try {
    		productScoreService.editProdSku(prodSku);
		} catch (CheckException e) {
			return ResultUtil.getError(e.getMessage());
		}
    	//保存日志,HttpServletRequest request
   		LoginUser userif= UserSession.getLoginUser(request);
		Operator operator = operatorService.findUserByUsername(userif.getUsername());
		operatorLogService.addUserLog(operator, request, "修改保存赠品信息:"+prodSku.getProd().getName());
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
        	model.addAttribute("attrValueMap", attrValueMap);
    	}
    	
		List<Brand> brandList = brandService.list();
    	model.addAttribute("brandList", brandList);
     
    	List<Unit> unitList = unitService.list();
    	model.addAttribute("unitList", unitList);
    	 
    	List<ProductLabel> pLabelList = productLabelService.list(" from ProductLabel where status= 1") ;
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
    public String sku(Model model,String selectedValues,Integer productId,BigDecimal salesPrice,BigDecimal volume,BigDecimal weight,String selectedImage) {
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
    	List<SkuScore> skus = new ArrayList<SkuScore>();
    	for (int i=0;i<result.size();i++) {
    		String attrValues = result.get(i);
    		SkuScore sku = skuScoreService.select("from SkuScore where productId = ? and attrValues = ?", productId, attrValues);
    		if(null == sku) {
    			sku = new SkuScore();
    			sku.setAttrValues(attrValues);
    			sku.setSkuPrice(salesPrice);
    			sku.setVolume(volume);
    			sku.setTranslateRate(1);
    			sku.setWeight(weight);
    			sku.setScore(2);
    		}
    		skus.add(sku);
    	}
    	
    	List<Unit> unitList = unitService.list();
    	
    	model.addAttribute("jsonImageArray",  JSON.parseArray(selectedImage));
    	model.addAttribute("unitList", unitList);
    	model.addAttribute("attrList", attrList);
    	model.addAttribute("attrValueList", attrValueList);
    	model.addAttribute("attrValueMap", attrValueMap);
    	model.addAttribute("skus", skus);
    	return "productScore/productSku";
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
    	ProductScore prod = productScoreService.get(productId);
    	List<SkuScore> skus = skuScoreService.list("from SkuScore where productId = ?  and status = 1 ", productId);
    	List<AttrValue> attrValueList = attrValueService.list("from AttrValue where status =1 order by attrId") ;
    	
    	model.addAttribute("skus", skus);
    	model.addAttribute("prod", prod);
    	model.addAttribute("attrList", attrList);
    	model.addAttribute("attrValueList", attrValueList);
    	return "productScore/skuList";
    }
    
    @RequestMapping(value = "noSku",method=RequestMethod.GET)
    public String noSku(Model model,Integer productId,BigDecimal salesPrice,BigDecimal volume,BigDecimal weight,Integer skuScoreId) {
    	SkuScore sku = null; 
    	List<SkuScore> skus = new ArrayList<SkuScore>();
    	if(null!=productId && null !=skuScoreId){
    	  sku = skuScoreService.select("from SkuScore where productId = ? and id = ?", productId,skuScoreId);
    	}
    	if(null == sku) {
			sku = new SkuScore();
			sku.setAttrValues("0");
			sku.setSkuPrice(salesPrice);
			sku.setVolume(volume);
			sku.setWeight(weight);
			sku.setTranslateRate(1);
			sku.setScore(2);
		}
    	skus.add(sku);
		List<Unit> unitList = unitService.list();
    	model.addAttribute("unitList", unitList);
    	model.addAttribute("skus", skus);
    	return "productScore/productNoSku";
    }
    
	/**
	 * SKU上下架切换
	 * 
	 * @param request
	 * @param id
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value = "/switchSkuFlag")
	@ResponseBody
	public String switchSkuFlag(HttpServletRequest request, Integer id) throws Exception {
		SkuScore sku = skuScoreService.get(id);
		sku.setFlag(!sku.getFlag());
		skuScoreService.save(sku);
		//保存日志,HttpServletRequest request
   		LoginUser userif= UserSession.getLoginUser(request);
		Operator operator = operatorService.findUserByUsername(userif.getUsername());
		operatorLogService.addUserLog(operator, request, "修改保存赠品SKU上下架切换");
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
    	Page<ProductScore> pageProduct = new Page<ProductScore>();
    	pageProduct.setPageNo(pageNo);
    	pageProduct = productScoreService.page(pageProduct, "from ProductScore");
    	ProductScore mainProd = productScoreService.get(mainProdId);
    	model.addAttribute("pageProduct", pageProduct);
    	model.addAttribute("mainProd", mainProd);
		return "productScore/productRelatedAdd";
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
    	Page<ProductScore> pageProduct = new Page<ProductScore>();
    	pageProduct.setPageNo(pageNo);
    	String hql = " from ProductScore p where 1 = 1 ";
    	List<Object> params = new ArrayList<>();
    	if(!StringUtils.isEmpty(codeOrName)){
    		hql = hql + " and p.searchWords like ? ";
    		params.add('%'+codeOrName+'%');
    	}
    	pageProduct = productScoreService.page(pageProduct, hql, params.toArray());
    	ProductScore mainProd = productScoreService.get(mainProdId);
    	model.addAttribute("pageProduct", pageProduct);
    	model.addAttribute("mainProd", mainProd);
    	return "productScore/searchResults";
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
    	return "redirect:"+pcSite+"/productScore/"+id;
    }
    
    /**
     * 批量导入页面
     * 
     * @return
     */
    @RequestMapping(value="betch",method=RequestMethod.GET)
    public String betchGet() {
    	return "productScore/betch" ;
    }
    
    
    @RequestMapping(value="betch",method=RequestMethod.POST)
    public  String betchPost(HttpServletRequest request,Model model) throws Exception {
    	MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		MultipartFile file = multipartRequest.getFile("file");
		List<ProdExcelModel> result =ProductExcel.getExcel(file.getInputStream()) ;
		Map<String,List<ProdExcelModel>> productMap = ProductExcel.importData(result);
		model.addAttribute("productMap", productMap);
		//保存日志,HttpServletRequest request
   		LoginUser userif= UserSession.getLoginUser(request);
		Operator operator = operatorService.findUserByUsername(userif.getUsername());
		operatorLogService.addUserLog(operator, request, "批量导入");
    	return "/productScore/productImportTable";
    }
    
    /**
     * 货品提示
     * @param product
     * @param pageNo
     * @return
     */
    @RequestMapping(value="getProduct")
    public @ResponseBody String getProduct(String product,@RequestParam(name="pageNo",defaultValue="1")Integer pageNo){
		Page<ProductScore> productPage = new Page<ProductScore>();
		productPage.setPageNo(pageNo);
		productPage = productScoreService.page(productPage," from ProductScore  where (name like ? or code like ?)","%"+product+"%","%"+product+"%");
    	return JSON.toJSONString(productPage);
    }
    
}
