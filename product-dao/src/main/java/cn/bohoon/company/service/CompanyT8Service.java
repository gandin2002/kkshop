package cn.bohoon.company.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;
import chi.data.container.Meta;
import chi.data.container.Row;
import chi.data.container.Table;
import chi.data.service.CAPInteropServiceExSoapStub;
import cn.bohoon.domain.ChiSyncBo;

@Component
public class CompanyT8Service {

	public List<Map<String,Object>> getQueryData(ChiSyncBo bo) {
		List<Map<String,Object>> listMap = new ArrayList<Map<String,Object>>() ;
		int tableIndex = 0 ;
		String selectedFields = "* " ;
		String whereClause = " " ;//条件
		String sortFields = "" ; //排序字段
		int fetchCount = 100000 ;
		boolean isDistinct = false ;
		int logMode = 4 ; 
		try {
			java.net.URL url = new java.net.URL(bo.syncUrl);
			CAPInteropServiceExSoapStub service = new CAPInteropServiceExSoapStub(url, null);
			String result = service.getQueryData(bo.groupId, bo.language, bo.userId, bo.password, bo.language, bo.progId, tableIndex, selectedFields, whereClause, sortFields, fetchCount, isDistinct, logMode) ;
			Table table = Table.loadXml(result);
			int count = null !=table ? table.getRowCount() : 0  ;
			for(int i=0 ;i<count;i++) {
				Map<String,Object> tableMap = new HashMap<String, Object>() ;
				Row row = table.getRow(i) ;
				Meta meta = row.getMeta() ;
				int rc = meta.getCount() ;
				for(int j=0;j<rc;j++) {
					String key = meta.get(j).getName()  ;
					Object value = row.get(meta.get(j).getName()) ;
					tableMap.put(key, value) ;
					listMap.add(tableMap) ;
				}
			}
		} catch (Exception e) {
			
		}
		
		return listMap ; 
	}
}
