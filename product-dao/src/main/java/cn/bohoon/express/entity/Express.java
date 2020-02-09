package cn.bohoon.express.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import cn.bohoon.express.domain.ExpressParams;
import cn.bohoon.framework.util.JsonUtil;

/*
 * 物流管理模板
 */
@Entity
@Table(name = "t_express")
public class Express {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Integer id;				//主键id
	private	Integer logisticsid;    //物流公司id
	private String backgroundImage; //背景图片
	@Column(columnDefinition = "text ",nullable = true)
	private String expressTemplate; //  html 格式
	@Column(columnDefinition = "text ",nullable = true)
	private String expressParams;  // 状态参数 json 格式
 
	@Transient
	public ExpressParams getExpressParamsEdcode(){
		return JsonUtil.parse(this.expressParams, ExpressParams.class);
	}
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getLogisticsid() {
		return logisticsid;
	}
	public void setLogisticsid(Integer logisticsid) {
		this.logisticsid = logisticsid;
	}
	public String getBackgroundImage() {
		return backgroundImage;
	}
	public void setBackgroundImage(String backgroundImage) {
		this.backgroundImage = backgroundImage;
	}
	public String getExpressTemplate() {
		return expressTemplate;
	}
	public void setExpressTemplate(String expressTemplate) {
		this.expressTemplate = expressTemplate;
	}
	public String getExpressParams() {
		return expressParams;
	}
	public void setExpressParams(String expressParams) {
		this.expressParams = expressParams;
	}
	
}
