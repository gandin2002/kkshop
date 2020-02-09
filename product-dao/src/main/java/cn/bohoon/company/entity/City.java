package cn.bohoon.company.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
/*
 * 城市表
 */
@Entity
@Table(name = "t_city")
public class City {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Integer id ;				   //id
	private Integer cityCode;			   //城市编码 
	private String first;                  //首字母
	private String name;                   //名称
 	private String spelling;               //英文名称
 	private Integer type;                  //类型    1 中国    2 省，直辖市   3 市，区
 	private Integer parentId;              //上级id
 	
	public Integer getCityCode() {
		return cityCode;
	}
	public void setCityCode(Integer cityCode) {
		this.cityCode = cityCode;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getFirst() {
		return first;
	}
	public void setFirst(String first) {
		this.first = first;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSpelling() {
		return spelling;
	}
	public void setSpelling(String spelling) {
		this.spelling = spelling;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public Integer getParentId() {
		return parentId;
	}
	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}
}
