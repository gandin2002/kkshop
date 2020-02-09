package cn.bohoon.order.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.bohoon.company.entity.Invoice;
import cn.bohoon.framework.service.BaseService;
import cn.bohoon.order.dao.OrderInvoiceDao;
import cn.bohoon.order.entity.OrderInvoice;

@Service
@Transactional
public class OrderInvoiceService extends BaseService<OrderInvoice,Integer>{

	@Autowired
	OrderInvoiceDao orderInvoiceDao;

    @Autowired
    OrderInvoiceService(OrderInvoiceDao orderInvoiceDao){
        super(orderInvoiceDao);
    }

	public void save(OrderInvoice iv, Invoice invoice) {
		
		iv.setTitle(invoice.getTitle());
		iv.setAccountBank(invoice.getAccountBank());
		iv.setCompanyAddress(invoice.getCompanyAddress());
		iv.setCompanyId(invoice.getCompanyId());
		iv.setCompanyName(invoice.getCompanyName());
		iv.setCompanyPhone(invoice.getCompanyPhone());
		iv.setContent(invoice.getContent());
		iv.setDepositBank(invoice.getDepositBank());
		iv.setInvoiceType(invoice.getInvoiceType());
		iv.setMemberId(invoice.getMemberId());
		iv.setTaxpayerNumber(invoice.getTaxpayerNumber());
		iv.setTitle(invoice.getTitle());
		save(iv) ;
	}

}
