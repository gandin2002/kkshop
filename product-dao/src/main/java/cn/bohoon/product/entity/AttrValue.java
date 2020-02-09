package cn.bohoon.product.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
/** 属性值 */
@Entity
@Table(name = "t_attr_value")
public class AttrValue {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id; //主键id
	private Integer attrId; //属性Id
	private String name;
	private Integer sort = 0;
	@Column(columnDefinition="tinyint(2)")
	private Boolean status ; //是否可用
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getAttrId() {
		return attrId;
	}
	public void setAttrId(Integer attrId) {
		this.attrId = attrId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getSort() {
		return sort;
	}
	public void setSort(Integer sort) {
		this.sort = sort;
	}
	public Boolean getStatus() {
		return status;
	}
	public void setStatus(Boolean status) {
		this.status = status;
	}
}
