package cn.bohoon.stock.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.bohoon.framework.service.BaseService;
import cn.bohoon.stock.dao.SupplierTypeDao;
import cn.bohoon.stock.entity.SupplierType;

@Service
@Transactional
public class SupplierTypeService extends BaseService<SupplierType,Integer>{

	@Autowired
	SupplierTypeDao supplierTypeDao;

    @Autowired
    SupplierTypeService(SupplierTypeDao supplierTypeDao){
        super(supplierTypeDao);
    }

}
