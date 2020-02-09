package cn.bohoon.company.domain;

/**
 * 企业规模
 * 
 * @author Administrator
 *
 */
public enum CompanyScaleEnum {
	
	LESS1 {
		@Override
		public String getName() {
			return "20人以下";
		}
	},
	LESS2 {
		@Override
		public String getName() {
			return "20-99人";
		}
	},
	LESS3 {
		@Override
		public String getName() {
			return "100-300人";
		}
	},
	LESS4 {
		@Override
		public String getName() {
			return "301-1000人";
		}
	},
	LESS5 {
		@Override
		public String getName() {
			return "1001-5000人";
		}
	},
	LESS6 {
		@Override
		public String getName() {
			return "5000人以上";
		}
	};

	public abstract String getName();
}
