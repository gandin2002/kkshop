package cn.bohoon.quotation.service;


import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.bohoon.quotation.dao.QuotationDao;
import cn.bohoon.quotation.dao.QuotationItemDao;
import cn.bohoon.quotation.domain.QuotationState;
import cn.bohoon.quotation.entity.Quotation;
import cn.bohoon.quotation.entity.QuotationItem;
import cn.bohoon.framework.service.BaseService;

@Service
@Transactional
public class QuotationService extends BaseService<Quotation,String>{

	@Autowired
	QuotationDao quotationDao;
	
	@Autowired
	QuotationItemDao quotationItemDao;

    @Autowired
    QuotationService(QuotationDao quotationDao){
        super(quotationDao);
    }
    
    public void saveQuotation(Quotation quotation, List<QuotationItem> itemList) {			
    	quotationDao.save(quotation);
    	quotationItemDao.saveBatch(itemList, itemList.size());
    }

    /**
     * 调整报价状态
     * 
     * @param state
     * @param quotationId
     */
	public void editState(QuotationState state, String quotationId,String detail) {
		quotationDao.execute("Update Quotation q set q.quotationState =?,q.denyDetail =?,q.checkTime =? where q.quotationState =? and q.id =?", 
				state,detail,new Date(),QuotationState.BE_ON_QUOTATION,quotationId);
		quotationItemDao.execute("update QuotationItem set quotationState=? where quotationId=?",state,quotationId) ;
	}

}
