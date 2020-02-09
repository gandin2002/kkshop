package cn.bohoon.distribution.domain;

public enum ValuationEnum {

	BYQUALITY{
		@Override
		public String getName() {
			return "按重量计算运费";
		}

		@Override
		public String getCode() {
			return "quality";
		}
		
	},
	BYVOLUME{
		@Override
		public String getName() {
			return "按体积计算运费";
		}

		@Override
		public String getCode() {
			return "volume";
		}
		
	},
	BYITEMS{
		@Override
		public String getName() {
			return "按件数计算运费";
		}

		@Override
		public String getCode() {
			return "items";
		}
		
	}
	 
	;
	public abstract String getName();
	public abstract String getCode();
}
