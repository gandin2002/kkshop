package cn.bohoon.wxmp.web;

import java.net.URLEncoder;
import java.text.MessageFormat;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;

import cn.bohoon.basicSetup.domain.SysParamType;
import cn.bohoon.basicSetup.entity.SysParam;
import cn.bohoon.basicSetup.helper.SysParamHelper;
import cn.bohoon.basicSetup.service.SysParamService;
import cn.bohoon.framework.util.JsonUtil;
import cn.bohoon.util.ResultUtil;
import cn.bohoon.wx.mp.connector.WxPmRequests;
import cn.bohoon.wx.mp.domain.WXButtion;

/**
 * 自定义菜单
 * @author HJ
 * 2018年3月1日,上午10:32:05
 */
@Controller
@RequestMapping(value="wxMpCustomMenu")
public class WxMpCustomMenuController {
	
	
	@Autowired
	SysParamService sysParamService;
	
	@Autowired
	WxPmRequests wxPmRequests;
	
	
	@RequestMapping(value="list",method=RequestMethod.GET)
	public String list(Model model){
		SysParam  sysParamMenu  = sysParamService.findParam(SysParamHelper.WX_MP_MENU, SysParamType.WX_MP_CONFIG);
		String sysOpention = sysParamMenu.getSysOption();
		if(!StringUtils.isEmpty(sysOpention)){
			String buttonArrayJson = JSON.parseObject(sysOpention).getString("button");	
			List<WXButtion> buttonList =  JSON.parseArray(buttonArrayJson,WXButtion.class);
			model.addAttribute("buttonList", buttonList);
		}
		
		SysParam  sysParamBnType  = sysParamService.findParam(SysParamHelper.WX_MP_BUTTON_TYPE, SysParamType.WX_MP_CONFIG);
		model.addAttribute("sysParamBnType", sysParamBnType);
		return "wxMpCustomMenu/wxMpCustomMenuList";
	}
	
	/**
	 * 上传新的菜单
	 * @return
	 */
	@RequestMapping(value="updateMenu",method=RequestMethod.GET)
	public @ResponseBody String updateMenu(){

		SysParam  sysParamMenu  = sysParamService.findParam(SysParamHelper.WX_MP_MENU, SysParamType.WX_MP_CONFIG);
		String sysOpention = sysParamMenu.getSysOption();
		
		
		wxPmRequests.updateMenu(sysOpention);
		
		return ResultUtil.getSuccessMsg();
	}
	
	/**
	 * 获取菜单
	 * @return
	 */
	@RequestMapping(value="menuGet",method=RequestMethod.GET)
	public @ResponseBody String menuGet(){
		String resultJson = wxPmRequests.getMenu();
		
		String menuJson =JSON.parseObject(resultJson).getString("menu");
		String buttonArrayJson = JSON.parseObject(menuJson).getString("button");	
		
		List<WXButtion> buttonList =  JSON.parseArray(buttonArrayJson,WXButtion.class);
		for (WXButtion wxButtion : buttonList) {
			wxButtion.setKey(UUID.randomUUID().toString());
			for (WXButtion wxButtion2 : wxButtion.getSub_button()) {
				wxButtion2.setKey(UUID.randomUUID().toString());
			}
		}
		Map<String,List<WXButtion>>  map = new  HashMap<String,List<WXButtion>>();
		map.put("button", buttonList);
		
		SysParam  menu  = sysParamService.findParam(SysParamHelper.WX_MP_MENU, SysParamType.WX_MP_CONFIG);
		menu.setSysOption(JsonUtil.toJson(map));
		sysParamService.save(menu);
		
		return ResultUtil.getSuccessMsg();
	}
	
	
	@RequestMapping(value="addButton",method=RequestMethod.GET)
	public String addButton(String key,Model model){
		
		
		SysParam  sysParamMenu  = sysParamService.findParam(SysParamHelper.WX_MP_MENU, SysParamType.WX_MP_CONFIG);
		String sysOpention = sysParamMenu.getSysOption();
		String buttonArrayJson = JSON.parseObject(sysOpention).getString("button");	
		List<WXButtion> buttonList =  JSON.parseArray(buttonArrayJson,WXButtion.class);
		
		for (WXButtion wxButtion : buttonList) {
			if(wxButtion.getKey().equals(key)){
				model.addAttribute("wxButtion", wxButtion);
			} 
		}
		
		SysParam  sysParamBnType  = sysParamService.findParam(SysParamHelper.WX_MP_BUTTON_TYPE, SysParamType.WX_MP_CONFIG);
		model.addAttribute("sysParamBnType", sysParamBnType); //获取BUTTON_TYPE 类型
		
		model.addAttribute("key", key);
		return "wxMpCustomMenu/wxMpCustomMenuAdd";
	}
	
