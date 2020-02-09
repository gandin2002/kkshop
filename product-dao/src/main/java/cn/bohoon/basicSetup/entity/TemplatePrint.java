package cn.bohoon.basicSetup.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import cn.bohoon.basicSetup.domain.TemplatePrintType;
/**
 * 发票模板
 * @author hj
 *2017年11月9日10:17:45
 */

@Entity
@Table(name="t_template_print" )
public class TemplatePrint {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Integer id;
	
	@Enumerated(EnumType.STRING)
	private  TemplatePrintType  templatePrintType; //模板类型
	private Integer templateChoose = 1 ; // 默认第一个模板
	@Column(columnDefinition="tinyint(2)")
	private Boolean qRcodeState = true; //是否打印二维码 
	@Column(columnDefinition="tinyint(2)")
	private Boolean orderIdState = true;  //是否打印订单
	@Column(columnDefinition="tinyint(2)")
	private Boolean printImageState = true;//是否打印图片
	@Column(columnDefinition="tinyint(2)")
	private Boolean barcodesState = true; //是否打印条形码
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public TemplatePrintType getTemplatePrintType() {
		return templatePrintType;
	}
	public void setTemplatePrintType(TemplatePrintType templatePrintType) {
		this.templatePrintType = templatePrintType;
	}
	public Integer getTemplateChoose() {
		return templateChoose;
	}
	public void setTemplateChoose(Integer templateChoose) {
		this.templateChoose = templateChoose;
	}
	public Boolean getqRcodeState() {
		return qRcodeState;
	}
	public void setqRcodeState(Boolean qRcodeState) {
		this.qRcodeState = qRcodeState;
	}
	public Boolean getOrderIdState() {
		return orderIdState;
	}
	public void setOrderIdState(Boolean orderIdState) {
		this.orderIdState = orderIdState;
	}
	public Boolean getPrintImageState() {
		return printImageState;
	}
	public void setPrintImageState(Boolean printImageState) {
		this.printImageState = printImageState;
	}
	public Boolean getBarcodesState() {
		return barcodesState;
	}
	public void setBarcodesState(Boolean barcodesState) {
		this.barcodesState = barcodesState;
	}
 
}
