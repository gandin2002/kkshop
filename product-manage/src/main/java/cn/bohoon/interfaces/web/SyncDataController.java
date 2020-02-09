package cn.bohoon.interfaces.web;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;

import cn.bohoon.framework.util.JsonUtil;
import cn.bohoon.interfaces.domain.DataTransWay;
import cn.bohoon.interfaces.entity.SyncDataLog;
import cn.bohoon.interfaces.entity.SyncGroup;
import cn.bohoon.interfaces.service.SyncDataJobService;
import cn.bohoon.interfaces.service.SyncDataLogService;
import cn.bohoon.interfaces.service.SyncDataService;
import cn.bohoon.interfaces.service.SyncGroupService;
import cn.bohoon.interfaces.service.SyncRelationService;
import cn.bohoon.userInfo.service.UserInfoService;
import cn.bohoon.util.ResultUtil;
import cn.bohoon.util.SyncDataUtils;

@Controller
@RequestMapping("syncData")
public class SyncDataController {
	@Autowired
	SyncDataService syncDataService ;
	@Autowired
	SyncGroupService syncGroupService ;
	@Autowired
	ThreadPoolTaskExecutor simpleExecutor ;
	@Autowired
	SyncDataLogService syncDataLogService ;
	@Autowired
	SyncRelationService syncRelationService;
	
	@Autowired
	JdbcTemplate jdbcTemplate;
	
	/**
	 * 全体小组手动同步
	 * 
	 * @return
	 */
	@RequestMapping("fromThirdParty")
	@ResponseBody
	public String syncFromThridParty() {
		List<SyncGroup> groups = syncGroupService.list() ;
		for(SyncGroup group : groups ) {
			if(!group.getStatus()) {
				continue ;
			}
			syncData(group);
		}
		return ResultUtil.getSuccessMsg();
	}
	
	/**
	 * 全体小组手动同步模块
	 * @param ModuleId
	 * @return
	 */
	@RequestMapping("FromThridPartyAndmodule")
	public @ResponseBody String syncFromThridPartyAndModule(Integer moduleId){
		List<SyncGroup> list = syncGroupService.list(" from SyncGroup where moduleId = ? and status = 1 order by sort asc", moduleId);
		syncDataList(list);
		return ResultUtil.getSuccessMsg();
	}
	
	
	/**
	 * 立即同步
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping("syncRightNow")
	@ResponseBody
	public String syncRightNow(Integer id) {
		SyncGroup group = syncGroupService.get(id) ;
		if(null != group && group.getStatus()) {
			syncData(group);
		}
			
		return ResultUtil.getSuccessMsg();
	}
	
	/**
	 * 同步模块
	 * @param list
	 */
	public void syncDataList(List<SyncGroup> list) {
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				for (SyncGroup group : list) {
					
					SyncDataLog syLog = new SyncDataLog() ;
					syLog.setGroupName(group.getGroupName());
					syncDataLogService.save(syLog);
					List<String> errorList = new ArrayList<String>() ;
					if(group.getTransWay().equals(DataTransWay.r)) {
						List<String> readErrorList = new ArrayList<String>() ;
						readErrorList = syncDataService.readDataSync(group) ;
						errorList.addAll(readErrorList) ;
					} 
					if(group.getTransWay().equals(DataTransWay.w)) {
						List<String> writeErrorList = new ArrayList<String>() ;
						writeErrorList = syncDataService.writeDataSync(group);
						errorList.addAll(writeErrorList) ;
					}
					if(group.getTransWay().equals(DataTransWay.rw)) {
						List<String> readErrorList = new ArrayList<String>() ;
						List<String> writeErrorList = new ArrayList<String>() ;
						readErrorList = syncDataService.readDataSync(group) ;
						writeErrorList = syncDataService.writeDataSync(group);
						errorList.addAll(readErrorList) ;
						errorList.addAll(writeErrorList) ;
					}
					
			
						
					JSONArray jsonArray = JSONArray.parseArray(group.getLaterSql());
					if(jsonArray != null){
						for (Object object : jsonArray) {
							String sql = object.toString();
							if (sql != null) {
								try {
									jdbcTemplate.execute(sql);

								} catch (Exception e) {
									errorList.add("修正SQL 执行错误! ");
								}
							}
						}
					}

					syLog.setErrorSql(JsonUtil.toJson(errorList));
					syLog.setSyncFinishTime(new Date());
					if(errorList.size() >0 ) {
						syLog.setState(false);
					}
					
					syncDataLogService.save(syLog);
					
				}
			}
		}) ;
		simpleExecutor.execute(thread);
	}
	
	public void syncData(SyncGroup group) {
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				SyncDataLog syLog = new SyncDataLog() ;
				syLog.setGroupName(group.getGroupName());
				syncDataLogService.save(syLog);
				List<String> errorList = new ArrayList<String>() ;
				if(group.getTransWay().equals(DataTransWay.r)) {
					List<String> readErrorList = new ArrayList<String>() ;
					readErrorList = syncDataService.readDataSync(group) ;
					errorList.addAll(readErrorList) ;
				} 
				if(group.getTransWay().equals(DataTransWay.w)) {
					List<String> writeErrorList = new ArrayList<String>() ;
					writeErrorList = syncDataService.writeDataSync(group);
					errorList.addAll(writeErrorList) ;
				}
				if(group.getTransWay().equals(DataTransWay.rw)) {
					List<String> readErrorList = new ArrayList<String>() ;
					List<String> writeErrorList = new ArrayList<String>() ;
					readErrorList = syncDataService.readDataSync(group) ;
					writeErrorList = syncDataService.writeDataSync(group);
					errorList.addAll(readErrorList) ;
					errorList.addAll(writeErrorList) ;
				}
				
		
					
				JSONArray jsonArray = JSONArray.parseArray(group.getLaterSql());
				if(jsonArray != null){
					for (Object object : jsonArray) {
						String sql = object.toString();
						if (sql != null) {
							try {
								jdbcTemplate.execute(sql);

							} catch (Exception e) {
								errorList.add("修正SQL 执行错误! ");
							}
						}
					}
				}

				syLog.setErrorSql(JsonUtil.toJson(errorList));
				syLog.setSyncFinishTime(new Date());
				if(errorList.size() >0 ) {
					syLog.setState(false);
				}
				
				syncDataLogService.save(syLog);
			}
		}) ;
		simpleExecutor.execute(thread);
	}
	@Autowired
	SyncDataJobService syncDataJobService;
	
	@Autowired
	UserInfoService userInfoService;
	
	
	@RequestMapping(value={"saveOrder","saveUserOrder"},method=RequestMethod.POST)
	public @ResponseBody String syncOrder(String orderId,String userId,HttpServletRequest request){
		return SyncDataUtils.syncSoleDate(userId, request.getRequestURI(), orderId);
	}
}
