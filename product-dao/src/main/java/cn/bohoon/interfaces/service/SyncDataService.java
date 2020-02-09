package cn.bohoon.interfaces.service;

import static cn.bohoon.interfaces.constant.SyncConstants.COMMA;

import java.text.ParseException;
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

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import cn.bohoon.framework.SpringContextHolder;
import cn.bohoon.framework.util.CipherUtil;
import cn.bohoon.framework.util.DateUtil;
import cn.bohoon.interfaces.constant.SyncConstants;
import cn.bohoon.interfaces.entity.ErpLink;
import cn.bohoon.interfaces.entity.SyncGroup;
import cn.bohoon.interfaces.entity.SyncRelation;
import cn.bohoon.util.IDUtil;
import cn.bohoon.util.JDBCTemplateUtil;

@Service
public class SyncDataService {

	static final String UUID = "UUID";
	static final String DATE = "DATE";
	static final String PASSWORD = "PASSWORD";
	static final String INIT_PASSWORD = "123456";

	static final String AUTO_GROWTH = "AUTO_GROWTH"; // 自动增长
	static final Integer INIT_AUTO_GROWTH = 1;
	@Autowired
	JdbcTemplate jdbcTemplate;
	
	Logger logger = LoggerFactory.getLogger(SyncDataService.class);
	
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
		SyncRelationService service = SpringContextHolder.getBean(SyncRelationService.class) ;
		List<SyncRelation> rls = service.list(hql, id);
		JSONObject json = new JSONObject();
		Map<String, String> mallThridColMap = new HashMap<String, String>();
		Map<String, String> thirdMallColMap = new HashMap<String, String>();
		Map<String, Object> mallValueMap = new HashMap<String, Object>();
		Map<String, Object> thirdValueMap = new HashMap<String, Object>();
		Map<String, Object> mallTypeMap = new HashMap<String, Object>();
		Map<String, Object> thirdTypeMap = new HashMap<String, Object>();
		
		List<String> mallParams = new ArrayList<String>();
		List<String> thirdParams = new ArrayList<String>();

