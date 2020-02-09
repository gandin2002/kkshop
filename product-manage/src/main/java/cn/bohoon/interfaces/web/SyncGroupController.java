package cn.bohoon.interfaces.web;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.alibaba.fastjson.JSONArray;

import cn.bohoon.excel.util.ExcelRead;
import cn.bohoon.framework.orm.domain.Page;
import cn.bohoon.framework.util.JsonUtil;
import cn.bohoon.interfaces.domain.DataTransWay;
import cn.bohoon.interfaces.domain.SyncExcelModel;
import cn.bohoon.interfaces.entity.ErpLink;
import cn.bohoon.interfaces.entity.SyncGroup;
import cn.bohoon.interfaces.entity.SyncModule;
import cn.bohoon.interfaces.service.ErpLinkService;
import cn.bohoon.interfaces.service.SyncGroupService;
import cn.bohoon.interfaces.service.SyncModuleService;
import cn.bohoon.interfaces.service.SyncRelationService;
import cn.bohoon.main.domain.LoginUser;
import cn.bohoon.main.domain.UserSession;
import cn.bohoon.main.system.entity.Operator;
import cn.bohoon.main.system.service.OperatorLogService;
import cn.bohoon.main.system.service.OperatorService;
import cn.bohoon.util.ResultUtil;

@Controller
@RequestMapping(value = "syncGroup")
public class SyncGroupController {

	@Autowired
	DataSource dataSource;
	@Value("${b2b.datasource.username}")
	String schemaPattern;
	
	@Autowired
	ErpLinkService erpLinkService;

	@Autowired
	SyncModuleService syncModuleService;
	@Autowired
	SyncGroupService syncGroupService;
	@Autowired
	SyncRelationService syncRelationService;

    @Autowired
    OperatorLogService operatorLogService;
    @Autowired
    OperatorService operatorService;

	@RequestMapping(value = "list", method = RequestMethod.GET)
	public String list(HttpServletRequest request, Model model) {
		Integer pageNo = ServletRequestUtils.getIntParameter(request, "pageNo", 1);
		Integer moduleId = ServletRequestUtils.getIntParameter(request, "moduleId",0);
		String tableName = ServletRequestUtils.getStringParameter(request, "tableName", "") ;
		Page<SyncGroup> page = new Page<SyncGroup>();
		page.setPageNo(pageNo);
		String hql = "from SyncGroup t where 1=1 ";
		 List<Object> params = new ArrayList<>();
        if(!moduleId.equals(0)) {
        	hql += " and t.moduleId=?" ;
        	params.add(moduleId) ;
        	model.addAttribute("moduleId", moduleId);
        }
        if (!StringUtils.isEmpty(tableName)){
    		hql = hql + " and t.mallTable like ? ";
    		params.add('%'+tableName+'%');
    		model.addAttribute("tableName", tableName);
        }
        
        
		hql += " order by sort asc" ;
		page = syncGroupService.page(page, hql,params.toArray());
		model.addAttribute("syncGroupPage", page);

		List<SyncModule> syncModules = syncModuleService.list();
		model.addAttribute("syncModules", syncModules);
		return "syncRelation/syncGroupList";
	}
	
	
	/**
	 * 去新增同步组
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "add", method = RequestMethod.GET)
	public String addGet( Model model) {
		initMallTables(model) ;
		return "syncRelation/syncGroupAdd";
	}
	
	/**
	 * 初始化本地库，获取本地表
	 * 
	 * @param model
	 */
	public void initMallTables(Model model) {
		try {
			Connection conn = dataSource.getConnection();
			String caLog = conn.getCatalog();
			String type[] = new String[] { "TABLE" };
			ResultSet rs = conn.getMetaData().getTables(caLog, schemaPattern, null, type);
			List<String> tables = new ArrayList<String>();
			while (rs.next()) {
				tables.add(rs.getString("TABLE_NAME"));
			}
			conn.close();
			model.addAttribute("tables", tables);
			
			List<SyncModule> syncModules = syncModuleService.list();
			model.addAttribute("syncModules", syncModules);

			List<ErpLink> erpLinks = erpLinkService.list();
			model.addAttribute("erpLinks", erpLinks);
			
		} catch (Exception e) {

		}
	}
	
