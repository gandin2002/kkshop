package cn.bohoon.company.domain;
/*
 * 账单状态
 */
public enum BillType {
	NOTOUT {
		public String getName() {
			return "未出";
		}
	},
	INPRINT {
		public String getName() {
			return "已出";
		}
	};
	public abstract String getName();
}
