package cn.bohoon.page.web;



import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.bohoon.framework.orm.domain.Page;
import cn.bohoon.util.ResultUtil;
import cn.bohoon.page.entity.HelpCenter;
import cn.bohoon.page.service.HelpCenterService;

@Controller
@RequestMapping(value = "helpCenter")
public class HelpCenterController {

    @Autowired
    HelpCenterService helpCenterService;

    @RequestMapping(value = "list", method = RequestMethod.GET)
    public String list(HttpServletRequest request,Model model)  {
        Integer pageNo=ServletRequestUtils.getIntParameter(request, "pageNo",1);
        Page<HelpCenter> page = new Page<HelpCenter>(5);
        page.setPageNo(pageNo);
        page=helpCenterService.page(page, "from HelpCenter");
        model.addAttribute("pageHelpCenter", page);
        return "helpCenter/helpCenterList";
    }
    
    @RequestMapping(value = "add", method = RequestMethod.GET)
    public String addGet(HttpServletRequest request,Model model)  {
    	int id=ServletRequestUtils.getIntParameter(request, "id",0);
    	if(id == 0 ) {
    		model.addAttribute("pid", id) ;
    		model.addAttribute("level", 1) ;
    	} else {
    		 HelpCenter item=helpCenterService.get(id);
    		 model.addAttribute("pid", id) ;
    		 model.addAttribute("level", item.getLevel()+1) ;
    		 model.addAttribute("item", item) ;
    	}
        return "helpCenter/helpCenterAdd";
    }
    
    @ResponseBody
    @RequestMapping(value = "add", method = RequestMethod.POST)
    public String addPost(HttpServletRequest request,HelpCenter helpCenter)  {
        helpCenterService.save(helpCenter);
		return ResultUtil.getSuccessMsg();
    }
    
    @RequestMapping(value = "edit", method = RequestMethod.GET)
    public String editGet(HttpServletRequest request,Model model) {
        Integer id=ServletRequestUtils.getIntParameter(request, "id",-1);
        HelpCenter item=helpCenterService.get(id);
        if(item.getLevel()>1) {
        	HelpCenter pItem = helpCenterService.get(item.getPid());
        	model.addAttribute("pItem",pItem);
        }
        model.addAttribute("item",item);
        return "helpCenter/helpCenterEdit";
    }

	@ResponseBody
    @RequestMapping(value = "edit", method = RequestMethod.POST)
    public String editPost(HelpCenter helpCenter) {
        helpCenterService.save(helpCenter);
        return ResultUtil.getSuccessMsg();
    }

	@ResponseBody
    @RequestMapping(value = "/delete")
    public String delete(HttpServletRequest request,Integer id) {
        helpCenterService.delete(id);
		return ResultUtil.getSuccessMsg();
    }

}