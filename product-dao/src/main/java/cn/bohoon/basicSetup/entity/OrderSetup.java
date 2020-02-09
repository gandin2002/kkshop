package cn.bohoon.basicSetup.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="t_order_setup")
public class OrderSetup {
	
	@Id
	private Integer id;
	
	private Integer notPaymentTime; //未付款订单取消时间  已天为单位
	
	@Column(columnDefinition="tinyint(2)")
	private Boolean notDeliverState; //true 付款后 发货前 false 付款后一段时间
	
	private Integer notDeliverTime;//付款后允许取消时间
	
	private Integer orderAutoTime; //订单自动确认时间
	
	private Integer orderDelayTime;//订单延长时间
	
	private Integer afterSaleDelayTime; //售后延长时间
	
	private Integer salesReturnTime; //无理由退货时间　不得低于７天
	
	private Integer afterSaleApplyTime; //申请售后一段时间后自动成功
	
	private Integer autoRefundTime; //财务退款默认审核通过时间
	
	private Integer autoSalesReturnTime;//卖家退货默认时间限制

	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getNotPaymentTime() {
		return notPaymentTime;
	}

	public void setNotPaymentTime(Integer notPaymentTime) {
		this.notPaymentTime = notPaymentTime;
	}

	public Boolean getNotDeliverState() {
		return notDeliverState;
	}

	public void setNotDeliverState(Boolean notDeliverState) {
		this.notDeliverState = notDeliverState;
	}

	public Integer getOrderAutoTime() {
		return orderAutoTime;
	}

	public void setOrderAutoTime(Integer orderAutoTime) {
		this.orderAutoTime = orderAutoTime;
	}

	public Integer getOrderDelayTime() {
		return orderDelayTime;
	}

	public void setOrderDelayTime(Integer orderDelayTime) {
		this.orderDelayTime = orderDelayTime;
	}

	public Integer getAfterSaleDelayTime() {
		return afterSaleDelayTime;
	}

	public void setAfterSaleDelayTime(Integer afterSaleDelayTime) {
		this.afterSaleDelayTime = afterSaleDelayTime;
	}

	public Integer getSalesReturnTime() {
		return salesReturnTime;
	}

	public void setSalesReturnTime(Integer salesReturnTime) {
		this.salesReturnTime = salesReturnTime;
	}

	public Integer getAfterSaleApplyTime() {
		return afterSaleApplyTime;
	}

	public void setAfterSaleApplyTime(Integer afterSaleApplyTime) {
		this.afterSaleApplyTime = afterSaleApplyTime;
	}

	public Integer getAutoRefundTime() {
		return autoRefundTime;
	}

	public void setAutoRefundTime(Integer autoRefundTime) {
		this.autoRefundTime = autoRefundTime;
	}

	public Integer getAutoSalesReturnTime() {
		return autoSalesReturnTime;
	}

	public void setAutoSalesReturnTime(Integer autoSalesReturnTime) {
		this.autoSalesReturnTime = autoSalesReturnTime;
	}

	public Integer getNotDeliverTime() {
		return notDeliverTime;
	}

	public void setNotDeliverTime(Integer notDeliverTime) {
		this.notDeliverTime = notDeliverTime;
	}
	
}
