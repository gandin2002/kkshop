package cn.bohoon.message.domain;

public enum MessageNodeType {
	
	REGISTER{
		@Override
		public String getName() {
			return "注册成功";
		}
	},ORDER_PAID {
		@Override
		public String getName() {
			return "付款成功";
		}
	},ORDER_CONFIRM_RECEIPT {
		@Override
		public String getName() {
			return "订单确认收获";
		}
	},AUDIT_NOTICE{
		@Override
		public String getName() {
			// TODO Auto-generated method stub
			return "审核通知";
		}
	},VERIFICATION_NOTICE {
		@Override
		public String getName() {
			// TODO Auto-generated method stub
			return "验证码通知";
		}
	},PLACING_ORDER_SUCCESS {
		@Override
		public String getName() {
			// TODO Auto-generated method stub
			return "下单成功通知";
		}
	},REPAYMENT_NOTICE {
		@Override
		public String getName() {
			// TODO Auto-generated method stub
			return "还款通知";
		}
	},OBLIGATION_NOTICE {
		@Override
		public String getName() {
			// TODO Auto-generated method stub
			return "代付款通知";
		}
	},PAYMENT_FAILURE_NOTICE{
		@Override
		public String getName() {
			// TODO Auto-generated method stub
			return "付款失败通知";
		}
	},CHANGE_PASSWORD_NOTICE{

		@Override
		public String getName() {
			// TODO Auto-generated method stub
			return "修改密码通知";
		}
		
	},COLLECTION_NOTICE{

		@Override
		public String getName() {
			// TODO Auto-generated method stub
			return "收款通知";
		}
		
	},LOGIN_SUCCESSFULLY_NOTICE{
		@Override
		public String getName() {
			// TODO Auto-generated method stub
			return "审核通知";
		}
	},LOGIN_SUCCESS{
		@Override
		public String getName() {
			// TODO Auto-generated method stub
			return "登录成功通知";
		}
	},ORDER_AFTER_NOTICE {
		@Override
		public String getName() {
			// TODO Auto-generated method stub
			return "订单售后通知";
		}
	},ORDER_DELIVER_GOODS {
		@Override
		public String getName() {
			// TODO Auto-generated method stub
			return "发货通知";
		}
	},ORDER_ORDER_REVIEW {
		@Override
		public String getName() {
			// TODO Auto-generated method stub
			return "订单审核通知";
		}
	},MAKING_NOTICE {
		@Override
		public String getName() {
			// TODO Auto-generated method stub
			return "关注成功";
		}
	},NOT_BINDING {
		@Override
		public String getName() {
			// TODO Auto-generated method stub
			return "没有绑定";
		}
	};
	public abstract String getName();
}
