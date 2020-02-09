package cn.bohoon.distribution.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import cn.bohoon.distribution.dao.ExpfeeTemplateDao;
import cn.bohoon.distribution.domain.ExpInfo;
import cn.bohoon.distribution.entity.ExpfeeCity;
import cn.bohoon.distribution.entity.ExpfeeOrder;
import cn.bohoon.distribution.entity.ExpfeeTemplate;
import cn.bohoon.framework.service.BaseService;

@Service
@Transactional
public class ExpfeeTemplateService extends BaseService<ExpfeeTemplate,Integer>{

	@Autowired
	ExpfeeTemplateDao expfeeTemplateDao;
	
	@Autowired
	ExpfeeCityService expfeeCityService ;
	@Autowired
	ExpfeeOrderService expfeeOrderService ;

    @Autowired
    ExpfeeTemplateService(ExpfeeTemplateDao expfeeTemplateDao){
        super(expfeeTemplateDao);
    }

	public void save(ExpInfo info) {
		ExpfeeTemplate template = info.getTemplate() ;
		if(template.getId() != null ) {
			template.setCreateTime(new Date());
		}
		template.setUpdateTime(new Date());
		if(template.getIsDefault()) {
			expfeeTemplateDao.execute(" update ExpfeeTemplate set isDefault=?", false) ;
		}
		
		save(template) ;
		
		List<ExpfeeOrder> feeOrders = info.getFeeOrder() ;
		List<ExpfeeCity> feeCitys = info.getFeeCity() ;
		if(template.getCalcTye() == 0 && null != feeOrders) {
			for(ExpfeeOrder feeOrder : feeOrders) {
				if(!StringUtils.isEmpty(feeOrder.getUserLevel())) {
					feeOrder.setEfId(template.getId());
					expfeeOrderService.save(feeOrder);
				}
			}
		}
		if(template.getCalcTye() == 1  && null != feeCitys) {
			for(ExpfeeCity feeCity : feeCitys) {
					feeCity.setEfId(template.getId());
					expfeeCityService.save(feeCity);
			}
		}
	}

	public ExpfeeTemplate getSet() {
		ExpfeeTemplate item = expfeeTemplateDao.select(" from ExpfeeTemplate where isDefault=1") ;
		return item ;
	}

}
