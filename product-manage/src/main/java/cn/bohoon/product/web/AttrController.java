package cn.bohoon.product.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.bohoon.framework.orm.domain.Page;
import cn.bohoon.framework.util.JsonUtil;
import cn.bohoon.main.domain.LoginUser;
import cn.bohoon.main.domain.UserSession;
import cn.bohoon.main.system.entity.Operator;
import cn.bohoon.main.system.service.OperatorLogService;
import cn.bohoon.main.system.service.OperatorService;
import cn.bohoon.product.entity.Attr;
import cn.bohoon.product.entity.AttrValue;
import cn.bohoon.product.entity.PSpecificationsP;
import cn.bohoon.product.entity.Product;
import cn.bohoon.product.service.AttrService;
import cn.bohoon.product.service.AttrValueService;
import cn.bohoon.product.service.PSpecificationsPService;
import cn.bohoon.product.service.ProductService;
import cn.bohoon.product.service.SkuAttrService;
import cn.bohoon.product.service.SkuSpecificationsSkuService;
import cn.bohoon.util.ResultUtil;

@Controller
@RequestMapping(value = "attr")
public class AttrController {

	@Autowired
	AttrService attrService;
	@Autowired
	AttrValueService attrValueService;
	@Autowired
	SkuAttrService skuAttrService;
	@Autowired
	PSpecificationsPService pSpecificationsPService;
	
	@Autowired
	ProductService productService;
    @Autowired
    OperatorLogService operatorLogService;
    @Autowired
    OperatorService operatorService;

