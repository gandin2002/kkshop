package cn.bohoon.interfaces.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.format.annotation.DateTimeFormat;

/**
 * 数据同步关系
 * 
 * @author djq
 *
 */
@Entity
@Table(name = "t_sync_relation")
public class SyncRelation {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Integer id;
	Integer groupId ; //同步组ID
	
	String mallTable;// 商城表名称
	String mallColName;// 商城表字段
	String mallColType;// 商城表字段类型

	String erpTable; // ERP表名称
	String erpColName;// ERP表字段名称
	String erpColType; // ERP表字段类型
	@Column(columnDefinition="tinyint(2)")
	Boolean isLink = false ; //是否关联字段
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	Date createTime; // 创建时间
	String mallValue ; //商城默认值
	String thirdValue ; //对接平台

//	@Transient
//	public ErpLink getErpLink() {
//		ErpLinkService service = SpringContextHolder.getBean(ErpLinkService.class);
//		if (!StringUtils.isEmpty(erpId)) {
//			return service.get(erpId);
//		}
//		return null;
//	}
//	
//	@Transient
//	public SyncModule getSyncModule() {
//		SyncModuleService service = SpringContextHolder.getBean(SyncModuleService.class);
//		if (!StringUtils.isEmpty(moduleId)) {
//			return service.get(moduleId);
//		}
//		return null;
//	}

	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	
	public Integer getGroupId() {
		return groupId;
	}

	public void setGroupId(Integer groupId) {
		this.groupId = groupId;
	}
	
	public String getMallTable() {
		return mallTable;
	}

	public void setMallTable(String mallTable) {
		this.mallTable = mallTable;
	}

	public String getMallColName() {
		return mallColName;
	}

	public void setMallColName(String mallColName) {
		this.mallColName = mallColName;
	}

	public String getMallColType() {
		return mallColType;
	}

	public void setMallColType(String mallColType) {
		this.mallColType = mallColType;
	}


	public String getErpTable() {
		return erpTable;
	}

	public void setErpTable(String erpTable) {
		this.erpTable = erpTable;
	}

	public String getErpColName() {
		return erpColName;
	}

	public void setErpColName(String erpColName) {
		this.erpColName = erpColName;
	}

	public String getErpColType() {
		return erpColType;
	}

	public void setErpColType(String erpColType) {
		this.erpColType = erpColType;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Boolean getIsLink() {
		return isLink;
	}

	public void setIsLink(Boolean isLink) {
		this.isLink = isLink;
	}

	public String getMallValue() {
		return mallValue;
	}

	public void setMallValue(String mallValue) {
		this.mallValue = mallValue;
	}

	public String getThirdValue() {
		return thirdValue;
	}

	public void setThirdValue(String thirdValue) {
		this.thirdValue = thirdValue;
	}
}
