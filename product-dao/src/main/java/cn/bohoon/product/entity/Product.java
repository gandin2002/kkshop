package cn.bohoon.product.entity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;

import cn.bohoon.basicSetup.domain.SysParamType;
import cn.bohoon.basicSetup.service.SysParamService;
import cn.bohoon.company.entity.Company;
import cn.bohoon.company.service.CompanyService;
import cn.bohoon.framework.SpringContextHolder;
import cn.bohoon.page.entity.PageNavigation;
import cn.bohoon.page.service.PageNavigationService;
import cn.bohoon.product.domain.PriceCountUtil;
import cn.bohoon.product.service.BrandService;
import cn.bohoon.product.service.CategoryService;
import cn.bohoon.product.service.ProductImageService;
import cn.bohoon.product.service.ProductInfoService;
import cn.bohoon.product.service.ProductParamService;
import cn.bohoon.product.service.SkuService;
import cn.bohoon.product.service.SkuWareLocationService;
import cn.bohoon.product.service.UnitService;
import cn.bohoon.quotation.entity.QuotationItem;
import cn.bohoon.userInfo.domain.LoginUser;
import cn.bohoon.userInfo.entity.UserInfo;
import cn.bohoon.userInfo.service.UserInfoService;
import cn.bohoon.util.UserSession;

/**
 * 商品信息
 */
@Entity
@Table(name = "t_product")
public class Product {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id; // id
	private String code; // 商品编码
	private String erpCode; // ERP-SKU编码
	private String name; // 商品名称
	private Integer brandId; // 品牌Id
	private String brandName; // 品牌名称
	private String categoryId; // 商品类别
	private String categoryName; // 商品类别
	
	private String lables; // 商品标签(辅助分类)
	private  Integer  days;  //当标签为新品时指定
	private  Integer   presaleNums; // 销售量
	private BigDecimal salesPrice; // 销售价格
	private BigDecimal displayPrice; // 显示价格
	private  String  salesMode;//销售方式
	@Column(columnDefinition = "int default 0")
	private Integer salesNum = 0 ; // 销售量
	private BigDecimal weight; // 重量
	private BigDecimal volume; // 体积
	private Integer unitId; // 基本单位
	private String unitName; // 基本单位
	private String erpUnitCode;//单位erpCode
	private Integer startNum = 0; // 起订量
	private String forShort="";//简称
	@Column(columnDefinition="tinyint(2)")
	private Boolean recommend; // 是否是推荐商品 0-否, 1-是
	@Column(columnDefinition="tinyint(2)")
	private Boolean flag; // 商品上下架 0 -下架 1 - 上架
	private String searchWords; // 搜索词
	private String sellPoint ; //商品卖点
	private Integer inventoryHint=0;//库存预警量
	@Column(columnDefinition = "datetime")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date createDate = new Date();//创建时间
	private Integer tax;//税码值
	private Integer taxId;//税码ID
	private String taxCode;//税码ID
	
	
	//加
	private Integer showSort;  //首页可按照此字段排序
	
	private String serveId = ""; //服务ID
	
	
	// 获取商品类别名称
	@Transient
	public String getCategoryNames(){
		CategoryService categoryService = SpringContextHolder.getBean(CategoryService.class);
		Category category = categoryService.get(getCategoryId());
		return category.getName();
		
	}
	
	//获得菜单分级	
    @Transient
    public List<PageNavigation>  getPageNavigation(){
    	List<PageNavigation> list=new ArrayList<PageNavigation>();
    	PageNavigationService pageNavigationService=SpringContextHolder.getBean(PageNavigationService.class);
    	List <PageNavigation> pageNavigationList=pageNavigationService.list(); ;
    	for (PageNavigation pageNavigation : pageNavigationList) {
    		 JSONObject json_test = JSONObject.parseObject(pageNavigation.getContent()) ;
    		 JSONArray jsonArray=json_test.getJSONArray("category");
    		 if (jsonArray==null) {
				continue;
    		 }
    		 for (Object object : jsonArray) {
				if (object.equals(this.getCategoryId())) {
					list.add(pageNavigation) ;
				}
			}
		}
    	
    	return list;
    }
    
    
    
