package cn.bohoon.product.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.format.annotation.DateTimeFormat;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * 品牌管理
 */
@Entity
@Table(name = "t_brand")
public class Brand {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Integer id; // 主键id
	String erpCode;//Erp Id 
	String name; // 品牌名称Id
	String image; // 图片
	
	@JSONField(serialize=false)
	@Column(columnDefinition="tinyint(2)")
	Boolean status = false; // 是否显示 0-不显示 1-显示
	@Column(columnDefinition = "int default 0")
	Integer sort; // 排序
	
	@Column(columnDefinition = "datetime")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JSONField(serialize=false)
	Date createDate = new Date();//创建时间

	
	public Brand() {
		super();
	}

	public Brand(Integer id, String name) {
		super();
		this.id = id;
		this.name = name;
	}
	
	

	public Brand(Integer id, String name, String image, Boolean status, Integer sort) {
		super();
		this.id = id;
		this.name = name;
		this.image = image;
		this.status = status;
		this.sort = sort;
	}

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

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public Boolean getStatus() {
		return status;
	}

	public void setStatus(Boolean status) {
		this.status = status;
	}

	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getErpCode() {
		return erpCode;
	}

	public void setErpCode(String erpCode) {
		this.erpCode = erpCode;
	}
}