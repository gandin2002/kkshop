package cn.bohoon.page.entity;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import cn.bohoon.framework.SpringContextHolder;
import cn.bohoon.page.domain.LINKLOC;
import cn.bohoon.page.service.BottomLinkService;

/**
 * PC底部导航链接
 * 
 * @author Administrator
 *
 */
@Entity
@Table(name = "t_bottom_link")
public class BottomLink {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	Integer id;
	@Enumerated(EnumType.STRING)
	LINKLOC loc =LINKLOC.friend ;//页面类型
	Integer pid = 0;
	String name; // 页面配置-商城自定义功能-PC底部导航链接-名称
	String link_url; // 页面配置-商城自定义功能-PC底部导航链接-链接
	String image; // 页面配置-商城自定义功能-PC底部导航链接-展示图
	@Column(columnDefinition="tinyint(2)")
	Boolean status; // 页面配置-商城自定义功能-PC底部导航链接-启用状态
	Integer sort; // 页面配置-商城自定义功能-PC底部导航链接-链接序号
	
	@Transient
	public List<BottomLink> getChildrens(){
		BottomLinkService service = SpringContextHolder.getBean(BottomLinkService.class) ;
		String hql = " from BottomLink where pid =? and status = 1 order by sort" ;
		return service.list(hql,id) ;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public LINKLOC getLoc() {
		return loc;
	}

	public void setLoc(LINKLOC loc) {
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

	public String getLink_url() {
		return link_url;
	}

	public void setLink_url(String link_url) {
		this.link_url = link_url;
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

}
