package cn.bohoon.interfaces.web;

import java.sql.Connection;
import java.sql.DriverManager;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;

import cn.bohoon.interfaces.entity.ErpLink;
import cn.bohoon.interfaces.service.ErpLinkService;
import cn.bohoon.main.util.ResultUtil;
import cn.bohoon.util.JDBCTemplateUtil;

@Controller
@RequestMapping(value="sqlConsole")
public class SqlConsoleController {
	
	@Autowired
	ErpLinkService erpLinkService;
	
	JdbcTemplate jdbcTemplate;
	
	@RequestMapping(value="console",method=RequestMethod.GET)
	public String show(Model model){
		List<ErpLink> erpLinks = erpLinkService.list();
		model.addAttribute("erpLinks", erpLinks);
		return "sqlConsole/sqlConsoleConsole";
	}
	
	@RequestMapping(value="testLink",method=RequestMethod.GET)
	public @ResponseBody String testLink(String id){
		ErpLink erpLink= erpLinkService.get(id);
		try {
			String jdbcUrl = erpLink.getSourceType().urlFormat();
			jdbcUrl  = MessageFormat.format(jdbcUrl, erpLink.getHostName(),erpLink.getPort()+"",erpLink.getDbName());
			Properties info = new Properties();
			info.put("user", erpLink.getUsername());
			info.put("password", erpLink.getPassword());
			Connection connection= DriverManager.getConnection(jdbcUrl, info);
			connection.close();
		} catch (Exception e) {
			return ResultUtil.getError("连接异常！");
		}
		return  ResultUtil.getMessage("连接成功");
	}
	
	@RequestMapping(value="executeSql",method=RequestMethod.POST)
	public @ResponseBody String executeSql(String sql,String erpId){
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("msg", "执行成功!");
		try {
			ErpLink erpLink= erpLinkService.get(erpId);
			JdbcTemplate jdbcTemplate = JDBCTemplateUtil.createJDBCTemplate(erpLink);
			if (sql.toUpperCase().startsWith("SELECT")) {
				List<Map<String, Object>> map = jdbcTemplate.queryForList(sql);
				jsonObject.put("data",map);
			}else if(sql.toUpperCase().startsWith("UPDATE")){
				int[] re= jdbcTemplate.batchUpdate(sql);
				List<String> strArray =new ArrayList<String>();
				for (int j : re) {
					strArray.add("已经改变"+j+"行数");
				}
				jsonObject.put("executeResult", strArray);
			}else{
				jdbcTemplate.execute(sql);
			}
		} catch (Exception e) {
			jsonObject.put("msg", "SQL错误!");
		}
		
		return jsonObject.toJSONString();
	}
}
