package cn.bohoon.userInfo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import cn.bohoon.framework.service.BaseService;
import cn.bohoon.userInfo.dao.ShippingInfoDao;
import cn.bohoon.userInfo.entity.ShippingInfo;
import cn.bohoon.util.IDUtil;

@Service
@Transactional
public class ShippingInfoService extends BaseService<ShippingInfo, String> {
	
	@Autowired
	ShippingInfoDao shippingInfoDao;
	
	@Autowired
	ShippingInfoService(ShippingInfoDao shippingInfoDao) {
		super(shippingInfoDao);
	}

	public void saveInfo(ShippingInfo shippingInfo) {
		if(StringUtils.isEmpty(shippingInfo.getId())) {
			String id = IDUtil.getInstance().getCommonId(this, ShippingInfo.class) ;
			shippingInfo.setId(id);
		}
		
		shippingInfo.setErpCode(shippingInfo.getId());
		String addressInfo = "";
		String province = shippingInfo.getProvince() == null ?"":shippingInfo.getProvince();
		String city =shippingInfo.getCity() == null ?"":shippingInfo.getCity() ;
		String county= shippingInfo.getCounty() == null ? "" : shippingInfo.getCounty();
		addressInfo = province+city+county+shippingInfo.getAddress();
		shippingInfo.setAddressInfo(addressInfo);
		shippingInfoDao.save(shippingInfo);
	}
	
	public ShippingInfo queryFirst(String userId) {
		List<ShippingInfo> shippingLists = getShipByUserId(userId) ;
		if(null != shippingLists && shippingLists.size() >0 ) {
			return shippingLists.get(0) ;
		}
		return null ;
	}

	public List<ShippingInfo> getShipByUserId(String userId) {
		String hql = " from ShippingInfo where userId=? order by first desc" ;
		return list(hql,userId);
	}
	
	public List<ShippingInfo> getShipByCompanyId(String companyId) {
		String hql = " from ShippingInfo where companyId =? order by first desc" ;
		return list(hql,companyId);
	}
	
	

}
