package cn.bohoon.product.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * 商品图片
 */
@Entity
@Table(name = "t_product_image")
public class ProductImage {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private String imageUrl;
	private Integer productId; //商品Id
	 
	@Transient
	public String getImageBig(){
		String fileExt = imageUrl.substring(imageUrl.lastIndexOf(".")).toLowerCase();
		return imageUrl.replace(fileExt, "_b"+fileExt);
	}
	
	@Transient
	public String getImageSmall(){
		String fileExt = imageUrl.substring(imageUrl.lastIndexOf(".")).toLowerCase();
		return imageUrl.replace(fileExt, "_s"+fileExt);
	}
	
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

	public String getImageUrl() {
		return imageUrl;
	}
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

}
