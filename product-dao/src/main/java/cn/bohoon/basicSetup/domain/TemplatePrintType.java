package cn.bohoon.basicSetup.domain;

public enum TemplatePrintType {
	
	PRINT_SHOPPING_LIST{
		public String getName(){
			return "购物清单";
		}
	},PRINT_PACKING_LIST{
		public String getName(){
			return "配货单";
		}
	},PRINT_SHOPPING_AND_PACKING{
		public String getName(){
			return "联合打印配货单与购物单";
		}
	},PRINT_EXCHANGE_LIST{
		public String getName(){
			return "换货货品单";
		}
	},PRINT_EXCHANGE_PACKING_LIST{
		public String getName(){
			return "换货配货单";
		}
	},PRINT_EXCHANGE_AND_PACKING{
		public String getName(){
			return "联合打印换货货品单与换货配货单";
		}
	},SEND_GOODS_NAME{
		@Override
		public String getName() {
			return "发货单";
		}
	},PRINT_COLLECTION_LIST{
		public String getName(){
			return  "门店提货单";
		}
	},PICKING_COLLECTION_LIST{
		public String getName(){
			return "门店配货单";
		}
	},UNLION_COLLECT_LIST{
		public String getName(){
			return "门店联合单";
		}
	};
	public abstract String getName();
	

}