		for (SyncRelation rl : rls) {
			json.put("erpTable", rl.getErpTable());
			json.put("mallTable", rl.getMallTable());
			if (!StringUtils.isEmpty(rl.getMallColName())) {
				mallThridColMap.put(rl.getMallColName(), rl.getErpColName());
				mallTypeMap.put(rl.getMallColName(),rl.getMallColType()); // mall字段类型
			}
			if (!StringUtils.isEmpty(rl.getErpColName())) {
				thirdMallColMap.put(rl.getErpColName(), rl.getMallColName());
				thirdTypeMap.put(rl.getErpColName(), rl.getErpColType());//erp字段类型
			}
			if (rl.getIsLink()) {
				if(!mallParams.contains(rl.getMallColName())){
					mallParams.add(rl.getMallColName());
					json.put(SyncConstants.MALLLINKCOL_KEY,mallParams);
				}
				if(!thirdParams.contains(rl.getErpColName())){
					thirdParams.add(rl.getErpColName());
					json.put(SyncConstants.THIRDLINKCOL_KEY,thirdParams);
				}
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
					value = IDUtil.getInstance().randString(10);
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
		
		json.put(SyncConstants.MALL_TYPE_MAP, mallTypeMap);
		json.put(SyncConstants.THIRD_TYPE_MAP, thirdTypeMap);
		
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

		thirdSql += SyncConstants.SQL_FROM +group.getErpTable();
		mallSql += SyncConstants.SQL_FROM + group.getMallTable();
		if (!StringUtils.isEmpty(group.getErpWhereCondition())) {
			thirdSql += group.getErpWhereCondition();
		}
		logger.info("thirdSql============" + thirdSql);
		// 查询对接平台 需要同步的表的数据
		List<Map<String, Object>> thirdDataList = erpTemplate.queryForList(thirdSql);

		JSONArray mallArray = json.getJSONArray(SyncConstants.MALLLINKCOL_KEY);
		JSONArray thirArray = json.getJSONArray(SyncConstants.THIRDLINKCOL_KEY);
		for (Map<String, Object> dataMap : thirdDataList) {
			if (mallArray != null ) {
				String selMallSql = mallSql;
				for (int i = 0; i < mallArray.size(); i++) {
					String str= (String) mallArray.get(i);
					if( i == 0){
						selMallSql += " where " + str +"= '"+dataMap.get(thirArray.get(i))+"'";
						continue;
					} 
					selMallSql+=" and "+str+"= '"+dataMap.get(thirArray.get(i))+"'";
				}
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
					
					
					
					for (int i = 0; i < mallArray.size(); i++) {
						String str= (String) mallArray.get(i);
						if( i == 0){
							updateSql += " where " + str +"= '"+dataMap.get(thirArray.get(i))+"'";
							continue;
						} 
						updateSql+=" and "+str+"= '"+dataMap.get(thirArray.get(i))+"'";
					}
					 
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
				logger.error(e.toString());
				logger.error("sql execute error =======" + insertSQL);
				errorSqlArray.add(insertSQL);
		}
		return errorSqlArray;
	}
	
	/**
	 * 商城同步到第三方数据源
	 * 
	 * @param group
	 */
	public List<String> writeDataSync(SyncGroup group,Object... objects) {
		List<String> errorSqlArray = new ArrayList<String>();
		ErpLink elk = group.getErpLink();
		JSONObject json = getGroupCols(group.getId());
		
	
		
		// 商城对接字段为key
		Map<String, Object> thirdMallColMap = json.getJSONObject(SyncConstants.THIRD_MALL_COLMAP);
		if (thirdMallColMap.isEmpty()) {
			return errorSqlArray;
		}
		JdbcTemplate erpTemplate = JDBCTemplateUtil.createJDBCTemplate(elk);
		String thirdSql = " select ";
		String mallSql = "select ";
		List<String> erpCols = new ArrayList<String>(thirdMallColMap.keySet());
		for (int i = 0; i < erpCols.size(); i++) {
			String key = erpCols.get(i);
			String value = thirdMallColMap.get(key) + "";
			if (!StringUtils.isEmpty(key)) {
				thirdSql += key.concat(COMMA);
			}
			if (!StringUtils.isEmpty(value)) {
				mallSql += value.concat(COMMA);
			}
		}
		// 去掉最后1个逗号
		thirdSql = thirdSql.substring(0, thirdSql.length() - 1);
		mallSql = mallSql.substring(0, mallSql.length() - 1);

		mallSql += " from  " + group.getMallTable();
		thirdSql += " from " +group.getErpTable();
		if (!StringUtils.isEmpty(group.getMallWhereCondition())) {
			mallSql += " "+group.getMallWhereCondition();
		}
		
		if(objects != null){
			for (Object string : objects) {
				if(!mallSql.toLowerCase().contains("where".toLowerCase())){
					mallSql +=" where 1 = 1 ";
				};
				mallSql += string.toString();
			}
		}
		
		List<Map<String, Object>> mallDataList = jdbcTemplate.queryForList(mallSql);
		logger.info("mallDataList.size()====" + mallDataList.size());

		JSONArray mallArray = json.getJSONArray(SyncConstants.MALLLINKCOL_KEY);
		JSONArray thirArray = json.getJSONArray(SyncConstants.THIRDLINKCOL_KEY);
		
		Map<String,Object> mallTypeMap = json.getJSONObject(SyncConstants.MALL_TYPE_MAP);
		Map<String,Object> thirdTypeMap =json.getJSONObject(SyncConstants.THIRD_TYPE_MAP);
		
		String updateSql = "" ;
		try {
			
			for (Map<String, Object> dataMap : mallDataList) {
				if (thirArray != null) {
//					String selThirdSql = thirdSql + " where " + thirdLinkCol + " = '" + dataMap.get(mallLinkCol) + "' ;";
					
					String selThirdSql = thirdSql;
					for (int i = 0; i < thirArray.size(); i++) {
						String str= (String) thirArray.get(i);
						if( i == 0){
							selThirdSql += " where " + str +"= '"+dataMap.get(mallArray.get(i))+"'";
							continue;
						} 
						selThirdSql+=" and "+str+"= '"+dataMap.get(mallArray.get(i))+"'";
					}
					
					
					List<Map<String, Object>> thirdHasList = erpTemplate.queryForList(selThirdSql);
					logger.info("querySQL-----------------" + selThirdSql + "---------------------");
					if (thirdHasList.size() > 0) { // 更新
						Map<String, Object> thirdMallValueMap = json.getJSONObject(SyncConstants.THIRD_DEFAULT_VALUE_MAP);
						List<String> thirdMallDefaultCols = new ArrayList<String>(thirdMallValueMap.keySet());
						
						updateSql = " update " + group.getErpTable() + " set ";
						for (int i = 0; i < erpCols.size(); i++) {
							String key = erpCols.get(i);
							String mallKey = thirdMallColMap.get(key) + "";
							if (StringUtils.isEmpty(dataMap.get(mallKey))) {
								continue;
							}
							
							if("DATETIME".equals(mallTypeMap.get(mallKey))){
								Long datelong  = 0L;
								try {
									if("INT".equals(thirdTypeMap.get(key))){
										datelong = DateUtil.formateDateStrShort(dataMap.get(mallKey).toString());
									}else{
										datelong = DateUtil.formateDateStr(dataMap.get(mallKey).toString());
									}
								} catch (Exception e) {
									datelong  = 0L;
								}
								String thirdValue =datelong.toString() + "";
								updateSql += key +"= '" + thirdValue + "',";
							}else{
								String thirdValue = dataMap.get(mallKey) + "";
								updateSql +=  key +"= '" + thirdValue + "',";
							}
						}
						// default value
						for (int i = 0; i < thirdMallDefaultCols.size(); i++) {
							String key = thirdMallDefaultCols.get(i);
							if (StringUtils.isEmpty(key)) {
								continue;
							}
							updateSql += key + "= '" + thirdMallValueMap.get(key) + "' ,";
						}
						
						if (updateSql.endsWith(COMMA)) {
							updateSql = updateSql.substring(0, updateSql.length() - 1);
						}
						for (int i = 0; i < thirArray.size(); i++) {
							String str= (String) thirArray.get(i);
							if( i == 0){
								updateSql += " where " + str +"= '"+dataMap.get(mallArray.get(i))+"'";
								continue;
							} 
							updateSql+=" and "+str+"= '"+dataMap.get(mallArray.get(i))+"'";
						}
						logger.info("executeSQl-----------------" + updateSql + "---------------------");
						erpTemplate.execute(updateSql);
						updateSql = "" ;
					} else { // 新增
						updateSql = insertThird(dataMap, group);
						
					}
				} else { // 直接插入
					updateSql = insertThird(dataMap, group);
				}
			}
		} catch (Exception e) {
			logger.error(e.toString());
			logger.error("sql execute error =======" + updateSql);
			errorSqlArray.add(updateSql);
		}
		return errorSqlArray;
	}

	
	
	/**
	 * 读插入数据
	 * 
	 * @param dataMap
	 * @param group
	 * @throws ParseException 
	 */
	public String insertThird(Map<String, Object> dataMap, SyncGroup group) {

		JSONObject json = getGroupCols(group.getId());

		JdbcTemplate erpTemplate = JDBCTemplateUtil.createJDBCTemplate(group.getErpLink());
		// 商城对接字段为key
		Map<String, Object> thirdMallColMap = json.getJSONObject(SyncConstants.THIRD_MALL_COLMAP);
		Map<String, Object> thirdValueMap = json.getJSONObject(SyncConstants.THIRD_DEFAULT_VALUE_MAP);
		
		Map<String,Object> mallTypeMap = json.getJSONObject(SyncConstants.MALL_TYPE_MAP);
		Map<String,Object> thirdTypeMap =json.getJSONObject(SyncConstants.THIRD_TYPE_MAP);
		List<String> thirdCols = new ArrayList<String>(thirdMallColMap.keySet());
		List<String> thirdDefaultCols = new ArrayList<String>(thirdValueMap.keySet());
		String insertSQL = " insert into " + group.getErpTable() + " (";
		for (int i = 0; i < thirdCols.size(); i++) {
			String key = thirdCols.get(i);
			String mallKey = thirdMallColMap.get(key) + "";
			if (StringUtils.isEmpty(key) || StringUtils.isEmpty(dataMap.get(mallKey))) {
				continue;
			}
			insertSQL += key.concat(COMMA);
		}
		// default value
		for (int i = 0; i < thirdDefaultCols.size(); i++) {
			String key = thirdDefaultCols.get(i);
			insertSQL += key.concat(COMMA);
		}
		if (insertSQL.endsWith(COMMA)) {
			insertSQL = insertSQL.substring(0, insertSQL.length() - 1);
		}
		insertSQL += " ) values (";
		for (int i = 0; i < thirdCols.size(); i++) {
			String key = thirdCols.get(i);
			String mallKey = thirdMallColMap.get(key) + "";
			if (StringUtils.isEmpty(key) || StringUtils.isEmpty(dataMap.get(mallKey))) {
				continue;
			}
			
			if("DATETIME".equals(mallTypeMap.get(mallKey))){
				Long datelong  = 0L;
				try {
					if("INT".equals(thirdTypeMap.get(key))){
						datelong = DateUtil.formateDateStrShort(dataMap.get(mallKey).toString());
					}else{
						datelong = DateUtil.formateDateStr(dataMap.get(mallKey).toString());
					}
				} catch (Exception e) {
					datelong  = 0L;
				}
				String thirdValue =datelong.toString() + "";
				insertSQL += "'" + thirdValue + "',";
			}else{
				String thirdValue = dataMap.get(mallKey) + "";
				insertSQL += "'" + thirdValue + "',";
			}
		}

		for (int i = 0; i < thirdDefaultCols.size(); i++) {
			String key = thirdDefaultCols.get(i);
			String value = thirdValueMap.get(key) + "";
			insertSQL += "'" + value + "',";
		}
		if (insertSQL.endsWith(COMMA)) {
			insertSQL = insertSQL.substring(0, insertSQL.length() - 1);
		}
		insertSQL += ") ";
		logger.info("insertSQL=======================" + insertSQL);
		erpTemplate.execute(insertSQL);
		return insertSQL ;
	}
}
