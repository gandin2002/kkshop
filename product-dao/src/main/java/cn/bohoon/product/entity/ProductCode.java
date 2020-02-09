package cn.bohoon.product.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
/**
 * 商品二维码
 * @author HJ
 * 2018年1月15日,上午10:59:57
 */
@Entity
@Table(name="t_product_code")
public class ProductCode {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	Integer id;

	Integer productId;

	// String barCode; //条形码 bese64字符串

	@Column(columnDefinition = "text", nullable = true)
	String qrCode;// 二维码bese64字符串

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getProductId() {
		return productId;
	}

	public void setProductId(Integer productId) {
		this.productId = productId;
	}

	public String getQrCode() {
		return qrCode;
	}

	public void setQrCode(String qrCode) {
		this.qrCode = qrCode;
	}
}
