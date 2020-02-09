package cn.bohoon.product.service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import cn.bohoon.framework.service.BaseService;
import cn.bohoon.product.dao.AttrDao;
import cn.bohoon.product.dao.AttrGroupDao;
import cn.bohoon.product.dao.AttrValueDao;
import cn.bohoon.product.dao.PSpecificationsPDao;
import cn.bohoon.product.dao.SkuSpecificationsSkuDao;
import cn.bohoon.product.entity.Attr;
import cn.bohoon.product.entity.AttrValue;
import cn.bohoon.product.entity.PSpecificationsP;
import cn.bohoon.product.entity.SkuSpecificationsSku;
import cn.bohoon.util.ConvertUtils;
/**
 * 属性信息   服务层
 * @author Administrator
 *
 */
@Service
@Transactional
public class PSpecificationsPService extends BaseService<PSpecificationsP,Integer>{

	@Autowired
	PSpecificationsPDao pSpecificationsPDao;

    @Autowired
    PSpecificationsPService(PSpecificationsPDao pSpecificationsPDao){
        super(pSpecificationsPDao);
    }

    
}