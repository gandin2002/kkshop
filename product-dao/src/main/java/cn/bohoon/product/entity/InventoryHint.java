package cn.bohoon.product.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/** 库存提示设置 */
@Entity
@Table(name = "t_inventory_hint")
public class InventoryHint {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private Integer min; //库存区间左值
	private Integer max; //库存区间右值
	private String hint; //提示文字
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getMin() {
		return min;
	}
	public void setMin(Integer min) {
		this.min = min;
	}
	public Integer getMax() {
		return max;
	}
	public void setMax(Integer max) {
		this.max = max;
	}
	public String getHint() {
		return hint;
	}
	public void setHint(String hint) {
		this.hint = hint;
	}
	
}
