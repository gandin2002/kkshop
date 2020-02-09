package cn.bohoon.stock.web;

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

import cn.bohoon.framework.orm.domain.Page;
import cn.bohoon.main.domain.LoginUser;
import cn.bohoon.main.domain.UserSession;
import cn.bohoon.main.system.entity.Operator;
import cn.bohoon.main.system.service.OperatorLogService;
import cn.bohoon.main.system.service.OperatorService;
import cn.bohoon.product.entity.Sku;
import cn.bohoon.product.entity.SkuWare;
import cn.bohoon.product.service.SkuService;
import cn.bohoon.product.service.SkuWareService;
import cn.bohoon.stock.domain.Purch;
import cn.bohoon.stock.domain.PurchaseState;
import cn.bohoon.stock.entity.PurchReturnItem;
import cn.bohoon.stock.entity.Purchase;
import cn.bohoon.stock.entity.PurchaseItem;
import cn.bohoon.stock.entity.PurchaseReturn;
import cn.bohoon.stock.service.PurchReturnItemService;
import cn.bohoon.stock.service.PurchaseItemService;
import cn.bohoon.stock.service.PurchaseReturnService;
import cn.bohoon.stock.service.PurchaseService;
import cn.bohoon.util.ResultUtil;

/**
 * 货品库位管理
 * @author dujianqiao
 *
 */
@Controller
@RequestMapping(value = "purchaseReturn")
public class PurchaseReturnController {
	@Autowired
	SkuService skuService ;
	@Autowired
	SkuWareService skuWareService ;
	@Autowired
	PurchaseService purchaseService ;
	@Autowired
	PurchaseItemService purchaseItemService ;
	@Autowired
	PurchaseReturnService purchaseReturnService ;
	@Autowired
	PurchReturnItemService purchReturnItemService ;

    @Autowired
    OperatorLogService operatorLogService;
    @Autowired
    OperatorService operatorService;
	
	
	/**
	 * 采购退购单列表
	 * 
	 * @param request
	 * @param model
	 * @param corporateName
	 * @return
	 */
	@RequestMapping(value = "list", method = RequestMethod.GET)
	public String list(HttpServletRequest request, Model model) {
		String id = ServletRequestUtils.getStringParameter(request, "id","") ;
		String operator = ServletRequestUtils.getStringParameter(request, "operator","") ;
		String supplierName = ServletRequestUtils.getStringParameter(request, "supplierName","") ;
		String wareHouseName = ServletRequestUtils.getStringParameter(request, "wareHouseName","") ;
		Integer pageNo = ServletRequestUtils.getIntParameter(request, "pageNo", 1);
		Page<PurchaseReturn> _thisPage = new Page<PurchaseReturn>();
		_thisPage.setPageNo(pageNo);
		String hql = "from PurchaseReturn p where 1 = 1 ";
		List<Object> params = new ArrayList<Object>();
		if (!StringUtils.isEmpty(id)) {
			hql = hql + " and p.id like ? ";
			params.add('%' + id + '%');
			model.addAttribute("id", id);
		}
		if (!StringUtils.isEmpty(operator)) {
			hql = hql + " and p.operator like ? ";
			params.add('%' + operator + '%');
			model.addAttribute("operator", operator);
		}
		if (!StringUtils.isEmpty(supplierName)) {
			hql = hql + " and p.supplierName like ? ";
			params.add('%' + supplierName + '%');
			model.addAttribute("operator", supplierName);
		}
		if (!StringUtils.isEmpty(wareHouseName)) {
			hql = hql + " and p.wareHouseName like ? ";
			params.add('%' + wareHouseName + '%');
			model.addAttribute("wareHouseName", wareHouseName);
		}
		hql += " order by id desc" ;
		_thisPage = purchaseReturnService.page(_thisPage, hql, params.toArray());
		model.addAttribute("_thisPage", _thisPage);
		
		return "purchase/purchaseReturnList";
	}
	
	
	/**
	 * 新增采购单
	 * 
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "add", method = RequestMethod.GET)
	public String addGet(HttpServletRequest request, Model model) {
		String id = ServletRequestUtils.getStringParameter(request, "id", "") ;
		if(!StringUtils.isEmpty(id)){
			Purchase purchase = purchaseService.get(id) ;
			String hql = " from PurchaseItem where purchaseId=?" ;
			List<PurchaseItem> items = purchaseItemService.list(hql, id) ;
			model.addAttribute("items", items) ;
			model.addAttribute("purchase", purchase) ;
		}
		String purHql = " from Purchase where state='PASS'  " ;
		List<Purchase> purchaseList = purchaseService.list(purHql) ;
		List<Purchase> canReturnList = new ArrayList<Purchase>() ;
		for(Purchase pu : purchaseList) {
			String hql = " select sum(quantity)  from PurchaseItem where purchaseId=? " ;
			Long count = purchaseItemService.select(hql, Long.class, pu.getId()) ;
			if(count > 0 ) {
				canReturnList.add(pu) ;
			}
		}
		model.addAttribute("purchaseList", canReturnList) ;
		return "purchase/purchaseReturnAdd";
	}

	/**
	 * 保存采购单信息
	 * 
	 * @param request
	 * @param delivery
	 * @return
	 * @throws Exception 
	 */
	@ResponseBody
	@RequestMapping(value = "add", method = RequestMethod.POST)
	public String addPost(HttpServletRequest request, Purch purch) throws Exception {
		try {
			List<PurchReturnItem> itemsReturn = purch.getItemReturn() ;
			if(StringUtils.isEmpty(itemsReturn) || itemsReturn.isEmpty()) {
				return ResultUtil.getError("数据不完整！") ;
			}
			LoginUser operator = UserSession.getLoginUser(request);
			if(!StringUtils.isEmpty(operator)) {
				purch.getPurchaseReturn().setOperator(operator.getUsername());
			}
		} catch (Exception e) {
		}
		String result = purchaseReturnService.save(purch);

 		//保存日志,HttpServletRequest request
       	LoginUser userif= UserSession.getLoginUser(request);
		Operator operator = operatorService.findUserByUsername(userif.getUsername());
		operatorLogService.addUserLog(operator, request, "新增采购退库单");
		return result ;
	}
	
