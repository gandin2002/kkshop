package cn.bohoon.product.entity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
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

import cn.bohoon.company.entity.Company;
import cn.bohoon.company.service.CompanyService;
import cn.bohoon.framework.SpringContextHolder;
import cn.bohoon.framework.util.DateUtil;
import cn.bohoon.product.domain.PriceCountUtil;
import cn.bohoon.product.service.AttrService;
import cn.bohoon.product.service.AttrValueService;
import cn.bohoon.product.service.ProductScoreService;
import cn.bohoon.product.service.ProductService;
import cn.bohoon.product.service.SkuAttrService;
import cn.bohoon.product.service.UnitService;
import cn.bohoon.userInfo.domain.LoginUser;
import cn.bohoon.userInfo.entity.UserInfo;
import cn.bohoon.userInfo.service.UserInfoService;
import cn.bohoon.util.UserSession;

/** 商品sku */
@Entity
@Table(name = "t_sku")
public class Sku {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id; // 商品skuId
	@JSONField(serialize = false)
	private String code; // 商品Sku编码
	private String name;// 名字+Sku 编码
	/*@JSONField(serialize = false)*/
	private String erpCode; // Sku-ERP编码
	private Integer productId; // 商品Id
	private BigDecimal skuPrice = new BigDecimal(0.00); // 商品sku价格
	private BigDecimal price = new BigDecimal(0.00) ;//标准价格
	
	@JSONField(serialize = false)
	@Column(columnDefinition = "tinyint(2)")
	private Boolean flag; // 商品sku上下架 0 -下架 1 - 上架
	private BigDecimal weight; // 重量
	private BigDecimal volume; // 体积
	private Integer inventory = 0; // 商品sku库存
	private String imageUrl; // 商品sku图片
	private String attrValues; // sku属性值串

	@JSONField(serialize = false)
	@Column(columnDefinition = "tinyint(2)")
	private Boolean status = true; // sku状态 0 - 无效 1 - 有效
	@Column(columnDefinition = "int default 0")
	private Integer salesNum = 0; // 销售量
	Integer score;// 积分百分比

	@JSONField(serialize = false)
	String barCode;// 条形码
	Integer translateRate; // 换算率
	Integer auxUnitId; // 辅助单位(销售单位)
	String auxUnitName; // 辅助单位(销售单位)
	String storageLocation;// 库位

	@Column(columnDefinition = "datetime")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	Date createDate = new Date();// 创建时间

	Integer tax;// 税码值
	Integer taxId;// 税码ID
	String taxCode;
	String erpUnitCode; //erpUnitCode
	
	Integer prompt = 3;   // 货品库存不足，显示提示天数多少天到达,默认3天

	String materialspec = "";  //多规格
	String productType;//商品类型    赠品：score ,非赠品：sell
	
	
	public Integer getPrompt() {
		return prompt;
	}

	public void setPrompt(Integer prompt) {
		this.prompt = prompt;
	}

	@JSONField(serialize = false)
	@Transient
	public String getCreateDateString() {
		return DateUtil.formatDate(this.createDate);
	}

	@JSONField(serialize = false)
	@Transient
	public List<String> getSkuAttr() {
		List<String> list =new ArrayList<String>();
		if(!StringUtils.isEmpty(attrValues)){
			list = Arrays.asList(attrValues.split(","));
		}
		return list;
	}

	@JSONField(serialize = false)
	@Transient
	public Product getProduct() {
		if(null != productType && productType.equals("score")){
			ProductScoreService service = SpringContextHolder.getBean(ProductScoreService.class);
			ProductScore productScore=service.get(productId);
			return new Product(productScore);
		}else{
			ProductService service = SpringContextHolder.getBean(ProductService.class);
			return service.get(productId);
		}
		
	}
	
	/*@JSONField(serialize = false)*/
	@Transient
	public String getImageUrl(){
		
		if(!StringUtils.isEmpty(this.imageUrl)){
			return this.imageUrl;
		}
		SkuAttrService service = SpringContextHolder.getBean(SkuAttrService.class);
		List<SkuAttr> list = service.list(" from SkuAttr where skuId = "+this.id);
		if(!list.isEmpty()){
			String image = list.get(0).getAttrImage();
			return StringUtils.isEmpty(image)? "" :image;
		}
		return "";
	}
	
