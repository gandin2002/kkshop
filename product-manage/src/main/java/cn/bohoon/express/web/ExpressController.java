package cn.bohoon.express.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import cn.bohoon.express.domain.ExpressParams;
import cn.bohoon.express.entity.Express;
import cn.bohoon.express.entity.Logistics;
import cn.bohoon.express.service.ExpressService;
import cn.bohoon.express.service.LogisticsService;
import cn.bohoon.framework.util.JsonUtil;
import cn.bohoon.main.system.service.UploadService;
import cn.bohoon.util.ResultUtil;
/**
 * 2017年11月7日13:59:00
 * @author Administrator
 * 快递模板
 */
@Controller
@RequestMapping(value = "express")
public class ExpressController {

	@Autowired
	ExpressService expressService;
	
	@Autowired
	LogisticsService logisticsService;
	
    @Autowired
    UploadService  uploadService;

 
	@RequestMapping(value = "add", method = RequestMethod.GET)
	public String addGet(HttpServletRequest request, Model model,Integer id) {
		Express express= expressService.select(" from Express where logisticsid = ? ",id);
		
		List<Express> list = expressService.list(" from Express where backgroundImage is not null and id != ? ",express.getId());
		
		Map<Express,Logistics> logisticssMap = new HashMap<>();
		for (Express express2 : list) {
			Logistics logistics = logisticsService.get(express2.getLogisticsid());
			logisticssMap.put(express2, logistics);
		}
		model.addAttribute("list", list);
		model.addAttribute("logisticssMap", logisticssMap);
		model.addAttribute("express", express);
		return "logistics/expressAdd";
	}

 
	@ResponseBody
	@RequestMapping(value = "add", method = RequestMethod.POST)
	public String addPost(HttpServletRequest request,ExpressParams expressParams, String  expressTemplate,Integer id,String backgroundImage) {
		Express express= expressService.get(id);
		express.setBackgroundImage(backgroundImage);
		express.setExpressTemplate(expressTemplate);
		express.setExpressParams(JsonUtil.toJson(expressParams));
		expressService.save(express);
		return ResultUtil.getSuccessMsg();
	}
	
	/**
	 * 上传背景图片
	 * @param file
	 * @param id
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value="addBackgroundImage",method=RequestMethod.POST)
	public String addBackgroundImage(@RequestParam("file") MultipartFile file,Integer id) throws Exception{
		String image = uploadService.handleFileUploadNoDomain(file,"express");
		Map<String,String> map = new HashMap<>();
		map.put("image", image);
		return JsonUtil.toJson(map);
	}
}
