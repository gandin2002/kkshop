package cn.bohoon.message.domain;

public enum SendType {

	PASSWORD_RESET {
		public String getName() {
			return "重设密码";
		}
	},
	MEMBER_BINDING {
		public String getName() {
			return "会员绑定";
		}
	},
	ORDER_REFUND {
		public String getName() {
			return "订单退款";
		}
	},
	PAYMENT_SUCCESSFUL {
		public String getName() {
			return "付款成功";
		}
	},
	ORDER_SHIPMENTS {
		public String getName() {
			return "订单发货";
		}
	},
	ORDER_MODIFY {
		public String getName() {
			return "订单信息修改";
		}
	},
	AUDIT_RESULT {
		public String getName() {
			return "审核结果通知";
		}
	},
	AFTER_SERVICE {
		public String getName() {
			return "售后服务处理进度通知";
		}
	},
	TAKE_GOODS_INFORM {
		public String getName() {
			return "提货通知";
		}
	},
	TAKE_GOODS_SUCCESS {
		public String getName() {
			return "提货成功";
		}
	},
	DISCOUNT_EXPIRE {
		public String getName() {
			return "优惠卷/礼品卡到期";
		}
	},
	FIND_PASSWORD {
		public String getName() {
			return "找回密码";
		}
	},
	SEND_MESSAGE {
		public String getName() {
			return "自定义消息";
		}
	};
	public abstract String getName();

}
