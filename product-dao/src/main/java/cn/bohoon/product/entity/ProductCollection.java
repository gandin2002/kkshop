package cn.bohoon.product.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;


	/**
	 * @author HX 货品集
	 */
	@Entity
	@Table(name = "t_product_collection")
	public class ProductCollection {
		@Id
		@GeneratedValue(strategy = GenerationType.IDENTITY)
		private Integer id;
		private Integer userId;// 用户Id
		private Integer productId;// 产品Id
		private String SortId;// 货品集分类ID
		private Integer productnum;// 购买数量
		@Column(columnDefinition = "datetime")
		Date createDate = new Date();// 设置时间
			
		public ProductCollection() {
		}
		
		public ProductCollection(Integer userId, Integer productId,Integer productnum) {
			super();
			this.userId = userId;
			this.productId = productId;
			this.productnum= productnum;
		}

		public Integer getId() {
			return id;
		}

		public void setId(Integer id) {
			this.id = id;
		}

		
		public Integer getProductnum() {
			return productnum;
		}

		public void setProductnum(Integer productnum) {
			this.productnum = productnum;
		}

		public Integer getProductId() {
			return productId;
		}

		public void setProductId(Integer productId) {
			this.productId = productId;
		}

		public String getSortId() {
			return SortId;
		}

		public void setSortId(String sortId) {
			SortId = sortId;
		}

		public Integer getUserId() {
			return userId;
		}

		public void setUserId(Integer userId) {
			this.userId = userId;
		}

		public Date getCreateDate() {
			return createDate;
		}

		public void setCreateDate(Date createDate) {
			this.createDate = createDate;
		}

	}
