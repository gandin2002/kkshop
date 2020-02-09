package cn.bohoon.distribution.domain;

import java.util.List;

import cn.bohoon.distribution.entity.ExpfeeCity;
import cn.bohoon.distribution.entity.ExpfeeOrder;
import cn.bohoon.distribution.entity.ExpfeeTemplate;

public class ExpInfo {

	ExpfeeTemplate template;
	List<ExpfeeCity> feeCity;
	List<ExpfeeOrder> feeOrder;

	public ExpfeeTemplate getTemplate() {
		return template;
	}

	public void setTemplate(ExpfeeTemplate template) {
		this.template = template;
	}

	public List<ExpfeeCity> getFeeCity() {
		return feeCity;
	}

	public void setFeeCity(List<ExpfeeCity> feeCity) {
		this.feeCity = feeCity;
	}

	public List<ExpfeeOrder> getFeeOrder() {
		return feeOrder;
	}

	public void setFeeOrder(List<ExpfeeOrder> feeOrder) {
		this.feeOrder = feeOrder;
	}

}
