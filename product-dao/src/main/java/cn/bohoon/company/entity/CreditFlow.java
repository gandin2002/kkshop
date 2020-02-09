package cn.bohoon.company.entity;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.format.annotation.DateTimeFormat;

import cn.bohoon.order.entity.OrderItem;

@Entity
@Table(name = "t_credit_flow")
public class CreditFlow {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	Integer id;// 信用流水ID
	String companyId; // 企业ID
	String productName;// 货品名称
	Integer productId; // 货品ID
	String skuId; // SKU ID
	BigDecimal price = new BigDecimal(0); // 价格
	@Column(columnDefinition = "datetime")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	Date createDate = new Date(); // 流水账时间

	public CreditFlow() {
		super();
	}
	
	public CreditFlow(OrderItem oit,Company company) {
		super();
		this.companyId = company.getId() ;
		this.productName = oit.getProductName() ;
		this.productId = oit.getProductId() ;
		this.skuId = oit.getSkuId() ;
		this.price = oit.getSubtotal() ;
		this.createDate = new Date() ;
	}
	

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	public String getCompanyId() {
		return companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public Integer getProductId() {
		return productId;
	}

	public void setProductId(Integer productId) {
		this.productId = productId;
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

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

}
