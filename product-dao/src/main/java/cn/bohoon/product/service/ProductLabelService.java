package cn.bohoon.product.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSON;

import cn.bohoon.basicSetup.domain.SysParamType;
import cn.bohoon.basicSetup.entity.SysParam;
import cn.bohoon.basicSetup.helper.SysParamHelper;
import cn.bohoon.basicSetup.service.SysParamService;
import cn.bohoon.framework.service.BaseService;
import cn.bohoon.product.dao.ProductLabelDao;
import cn.bohoon.product.entity.ProductLabel;

@Service
@Transactional
public class ProductLabelService extends BaseService<ProductLabel,Integer>{

	@Autowired
	ProductLabelDao productLabelDao;

	@Autowired
	SysParamService  sysParamService;
	
	
    @Autowired
    ProductLabelService(ProductLabelDao productLabelDao){
        super(productLabelDao);
    }
    
    /**
     * 获取默认数据
     * @return
     */
    public List<String> getDefaultData(){
    	SysParam sysParam = sysParamService.findParam(SysParamHelper.PRODUCT_LABEL_DEFAULT_DATA, SysParamType.PRODUCT_LABEL);
    	List<String> defaultData = new ArrayList<>();
    	if(sysParam!=null && !StringUtils.isEmpty(sysParam.getSysOption())){
    		String source = sysParam.getSysOption();
    		List<String> jsons = JSON.parseArray(source, String.class);
    		for (String json : jsons) {
    			String name = JSON.parseObject(json).getString("name");
    			defaultData.add(name);
    		}
    	}
		return defaultData;
    }
}