    @Transient
    public List<PageNavigation>  getPageNavigations(){
    	List<PageNavigation> list=new ArrayList<PageNavigation>();
    	PageNavigationService pageNavigationService=SpringContextHolder.getBean(PageNavigationService.class);
    	List <PageNavigation> pageNavigationList=pageNavigationService.list( " from PageNavigation where level = 3");
    	for (PageNavigation pageNavigation : pageNavigationList) {
    			if(pageNavigation.getContent().contains(this.categoryId)){
    				while (pageNavigation != null) {
    					list.add(pageNavigation);
    					pageNavigation = pageNavigationService.get(pageNavigation.getPid());
    				}
    				return list;
    			}
		}
    	return list;
    }
    
    @Transient
    public List<PageNavigation>  getPageNavigation2s(){
    	List<PageNavigation> list=new ArrayList<PageNavigation>();
    	PageNavigationService pageNavigationService=SpringContextHolder.getBean(PageNavigationService.class);
    	List <PageNavigation> pageNavigationList=pageNavigationService.list( " from PageNavigation where level = 2");
    	for (PageNavigation pageNavigation : pageNavigationList) {
    			if(pageNavigation.getContent().contains(this.categoryId)){
    				while (pageNavigation != null) {
    					list.add(pageNavigation);
    					pageNavigation = pageNavigationService.get(pageNavigation.getPid());
    				}
    				return list;
    			}
		}
    	return list;
    }
	
	public String getForShort() {
		return forShort;
	}

	public void setForShort(String forShort) {
		this.forShort = forShort;
	}

	public Integer getShowSort() {
		return showSort;
	}
	public void setShowSort(Integer showSort) {
		this.showSort = showSort;
	}
	public Integer getDays() {
		return days;
	}
	public void setDays(Integer days) {
		this.days = days;
	}
	public Integer getPresaleNums() {
		return presaleNums;
	}
	public void setPresaleNums(Integer presaleNums) {
		this.presaleNums = presaleNums;
	}
	@Transient
	@JSONField(serialize=false)
	public SkuWareLocation getSkuWareLocation(){  
		SkuWareLocationService SkuWareLocationService=	SpringContextHolder.getBean(SkuWareLocationService.class);
		SkuService skuService= SpringContextHolder.getBean(SkuService.class);
		if(null !=id ){
		Sku sku=skuService.getSkuByPorductId(id);
			if (sku!=null) {
				SkuWareLocation skuWareLocation=SkuWareLocationService.getSkuWareLocation(sku.getId());
				return skuWareLocation;	
			}
	
		}
		return null;
	}
	@Transient
	@JSONField(serialize=false)
	QuotationItem quotationItem ;

	@JSONField(serialize=false)
	@Transient
	public Category getCategory() {
		CategoryService service = SpringContextHolder.getBean(CategoryService.class);
		if(null != categoryId) {
			return service.get(categoryId);
		}
		return null ;
	}
	@Transient
	public ProductParam getProductParam(){
		ProductParamService service=SpringContextHolder.getBean(ProductParamService.class);
		if(null !=id ){
			return service.getProductParam(id);
		}
		return null;
	}
	@Transient
	@JSONField(serialize=false)
	public Brand getBrand() {
		BrandService service = SpringContextHolder.getBean(BrandService.class);
		if(null != brandId) {
			return service.get(brandId);
		}
		return null ;
	}

	@Transient
	@JSONField(serialize=false)
	public Unit getUnit() {
		UnitService service = SpringContextHolder.getBean(UnitService.class);
		if(null != unitId) {
			return service.get(unitId);
		}
		return null ;
	}

	@Transient
	@JSONField(serialize=false)
	public List<ProductImage> getProductImageList() {
		ProductImageService productImageService = SpringContextHolder.getBean(ProductImageService.class);
		return productImageService.getProductImage(id);
	}

	@JSONField(serialize=false)
	@Transient
	public ProductImage getProductImage() {
		List<ProductImage> list = getProductImageList();
		if (list.isEmpty()) {
			return null;
		}
		return list.get(0);
	}

	@JSONField(serialize=false)
	@Transient
	public ProductInfo getProductInfo() {
		ProductInfoService service = SpringContextHolder.getBean(ProductInfoService.class);
		return service.get(id);
	}


	
	@JSONField(serialize=false)
	@Transient
	public BigDecimal getCompanyVipPrice(String companyId) {
		
		CompanyService companyService = SpringContextHolder.getBean(CompanyService.class) ;
		UserInfoService userInfoService = SpringContextHolder.getBean(UserInfoService.class) ;
		if (!StringUtils.isEmpty( companyId.trim() )){
			
			Company company = companyService.get(companyId);
			UserInfo userInfo = userInfoService.list("from UserInfo where id=?", company.getUserId()).get(0);
			if (userInfo != null){
				return  getVipShowPrice(userInfo.getId());
			}
		}		
			return    getVipShowPrice();
	}
 
