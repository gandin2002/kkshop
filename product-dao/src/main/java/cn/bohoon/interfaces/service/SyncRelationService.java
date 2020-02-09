package cn.bohoon.interfaces.service;

import static cn.bohoon.interfaces.constant.SyncConstants.COMMA;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSONObject;

import cn.bohoon.framework.service.BaseService;
import cn.bohoon.framework.util.CipherUtil;
import cn.bohoon.framework.util.DateUtil;
import cn.bohoon.interfaces.constant.SyncConstants;
import cn.bohoon.interfaces.dao.SyncRelationDao;
import cn.bohoon.interfaces.entity.ErpLink;
import cn.bohoon.interfaces.entity.SyncGroup;
import cn.bohoon.interfaces.entity.SyncRelation;
import cn.bohoon.util.IDUtil;
import cn.bohoon.util.JDBCTemplateUtil;

@Service
public class SyncRelationService extends BaseService<SyncRelation, Integer> {

	static final String UUID = "UUID";
	static final String DATE = "DATE";
	static final String PASSWORD = "PASSWORD";
	static final String INIT_PASSWORD = "123456";

	static final String AUTO_GROWTH = "AUTO_GROWTH"; // 自动增长
	static final Integer INIT_AUTO_GROWTH = 1;

	@Autowired
	JdbcTemplate jdbcTemplate;
	@Autowired
	SyncRelationDao syncRelationDao;

	Logger logger = LoggerFactory.getLogger(SyncRelationService.class);

	@Autowired
	SyncRelationService(SyncRelationDao syncRelationDao) {
		super(syncRelationDao);
	}

	/**
	 * <p>
	 * 获取 商城同步数据表列字段，对接平台数据表列字段
	 * </p>
	 * <p>
	 * 获取 商城同步数据默认字段，对接平台同步数据默认字段
	 * </p>
	 * 
	 * @param id
	 * @return
	 */
	public JSONObject getGroupCols(Integer id) {
		String hql = " from SyncRelation where groupId=?";
		List<SyncRelation> rls = list(hql, id);
		JSONObject json = new JSONObject();
		Map<String, String> mallThridColMap = new HashMap<String, String>();
		Map<String, String> thirdMallColMap = new HashMap<String, String>();
		Map<String, Object> mallValueMap = new HashMap<String, Object>();
		Map<String, Object> thirdValueMap = new HashMap<String, Object>();

		for (SyncRelation rl : rls) {
			if (!StringUtils.isEmpty(rl.getMallColName())) {
				mallThridColMap.put(rl.getMallColName(), rl.getErpColName());
			}
			if (!StringUtils.isEmpty(rl.getErpColName())) {
				thirdMallColMap.put(rl.getErpColName(), rl.getMallColName());
			}
			if (rl.getIsLink()) {
				json.put(SyncConstants.MALLLINKCOL_KEY, rl.getMallColName());
				json.put(SyncConstants.THIRDLINKCOL_KEY, rl.getErpColName());
			}
			if (!StringUtils.isEmpty(rl.getMallValue())) {
				Object value = rl.getMallValue();
				if (rl.getMallValue().equals(UUID)) {
					value = IDUtil.getMemberId();
				}
				if (rl.getMallValue().equals(DATE)) {
					value = DateUtil.getNowSimple(DateUtil.simple);
				}
				if (rl.getMallValue().equals(PASSWORD)) {
					value = CipherUtil.md5(INIT_PASSWORD);
				}
				if (rl.getMallValue().equals(AUTO_GROWTH)) {
					value = INIT_AUTO_GROWTH;
				}
				mallValueMap.put(rl.getMallColName(), value);
			}
			if (!StringUtils.isEmpty(rl.getThirdValue())) {
				thirdValueMap.put(rl.getErpColName(), rl.getThirdValue());
			}
		}
		json.put(SyncConstants.MALL_DEFAULT_VALUE_MAP, mallValueMap);
		json.put(SyncConstants.THIRD_DEFAULT_VALUE_MAP, thirdValueMap);

		json.put(SyncConstants.MALL_THRID_COLMAP, mallThridColMap);
		json.put(SyncConstants.THIRD_MALL_COLMAP, thirdMallColMap);
		return json;
	}

