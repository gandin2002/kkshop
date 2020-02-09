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

import com.alibaba.fastjson.annotation.JSONField;

import cn.bohoon.framework.SpringContextHolder;
import cn.bohoon.product.service.BrandService;
import cn.bohoon.product.service.CategoryService;
import cn.bohoon.product.service.ProductImageService;
import cn.bohoon.product.service.ProductInfoService;
import cn.bohoon.product.service.UnitService;
import cn.bohoon.quotation.domain.QuotationState;
import cn.bohoon.quotation.entity.QuotationItem;
import cn.bohoon.quotation.service.QuotationItemService;
import cn.bohoon.userInfo.domain.LoginUser;
import cn.bohoon.userInfo.entity.UserInfo;
import cn.bohoon.userInfo.service.UserInfoService;
import cn.bohoon.util.UserSession;

/**
 * 赠品信息
 */
@Entity
@Table(name = "t_product_score")
public class ProductScore {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Integer id; // id
	String code; // 商品编码
	String erpCode; // ERP-SKU编码
	String name; // 商品名称
	Integer brandId; // 品牌Id
	String brandName; // 品牌Id
	String categoryId; // 商品类别
	String categoryName; // 商品类别
	Integer pId = 0 ;

	String lables; // 商品标签(辅助分类)
	BigDecimal salesPrice; // 销售价格
	BigDecimal displayPrice; // 显示价格

	@Column(columnDefinition = "int default 0")
	Integer salesNum = 0; // 销售量
	BigDecimal weight; // 重量
	BigDecimal volume; // 体积
	Integer unitId; // 基本单位
	String unitName; // 基本单位
	Integer startNum = 0; // 起订量

	@Column(columnDefinition = "tinyint(2)")
	Boolean recommend; // 是否是推荐商品 0-否, 1-是
	@Column(columnDefinition = "tinyint(2)")
	Boolean flag; // 商品上下架 0 -下架 1 - 上架
	String searchWords; // 搜索词
	String sellPoint; // 商品卖点
	Integer inventoryHint;// 库存预警量
	@Column(columnDefinition = "datetime")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	Date createDate = new Date();// 创建时间
	Integer tax;// 税码值
	Integer taxId;// 税码ID

	@Transient
	QuotationItem quotationItem;
	
	

	public ProductScore() {
		super();
	}
	
	public ProductScore(Product pro) {
		super();
		this.code = pro.getCode();
		this.erpCode = pro.getErpCode();
		this.name = pro.getName();
		this.brandId = pro.getBrandId();
		this.brandName = pro.getBrandName();
		this.categoryId = pro.getCategoryId();
		this.categoryName = pro.getCategoryName();
		this.pId = pro.getId() ;
		this.lables = pro.getLables();
		this.salesPrice = pro.getSalesPrice();
		this.displayPrice = pro.getDisplayPrice();
		this.salesNum = pro.getSalesNum();
		this.weight = pro.getWeight();
		this.volume = pro.getVolume();
		this.unitId = pro.getUnitId();
		this.unitName = pro.getUnitName();
		this.startNum = pro.getStartNum() ;
		this.recommend = pro.getRecommend() ;
		this.flag = pro.getFlag() ;
		this.searchWords = pro.getSearchWords() ;
		this.sellPoint = pro.getSellPoint() ;
		this.inventoryHint = pro.getInventoryHint() ;
		this.createDate = new Date() ;
		this.tax = pro.getTax();
		this.taxId = pro.getTaxId() ;
	}
	
	public ProductScore(Integer id, String code, String erpCode, String name, Integer brandId, String brandName, String categoryId, String categoryName, Integer pId, String lables, BigDecimal salesPrice,
			BigDecimal displayPrice, Integer salesNum, BigDecimal weight, BigDecimal volume, Integer unitId, String unitName, Integer startNum, Boolean recommend, Boolean flag, String searchWords,
			String sellPoint, Integer inventoryHint, Date createDate, Integer tax, Integer taxId) {
		super();
		this.id = id;
		this.code = code;
		this.erpCode = erpCode;
		this.name = name;
		this.brandId = brandId;
		this.brandName = brandName;
		this.categoryId = categoryId;
		this.categoryName = categoryName;
		this.pId = pId;
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
		this.tax = tax;
		this.taxId = taxId;
	}

	@JSONField(serialize = false)
	@Transient
	public Category getCategory() {
		CategoryService service = SpringContextHolder.getBean(CategoryService.class);
		if (null != categoryId) {
			return service.get(categoryId);
		}
		return null;
	}

	@Transient
	public Brand getBrand() {
		BrandService service = SpringContextHolder.getBean(BrandService.class);
		if (null != brandId) {
			return service.get(brandId);
		}
		return null;
	}

	@Transient
	public Unit getUnit() {
		UnitService service = SpringContextHolder.getBean(UnitService.class);
		if (null != unitId) {
			return service.get(unitId);
		}
		return null;
	}

	@Transient
	public List<ProductImage> getProductImageList() {
		ProductImageService productImageService = SpringContextHolder.getBean(ProductImageService.class);
		return productImageService.getProductImage(id);
	}

	@JSONField(serialize = false)
	@Transient
	public ProductImage getProductImage() {
		List<ProductImage> list = getProductImageList();
		if (list.isEmpty()) {
			return null;
		}
		return list.get(0);
	}

	@Transient
	public ProductInfo getProductInfo() {
		ProductInfoService service = SpringContextHolder.getBean(ProductInfoService.class);
		return service.get(id);
	}

	@JSONField(serialize = false)
	@Transient
	public BigDecimal getVipPrice() {
		if (null != getQuotationItem(null)) {
			this.salesPrice = quotationItem.getQuotationPrice();
		}
		return salesPrice;
	}

	@JSONField(serialize = false)
	@Transient
	public BigDecimal getVipPrice(String userId) {
		if (null != getQuotationItem(userId)) {
			this.salesPrice = quotationItem.getQuotationPrice();
		}
		return salesPrice;
	}

	@JSONField(serialize = false)
	@Transient
	public QuotationItem getQuotationItem(String userId) {
		if (null != quotationItem) {
			return quotationItem;
		}
		LoginUser loginUser = UserSession.getLoginUser();
		if (!StringUtils.isEmpty(loginUser)) {
			userId = loginUser.getUserId();
		}
		if (StringUtils.isEmpty(userId)) {
			return quotationItem;
		}

		QuotationItemService service = SpringContextHolder.getBean(QuotationItemService.class);
		UserInfoService userInfoService = SpringContextHolder.getBean(UserInfoService.class);
		UserInfo userInfo = userInfoService.queryRespUser(userId);
		if (null == userInfo) {
			return quotationItem;
		}
		String hql = " from QuotationItem where userInfoId=? and productId=? and quotationState=? and ValidDate > ? order by ValidDate,id desc";
		List<Object> params = new ArrayList<>();

		params.add(userInfo.getId());
		params.add(id);
		params.add(QuotationState.PASS_QUOTATION);
		params.add(new Date());
		List<QuotationItem> items = service.list(hql, params.toArray());
		if (items.size() > 0) {
			this.quotationItem = items.get(0);
		}

		return quotationItem;
	}
	
	public Integer getpId() {
		return pId;
	}

	public void setpId(Integer pId) {
		this.pId = pId;
	}

	@Transient
	public BigDecimal getVipShowPrice() {
		if (getVipPrice().equals(salesPrice)) {
			return displayPrice;
		}
		return salesPrice;
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
}