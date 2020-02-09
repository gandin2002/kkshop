package cn.bohoon.product.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 收藏分类
 */
@Entity
@Table(name = "t_favorite_group")
public class FavoriteGroup {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	Integer id;
	String userId;
	String groupName;
	String SetGroupDate;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getGroupName() {
		return groupName;
	}
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	public String getSetGroupDate() {
		return SetGroupDate;
	}
	public void setSetGroupDate(String setGroupDate) {
		SetGroupDate = setGroupDate;
	}
	
	
	
	
}
