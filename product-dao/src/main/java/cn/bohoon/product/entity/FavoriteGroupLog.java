package cn.bohoon.product.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
@Entity
@Table(name = "t_favorite_group_log")
public class FavoriteGroupLog { 
	/**
	 * @author admin 商品分组收藏
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	Integer id;
	String groupId;
	String productId;
	

	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getGroupId() {
		return groupId;
	}
	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}
	public String getProductId() {
		return productId;
	}
	public void setProductId(String productId) {
		this.productId = productId;
	}
	
	
	
	
    
	
}
