package cn.bohoon.interfaces.web;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.ResultSet;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.bohoon.framework.orm.domain.Page;
import cn.bohoon.framework.util.DateUtil;
import cn.bohoon.framework.util.JsonUtil;
import cn.bohoon.interfaces.entity.ErpLink;
import cn.bohoon.interfaces.entity.SyncGroup;
import cn.bohoon.interfaces.entity.SyncRelation;
import cn.bohoon.interfaces.service.ErpLinkService;
import cn.bohoon.interfaces.service.SyncGroupService;
import cn.bohoon.interfaces.service.SyncModuleService;
import cn.bohoon.interfaces.service.SyncRelationService;
import cn.bohoon.util.ResultUtil;

@Controller
@RequestMapping(value = "syncRelation")
public class SyncRelationController {

	@Autowired
	DataSource dataSource;
	@Value("${b2b.datasource.username}")
	String schemaPattern;
	@Autowired
	ErpLinkService erpLinkService;
	@Autowired
	SyncGroupService syncGroupService ;
	@Autowired
	SyncModuleService syncModuleService;
	@Autowired
	SyncRelationService syncRelationService;

	@RequestMapping(value = "list", method = RequestMethod.GET)
	public String list(HttpServletRequest request, Model model) throws Exception {
		Integer pageNo = ServletRequestUtils.getIntParameter(request, "pageNo", 1);
		String startTime = ServletRequestUtils.getStringParameter(request, "startTime","");
		String endTime = ServletRequestUtils.getStringParameter(request, "endTime","");
		Integer groupId = ServletRequestUtils.getIntParameter(request, "groupId", 0) ;
		
		Page<SyncRelation> page = new Page<SyncRelation>();
		page.setPageNo(pageNo);
		String hql = "from SyncRelation t where groupId="+groupId ;
		 List<Object> params = new ArrayList<>();
    
        
        if (!StringUtils.isEmpty(startTime)){
    		hql = hql + " and t.createTime >= ?";
    		params.add(DateUtil.switchStringToDate(startTime, "yy-MM-dd"));
    		model.addAttribute("startTime", startTime);
        }
        if (!StringUtils.isEmpty(endTime)){
        	hql = hql + " and t.createTime < ?";
        	params.add(DateUtil.getNDayAfter(endTime, 1));
        	model.addAttribute("endTime", endTime);
        }
        
		hql += " order by createTime desc" ;
		page = syncRelationService.page(page, hql,params.toArray());
		model.addAttribute("syncRelationPage", page);
		if(null != groupId) {
			SyncGroup syncGroup = syncGroupService.get(groupId) ;
			model.addAttribute("syncGroup", syncGroup);
		}
		model.addAttribute("groupId", groupId);
		return "syncRelation/syncRelationList";
	}

	@RequestMapping(value = "add", method = RequestMethod.GET)
	public String addGet(Integer groupId, Model model) throws Exception {
		initMallTables(groupId,model) ;
		return "syncRelation/syncRelationAdd";
	}
	
	public void initMallTables(Integer groupId ,Model model) {
		try {
			SyncGroup group = syncGroupService.get(groupId) ;
			String mallTable = group.getMallTable() ;
			String erpTable = group.getErpTable() ;
			List<Map<String, String>> mallCols = getColumns(dataSource.getConnection(), mallTable) ;
			model.addAttribute("mallTable", mallTable) ;
			model.addAttribute("mallCols", mallCols) ;
			
			ErpLink erpLink = erpLinkService.get(group.getErpId()) ;
			List<Map<String, String>> erpCols = getColumns(erpLink, erpTable) ;
			model.addAttribute("erpTable", erpTable) ;
			model.addAttribute("erpCols", erpCols) ;
			model.addAttribute("groupId", groupId);
		} catch (Exception e) {

		}
	}
	
	@RequestMapping(value = "edit", method = RequestMethod.GET)
    public String editGet(HttpServletRequest request,Model model) throws Exception {
        Integer id=ServletRequestUtils.getIntParameter(request, "id",-1);
        SyncRelation item = syncRelationService.get(id) ;
        Integer groupId=ServletRequestUtils.getIntParameter(request, "groupId",item.getGroupId());
		initMallTables(groupId ,model) ;
        model.addAttribute("item", item) ;
        model.addAttribute("groupId", groupId) ;
		
        return "syncRelation/syncRelationEdit";
    }
	
	@RequestMapping(value = "queryMallColName", method = RequestMethod.GET)
	@ResponseBody
	public String queryMallColName(HttpServletRequest request, String tbname) throws Exception {
		Connection conn = dataSource.getConnection();
		List<Map<String, String>> results = getColumns(conn,tbname) ;
		return JsonUtil.toJson(results);
	}
	
	@RequestMapping(value = "queryErpColName", method = RequestMethod.GET)
	@ResponseBody
	public String queryErpColName(HttpServletRequest request, String tbname, String id) throws Exception {
		ErpLink erpLink = erpLinkService.get(id);
		List<Map<String, String>> results = getColumns(erpLink,tbname);
		return JsonUtil.toJson(results);
	}
	
	private List<Map<String, String>> getColumns(ErpLink erpLink,String tbname) throws Exception{
		String dbName = erpLink.getDbName();
		Driver driver = (Driver) Class.forName(erpLink.getSourceType().getDriver()).newInstance();
		String jdbcUrl = erpLink.getSourceType().urlFormat();
		jdbcUrl = MessageFormat.format(jdbcUrl, erpLink.getHostName(), erpLink.getPort() + "", dbName);
		Properties info = new Properties();
		info.put("user", erpLink.getUsername());
		info.put("password", erpLink.getPassword());
		Connection conn = driver.connect(jdbcUrl, info);
		return getColumns(conn,tbname);
	}
	
	private  List<Map<String, String>> getColumns(Connection conn , String tbname) {
		List<Map<String, String>> results = new ArrayList<Map<String, String>>();
		try {
			ResultSet rs = conn.getMetaData().getColumns(null, null, tbname, (String) null);
			while (rs.next()) {
				Map<String, String> result = new HashMap<String, String>();
				String name = rs.getString("COLUMN_NAME");
				String type = rs.getString("TYPE_NAME").toUpperCase();
				result.put(name, type);
				results.add(result);
			}
			conn.close();
		} catch (Exception e) {

		}
		return results ;
	}

	

	@ResponseBody
	@RequestMapping(value = "add", method = RequestMethod.POST)
	public String addPost(HttpServletRequest request, SyncRelation model) {
		if(StringUtils.isEmpty(model.getMallColName()) && StringUtils.isEmpty(model.getErpColName())) {
			return ResultUtil.getError("至少选择一个字段！") ;
		}
		model.setCreateTime(new Date());
		syncRelationService.save(model);
		return ResultUtil.getSuccessMsg();
	}
	
	
	@ResponseBody
    @RequestMapping(value = "/delete")
    public String delete(HttpServletRequest request,Integer id) {
		syncRelationService.delete(id);
		return ResultUtil.getSuccessMsg();
    }
}
