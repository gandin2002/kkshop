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
import cn.bohoon.product.dao.AttrValueDao;
import cn.bohoon.product.entity.Attr;
import cn.bohoon.product.entity.AttrValue;
import cn.bohoon.util.ConvertUtils;
/**
 * 属性信息   服务层
 * @author Administrator
 *
 */
@Service
@Transactional
public class AttrService extends BaseService<Attr,Integer>{

	@Autowired
	AttrValueDao attrValueDao;

    @Autowired
    AttrService(AttrDao attrDao){
        super(attrDao);
    }
    
    /**
     * 获取属性名称
     * 
     * @param ids
     * @return
     */
    public String getAttrNames(String ids) {
    	
    	Map<String,Object> params = new HashMap<String,Object>();
    	Integer [] intIds =  ConvertUtils.parseIntArr(ids) ;
    	params.put("ids", Arrays.asList(intIds));
    	List<String> result = list("select name from Attr where id IN :ids",String.class,params);
    	return result.toString() ;
    	
     
    }
    
    public void saveAttrAndAttrValue(Attr attr, List<AttrValue> attrValueList){
		if(StringUtils.isEmpty(attr.getId())) {
    		save(attr);
    		for(AttrValue attrValue : attrValueList) {
    			attrValue.setAttrId(attr.getId());
    			attrValue.setStatus(true);
    			attrValueDao.save(attrValue);
    		}
    	}else {
    		update(attr);
    		Integer id = attr.getId();
    		attrValueDao.execute("update AttrValue set status = 0 where attrId = ?", id);
    		for(AttrValue attrValue : attrValueList) {
    			attrValue.setStatus(true);
    			if(!StringUtils.isEmpty(attrValue.getId())) {
    				attrValue.setAttrId(id);
    				attrValueDao.update(attrValue);
    			}else{
//    				AttrValue aValue = attrValueDao.select("from AttrValue where name = ?", attrValue.getName());
//    				if(null == aValue) {
    					attrValue.setAttrId(id);
    					attrValueDao.save(attrValue);
//    				}else {
//    					aValue.setSort(attrValue.getSort());
//    					aValue.setAttrId(id);
//    					aValue.setStatus(true);
//    					attrValueDao.update(aValue);
//    				}
    			}
			}
    	}
    }
    
    public void deleteAttrAndAttrValue(Integer id){
    	delete(id);
    	attrValueDao.execute("delete from AttrValue where attrId = ?", id);
    }
    
}