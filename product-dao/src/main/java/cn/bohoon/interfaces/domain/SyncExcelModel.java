package cn.bohoon.interfaces.domain;

import cn.bohoon.excel.util.ExportConfig;
import cn.bohoon.framework.SpringContextHolder;
import cn.bohoon.interfaces.entity.SyncGroup;
import cn.bohoon.product.entity.Product;
import cn.bohoon.product.service.SkuService;

public class SyncExcelModel {
	//同步组名称    模块  商城表  同步数据源 传输方式  启用状态
	
	Integer id; //同步组ID
	@ExportConfig(name = "同步组名称 ")
	String groupName ;//同步组名称
	@ExportConfig(name = "模块")
	String moduleName; // 模块
	@ExportConfig(name = "商城表")
	String mallTable; // 商城表名称
	@ExportConfig(name = "同步数据源")
	String sourceName; 
	@ExportConfig(name = "同步表")
	String erpTable;  // ERP表名称.
	@ExportConfig(name = "传输方式")
	String transWayName; // 传输方式
	@ExportConfig(name = "启用状态")
	String status;//启用状态
	
	public void setParams(SyncGroup syncGroup){
		this.groupName = syncGroup.getGroupName();
		this.moduleName = syncGroup.getSyncModule().getModuleName();
		this.mallTable=syncGroup.getMallTable();
		this.sourceName=syncGroup.getErpLink().getSourceName();
		this.erpTable=syncGroup.getErpTable();
		this.transWayName=syncGroup.getTransWay().getName();
		this.status=syncGroup.getStatus()?"是":"否";
	
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

	public String getModuleName() {
		return moduleName;
	}

	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
	}

	public String getMallTable() {
		return mallTable;
	}

	public void setMallTable(String mallTable) {
		this.mallTable = mallTable;
	}

	public String getSourceName() {
		return sourceName;
	}

	public void setSourceName(String sourceName) {
		this.sourceName = sourceName;
	}

	public String getErpTable() {
		return erpTable;
	}

	public void setErpTable(String erpTable) {
		this.erpTable = erpTable;
	}

	public String getTransWayName() {
		return transWayName;
	}

	public void setTransWayName(String transWayName) {
		this.transWayName = transWayName;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	

	

}
