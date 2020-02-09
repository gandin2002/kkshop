package cn.bohoon.userInfo.domain;

public enum CreditLogType {
		GAIN{
			public String getName(){
				return "获得";
			}
		},DEDUCT{
			public String getName(){
				return "减去";
			}
		},USE{
			public String getName(){
				return "使用";
			}
		},RETURN{
			public String getName(){
				return "退还";
			}
		};
	public abstract String getName();
}
