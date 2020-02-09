package cn.bohoon.interfaces.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.springframework.util.StringUtils;

import cn.bohoon.framework.SpringContextHolder;
import cn.bohoon.interfaces.domain.DataTransWay;
import cn.bohoon.interfaces.service.ErpLinkService;
import cn.bohoon.interfaces.service.SyncModuleService;

@Entity
@Table(name = "t_sync_group")
public class SyncGroup {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Integer id; //同步组ID
	String groupName ;//同步组名称
	Integer moduleId; // 模块ID
	String mallTable; // 商城表名称
	String mallWhereCondition; //商城同步到第三方平台的数据查询条件
	@Enumerated(EnumType.STRING)
	DataTransWay transWay; // 传输方式
	String erpId;     //ERP数据源ID
	String erpTable;  // ERP表名称.
	String erpWhereCondition ; //第三方ERP 平台同步到商城上的数据查询条件
	@Column(columnDefinition="tinyint(2)")
	Boolean status = false ;
	@Column(columnDefinition="text")
	String laterSql = ""; //最后执行修正的SQL
	Integer sort;//排序
	
	
	@Transient
	public SyncModule getSyncModule() {
		SyncModuleService service = SpringContextHolder.getBean(SyncModuleService.class);
		if (!StringUtils.isEmpty(moduleId)) {
			return service.get(moduleId);
		}
		return null;
	}
	
	@Transient
	public ErpLink getErpLink() {
		ErpLinkService service = SpringContextHolder.getBean(ErpLinkService.class);
		if (!StringUtils.isEmpty(erpId)) {
			return service.get(erpId);
		}
		return null;
	}
	
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getGroupName() {
		return groupName;
	}
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	public Integer getModuleId() {
		return moduleId;
	}
	public void setModuleId(Integer moduleId) {
		this.moduleId = moduleId;
	}
	public String getMallTable() {
		return mallTable;
	}
	public void setMallTable(String mallTable) {
		this.mallTable = mallTable;
	}
	public String getErpId() {
		return erpId;
	}
	public void setErpId(String erpId) {
		this.erpId = erpId;
	}
	public String getErpTable() {
		return erpTable;
	}
	public void setErpTable(String erpTable) {
		this.erpTable = erpTable;
	}

	public DataTransWay getTransWay() {
		return transWay;
	}

	public void setTransWay(DataTransWay transWay) {
		this.transWay = transWay;
	}

	public Boolean getStatus() {
		return status;
	}

	public void setStatus(Boolean status) {
		this.status = status;
	}

	public String getMallWhereCondition() {
		return mallWhereCondition;
	}

	public void setMallWhereCondition(String mallWhereCondition) {
		this.mallWhereCondition = mallWhereCondition;
	}

	public String getErpWhereCondition() {
		return erpWhereCondition;
	}

	public void setErpWhereCondition(String erpWhereCondition) {
		this.erpWhereCondition = erpWhereCondition;
	}
	public String getLaterSql() {
		return laterSql;
	}

	public void setLaterSql(String laterSql) {
		this.laterSql = laterSql;
	}

	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}
}