	@JSONField(serialize=false)
	@Transient
	public BigDecimal getVipPrice(String userId) {
		Sku sku = PriceCountUtil.getSKu(this);
		BigDecimal price = PriceCountUtil.getPrice(userId, sku);
		return  price;
	}
	
	@JSONField(serialize=false)
	@Transient
	public BigDecimal getVipPrice() {
		LoginUser loginUser = UserSession.getLoginUser();
		String userId  = "";
		if(loginUser != null){
			  userId = loginUser.getUserId();
		}
		Sku sku = PriceCountUtil.getSKu(this);
		BigDecimal price = PriceCountUtil.getPrice(userId, sku);
		return  price;
	}
		
	@JSONField(serialize=false)
	@Transient
	public BigDecimal getVipShowPrice() {
		LoginUser loginUser = UserSession.getLoginUser();
		String userId  = "";
		if(loginUser != null){
			  userId = loginUser.getUserId();
		}
		BigDecimal price = PriceCountUtil.getVipPrice(userId,this);
		return price ;
	}
	
	@JSONField(serialize=false)
	@Transient
	public BigDecimal getVipShowPrice(String userId) {
		BigDecimal price = PriceCountUtil.getVipPrice(userId,this);
		if(price == null) {
			return displayPrice ;
		}
		return price ;
	}
	/**
	 * 市场价格
	 * @return
	 */
	@JSONField(serialize=false)
	@Transient
	public BigDecimal getSkuPrice(){
		Sku sku = PriceCountUtil.getSKu(this);
		if(sku == null){
			return this.salesPrice;
		}
		if(sku.getPrice() == null){
			return sku.getSkuPrice();
		}
		return sku.getPrice();
	}
	
	@JSONField(serialize=false)
	@Transient
	public BigDecimal getQuotationItemPrice(){
		LoginUser loginUser = UserSession.getLoginUser();
		String userId  = "";
		if(loginUser != null){
			  userId = loginUser.getUserId();
		}
		BigDecimal price = PriceCountUtil.getQuotationItemPrice(userId,this);
		return price;
	}
	
	@JSONField(serialize=false)
	@Transient
	public Sku getSku(){
		return PriceCountUtil.getSKu(this);
	}
	
	public Product(){
		super();
	}
	
	public Product(ProductScore productScore) {
		this.code = productScore.getCode();
		this.erpCode = productScore.getErpCode();
		this.name = productScore.getName();
		this.brandId = productScore.getBrandId();
		this.brandName = productScore.getBrandName();
		this.categoryId = productScore.getCategoryId();
		this.categoryName = productScore.getCategoryName();
		this.lables = productScore.getLables();
		this.salesPrice = productScore.getSalesPrice();
		this.displayPrice = productScore.getDisplayPrice();
		this.salesNum = productScore.getSalesNum();
		this.weight = productScore.getWeight();
		this.volume = productScore.getVolume();
		this.unitId = productScore.getUnitId();
		this.unitName = productScore.getUnitName();
		this.startNum = productScore.getStartNum();
		this.recommend = productScore.getRecommend();
		this.flag = productScore.getFlag();
		this.searchWords = productScore.getSearchWords();
		this.sellPoint = productScore.getSellPoint();
		this.inventoryHint = productScore.getInventoryHint();
		this.createDate = productScore.getCreateDate();
	}
	
