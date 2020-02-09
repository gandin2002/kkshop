package cn.bohoon.order.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="t_pay_way")
public class PayWay {

	@Id
	String settleTypeId;
	String settleTypeName;
	Date lastOperateTime;
	String remark;
	Integer currentState;
	
	public String getSettleTypeId() {
		return settleTypeId;
	}
	public void setSettleTypeId(String settleTypeId) {
		this.settleTypeId = settleTypeId;
	}
	public String getSettleTypeName() {
		return settleTypeName;
	}
	public void setSettleTypeName(String settleTypeName) {
		this.settleTypeName = settleTypeName;
	}
	public Date getLastOperateTime() {
		return lastOperateTime;
	}
	public void setLastOperateTime(Date lastOperateTime) {
		this.lastOperateTime = lastOperateTime;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public Integer getCurrentState() {
		return currentState;
	}
	public void setCurrentState(Integer currentState) {
		this.currentState = currentState;
	}
}