	/**
	 * 增加
	 * @param source
	 * @param pKey
	 * @param state
	 * @return
	 */
	
	@RequestMapping(value="addButton",method=RequestMethod.POST)
	public @ResponseBody String addButton(WXButtion source,String pKey,@RequestParam(name="state",defaultValue="false",required=false)Boolean state){
		
		SysParam  sysParamMenu  = sysParamService.findParam(SysParamHelper.WX_MP_MENU, SysParamType.WX_MP_CONFIG);
		String sysOpention = sysParamMenu.getSysOption();
		String buttonArrayJson = JSON.parseObject(sysOpention).getString("button");	
		List<WXButtion> buttonList =  JSON.parseArray(buttonArrayJson,WXButtion.class);
		
		String type = source.getType();
		
		if(state){
			if("view".equals(type) || "miniprogram".equals(type)){
				SysParam  sysParamOAuth  = sysParamService.findParam(SysParamHelper.WX_MP_OAUTH,SysParamType.WX_MP_CONFIG);
				if(!StringUtils.isEmpty(sysParamOAuth.getValue())){
					 String oAuthPath= sysParamOAuth.getValue();
					 String requestUrl = replaceAccessTokenReg(oAuthPath, "redirect_uri",URLEncoder.encode(source.getUrl()));
					 source.setUrl(requestUrl);
				}
			}
		}
		if(!StringUtils.isEmpty(source.getUrl())){
		}
		
	
		source.setKey(UUID.randomUUID().toString());
		if(!StringUtils.isEmpty(pKey)){
			for (WXButtion wxButtion : buttonList) {
				if(wxButtion.getKey().equals(pKey)){
					if(wxButtion.getSub_button().size()>=5){
						return ResultUtil.getError("添加失败! 二级菜单数组，个数应为1~5个！");
					}
					wxButtion.getSub_button().add(source);
				} 
			}
		}else{
			if(buttonList.size()>=3){
				return ResultUtil.getError("添加失败! 一级菜单数组，个数应为1~3个！");
			}
			buttonList.add(source);
		}

		Map<String,List<WXButtion>>  map = new  HashMap<String,List<WXButtion>>();
		map.put("button", buttonList);
		
		SysParam  menu  = sysParamService.findParam(SysParamHelper.WX_MP_MENU, SysParamType.WX_MP_CONFIG);
		menu.setSysOption(JsonUtil.toJson(map));
		sysParamService.save(menu);
		
		return ResultUtil.getSuccessMsg();
	}
 
