package cn.bohoon.product.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "t_product_param")
public class ProductParam {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Integer id ;//id
	private Integer productId; //商品Id
	private String name ;//参数名称
	private String value ; //参数值
	private String ptype ;//参数类型
	@Column(columnDefinition="tinyint(2)")
	private Boolean fliterSearch ; //是否是过滤条件
	private String categoryId ;//分类ID ，参数归属分类信息

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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getPtype() {
		return ptype;
	}

	public void setPtype(String ptype) {
		this.ptype = ptype;
	}

	public Boolean getFliterSearch() {
		return fliterSearch;
	}

	public void setFliterSearch(Boolean fliterSearch) {
		this.fliterSearch = fliterSearch;
	}

	public String getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}
	
	
	
}
