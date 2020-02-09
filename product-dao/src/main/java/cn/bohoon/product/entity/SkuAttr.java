package cn.bohoon.product.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import cn.bohoon.basicSetup.domain.SysParamType;
import cn.bohoon.basicSetup.service.SysParamService;
import cn.bohoon.framework.SpringContextHolder;
/** 商品sku属性 */
@Entity
@Table(name = "t_sku_attr")
public class SkuAttr {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private Integer skuId;  //商品skuId
	private Integer attrId; //属性Id
	private Integer attrValueId; //属性值Id
	private String  attrImage;//属性图片
	
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
	public Integer getAttrId() {
		return attrId;
	}
	public void setAttrId(Integer attrId) {
		this.attrId = attrId;
	}
	public Integer getAttrValueId() {
		return attrValueId;
	}
	public void setAttrValueId(Integer attrValueId) {
		this.attrValueId = attrValueId;
	}
	public String getAttrImage() {
		if(this.attrImage==null){
			SysParamService sysParamService = SpringContextHolder.getBean(SysParamService.class) ;
			if(sysParamService!=null){
				String value= sysParamService.findParam("PRODUCT_DEFAULT_IMAGE",SysParamType.PRODUCT_PARAM).getValue();
				return value;
			}
		}
		
		return attrImage;
	}
	public void setAttrImage(String attrImage) {
		this.attrImage = attrImage;
	}
}
