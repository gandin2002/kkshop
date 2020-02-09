package cn.bohoon.product.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.format.annotation.DateTimeFormat;
/**
 * 
 * 商品标签
 * @author Administrator
 *
 */
@Entity
@Table(name = "t_product_label")
public class ProductLabel {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id; //标签ID

	private String name; //标签名称

	@Column(columnDefinition="tinyint(2)")
	private Boolean status = true ; //标签状态

	private Integer sort = 0; //标签排序

	@Column(columnDefinition = "datetime(0)")
	@DateTimeFormat( pattern = "yyyy-MM-dd HH:mm:ss" )
	private Date createTime; //创建时间
	
	private String image;// 图片
	
	private Integer  presalesNum; //当标签为热卖时才有此字段
    private Integer  days; //当新品才有天数
    
    
	public Integer getPresalesNum() {
		return presalesNum;
	}

	public void setPresalesNum(Integer presalesNum) {
		this.presalesNum = presalesNum;
	}

	public Integer getDays() {
		return days;
	}

	public void setDays(Integer days) {
		this.days = days;
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

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	
	
}
