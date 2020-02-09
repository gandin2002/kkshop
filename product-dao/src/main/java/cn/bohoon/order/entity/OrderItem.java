package cn.bohoon.order.entity;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.annotation.JSONField;

import cn.bohoon.framework.SpringContextHolder;
import cn.bohoon.order.domain.OrderState;
import cn.bohoon.order.service.AmOrderItemService;
import cn.bohoon.product.entity.Product;
import cn.bohoon.product.entity.ProductImage;
import cn.bohoon.product.entity.Sku;
import cn.bohoon.product.service.ProductImageService;
import cn.bohoon.product.service.ProductService;
import cn.bohoon.product.service.SkuService;

/** 订单明细 */
@Entity
@Table(name = "t_order_item")
public class OrderItem {

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
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public OrderItem() {
		super();
	}

	public OrderItem(AmOrderItem aoi) {
		this.orderId = aoi.getOrderId();
		this.userId = aoi.getUserId();
		this.username = aoi.getUsername();
		this.memberId = aoi.getMemberId();
		this.skuId = aoi.getSkuId();
		this.score = aoi.getScore();
		this.skuErpCode = aoi.getSkuErpCode();
		this.price = aoi.getPrice();
		this.ordinaryPrice = aoi.getOrdinaryPrice();
		this.skuImage = aoi.getSkuImage();
		this.productId = aoi.getProductId();
		this.productName = aoi.getProductName();
		this.attrAndAttrValues = aoi.getAttrAndAttrValues();
		this.weight = aoi.getWeight();
		this.volume = aoi.getVolume();
		this.quantity = aoi.getQuantity();
		this.unitName = aoi.getUnitName();
		this.skuCode = aoi.getSkuCode();
		this.tax = aoi.getTax() ;
	}

	public String getRealname() {
		return realname;
	}

	public void setRealname(String realname) {
		this.realname = realname;
	}

	@Transient
	public boolean getCanService() {
		boolean canService = true;
		AmOrderItemService service = SpringContextHolder.getBean(AmOrderItemService.class);
		String sql2 = "select count(*) from AmOrderItem where orderId=? and skuId=?";
		Long count = service.select(sql2, Long.class, orderId, skuId);
		if (count > 0) {
			canService = false;
		}
		return canService;
	}

	@JSONField(serialize = false)
	@Transient
	public BigDecimal getSubtotal() {
		BigDecimal subtotal = price.multiply(new BigDecimal(quantity));
		return subtotal;
	}

	@JSONField(serialize = false)
	public BigDecimal getOrdinarySubTotal() {
		BigDecimal subtotal = ordinaryPrice.multiply(new BigDecimal(quantity));
		return subtotal;
	}

	@JSONField(serialize = false)
	@Transient
	public BigDecimal getWeighttotal() {
		BigDecimal weighttotal = weight.multiply(new BigDecimal(quantity));
		return weighttotal;
	}

	@JSONField(serialize = false)
	@Transient
	public BigDecimal getVolumetotal() {
		BigDecimal volumetotal = volume.multiply(new BigDecimal(quantity));
		return volumetotal;
	}

	@JSONField(serialize = false)
	@Transient
	public List<ProductImage> getProductImageList() {
		ProductImageService productImageService = SpringContextHolder.getBean(ProductImageService.class);
		return productImageService.getProductImage(productId);
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
	
	@JSONField(serialize = false)
	@Transient
	public Product getProduct(){
		if(!StringUtils.isEmpty(this.productId)){
				ProductService productService = SpringContextHolder.getBean(ProductService.class);
				return productService.get(productId);
		}
		return null;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
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

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
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

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
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

	public String getSkuImage() {
		return skuImage;
	}

	public void setSkuImage(String skuImage) {
		this.skuImage = skuImage;
	}

	public BigDecimal getOrdinaryPrice() {
		return ordinaryPrice;
	}

	public void setOrdinaryPrice(BigDecimal ordinaryPrice) {
		this.ordinaryPrice = ordinaryPrice;
	}

	public String getSkuErpCode() {
		return skuErpCode;
	}

	public void setSkuErpCode(String skuErpCode) {
		this.skuErpCode = skuErpCode;
	}

	public String getSkuCode() {
		return skuCode;
	}

	public void setSkuCode(String skuCode) {
		this.skuCode = skuCode;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Boolean getStorageOut() {
		return storageOut;
	}

	public void setStorageOut(Boolean storageOut) {
		this.storageOut = storageOut;
	}

	public Integer getTax() {
		return tax;
	}

	public void setTax(Integer tax) {
		this.tax = tax;
	}

	@Override
	public String toString() {
		return "OrderItem [id=" + id + ", orderId=" + orderId + ", lables=" + lables + ", userId=" + userId
				+ ", username=" + username + ", memberId=" + memberId + ", skuId=" + skuId + ", score=" + score
				+ ", skuErpCode=" + skuErpCode + ", price=" + price + ", ordinaryPrice=" + ordinaryPrice + ", skuImage="
				+ skuImage + ", productId=" + productId + ", productName=" + productName + ", attrAndAttrValues="
				+ attrAndAttrValues + ", weight=" + weight + ", volume=" + volume + ", quantity=" + quantity
				+ ", unitName=" + unitName + ", skuCode=" + skuCode + ", storageOut=" + storageOut + ", createDate="
				+ createDate + ", orderState=" + orderState + ", tax=" + tax + ", taxId=" + taxId + ", erpTaxId="
				+ erpTaxId + ", rowNo=" + rowNo + ", unitCode=" + unitCode + ", RequirementDate=" + RequirementDate
				+ ", ConsignmentDate=" + ConsignmentDate + ", otax=" + otax + ", oAmountWithTax=" + oAmountWithTax
				+ ", oAmount=" + oAmount + ", saleUnitName=" + saleUnitName + ", saleQuantity=" + saleQuantity
				+ ", realname=" + realname + ", title=" + title + "]";
	}

	public OrderState getOrderState() {
		return orderState;
	}

	public void setOrderState(OrderState orderState) {
		this.orderState = orderState;
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

	public BigDecimal getOtax() {
		return otax;
	}

	public void setOtax(BigDecimal otax) {
		this.otax = otax;
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

	public String getLables() {
		return lables;
	}

	public void setLables(String lables) {
		this.lables = lables;
	}
}