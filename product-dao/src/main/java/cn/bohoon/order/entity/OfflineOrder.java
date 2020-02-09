package cn.bohoon.order.entity;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.format.annotation.DateTimeFormat;

import com.alibaba.fastjson.annotation.JSONField;

import cn.bohoon.order.domain.OrderState;
/** 线下订单详情 */
@Entity
@Table(name = "t_offlineOrder" )
public class OfflineOrder {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Integer id;

	@Column(length = 64)
	String orderId; // 订单ID
	
	private String lables; // 商品标签(辅助分类)

	@Column(length = 64)
	String userId; // 下单人ID
	String username; // 下单人名称
	@Column(length = 64)
	String memberId; // 会员ID
	String skuId; // 商品skuId
	Integer score; // 购买货品获取 积分
	String skuErpCode; // 商品sku ErpCode 编码 【需与T8物料编码一一对应】
	BigDecimal price; // 商品购买时的单个价格
	BigDecimal ordinaryPrice; // 商品折扣优惠之前的单个价
	String skuImage; // 商品sku图片
	Integer productId; // 商品ID
	String productName; // 商品名称
	String attrAndAttrValues; // 商品属性值
	BigDecimal weight; // 重量
	BigDecimal volume; // 体积
	Integer quantity; // 数量/最小计量单位
	String unitName; // 最小计量单位
	String skuCode; // 商品sku编码
	@Column(columnDefinition = "tinyint(2)")
	Boolean storageOut = false; // 订单状态更新为完成已发货时更新为true

	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	@Column(columnDefinition = "datetime")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	Date createDate = new Date();// 订单创建时间
	@Enumerated(EnumType.STRING)
	OrderState orderState = OrderState.INIT_STATE; //订单状态 
	Integer tax; // 税率
	Integer taxId;//税码ID
	String erpTaxId;//税码ID
	Integer rowNo = 1;//个数
	String unitCode;//unit Code
	
	Integer RequirementDate = 0; //商品期望收货日期
	Integer ConsignmentDate = 0; //商品预计收货日期
	
	BigDecimal otax = new BigDecimal(0);//税金
	BigDecimal oAmountWithTax = new BigDecimal(0); //总金额 含税金额
	BigDecimal oAmount = new BigDecimal(0);//不含税金额
	
	String saleUnitName; //销售单位名字
	Integer saleQuantity;//销售数量  换算单位数量
	String realname; 	//下单用户
	String title; //下单部门名称
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
	public String getLables() {
		return lables;
	}
	public void setLables(String lables) {
		this.lables = lables;
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
	public String getSkuId() {
		return skuId;
	}
	public void setSkuId(String skuId) {
		this.skuId = skuId;
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
	public String getAttrAndAttrValues() {
		return attrAndAttrValues;
	}
	public void setAttrAndAttrValues(String attrAndAttrValues) {
		this.attrAndAttrValues = attrAndAttrValues;
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
	public Integer getQuantity() {
		return quantity;
	}
	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}
	public String getUnitName() {
		return unitName;
	}
	public void setUnitName(String unitName) {
		this.unitName = unitName;
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
	public OrderState getOrderState() {
		return orderState;
	}
	public void setOrderState(OrderState orderState) {
		this.orderState = orderState;
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
	public String getErpTaxId() {
		return erpTaxId;
	}
	public void setErpTaxId(String erpTaxId) {
		this.erpTaxId = erpTaxId;
	}
	public Integer getRowNo() {
		return rowNo;
	}
	public void setRowNo(Integer rowNo) {
		this.rowNo = rowNo;
	}
	public String getUnitCode() {
		return unitCode;
	}
	public void setUnitCode(String unitCode) {
		this.unitCode = unitCode;
	}
	public Integer getRequirementDate() {
		return RequirementDate;
	}
	public void setRequirementDate(Integer requirementDate) {
		RequirementDate = requirementDate;
	}
	public Integer getConsignmentDate() {
		return ConsignmentDate;
	}
	public void setConsignmentDate(Integer consignmentDate) {
		ConsignmentDate = consignmentDate;
	}
	public BigDecimal getOtax() {
		return otax;
	}
	public void setOtax(BigDecimal otax) {
		this.otax = otax;
	}
	public BigDecimal getoAmountWithTax() {
		return oAmountWithTax;
	}
	public void setoAmountWithTax(BigDecimal oAmountWithTax) {
		this.oAmountWithTax = oAmountWithTax;
	}
	public BigDecimal getoAmount() {
		return oAmount;
	}
	public void setoAmount(BigDecimal oAmount) {
		this.oAmount = oAmount;
	}
	public String getSaleUnitName() {
		return saleUnitName;
	}
	public void setSaleUnitName(String saleUnitName) {
		this.saleUnitName = saleUnitName;
	}
	public Integer getSaleQuantity() {
		return saleQuantity;
	}
	public void setSaleQuantity(Integer saleQuantity) {
		this.saleQuantity = saleQuantity;
	}
	public String getRealname() {
		return realname;
	}
	public void setRealname(String realname) {
		this.realname = realname;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public OfflineOrder(Integer id, String orderId, String lables, String userId, String username, String memberId,
			String skuId, Integer score, String skuErpCode, BigDecimal price, BigDecimal ordinaryPrice, String skuImage,
			Integer productId, String productName, String attrAndAttrValues, BigDecimal weight, BigDecimal volume,
			Integer quantity, String unitName, String skuCode, Boolean storageOut, Date createDate,
			OrderState orderState, Integer tax, Integer taxId, String erpTaxId, Integer rowNo, String unitCode,
			Integer requirementDate, Integer consignmentDate, BigDecimal otax, BigDecimal oAmountWithTax,
			BigDecimal oAmount, String saleUnitName, Integer saleQuantity, String realname, String title) {
		super();
		this.id = id;
		this.orderId = orderId;
		this.lables = lables;
		this.userId = userId;
		this.username = username;
		this.memberId = memberId;
		this.skuId = skuId;
		this.score = score;
		this.skuErpCode = skuErpCode;
		this.price = price;
		this.ordinaryPrice = ordinaryPrice;
		this.skuImage = skuImage;
		this.productId = productId;
		this.productName = productName;
		this.attrAndAttrValues = attrAndAttrValues;
		this.weight = weight;
		this.volume = volume;
		this.quantity = quantity;
		this.unitName = unitName;
		this.skuCode = skuCode;
		this.storageOut = storageOut;
		this.createDate = createDate;
		this.orderState = orderState;
		this.tax = tax;
		this.taxId = taxId;
		this.erpTaxId = erpTaxId;
		this.rowNo = rowNo;
		this.unitCode = unitCode;
		RequirementDate = requirementDate;
		ConsignmentDate = consignmentDate;
		this.otax = otax;
		this.oAmountWithTax = oAmountWithTax;
		this.oAmount = oAmount;
		this.saleUnitName = saleUnitName;
		this.saleQuantity = saleQuantity;
		this.realname = realname;
		this.title = title;
	}
	public OfflineOrder() {
		super();
		// TODO Auto-generated constructor stub
	}
	
      

}
