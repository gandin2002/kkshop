package cn.bohoon.order.service;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import cn.bohoon.framework.service.BaseService;
import cn.bohoon.framework.util.RequestUtil;
import cn.bohoon.order.dao.AmOrderLogDao;
import cn.bohoon.order.domain.OperateIdentity;
import cn.bohoon.order.domain.OrderActivity;
import cn.bohoon.order.entity.AfterMarketOrder;
import cn.bohoon.order.entity.AmOrderLog;

@Service
@Transactional
public class AmOrderLogService extends BaseService<AmOrderLog,String>{

	@Autowired
	AmOrderLogDao amOrderLogDao;

    @Autowired
    AmOrderLogService(AmOrderLogDao amOrderLogDao){
        super(amOrderLogDao);
    }
 
    /**
     * 售后订单日志
     * 
     * @param loginUser
     * @param amOrder
     * @param oa
     * @param oi
     * @param note
     * @throws Exception
     */
    public void save(String loginUser ,AfterMarketOrder amOrder, OrderActivity oa,OperateIdentity oi, String note) throws Exception {
		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest(); 
		AmOrderLog orderLog = new AmOrderLog() ;
		orderLog.setAmOrderId(amOrder.getId());
		orderLog.setOrderUserId(amOrder.getUserId());
		orderLog.setOrderUserName(amOrder.getUsername());
		orderLog.setOperatIp(RequestUtil.getRemoteAddr(request));
		orderLog.setUsername(loginUser);
		orderLog.setOperateIdentity(oi);
		orderLog.setOrderActivity(oa);
		orderLog.setNote(note);
		orderLog.setCreateDate(new Date());
		save(orderLog) ;
	}
    
}
