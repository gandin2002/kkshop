package cn.bohoon.company.domain;

/**
 * 月采购量
 * @author Administrator
 *
 */

public enum AmountPurchased {
	
     ONE_THAN {
		public String getName() {
			return "1K以内";
		}
	},
     ONE_TO_FIVE{
		public String getName() {
			return "1K~5K";
		}
	},
     FIVE_TO_ONE_W {
		public String getName() {
			return "5K~1W";
		}
	},
     ONE_W_TO_FIVE_W {
		public String getName() {
			return "1W~5W";
		}
	},
     TEN_THAN {
		public String getName() {
			return "10W以上";
		}
	};
	public abstract String getName();

}
