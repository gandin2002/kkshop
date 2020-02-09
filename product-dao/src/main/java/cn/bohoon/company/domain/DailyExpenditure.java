package cn.bohoon.company.domain;

/**
 * 日常支出方式
 * @author Administrator
 *
 */

public enum  DailyExpenditure {
	READY_MONEY {
		public String getName() {
			return "现金";
		}
	},
	TRANSFER_ACCOUNTS {
		public String getName() {
			return "转账";
		}
	},
	CHEQUE {
		public String getName() {
			return "支票";
		}
	},
	THIRD_PARTY_PAYMEN {
		public String getName() {
			return "第三方支付";
		}
	},
	ELSE {
		public String getName() {
			return "其他";
		}
	};
	 
	public abstract String getName();

}