	/**
	 * 新增保存
	 * 
	 * @param model
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value = "add", method = RequestMethod.POST)
	@ResponseBody
	public String addPost(SyncGroup model,@RequestParam(name="laterSqls")String laterSqls[],HttpServletRequest request) throws Exception {
		for (int i = 0; i < laterSqls.length; i++) {
			String string = laterSqls[i];
			laterSqls[i] =  string.replace("\\44",",");
		}
		model.setLaterSql(JSONArray.toJSONString(laterSqls));
		syncGroupService.save(model);
		//保存日志,HttpServletRequest request
   		LoginUser userif= UserSession.getLoginUser(request);
		Operator operator = operatorService.findUserByUsername(userif.getUsername());
		operatorLogService.addUserLog(operator, request, "新增同步组:"+model.getGroupName());
		return ResultUtil.getSuccessMsg();
	}
	
	@RequestMapping(value = "changeDs", method = RequestMethod.GET)
	@ResponseBody
	public String changeDs(HttpServletRequest request, String id) throws Exception {
		List<String> tables = getDataSourceTables(id) ;
		return JsonUtil.toJson(tables);
	}
	
	private List<String> getDataSourceTables(String id) {
		List<String> tables = new ArrayList<String>();
		try {
			ErpLink erpLink = erpLinkService.get(id);
			String dbName = erpLink.getDbName();
			String[] type = new String[] { "TABLE" };
			Driver driver = (Driver) Class.forName(erpLink.getSourceType().getDriver()).newInstance();
			String jdbcUrl = erpLink.getSourceType().urlFormat();
			jdbcUrl = MessageFormat.format(jdbcUrl, erpLink.getHostName(), erpLink.getPort() + "", dbName);
			Properties info = new Properties();
			info.put("user", erpLink.getUsername());
			info.put("password", erpLink.getPassword());
			Connection conn = driver.connect(jdbcUrl, info);
			String cataLog = conn.getCatalog();
			ResultSet rs =  null ;
			if(erpLink.getSourceType().getCode().equals("mysql")) {
				rs = conn.getMetaData().getTables(cataLog, dbName, "%", type);
			}
			if(erpLink.getSourceType().getCode().equals("sqlserver")) {
				Statement statement = conn.createStatement() ;
				rs = statement.executeQuery("select name as TABLE_NAME from sysobjects where xtype='U' ") ;
			}
			while (rs.next()) {
				tables.add(rs.getString("TABLE_NAME"));
			}
			conn.close();
		} catch (Exception e) {
		}
		return tables ;
	}
	
	/**
	 * 去编辑页面
	 * 
	 * @param id
	 * @param model
	 * @return
	 */
	@RequestMapping(value="edit" ,method=RequestMethod.GET) 
	public String editGet(Integer id,Model model) {
		SyncGroup group = syncGroupService.get(id) ;
		JSONArray jArray = JSONArray.parseArray(group.getLaterSql());
		if(jArray != null){
			 for (int i = 0; i < jArray.size(); i++) {
					String string = jArray.get(i).toString();
					jArray.set(i, string.replace(",","\\44"));
				}
		}
		model.addAttribute("jArray",jArray);
		model.addAttribute("group", group) ;
		initMallTables(model);
		model.addAttribute("erpTables",getDataSourceTables(group.getErpId()));
		return "syncRelation/syncGroupEdit";
	}
	
	
	/**
	 * 编辑保存
	 * 
	 * @param model
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value = "edit", method = RequestMethod.POST)
	@ResponseBody
	public String editPost(SyncGroup model,@RequestParam(name="laterSqls")String laterSqls[],HttpServletRequest request) throws Exception {
		
		for (int i = 0; i < laterSqls.length; i++) {
			String string = laterSqls[i];
			laterSqls[i] =  string.replace("\\44",",");
		}
		
		
		SyncGroup group = syncGroupService.get(model.getId()) ;
		group.setModuleId(model.getModuleId());
		group.setGroupName(model.getGroupName());
		group.setTransWay(model.getTransWay());
		group.setStatus(model.getStatus());
		group.setErpWhereCondition(model.getErpWhereCondition());
		group.setMallWhereCondition(model.getMallWhereCondition());
		group.setLaterSql(JSONArray.toJSONString(laterSqls));
		syncGroupService.save(group);
		//保存日志,HttpServletRequest request
   		LoginUser userif= UserSession.getLoginUser(request);
		Operator operator = operatorService.findUserByUsername(userif.getUsername());
		operatorLogService.addUserLog(operator, request, "修改保存同步组:"+group.getGroupName());
		return ResultUtil.getSuccessMsg();
	}
	
	/**
	 * 删除同步小组
	 * 
	 * @param request
	 * @param id
	 * @return
	 * @throws Exception 
	 */
	@ResponseBody
    @RequestMapping(value = "/delete")
    public String delete(HttpServletRequest request,Integer id) throws Exception {
		syncGroupService.delete(id);
		syncRelationService.execute("delete from SyncRelation where groupId=?", id) ;
		//保存日志,HttpServletRequest request
   		LoginUser userif= UserSession.getLoginUser(request);
		Operator operator = operatorService.findUserByUsername(userif.getUsername());
		operatorLogService.addUserLog(operator, request, "删除同步组:id"+id.toString());
		return ResultUtil.getSuccessMsg();
    }
	
	
	/**
	 * 状态切换
	 * 
	 * @param request
	 * @param id
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value = "/switchStatus")
	@ResponseBody
	public String switchStatus(HttpServletRequest request, Integer id) throws Exception {
		SyncGroup group = syncGroupService.get(id);
		group.setStatus(!group.getStatus());
		syncGroupService.save(group);
		//保存日志,HttpServletRequest request
   		LoginUser userif= UserSession.getLoginUser(request);
		Operator operator = operatorService.findUserByUsername(userif.getUsername());
		operatorLogService.addUserLog(operator, request, "状态切换同步组:"+group.getGroupName());
		return ResultUtil.getSuccessMsg();
	}
	/**
     * 批量导入页面
     * 
     * @return
     */
    @RequestMapping(value="syncBetch",method=RequestMethod.GET)
    public String betchGet() {
    	
    	return "syncRelation/syncBetch" ;
    }
    
    
    @RequestMapping(value="syncBetch",method=RequestMethod.POST)
    public  String betchPost(HttpServletRequest request,Model model) throws Exception {
    	MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		MultipartFile file = multipartRequest.getFile("file");
		List<SyncExcelModel> result =ExcelRead.fastReadExcel(file.getInputStream(), SyncExcelModel.class) ;
		for (SyncExcelModel syncExcelModel : result) {
			SyncModule module=syncModuleService.select("from SyncModule where moduleName = ?", syncExcelModel.getModuleName());
			ErpLink erpLink=erpLinkService.select("from ErpLink where sourceName = ?", syncExcelModel.getSourceName());
			SyncGroup syncGroup=new SyncGroup();
			syncGroup.setGroupName(syncExcelModel.getGroupName());
			syncGroup.setModuleId(module.getId());
			syncGroup.setMallTable(syncExcelModel.getMallTable());
			syncGroup.setErpId(erpLink.getId());
			syncGroup.setErpTable(syncExcelModel.getErpTable());
			syncGroup.setTransWay(DataTransWay.valueOf(syncExcelModel.getTransWayName()));
			if (syncExcelModel.getStatus().equals("是")) {
				syncGroup.setStatus(true);
			}else{
				syncGroup.setStatus(false);
			}
			syncGroupService.save(syncGroup);
	
		}
		//保存日志,HttpServletRequest request
   		LoginUser userif= UserSession.getLoginUser(request);
		Operator operator = operatorService.findUserByUsername(userif.getUsername());
		operatorLogService.addUserLog(operator, request, "同步组批量导入");
    	return "syncRelation/syncBetch";
    }
    