	@RequestMapping(value="edit",method=RequestMethod.GET)
	public  String edit(String key,Model model){
		SysParam  sysParamMenu  = sysParamService.findParam(SysParamHelper.WX_MP_MENU, SysParamType.WX_MP_CONFIG);
		String sysOpention = sysParamMenu.getSysOption();
		String buttonArrayJson = JSON.parseObject(sysOpention).getString("button");	
		List<WXButtion> buttonList =  JSON.parseArray(buttonArrayJson,WXButtion.class);
		
		String url ="";
		
		for (WXButtion wxButtion : buttonList) {
			if(!wxButtion.getKey().equals(key)){
				for (WXButtion wxSubButtion : wxButtion.getSub_button()) {
					if(wxSubButtion.getKey().equals(key)){
						model.addAttribute("wxSubButtion", wxButtion);
						model.addAttribute("wxButtion", wxSubButtion);
						
						url = wxSubButtion.getUrl();
					}
				}
			}else{
				model.addAttribute("wxButtion", wxButtion);
				url =wxButtion.getUrl();
			}
		}
		 Map<String,String> parseMap= parseUrl(url);
		 if(!StringUtils.isEmpty(parseMap.get("redirect_uri"))){
			 model.addAttribute("state",true);
			 
//			 SysParam  accreditUrl  = sysParamService.findParam(SysParamHelper.WX_MP_MENU_ACCREDIT_URL,SysParamType.WX_MP_CONFIG);
			
			 String redirect_uri =  parseMap.get("redirect_uri");
			 
			 model.addAttribute("redirect_uri", redirect_uri);
		 }
		
		
		
		SysParam  sysParamBnType  = sysParamService.findParam(SysParamHelper.WX_MP_BUTTON_TYPE, SysParamType.WX_MP_CONFIG);
		model.addAttribute("sysParamBnType", sysParamBnType);
		
		return "wxMpCustomMenu/wxMpCustomMenuEdit";
	}
	/**
	 * 修改
	 * @param wxButtion
	 * @return
	 */
	@RequestMapping(value="edit",method=RequestMethod.POST)
	public @ResponseBody String edit(WXButtion wxButtion,@RequestParam(name="state",defaultValue="false",required=false)Boolean state){
		SysParam  sysParamMenu  = sysParamService.findParam(SysParamHelper.WX_MP_MENU, SysParamType.WX_MP_CONFIG);
		String sysOpention = sysParamMenu.getSysOption();
		String buttonArrayJson = JSON.parseObject(sysOpention).getString("button");	
		List<WXButtion> buttonList =  JSON.parseArray(buttonArrayJson,WXButtion.class);
		
		String type = wxButtion.getType();
		
		if(state){
			if("view".equals(type) || "miniprogram".equals(type)){
				SysParam  sysParamOAuth  = sysParamService.findParam(SysParamHelper.WX_MP_OAUTH,SysParamType.WX_MP_CONFIG);
				if(!StringUtils.isEmpty(sysParamOAuth.getValue())){
					 String oAuthPath= sysParamOAuth.getValue();
					 String requestUrl = MessageFormat.format(oAuthPath,wxButtion.getUrl());
					 wxButtion.setUrl(requestUrl);
				}
			}
		}
		
		
		for (WXButtion wxButtion2 : buttonList) {
			if (!wxButtion2.getKey().equals(wxButtion.getKey())) {
				List<WXButtion> wxButtionList2 = wxButtion2.getSub_button();
				for (WXButtion wxButtion4 : wxButtionList2) {
					if (wxButtion4.getKey().equals(wxButtion.getKey())) {
						wxButtion4.setAppid(wxButtion.getAppid());
						wxButtion4.setMedia_id(wxButtion.getMedia_id());
						wxButtion4.setName(wxButtion.getName());
						wxButtion4.setPagepath(wxButtion.getPagepath());
						wxButtion4.setType(wxButtion.getType());
						wxButtion4.setUrl(wxButtion.getUrl());
						
						Map<String,String> parseMap= parseUrl(wxButtion4.getUrl());
						 if(!StringUtils.isEmpty(parseMap.get("redirect_uri"))){
							 String url=replaceAccessTokenReg(wxButtion4.getUrl(), "redirect_uri",URLEncoder.encode(wxButtion.getUrl()));
							 wxButtion2.setUrl(url);
						 }
					}
				}
			} else {
				
				wxButtion2.setAppid(wxButtion.getAppid());
				wxButtion2.setMedia_id(wxButtion.getMedia_id());
				wxButtion2.setName(wxButtion.getName());
				wxButtion2.setPagepath(wxButtion.getPagepath());
				wxButtion2.setType(wxButtion.getType());
				
				 Map<String,String> parseMap= parseUrl(wxButtion2.getUrl());
				 if(!StringUtils.isEmpty(parseMap.get("redirect_uri"))){
					 String url=replaceAccessTokenReg(wxButtion2.getUrl(), "redirect_uri",URLEncoder.encode(wxButtion.getUrl()));
					 wxButtion2.setUrl(url);
				 }
				
			}
		}
 
		
		Map<String,List<WXButtion>>  map = new  HashMap<String,List<WXButtion>>();
		map.put("button", buttonList);
		
		SysParam  menu  = sysParamService.findParam(SysParamHelper.WX_MP_MENU, SysParamType.WX_MP_CONFIG);
		menu.setSysOption(JsonUtil.toJson(map));
		sysParamService.save(menu);
		
		return ResultUtil.getSuccessMsg();
	}
	/**
	 * 删除
	 * @param key
	 * @return
	 */
	@RequestMapping(value="delete",method=RequestMethod.GET)
	public @ResponseBody String delete(String key){
		
		SysParam  sysParamMenu  = sysParamService.findParam(SysParamHelper.WX_MP_MENU, SysParamType.WX_MP_CONFIG);
		String sysOpention = sysParamMenu.getSysOption();
		String buttonArrayJson = JSON.parseObject(sysOpention).getString("button");	
		List<WXButtion> buttonList =  JSON.parseArray(buttonArrayJson,WXButtion.class);
		
		for (WXButtion wxButtion : buttonList) {
			if(!wxButtion.getKey().equals(key)){
				for (WXButtion wxButtion2 : wxButtion.getSub_button()) {
					if(wxButtion2.getKey().equals(key)){
						wxButtion.getSub_button().remove(wxButtion2);
						break;
					}
				}
			}else{
				buttonList.remove(wxButtion);
				break;
			}
			
		}
		
		Map<String,List<WXButtion>>  map = new  HashMap<String,List<WXButtion>>();
		map.put("button", buttonList);
		
		SysParam  menu  = sysParamService.findParam(SysParamHelper.WX_MP_MENU, SysParamType.WX_MP_CONFIG);
		menu.setSysOption(JsonUtil.toJson(map));
		sysParamService.save(menu);
		
		return ResultUtil.getSuccessMsg();
	}
	
