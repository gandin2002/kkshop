package cn.bohoon.company.domain;

/**
 * 发票类型
 * @author Administrator
 *
 */

public enum InvoiceType {
	
	VAT_SPECIAL_INVOICE {
		public String getName() {
			return "增值税专用发票";
		}
	},
	PU_SPECIAL_INVOICE {
		public String getName() {
			return "普通税普通发票";
		}
	},
	NO_INVOICING {
		public String getName() {
			return "不开票";
		}
	};
	
	public abstract String getName();

}
