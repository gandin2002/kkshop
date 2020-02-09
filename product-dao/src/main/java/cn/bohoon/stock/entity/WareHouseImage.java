package cn.bohoon.stock.entity;


import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * 发货地址图片管理
 * 
 */
@Entity
@Table(name = "t_warehouse_image")
public class WareHouseImage {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private String imageUrl;
	private Integer wareHouseId; //发货地址Id
	 
	@Transient
	public String getImageBig(){
		String fileExt = imageUrl.substring(imageUrl.lastIndexOf(".")).toLowerCase();
		return imageUrl.replace(fileExt, "_b"+fileExt);
	}
	
	@Transient
	public String getImageSmall(){
		String fileExt = imageUrl.substring(imageUrl.lastIndexOf(".")).toLowerCase();
		return imageUrl.replace(fileExt, "_s"+fileExt);
	}
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getWareHouseId() {
		return wareHouseId;
	}

	public void setWareHouseId(Integer wareHouseId) {
		this.wareHouseId = wareHouseId;
	}

	public String getImageUrl() {
		return imageUrl;
	}
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}




}
