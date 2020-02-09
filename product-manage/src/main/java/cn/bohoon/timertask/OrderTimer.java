package cn.bohoon.timertask;

import java.util.List;

import org.apache.commons.lang.math.NumberUtils;
import org.hibernate.annotations.common.util.impl.LoggerFactory;
import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import cn.bohoon.basicSetup.dao.OrderSetupDao;
import cn.bohoon.basicSetup.domain.SysParamType;
import cn.bohoon.basicSetup.entity.OrderSetup;
import cn.bohoon.basicSetup.entity.SysParam;
import cn.bohoon.basicSetup.service.OrderSetupService;
import cn.bohoon.basicSetup.service.SysParamService;

/**
 * 定时分析订单相关状态
 * @author HJ
 * 2017年11月13日,下午5:12:34
 */
@Component
public class OrderTimer {
	
        
	@Autowired
	OrderSetupDao orderSetupDao;
	
	@Autowired
	JdbcTemplate jdbcTemplate;
	
	@Autowired
	SysParamService sysParamService;
	
	@Autowired
	OrderSetupService  orderSetupService;

    Logger log =  LoggerFactory.logger(OrderSetupService.class);
    /**
     * 每分钟执行一次
     */
  //  @Scheduled(cron="1-2 * * * * ?") 
    public void orderTask(){
    	OrderSetup orderSetup = orderSetupDao.get(1);
    	if(orderSetup!=null){
    			
			//log.info("----------------开始处理订单----------------");
			SysParam sysParam = sysParamService.findParam("NON_DELIVERY_ORDER_TIME", SysParamType.ORDER_PARAM);
			SysParam sysParam2 = sysParamService.findParam("ORDER_AUTO_TIME", SysParamType.ORDER_PARAM);
			if (NumberUtils.isNumber(sysParam.getValue())) {
				Integer notPayTime = Integer.valueOf(sysParam.getValue()); // 未付款时间

				//log.info("----------------开始分析未付款订单-----------------");

				if (!StringUtils.isEmpty(notPayTime)) {
					jdbcTemplate
							.update(" UPDATE t_order SET orderState = 'CANCEL' WHERE orderState = 'WAIT_BUYER_PAY' AND delayTime is null AND DATE_ADD(createDate,INTERVAL "
									+ notPayTime + " DAY)<NOW() ");
				}
				//log.info("----------------结束分析未付款订单-----------------");

			}
			if (NumberUtils.isNumber(sysParam2.getValue())) {

				Integer orderAutoTime = Integer.valueOf(sysParam2.getValue()); // 订单自动确认时间
				//log.info("----------------开始分析订单自动确认时间-----------------");
				if (!StringUtils.isEmpty(orderAutoTime)) {
					jdbcTemplate
							.update(" UPDATE t_order SET orderState = 'TRADE_FINISHED' WHERE orderState = 'WAIT_REVICE' AND delayTime is null AND DATE_ADD(createDate,INTERVAL "
									+ orderAutoTime + " DAY)<NOW() ");
				}
				//log.info("----------------结束分析订单自动确认时间----------------");
				
				log.info("----------------开始分析订单订单延长收货时间----------------");
				if (!StringUtils.isEmpty(orderAutoTime)) {
					jdbcTemplate.update(
							" UPDATE t_order SET orderState = 'TRADE_FINISHED' WHERE  orderState = 'DELAY_DELIVERY' AND delayTime < NOW() ");
				}
				//log.info("----------------结束分析订单订单延长收货时间----------------");

			}
	    		
	    		
    		//log.info("----------------订单处理结束----------------");
    	}
    }
    
}
