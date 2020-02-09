package cn.bohoon.product.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/** 推荐商品 */
@Entity
@Table(name = "t_product_recommend")
public class ProductRecommend {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private Integer productId;
	@Column(columnDefinition="tinyint(2)")
	private Boolean flag; // 1-启用    0-禁用

	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getProductId() {
		return productId;
	}
	public void setProductId(Integer productId) {
		this.productId = productId;
	}
	public Boolean getFlag() {
		return flag;
	}
	public void setFlag(Boolean flag) {
		this.flag = flag;
	}
	
}
