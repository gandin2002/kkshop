package cn.bohoon.order.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/** 购物车 */
@Entity
@Table(name = "t_shopping_cart")
public class ShoppingCart {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(length=64)
	private String userId;  // 用户ID
	/* 商品skuId*/
	private Integer skuId;
	/* 商品sku数量 (动态获取 主计量单位)*/
	private Integer quantity = 1;
	/* 创建时间*/
	@Column(columnDefinition = "datetime")
	private Date createDate = new Date();
	
	public ShoppingCart() {
	}

	public ShoppingCart(String userId, Integer skuId, Integer quantity) {
		this.userId = userId;
		this.skuId = skuId;
		this.quantity = quantity;
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

	public Integer getSkuId() {
		return skuId;
	}

	public void setSkuId(Integer skuId) {
		this.skuId = skuId;
	}
	
	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

}