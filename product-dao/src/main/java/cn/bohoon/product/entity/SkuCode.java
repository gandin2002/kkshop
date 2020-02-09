package cn.bohoon.product.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * SKu 二维码
 * @author HJ
 * 2018年2月2日,下午3:09:06
 */
@Entity
@Table(name="t_sku_code")
public class SkuCode {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	Integer id;
	
	Integer skuId;

	@Column(columnDefinition = "text", nullable = true)
	String qrcode;// 二维码bese64字符串

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

	public String getQrcode() {
		return qrcode;
	}

	public void setQrcode(String qrcode) {
		this.qrcode = qrcode;
	}

}