	/**
	 * 货品属性
	 * 
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "list")
	public String list(Model model, HttpServletRequest request) {
		Integer pageNo = ServletRequestUtils.getIntParameter(request, "pageNo", 1);
		String name = ServletRequestUtils.getStringParameter(request, "name", "") ;
		Page<Attr> pageAttr = new Page<Attr>();
		pageAttr.setPageNo(pageNo);
		String hql = " from Attr a where 1 = 1 ";
		List<Object> params = new ArrayList<>();
		if (!StringUtils.isEmpty(name)) {
			hql = hql + " and a.name like ? ";
			params.add('%' + name + '%');
			model.addAttribute("name", name);
		}
		pageAttr = attrService.page(pageAttr, hql, params.toArray());
		Map<Attr, List<AttrValue>> mapAttrValue = new HashMap<Attr, List<AttrValue>>();
		for(Attr attr : pageAttr.getResult()){
			List<AttrValue> vList = attrValueService.list("from AttrValue where status = 1 and attrId = ? order by sort asc", attr.getId());
			mapAttrValue.put(attr, vList);
		}
		model.addAttribute("mapAttrValue", mapAttrValue);
		model.addAttribute("pageAttr", pageAttr);
		return "attr/attrList";
	}
	
	
	/**
	 * 货品属性
	 * 
	 * @param str
	 * @param pageNo
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/getAttr",method = RequestMethod.GET)
	public String getAttr(HttpServletRequest request){
		Integer pageNo = ServletRequestUtils.getIntParameter(request, "pageNo", 1);
		String str = ServletRequestUtils.getStringParameter(request, "str", "") ;
		Page<Attr> attrPage = new Page<Attr>();
		attrPage.setPageNo(pageNo);
		String hql =" from Attr where 1 = 1";
		List<Object> params = new ArrayList<>();
		 
		if(!StringUtils.isEmpty(str)){
			hql +=" and name like  ? ";
			params.add("%"+str+"%");
		}
		attrPage = attrService.page(attrPage, hql,params.toArray());
		return JsonUtil.toJson(attrPage);
	}
	
	@ResponseBody
	@RequestMapping(value="/getAttrAndValues",method = RequestMethod.GET)
	public Map<String,Object> getAttrAndValues(Integer id){
		//获取当前规格
		Attr attr = attrService.get(id);
		//获取当前规格 对应的值
		List<AttrValue> vList = attrValueService.list("from AttrValue where status = 1 and attrId = ? order by sort asc", attr.getId());
		Map<String,Object> result = new HashMap<>() ;
		//当前规格的名称
		String check = "<input type='checkbox' name='select-attr' value='"+id+"' class='select-attr'>"+attr.getName() ;
		//存储在 MAP中
		result.put("check", check) ;
		//使用拼接字符串进行拼接
		//标题
		String attrTr = "<tr id='select-attr"+id+"' style='display:none;' class='select-attr-tr'>" ;
		attrTr += " <td><strong>"+attr.getName()+"</strong></td>" ;
		attrTr += " <td>" ;
		attrTr += " <div class='icheck-inline' style='margin-top: 0'>" ;
		//循环拼接值
		for(AttrValue attrValue : vList) {
			
			attrTr += " <label style='margin-bottom: 0'>" ;
			attrTr += " <input type='checkbox' class='icheck selectValue' value='"+attrValue.getId()+"' >" ;
			attrTr += " <span>"+attrValue.getName()+"</span>" ;
			attrTr += " </label>" ;
			
			attrTr +=  "<input value='' type='hidden' class='inputImage'>";
			attrTr +=  "<img alt='图' class='attrImg'   src='/assets/images/skuDefaultImage.jpg'      width='23px' height='23px'>";
			attrTr +=  "<input type='file' class='imgfile' style='display: none;'  accept='.jpg,.bmp,.jpeg,.png'>";
			attrTr +=  "<div class='qrMl' style='display: none; z-index: 999; position: absolute; background-color: #fff; width: 200px;height: 200px;box-shadow: 5px 5px rgba(102,102,102,.1);border: 1px solid #eee;'>";
			attrTr +=  "<button type='button' class='close close-click' style='margin: 10px;'></button>";
			attrTr +=  "<img src='' style='width: 151px;height: 151px;margin-left: 20px'>";
			attrTr +=  "</div>";
			attrTr += "<i style='color: red;'  t_srot='"+attrValue.getSort()+"'   class='fa fa-remove del-attrValue'></i>";
			
		}
		
		attrTr += " <label class='form-group form-md-line-input has-success' style='padding-top: 0px !important; margin-top:3px;margin-bottom: -10px !important;margin-left: 10px !important;'>";
		attrTr += "<div class='col-md-6'>";
		attrTr += "<input t_attrId='"+id+"' type='text' class='form-control form_control_1'  placeholder='规格值'>";
		attrTr += "<span class='form-control-focus'> </span>";
		attrTr += "</div>";
		attrTr += "</label>";
		
		attrTr += " </div>" ;
		attrTr += " </td>" ;
		attrTr += " </tr>" ;
		result.put("attrTr", attrTr) ;
		return result ;
	}

	@InitBinder(value="attr")  
	public void initBinder2(WebDataBinder binder){  
		binder.setFieldDefaultPrefix("attr.");  
	}
	
	@InitBinder(value="attrValue")  
	public void initBinder1(WebDataBinder binder){  
		binder.setFieldDefaultPrefix("attrValue.");  
	}
	
	@RequestMapping(value = "add", method = RequestMethod.GET)
	public String addGet(Model model) {
		return "attr/attrAdd";
	}
	@RequestMapping(value = "addFromProduct", method = RequestMethod.GET)
	public String addFromProductGet(Model model) {
		return "attr/addFromProduct";
	}
	@RequestMapping(value = "specificationsadd", method = RequestMethod.GET)
    	public  String specificationsaddGet(Model model , Integer productId) {
			List<Product> products=new ArrayList<Product>();
			String name="";
		    List<PSpecificationsP> psp= pSpecificationsPService.list("from PSpecificationsP where mainPId=?",productId);
		    for (PSpecificationsP pspp : psp) {
		    	name=pspp.getName();
				Product product= productService.select("from Product where id=?", pspp.getVicePId());
				products.add(product);
			}
		    model.addAttribute("products", products);
		    model.addAttribute("name", name);
		    model.addAttribute("productId", productId);
            return "attr/specificationsadd";
    }
	
	@ResponseBody
	@RequestMapping(value = "add",method=RequestMethod.POST)
	public String addPost(Attr attr, AttrValue attrValue, Integer[] sort,HttpServletRequest request) throws Exception{
		if(StringUtils.isEmpty(attrValue.getName()))
			return ResultUtil.getError("属性值不能为空");
		String[] name = attrValue.getName().split(",");
		
		Set<String> set = new HashSet<String>();
		for(String name1 : name){
			if(StringUtils.isEmpty(name1)) {
				return ResultUtil.getError("属性值不能为空");
			}
			
			set.add(name1);
		}
		if(set.size() != name.length) {
			return ResultUtil.getError("属性值不能重复");
		}
			
		List<AttrValue> attrValueList = new ArrayList<>();
		for(int i=0;i<name.length;i++){
			AttrValue aValue = new AttrValue();
			aValue.setName(name[i]);
			aValue.setSort(0);
			if(sort.length >i) {
				aValue.setSort(sort[i]);
			}
			attrValueList.add(aValue);
		}
		attrService.saveAttrAndAttrValue(attr, attrValueList);
		//保存日志,HttpServletRequest request
   		LoginUser userif= UserSession.getLoginUser(request);
		Operator operator = operatorService.findUserByUsername(userif.getUsername());
		operatorLogService.addUserLog(operator, request, "新增规格:"+attr.getName());
		
		return ResultUtil.getSuccessMsg();
	}

	@RequestMapping(value="edit", method=RequestMethod.GET)
	public String editGet(HttpServletRequest request,Model model,Integer id){
		Attr attr = attrService.get(id);
		List<AttrValue> listAttrValue = attrValueService.list("from AttrValue where status = 1 and attrId = ? order by sort asc", attr.getId());
		model.addAttribute("attr", attr);
		model.addAttribute("listAttrValue", listAttrValue);
		return "attr/attrEdit";
	}
	
	@ResponseBody
	@RequestMapping(value = "edit",method=RequestMethod.POST)
	public String editPost(HttpServletRequest request,Attr attr,AttrValue attrValue,Integer[] sort,Integer[] ids) throws Exception{
		if(StringUtils.isEmpty(attrValue.getName())){
			return ResultUtil.getError("属性值不能为空");
		}
		String[] name = attrValue.getName().split(",");
		Attr a = attrService.select("from Attr where name = ? and id <> ?", attr.getName(),attr.getId());
		if(a!=null){
			return ResultUtil.getError("属性名称重复");
		}
		Set<String> set = new HashSet<String>();
		for(String name1 : name){
			if(StringUtils.isEmpty(name1)){
				return ResultUtil.getError("属性值不能为空");
			}
			set.add(name1);
		}
		if(set.size() != name.length){
			return ResultUtil.getError("属性值不能重复");
		}
			 
		List<AttrValue> attrValueList = new ArrayList<>();
		for(int i=0;i<name.length;i++){
			AttrValue attrValue1 = new AttrValue();
			attrValue1.setName(name[i]);
			attrValue1.setSort(sort[i]);
			if(i<ids.length)
				attrValue1.setId(ids[i]);
			attrValueList.add(attrValue1);
		}
		attrService.saveAttrAndAttrValue(attr, attrValueList);
		//保存日志,HttpServletRequest request
   		LoginUser userif= UserSession.getLoginUser(request);
		Operator operator = operatorService.findUserByUsername(userif.getUsername());
		operatorLogService.addUserLog(operator, request, "修改规格:"+attr.getName());
		return ResultUtil.getSuccessMsg();
	}
	
	@ResponseBody
	@RequestMapping(value="delete" , method=RequestMethod.GET)
	public String deleteGet(Integer id,HttpServletRequest request) throws Exception{
		String hql = "select count(1) from SkuAttr where  attrId=?" ; 
		Long num = skuAttrService.select(hql, Long.class, id);
		
		if(num > 0){
			return ResultUtil.getError("该规格已被引用 不能删除!");
		}
		attrService.delete(id);
		attrValueService.execute(" delete from AttrValue where attrId=?", id) ;
		//保存日志,HttpServletRequest request
   		LoginUser userif= UserSession.getLoginUser(request);
		Operator operator = operatorService.findUserByUsername(userif.getUsername());
		operatorLogService.addUserLog(operator, request, "删除规格id:"+id.toString());
		return ResultUtil.getSuccessMsg() ;
	}
	
	/**
	 * 删除规则值
	 * @param attrValueId 规格值ID 
	 * @return
	 * @throws Exception 
	 */
	@ResponseBody
	@RequestMapping(value="deleteValue" , method=RequestMethod.GET)
	public String deleteValue(Integer attrValueId,HttpServletRequest request) throws Exception{
		String hql = "select count(1) from SkuAttr where  attrValueId=?" ; 
		Long num = skuAttrService.select(hql, Long.class, attrValueId);
		
		if(num > 0){
			return ResultUtil.getError("该规格值已被引用 不能删除!");
		}
		String sql = " select count(avl) from AttrValue avl ,AttrValue av2 ,Attr at where avl.attrId = at.id and av2.attrId = at.id  and av2.id="+attrValueId ;
		Long avls = attrValueService.select(sql, Long.class) ;
		if(avls <= 1){
			return ResultUtil.getError("至少保留一个规格值！");
		}
		attrValueService.delete(attrValueId);
		//保存日志,HttpServletRequest request
   		LoginUser userif= UserSession.getLoginUser(request);
		Operator operator = operatorService.findUserByUsername(userif.getUsername());
		operatorLogService.addUserLog(operator, request, "删除规格值id:"+attrValueId.toString());
		return ResultUtil.getSuccessMsg() ;
	}
	
