package cn.bohoon.interfaces.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 数据同步模块
 * @author djq
 *
 */
@Entity
@Table(name = "t_sync_module")
public class SyncModule {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Integer id;
	String moduleName;// 模块名称

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getModuleName() {
		return moduleName;
	}

	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
	}

}
