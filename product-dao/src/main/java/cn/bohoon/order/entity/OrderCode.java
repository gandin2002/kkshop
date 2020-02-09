package cn.bohoon.order.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
/**
 * 条形码 与 二维码 数据
 * @author hj
 *2017年11月9日10:17:51
 */
@Entity
@Table(name="t_order_code")
public class OrderCode {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Integer id;
	
	@Column(length=64)
	private String orderId ;
	
	@Column(columnDefinition = "text ",nullable = true)
	private String barCode; //条形码 bese64 字符串
	
	@Column(columnDefinition = "text ",nullable = true)
	private String qrCode; //二维码bese64 字符串
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public String getBarCode() {
		return barCode;
	}
	public void setBarCode(String barCode) {
		this.barCode = barCode;
	}
	public String getQrCode() {
		return qrCode;
	}
	public void setQrCode(String qrCode) {
		this.qrCode = qrCode;
	}
}
