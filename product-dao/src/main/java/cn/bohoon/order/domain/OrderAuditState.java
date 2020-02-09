package cn.bohoon.order.domain;

/**
 * 部门的订单审核状态
 */
public enum OrderAuditState {

	

	/******************** 订单审核状态  *************************/
	WAIT_AUDIT {
		@Override
		public String getName() {
			return "待审核";
		}

		
	},
	FINISH_AUDIT{

		@Override
		public String getName() {
			return "已审核";
		}
		
		
	};
	
	
	

	public abstract String getName();

}
