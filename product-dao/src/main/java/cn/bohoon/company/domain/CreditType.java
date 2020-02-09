package cn.bohoon.company.domain;

/**
 * 信用类型
 */
public enum CreditType {
	ACQUIRE {
		public String getName() {
			return "获得";
		}
	},
	DEDUCT {
		public String getName() {
			return "扣除余额";
		}
	},
	EMPLOY {
		public String getName() {
			return "使用余额";
		}
	},
	BACK {
		public String getName() {
			return "退还余额";
		}
	},
	ADD {
		public String getName() {
			return "手动添加额度";
		}
	},
	REDUCE {
		public String getName() {
			return "手动减少额度";
		}
	},
	SETTING  {
		public String getName() {
			return "重新设置额度";
		}
	};
	public abstract String getName();
}