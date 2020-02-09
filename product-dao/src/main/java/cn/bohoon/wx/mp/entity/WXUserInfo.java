package cn.bohoon.wx.mp.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 微信 用户信息
 * @author HJ
 * 2018年3月22日,下午2:09:57
 */
@Entity
@Table(name="t_wx_user_info")
public class WXUserInfo {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	Integer id;
	String openid;	//用户的标识，对当前公众号唯一
	String unionid;	//只有在用户将公众号绑定到微信开放平台帐号后，才会出现该字段。
	String miniOpenid;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getOpenid() {
		return openid;
	}
	public void setOpenid(String openid) {
		this.openid = openid;
	}
	public String getUnionid() {
		return unionid;
	}
	public void setUnionid(String unionid) {
		this.unionid = unionid;
	}
	public String getMiniOpenid() {
		return miniOpenid;
	}
	public void setMiniOpenid(String miniOpenid) {
		this.miniOpenid = miniOpenid;
	}
	@Override
	public String toString() {
		return "WXUserInfo [id=" + id + ", openid=" + openid + ", unionid=" + unionid + ", miniOpenid=" + miniOpenid + "]";
	}
	
	
}
