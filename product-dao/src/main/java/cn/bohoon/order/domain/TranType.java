package cn.bohoon.order.domain;

/**
 * 配送方式
 */
public enum TranType {
	LOGISTICS {
		public String getName() {
			return "物流运输";
		}
	},
	EXPRESS {
		public String getName() {
			return "快递运输";
		}
	};
	public abstract String getName();
}