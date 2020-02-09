package cn.bohoon.page.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.hql.internal.ast.tree.FromElement;
import org.springframework.format.annotation.DateTimeFormat;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;

import cn.bohoon.company.entity.CompanyDepartment;
import cn.bohoon.framework.SpringContextHolder;
import cn.bohoon.framework.util.JsonUtil;
import cn.bohoon.page.service.PageNavigationService;
import cn.bohoon.product.entity.Product;

@Entity
@Table(name = "t_page_navigation")
public class PageNavigation implements Comparable<PageNavigation>{

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	Integer id;
	String loc;// pc端 还是微信端
	Integer pid = 0;
	String name; // 目录名称
	String image; // 图片
	Integer level; // 目录级别
	String type; // 筛选分类
	@Column(columnDefinition = "text")
	String content; // 分类内容 json 格式串
	String adImage;// 广告图片
	String adUrl; // 广告图片路径
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	Date createTime; // 创建时间
	Integer sort; // 顺序
	@Column(columnDefinition="tinyint(2)")
	Boolean status; // 启用状态
	
	
	
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getLoc() {
		return loc;
	}

	public void setLoc(String loc) {
		this.loc = loc;
	}

	public Integer getPid() {
		return pid;
	}

	public void setPid(Integer pid) {
		this.pid = pid;
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

	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getAdImage() {
		return adImage;
	}

	public void setAdImage(String adImage) {
		this.adImage = adImage;
	}

	public String getAdUrl() {
		return adUrl;
	}

	public void setAdUrl(String adUrl) {
		this.adUrl = adUrl;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
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

	public void setStatus(Boolean status) {
		this.status = status;
	}
	
	@Override
	@JSONField(serialize=false)
	public boolean equals(Object obj) {
		PageNavigation  p =(PageNavigation) obj;
		return   id.equals(p.id);
	}
	
	@Override
	@JSONField(serialize=false)
	public int hashCode() {
			String str = id.toString() ;
		return str.hashCode();
	}
	
	@Override
	@JSONField(serialize=false)
	public int compareTo(PageNavigation arg0) {
		  int i = this.getLevel() - arg0.getLevel();//先按照等级
	      return i; 
	}

}