    /**
     * 新增与修改
     * @param id
     * @param model
     * @return
     */
    @RequestMapping(value={"syncModuleAdd","syncModuleEdit"},method=RequestMethod.GET)
    public String syncModule(Integer id,Model model){
    	if(!StringUtils.isEmpty(id)){
    		SyncModule syncModule = syncModuleService.get(id);
    		model.addAttribute("syncModule", syncModule);
    		return "syncRelation/syncModuleEdit";
    	}
    	return "syncRelation/syncModuleAdd";
    }
    
    /**
     * 新增与修改
     * @param id
     * @param model
     * @return
     */
    @RequestMapping(value={"syncModuleAdd","syncModuleEdit"},method=RequestMethod.POST)
    public @ResponseBody String syncModuleAddOrEdit(SyncModule syncModule){
    	syncModuleService.save(syncModule);
    	return ResultUtil.getSuccessMsg();
    }
    
    
    /**
     * 删除
     * @param id
     * @param model
     * @return
     */
    @RequestMapping(value="syncModuleDelete",method=RequestMethod.GET)
    public @ResponseBody String syncModuleDelete(@RequestParam(name="id",required=true)Integer id){
 
    	List<SyncGroup> sgpList = syncGroupService.list(" from SyncGroup where moduleId = ? ",id);
    	for (SyncGroup syncGroup : sgpList) {
			syncGroup.setModuleId(999999);
		}
    	syncGroupService.saveBatch(sgpList, sgpList.size());
    	
       	syncModuleService.delete(id);
       	
    	return ResultUtil.getSuccessMsg();
    }
    
    /**
     * 修改SyncModule-Sort
     * @param id
     * @param sort
     * @return
     */
    @RequestMapping(value="syncModuleDelete",method=RequestMethod.POST)
    public @ResponseBody String editSyncGroupSort(@RequestParam(value="id",required = true)Integer id ,@RequestParam(value="sort",required=true)Integer sort){
    	SyncGroup syncGroup = syncGroupService.get(id);
//    	syncGroup.setSort(sort);
    	syncGroupService.save(syncGroup);
    	return ResultUtil.getSuccessMsg();
    }
    
}
