package cn.bohoon.quotation.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.bohoon.framework.service.BaseService;
import cn.bohoon.quotation.dao.QuotationItemDao;
import cn.bohoon.quotation.dao.TieredPriceDao;
import cn.bohoon.quotation.entity.QuotationItem;
import cn.bohoon.quotation.entity.TieredPrice;

@Service
@Transactional
public class TieredPriceService extends BaseService<TieredPrice,Integer>{
     @Autowired
     TieredPriceDao  tieredPriceDao;
     @Autowired
     TieredPriceService(TieredPriceDao tieredPriceDao){
         super(tieredPriceDao);
     }
}