	/**
	 * 第三方数据源同步到商城
	 * 
	 * @param group
	 */
	public List<String> readDataSync(SyncGroup group) {
		List<String> errorSqlArray = new ArrayList<String>();
		ErpLink elk = group.getErpLink();
		JSONObject json = getGroupCols(group.getId());
		// 商城对接字段为key
		Map<String, Object> mallKeyThirValueMap = json.getJSONObject(SyncConstants.MALL_THRID_COLMAP);
		if (mallKeyThirValueMap.isEmpty()) {
			return errorSqlArray;
		}
		JdbcTemplate erpTemplate = JDBCTemplateUtil.createJDBCTemplate(elk);
		String thirdSql = SyncConstants._SELECT;
		String mallSql = SyncConstants._SELECT;
		List<String> mallCols = new ArrayList<String>(mallKeyThirValueMap.keySet());
		for (int i = 0; i < mallCols.size(); i++) {
			String key = mallCols.get(i);
			String value = mallKeyThirValueMap.get(key) + "";
			if (!StringUtils.isEmpty(key)) {
				mallSql += key.concat(COMMA);
			}
			if (!StringUtils.isEmpty(value)) {
				thirdSql += value.concat(COMMA);
			}
		}
		// 去掉最后1个逗号
		thirdSql = thirdSql.substring(0, thirdSql.length() - 1);
		mallSql = mallSql.substring(0, mallSql.length() - 1);

		thirdSql += SyncConstants.SQL_FROM + group.getErpTable();
		mallSql += SyncConstants.SQL_FROM + group.getMallTable();
		if (!StringUtils.isEmpty(group.getErpWhereCondition())) {
			thirdSql += group.getErpWhereCondition();
		}
		logger.info("thirdSql============" + thirdSql);
		// 查询对接平台 需要同步的表的数据
		List<Map<String, Object>> thirdDataList = erpTemplate.queryForList(thirdSql);

		String mallLinkCol = json.getString(SyncConstants.MALLLINKCOL_KEY);
		String thirdLinkCol = json.getString(SyncConstants.THIRDLINKCOL_KEY);
		for (Map<String, Object> dataMap : thirdDataList) {
			if (!StringUtils.isEmpty(mallLinkCol)) {
				String selMallSql = mallSql + " where " + mallLinkCol + " = '" + dataMap.get(thirdLinkCol) + "' ;";
				logger.info("selMallSql============" + selMallSql);
				List<Map<String, Object>> mallHasList = jdbcTemplate.queryForList(selMallSql);
				if (mallHasList.size() > 0) { // 更新
					Map<String, Object> mallValueMap = json.getJSONObject(SyncConstants.MALL_DEFAULT_VALUE_MAP);
					List<String> mallDefaultCols = new ArrayList<String>(mallValueMap.keySet());
					String updateSql = " update " + group.getMallTable() + " set ";
					for (int i = 0; i < mallCols.size(); i++) {
						String key = mallCols.get(i);
						String thirdKey = mallKeyThirValueMap.get(key) + "";
						if (StringUtils.isEmpty(dataMap.get(thirdKey))) {
							continue;
						}
						String thirdValue = dataMap.get(thirdKey) + "";
						updateSql += key + "='" + thirdValue + "' ,";

					}
					// default value
					for (int i = 0; i < mallDefaultCols.size(); i++) {
						String key = mallDefaultCols.get(i);
						if (StringUtils.isEmpty(key)) {
							continue;
						}
						updateSql += key + "= '" + mallValueMap.get(key) + "' ,";
					}

					if (updateSql.endsWith(COMMA)) {
						updateSql = updateSql.substring(0, updateSql.length() - 1);
					}
					updateSql += " where " + mallLinkCol + " = '" + dataMap.get(thirdLinkCol) + "' ;";
					logger.info("updateSQL=======================" + updateSql);
					try {
						jdbcTemplate.execute(updateSql);
					} catch (Exception e) {
						logger.error("sql execute error =======" + updateSql);
						errorSqlArray.add(updateSql);
					}
				} else { // 新增
					List<String> arrays = insertLocal(dataMap, group);
					if (arrays.size() > 0) {
						errorSqlArray.addAll(arrays);
					}
				}
			} else { // 直接插入
				List<String> arrays = insertLocal(dataMap, group);
				if (arrays.size() > 0) {
					errorSqlArray.addAll(arrays);
				}
			}
		}
		return errorSqlArray;
	}

	/**
	 * 读插入数据
	 * 
	 * @param dataMap
	 * @param group
	 */
	public List<String> insertLocal(Map<String, Object> dataMap, SyncGroup group) {
		List<String> errorSqlArray = new ArrayList<String>();
		JSONObject json = getGroupCols(group.getId());
		// 商城对接字段为key
		Map<String, Object> mallThridColMap = json.getJSONObject(SyncConstants.MALL_THRID_COLMAP);
		Map<String, Object> mallValueMap = json.getJSONObject(SyncConstants.MALL_DEFAULT_VALUE_MAP);
		List<String> mallCols = new ArrayList<String>(mallThridColMap.keySet());
		List<String> mallDefaultCols = new ArrayList<String>(mallValueMap.keySet());
		String insertSQL = " insert into " + group.getMallTable() + " (";
		for (int i = 0; i < mallCols.size(); i++) {
			String key = mallCols.get(i);
			String thirdKey = mallThridColMap.get(key) + "";
			if (StringUtils.isEmpty(key) || StringUtils.isEmpty(dataMap.get(thirdKey))) {
				continue;
			}

			insertSQL += key.concat(COMMA);

		}

		// default value
		for (int i = 0; i < mallDefaultCols.size(); i++) {
			String key = mallDefaultCols.get(i);
			insertSQL += key.concat(COMMA);
		}
		if (insertSQL.endsWith(COMMA)) {
			insertSQL = insertSQL.substring(0, insertSQL.length() - 1);
		}
		insertSQL += " ) values (";
		for (int i = 0; i < mallCols.size(); i++) {
			String key = mallCols.get(i);
			String thirdKey = mallThridColMap.get(key) + "";
			if (StringUtils.isEmpty(key) || StringUtils.isEmpty(dataMap.get(thirdKey))) {
				continue;
			}
			String thirdValue = dataMap.get(thirdKey) + "";
			insertSQL += "'" + thirdValue + "',";
		}

		for (int i = 0; i < mallDefaultCols.size(); i++) {
			String key = mallDefaultCols.get(i);
			String value = mallValueMap.get(key) + "";
			insertSQL += "'" + value + "',";
		}
		if (insertSQL.endsWith(COMMA)) {
			insertSQL = insertSQL.substring(0, insertSQL.length() - 1);
		}
		insertSQL += ") ";
		logger.info("insertSQL=======================" + insertSQL);
		try {
			jdbcTemplate.execute(insertSQL);
		} catch (Exception e) {
			logger.error("sql execute error =======" + insertSQL);
			errorSqlArray.add(insertSQL);
		}
		return errorSqlArray;
	}

	

}
