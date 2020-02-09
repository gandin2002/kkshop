package cn.bohoon.product.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.format.annotation.DateTimeFormat;

/**
 * 商品计量单位 模型
 *
 */
@Entity
@Table(name = "t_unit")
public class Unit {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id; //id
	private String code; //编码
	private String name; //名称
	private Boolean state;//状态
	private Integer sort = 99;//排序
	
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date createDate = new Date();//创建时间
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public Boolean getState() {
		return state;
	}
	public void setState(Boolean state) {
		this.state = state;
	}
	public Integer getSort() {
		return sort;
	}
	public void setSort(Integer sort) {
		this.sort = sort;
	}
}
