package cn.bohoon.product.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
/** 商品描述 */
@Entity
@Table(name = "t_product_info")
public class ProductInfo {
	@Id
	private Integer id; //商品Id
	@Lob
	@Column(columnDefinition="text")  
	private String description; //商品介绍
	
	public Integer getId() {
		return id;
	}
	 
	public void setId(Integer id) {
		this.id = id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
