package cn.bohoon.product.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
/** 属性名称 */
@Entity
@Table(name = "t_attr")
public class Attr {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id; //主键id
	private String name;
	private String meno;
	@DateTimeFormat(iso=ISO.DATE_TIME)
	private Date createDate = new Date();//创建时间
	@Enumerated(EnumType.STRING)
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getMeno() {
		return meno;
	}
	public void setMeno(String meno) {
		this.meno = meno;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
}
