package cn.bohoon.product.entity;

import java.math.BigDecimal;
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

import com.alibaba.fastjson.annotation.JSONField;

import cn.bohoon.framework.SpringContextHolder;
import cn.bohoon.framework.util.DateUtil;
import cn.bohoon.product.service.AttrService;
import cn.bohoon.product.service.AttrValueService;
import cn.bohoon.product.service.ProductScoreService;

@Entity
@Table(name = "t_sku_score")
public class SkuScore {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Integer id; // 商品skuId

	@JSONField(serialize = false)
	String code; // 商品Sku编码
	String name;// 名字+Sku 编码
	@JSONField(serialize = false)
	String erpCode; // Sku-ERP编码
	Integer productId; // 商品Id
	BigDecimal skuPrice = new BigDecimal(0.00); // 商品sku价格

	@JSONField(serialize = false)
	@Column(columnDefinition = "tinyint(2)")
	Boolean flag; // 商品sku上下架 0 -下架 1 - 上架
	BigDecimal weight; // 重量
	BigDecimal volume; // 体积
	Integer inventory = 0; // 商品sku库存
	String imageUrl; // 商品sku图片
	String attrValues; // sku属性值串

	@JSONField(serialize = false)
	@Column(columnDefinition = "tinyint(2)")
	Boolean status = true; // sku状态 0 - 无效 1 - 有效
	@Column(columnDefinition = "int default 0")
	Integer salesNum = 0; // 销售量
	Integer score;// 积分

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
	Integer exchangeAmout;// 可兑换量
	String productType="score";//商品类型    默认赠品：score ,非赠品：sell
	
	
	@Transient
	ProductScore productScore;
	
	
	
	public SkuScore() {
		super();
	}

	public SkuScore(Sku sku) {
		super();
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
		this.tax = sku.getTax();
		this.taxId = sku.getTaxId();
		this.productType = sku.getProductType();
	}

	public SkuScore(Integer id, String code, String name, String erpCode, Integer productId, BigDecimal skuPrice, Boolean flag, BigDecimal weight, BigDecimal volume, Integer inventory, String imageUrl,
			String attrValues, Boolean status, Integer salesNum, Integer score, String barCode, Integer translateRate, Integer auxUnitId, String auxUnitName, String storageLocation, Date createDate,
			Integer tax, Integer taxId ,Integer exchangeAmout,String productType) {
		super();
		this.id = id;
		this.code = code;
		this.name = name;
		this.erpCode = erpCode;
		this.productId = productId;
		this.skuPrice = skuPrice;
		this.flag = flag;
		this.weight = weight;
		this.volume = volume;
		this.inventory = inventory;
		this.imageUrl = imageUrl;
		this.attrValues = attrValues;
		this.status = status;
		this.salesNum = salesNum;
		this.score = score;
		this.barCode = barCode;
		this.translateRate = translateRate;
		this.auxUnitId = auxUnitId;
		this.auxUnitName = auxUnitName;
		this.storageLocation = storageLocation;
		this.createDate = createDate;
		this.tax = tax;
		this.taxId = taxId;
		this.exchangeAmout = exchangeAmout;
		this.productType = productType;
	}


	@Transient
	public String getCreateDateString() {
		return DateUtil.formatDate(this.createDate);
	}

	@JSONField(serialize=false)
	@Transient
	public ProductScore getProduct() {
		ProductScoreService service = SpringContextHolder.getBean(ProductScoreService.class) ;
		return service.get(productId) ;
	}
 
	
	
	@Transient
	public List<String> getSkuAttr(){
		return Arrays.asList(attrValues.split(","));
	}
	
	@JSONField(serialize = false)
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
	
	@JSONField(serialize= false )
	@Transient
	public String getAttrAndAttrValues(){
		AttrService attrService = SpringContextHolder.getBean(AttrService.class);
		AttrValueService attrValueService = SpringContextHolder.getBean(AttrValueService.class);
		List<String> skuAttr = getSkuAttr();
		StringBuffer buffer = new StringBuffer();
		for(String skuAttr1 : skuAttr){
			AttrValue attrValue = attrValueService.get(Integer.parseInt(skuAttr1));
			if(null == attrValue ) {
				buffer.append("无SKU规格");
				buffer.append(" ");
				continue ;
			}
			Attr attr = attrService.get(attrValue.getAttrId());
			if(null == attr ) {
				buffer.append("无SKU规格");
				buffer.append(" ");
				continue ;
			}
			buffer.append(attr.getName());
			buffer.append(":");
			buffer.append(attrValue.getName());
			buffer.append(" ");
		}
		return buffer.toString();
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	public String getImageUrl() {
		return imageUrl;
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
		  return auxUnitName.trim();
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

	public ProductScore getProductScore() {
		return productScore;
	}

	public void setProductScore(ProductScore productScore) {
		this.productScore = productScore;
	}

	public Integer getExchangeAmout() {
		return exchangeAmout;
	}

	public void setExchangeAmout(Integer exchangeAmout) {
		this.exchangeAmout = exchangeAmout;
	}

	public String getProductType() {
		return productType;
	}

	public void setProductType(String productType) {
		this.productType = productType;
	}
}