	/**
	 * 查看
	 * 
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "view", method = RequestMethod.GET)
	public String viewGet(HttpServletRequest request, Model model) {
		String id = ServletRequestUtils.getStringParameter(request, "id", "") ;
		PurchaseReturn purchaseReturn = purchaseReturnService.get(id) ;
		String hql = " from PurchReturnItem where purchaseId=?" ;
		List<PurchReturnItem> items = purchReturnItemService.list(hql, id) ;
		model.addAttribute("items", items) ;
		model.addAttribute("purchaseReturn", purchaseReturn) ;
		return "purchase/purchaseReturnView";
	}
	
	/**
	 * 删除采购信息
	 * 
	 * @param request
	 * @param id
	 * @return
	 * @throws Exception 
	 */
	@ResponseBody
	@RequestMapping(value = "/delete")
	public String delete(HttpServletRequest request, String id) throws Exception {
		purchaseReturnService.delete(id);
		purchReturnItemService.execute(" delete from PurchReturnItem where purchaseId=?", id) ;
		//保存日志,HttpServletRequest request
       	LoginUser userif= UserSession.getLoginUser(request);
		Operator operator = operatorService.findUserByUsername(userif.getUsername());
		operatorLogService.addUserLog(operator, request, "删除采购退库单:id"+id);
		return ResultUtil.getSuccessMsg();
	}
	
	
	/**
	 * 审核拒绝
	 * 
	 * @param id
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value = "/refuse", method = RequestMethod.GET)
	@ResponseBody
	public String refuse(HttpServletRequest request, String id) throws Exception {
		PurchaseReturn purchaseReturn = purchaseReturnService.get(id) ;
		purchaseReturn.setState(PurchaseState.REFUSE);
		purchaseReturnService.save(purchaseReturn);
		//保存日志,HttpServletRequest request
       	LoginUser userif= UserSession.getLoginUser(request);
		Operator operator = operatorService.findUserByUsername(userif.getUsername());
		operatorLogService.addUserLog(operator, request, "审核拒绝采购退库单:id"+id);
		return ResultUtil.getSuccessMsg();
	}

	/**
	 * 审核通过
	 * 
	 * @param id
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value = "/pass", method = RequestMethod.GET)
	@ResponseBody
	public String pass(HttpServletRequest request, String id) throws Exception{
		PurchaseReturn purchaseReturn = purchaseReturnService.get(id) ;
		purchaseReturn.setState(PurchaseState.PASS);
		String hql = " from PurchReturnItem where purchaseId=?" ;
		List<PurchReturnItem> items = purchReturnItemService.list(hql, id) ;
		int wareHouseId = purchaseReturn.getWareHouseId() ;
		String purchaseId = purchaseReturn.getPurchaseId() ;
		
		/**
		 * 校验
		 * 
		 */
		for(PurchReturnItem item:items) {
			Integer skuId = item.getSkuId() ;
			String pItemHql  = " from PurchaseItem where purchaseId=? and skuId=? " ;
			
			PurchaseItem pItem = purchaseItemService.select(pItemHql,purchaseId,skuId) ;
			if(null !=pItem && pItem.getQuantity() < item.getQuantity()) {
				return ResultUtil.getError("退购数量大于采购数量！") ;
			}
		}
		
		//出库
		for(PurchReturnItem item:items) {
			Integer skuId = item.getSkuId() ;
			String pItemHql  = " from PurchaseItem where purchaseId=? and skuId=? " ;
			
			PurchaseItem pItem = purchaseItemService.select(pItemHql,purchaseId,skuId) ;
		
			if(!StringUtils.isEmpty(skuId)) {
				Sku sku = skuService.get(item.getSkuId()) ;
				int inventory = null != sku && null != sku.getInventory() ? sku.getInventory() : 0 ;
				int quantity = null != item.getQuantity() ? item.getQuantity() : 0 ;
				inventory = inventory - quantity ;
				sku.setInventory(inventory);
				skuService.save(sku);
				SkuWare skuWare = skuWareService.select(" from SkuWare where skuId=? and wareHouseId=? ", item.getSkuId(),wareHouseId) ;
				if(null == skuWare) {
					skuWare = new SkuWare() ;
					skuWare.setSkuId(sku.getId());
					skuWare.setQuantity(quantity);
					skuWare.setWareHouseId(wareHouseId);
					skuWare.setWareHouseName(purchaseReturn.getWareHouseName());
				} else {
					int skuWareQuantity = null != skuWare.getQuantity() ? skuWare.getQuantity() : 0 ;
					skuWareQuantity = skuWareQuantity - quantity ;
					skuWare.setQuantity(skuWareQuantity);
				}
				skuWareService.save(skuWare);
				if(null != pItem) {
					pItem.setQuantity(pItem.getQuantity() - quantity);
					purchaseItemService.save(pItem);
				}
			}
			
		}
		purchaseReturn.setOutWareHouseTime(new Date());
		purchaseReturnService.save(purchaseReturn) ;
		//保存日志,HttpServletRequest request
       	LoginUser userif= UserSession.getLoginUser(request);
		Operator operator = operatorService.findUserByUsername(userif.getUsername());
		operatorLogService.addUserLog(operator, request, "审核通过采购退库单:id"+id);
		return ResultUtil.getSuccessMsg();
	}
	
	
	
}
