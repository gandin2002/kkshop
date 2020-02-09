package cn.bohoon.payment.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import cn.bohoon.framework.util.JsonUtil;
import cn.bohoon.payment.domain.AlipayVo;
import cn.bohoon.payment.domain.WechatPayVo;
//支付类型
@Entity
@Table(name = "t_payment_type")
public class PaymentType {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	Integer id;
	String code;	 //支付编码
	String name;     //支付类型名称
	String display;  //链接地址
	String intro;    //简介
	@Column(columnDefinition="tinyint(2)")
	Boolean isPC;//是否启用PC
	@Column(columnDefinition="tinyint(2)")
	Boolean isWap;//是否启用微信
	@Column(columnDefinition="tinyint(2)")
	Boolean isDefult = false ;//是否默认
	@Column(columnDefinition = "varchar(1000) not null")
	String configJson = "" ;//配置参数
	String reFundPwd = "111111" ; //退款密码
	
	@Transient
	public AlipayVo alipayVo(){
	    return	JsonUtil.parse(configJson, AlipayVo.class);
	}
	
	@Transient
	public WechatPayVo getWxConfigMap(){
	    return	JsonUtil.parse(configJson, WechatPayVo.class);
	}
	
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
	public String getIntro() {
		return intro;
	}
	public void setIntro(String intro) {
		this.intro = intro;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getDisplay() {
		return display;
	}
	public void setDisplay(String display) {
		this.display = display;
	}
	public Boolean getIsPC() {
		return isPC;
	}
	public void setIsPC(Boolean isPC) {
		this.isPC = isPC;
	}
	public Boolean getIsWap() {
		return isWap;
	}
	public void setIsWap(Boolean isWap) {
		this.isWap = isWap;
	}
	public String getConfigJson() {
		return configJson;
	}
	public void setConfigJson(String configJson) {
		this.configJson = configJson;
	}

	public String getReFundPwd() {
		return reFundPwd;
	}

	public void setReFundPwd(String reFundPwd) {
		this.reFundPwd = reFundPwd;
	}

	public Boolean getIsDefult() {
		return isDefult;
	}

	public void setIsDefult(Boolean isDefult) {
		this.isDefult = isDefult;
	}

}
