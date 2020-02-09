package cn.bohoon.product.web;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.collections.keyvalue.TiedMapEntry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.bohoon.framework.orm.domain.Page;
import cn.bohoon.framework.util.DateUtil;
import cn.bohoon.framework.util.JsonUtil;
import cn.bohoon.main.domain.LoginUser;
import cn.bohoon.main.domain.UserSession;
import cn.bohoon.main.system.entity.Operator;
import cn.bohoon.main.system.service.OperatorLogService;
import cn.bohoon.main.system.service.OperatorService;
import cn.bohoon.product.entity.Attr;
import cn.bohoon.product.entity.Category;
import cn.bohoon.product.entity.Product;
import cn.bohoon.product.entity.ProductLabel;
import cn.bohoon.product.entity.Sku;
import cn.bohoon.product.service.CategoryService;
import cn.bohoon.product.service.ProductLabelService;
import cn.bohoon.product.service.ProductService;
import cn.bohoon.product.service.SkuService;
import cn.bohoon.timertask.IncreRateTask;
import cn.bohoon.util.ResultUtil;
import javassist.compiler.ast.NewExpr;

@Controller
@RequestMapping(value = "productLabel")
public class ProductLabelController {

    @Autowired
    ProductLabelService productLabelService;
    @Autowired
    ProductService productService;
    @Autowired
    SkuService skuService;
    @Autowired
    OperatorLogService operatorLogService;
    @Autowired
    CategoryService categoryService;
    @Autowired
    OperatorService operatorService;
    
    @RequestMapping(value = "list", method = RequestMethod.GET)
    public String list(HttpServletRequest request,Model model ,String name, String startTime, String endTime)  throws Exception {
        Integer pageNo=ServletRequestUtils.getIntParameter(request, "pageNo",1);
        Page<ProductLabel> pageProductLabel=new Page<ProductLabel>();
        pageProductLabel.setPageNo(pageNo);
        String hql = " from ProductLabel p where 1 = 1 ";
        List<Object> params = new ArrayList<>();
        if(!StringUtils.isEmpty(name)){
    		hql = hql + " and p.name like ? ";
    		params.add('%'+name+'%');
    		model.addAttribute("name", name);
    	}
        if (!StringUtils.isEmpty(startTime)){
    		hql = hql + " and p.createTime >= ? ";
    		params.add(DateUtil.switchStringToDate(startTime, "yy-MM-dd"));
    		model.addAttribute("startTime", startTime);
        }
        if (!StringUtils.isEmpty(endTime)){
        	hql = hql + " and p.createTime < ? ";
        	params.add(DateUtil.getNDayAfter(endTime, 1));
        	model.addAttribute("endTime", endTime);
        }
        hql = hql+ "order by sort desc ";
        pageProductLabel=productLabelService.page(pageProductLabel, hql, params.toArray());
        
        List<String>  defaultData = productLabelService.getDefaultData();
        model.addAttribute("defaultData", defaultData);
        model.addAttribute("pageProductLabel", pageProductLabel);
        return "productLabel/productLabelList";
    }
    
    @RequestMapping(value = "add", method = RequestMethod.GET)
    public String addGet(HttpServletRequest request,Model model)  {
        return "productLabel/productLabelAdd";
    }
    
    @ResponseBody
    @RequestMapping(value = "add", method = RequestMethod.POST)
    public String addPost(HttpServletRequest request,ProductLabel productLabel) throws Exception  {
    	productLabel.setCreateTime(new Date());
    	ProductLabel pll = productLabelService.select(" from ProductLabel where name = ? ", productLabel.getName());
    	if(pll != null){
    		return  ResultUtil.getError("名称已存在！");
    	}
    	productLabelService.save(productLabel);
    	//保存日志,HttpServletRequest request
   		LoginUser userif= UserSession.getLoginUser(request);
		Operator operator = operatorService.findUserByUsername(userif.getUsername());
		operatorLogService.addUserLog(operator, request, "新增标签:"+productLabel.getName());
		return ResultUtil.getSuccessMsg();
    }
    
