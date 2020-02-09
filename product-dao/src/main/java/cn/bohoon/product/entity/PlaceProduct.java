package cn.bohoon.product.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
/** 商品推荐 */
@Entity
@Table(name = "t_place_product")
public class PlaceProduct {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Integer id; 
	String location; //位置
	String category1;
	Integer productId; //商品ID
	@Column(columnDefinition="int default 0")
	Integer sort = 0; //排序
	@Column(columnDefinition="tinyint(2)")
	Boolean status = false; //状态  0-未发布    1-已发布
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public String getCategory1() {
		return category1;
	}
	public void setCategory1(String category1) {
		this.category1 = category1;
	}
	public Integer getProductId() {
		return productId;
	}
	public void setProductId(Integer productId) {
		this.productId = productId;
	}
	public Integer getSort() {
		return sort;
	}
	public void setSort(Integer sort) {
		this.sort = sort;
	}
	public Boolean getStatus() {
		return status;
	}
	public void setStatus(Boolean status) {
		this.status = status;
	}
}
