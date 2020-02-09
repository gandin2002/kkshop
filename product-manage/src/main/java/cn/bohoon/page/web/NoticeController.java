package cn.bohoon.page.web;

import java.util.ArrayList;
import java.util.Date;
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
import cn.bohoon.framework.util.DateUtil;
import cn.bohoon.main.domain.LoginUser;
import cn.bohoon.main.domain.UserSession;
import cn.bohoon.main.system.entity.Operator;
import cn.bohoon.main.system.service.OperatorLogService;
import cn.bohoon.main.system.service.OperatorService;
import cn.bohoon.util.ResultUtil;
import cn.bohoon.page.entity.Notice;
import cn.bohoon.page.service.NoticeService;

@Controller
@RequestMapping("notice")
public class NoticeController {

    @Autowired
    NoticeService noticeService;
    @Autowired
    OperatorLogService operatorLogService;
    @Autowired
    OperatorService operatorService;
 
    
    @RequestMapping(value = "list", method = RequestMethod.GET)
    public String list(HttpServletRequest request, Model model) throws Exception{
    	String title = ServletRequestUtils.getStringParameter(request, "title","");
    	Integer pageNo = ServletRequestUtils.getIntParameter(request, "pageNo", 1);
		String startTime = ServletRequestUtils.getStringParameter(request, "startTime","");
		String endTime = ServletRequestUtils.getStringParameter(request, "endTime","");
    	Page<Notice> pageNotice = new Page<Notice>();
    	pageNotice.setPageNo(pageNo);
    	String hql = " from Notice n where 1=1 ";
    	List<Object> params = new ArrayList<>();
    	if(!StringUtils.isEmpty(title)){
    		hql = hql+" and n.title like ? ";
    		params.add('%'+title+'%');
    		model.addAttribute("title", title);
    	}
    	if (!StringUtils.isEmpty(startTime)){
    		hql = hql + " and n.publishTime >= ? and status = 1 ";
    		params.add(DateUtil.switchStringToDate(startTime, "yy-MM-dd"));
    		model.addAttribute("startTime", startTime);
        }
        if (!StringUtils.isEmpty(endTime)){
        	hql = hql + " and n.publishTime < ? and status = 1 ";
        	params.add(DateUtil.getNDayAfter(endTime, 1));
        	model.addAttribute("endTime", endTime);
        }
        hql = hql + " order by n.id desc ";
    	pageNotice = noticeService.page(pageNotice, hql, params.toArray());
    	model.addAttribute("pageNotice", pageNotice);
    	return "notice/noticeList";
    }
    
    @RequestMapping(value = "add", method = RequestMethod.GET)
    public String addGet(HttpServletRequest request, Model model){
    	return "notice/noticeAdd";
    }
    
    @ResponseBody
    @RequestMapping(value = "add", method = RequestMethod.POST)
    public String addPost(HttpServletRequest request, Notice notice) throws Exception {
    	Date date = new Date();
    	notice.setCreateTime(date);
    	notice.setModifyTime(date);
    	notice.setStatus(false);
        noticeService.save(notice);
      //保存日志,HttpServletRequest request
   		LoginUser userif= UserSession.getLoginUser(request);
		Operator operator = operatorService.findUserByUsername(userif.getUsername());
		operatorLogService.addUserLog(operator, request, "新增公告:"+notice.getTitle());
        return ResultUtil.getSuccessMsg();
    }
    
    @RequestMapping(value = "edit",method=RequestMethod.GET)
    public String editGet(HttpServletRequest request,Model model){
    	Integer id = ServletRequestUtils.getIntParameter(request, "id", -1);
    	Notice notice = noticeService.get(id);
		model.addAttribute("notice",notice);
		return "notice/noticeEdit";
    }
    
    @ResponseBody
    @RequestMapping(value = "edit",method=RequestMethod.POST)
    public String editPost(HttpServletRequest request, Integer id, String title, String content) throws Exception{
    	Notice notice = noticeService.get(id);
    	notice.setTitle(title);
    	notice.setContent(content);
    	notice.setModifyTime(new Date());
    	notice.setStatus(false);
    	noticeService.update(notice);
    	//保存日志,HttpServletRequest request
   		LoginUser userif= UserSession.getLoginUser(request);
		Operator operator = operatorService.findUserByUsername(userif.getUsername());
		operatorLogService.addUserLog(operator, request, "修改公告:"+notice.getTitle());
    	return ResultUtil.getSuccessMsg();
    }
    
    @ResponseBody
    @RequestMapping(value = "delete", method = RequestMethod.GET)
    public String delete(HttpServletRequest request,Integer id) throws Exception{
    	noticeService.delete(id);
    	//保存日志,HttpServletRequest request
   		LoginUser userif= UserSession.getLoginUser(request);
		Operator operator = operatorService.findUserByUsername(userif.getUsername());
		operatorLogService.addUserLog(operator, request, "删除公告:id"+id.toString());
    	return ResultUtil.getSuccessMsg();
    }
    
    @RequestMapping(value = "publish", method = RequestMethod.GET)
    public String modifiedStatus(HttpServletRequest request, Integer id) throws Exception{
    	Notice notice=noticeService.get(id);
    	Boolean status = notice.getStatus();
		if (status) {
			notice.setPublishTime(new Date());
		} 
		notice.setStatus(!status);
		noticeService.update(notice);
		String str = request.getQueryString();
		//保存日志,HttpServletRequest request
   		LoginUser userif= UserSession.getLoginUser(request);
		Operator operator = operatorService.findUserByUsername(userif.getUsername());
		operatorLogService.addUserLog(operator, request, "发布/取消发布公告:id"+id.toString());
    	return "redirect:/notice/list?"+str.substring(str.indexOf("&")+1);
    }
    
}
