package cn.bohoon.product.domain;

import java.util.List;

/**
 * 类型参数
 * @author Administrator
 *
 */
public class AttrParam {

//	private String paramgroupName ;
	
	private String paramName ; //参数名称
	
	private String paramMust ; //是否必填项目
	
	private String paramSearch ;//是否过滤条件
	
	private String paramType ; // 参数类型
	 
	private List<String> params;//参数值

//	public String getParamgroupName() {
//		return paramgroupName;
//	}
//
//	public void setParamgroupName(String paramgroupName) {
//		this.paramgroupName = paramgroupName;
//	}

	public String getParamName() {
		return paramName;
	}

	public void setParamName(String paramName) {
		this.paramName = paramName;
	}

	public String getParamMust() {
		return paramMust;
	}

	public void setParamMust(String paramMust) {
		this.paramMust = paramMust;
	}

	public String getParamSearch() {
		return paramSearch;
	}

	public void setParamSearch(String paramSearch) {
		this.paramSearch = paramSearch;
	}

	public String getParamType() {
		return paramType;
	}

	public void setParamType(String paramType) {
		this.paramType = paramType;
	}

	public List<String> getParams() {
		return params;
	}

	public void setParams(List<String> params) {
		this.params = params;
	}

	


	
	
	
}
