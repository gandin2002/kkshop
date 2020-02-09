package cn.bohoon.company.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.bohoon.company.dao.InvoiceDao;
import cn.bohoon.company.entity.Invoice;
import cn.bohoon.framework.service.BaseService;
import cn.bohoon.order.entity.OrderInvoice;

@Service
@Transactional
public class InvoiceService extends BaseService<Invoice,Integer>{

	@Autowired
	InvoiceDao invoiceDao;

    @Autowired
    InvoiceService(InvoiceDao invoiceDao){
        super(invoiceDao);
    }

	public void save(OrderInvoice invoice) {
		String title = invoice.getTitle() ;
		String comId = invoice.getCompanyId() ;
		Invoice model = invoiceDao.select(" from Invoice where title=? and companyId=?", title,comId) ;
		if(null != model) {
			return ;
		}
		Invoice iv = new Invoice() ;
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
