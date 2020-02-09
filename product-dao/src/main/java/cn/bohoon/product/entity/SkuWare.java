package cn.bohoon.product.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 仓库货品数量表
 * 
 * @author dujianqiao
 *
 */
@Entity
@Table(name = "t_sku_warehouse")
public class SkuWare {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Integer id;
	Integer skuId; // 货品ID
	String  skuCode;
	Integer wareHouseId; // 仓库ID
	String  wareHouseCode;//仓库Code
	String wareHouseName; // 仓库名称
	Integer quantity = 0 ; // 数量
	

	public String getSkuCode() {
		return skuCode;
	}

	public void setSkuCode(String skuCode) {
		this.skuCode = skuCode;
	}

	public String getWareHouseCode() {
		return wareHouseCode;
	}

	public void setWareHouseCode(String wareHouseCode) {
		this.wareHouseCode = wareHouseCode;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getSkuId() {
		return skuId;
	}

	public void setSkuId(Integer skuId) {
		this.skuId = skuId;
	}

	public Integer getWareHouseId() {
		return wareHouseId;
	}

	public void setWareHouseId(Integer wareHouseId) {
		this.wareHouseId = wareHouseId;
	}

	public String getWareHouseName() {
		return wareHouseName;
	}

	public void setWareHouseName(String wareHouseName) {
		this.wareHouseName = wareHouseName;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

}
