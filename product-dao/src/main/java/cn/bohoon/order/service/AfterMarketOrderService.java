package cn.bohoon.order.service;

import java.math.BigDecimal;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.bohoon.framework.service.BaseService;
import cn.bohoon.order.dao.AfterMarketOrderDao;
import cn.bohoon.order.domain.OperateIdentity;
import cn.bohoon.order.domain.OrderActivity;
import cn.bohoon.order.domain.ReOrderType;
import cn.bohoon.order.entity.AfterMarketOrder;
import cn.bohoon.order.entity.AmOrderItem;
import cn.bohoon.order.entity.OrderItem;
import cn.bohoon.product.entity.Sku;
import cn.bohoon.product.service.SkuService;
import cn.bohoon.util.ConvertUtils;

@Service
@Transactional
public class AfterMarketOrderService extends BaseService<AfterMarketOrder,String>{
	
	@Autowired
	SkuService skuService ;
	@Autowired
	AfterMarketOrderDao afterMarketOrderDao;
	@Autowired
	OrderItemService orderItemService;
	@Autowired
	AmOrderLogService amOrderLogService ;
	@Autowired
	AmOrderItemService amOrderItemService ;
	
	Logger logger = LoggerFactory.getLogger(AfterMarketOrderService.class) ;

    @Autowired
    AfterMarketOrderService(AfterMarketOrderDao afterMarketOrderDao){
        super(afterMarketOrderDao);
    }

    /**
     * 创建售后订单
     * 
     * @param amOrder
     * @param itemId
     * @throws Exception
     */
	public void createOrder(AfterMarketOrder amOrder, String itemId) throws Exception {
		
		Integer[] itIds = ConvertUtils.parseIntArr(itemId) ;
		BigDecimal reFundFee = new BigDecimal(0) ;
		for(Integer itd : itIds) {
			OrderItem oit = orderItemService.get(itd) ;
			String skuId=oit.getSkuId();
			AmOrderItem aoi = new AmOrderItem(oit) ;
			aoi.setAmOrderId(amOrder.getId());
			aoi.setOrderId(amOrder.getOrderId());
			Sku sku=skuService.select("from Sku where id="+skuId);
			reFundFee = reFundFee.add(oit.getPrice().multiply(new BigDecimal(oit.getQuantity()/sku.getTranslateRate()))) ;
			
			amOrderItemService.save(aoi);
		}
		amOrder.setReFundFee(reFundFee);
		afterMarketOrderDao.save(amOrder) ;
		
	}
	
	/**
	 * 创建售后订单
	 * @param amOrder
	 */
	public void createOrder(AfterMarketOrder amOrder) {
		String hql = "from OrderItem where orderId=?" ;
		List<OrderItem> oits = orderItemService.list(hql ,amOrder.getOrderId()) ;
		BigDecimal reFundFee = new BigDecimal(0) ;
		for(OrderItem oit : oits) {
			AmOrderItem aoi = new AmOrderItem(oit) ;
			aoi.setAmOrderId(amOrder.getId());
			aoi.setOrderId(amOrder.getOrderId());
			reFundFee = reFundFee.add(oit.getPrice().multiply(new BigDecimal(oit.getQuantity()))) ;
			amOrderItemService.save(aoi);
		}
		amOrder.setReFundFee(reFundFee);
		afterMarketOrderDao.save(amOrder) ;
	}
	
	
	/**
	 * 重复提交
	 * 
	 * @param amOrder
	 * @throws Exception
	 */
	public void reSubmit(AfterMarketOrder amOrder) throws Exception {
		String note = "售后服务重新提交！" ;
		afterMarketOrderDao.save(amOrder) ;
		amOrderLogService.save(amOrder.getUsername(), amOrder, OrderActivity.APPLY_SERVICE,OperateIdentity.CONSUMER, note);
	}

