package cn.bohoon.product.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/** 浏览记录SPU */
@Entity
@Table(name = "t_browse")
public class Browse {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Integer id;
	String userId;
	Integer productId;
	@Column(columnDefinition = "datetime")
	Date createDate = new Date();
	Integer vcs = 1 ; //浏览次数
	
	
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

	public Integer getVcs() {
		return vcs;
	}

	public void setVcs(Integer vcs) {
		this.vcs = vcs;
	}
	
}