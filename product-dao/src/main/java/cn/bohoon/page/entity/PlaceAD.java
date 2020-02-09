package cn.bohoon.page.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
/** 广告位管理 */
@Entity
@Table(name = "t_place_ad")
public class PlaceAD {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id; //主键id
	private String location; //位置
	private String image; //图片
	private String url; //图片链接URL
	@Column(columnDefinition="int default 0")
	private Integer sort; //排序
	@Column(columnDefinition="tinyint(2)")
	private Boolean status; //状态   0-未发布   1-已发布
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public Integer getSort() {
		return sort;
	}
	public void setSort(Integer sort) {
		this.sort = sort;
	}
	public Boolean getStatus() {
		return status;
	}
	public void setStatus( Boolean status) {
		this.status = status;
	}

}
