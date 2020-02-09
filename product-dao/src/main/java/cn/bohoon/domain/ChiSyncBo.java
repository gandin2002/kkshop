package cn.bohoon.domain;

import java.util.Map;

import cn.bohoon.basicSetup.domain.SysParamType;
import cn.bohoon.basicSetup.helper.SysParamHelper;
import cn.bohoon.basicSetup.service.SysParamService;
import cn.bohoon.framework.SpringContextHolder;

public class ChiSyncBo {

	public String syncUrl ;
	public String groupId ;
	public String language ;
	public String userId ;
	public byte[] password ;
	public String progId ;
	
	public ChiSyncBo(String progId){
		SysParamService paramService = SpringContextHolder.getBean(SysParamService.class) ;
		Map<String, String> params = paramService.findParamMap(SysParamType.CHI_SYNC_PARAM) ;
		syncUrl = params.get(SysParamHelper.SYNC_CHI_URL) ;
		groupId = params.get(SysParamHelper.SYNC_CHI_GROUP_ID) ;
		language = params.get(SysParamHelper.SYNC_CHI_LANGUAGE) ;
		userId = params.get(SysParamHelper.SYNC_CHI_USER_ID) ;
		String pwd = params.get(SysParamHelper.SYNC_CHI_PWD) ;
		password = getBytes(pwd);
		this.progId = progId ;
		
	}
	
	
	private byte[] getBytes(String value) {
		try {
			return value.getBytes("utf-8") ;
		} catch (Exception e) {
			
		}
		return null ;
	}
}
