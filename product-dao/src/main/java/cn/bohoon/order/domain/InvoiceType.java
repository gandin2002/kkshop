package cn.bohoon.order.domain;

/**
 * 发票信息
 */
public enum InvoiceType {
	ORDINARY {
		public String getName() {
			return "纸质普通发票";
		}
	},
	VALUEADDED {
		public String getName() {
			return "增值税发票";
		}
	},
	NOTNEED {
		public String getName() {
			return "无发票需求";
		}
	};
	public abstract String getName();
}