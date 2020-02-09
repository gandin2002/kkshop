package cn.bohoon.product.web;



import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.bohoon.framework.orm.domain.Page;
import cn.bohoon.main.domain.LoginUser;
import cn.bohoon.main.domain.UserSession;
import cn.bohoon.main.system.entity.Operator;
import cn.bohoon.main.system.service.OperatorLogService;
import cn.bohoon.main.system.service.OperatorService;
import cn.bohoon.product.entity.TaxCode;
import cn.bohoon.product.service.TaxCodeService;
import cn.bohoon.util.ResultUtil;
/**
 *税码管理
 * @author HJ
 * 2018年2月8日,上午10:22:14
 */
@Controller
@RequestMapping(value = "taxCode")
public class TaxCodeController {

    @Autowired
    TaxCodeService taxCodeService;
    @Autowired
    OperatorLogService operatorLogService;
    @Autowired
    OperatorService operatorService;

    @RequestMapping(value = "list", method = RequestMethod.GET)
    public String list(HttpServletRequest request,Model model,String name)  {
        Integer pageNo=ServletRequestUtils.getIntParameter(request, "pageNo",1);

        Page<TaxCode> pageTaxCode=new Page<TaxCode>();
        pageTaxCode.setPageNo(pageNo);

        String hql =" from TaxCode";
        if(!StringUtils.isEmpty(name)){
        	hql +=" where name like '%"+name+"%'";
        }

        pageTaxCode=taxCodeService.page(pageTaxCode,hql);

        model.addAttribute("name", name);
        model.addAttribute("pageTaxCode", pageTaxCode);
        return "taxCode/taxCodeList";
    }
    
    @RequestMapping(value = "add", method = RequestMethod.GET)
    public String addGet(HttpServletRequest request,Model model)  {
        return "taxCode/taxCodeAdd";
    }
    
    @ResponseBody
    @RequestMapping(value = "add", method = RequestMethod.POST)
    public String addPost(HttpServletRequest request,TaxCode taxCode) throws Exception  {
    	if(taxCode.getState()){
    		taxCodeService.execute(" update TaxCode  set state = 0 ");
    	}
    	List<TaxCode> tcArray = taxCodeService.list(" from TaxCode order by  sort ");
    	if(!tcArray.isEmpty()){
    		taxCode.setSort(tcArray.get(tcArray.size()-1).getSort()+1);
    	}
    	
        taxCodeService.save(taxCode);
        //保存日志,HttpServletRequest request
   		LoginUser userif= UserSession.getLoginUser(request);
		Operator operator = operatorService.findUserByUsername(userif.getUsername());
		operatorLogService.addUserLog(operator, request, "新增税码:"+taxCode.getName());
		return ResultUtil.getSuccessMsg();
    }
    
    @RequestMapping(value = "edit", method = RequestMethod.GET)
    public String editGet(HttpServletRequest request,Model model) {
        Integer id=ServletRequestUtils.getIntParameter(request, "id",-1);
        TaxCode taxCode=taxCodeService.get(id);
        model.addAttribute("item",taxCode);
        return "taxCode/taxCodeEdit";
    }

	@ResponseBody
    @RequestMapping(value = "edit", method = RequestMethod.POST)
    public String editPost(TaxCode taxCode,HttpServletRequest request) throws Exception {
    	if(taxCode.getState()){
    		taxCodeService.execute(" update TaxCode  set state = 0 ");
    	}
        taxCodeService.update(taxCode);
        //保存日志,HttpServletRequest request
   		LoginUser userif= UserSession.getLoginUser(request);
		Operator operator = operatorService.findUserByUsername(userif.getUsername());
		operatorLogService.addUserLog(operator, request, "修改税码:"+taxCode.getName());
        return ResultUtil.getSuccessMsg();
    }

	@ResponseBody
    @RequestMapping(value = "/delete")
    public String delete(HttpServletRequest request,Integer id) throws Exception {
        taxCodeService.delete(id);
        //保存日志,HttpServletRequest request
   		LoginUser userif= UserSession.getLoginUser(request);
		Operator operator = operatorService.findUserByUsername(userif.getUsername());
		operatorLogService.addUserLog(operator, request, "删除税码id:"+id.toString());
		return ResultUtil.getSuccessMsg();
    }

}