	/**
	 * 移动 菜单
	 * @param key
	 * @param state
	 * @return
	 */
	@RequestMapping(value="rmoveMenu" ,method=RequestMethod.GET)
	public @ResponseBody String rmoveMenu(String key,Boolean state){
		
		SysParam  sysParamMenu  = sysParamService.findParam(SysParamHelper.WX_MP_MENU, SysParamType.WX_MP_CONFIG);
		String sysOpention = sysParamMenu.getSysOption();
		String buttonArrayJson = JSON.parseObject(sysOpention).getString("button");	
		List<WXButtion> buttonList =  JSON.parseArray(buttonArrayJson,WXButtion.class);
 
		for (int i = 0; i < buttonList.size(); i++) {
			WXButtion wxButtion = (WXButtion)  buttonList.get(i);
			if(!wxButtion.getKey().equals(key)){
				List<WXButtion>  wxButtionList= wxButtion.getSub_button();	
				for (int j = 0; j < wxButtionList.size(); j++) {
					WXButtion wxButtion2 = (WXButtion)  wxButtionList.get(j);
					if(wxButtion2.getKey().equals(key)){
						int num = 0;
						if (state) {
							if (j > 0) {
								num = j - 1;
							}
						}else {
							num = j + 1;
							if(num >=wxButtionList.size()){
								break;
							}
						}
						Collections.swap(wxButtionList, j, num);
						break;
					}
				}
			} else {
				int num = 0;
				if (state) {
					if (i > 0) {
						num = i - 1;
					}
				} else {
					num = i + 1;
				}
				Collections.swap(buttonList, i, num);
				break;
			}
		}
 
		
		
		Map<String,List<WXButtion>>  map = new  HashMap<String,List<WXButtion>>();
		map.put("button", buttonList);
		
		SysParam  menu  = sysParamService.findParam(SysParamHelper.WX_MP_MENU, SysParamType.WX_MP_CONFIG);
		menu.setSysOption(JsonUtil.toJson(map));
		sysParamService.save(menu);
		
		return ResultUtil.getSuccessMsg();
	}
	
	/**
	 * 获取url 参数
	 * @param url
	 * @return
	 */
	public static Map<String, String> parseUrl(String url) {
        Map<String, String> map =  new HashMap<String, String>();
         
        if (url != null && url.indexOf("&") > -1 && url.indexOf("=") > -1) {
            map = new HashMap<String, String>();
             
            String[] arrTemp = url.split("&");          
            for (String str : arrTemp) {
                String[] qs = str.split("=");
                map.put(qs[0], qs[1]);
            }
        }
         
        return map;
    }
	
    public static String replaceAccessTokenReg(String url, String name, String accessToken) {  
        if (!StringUtils.isEmpty(url) && !StringUtils.isEmpty(accessToken)) {  
            url = url.replaceAll("(" + name + "=[^&]*)", name + "=" + accessToken);  
        }  
        return url;  
    }  
	
}
