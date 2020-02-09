package cn.bohoon.product.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.util.StringUtils;

import cn.bohoon.framework.SpringContextHolder;
import cn.bohoon.product.service.AttrGroupService;
import cn.bohoon.product.service.BrandService;

/** 商品分类 */
@Entity
@Table(name = "t_category")
public class Category {
	@Id
	private String id; //id
	private String pid; //父id
	private String name; //分类名称
	@Column(columnDefinition="int default 0")
	private Integer sort = 0; //排序
	private Integer level; //层级
	@Column(columnDefinition="tinyint(2)")
	private Boolean status = false; //推荐状态   0-取消推荐   1-已推荐
	@Column(columnDefinition="tinyint(2)")
	private Boolean display = false; //前台显示  0-不显示    1-显示
	private String brandIds ;//品牌信息
	private Integer attrGoupId ;
	private Integer scorePercentage = 0;
	private String image;
	
	@Column(columnDefinition = "datetime")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date createDate = new Date();//创建时间
	
	
	

	@Transient
	public String getAttrGoupName() {
		String name = "" ;
		if(!StringUtils.isEmpty(attrGoupId)){
			AttrGroupService service = (AttrGroupService) SpringContextHolder.getBean(AttrGroupService.class) ;
			name = service.get(attrGoupId).getName() ;
		}
		return name ; 
	}
	
	@Transient
	public String getBrandNames() {
		if(!StringUtils.isEmpty(brandIds)) {
			BrandService service = SpringContextHolder.getBean(BrandService.class);
			return service.getBrandNames(brandIds) ;
		}
		return "" ;
	}
	 
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getPid() {
		return pid;
	}
	public void setPid(String pid) {
		this.pid = pid;
	}
	
	public Integer getScorePercentage() {
		return scorePercentage;
	}
	
	public void setScorePercentage(Integer scorePercentage) {
		this.scorePercentage = scorePercentage;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public Integer getSort() {
		return sort;
	}
	public Integer getLevel() {
		return level;
	}
	public void setLevel(Integer level) {
		this.level = level;
	}
	public void setSort(Integer sort) {
		this.sort = sort;
	}
	public Boolean getStatus() {
		return status;
	}
	public void setStatus(Boolean status) {
		this.status = status;
	}

	public Boolean getDisplay() {
		return display;
	}
	public void setDisplay(Boolean display) {
		this.display = display;
	}
	
	public String getBrandIds() {
		return brandIds;
	}

	public void setBrandIds(String brandIds) {
		this.brandIds = brandIds;
	}

	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	public Integer getAttrGoupId() {
		return attrGoupId;
	}
	public void setAttrGoupId(Integer attrGoupId) {
		this.attrGoupId = attrGoupId;
	}

	public synchronized final Date getCreateDate() {
		return createDate;
	}

	public synchronized final void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	
}
