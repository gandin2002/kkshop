package cn.bohoon.company.domain;

/**
 * 申请账单时间
 * @author Administrator
 *
 */

public enum BillTime {
	
	NOW {
		public String getName() {
			return "现结";
		}
	},
	FIFTEEN {
		public String getName() {
			return "15天";
		}
	},
	MONTHLY {
		public String getName() {
			return "月结";
		}
	},
	QUARTERLY {
		public String getName() {
			return "季度结算";
		}
	},
	SIXMONTHS {
		public String getName() {
			return "6个月结算";
		}
	},
	 NINEMONTHS {
		public String getName() {
			return "9个月结算";
		}
	};
	public abstract String getName();

}
