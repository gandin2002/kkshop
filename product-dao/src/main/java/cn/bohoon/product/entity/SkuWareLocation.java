package cn.bohoon.product.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
/**
 * 货品库位信息
 * 
 * @author dujianqiao
 *
 */
@Entity
@Table(name = "t_sku_warelocation")
public class SkuWareLocation {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Integer id; //货品库位ID
	Integer skuId ;//货品ID
	String skuCode ; //货品CODE 
	String barCode ;//货品条形码
	String skuName ; //货品名称
	String attrName ; //货品规格
	
	Integer wareHouseId ; //仓库ID
	@Column(length = 64)
	String wareHouseName ; //仓库名称
	
	Integer locationId ; //库位ID
	@Column(length = 64)
	String localtionName ; //库位名称
	
	@Column(columnDefinition="tinyint(2)")
	Boolean isDefault = false ; //是否默认
	Boolean status = true ; //状态
	Integer inventory; //库存量
	
	String manager ; //管理员

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

	public String getSkuCode() {
		return skuCode;
	}

	public void setSkuCode(String skuCode) {
		this.skuCode = skuCode;
	}

	public String getBarCode() {
		return barCode;
	}

	public void setBarCode(String barCode) {
		this.barCode = barCode;
	}

	public String getSkuName() {
		return skuName;
	}

	public void setSkuName(String skuName) {
		this.skuName = skuName;
	}

	public String getAttrName() {
		return attrName;
	}

	public void setAttrName(String attrName) {
		this.attrName = attrName;
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

	public Integer getLocationId() {
		return locationId;
	}

	public void setLocationId(Integer locationId) {
		this.locationId = locationId;
	}

	public String getLocaltionName() {
		return localtionName;
	}

	public void setLocaltionName(String localtionName) {
		this.localtionName = localtionName;
	}

	public Boolean getIsDefault() {
		return isDefault;
	}

	public void setIsDefault(Boolean isDefault) {
		this.isDefault = isDefault;
	}

	public Boolean getStatus() {
		return status;
	}

	public void setStatus(Boolean status) {
		this.status = status;
	}

	public Integer getInventory() {
		return inventory;
	}

	public void setInventory(Integer inventory) {
		this.inventory = inventory;
	}

	public String getManager() {
		return manager;
	}

	public void setManager(String manager) {
		this.manager = manager;
	}
	
	
}
