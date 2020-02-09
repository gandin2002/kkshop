package cn.bohoon.quotation.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.bohoon.framework.service.BaseService;
import cn.bohoon.product.entity.Product;
import cn.bohoon.quotation.dao.QuotationItemDao;
import cn.bohoon.quotation.domain.QuotationState;
import cn.bohoon.quotation.entity.QuotationItem;
import cn.bohoon.userInfo.entity.User;

@Service
@Transactional
public class QuotationItemService extends BaseService<QuotationItem,Integer>{

	@Autowired
	QuotationItemDao quotationItemDao;

    @Autowired
    QuotationItemService(QuotationItemDao quotationItemDao){
        super(quotationItemDao);
    }
    
    
    /**
     * 获取用户议价后商品的价格
     * @return 议价后的价格
     */
    public BigDecimal getQuatationItem_(User user,Product product){
    	
    	// 判断议价后的商品
		String hql = "from QuotationItem where userInfoId=?  and ValidDate > ? and productId=?"+ 
				" and quotationState = ? order by ValidDate,id DESC";
		
		List<Object> params_1 = new ArrayList<Object>();
		params_1.add(user.getUserInfoId());
		params_1.add(new Date());
		params_1.add(product.getId());
		params_1.add(QuotationState.PASS_QUOTATION);
		
		List<QuotationItem> list1 = this.list(hql,params_1.toArray());
		
		if (list1.size() > 0){
			
			return list1.get(0).getQuotationPrice();
			
		}else{
			
			return product.getCompanyVipPrice(user.getUserInfoId());
		}
    }

	    

}
