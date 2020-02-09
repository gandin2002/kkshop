package cn.bohoon.util;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import cn.bohoon.distribution.entity.ExpfeeCity;
import cn.bohoon.distribution.entity.ExpfeeOrder;
import cn.bohoon.distribution.entity.ExpfeeTemplate;
import cn.bohoon.distribution.service.ExpfeeCityService;
import cn.bohoon.distribution.service.ExpfeeOrderService;
import cn.bohoon.framework.SpringContextHolder;
import cn.bohoon.userInfo.entity.ShippingInfo;
import cn.bohoon.userInfo.entity.UserInfo;
import cn.bohoon.userInfo.entity.UserLevel;
import cn.bohoon.userInfo.service.UserInfoService;
import cn.bohoon.userInfo.service.UserLevelService;

public class ExpFeeUtil {
	
	public static BigDecimal calcDefault(ShippingInfo sif,BigDecimal price, String userId, ExpfeeTemplate template) {
		BigDecimal deliverFee = new BigDecimal(15) ; //运费
		if(template.getCalcTye() == 0) {
			deliverFee = calcByPrice(price, userId, template) ;
		} else {
			deliverFee = calcByTemplate(sif,price, userId, template) ;
		}
		return deliverFee ;
	}

	public static BigDecimal calcByTemplate(ShippingInfo sif ,BigDecimal weight, String userId, ExpfeeTemplate template) {
		BigDecimal deliverFee = new BigDecimal(15) ; //运费
		//判断城市
		if (sif!=null&&sif.getProvince()!=null) {
			ExpfeeCityService expfeeCityService=SpringContextHolder.getBean(ExpfeeCityService.class);
			List<ExpfeeCity> list= expfeeCityService.list("from ExpfeeCity where efId=?", template.getId());
			Boolean state=false;
			for (ExpfeeCity expfeeCity : list) {
				
				 String[] e= expfeeCity.getcIds().split(",");
				 List<String> ids= Arrays.asList(e);  
				 
				 for (String st : ids) {
					 
					if (st.substring(0, 2).equals(sif.getProvince().substring(0, 2))) {//
						template.setFqFee(expfeeCity.getFqFee());
						template.setFquality(expfeeCity.getFquality());
						template.setConFee(expfeeCity.getConFee());
						template.setConHeavy(expfeeCity.getConHeavy());
						state=true;
						break;
					}
					
				}
				if (state) {
					break;
				}
				
			}
		}
		
		//使用总重量计算运费
		if (weight.doubleValue()<=template.getFquality().doubleValue()) {
			deliverFee=template.getFqFee();
		}else if (weight.doubleValue()>template.getFquality().doubleValue()) {
			//超出的部分
			Double surplus=weight.doubleValue()-template.getFquality().doubleValue();
			Double fc= surplus%template.getConHeavy().doubleValue()==0?surplus/template.getConHeavy().doubleValue():surplus/template.getConHeavy().doubleValue()+1;
			Double nm= fc*template.getConFee().doubleValue();
			BigDecimal addMoney = BigDecimal.valueOf(nm);
			deliverFee=template.getFqFee().add(addMoney);
		}
		
		return deliverFee;
	}

	public static BigDecimal calcByPrice(BigDecimal price, String userId, ExpfeeTemplate template) {
		BigDecimal deliverFee = new BigDecimal(15) ; //运费expFee
		UserLevelService userLevelService = SpringContextHolder.getBean(UserLevelService.class) ;
		UserInfoService userInfoService = SpringContextHolder.getBean(UserInfoService.class) ;
		//积分经验值
		UserInfo userInfo = userInfoService.get(userId) ;
		//所需经验
		UserLevel level = userLevelService.getUserLevel(userInfo.getExp()) ;
		ExpfeeOrderService expFeeOrderService = SpringContextHolder.getBean(ExpfeeOrderService.class) ;
		
		List<ExpfeeOrder> efos = expFeeOrderService.list(" from ExpfeeOrder where efId =? order by expFee",template.getId()) ;
		for(ExpfeeOrder efo : efos) {
			UserLevel ul = userLevelService.getUserLevelByName(efo.getUserLevel()) ;
			if(ul != null && level.getName().equals(ul.getName()) && price.intValue() >= efo.getOrderFee().intValue()) {
				return efo.getExpFee() ;
			}
		}
		return deliverFee; 

	}
}
