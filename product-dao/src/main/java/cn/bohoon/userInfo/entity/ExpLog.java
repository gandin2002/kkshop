package cn.bohoon.userInfo.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import cn.bohoon.userInfo.domain.ExpType;
/**
 * 经验记录
 * @author hj
 *2017年11月9日10:19:05
 */
@Entity
@Table(name="t_exp_log")
public class ExpLog {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private String memberId;
	private Date createDate = new Date();
	private ExpType expType; // 类型
	private String orderId; // 订单
	private Integer exp; // 经验值
	private String note;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getMemberId() {
		return memberId;
	}
	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public ExpType getExpType() {
		return expType;
	}
	public void setExpType(ExpType expType) {
		this.expType = expType;
	}
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public Integer getExp() {
		return exp;
	}
	public void setExp(Integer exp) {
		this.exp = exp;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}

}
