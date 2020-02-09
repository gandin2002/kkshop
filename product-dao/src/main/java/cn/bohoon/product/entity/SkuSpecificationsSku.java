package cn.bohoon.product.entity;



import javax.persistence.Entity;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "t_sku_specifications_sku")
public class SkuSpecificationsSku {
		
		@Id
		@GeneratedValue(strategy = GenerationType.IDENTITY)
		private Integer id; //主键id
		private String name;//关联规格名称
		private Integer mainSkuId;//主要商品ID
		private Integer viceSkuId;//所关联的商品ID
		
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public Integer getId() {
			return id;
		}
		public void setId(Integer id) {
			this.id = id;
		}
		public Integer getMainSkuId() {
			return mainSkuId;
		}
		public void setMainSkuId(Integer mainSkuId) {
			this.mainSkuId = mainSkuId;
		}
		public Integer getViceSkuId() {
			return viceSkuId;
		}
		public void setViceSkuId(Integer viceSkuId) {
			this.viceSkuId = viceSkuId;
		}
	
		
		
		
		
		
	
}