    @RequestMapping(value = "edit", method = RequestMethod.GET)
    public String editGet(HttpServletRequest request,Model model,Boolean editState) {
    	  /* Integer id=ServletRequestUtils.getIntParameter(request, "id","");*/
        Integer id=ServletRequestUtils.getIntParameter(request, "id",-1);
        ProductLabel productLabel=productLabelService.get(id);
        
        model.addAttribute("editState", editState);
        model.addAttribute("item",productLabel);
        System.out.println("名字"+productLabel.getName());
        
        return "productLabel/productLabelEdit";
    }

	@ResponseBody
    @RequestMapping(value = "edit", method = RequestMethod.POST)
    public String editPost(ProductLabel productLabel,HttpServletRequest request) throws Exception {
		
        productLabelService.update(productLabel);
        //保存日志,HttpServletRequest request
   		LoginUser userif= UserSession.getLoginUser(request);
		Operator operator = operatorService.findUserByUsername(userif.getUsername());
		operatorLogService.addUserLog(operator, request, "修改标签:"+productLabel.getName());
        return ResultUtil.getSuccessMsg();
    }

	@ResponseBody
    @RequestMapping(value = "/delete")
    public String delete(HttpServletRequest request,Integer id) throws Exception {
        productLabelService.delete(id);
        //保存日志,HttpServletRequest request
   		LoginUser userif= UserSession.getLoginUser(request);
		Operator operator = operatorService.findUserByUsername(userif.getUsername());
		operatorLogService.addUserLog(operator, request, "删除标签id:"+id.toString());
		return ResultUtil.getSuccessMsg();
    }
	
	@Autowired
	IncreRateTask  increRateTask;

