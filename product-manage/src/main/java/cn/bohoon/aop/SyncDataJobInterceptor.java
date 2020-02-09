package cn.bohoon.aop;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import cn.bohoon.basicSetup.helper.SysParamCache;
import cn.bohoon.interfaces.entity.SyncDataJob;
import cn.bohoon.interfaces.service.SyncDataJobService;
import cn.bohoon.util.SyncDataJobExecution;

public class SyncDataJobInterceptor extends HandlerInterceptorAdapter {

	final String POST_METHOD = "POST" ;
	final String REQUEST_ADD = "add" ;
	final String REQUEST_SAVE = "save" ;
	final String REQUEST_EDIT = "edit" ;
	@Autowired
	SyncDataJobService syncDataJobService;
	
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
		// TODO Auto-generated method stub
		String url= request.getRequestURI() ;
		String method = request.getMethod() ;
		String lowerUrl = url.toLowerCase() ;
		if(url.equals("/syncData/saveOrder")||url.equals("/syncData/saveUserOrder")){
			method = "";
		}
		if(POST_METHOD.equals(method)) {
			if(lowerUrl.contains(REQUEST_SAVE)||lowerUrl.contains(REQUEST_EDIT) || lowerUrl.contains(REQUEST_ADD)) {
				String hql = " from SyncDataJob job where job.sceneUrl like ? " ;
				List<Object> params = new ArrayList<Object>();
				params.add('%' + url+ '%');
				List<SyncDataJob> jobs = syncDataJobService.list(hql, params.toArray()) ;
				for(SyncDataJob job : jobs) {
//					if(!StringUtils.isEmpty(job.getModuleId())){
//						SyncDataJobExecution.syncDataList(job.getGroupList(), job);
//					}else{
//						SyncDataJobExecution.syncData(job.getId());
//					}
				}
			}
		}
		String view = null != modelAndView ?modelAndView.getViewName() :"" ;
		
		if(null != modelAndView && !view.contains("redirect:")){
			modelAndView.addAllObjects(SysParamCache.getCache().getSysParamsV()) ;
		}
		
		
	}

}
