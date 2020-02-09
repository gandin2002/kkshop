package cn.bohoon.util;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSONArray;

import cn.bohoon.framework.SpringContextHolder;
import cn.bohoon.framework.util.JsonUtil;
import cn.bohoon.interfaces.domain.DataTransWay;
import cn.bohoon.interfaces.domain.SyncType;
import cn.bohoon.interfaces.entity.SyncDataJob;
import cn.bohoon.interfaces.entity.SyncDataLog;
import cn.bohoon.interfaces.entity.SyncGroup;
import cn.bohoon.interfaces.service.SyncDataJobService;
import cn.bohoon.interfaces.service.SyncDataLogService;
import cn.bohoon.interfaces.service.SyncDataService;

public class SyncDataJobExecution implements Job{

	/**
	 *  事件处理
	 */
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		String jobKey  = context.getTrigger().getJobKey()+"" ;
		jobKey = jobKey.replaceAll(SyncDataQuartz.JOB_GROUP_NAME.concat("."), "") ;
		Integer id = ConvertUtils.parseInteger(jobKey) ;
		
		SyncDataJobService service = SpringContextHolder.getBean(SyncDataJobService.class) ;
		SyncDataJob  syncDataJob = service.get(id);
		if(!StringUtils.isEmpty(syncDataJob.getModuleId())){
			syncDataList(syncDataJob.getGroupList(), syncDataJob);
		}else{
			syncData(id) ;
		}
	}
	
	
	/**
	 * 模块同步数据
	 * 
	 * @param group
	 */
	public static void syncDataList(List<SyncGroup> syncgrouplist,SyncDataJob job,Object... object) {
		SyncDataJobService service = SpringContextHolder.getBean(SyncDataJobService.class) ;
		ThreadPoolTaskExecutor executor = SpringContextHolder.getBean(ThreadPoolTaskExecutor.class) ;
		SyncDataLogService logService = SpringContextHolder.getBean(SyncDataLogService.class) ;
		SyncDataService syncService = SpringContextHolder.getBean(SyncDataService.class) ;
		
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				for (SyncGroup group : syncgrouplist) {
					SyncDataLog syLog = new SyncDataLog() ;
					syLog.setSyncType(SyncType.AUTO_SYNC);
					syLog.setGroupName(group.getGroupName());
					logService.save(syLog);
					job.setLastLogId(syLog.getId());
					job.setLastExecute(syLog.getSyncTime());
					
					List<String> errorList = new ArrayList<String>() ;
					if(group.getTransWay().equals(DataTransWay.r)) {
						List<String> readErrorList = new ArrayList<String>() ;
						readErrorList = syncService.readDataSync(group) ;
						errorList.addAll(readErrorList) ;
					} 
					if(group.getTransWay().equals(DataTransWay.w)) {
						List<String> writeErrorList = new ArrayList<String>() ;
						writeErrorList = syncService.writeDataSync(group,object);
						errorList.addAll(writeErrorList) ;
					}
					if(group.getTransWay().equals(DataTransWay.rw)) {
						List<String> readErrorList = new ArrayList<String>() ;
						List<String> writeErrorList = new ArrayList<String>() ;
						readErrorList = syncService.readDataSync(group) ;
						writeErrorList = syncService.writeDataSync(group);
						errorList.addAll(readErrorList) ;
						errorList.addAll(writeErrorList) ;
					}
					
					
					 if(!executeSql(group)){
						 errorList.add("修正SQL 执行错误! ");
					 }
					
					syLog.setErrorSql(JsonUtil.toJson(errorList));
					syLog.setSyncFinishTime(new Date());
					if(errorList.size() >0 ) {
						syLog.setState(false);
					}
					job.setState(syLog.getState());
					service.save(job);
					logService.save(syLog);
				}
			}
		}) ;
		executor.execute(thread);
	}
	
	/**
	 * 同步数据
	 * 
	 * @param group
	 */
	public static void syncData(Integer jobId,Object... object) {
		SyncDataJobService service = SpringContextHolder.getBean(SyncDataJobService.class) ;
		SyncDataJob job = service.get(jobId) ;
		if(StringUtils.isEmpty(job)) {
			return ;
		}
		SyncGroup group = job.getGroup();
		if (StringUtils.isEmpty(group) || !group.getStatus()) {
			return ;
		}
		ThreadPoolTaskExecutor executor = SpringContextHolder.getBean(ThreadPoolTaskExecutor.class) ;
		SyncDataLogService logService = SpringContextHolder.getBean(SyncDataLogService.class) ;
		SyncDataService syncService = SpringContextHolder.getBean(SyncDataService.class) ;
		
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
					SyncDataLog syLog = new SyncDataLog() ;
					syLog.setSyncType(SyncType.AUTO_SYNC);
					syLog.setGroupName(group.getGroupName());
					logService.save(syLog);
					job.setLastLogId(syLog.getId());
					job.setLastExecute(syLog.getSyncTime());
					
					List<String> errorList = new ArrayList<String>() ;
					if(group.getTransWay().equals(DataTransWay.r)) {
						List<String> readErrorList = new ArrayList<String>() ;
						readErrorList = syncService.readDataSync(group) ;
						errorList.addAll(readErrorList) ;
					} 
					if(group.getTransWay().equals(DataTransWay.w)) {
						List<String> writeErrorList = new ArrayList<String>() ;
						writeErrorList = syncService.writeDataSync(group,object);
						errorList.addAll(writeErrorList) ;
					}
					if(group.getTransWay().equals(DataTransWay.rw)) {
						List<String> readErrorList = new ArrayList<String>() ;
						List<String> writeErrorList = new ArrayList<String>() ;
						readErrorList = syncService.readDataSync(group) ;
						writeErrorList = syncService.writeDataSync(group);
						errorList.addAll(readErrorList) ;
						errorList.addAll(writeErrorList) ;
					}
					
					 if(!executeSql(group)){
						 errorList.add("修正SQL 执行错误! ");
					 }
					
					syLog.setErrorSql(JsonUtil.toJson(errorList));
					syLog.setSyncFinishTime(new Date());
					if(errorList.size() >0 ) {
						syLog.setState(false);
					}
					job.setState(syLog.getState());
					service.save(job);
					logService.save(syLog);
				}
		}) ;
		executor.execute(thread);
	}

	public static Boolean executeSql(SyncGroup group){
		try {
			JdbcTemplate jdbcTemplate = SpringContextHolder.getBean(JdbcTemplate.class);
			JSONArray jsonArray = JSONArray.parseArray(group.getLaterSql());
			if(jsonArray != null){
				for (Object object : jsonArray) {
					 String sql= object.toString();
					 if(sql != null){
						 jdbcTemplate.execute(sql);
					 }
				}
			}
		} catch (Exception e) {
			
			return false;
		}
		return true;
	}
}
