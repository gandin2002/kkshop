package cn.bohoon.userInfo.domain;

/**
 * 用户来源
 * @author HJ
 * 2018年3月22日,下午2:45:31
 */
public enum MemberSource {
	
	WECHAT{
		public String getName(){
			return "微信";
		}
	},PC{
		public String getName(){
			return "PC";
		}
	};

	public abstract String getName();
}
