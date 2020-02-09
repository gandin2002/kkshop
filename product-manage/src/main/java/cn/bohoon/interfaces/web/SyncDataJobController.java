package cn.bohoon.interfaces.web;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.bohoon.framework.orm.domain.Page;
import cn.bohoon.interfaces.entity.SyncGroup;
import cn.bohoon.interfaces.entity.SyncModule;
import cn.bohoon.interfaces.entity.SyncDataJob;
import cn.bohoon.interfaces.service.SyncDataJobService;
import cn.bohoon.interfaces.service.SyncGroupService;
import cn.bohoon.interfaces.service.SyncModuleService;
import cn.bohoon.main.domain.LoginUser;
import cn.bohoon.main.domain.UserSession;
import cn.bohoon.main.system.entity.Operator;
import cn.bohoon.main.system.service.OperatorLogService;
import cn.bohoon.main.system.service.OperatorService;
import cn.bohoon.util.ResultUtil;
import cn.bohoon.util.SyncDataQuartz;

/**
 * 同步任务管理
 * 
 * @author dujianqiao
 *
 */
@Controller
@RequestMapping("syncDataJob")
public class SyncDataJobController {

	@Autowired
	SyncGroupService syncGroupService;
	@Autowired
	SyncDataJobService syncDataJobService;

    @Autowired
    OperatorLogService operatorLogService;
    @Autowired
    OperatorService operatorService;

	
	/**
	 * 同步任务列表
	 * 
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/list")
	public String list(HttpServletRequest request, Model model) {
		Integer pageNo = ServletRequestUtils.getIntParameter(request, "pageNo", 1);
		String jobName = ServletRequestUtils.getStringParameter(request, "jobName", "");
		Page<SyncDataJob> jobPage = new Page<SyncDataJob>();
		jobPage.setPageNo(pageNo);
		String hql = " from SyncDataJob where 1=1 ";
		List<Object> params = new ArrayList<Object>();
		if (!StringUtils.isEmpty(jobName)) {
			hql = hql.concat(" and jobName like ? ");
			params.add('%' + jobName + '%');
			model.addAttribute("jobName", jobName);
		}
		hql = hql.concat(" order by id desc ");
		jobPage = syncDataJobService.page(jobPage, hql, params.toArray());
		model.addAttribute("jobPage", jobPage);
		return "syncRelation/syncDataJobList";
	}
	
	
	
	/**
	 * 去新增同步组
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "add", method = RequestMethod.GET)
	public String addGet( Model model) {
		String hql = " from SyncGroup where status =1 and id not in (select groupId from SyncDataJob) " ;
		List<SyncGroup> syncGroups = syncGroupService.list(hql) ;
		model.addAttribute("syncGroups", syncGroups) ;
		return "syncRelation/syncDataJobAdd";
	}
	
	/**
	 * 新增保存同步任务
	 * 
	 * @param request
	 * @param syncDataJob
	 * @return
	 */
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	@ResponseBody
	public String addPost(HttpServletRequest request, SyncDataJob syncDataJob){
		syncDataJobService.save(syncDataJob);
		try {
			if(!StringUtils.isEmpty(syncDataJob.getCronExpression())) {
				SyncDataQuartz.addJob(syncDataJob);
			}
		} catch (Exception e) {
			return ResultUtil.getError("创建定时任务失败！") ;
		}
		return ResultUtil.getSuccessMsg();
	}
	
	@Autowired
	SyncModuleService syncModuleService;
	
	/**
	 * 去新增同步组
	 * 
	 * @param model
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "edit", method = RequestMethod.GET)
	public String addGet( Model model,Integer id) {
		String hql = " from SyncGroup where status =1 and id not in (select groupId from SyncDataJob) " ;
		List<SyncGroup> syncGroups = syncGroupService.list(hql) ;
		model.addAttribute("syncGroups", syncGroups);
		SyncDataJob syncDataJob = syncDataJobService.get(id) ;
		model.addAttribute("syncDataJob", syncDataJob);
		return "syncRelation/syncDataJobEdit";
	}
	
	
	
	/**
	 *  编辑保存同步任务
	 *  
	 * @param request
	 * @param syncDataJob
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value = "/edit", method = RequestMethod.POST)
	@ResponseBody
	public String editPost(HttpServletRequest request, SyncDataJob syncDataJob) throws Exception{
		SyncDataJob job = syncDataJobService.get(syncDataJob.getId()) ;
		job.setJobName(syncDataJob.getJobName());
		job.setCronExpression(syncDataJob.getCronExpression());
		job.setSceneUrl(syncDataJob.getSceneUrl());
		syncDataJobService.save(job);
		try {
			if(!StringUtils.isEmpty(job.getCronExpression())) {
				SyncDataQuartz.modifyJobTime(job);
			}
		} catch (Exception e) {
			return ResultUtil.getError("调整定时任务失败！") ;
		}

 		//保存日志,HttpServletRequest request
       	LoginUser userif= UserSession.getLoginUser(request);
		Operator operator = operatorService.findUserByUsername(userif.getUsername());
		operatorLogService.addUserLog(operator, request, "修改同步任务进程:"+job.getJobName());
		return ResultUtil.getSuccessMsg();
	}
	
	/**
	 * 删除同步任务
	 * 
	 * @param request
	 * @param id
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value = "/delete")
	@ResponseBody
	public String delete(HttpServletRequest request, Integer id) throws Exception{
		SyncDataJob job = syncDataJobService.get(id) ;
		try {
			if(!StringUtils.isEmpty(job.getCronExpression())) {
				SyncDataQuartz.removeJob(job);
			}
			syncDataJobService.delete(id);
		} catch (Exception e) {
			return ResultUtil.getError("删除定时任务失败！") ;
		}
		//保存日志,HttpServletRequest request
       	LoginUser userif= UserSession.getLoginUser(request);
		Operator operator = operatorService.findUserByUsername(userif.getUsername());
		operatorLogService.addUserLog(operator, request, "删除同步任务进程:"+job.getJobName());
		return ResultUtil.getSuccessMsg();
	}
	
	@RequestMapping(value="syncDateJobModuleAdd",method=RequestMethod.GET)
	public  String getSyncDateJobModuleAdd(Model model){
		List<SyncModule> smlist = syncModuleService.list();
		model.addAttribute("smlist", smlist);
		return "syncRelation/syncDataJobModuleAdd";
	}
	
	@RequestMapping(value="syncModuleAdd",method=RequestMethod.POST)
	public @ResponseBody String getSyncModuleAdd(SyncDataJob syncDataJob){
		syncDataJobService.save(syncDataJob);
		if(!StringUtils.isEmpty(syncDataJob.getCronExpression())) {
			try {
				SyncDataQuartz.addJob(syncDataJob);
			} catch (Exception e) {
				return  ResultUtil.getError("创建失败");
			}
		}
		return ResultUtil.getSuccessMsg();
	}
	
	
	@RequestMapping(value="syncDataJobModuleEdit",method=RequestMethod.GET)
	public  String getSyncDateJobModuleEdit(Model model,Integer id){
		List<SyncModule> smlist = syncModuleService.list();
		SyncDataJob syncDataJob = syncDataJobService.get(id);
		model.addAttribute("syncDataJob", syncDataJob);
		model.addAttribute("smlist", smlist);
		return "syncRelation/syncDataJobModuleEdit";
	}
}
