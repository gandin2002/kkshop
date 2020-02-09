package cn.bohoon.stock.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/*
 * 仓库库位信息
 */
@Entity
@Table(name = "t_warehouse_location")
public class WarehLocation {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	Integer id; // 发货主键id
	
	@Column(length = 64)
	String name ; //库位名称
	
	@Column(length = 64)
	String code ; //库位编码

	String typeId; // 仓库类型
	
	@Column(length = 64)
	String typeName; // 仓库类型名称
	
	Integer wareHouseId ; //仓库ID
	
	@Column(length = 64)
	String wareHouseName ; //仓库名称

	String mark; // 备注

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getTypeId() {
		return typeId;
	}

	public void setTypeId(String typeId) {
		this.typeId = typeId;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
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

	public String getMark() {
		return mark;
	}

	public void setMark(String mark) {
		this.mark = mark;
	}

}
