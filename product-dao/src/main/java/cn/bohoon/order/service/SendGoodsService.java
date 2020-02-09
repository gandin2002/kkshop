package cn.bohoon.order.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.bohoon.framework.service.BaseService;
import cn.bohoon.framework.util.JsonUtil;
import cn.bohoon.order.dao.SendGoodsDao;
import cn.bohoon.order.domain.SendGoodInfo;
import cn.bohoon.order.entity.OrderItem;
import cn.bohoon.order.entity.SendGoods;
import cn.bohoon.product.service.SkuScoreService;
import cn.bohoon.product.service.SkuService;
import cn.bohoon.util.ConvertUtils;
import cn.bohoon.util.IDUtil;
import cn.bohoon.util.ResultUtil;

@Service
@Transactional
public class SendGoodsService extends BaseService<SendGoods, String> {
	
	
	@Autowired
	SendGoodsDao sendGoodsDao;
	@Autowired
	SkuService skuService ;
	@Autowired
	SkuScoreService skuScoreService ;
	@Autowired
	OrderItemService orderItemService;
	@Autowired
	SendGoodsService(SendGoodsDao sendGoodsDao) {
		super(sendGoodsDao);
	}
	
	/**
	 * 订单，换货单发货 扣库存
	 * 
	 * @param sendGoods
	 * @param itemId
	 * @param sendNum
	 * @param quantity
	 * @param sendedNum
	 * @return
	 * @throws Exception
	 */
	public String saveKq(SendGoods sendGoods, String itemId, String sendNum, String quantity, String sendedNum)
			throws Exception {
		List<String> itemIds = Arrays.asList(itemId.split(","));
		List<String> sendNums = Arrays.asList(sendNum.split(","));
		List<String> quantitys = Arrays.asList(quantity.split(","));
		List<String> sendedNums = Arrays.asList(sendedNum.split(","));
		int len = null != itemIds ? itemIds.size() : 0;
		List<SendGoodInfo> sgis = new ArrayList<SendGoodInfo>();
		Integer currSendNum = 0 ;
		Integer wareHouseId = sendGoods.getSendId() ;
		for (int i = 0; i < len; i++) {
			SendGoodInfo sgi = new SendGoodInfo();
			int q = ConvertUtils.parseInteger(quantitys.get(i));
			int seded = ConvertUtils.parseInteger(sendedNums.get(i));
			int senum = ConvertUtils.parseInteger(sendNums.get(i));
			if (q < (seded + senum)) {
				return ResultUtil.getError("超量发货！");
			}
			sgi.setId(ConvertUtils.parseInteger(itemIds.get(i)));
			sgi.setNum(senum);
			sgis.add(sgi);
			OrderItem oit = orderItemService.get(sgi.getId());
			if (q == (seded + senum)) {
				oit.setStorageOut(true);
				orderItemService.save(oit);
			}
			Integer skuId = ConvertUtils.parseInteger(oit.getSkuId()) ;
			//减库存
			skuService.subInventory(senum, skuId, "2", wareHouseId);
			//"下单立减库存":"0","付款立减库存":"1","发货立减库存":"2"
			//skuService.execute(" update Sku set inventory=inventory-? where id=?", senum,skuId,"2") ;
			//仓库 去库存
			//SkuWareService.execute("update SkuWare set quantity=quantity-? where wareHouseId=? and skuId=?", senum,wareHouseId,skuId) ;
			currSendNum += senum ;
		}
		String sendItem = JsonUtil.toJson(sgis);
		sendGoods.setSendItem(sendItem);
		String FHID = IDUtil.getInstance().getId("FH");
		sendGoods.setId(FHID);
		sendGoods.setCurrSendNum(currSendNum);
		sendGoodsDao.save(sendGoods);
		return "";
	}
	
	
	/**
	 * 发送赠品
	 * 
	 * @param sendGoods
	 * @param itemId
	 * @param sendNum
	 * @param quantity
	 * @param sendedNum
	 * @return
	 * @throws Exception
	 */
	public String saveScoreGoodsKq(SendGoods sendGoods, String itemId, String sendNum, String quantity, String sendedNum)
			throws Exception {
		List<String> itemIds = Arrays.asList(itemId.split(","));
		List<String> sendNums = Arrays.asList(sendNum.split(","));
		List<String> quantitys = Arrays.asList(quantity.split(","));
		List<String> sendedNums = Arrays.asList(sendedNum.split(","));
		int len = null != itemIds ? itemIds.size() : 0;
		List<SendGoodInfo> sgis = new ArrayList<SendGoodInfo>();
		Integer currSendNum = 0 ;
//		Integer wareHouseId = sendGoods.getSendId() ;
		for (int i = 0; i < len; i++) {
			SendGoodInfo sgi = new SendGoodInfo();
			int q = ConvertUtils.parseInteger(quantitys.get(i));
			int seded = ConvertUtils.parseInteger(sendedNums.get(i));
			int senum = ConvertUtils.parseInteger(sendNums.get(i));
			if (q < (seded + senum)) {
				return ResultUtil.getError("超量发货！");
			}
			sgi.setId(ConvertUtils.parseInteger(itemIds.get(i)));
			sgi.setNum(senum);
			sgis.add(sgi);
			OrderItem oit = orderItemService.get(sgi.getId());
			if (q == (seded + senum)) {
				oit.setStorageOut(true);
				orderItemService.save(oit);
			}
			Integer skuId = ConvertUtils.parseInteger(oit.getSkuId()) ;
			skuScoreService.execute(" update SkuScore set inventory=inventory-? where id=?", senum,skuId) ;
			//仓库 去库存
//			skuService.execute("update SkuWare set quantity=quantity-? where wareHouseId=? and skuId=?", senum,wareHouseId,skuId) ;
			currSendNum += senum ;
		}
		String sendItem = JsonUtil.toJson(sgis);
		sendGoods.setSendItem(sendItem);
		String FHID = IDUtil.getInstance().getId("FH");
		sendGoods.setId(FHID);
		sendGoods.setCurrSendNum(currSendNum);
		sendGoodsDao.save(sendGoods);
		return "";
	}
	