	/**
	 * 审核通过
	 * @param amOrder
	 * @param managerName
	 */
	public void auditPass(AfterMarketOrder amOrder,String managerName) {
		String note = "后台审核通过" ;
		afterMarketOrderDao.save(amOrder) ;
		try {
			amOrderLogService.save(managerName, amOrder, OrderActivity.APPLY_SERVICE,OperateIdentity.MANAGER, note);
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	}

	/**
	 * 审核拒绝
	 * @param amOrder
	 * @param managerName
	 */
	public void auditRefuse(AfterMarketOrder amOrder, String managerName) {
		String note = "后台审核拒绝" ;
		afterMarketOrderDao.save(amOrder) ;
		try {
			amOrderLogService.save(managerName, amOrder, OrderActivity.APPLY_SERVICE,OperateIdentity.MANAGER, note);
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	}

	/**
	 * 财务审核通过
	 * 
	 * @param id
	 * @return
	 */
	public void financePass(AfterMarketOrder amOrder, String managerName) {
		String note = "后台财务审核通过" ;
		afterMarketOrderDao.save(amOrder) ;
		try {
			amOrderLogService.save(managerName, amOrder, OrderActivity.APPLY_SERVICE,OperateIdentity.MANAGER, note);
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		
	}

	/**
	 * 退款 售后完成
	 * 
	 * @param id
	 * @return
	 */
	public void beingRefund(AfterMarketOrder amOrder, String managerName) {
		String note = "后台退款，售后完成" ;
		afterMarketOrderDao.save(amOrder) ;
		try {
			if(amOrder.getReOrderType().equals(ReOrderType.REFUNDANDGOODS)) {
				String hql = " from AmOrderItem where amOrderId=? " ;
				List<AmOrderItem>aois = amOrderItemService.list(hql ,amOrder.getId()) ;
				for(AmOrderItem aoi : aois ) {
					Integer skuId = ConvertUtils.parseInteger(aoi.getSkuId()) ;
					skuService.execute(" update Sku set inventory=inventory+"+aoi.getQuantity()+" where id=?", skuId) ;
				}
			}
			amOrderLogService.save(managerName, amOrder, OrderActivity.APPLY_SERVICE,OperateIdentity.MANAGER, note);
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		
	}

	/**
	 * 前台补充退货信息
	 * 
	 * @param username
	 * @param amOrder
	 */
	public void reTurnGoods(String username, AfterMarketOrder amOrder) {
		String note = "买家退货" ;
		afterMarketOrderDao.save(amOrder) ;
		try {
			amOrderLogService.save(username, amOrder, OrderActivity.APPLY_SERVICE,OperateIdentity.CONSUMER, note);
		} catch (Exception e) {
			logger.error(e.getMessage());
		}		
	}

	/**
	 * 管理端手动确认收货
	 * 
	 * @param id
	 * @return
	 */
	public void reviceGoods(AfterMarketOrder amOrder, String managerName) {
		String note = "管理端用户手动确认收货" ;
		afterMarketOrderDao.save(amOrder) ;
		try {
			amOrderLogService.save(managerName, amOrder, OrderActivity.APPLY_SERVICE,OperateIdentity.MANAGER, note);
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		
	}

	/**
	 * 换货发货
	 * 
	 * @param amOrder
	 * @param managerName
	 */
	public void exChangeSendGoods(AfterMarketOrder amOrder, String managerName) {
		String note = "管理端换货发货" ;
		afterMarketOrderDao.save(amOrder) ;
		try {
			amOrderLogService.save(managerName, amOrder, OrderActivity.APPLY_SERVICE,OperateIdentity.MANAGER, note);
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	}

	/**
	 * 买家收到售后货品
	 * 
	 * @param username
	 * @param amOrder
	 */
	public void reviceExChangeGoods(String username, AfterMarketOrder amOrder) {
		String note = "买家收到售后货品" ;
		afterMarketOrderDao.save(amOrder) ;
		try {
			amOrderLogService.save(username, amOrder, OrderActivity.APPLY_SERVICE,OperateIdentity.CONSUMER, note);
		} catch (Exception e) {
			logger.error(e.getMessage());
		}	
	}

	

	

}