	@JSONField(serialize = false)
	@Transient
	public Unit getAuxUnit() {
		UnitService service = SpringContextHolder.getBean(UnitService.class);
		if (null != auxUnitId) {
			return service.get(auxUnitId);
		}
		return null;
	}

	@Transient
	public String getAttrAndAttrValues() {
		AttrService attrService = SpringContextHolder.getBean(AttrService.class);
		AttrValueService attrValueService = SpringContextHolder.getBean(AttrValueService.class);
		List<String> skuAttr = getSkuAttr();
		StringBuffer buffer = new StringBuffer();
		for (String skuAttr1 : skuAttr) {
			AttrValue attrValue = attrValueService.get(Integer.parseInt(skuAttr1));
			if (null == attrValue) {
				buffer.append("无SKU规格");
				buffer.append(" ");
				continue;
			}
			Attr attr = attrService.get(attrValue.getAttrId());
			if (null == attr) {
				buffer.append("无SKU规格");
				buffer.append(" ");
				continue;
			}
			buffer.append(attr.getName());
			buffer.append(":");
			buffer.append(attrValue.getName());
			buffer.append(" ");
		}
		return buffer.toString();
	}
	
	
	@Transient
	public String getpublicAttrValue(Integer i){
		

		AttrValueService attrValueService = SpringContextHolder.getBean(AttrValueService.class);
		
		List<String> skuAttr = getSkuAttr();
		
		
		if (null != skuAttr && skuAttr.size() > i){
			
			AttrValue attrValue = attrValueService.get(new Integer(skuAttr.get(i)));
			
			if (null != attrValue){
				
				return attrValue.getName();
				
			}else{
				
				return "无";
			}
			
		}else{
			
			return "无";
		}
		
	}
	
	@Transient
	public String getAttrValue1(){
		
		return getpublicAttrValue(0);
	}
	
	@Transient
	public String getAttrValue2(){
		
		return getpublicAttrValue(1);
	}

	@Transient
	public String getAttrValueNames() {
		AttrService attrService = SpringContextHolder.getBean(AttrService.class);
		AttrValueService attrValueService = SpringContextHolder.getBean(AttrValueService.class);
		List<String> skuAttr = getSkuAttr();
		StringBuffer buffer = new StringBuffer();
		for (String skuAttr1 : skuAttr) {
			AttrValue attrValue = attrValueService.get(Integer.parseInt(skuAttr1));
			if (null == attrValue) {
				buffer.append("无SKU规格");
				buffer.append(" ");
				continue;
			}
			Attr attr = attrService.get(attrValue.getAttrId());
			if (null == attr) {
				buffer.append("无SKU规格");
				buffer.append(" ");
				continue;
			}
			buffer.append(attrValue.getName());
			//buffer.append(";");
		}
		return buffer.toString();
	}
	
	
	@JSONField(serialize = false)
	@Transient
	public BigDecimal getCompanyVipPrice(String companyId) {

		CompanyService companyService = SpringContextHolder.getBean(CompanyService.class);
		UserInfoService userInfoService = SpringContextHolder.getBean(UserInfoService.class);
		if (!StringUtils.isEmpty(companyId.trim())) {

			Company company = companyService.get(companyId);
			UserInfo userInfo = userInfoService.list("from UserInfo where id=?", company.getUserId()).get(0);
			if (userInfo != null) {
				BigDecimal db = getVipPrice(userInfo.getId());
				this.skuPrice = db;
				return db;
			}
		}
		return new BigDecimal(0);

	}
		
		
//----------------价格
	@JSONField(serialize = false)
	@Transient
	public BigDecimal getVipPrice() {
		LoginUser loginUser = UserSession.getLoginUser();
		String userId  = "";
		if(loginUser != null){
			  userId = loginUser.getUserId();
		}
		BigDecimal price = PriceCountUtil.getPrice(userId,this);
		return price;
	}

	@JSONField(serialize = false)
	@Transient
	public BigDecimal getVipPrice(String userId) {
		BigDecimal price = PriceCountUtil.getPrice(userId,this);
		return price;
	}
	
