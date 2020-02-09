package cn.bohoon.quotation.dao;

import org.springframework.stereotype.Repository;

import cn.bohoon.framework.orm.jpa.AbstractDao;
import cn.bohoon.quotation.entity.QuotationItem;
import cn.bohoon.quotation.entity.TieredPrice;
@Repository
public class TieredPriceDao extends AbstractDao<TieredPrice,Integer>{

}
