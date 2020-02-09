package cn.bohoon.userInfo.domain;

public enum IdentityType {

	PHONE {
		public String getName() {
			return "手机号登录";
		}
	},
	EMAIL {
		public String getName() {
			return "邮箱号码登录";
		}
	},
	QQ {
		public String getName() {
			return "QQ登录";
		}
	},
	WECHAT {
		public String getName() {
			return "微信登录";
		}
	},
	WXMINIAPP {
		public String getName() {
			return "微信小程序登录";
		}
	},
	ALIPAY {
		public String getName() {
			return "支付宝登录";
		}
	};
	public abstract String getName();

}
