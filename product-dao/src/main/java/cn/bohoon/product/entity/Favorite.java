package cn.bohoon.product.entity;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author admin 商品收藏
 */
@Entity
@Table(name = "t_favorite")
public class Favorite {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Integer id;
	String userId;
	Integer productId;
	
 
	@Column(columnDefinition = "datetime")
	Date createDate = new Date();

	public Favorite() {
		super();
	}

	public Favorite(String userId, Integer productId) {
		super();
		this.userId = userId;
		this.productId = productId;
	}

	public Favorite(Integer id, String userId, Integer productId, Date createDate) {
		super();
		this.id = id;
		this.userId = userId;
		this.productId = productId;
		this.createDate = createDate;
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

	public Integer getProductId() {
		return productId;
	}

	public void setProductId(Integer productId) {
		this.productId = productId;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	
}