	@JSONField(serialize = false)
	@Transient
	public BigDecimal getVipShowPrice() {
		LoginUser loginUser = UserSession.getLoginUser();
		String userId  = "";
		if(loginUser != null){
			  userId = loginUser.getUserId();
		}
		BigDecimal price = PriceCountUtil.getVipPrice(userId,this);
		return price;
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
	/**
	 * 市场价格
	 * @return
	 */
	@JSONField(serialize=false)
	@Transient
	public BigDecimal getIsPrice(){
		if(this.getPrice() == null){
			return this.getSkuPrice();
		}
		return this.getPrice();
	}
	
	
//价格------------------end
	
	
	public Sku() {
		super();
	}
	
	public Sku(SkuScore sku) {
		super();
		this.productId = sku.getProductId();
		this.code = sku.getCode() ;
		this.name = sku.getName() ;
		this.erpCode = sku.getErpCode() ;
		this.skuPrice = sku.getSkuPrice();
		this.flag = sku.getFlag() ;
		this.weight = sku.getWeight() ;
		this.volume = sku.getVolume() ;
		this.inventory = sku.getInventory() ;
		this.imageUrl = sku.getImageUrl() ;
		this.attrValues = sku.getAttrValues();
		this.status = sku.getStatus();
		this.salesNum = sku.getSalesNum() ;
		this.score = sku.getScore() ;
		this.barCode = sku.getBarCode();
		this.translateRate = sku.getTranslateRate();
		this.auxUnitId = sku.getAuxUnitId();
		this.auxUnitName = sku.getAuxUnitName();
		this.storageLocation = sku.getStorageLocation();
		this.createDate = Calendar.getInstance().getTime() ;
		this.tax = sku.getTax() ;
		this.taxId = sku.getTaxId() ;
		this.productType = sku.getProductType();
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

	public Integer getProductId() {
		return productId;
	}

	public void setProductId(Integer productId) {
		this.productId = productId;
	}

	public BigDecimal getSkuPrice() {
		return skuPrice;
	}

	public void setSkuPrice(BigDecimal skuPrice) {
		this.skuPrice = skuPrice;
	}

	public Boolean getFlag() {
		return flag;
	}

	public void setFlag(Boolean flag) {
		this.flag = flag;
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

	public Integer getInventory() {
		return inventory;
	}

	public void setInventory(Integer inventory) {
		this.inventory = inventory;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public String getAttrValues() {
		return attrValues;
	}

	public void setAttrValues(String attrValues) {
		this.attrValues = attrValues;
	}

	public Boolean getStatus() {
		return status;
	}

	public void setStatus(Boolean status) {
		this.status = status;
	}

	public Integer getSalesNum() {
		return salesNum;
	}

	public void setSalesNum(Integer salesNum) {
		this.salesNum = salesNum;
	}

	public Integer getScore() {
		return score;
	}

	public void setScore(Integer score) {
		this.score = score;
	}

	public String getBarCode() {
		return barCode;
	}

	public void setBarCode(String barCode) {
		this.barCode = barCode;
	}

	public Integer getTranslateRate() {
		return translateRate;
	}

	public void setTranslateRate(Integer translateRate) {
		this.translateRate = translateRate;
	}

	public Integer getAuxUnitId() {
		return auxUnitId;
	}

	public void setAuxUnitId(Integer auxUnitId) {
		this.auxUnitId = auxUnitId;
	}

	public String getAuxUnitName() {
		return auxUnitName;
	}

	public void setAuxUnitName(String auxUnitName) {
		this.auxUnitName = auxUnitName;
	}

	public String getStorageLocation() {
		return storageLocation;
	}

	public void setStorageLocation(String storageLocation) {
		this.storageLocation = storageLocation;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	public String getTaxCode() {
		return taxCode;
	}

	public void setTaxCode(String taxCode) {
		this.taxCode = taxCode;
	}

	public Integer getTaxId() {
		return taxId;
	}

	public void setTaxId(Integer taxId) {
		this.taxId = taxId;
	}

	public String getErpUnitCode() {
		return erpUnitCode;
	}

	public void setErpUnitCode(String erpUnitCode) {
		this.erpUnitCode = erpUnitCode;
	}

	public String getMaterialspec() {
		return materialspec;
	}

	public void setMaterialspec(String materialspec) {
		this.materialspec = materialspec;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public String getProductType() {
		return productType;
	}

	public void setProductType(String productType) {
		this.productType = productType;
	}
}
