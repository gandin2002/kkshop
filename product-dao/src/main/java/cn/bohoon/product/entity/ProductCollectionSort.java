package cn.bohoon.product.entity;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author HX 货品集分类
 */
@Entity
@Table(name = "t_product_collectionSort")
public class ProductCollectionSort {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private Integer userId;// 用户Id
	private Integer productId;// 产品Id
	private String CollectionSort;// 货品集分类
	@Column(columnDefinition = "tinyint(2)") // 商品定期购买 0 -不定期 1 - 定期
	Boolean regularIntervals;// 是否定期购买

	@Column(columnDefinition = "datetime")
	Date createDate = new Date();// 设置时间

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public Integer getProductId() {
		return productId;
	}

	public void setProductId(Integer productId) {
		this.productId = productId;
	}

	public String getCollectionSort() {
		return CollectionSort;
	}

	public void setCollectionSort(String collectionSort) {
		CollectionSort = collectionSort;
	}

	public Boolean getRegularIntervals() {
		return regularIntervals;
	}

	public void setRegularIntervals(Boolean regularIntervals) {
		this.regularIntervals = regularIntervals;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

}
