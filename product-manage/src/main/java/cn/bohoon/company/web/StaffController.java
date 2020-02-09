package cn.bohoon.company.web;

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

import cn.bohoon.framework.orm.domain.Page;
import cn.bohoon.framework.util.DateUtil;
import cn.bohoon.userInfo.entity.UserInfo;
import cn.bohoon.userInfo.service.UserInfoService;
/*
 * 员工记录
 */
@Controller
@RequestMapping(value = "staff")
public class StaffController {
			    
	@Autowired
	UserInfoService userInfoService;
			    
    @RequestMapping(value = "list", method = RequestMethod.GET)
	public String list(HttpServletRequest request,Model model,Integer id,String phone) throws Exception {
		Integer pageNo=ServletRequestUtils.getIntParameter(request, "pageNo",1);
		String companyId = ServletRequestUtils.getStringParameter(request, "companyId","");
		String startTime = ServletRequestUtils.getStringParameter(request, "startTime","");
		String endTime = ServletRequestUtils.getStringParameter(request, "endTime","");
		Page<UserInfo> pageUserInfo=new Page<UserInfo>(5);
		pageUserInfo.setPageNo(pageNo);	
		String hql = " from UserInfo u where 1 = 1";
        List<Object> params = new ArrayList<Object>();
        if (companyId!=null) {
			hql = hql + "  and u.companyId=?";
			params.add(companyId);	
		} 
        if (id!=null) {
			hql = hql + " and u.id = ? ";
			params.add(id);	
		} 
        if (!StringUtils.isEmpty(phone)) {
			hql = hql + " and u.phone like ? ";
			params.add('%' + phone + '%');	
		} 
        if (!StringUtils.isEmpty(startTime)){
    		hql = hql + " and u.createTime >= ?";
    		params.add(DateUtil.switchStringToDate(startTime, "yy-MM-dd"));
    		model.addAttribute("startTime", startTime);
        }
        if (!StringUtils.isEmpty(endTime)){
        	hql = hql + " and u.createTime <= ?";
        	params.add(DateUtil.getNDayAfter(endTime, 1));
        	model.addAttribute("endTime", endTime);
        }	
        hql+="ORDER BY u.createTime desc";
		pageUserInfo=userInfoService.page(pageUserInfo, hql,params.toArray());
		model.addAttribute("phone", phone);
		model.addAttribute("id", id);
		model.addAttribute("memberId", companyId);
		model.addAttribute("pageUserInfo", pageUserInfo);
		return "company/staffList";			
	}
}
