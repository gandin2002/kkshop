package cn.bohoon.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.poi.util.StringUtil;
import org.apache.velocity.VelocityContext;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.alibaba.fastjson.JSONObject;

import cn.bohoon.company.entity.Company;
import cn.bohoon.framework.util.RequestUtil;
import cn.bohoon.order.entity.AfterMarketOrder;
import cn.bohoon.order.entity.Order;
import cn.bohoon.order.entity.OrderItem;
import cn.bohoon.order.entity.OrderLog;
import cn.bohoon.order.entity.SendGoods;
import cn.bohoon.userInfo.entity.UserInfo;

public class ValidateFillingUtil {
	
	
	public static VelocityContext fillingDate(Object... objects){
		
		VelocityContext velocity = new VelocityContext(); // 如model 一样传参
		
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
		String ip = RequestUtil.getRemoteAddr(request) ;
		
		//------------------基础信息--------------------
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		String date= simpleDateFormat.format(new Date());
		velocity.put("date",date);
		velocity.put("ip",ip);
		//-----------------end---------------------------

		for (Object object : objects) {
			if(object != null){
				
				//------------------用户信息---------------------
				if(object instanceof UserInfo){
					UserInfo userInfo = (UserInfo)object;
					
					velocity.put("passport_id",userInfo.getId());
					velocity.put("nickname", userInfo.getNickname());
					velocity.put("email",userInfo.getEmail());
					velocity.put("phone",userInfo.getPhone());
					velocity.put("realname",userInfo.getRealname());
				}
				//------------------end -----------------------
				//------------------订单信息--------------------
				if(object instanceof Order){
					 Order order = (Order)object;
					 
					 List<OrderItem>  list= order.getOrderItems();
					 Set<String> pName = new HashSet<String>();
					 
					 for (OrderItem orderItem : list) {
						 if(pName.size()>3){
							 pName.add("....");
							 break;
						 }
						 pName.add(orderItem.getProductName());
					}

					velocity.put("order_info",order.getShippingInfo());
					velocity.put("order_number", order.getId());
					velocity.put("order_goods", StringUtil.join(pName.toArray(), ","));
					velocity.put("order_total_pay",order.getTotal());	
				}
				//------------------end-------------------------
				
				//------------------订单售后信息------------------
				if(object instanceof AfterMarketOrder){
					AfterMarketOrder afterMarketOrder = (AfterMarketOrder)object;
					velocity.put("order_pay_back", afterMarketOrder.getReFundFee());
					velocity.put("afterservice_ctime", afterMarketOrder.getCreateDate());
					velocity.put("afterservice_type",  afterMarketOrder.getReOrderType().name());
					velocity.put("afterservice_number", afterMarketOrder.getId());
					velocity.put("afterservice_status", afterMarketOrder.getOrderState().getName());
					velocity.put("afterservice_process", afterMarketOrder.getOrderState().getDescribe());
				}
				//--------------------end-------------------------
				
				//-------------------发货订单信息-----------------
				if(object instanceof  SendGoods){
					SendGoods sendGoods = (SendGoods)object;
					velocity.put("express_company",sendGoods.getTransCompany());
					velocity.put("express_number",sendGoods.getTransNum());
					velocity.put("sendTime", sendGoods.getSendTime());
				}
				//--------------------end------------------------
				//--------------------企业审核-------------------
				if(object instanceof Company){
					Company company = (Company)object;
					velocity.put("check","企业认证!");
					velocity.put("checkSate",company.getCompanySate().name());
					velocity.put("companyName",company.getName());
					velocity.put("remark", company.getCheckWarn());
				}
				//---------------------end------------------------
				
				//---------------------OrderLog-------------------
				if(object instanceof OrderLog){
					OrderLog orderLog= (OrderLog) object;
					velocity.put("name", orderLog.getOrderCheckState().name());
					velocity.put("note", orderLog.getNote());
					velocity.put("handlingUsername", orderLog.getUsername());
					velocity.put("handlingTime", orderLog.getCreateDate());
				}
				if(object instanceof AfterMarketOrder){
					AfterMarketOrder eamOrder= (AfterMarketOrder) object;
					velocity.put("passport_id", eamOrder.getUserId());
					velocity.put("nickname", eamOrder.getUsername());
					velocity.put("order_number", eamOrder.getOrderId());
					velocity.put("afterservice_type", eamOrder.getReOrderTypes());
					velocity.put("afterservice_number", eamOrder.getId());
					velocity.put("afterservice_status", eamOrder.getOrderState().getName());
					velocity.put("afterservice_process", eamOrder.getOrderState().getName());
					velocity.put("afterservice_ctime", eamOrder.getCreateDate().toGMTString());
				}
				//--------------------msgCode--------------------
				if(object instanceof JSONObject){
					JSONObject jsonobject= (JSONObject) object;
					velocity.put("msgCode",jsonobject.get("msgCode"));
					velocity.put("msghead", jsonobject.get("msghead"));
				}
				//---------------------end-----------------------
			}
		}
 
		
	
		return velocity;
	}
}
