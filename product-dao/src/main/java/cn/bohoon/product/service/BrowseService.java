package cn.bohoon.product.service;

import cn.bohoon.product.dao.BrowseDao;
import cn.bohoon.product.entity.Browse;
import cn.bohoon.product.entity.Product;
import cn.bohoon.userInfo.domain.LoginUser;
import cn.bohoon.userInfo.entity.UserInfo;

import org.springframework.stereotype.Service;

import cn.bohoon.framework.orm.domain.Page;
import cn.bohoon.framework.service.BaseService;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

/**
 * 商品浏览记录 服务层
 * 
 * @author Administrator
 *
 */
@Service
@Transactional
public class BrowseService extends BaseService<Browse, Integer> {

	@Autowired
	BrowseDao browseDao;
	@Autowired
	ProductService productService ;

	@Autowired
	BrowseService(BrowseDao browseDao) {
		super(browseDao);
	}
	
	
	public List<Product> addBrowsList(UserInfo user, Integer id) {
	    if(null != user){
			String hql = " from Browse where userId=? and productId=?" ;
			Browse browse = select(hql,user.getId(),id) ;
			if(null == browse) {
				browse = new Browse() ;
				browse.setUserId(user.getId());
				browse.setProductId(id);
				
			} else {
				browse.setVcs(browse.getVcs()+1);
				browse.setCreateDate(new Date());
			}
			save(browse);
			String phql = "select p from Product p ,Browse b where p.id=b.productId and b.userId=?";
			Page<Product> productPage = new Page<Product>() ;
			List<Product> products = productService.page(productPage,phql,user.getId()).getResult();
			return products ;
	    }else{
		   return null;
	    }
		
	}


	public List<Product> getSeeList(LoginUser user) {
		List<Product> products = new ArrayList<Product>() ;
		if(user != null ) {
			String phql = "select p from Product p ,Browse b where p.id=b.productId and b.userId=? and p.flag = 1 order by b.createDate ,b.vcs desc " ;
			Page<Product> productPage = new Page<Product>(3) ;
			products = productService.page(productPage,phql,user.getUserId()).getResult() ;
		}
		return products ;
	}

}