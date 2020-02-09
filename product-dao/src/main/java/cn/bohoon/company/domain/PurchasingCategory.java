package cn.bohoon.company.domain;

/**
 * 主要采购品类别
 * @author Administrator
 *
 */

public enum PurchasingCategory {
	
     OFFICE_CON {
		public String getName() {
			return "办公耗材";
		}
	},
     OFFICE_EQUIPMENT {
		public String getName() {
			return "IT办公设备";
		}
	},
     DIGITAL_PRODUCTS {
		public String getName() {
			return "数码产品";
		}
	},
     OFFICE_DAILY {
		public String getName() {
			return "办公日用";
		}
	},
     OFFICE_FURNITURE {
		public String getName() {
			return "办公家具";
		}
	};
	/* NINEMONTHS {
		public String getName() {
			return "礼品(商务礼品、员工福利等)";
		}
	};*/
	public abstract String getName();

}