	/**
	 * 发送维修发货，不用扣库存
	 * 
	 * @param sendGoods
	 * @param itemId
	 * @param sendNum
	 * @param quantity
	 * @param sendedNum
	 * @return
	 * @throws Exception
	 */
	public String save(SendGoods sendGoods, String itemId, String sendNum, String quantity, String sendedNum)
			throws Exception {
		List<String> itemIds = Arrays.asList(itemId.split(","));
		List<String> sendNums = Arrays.asList(sendNum.split(","));
		List<String> quantitys = Arrays.asList(quantity.split(","));
		List<String> sendedNums = Arrays.asList(sendedNum.split(","));
		int len = null != itemIds ? itemIds.size() : 0;
		List<SendGoodInfo> sgis = new ArrayList<SendGoodInfo>();
		Integer currSendNum = 0 ;
//		Integer wareHouseId = sendGoods.getSendId() ;
		for (int i = 0; i < len; i++) {
			SendGoodInfo sgi = new SendGoodInfo();
			int q = ConvertUtils.parseInteger(quantitys.get(i));
			int seded = ConvertUtils.parseInteger(sendedNums.get(i));
			int senum = ConvertUtils.parseInteger(sendNums.get(i));
			if (q < (seded + senum)) {
				return ResultUtil.getError("超量发货！");
			}
			sgi.setId(ConvertUtils.parseInteger(itemIds.get(i)));
			sgi.setNum(senum);
			sgis.add(sgi);
			OrderItem oit = orderItemService.get(sgi.getId());
			if (q == (seded + senum)) {
				oit.setStorageOut(true);
				orderItemService.save(oit);
			}
//			Integer skuId = ConvertUtils.parseInteger(oit.getSkuId()) ;
//			skuService.execute(" update Sku set inventory=inventory-? where id=?", senum,skuId) ;
			//仓库 去库存
//			skuService.execute("update SkuWare set quantity=quantity-? where wareHouseId=? and skuId=?", senum,wareHouseId,skuId) ;
			currSendNum += senum ;
		}
		String sendItem = JsonUtil.toJson(sgis);
		sendGoods.setSendItem(sendItem);
		String FHID = IDUtil.getInstance().getId("FH");
		sendGoods.setId(FHID);
		sendGoods.setCurrSendNum(currSendNum);
		sendGoodsDao.save(sendGoods);
		return "";
	}
}
