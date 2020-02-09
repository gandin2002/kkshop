package cn.bohoon.company.domain;


/**
 * 合约审核状态
 * @author Administrator
 *
 */

public enum CompanyConcatState {

	
	INIT {
		public String getName() {
			return "审核中";
		}
	},
	PASS {
		public String getName() {
			return "审核通过";
		}
	},
	UNPASS {
		public String getName() {
			return "审核未通过";
		}
	},
	NOTAPLLY {
		public String getName() {
			return "未申请";
		}
	};
	public abstract String getName();
}
