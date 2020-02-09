package cn.bohoon.product.entity;



import javax.persistence.Entity;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "t_p_specifications_p")
public class PSpecificationsP {
		
		@Id
		@GeneratedValue(strategy = GenerationType.IDENTITY)
		private Integer id; //主键id
		private String name;//关联规格名称
		private Integer mainPId;//主要货品ID
		private Integer vicePId;//所关联的货品ID
		
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
		public Integer getMainPId() {
			return mainPId;
		}
		public void setMainPId(Integer mainPId) {
			this.mainPId = mainPId;
		}
		public Integer getVicePId() {
			return vicePId;
		}
		public void setVicePId(Integer vicePId) {
			this.vicePId = vicePId;
		}
	

		
	
}
