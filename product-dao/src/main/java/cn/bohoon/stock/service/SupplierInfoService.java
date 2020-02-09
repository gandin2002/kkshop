package cn.bohoon.stock.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import cn.bohoon.framework.service.BaseService;
import cn.bohoon.stock.dao.SupplierInfoDao;
import cn.bohoon.stock.entity.SupplierInfo;
import cn.bohoon.util.IDUtil;

@Service
@Transactional
public class SupplierInfoService extends BaseService<SupplierInfo,String>{

	@Autowired
	SupplierInfoDao supplierInfoDao;

    @Autowired
    SupplierInfoService(SupplierInfoDao supplierInfoDao){
        super(supplierInfoDao);
    }

	public void addSupplier(SupplierInfo entity) {
		if(StringUtils.isEmpty(entity.getId())) {
			String id = IDUtil.getInstance().getIdByDb(this, SupplierInfo.class, "SP", null) ;
			entity.setId(id);
		}
		save(entity);
		
	}

}
