package cn.bohoon.order.domain;

/**
 * 付款方式
 */
public enum SettleWay {
	WECHAT {
		public String getName() {
			return "微信支付";
		}
	},
	ALIPAY {
		public String getName() {
			return "支付宝支付";
		}
	},
	CREDIT {	//预留  系统中未使用
		public String getName() {
			return "信用支付";
		}
	},
	ONLINE_BANK{
		public String getName() {
			return "企业网银";
		}
	},
	PAY_OFFLINE {
		public String getName() {
			return "线下支付";
		}
	},
	_DEFAULT {
		public String getName() {
			return "";
		}
	};
	public abstract String getName();
}