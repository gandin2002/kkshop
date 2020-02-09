package cn.bohoon.page.web;




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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import cn.bohoon.framework.orm.domain.Page;
import cn.bohoon.main.domain.LoginUser;
import cn.bohoon.main.domain.UserSession;
import cn.bohoon.main.system.entity.Operator;
import cn.bohoon.main.system.service.OperatorLogService;
import cn.bohoon.main.system.service.OperatorService;
import cn.bohoon.main.system.service.UploadService;
import cn.bohoon.util.ResultUtil;
import cn.bohoon.page.entity.PlaceAD;
import cn.bohoon.page.service.PlaceADService;


@Controller
@RequestMapping(value="/placeAD")
public class PlaceADController {

    @Autowired
    PlaceADService placeADService;
    @Autowired
    UploadService  uploadService;
    @Autowired
    OperatorLogService operatorLogService;
    @Autowired
    OperatorService operatorService;

    
    @RequestMapping(value = "/list",method = RequestMethod.GET)
    public String list(Model model,HttpServletRequest request,PlaceAD paleceAd){
    	Integer pageNo = ServletRequestUtils.getIntParameter(request, "pageNo", 1);
    	Page<PlaceAD> pagePlaceAD = new Page<PlaceAD>();
    	pagePlaceAD.setPageNo(pageNo);
    	String sql = new String("from PlaceAD where 1 = 1 ");
    	List<Object> params = new ArrayList<>();
    	if(!StringUtils.isEmpty(paleceAd.getLocation())){
    		params.add(paleceAd.getLocation());
    		sql += " and location = ? ";
    	}
    	sql += "order by location,sort desc ";
    	pagePlaceAD = placeADService.page(pagePlaceAD, sql.toString(),params.toArray());
    	model.addAttribute("pagePlaceAD", pagePlaceAD);
    	return "placeAD/placeADList";
    }
    
    @RequestMapping(value="/add",method=RequestMethod.GET)
    public String addGet(){
    	return "placeAD/placeADAdd";
    }
    
    @ResponseBody
    @RequestMapping(value="/add",method=RequestMethod.POST)
    public String addPost(PlaceAD placeAD,HttpServletRequest request) throws Exception{
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		MultipartFile file = multipartRequest.getFile("file");
		if (!StringUtils.isEmpty(file)) {
			String image = uploadService.handleFileUploadNoDomain(file,"ad");
			placeAD.setImage(image);
		}
		placeAD.setStatus(true);
		placeADService.save(placeAD);
		//保存日志,HttpServletRequest request
   		LoginUser userif= UserSession.getLoginUser(request);
		Operator operator = operatorService.findUserByUsername(userif.getUsername());
		operatorLogService.addUserLog(operator, request, "新增图片广告位:id"+placeAD.getId().toString());
    	return ResultUtil.getSuccessMsg();
    }
    
    
    @RequestMapping(value="/edit",method=RequestMethod.GET)
    public String editGet(Integer id,Model model){
    	PlaceAD placeAD=placeADService.get(id);
    	model.addAttribute("placeAD", placeAD);
    	return "placeAD/placeADEdit";
    }
    
    @ResponseBody
    @RequestMapping(value="/edit",method=RequestMethod.POST)
    public String editPost(PlaceAD placeAD,HttpServletRequest request) throws Exception{
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		MultipartFile file = multipartRequest.getFile("file");
		PlaceAD placeADs = placeADService.get(placeAD.getId());
		if (!StringUtils.isEmpty(file)) {
			String image = uploadService.handleFileUploadNoDomain(file,"ad");
			placeAD.setImage(image);
		}else{
			placeAD.setImage(placeADs.getImage());
		}
		placeADService.save(placeAD);
		//保存日志,HttpServletRequest request
   		LoginUser userif= UserSession.getLoginUser(request);
		Operator operator = operatorService.findUserByUsername(userif.getUsername());
		operatorLogService.addUserLog(operator, request, "修改图片广告位:id"+placeAD.getId().toString());
    	return ResultUtil.getSuccessMsg();
    }
    
    
    @ResponseBody()
    @RequestMapping(value="/delete",method=RequestMethod.GET)
    public String delete(Integer id,HttpServletRequest request) throws Exception{
    	placeADService.delete(id);
    	//保存日志,HttpServletRequest request
   		LoginUser userif= UserSession.getLoginUser(request);
		Operator operator = operatorService.findUserByUsername(userif.getUsername());
		operatorLogService.addUserLog(operator, request, "删除图片广告位:id"+id.toString());
    	return ResultUtil.getSuccessMsg();
    }
    
    @ResponseBody
    @RequestMapping(value = "/modifiedStatus", method = RequestMethod.GET)
    public String modifiedStatus(HttpServletRequest request, Integer id) throws Exception{
    	PlaceAD placeAD=placeADService.get(id);
		placeAD.setStatus(!placeAD.getStatus());
		placeADService.save(placeAD);
		//保存日志,HttpServletRequest request
   		LoginUser userif= UserSession.getLoginUser(request);
		Operator operator = operatorService.findUserByUsername(userif.getUsername());
		operatorLogService.addUserLog(operator, request, "修改状态图片广告位:id"+id.toString());
    	return  ResultUtil.getSuccessMsg();
    }
}
