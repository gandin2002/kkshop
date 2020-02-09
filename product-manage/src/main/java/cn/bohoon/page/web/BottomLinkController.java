package cn.bohoon.page.web;



import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.bohoon.main.domain.LoginUser;
import cn.bohoon.main.domain.UserSession;
import cn.bohoon.main.system.entity.Operator;
import cn.bohoon.main.system.service.OperatorLogService;
import cn.bohoon.main.system.service.OperatorService;
import cn.bohoon.page.entity.BottomLink;
import cn.bohoon.page.service.BottomLinkService;
import cn.bohoon.util.ResultUtil;

@Controller
@RequestMapping(value = "bottomLink")
public class BottomLinkController {

    @Autowired
    BottomLinkService bottomLinkService;

    @Autowired
    OperatorLogService operatorLogService;
    @Autowired
    OperatorService operatorService;


    /**
     * 友情链接
     *
     * @param request
     * @param model
     * @return
     */
    @RequestMapping(value = "friend", method = RequestMethod.GET)
    public String friend(HttpServletRequest request,Model model)  {
        List<BottomLink> links  =bottomLinkService.selectAllBySort() ;

        model.addAttribute("links", links);
        return "bottomLink/friendLinkList";
    }
    
    @RequestMapping(value = "add", method = RequestMethod.GET)
    public String addGet(HttpServletRequest request,Model model,String loc)  {
    	Integer pid=ServletRequestUtils.getIntParameter(request, "pid",0);
    	if(pid!=0) {
    		BottomLink item =bottomLinkService.get(pid);
    		model.addAttribute("item",item);
    	}
    	model.addAttribute("pid", pid) ;
    	model.addAttribute("loc", loc) ;
        return "bottomLink/bottomLinkAdd";
    }
    
    @ResponseBody
    @RequestMapping(value = "add", method = RequestMethod.POST)
    public String addPost(HttpServletRequest request,BottomLink bottomLink) throws Exception  {
        bottomLinkService.save(bottomLink);
      //保存日志,HttpServletRequest request
   		LoginUser userif= UserSession.getLoginUser(request);
		Operator operator = operatorService.findUserByUsername(userif.getUsername());
		operatorLogService.addUserLog(operator, request, "新增友情链接:"+bottomLink.getName());
		return ResultUtil.getSuccessMsg();
    }
    
    @RequestMapping(value = "edit", method = RequestMethod.GET)
    public String editGet(HttpServletRequest request,Model model) {
        Integer id=ServletRequestUtils.getIntParameter(request, "id",-1);
        BottomLink item = bottomLinkService.get(id);
        if(item.getPid() != 0 ) {
        	BottomLink pItem = bottomLinkService.get(item.getPid());
        	model.addAttribute("pItem",pItem);
        }
        model.addAttribute("pid",item.getPid());
        model.addAttribute("item",item);
        return "bottomLink/bottomLinkEdit";
    }

	@ResponseBody
    @RequestMapping(value = "edit", method = RequestMethod.POST)
    public String editPost(BottomLink bottomLink,HttpServletRequest request) throws Exception {
        bottomLinkService.save(bottomLink);
      //保存日志,HttpServletRequest request
   		LoginUser userif= UserSession.getLoginUser(request);
		Operator operator = operatorService.findUserByUsername(userif.getUsername());
		operatorLogService.addUserLog(operator, request, "修改友情链接:"+bottomLink.getName());
        return ResultUtil.getSuccessMsg();
    }

	@ResponseBody
    @RequestMapping(value = "/delete")
    public String delete(HttpServletRequest request,Integer id) throws Exception {
        bottomLinkService.delete(id);
      //保存日志,HttpServletRequest request
   		LoginUser userif= UserSession.getLoginUser(request);
		Operator operator = operatorService.findUserByUsername(userif.getUsername());
		operatorLogService.addUserLog(operator, request, "删除友情链接:id"+id.toString());
		return ResultUtil.getSuccessMsg();
    }

}