package cn.bohoon.product.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.springframework.util.StringUtils;

import cn.bohoon.framework.SpringContextHolder;
import cn.bohoon.product.service.ProductService;

/**
 * 关联商品管理
 * 
 * @author
 *
 */
@Entity
@Table(name = "t_product_relevance")
public class ProductRelevance {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Integer id;
	String productIds;
	Boolean state = true ; //true 为启用状态，false 为废弃状态
	Integer total;//数量，总数
	
	@Transient
	public String getProductNames() {
		if(!StringUtils.isEmpty(productIds)) {
			ProductService service = SpringContextHolder.getBean(ProductService.class);
			return service.getProductNames(productIds);
		}
		
		return "" ;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getProductIds() {
		return productIds;
	}

	public void setProductIds(String productIds) {
		this.productIds = productIds;
	}

	public Boolean getState() {
		return state;
	}

	public void setState(Boolean state) {
		this.state = state;
	}
	public synchronized final Integer getTotal() {
		return total;
	}

	public synchronized final void setTotal(Integer total) {
		this.total = total;
	}

}
