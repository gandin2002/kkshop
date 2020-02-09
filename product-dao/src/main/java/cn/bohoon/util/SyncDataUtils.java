package cn.bohoon.util;

import java.util.ArrayList;
import java.util.List;

import org.springframework.util.StringUtils;

import cn.bohoon.framework.SpringContextHolder;
import cn.bohoon.interfaces.entity.SyncDataJob;
import cn.bohoon.interfaces.entity.SyncGroup;
import cn.bohoon.interfaces.service.SyncDataJobService;
import cn.bohoon.userInfo.entity.UserInfo;
import cn.bohoon.userInfo.service.UserInfoService;

/**
 * 单一同步
 * @author HJ
 * 2018年7月13日,下午5:23:46
 */
public class SyncDataUtils {
	
	public static String syncSoleDate(String userId,String url,String orderId){
		return syncSoleDate(userId,url,orderId,"");
	}
	
	
	public static String syncSoleDate(String userId,String url,String orderId,String orderReceiptId){
		
		UserInfoService userInfoService = SpringContextHolder.getBean(UserInfoService.class) ;
		if(StringUtils.isEmpty(userId)){
			return ResultUtil.getError("错误用户!");
		}
		UserInfo userInfo = userInfoService.get(userId);
		if(userInfo == null){
			return ResultUtil.getError("错误用户!");
		}
		SyncDataJobService syncDataJobService  = SpringContextHolder.getBean(SyncDataJobService.class);
		String hql = " from SyncDataJob job where job.sceneUrl like ? " ;
		List<Object> params = new ArrayList<Object>();
		params.add("%"+url+"%");
		List<SyncDataJob> jobs = syncDataJobService.list(hql, params.toArray());
		for(SyncDataJob job : jobs) {
			SyncGroup syncGroup = job.getGroup();
			String sql = new String();
			if (!StringUtils.isEmpty(orderId)) {
				if (syncGroup.getMallTable().equals("t_order")) { // 同步订单
					sql += " and id = '" + orderId+"'";
				}
				if (syncGroup.getMallTable().equals("t_order_item")) { // 同步订单详情
					sql += " and orderId = '" + orderId+"'";
				}
			}
			if (syncGroup.getMallTable().equals("t_user_info")) { // 个人用户 同步+
				if(!StringUtils.isEmpty(userId)){
					sql += " and id = '" + userId+"'";
				}
			}
			if (syncGroup.getMallTable().equals("t_company")) { // 企业
				if(!StringUtils.isEmpty(userInfo.getCompanyId())){
					sql += " and id = '"  + userInfo.getCompanyId() +"'";
				}
			}
			if(syncGroup.getMallTable().equals("t_shipping_info")){
				if(!StringUtils.isEmpty(userInfo.getCompanyId())){
					sql += " and companyId = '"  + userInfo.getCompanyId() +"'";
				}
			}
			if(syncGroup.getMallTable().equals("t_order_receipt")){
				if(!StringUtils.isEmpty(orderReceiptId)){
					sql += " and id = '"  + orderReceiptId +"'";
				}
			}
			if(!StringUtils.isEmpty(sql)){
				System.out.println("--------------开始执行同步 表："+syncGroup.getMallTable()+"--------------");
				SyncDataJobExecution.syncData(job.getId(),sql);
			}
		}
		return ResultUtil.getSuccessMsg();
	}
 
}
