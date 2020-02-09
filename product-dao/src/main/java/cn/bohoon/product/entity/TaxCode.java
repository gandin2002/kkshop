package cn.bohoon.product.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.format.annotation.DateTimeFormat;
/**
 * 税码
 * @author HJ
 * 2018年2月8日,上午10:22:49
 */
@Table(name="t_tax_code")
@Entity
public class TaxCode {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	Integer id;
	String name; //名称
	Integer tax; //税率
	Integer sort = 1; //排序
	@Column(columnDefinition = "datetime")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	Date createDate = new Date(); //创建时间
	@Column(columnDefinition="INT default 0")
	Boolean state = false; //true 为默认选中
	String erpCode;//erpDe;
	Double taxRate;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getTax() {
		return tax;
	}
	public void setTax(Integer tax) {
		this.tax = tax;
	}
	public Integer getSort() {
		return sort;
	}
	public void setSort(Integer sort) {
		this.sort = sort;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public Boolean getState() {
		return state;
	}
	public void setState(Boolean state) {
		this.state = state;
	}
	public String getErpCode() {
		return erpCode;
	}
	public void setErpCode(String erpCode) {
		this.erpCode = erpCode;
	}
	public Double getTaxRate() {
		return taxRate;
	}
	public void setTaxRate(Double taxRate) {
		this.taxRate = taxRate;
	}
}
