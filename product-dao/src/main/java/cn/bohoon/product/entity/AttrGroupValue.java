package cn.bohoon.product.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.annotation.JsonFormat.Value;

import cn.bohoon.framework.SpringContextHolder;
import cn.bohoon.product.domain.AttrParam;
import cn.bohoon.product.service.AttrService;

/**
 * 商品类型枚举参数值
 * 
 * @author Administrator
 *
 */
@Entity
@Table(name = "t_attr_group_value")
public class AttrGroupValue {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	Integer id; //自增长ID

	String attrGroupName; // 类型为枚举类型的 名称

	String attrGroupId; // 对应参数组ID
	
	String Value ;//对应的值

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getAttrGroupName() {
		return attrGroupName;
	}

	public void setAttrGroupName(String attrGroupName) {
		this.attrGroupName = attrGroupName;
	}

	public String getAttrGroupId() {
		return attrGroupId;
	}

	public void setAttrGroupId(String attrGroupId) {
		this.attrGroupId = attrGroupId;
	}

	public String getValue() {
		return Value;
	}

	public void setValue(String value) {
		Value = value;
	}

	

}
