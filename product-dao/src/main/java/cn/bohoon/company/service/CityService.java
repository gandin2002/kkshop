package cn.bohoon.company.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.bohoon.company.dao.CityDao;
import cn.bohoon.company.entity.City;
import cn.bohoon.framework.service.BaseService;

@Service
@Transactional
public class CityService extends BaseService<City,Integer>{

	@Autowired
	CityDao cityDao;

    @Autowired
    CityService(CityDao cityDao){
        super(cityDao);
    }
//    //添加缓存
//    @Cacheable(value="cityCacheable")
	public List<City> listAll() {
    	return list("from City") ;
	}

    
   	public List<City> listAllProvice() {
       	return list("from City where type =2") ;
   	}
   	
    /**
     * 获取子节点
     * @param parentId
     * @return
     */
	public List<City> findChildList(Integer parentId){
		return cityDao.findAll(" from City where parentId = ? ",parentId);
	}
	
	/**
	 * 根据Code 找地址
	 * @param cityCode
	 * @return
	 */
	public City findCictBycode(Integer cityCode){
		return cityDao.select(" from City where cityCode = ? ",cityCode);
	}
}
