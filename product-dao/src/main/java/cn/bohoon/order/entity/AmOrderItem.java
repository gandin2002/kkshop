package cn.bohoon.order.entity;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.format.annotation.DateTimeFormat;

/** 售后订单清单项 */
@Entity
@Table(name = "t_order_service_item")
public class AmOrderItem {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Integer id;

	@Column(length = 64)
	String orderId; // 订单ID

	@Column(length = 64)
	String amOrderId; /// 售后订单ID

	@Column(length = 64)
	String userId; // 下单人ID

	String username; // 下单人名称

	@Column(length = 64)
	String memberId; // 会员ID

	String skuId; // 商品skuId
	
	Integer score; // 购买货品获取 积分
	String skuErpCode; // 商品sku ErpCode 编码 【需与T8物料编码一一对应】
	BigDecimal price; // 商品购买时的单个价格
	BigDecimal ordinaryPrice; // 商品折扣优惠之前的单个价格
	String skuImage; // 商品sku图片
	Integer productId; // 商品ID
	String productName; // 商品名称
	String attrAndAttrValues; // 商品属性值
	BigDecimal weight; // 重量
	BigDecimal volume; // 体积
	Integer quantity = 0 ; // 数量/最小计量单位
	String unitName; // 最小计量单位

	String skuCode; // 商品sku编码
	@Column(columnDefinition = "tinyint(2)")
	Boolean storageOut = false; // 订单状态更新为完成已发货时更新为true
	@Column(columnDefinition = "datetime")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	Date createDate = new Date(); // 订单创建时间

	Integer tax; // 税率

	public AmOrderItem() {
		super();
	}

	public AmOrderItem(OrderItem oit) {
		this.orderId = oit.getOrderId();
		this.userId = oit.getUserId();
		this.username = oit.getUsername();
		this.memberId = oit.getMemberId();
		this.skuId = oit.getSkuId();
		this.score = oit.getScore();
		this.skuErpCode = oit.getSkuErpCode();
		this.price = oit.getPrice();
		this.ordinaryPrice = oit.getOrdinaryPrice();
		this.skuImage = oit.getSkuImage();
		this.productId = oit.getProductId();
		this.productName = oit.getProductName();
		this.attrAndAttrValues = oit.getAttrAndAttrValues();
		this.weight = oit.getWeight();
		this.volume = oit.getVolume();
		this.quantity = oit.getQuantity();
		this.unitName = oit.getUnitName();
		this.skuCode = oit.getSkuCode();
		this.tax = oit.getTax() ;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getAmOrderId() {
		return amOrderId;
	}

	public void setAmOrderId(String amOrderId) {
		this.amOrderId = amOrderId;
	}

	public String getSkuId() {
		return skuId;
	}

	public void setSkuId(String skuId) {
		this.skuId = skuId;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public BigDecimal getOrdinaryPrice() {
		return ordinaryPrice;
	}

	public void setOrdinaryPrice(BigDecimal ordinaryPrice) {
		this.ordinaryPrice = ordinaryPrice;
	}

	public String getSkuImage() {
		return skuImage;
	}

	public void setSkuImage(String skuImage) {
		this.skuImage = skuImage;
	}

	public Integer getProductId() {
		return productId;
	}

	public void setProductId(Integer productId) {
		this.productId = productId;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public String getAttrAndAttrValues() {
		return attrAndAttrValues;
	}

	public void setAttrAndAttrValues(String attrAndAttrValues) {
		this.attrAndAttrValues = attrAndAttrValues;
	}

	public String getUnitName() {
		return unitName;
	}

	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getMemberId() {
		return memberId;
	}

	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}

	public Integer getScore() {
		return score;
	}

	public void setScore(Integer score) {
		this.score = score;
	}

	public String getSkuErpCode() {
		return skuErpCode;
	}

	public void setSkuErpCode(String skuErpCode) {
		this.skuErpCode = skuErpCode;
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

	public String getSkuCode() {
		return skuCode;
	}

	public void setSkuCode(String skuCode) {
		this.skuCode = skuCode;
	}

	public Boolean getStorageOut() {
		return storageOut;
	}

	public void setStorageOut(Boolean storageOut) {
		this.storageOut = storageOut;
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

}
