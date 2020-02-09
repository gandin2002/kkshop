package cn.bohoon.product.domain;
/**
 * SKU 库存操作类型
 * @author HJ 
 * 2018年1月15日,上午11:20:03
 */
public enum SkuOperationType {
	MANUALLY_ADD{
		public String  getName(){
			return "手动增加";
		}
	},MANUALLY_SUB{
		public String  getName(){
			return "手动减少";
		}
	},
	MANUALLY_NEW{
		public String  getName(){
			return "手动设置新经验值";
		}
	};
	public abstract String getName();
}
