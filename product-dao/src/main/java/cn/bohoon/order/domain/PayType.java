package cn.bohoon.order.domain;

/**
 * 付款类型
 */
public enum PayType {
	ORDER {
		public String getName() {
			return "订单付款";
		}
	},
	CREDIT {
		public String getName() {
			return "信用还款";
		}
	},
	DELIVER {
		public String getName() {
			return "运费";
		}
	},
	OTHER {
		public String getName() {
			return "其它费用";
		}
	};
	public abstract String getName();
}