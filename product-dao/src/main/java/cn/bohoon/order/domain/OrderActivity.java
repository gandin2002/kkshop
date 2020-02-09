package cn.bohoon.order.domain;

public enum OrderActivity {
	APPLY_SERVICE {
		@Override
		public String getName() {
			return "申请售后！";
		}

	};

	public abstract String getName();
}
