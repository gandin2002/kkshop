package cn.bohoon.basicsetup.web;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;

import cn.bohoon.basicSetup.domain.BankAccount;
import cn.bohoon.basicSetup.domain.SysParamType;
import cn.bohoon.basicSetup.entity.SysParam;
import cn.bohoon.basicSetup.helper.SysParamHelper;
import cn.bohoon.basicSetup.service.SysParamService;
import cn.bohoon.company.entity.City;
import cn.bohoon.company.service.CityService;
import cn.bohoon.framework.util.JsonUtil;
import cn.bohoon.main.domain.LoginUser;
import cn.bohoon.main.domain.UserSession;
import cn.bohoon.main.system.entity.Operator;
import cn.bohoon.main.system.service.OperatorLogService;
import cn.bohoon.main.system.service.OperatorService;
import cn.bohoon.main.util.ResultUtil;
import cn.bohoon.util.IDUtil;

/**
 * 银行账号设置
 * 
 * @author HJ 2018年1月12日,上午9:47:51
 */
@Controller
@RequestMapping(value = "bankSetup")
public class BankSetupController {

	@Autowired
	SysParamService sysParamService;
	@Autowired
	CityService cityService;
	@Autowired
    OperatorLogService operatorLogService;
    @Autowired
    OperatorService operatorService;

	/**
	 * 数据展示
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "display", method = RequestMethod.GET)
	public String display(Model model) {
		SysParam sysParam = sysParamService.findParam(SysParamHelper.BANK_DATA_LIST, SysParamType.BACKBANK_PARAM);
		model.addAttribute("sysParam", sysParam);
		
		return "basicSetup/bankSetup/bankList";
	}

	@RequestMapping(value = "add", method = RequestMethod.GET)
	public String add(Model model) {
		List<City> listCity = cityService.list(" from City where type = ?  ", 2);
		model.addAttribute("listCity", listCity);
		return "basicSetup/bankSetup/bankAdd";
	}

	/**
	 * 添加银行
	 * 
	 * @param bankAccount
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value = "add", method = RequestMethod.POST)
	public @ResponseBody String add(BankAccount bankAccount,HttpServletRequest request) throws Exception {
		SysParam sysParam = sysParamService.findParam(SysParamHelper.BANK_DATA_LIST, SysParamType.BACKBANK_PARAM);
		String address = cityService.findCictBycode(bankAccount.getProvince()).getName(); // 根据code
																			// 查询地址名字
		if (!StringUtils.isEmpty(bankAccount.getCity())) {
			address += "," + cityService.findCictBycode(bankAccount.getCity()).getName();
		}
		if (!StringUtils.isEmpty(bankAccount.getArea())) {
			address += "," + cityService.findCictBycode(bankAccount.getArea()).getName();
		}
		bankAccount.setId(IDUtil.getMemberId());
		bankAccount.setAddress(address);
		List<BankAccount> list = new ArrayList<>();
		if (!StringUtils.isEmpty(sysParam.getSysOption())) {
			list = JSON.parseArray(sysParam.getSysOption(), BankAccount.class);
		} 
		list.add(bankAccount);
		sysParam.setSysOption(JSON.toJSONString(list));
		sysParamService.save(sysParam);
		//保存日志,HttpServletRequest request
   		LoginUser userif= UserSession.getLoginUser(request);
		Operator operator = operatorService.findUserByUsername(userif.getUsername());
		operatorLogService.addUserLog(operator, request, "新增收款银行设置");
		return ResultUtil.getSuccess();
	}
	@RequestMapping(value = "edit", method = RequestMethod.GET)
	public String edit(@RequestParam(name="id",required=true) String id,Model model){
		SysParam sysParam = sysParamService.findParam(SysParamHelper.BANK_DATA_LIST, SysParamType.BACKBANK_PARAM);
		List<BankAccount> list = JSON.parseArray(sysParam.getSysOption(), BankAccount.class);
		for (BankAccount bankAccount : list) {
			if(id.equals(bankAccount.getId())){
				
			    List<City> listProvince= cityService.list(" from City where type = ?  ",2);
				model.addAttribute("listProvince", listProvince);
				if(bankAccount.getCity() != null){
				    List<City> listCity= cityService.list(" from City where parentId = ?  and type = ?  ",bankAccount.getProvince(),3);
					model.addAttribute("listCity", listCity);
					
					if(bankAccount.getArea() != null){
					    List<City> listArea= cityService.list(" from City where parentId = ?  and  type = ?  ",bankAccount.getCity(),4);
						model.addAttribute("listArea", listArea);
					}
					
				}

				
				model.addAttribute("bankAccount", bankAccount);
 
			}
		}
 
		return "basicSetup/bankSetup/bankEdit";
	}
	
	@RequestMapping(value="edit",method=RequestMethod.POST)
	public @ResponseBody  String edit(BankAccount bankAccount,HttpServletRequest request) throws Exception{
		SysParam sysParam = sysParamService.findParam(SysParamHelper.BANK_DATA_LIST, SysParamType.BACKBANK_PARAM);
		String address = cityService.findCictBycode(bankAccount.getProvince()).getName(); // 根据code
		// 查询地址名字
		if (!StringUtils.isEmpty(bankAccount.getCity())) {
			address += "," + cityService.findCictBycode(bankAccount.getCity()).getName();
		}
		if (!StringUtils.isEmpty(bankAccount.getArea())) {
			address += "," + cityService.findCictBycode(bankAccount.getArea()).getName();
		}
		bankAccount.setAddress(address);
		List<BankAccount> list = JSON.parseArray(sysParam.getSysOption(), BankAccount.class);
		for (int i = 0; i < list.size(); i++) {
			BankAccount ba = list.get(i);
			if(bankAccount.getId().equals(ba.getId())){
				list.set(i, bankAccount);
			}
		}
	    sysParam.setSysOption(JsonUtil.toJson(list));
		sysParamService.save(sysParam);
		//保存日志,HttpServletRequest request
   		LoginUser userif= UserSession.getLoginUser(request);
		Operator operator = operatorService.findUserByUsername(userif.getUsername());
		operatorLogService.addUserLog(operator, request, "修改收款银行设置");
	    return ResultUtil.getSuccess();
	}
	
	@RequestMapping(value="delete",method=RequestMethod.GET)
	public @ResponseBody String delete(@RequestParam(name="id",required=true) String id,HttpServletRequest request) throws Exception{
		SysParam sysParam = sysParamService.findParam(SysParamHelper.BANK_DATA_LIST, SysParamType.BACKBANK_PARAM);
		List<BankAccount> list = JSON.parseArray(sysParam.getSysOption(), BankAccount.class);
		for (BankAccount bankAccount : list) {
			if(id.equals(bankAccount.getId())){
				list.remove(bankAccount);
			}
		}
	    sysParam.setSysOption(JsonUtil.toJson(list));
		sysParamService.save(sysParam);
		//保存日志,HttpServletRequest request
   		LoginUser userif= UserSession.getLoginUser(request);
		Operator operator = operatorService.findUserByUsername(userif.getUsername());
		operatorLogService.addUserLog(operator, request, "删除收款银行设置");
		return ResultUtil.getSuccess();
	}

}
