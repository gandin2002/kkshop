package cn.bohoon.userInfo.domain;

public enum ScoreType {
	MANUALLY_ADD {
		public String getName() {
			return "手动增加";
		}
	},
	MANUALLY_SUB {
		public String getName() {
			return "手动减少";
		}
	},
	MANUALLY_NEW {
		public String getName() {
			return "手动设置新积分";
		}
	},
	BINDING {
		public String getName() {
			return "绑定验证";
		}
	},
	CONCERN {
		public String getName() {
			return "关注积分";
		}
	},
	COMMEND {
		public String getName() {
			return "推荐积分";
		}
	},
	COMMEND_PLACE_AN_ORDER {
		public String getName(){
			return "推荐人下单返积分";
		}
	},
	COMMEND_REFUND_OR_SALES {
		public String getName(){
			return "推荐人退款或退货扣除积分";
		}
	}
	,
	DEDUCT {
		public String getName() {
			return "积分扣减";
		}
	},
	SHARE_ORDER {
		public String getName() {
			return "会员晒单";
		}
	},
	EARLIER_SHARE_ORDER {
		public String getName() {
			return "抢先晒单";
		}
	},
	BUY_GOODS {
		public String getName() {
			return "购物返积分";
		}
	},
	EVALUATION {
		public String getName() {
			return "评价晒单返积分";
		}
	},
	REFUND_OR_SALES {
		public String getName(){
			return "退货或退款退积分";
		}
	},
	EMALI_SUBMIT {
		public String getName(){
			return "邮箱认证返积分";
		}
	},
	PHONE_SUBMIT {
		public String getName(){
			return "手机号认证返积分";
		}
		
	},
	FOLLOW_ON_WECHAT {
		public String getName(){
			return "首次关注公众号获取积分";
		}
	},
	SIGN_IN {
		public String getName() {
			return "签到领积分";
		}
		
	};
	public abstract String getName();

}