	/**
	 * 添加属性值
	 * @param attrId 属性ID
	 * @param AttrValue 属性值 名
	 * @param srot 排序
	 * @return  
	 * @throws Exception 
	 */
	@ResponseBody
	@RequestMapping(value="addAttrValue",method=RequestMethod.POST)
	public String addAttrValue(Integer attrId,String AttrValue,Integer srot,HttpServletRequest request) throws Exception{
//		AttrValue source = attrValueService.select(" from AttrValue where name = ? ", AttrValue);
//		if(source!=null){
//			return ResultUtil.getMessage("属性值重复 请重新输入");
//		}
		
		 AttrValue attrvalue= new AttrValue();
		 attrvalue.setName(AttrValue);
		 attrvalue.setSort(srot+1);
		 attrvalue.setAttrId(attrId);
		 attrvalue.setStatus(true);
		 attrValueService.save(attrvalue);
		//保存日志,HttpServletRequest request
   		LoginUser userif= UserSession.getLoginUser(request);
		Operator operator = operatorService.findUserByUsername(userif.getUsername());
		operatorLogService.addUserLog(operator, request, "新增规格值:"+attrvalue.getName());
		
		return ResultUtil.getData(1, "操作成功!", JsonUtil.toJson(attrvalue));
	}
	
}
