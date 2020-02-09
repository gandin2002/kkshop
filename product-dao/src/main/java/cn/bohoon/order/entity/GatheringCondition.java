package cn.bohoon.order.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 付款条款
 * @author HJ
 * 2018年7月2日,下午4:11:13
 */
@Entity
@Table(name="t_gathering_condition")
public class GatheringCondition {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	Integer id;
	String conditionName;
	String erpCode;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getConditionName() {
		return conditionName;
	}
	public void setConditionName(String conditionName) {
		this.conditionName = conditionName;
	}
	public String getErpCode() {
		return erpCode;
	}
	public void setErpCode(String erpCode) {
		this.erpCode = erpCode;
	}
}
