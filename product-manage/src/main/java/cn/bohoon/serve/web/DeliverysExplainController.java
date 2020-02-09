package cn.bohoon.serve.web;



import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;


import cn.bohoon.serve.entity.DeliverysExplain;
import cn.bohoon.serve.service.DeliverysExplainService;
import cn.bohoon.framework.orm.domain.Page;
import cn.bohoon.main.domain.LoginUser;
import cn.bohoon.main.domain.UserSession;
import cn.bohoon.main.system.entity.Operator;
import cn.bohoon.main.system.service.OperatorLogService;
import cn.bohoon.main.system.service.OperatorService;
import cn.bohoon.util.ResultUtil;

@Controller
@RequestMapping(value = "deliverysExplain")
public class DeliverysExplainController {

    @Autowired
    DeliverysExplainService deliverysExplainService;

    @Autowired
    OperatorLogService operatorLogService;
    @Autowired
    OperatorService operatorService;

    @RequestMapping(value = "list", method = RequestMethod.GET)
    public String list(HttpServletRequest request,Model model)  {
        Integer pageNo=ServletRequestUtils.getIntParameter(request, "pageNo",1);

        Page<DeliverysExplain> pageDeliverysExplain=new Page<DeliverysExplain>(5);
        pageDeliverysExplain.setPageNo(pageNo);


        pageDeliverysExplain=deliverysExplainService.page(pageDeliverysExplain, "from DeliverysExplain");

        model.addAttribute("pageDeliverysExplain", pageDeliverysExplain);
        return "deliverysExplain/deliverysExplainList";
    }
    
    @RequestMapping(value = "add", method = RequestMethod.GET)
    public String addGet(HttpServletRequest request,Model model)  {
        return "deliverysExplain/deliverysExplainAdd";
    }
    
    @ResponseBody
    @RequestMapping(value = "add", method = RequestMethod.POST)
    public String addPost(HttpServletRequest request,DeliverysExplain deliverysExplain) throws Exception  {
    	deliverysExplain.setCreateTime(new Date());
    	deliverysExplainService.save(deliverysExplain);
    	//保存日志,HttpServletRequest request
   		LoginUser userif= UserSession.getLoginUser(request);
		Operator operator = operatorService.findUserByUsername(userif.getUsername());
		operatorLogService.addUserLog(operator, request, "新增配送说明:"+deliverysExplain.getId().toString());
		return ResultUtil.getSuccessMsg();
    }
    
    @RequestMapping(value = "edit", method = RequestMethod.GET)
    public String editGet(HttpServletRequest request,Model model) {
        Integer id=ServletRequestUtils.getIntParameter(request, "id",-1);
        DeliverysExplain deliverysExplain=deliverysExplainService.get(id);
        model.addAttribute("item",deliverysExplain);
        return "deliverysExplain/deliverysExplainEdit";
    }

	@ResponseBody
    @RequestMapping(value = "edit", method = RequestMethod.POST)
    public String editPost(DeliverysExplain deliverysExplain,HttpServletRequest request) throws Exception {
		deliverysExplain.setCreateTime(new Date());
        deliverysExplainService.save(deliverysExplain);
      //保存日志,HttpServletRequest request
   		LoginUser userif= UserSession.getLoginUser(request);
		Operator operator = operatorService.findUserByUsername(userif.getUsername());
		operatorLogService.addUserLog(operator, request, "修改配送说明:"+deliverysExplain.getId().toString());
        return ResultUtil.getSuccessMsg();
    }

	@ResponseBody
    @RequestMapping(value = "/delete")
    public String delete(HttpServletRequest request,Integer id) throws Exception {
        deliverysExplainService.delete(id);
      //保存日志,HttpServletRequest request
   		LoginUser userif= UserSession.getLoginUser(request);
		Operator operator = operatorService.findUserByUsername(userif.getUsername());
		operatorLogService.addUserLog(operator, request, "修改配送说明:"+id.toString());
		return ResultUtil.getSuccessMsg();
    }

}