	@RequestMapping(value = "/checkSee",method=RequestMethod.GET)
    public String checkSee(HttpServletRequest request,Model model,String labels,String name,String categoryId) {
		
		Integer pageNo = ServletRequestUtils.getIntParameter(request, "pageNo", 1);
		Page<Product> pagePro = new Page<Product>();
		
		pagePro.setPageNo(pageNo);
		Integer  i=1;
		
		String hql = " from Product where lables  like  ?  and flag=1 ";
		Map<Product,Sku> inventoryMap = new HashMap<Product,Sku>();
		if(pageNo<=1){
			i=1;
		}else{
		 	i=(pageNo-1)*pagePro.getPageSize()+i;
		}
		if(labels.equals("热卖")){
			 List<Object> params2 = new ArrayList<>();
			
				String hql2=" from Product  where flag=1";
			
			 if(!StringUtils.isEmpty(name)){
			    	hql2+=" and name like ?";
			    	params2.add('%'+name+'%');		
			    	model.addAttribute("name",name);
			    } 
			 //选择分类ID
			 if (!StringUtils.isEmpty(categoryId)) {
				 	hql2+=" and categoryId = ?";
			    	params2.add(categoryId);		
			    	model.addAttribute("categoryId",categoryId);
				}
			 if(!StringUtils.isEmpty(labels)){
			    	hql2+=" and lables like ? ";
			    	params2.add('%'+labels+'%');		
			    	model.addAttribute("labels",labels);
			    }
			 hql2+=" order by showSort asc,salesNum desc ";
			 pagePro = productService.page(pagePro, hql2,params2.toArray());
			 
			     Iterator<Product> iterator = pagePro.getResult().iterator();
			     while(iterator.hasNext()){
			    	 Product pro = iterator.next();
			    	 if(null!= pro.getId()) {
					if(null == pro.getShowSort()) {
						pro.setShowSort(i);
					}
					       i++;
							Sku sku=skuService.getSkuByPorductId(pro.getId());
							if(null != sku) {
								inventoryMap.put(pro, sku) ;
							
							}
							
						}
			    	 
			     }
			  
			     productService.saveBatch(pagePro.getResult(), pagePro.getResult().size());
			     
		}else if (labels.equals("新品")){
				String hql3=" from Product where lables like  '%新品%' ";
				List<Object> params3=new ArrayList<Object>();
				 if(!StringUtils.isEmpty(name)){
				    	hql3+=" and name like ?";
				    	params3.add('%'+name+'%');		
				    	model.addAttribute("name",name);
				    }
				 //选择分类ID
				 if (!StringUtils.isEmpty(categoryId)) {
						 hql3+=" and categoryId = ?";
						 params3.add(categoryId);		
						 model.addAttribute("categoryId",categoryId);
					}
				 hql3+=" order by showSort asc,showSort asc  ";
				 pagePro = productService.page(pagePro, hql3,params3.toArray());
						 for(Product pro:pagePro.getResult()){
							 if(null==pro.getShowSort()){
								 pro.setShowSort(i);
							 }
							 i++;
							 System.out.println("排序"+pro.getShowSort());
						 }
					
				 productService.saveBatch(pagePro.getResult(), pagePro.getResult().size());

		}else{
			List<Object> params = new ArrayList<>();
			params.add('%' + labels + '%');
			if (!StringUtils.isEmpty(name)) {
				hql += " and name like ?";
				params.add('%'+ name+'%');
				model.addAttribute("name", name);
			}
			 //选择分类ID
			 if (!StringUtils.isEmpty(categoryId)) {
					 hql+=" and categoryId = ?";
					 params.add(categoryId);		
					model.addAttribute("categoryId",categoryId);
				}
			hql+=" order by showSort asc,presaleNums desc ";
			pagePro = productService.page(pagePro, hql, params.toArray());
			
			for (Product pro : pagePro.getResult()) {
				if (null != pro.getId()) {
					if (null == pro.getShowSort()) {
						pro.setShowSort(i);
					}
					i++;
					Sku sku = skuService.getSkuByPorductId(pro.getId());
					if (null != sku) {
						inventoryMap.put(pro, sku);
					}
				}
			}
			productService.saveBatch(pagePro.getResult(), pagePro.getResult().size());
		}
		
		//分类筛选
		List<Category> categorys=categoryService.list("from Category where level=3");
		model.addAttribute("categorys", categorys);
		model.addAttribute("inventoryMap", inventoryMap);
		model.addAttribute("pagePro", pagePro);
		model.addAttribute("labels", labels);

        return "productLabel/checkSee";
    }
	
	@ResponseBody
    @RequestMapping(value = "/updateShow")
    public String  updateSort(HttpServletRequest request,Integer id,Integer showSort) throws Exception {
        Product product = productService.get(id);
        product.setShowSort(showSort);
        productService.update(product);
        //保存日志,HttpServletRequest request
   		LoginUser userif= UserSession.getLoginUser(request);
		Operator operator = operatorService.findUserByUsername(userif.getUsername());
		operatorLogService.addUserLog(operator, request, "修改商品标签排序");
		return ResultUtil.getSuccessMsg();
    }
	/**
	 * 货品分类
	 * 
	 * @param str
	 * @param pageNo
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/getCategory",method = RequestMethod.GET)
	public String getAttr(HttpServletRequest request){
		Integer pageNo = ServletRequestUtils.getIntParameter(request, "pageNo", 1);
		String str = ServletRequestUtils.getStringParameter(request, "str", "") ;
		Page<Category> attrPage = new Page<Category>();
		attrPage.setPageNo(pageNo);
		String hql ="from Category where level=3";
		List<Object> params = new ArrayList<>();
		 
		if(!StringUtils.isEmpty(str)){
			hql +=" and name like  ? ";
			params.add("%"+str+"%");
		}
		attrPage = categoryService.page(attrPage, hql,params.toArray());
		return JsonUtil.toJson(attrPage);
	}
}