	public Product(Integer id, String code, String erpCode, String name, Integer brandId, String brandName,
			String categoryId, String categoryName, String lables, BigDecimal salesPrice, BigDecimal displayPrice,
			Integer salesNum, BigDecimal weight, BigDecimal volume, Integer unitId, String unitName, Integer startNum,
			Boolean recommend, Boolean flag, String searchWords, String sellPoint, Integer inventoryHint,
			Date createDate, QuotationItem quotationItem) {
		this.id = id;
		this.code = code;
		this.erpCode = erpCode;
		this.name = name;
		this.brandId = brandId;
		this.brandName = brandName;
		this.categoryId = categoryId;
		this.categoryName = categoryName;
		this.lables = lables;
		this.salesPrice = salesPrice;
		this.displayPrice = displayPrice;
		this.salesNum = salesNum;
		this.weight = weight;
		this.volume = volume;
		this.unitId = unitId;
		this.unitName = unitName;
		this.startNum = startNum;
		this.recommend = recommend;
		this.flag = flag;
		this.searchWords = searchWords;
		this.sellPoint = sellPoint;
		this.inventoryHint = inventoryHint;
		this.createDate = createDate;
		this.quotationItem = quotationItem;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getErpCode() {
		return erpCode;
	}

	public void setErpCode(String erpCode) {
		this.erpCode = erpCode;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getBrandId() {
		return brandId;
	}

	public void setBrandId(Integer brandId) {
		this.brandId = brandId;
	}

	public String getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}

	public String getLables() {
		return lables;
	}

	public void setLables(String lables) {
		this.lables = lables;
	}

	public BigDecimal getSalesPrice() {
		return salesPrice;
	}

	public void setSalesPrice(BigDecimal salesPrice) {
		this.salesPrice = salesPrice;
	}

	public BigDecimal getDisplayPrice() {
		return displayPrice;
	}

	public void setDisplayPrice(BigDecimal displayPrice) {
		this.displayPrice = displayPrice;
	}

	public Integer getSalesNum() {
		return salesNum;
	}

	public void setSalesNum(Integer salesNum) {
		this.salesNum = salesNum;
	}

	public BigDecimal getWeight() {
		return weight;
	}

	public String getSalesMode() {
		return salesMode;
	}
	public void setSalesMode(String salesMode) {
		this.salesMode = salesMode;
	}
	public void setWeight(BigDecimal weight) {
		this.weight = weight;
	}

	public BigDecimal getVolume() {
		return volume;
	}

	public void setVolume(BigDecimal volume) {
		this.volume = volume;
	}

	public Integer getUnitId() {
		return unitId;
	}

	public void setUnitId(Integer unitId) {
		this.unitId = unitId;
	}

	public Boolean getRecommend() {
		return recommend;
	}

	public void setRecommend(Boolean recommend) {
		this.recommend = recommend;
	}

	public Boolean getFlag() {
		return flag;
	}

	public void setFlag(Boolean flag) {
		this.flag = flag;
	}

	public Integer getStartNum() {
		return startNum;
	}

	public void setStartNum(Integer startNum) {
		this.startNum = startNum;
	}

	public String getBrandName() {
		return brandName;
	}

	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public String getUnitName() {
		return unitName;
	}

	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}

	public String getSearchWords() {
		return searchWords;
	}

	public void setSearchWords(String searchWords) {
		this.searchWords = searchWords;
	}

	public String getSellPoint() {
		return sellPoint;
	}

	public void setSellPoint(String sellPoint) {
		this.sellPoint = sellPoint;
	}

	public Integer getInventoryHint() {
		if(this.inventoryHint==null){
			this.inventoryHint=0;
		}
		try {
			if(inventoryHint==0){
				SysParamService sysParamService = SpringContextHolder.getBean(SysParamService.class) ;
				if(sysParamService!=null){
					String value= sysParamService.findParam("INVENTORY_DEFAULT_WARNING", SysParamType.PRODUCT_PARAM).getValue();
					inventoryHint=Integer.parseInt(value);
					return inventoryHint;
				}
		} 
		}catch (Exception e) {
			System.out.println("------------------------");
			System.out.println("库存预警量参数值输入不正确");
			return 0;
		}
		return inventoryHint;
		}
		

	public void setInventoryHint(Integer inventoryHint) {
		this.inventoryHint = inventoryHint;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Integer getTax() {
		return tax;
	}

	public void setTax(Integer tax) {
		this.tax = tax;
	}

	public Integer getTaxId() {
		return taxId;
	}

	public void setTaxId(Integer taxId) {
		this.taxId = taxId;
	}

	public String getServeId() {
		return serveId;
	}

	public void setServeId(String serveId) {
		this.serveId = serveId;
	}

	public String getTaxCode() {
		return taxCode;
	}

	public void setTaxCode(String taxCode) {
		this.taxCode = taxCode;
	}

	public String getErpUnitCode() {
		return erpUnitCode;
	}

	public void setErpUnitCode(String erpUnitCode) {
		this.erpUnitCode = erpUnitCode;
	}
}