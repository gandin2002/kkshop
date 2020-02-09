package cn.bohoon.stock.domain;

public enum PurchaseState {

	INIT {
		@Override
		public String getName() {
			return "待审核";
		}

		@Override
		public String getCode() {
			return "WAIT AUDIT";
		}
	} ,
	PASS {
		@Override
		public String getName() {
			return "审核通过";
		}

		@Override
		public String getCode() {
			return "PASS";
		}
	} ,
	REFUSE {
		@Override
		public String getName() {
			return "审核拒绝";
		}

		@Override
		public String getCode() {
			return "REFUSE";
		}
	} ;
	
	public abstract String getName() ;
	public abstract String getCode() ;
}
