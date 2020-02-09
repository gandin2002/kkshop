package cn.bohoon.stock.entity;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.format.annotation.DateTimeFormat;

import cn.bohoon.stock.domain.PurchaseState;

/**
 * 采购入库 采购单
 * 
 * @author dujianqiao
 *
 */
@Entity
@Table(name = "t_purchase")
public class Purchase {

	@Id
	@Column(length = 64)
	String id; // 主键
	
	Integer wareHouseId; // 仓库ID
	@Column(length = 64)
	String wareHouseName; // 仓库名称
	
	@Column(length = 64)
	String supplierId; // 供应商ID
	@Column(length = 64)
	String supplierName; //供应商名称
	
	BigDecimal total = new BigDecimal(0) ; //总金额
	Integer totalNum = 0 ; //采购总数量
	String operatorId ; //操作人ID
	String operator ; //操作人
	
	@Column(columnDefinition = "datetime")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	Date inWareHouseTime ; //入库时间
	
	@Enumerated(EnumType.STRING)
	PurchaseState state = PurchaseState.INIT ;  //采购单状态
	String mark ; //备注

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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

	public String getSupplierId() {
		return supplierId;
	}

	public void setSupplierId(String supplierId) {
		this.supplierId = supplierId;
	}

	public String getSupplierName() {
		return supplierName;
	}

	public void setSupplierName(String supplierName) {
		this.supplierName = supplierName;
	}

	public BigDecimal getTotal() {
		return total;
	}

	public void setTotal(BigDecimal total) {
		this.total = total;
	}

	public Integer getTotalNum() {
		return totalNum;
	}

	public void setTotalNum(Integer totalNum) {
		this.totalNum = totalNum;
	}

	public String getOperatorId() {
		return operatorId;
	}

	public void setOperatorId(String operatorId) {
		this.operatorId = operatorId;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public Date getInWareHouseTime() {
		return inWareHouseTime;
	}

	public void setInWareHouseTime(Date inWareHouseTime) {
		this.inWareHouseTime = inWareHouseTime;
	}

	public PurchaseState getState() {
		return state;
	}

	public void setState(PurchaseState state) {
		this.state = state;
	}

	public String getMark() {
		return mark;
	}

	public void setMark(String mark) {
		this.mark = mark;
	}
	
	
	
}
