package cn.bohoon.interfaces.web;

import java.sql.Connection;
import java.sql.Driver;
import java.text.MessageFormat;
import java.util.List;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.bohoon.interfaces.entity.ErpLink;
import cn.bohoon.interfaces.service.ErpLinkService;
import cn.bohoon.interfaces.service.SyncGroupService;
import cn.bohoon.main.domain.LoginUser;
import cn.bohoon.main.domain.UserSession;
import cn.bohoon.main.system.entity.Operator;
import cn.bohoon.main.system.service.OperatorLogService;
import cn.bohoon.main.system.service.OperatorService;
import cn.bohoon.main.util.IDUtil;
import cn.bohoon.util.ResultUtil;

@Controller
@RequestMapping(value = "erpLink")
public class ErpLinkController {

	@Autowired
	ErpLinkService erpLinkService ;
	@Autowired
	SyncGroupService syncGroupService;
    @Autowired
    OperatorLogService operatorLogService;
    @Autowired
    OperatorService operatorService;
	
	/**
	 * ERP 数据源管理
	 * 
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "dataSource", method = RequestMethod.GET)
	public String list(HttpServletRequest request, Model model) {
		String id = ServletRequestUtils.getStringParameter(request, "id", "");
		ErpLink item = null;
		if (!StringUtils.isEmpty(id)) {
			item = erpLinkService.get(id);
		}
		List<ErpLink> list = erpLinkService.list();
		
		model.addAttribute("item", item);
		model.addAttribute("list", list);
		return "erpLink/erpLink";
	}
	
	/**
	 * 保存ERP数据源信息
	 * 
	 * @param request
	 * @param model
	 * @return
	 * @throws Exception 
	 */
	@ResponseBody
    @RequestMapping(value = "save", method = RequestMethod.POST)
    public String addPost(HttpServletRequest request,ErpLink model) throws Exception  {
		if(StringUtils.isEmpty(model.getId())) {
			model.setId(IDUtil.getInstance().getId("DS")) ;
		}
		erpLinkService.save(model);
		//保存日志,HttpServletRequest request
   		LoginUser userif= UserSession.getLoginUser(request);
		Operator operator = operatorService.findUserByUsername(userif.getUsername());
		operatorLogService.addUserLog(operator, request, "新增保存数据源信息:"+model.getSourceName());
		return ResultUtil.getSuccessMsg();
    }
	
	/**
	 * 测试数据库链接
	 * 
	 * @param request
	 * @param model
	 * @return
	 * @throws Exception 
	 */
	@ResponseBody
    @RequestMapping(value = "testConn", method = RequestMethod.POST)
    public String testConn(HttpServletRequest request,ErpLink model) throws Exception  {
		try {
			Driver driver = (Driver) Class.forName(model.getSourceType().getDriver()).newInstance();
			String jdbcUrl = model.getSourceType().urlFormat() ;
			jdbcUrl = MessageFormat.format(jdbcUrl, model.getHostName(),model.getPort()+"",model.getDbName()) ;
			Properties info = new Properties();
	        info.put("user", model.getUsername());
	        info.put("password", model.getPassword());
	        Connection connection = driver.connect(jdbcUrl,info);
	        System.out.println("连接成功！"+connection);
	        connection.close();
		} catch (Exception e) {
			return ResultUtil.getError("连接异常！");
		}
		//保存日志,HttpServletRequest request
   		LoginUser userif= UserSession.getLoginUser(request);
		Operator operator = operatorService.findUserByUsername(userif.getUsername());
		operatorLogService.addUserLog(operator, request, "测试数据库数据源链接");
		return ResultUtil.getMessage("连接成功！") ;
    }
	
	/**
	 * 删除ERP数据源
	 * 
	 * @param id
	 * @return
	 * @throws Exception 
	 */
	@ResponseBody
    @RequestMapping(value = "delete")
    public String delete(String id,HttpServletRequest request) throws Exception  {
		String hql = "select count(1) from SyncGroup where erpId=? " ;
		Long gs = syncGroupService.select(hql, Long.class, id) ;
		if(gs > 0 ) {
			return ResultUtil.getError("该数据源正在使用中，无法删除！") ;
		}
		erpLinkService.delete(id);
		//保存日志,HttpServletRequest request
   		LoginUser userif= UserSession.getLoginUser(request);
		Operator operator = operatorService.findUserByUsername(userif.getUsername());
		operatorLogService.addUserLog(operator, request, "删除数据源信息:id"+id);
		return ResultUtil.getSuccessMsg();
    }
}
