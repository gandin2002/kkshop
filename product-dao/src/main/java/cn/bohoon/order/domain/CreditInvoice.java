package cn.bohoon.order.domain;

public enum CreditInvoice {

	CREDIT_IS{
		@Override
		public String getName(){
			return "信用支付-可开票";
		}
		
	},
	
	CREDIT_NOT{
		@Override
		public String getName(){
			return "信用支付-不可开票";
		}
		
	};
	

	public abstract String getName();
}
