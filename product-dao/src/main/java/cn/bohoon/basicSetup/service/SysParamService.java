package cn.bohoon.basicSetup.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.bohoon.basicSetup.dao.SysParamDao;
import cn.bohoon.basicSetup.domain.SysParamType;
import cn.bohoon.basicSetup.entity.SysParam;
import cn.bohoon.framework.service.BaseService;

@Service
@Transactional
public class SysParamService extends BaseService<SysParam, Integer> {

	@Autowired
	SysParamDao sysParamDao;

	@Autowired
	SysParamService(SysParamDao sysParamDao) {
		super(sysParamDao);
	}

	/**
	 * 查找参数
	 * 
	 * @param sysParamType
	 * @return 返回Map 集合
	 */
	public Map<String, String> findParamMap(SysParamType sysParamType) {
		Map<String, String> paramMap = new HashMap<>();
		List<SysParam> paramList = sysParamDao.findAll(" from SysParam where sysParamType = ? ", sysParamType);
		for (SysParam sysParam : paramList) {
			paramMap.put(sysParam.getCode(), sysParam.getValue());
		}
		return paramMap;
	}
	
	/**
	 *  获得Code List
	 * @param sysParamType
	 * @return
	 */
	public List<SysParam> findCodeList(SysParamType sysParamType){
		List<SysParam> paramList = sysParamDao.findAll(" from SysParam where sysParamType = ? and page_show=1", sysParamType);

		return paramList;
	}

	/**
	 * 查找参数
	 * 
	 * @param sysParamType
	 * @return 返回SysParam 
	 */
	public SysParam findParam(String code,SysParamType sysParamType) {
		return sysParamDao.select(" from SysParam where sysParamType = ?  and code = ? ", sysParamType,code);
	}

}
