package cn.bohoon.order.domain;

/**
 * 结算方式
 */
public enum CashWay {
	CASH {
		public String getName() {
			return "现金";
		}
	},
	BANK_TRANSFER {
		public String getName() {
			return "银行转账";
		}
	},
	_DEFAULT {
		public String getName() {
			return "";
		}
	};
	public abstract String getName();
}