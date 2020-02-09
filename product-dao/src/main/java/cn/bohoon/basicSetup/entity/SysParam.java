package cn.bohoon.basicSetup.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;

import cn.bohoon.basicSetup.domain.SysParamType;
import cn.bohoon.basicSetup.domain.SysValueType;

@Entity
@Table(name = "t_sys_param")
public class SysParam {

	@Id
	Integer id; // 主键 自增字段
	String display; // 名称
	String value; // 值
	String code; // 参数编码
	String descs; // 描述
	
	@Column(columnDefinition="text")
	String sysOption;// 选项
	@Enumerated(EnumType.STRING)
	SysParamType sysParamType; // 参数类型
	@Enumerated(EnumType.STRING)
	SysValueType sysValueType; // 参数值类型
	
	Boolean page_show = true;    // 是否在页面上显示
	


	public Boolean getPage_show() {
		return page_show;
	}

	public void setPage_show(Boolean page_show) {
		this.page_show = page_show;
	}

	@Transient
	public Object getSysOptionMap() {
		return JSON.parse(this.sysOption);
	}

	@Transient
	public JSONArray getSysListMap() {
		return JSON.parseArray(this.sysOption);
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getDisplay() {
		return display;
	}

	public void setDisplay(String display) {
		this.display = display;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getDescs() {
		return descs;
	}

	public void setDescs(String descs) {
		this.descs = descs;
	}

	public SysParamType getSysParamType() {
		return sysParamType;
	}

	public void setSysParamType(SysParamType sysParamType) {
		this.sysParamType = sysParamType;
	}

	public SysValueType getSysValueType() {
		return sysValueType;
	}

	public void setSysValueType(SysValueType sysValueType) {
		this.sysValueType = sysValueType;
	}

	public String getSysOption() {
		return sysOption;
	}

	public void setSysOption(String sysOption) {
		this.sysOption = sysOption;
	}

}
