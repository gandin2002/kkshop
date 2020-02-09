package cn.bohoon.userInfo.domain;

/**
 * 领取积分的状态
 * @author Administrator
 *
 */

public enum SignStatus {
	
	
	
	HAVA_SIGNED_IN{
		
		
		public String toString(){
			
			return "已签到";
		}
	},
	
	SIGNED_IN{
		
		public String toString(){
	
			return "签到";
		}
	},
	
	RECEIVE_INTEGRAL{
		
		@Override
		public String toString() {
			return "领取积分";
		}
		
	},
	
	RECEIVED_INTEGRAL{
		
		public String toString(){
			return "已领取积分";
		}
		
	}

	
	
}
