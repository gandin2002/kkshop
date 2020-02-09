package cn.bohoon.product.entity;

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

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSONObject;

import cn.bohoon.framework.SpringContextHolder;
import cn.bohoon.product.domain.AttrParam;
import cn.bohoon.product.service.AttrService;

/**
 * 商品类型
 * 
 * @author Administrator
 *
 */
@Entity
@Table(name = "t_attr_group")
public class AttrGroup {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	Integer id; // 类型ID 自增长

	String name; // 类型名称

	String specsIds; // 规格ID

	@Column(columnDefinition = "tinyint(2)")
	Boolean teamType; // 有无参数组

	@Column(columnDefinition = "text")
	String paramInfo;// 参数信息 json 字符串表示

	@Column(columnDefinition = "datetime(0)")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	Date createTime = new Date() ; // 创建时间

	@Column(columnDefinition = "tinyint(2)")
	Boolean status; // 状态

	@Transient
	List<AttrParam> params;

	@Transient
	public String getSpecsNames() {
		if (!StringUtils.isEmpty(specsIds)) {
			AttrService service = SpringContextHolder.getBean(AttrService.class);
			return service.getAttrNames(specsIds);
		}
		return "";
	}

	public List<AttrParam> getParams() {
		if (!StringUtils.isEmpty(paramInfo)) {
			ArrayList<AttrParam> list = (ArrayList<AttrParam>) JSONObject.parseArray(paramInfo, AttrParam.class);
			return list;
		}
		if (!StringUtils.isEmpty(params)) {
			ArrayList<AttrParam> list = new ArrayList<AttrParam>();
			for (AttrParam pm : params) {
				if (!StringUtils.isEmpty(pm.getParamName())) {
					list.add(pm);
				}
				
			}
			this.params = list;
		}
		return params;
	}

	public void setParams(List<AttrParam> params) {
		if (!StringUtils.isEmpty(params)) {
			ArrayList<AttrParam> list = new ArrayList<AttrParam>();
			for (AttrParam pm : params) {
				if (!StringUtils.isEmpty(pm.getParamName())) {
					list.add(pm);
				}
			}
			this.params = list;
		}
		this.params = params;
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

	public String getSpecsIds() {
		return specsIds;
	}

	public void setSpecsIds(String specsIds) {
		this.specsIds = specsIds;
	}

	public Boolean getTeamType() {
		return teamType;
	}

	public void setTeamType(Boolean teamType) {
		this.teamType = teamType;
	}

	public String getParamInfo() {
		return paramInfo;
	}

	public void setParamInfo(String paramInfo) {
		this.paramInfo = paramInfo;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Boolean getStatus() {
		return status;
	}

	public void setStatus(Boolean status) {
		this.status = status;
	}

}
