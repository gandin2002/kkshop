package cn.bohoon.order.domain;

public enum OrderType {
	
	SHOPPING {
		@Override
		public String getName() {
			return "商城订单";
		}
	},
	SCOREEXCHANGE {
		@Override
		public String getName() {
			return "积分兑换订单";
		}
	};
	
	public abstract String getName();
}
