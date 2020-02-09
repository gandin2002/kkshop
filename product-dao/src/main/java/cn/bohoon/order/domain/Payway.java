package cn.bohoon.order.domain;

/**
 * 支付方式
 */
public enum Payway {
	PAY_DELIVERY {
		public String getName() {
			return "款到发货";
		}
	},
	DELIVERY_PAY {
		public String getName() {
			return "货到付款";
		}
	},
	PAY_ONLINE {
		public String getName() {
			return "在线支付";
		}
	},
	PAY_OFFLINE {
		public String getName() {
			return "线下支付";
		}
	};
	public abstract String getName();
}