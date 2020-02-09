package cn.bohoon.page.web;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSONObject;

import cn.bohoon.basicSetup.helper.SysParamCache;
import cn.bohoon.basicSetup.helper.SysParamHelper;
import cn.bohoon.framework.orm.domain.Page;
import cn.bohoon.page.entity.DefinePage;
import cn.bohoon.page.entity.DefineTemplate;
import cn.bohoon.page.service.DefinePageService;
import cn.bohoon.page.service.DefineTemplateService;
import cn.bohoon.util.ResultUtil;

@Controller
@RequestMapping(value = "definePage")
public class DefinePageController {

	
	@Autowired
	DefinePageService definePageService;
	@Autowired
	DefineTemplateService defineTemplateService;

	/**
	 * 自定义页面列表
	 * 
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "list", method = RequestMethod.GET)
	public String list(HttpServletRequest request, Model model) {
		Integer pageNo = ServletRequestUtils.getIntParameter(request, "pageNo", 1);
		String templateId = ServletRequestUtils.getStringParameter(request, "templateId", "");
		String title = ServletRequestUtils.getStringParameter(request, "title", "");
		
		Page<DefinePage> page = new Page<DefinePage>();
		page.setPageNo(pageNo);
		String hql = "from DefinePage t where 1 =1 ";
		List<Object> params = new ArrayList<Object>();
		if (!StringUtils.isEmpty(templateId)) {
			hql += " and t.templateId like ?";
			params.add('%' + templateId + '%');
			model.addAttribute("templateId", templateId);
		}
		if (!StringUtils.isEmpty(title)) {
			hql += " and t.title like ?";
			params.add('%' + title + '%');
			model.addAttribute("title", title);
		}
		page = definePageService.page(page, hql,params.toArray());
		Map<DefinePage, Boolean> dpMap = new HashMap<DefinePage, Boolean>();
		model.addAttribute("pageDefinePage", page);
		String tHqls = " from DefineTemplate where isSystem=1 " ;
		DefineTemplate sdt = defineTemplateService.select(tHqls) ;
		for(DefinePage dp : page.getResult()) {
			if(null != sdt  && dp.getTemplateId().equals(sdt.getId())) {
				dpMap.put(dp, true) ;
			} else {
				dpMap.put(dp, false) ;
			}
		}
		
		String tHql = " from DefineTemplate where isSystem=0 " ;
		List<DefineTemplate> templates = defineTemplateService.list(tHql) ;

		model.addAttribute("dpMap", dpMap) ;
		model.addAttribute("templates", templates) ;
		
		
		return "definePage/definePageList";
	}

	/**
	 * 新增自定义页面
	 * 
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "add", method = RequestMethod.GET)
	public String addGet(HttpServletRequest request, Model model) {
		String hql = " from DefineTemplate where isSystem=0 " ;
		List<DefineTemplate> templates = defineTemplateService.list(hql) ;
		model.addAttribute("templates", templates) ;
		return "definePage/definePageAdd";
	}

	/**
	 * 新增保存自定义页面
	 * 
	 * @param request
	 * @param definePage
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "add", method = RequestMethod.POST)
	public String addPost(HttpServletRequest request, DefinePage definePage) {
		definePage.setCreateTime(new Date());
		definePageService.save(definePage);
		return ResultUtil.getSuccessMsg();
	}

	/**
	 * 去编辑自定义页面
	 * 
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "edit", method = RequestMethod.GET)
	public String editGet(HttpServletRequest request, Model model) {
		Integer id = ServletRequestUtils.getIntParameter(request, "id", -1);
		DefinePage definePage = definePageService.get(id);
		model.addAttribute("item", definePage);
		
		String hql = " from DefineTemplate where isSystem=0 " ;
		List<DefineTemplate> templates = defineTemplateService.list(hql) ;
		model.addAttribute("templates", templates) ;
		
		return "definePage/definePageEdit";
	}

	/**
	 * 编辑保存自定义页面
	 * 
	 * @param definePage
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "edit", method = RequestMethod.POST)
	public String editPost(DefinePage definePage) {
		definePageService.save(definePage);
		return ResultUtil.getSuccessMsg();
	}

	/**
	 * 删除自定义页面
	 * 
	 * @param request
	 * @param id
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/delete")
	public String delete(HttpServletRequest request, Integer id) {
		definePageService.delete(id);
		return ResultUtil.getSuccessMsg();
	}

	/**
	 * 查看
	 * 
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "showLink", method = RequestMethod.GET)
	public String showLink(HttpServletRequest request, Model model) {
		Integer id = ServletRequestUtils.getIntParameter(request, "id", -1);
		model.addAttribute("id", id);
		String pcSite = SysParamCache.getCache().getSysParamValue(SysParamHelper.PC_SITE) ;
		model.addAttribute("portalDomain", pcSite);
		return "definePage/showLink";
	}

	@RequestMapping(value = "pcPreview", method = RequestMethod.GET)
	public String pcPreview(HttpServletRequest request, Model model) {
		Integer id = ServletRequestUtils.getIntParameter(request, "id", -1);
		DefinePage entity = definePageService.get(id);
		model.addAttribute("item", entity);
		return "definePage/pcPreview";
	}
	
	
	
	/*@Value("${file.upload.path)")
	String filePath;*/
	//文件上傳
	@ResponseBody
	@RequestMapping(value = "/uploadPath1", method = RequestMethod.POST)
	public String upload(HttpServletRequest request,  MultipartFile file) throws IOException {
		/*String pcSite = SysParamCache.getCache().getSysParamValue(SysParamHelper.PC_SITE) ;*/
	
		 long  mills=System.currentTimeMillis();
	     File pathNext=null;
	     @SuppressWarnings("deprecation")
		String path2=request.getRealPath("");
	     
	     String replace=File.separator+"webapp";
	     String  quwebapp= path2.replaceAll(replace,"");
	     String substring = quwebapp.substring(0,quwebapp.length()-1)+"resources"+File.separator+"static"+File.separator+"fileUpload";
	     String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+"/";  
	     String pattern = "";  
	
		if(!file.isEmpty()){
			   String uploadFileName = file.getOriginalFilename();  
               //上传文件  
              
               if (uploadFileName.contains(".")) {  
                   pattern = uploadFileName.substring(uploadFileName.lastIndexOf("."));  
               }  
			
			File  path3=new File(substring);
			if(!path3.exists()){
				path3.mkdirs();
			}
			// pathNext=new File(filePath+File.separator+System.currentTimeMillis()+pattern);
			pathNext=new File(path3+File.separator+mills+pattern);
			System.out.println("下面路徑"+pathNext);
		
			/*	file.transferTo(pathNext);*/
			file.transferTo(pathNext);
			 
		}
		System.out.println(basePath+"fileUpload"+"/"+mills+pattern);
		
		    return    JSONObject.toJSONString(basePath+"fileUpload"+"/"+mills+pattern);
	}

}