package cn.bohoon.serve.web;



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

import cn.bohoon.serve.domain.SaleType;
import cn.bohoon.serve.entity.Sale;
import cn.bohoon.serve.service.SaleService;
import cn.bohoon.framework.orm.domain.Page;
import cn.bohoon.main.domain.LoginUser;
import cn.bohoon.main.domain.UserSession;
import cn.bohoon.main.system.entity.Operator;
import cn.bohoon.main.system.service.OperatorLogService;
import cn.bohoon.main.system.service.OperatorService;
import cn.bohoon.util.ResultUtil;

@Controller
@RequestMapping(value = "sale")
public class SaleController {

    @Autowired
    SaleService saleService;
    @Autowired
    OperatorLogService operatorLogService;
    @Autowired
    OperatorService operatorService;


    @RequestMapping(value = "list", method = RequestMethod.GET)
    public String list(HttpServletRequest request,Model model,SaleType saleType)  {
        Integer pageNo=ServletRequestUtils.getIntParameter(request, "pageNo",1);

        Page<Sale> pageSale=new Page<Sale>(5);
        pageSale.setPageNo(pageNo);
        String hql = "from Sale s where 1 = 1 ";
        List<Object> params = new ArrayList<Object>();
        if (!StringUtils.isEmpty(saleType)) {
			hql = hql + " and s.saleType like ? ";
			params.add(saleType);
			model.addAttribute("saleType", saleType);
		}
        pageSale=saleService.page(pageSale, hql,params.toArray());
        model.addAttribute("pageSale", pageSale);
        return "sale/saleList";
    }
    
    @RequestMapping(value = "add", method = RequestMethod.GET)
    public String addGet(HttpServletRequest request,Model model)  {
        return "sale/saleAdd";
    }
    
    @ResponseBody
    @RequestMapping(value = "add", method = RequestMethod.POST)
    public String addPost(HttpServletRequest request,Sale sale) throws Exception  {
    	sale.setCreateTime(new Date());
        saleService.save(sale);

 		//保存日志,HttpServletRequest request
       	LoginUser userif= UserSession.getLoginUser(request);
		Operator operator = operatorService.findUserByUsername(userif.getUsername());
		operatorLogService.addUserLog(operator, request, "新增售后原因:id"+sale.getId().toString());
		return ResultUtil.getSuccessMsg();
    }
    
    @RequestMapping(value = "edit", method = RequestMethod.GET)
    public String editGet(HttpServletRequest request,Model model) {
        Integer id=ServletRequestUtils.getIntParameter(request, "id",-1);
        Sale sale=saleService.get(id);
        model.addAttribute("item",sale);
        return "sale/saleEdit";
    }

	@ResponseBody
    @RequestMapping(value = "edit", method = RequestMethod.POST)
    public String editPost(Sale sale,HttpServletRequest request) throws Exception {
		sale.setCreateTime(new Date());
        saleService.save(sale);
      //保存日志,HttpServletRequest request
       	LoginUser userif= UserSession.getLoginUser(request);
		Operator operator = operatorService.findUserByUsername(userif.getUsername());
		operatorLogService.addUserLog(operator, request, "修改售后原因:id"+sale.getId().toString());
        return ResultUtil.getSuccessMsg();
    }

	@ResponseBody
    @RequestMapping(value = "/delete")
    public String delete(HttpServletRequest request,Integer id) throws Exception {
        saleService.delete(id);
      //保存日志,HttpServletRequest request
       	LoginUser userif= UserSession.getLoginUser(request);
		Operator operator = operatorService.findUserByUsername(userif.getUsername());
		operatorLogService.addUserLog(operator, request, "删除售后原因:id"+id.toString());
		return ResultUtil.getSuccessMsg();
    }

}