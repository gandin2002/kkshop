package cn.bohoon.timertask;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import cn.bohoon.basicSetup.domain.SysParamType;
import cn.bohoon.basicSetup.entity.SysParam;
import cn.bohoon.basicSetup.service.SysParamService;
import cn.bohoon.framework.util.DateUtil;
import cn.bohoon.order.entity.OrderRate;
import cn.bohoon.order.service.OrderRateService;
import cn.bohoon.order.service.OrderService;
import cn.bohoon.product.entity.Product;
import cn.bohoon.product.entity.ProductLabel;
import cn.bohoon.product.entity.Sku;
import cn.bohoon.product.service.ProductLabelService;
import cn.bohoon.product.service.ProductService;
import cn.bohoon.product.service.SkuService;
import cn.bohoon.userInfo.entity.UserInRate;
import cn.bohoon.userInfo.service.UserInRateService;
import cn.bohoon.userInfo.service.UserService;

@Component
public class IncreRateTask {
	
	@Autowired
	UserService userService ;
	@Autowired
	OrderService orderService ;
	@Autowired
	OrderRateService orderRateService ;
	@Autowired
	UserInRateService userInRateService ;
	@Autowired
	SysParamService sysParamService;
	@Autowired
	SkuService skuService;
	@Autowired
	ProductService productService;
	
	@Autowired
	ProductLabelService  productLabelService;
	@PersistenceContext
	EntityManager entityManager;
	
	/**
	 * 每天23点59分开始执行
	 * 
	 */
	@Scheduled(cron = "0 59 23 * * ?")
	public void incrementRate() {
		userIncrement() ;
		orderRateCalc() ;
		AutoShelves() ;
		changeLabels();
		changeLabelsRote();
	}
	
	private void AutoShelves(){
		
		// 自动下架   --->  0 代表不自动下架，  1.代表自动下架(自动下架指的是那些库存为0的规格商品自动下架)
		SysParam findParam = sysParamService.findParam("AUTOMATIC_COMMODITY_FRAME", SysParamType.PRODUCT_PARAM);
		
		
		if ("1".equals( findParam.getValue() )){
			
			
			List<Sku> skuList = skuService.list("from Sku where inventory <= 0 and flag=?",true);
			
			if (null != skuList && skuList.size() > 0){
				for (Sku sku : skuList){
					
					sku.setFlag(false);
					skuService.update(sku);
					
					
					// 判断商品的规格是否都为下架，都下架则商品下架
					List<Sku> hp = skuService.list("from Sku where productId=? and flag=?",sku.getProductId(),true);
					
					if (null == hp || hp.size() <= 0){
						
						// 则说明商品的规格都下架了，则商品需要下架
						Product p = productService.select("from Product where id=?", sku.getProductId());
						p.setFlag(false);
						productService.update(p);
					}
				}
				
			}
			
		}
		
	}

	private void orderRateCalc() {
		String now = DateUtil.getNowSimple(DateUtil.COMMON_DATE) ;
		BigDecimal amount = orderService.select(" select sum(total) from Order where createDate like '"+now+"%'",BigDecimal.class) ;
		Long transNum = orderService.select(" select count(*) from Order where createDate like '"+now+"%'",Long.class) ;
		OrderRate rate = new OrderRate() ;
		if(null == amount ) {
			amount = new BigDecimal(0) ;
		}
		rate.setAmount(amount);
		rate.setTransNum(transNum);
		rate.setDat(now);
		orderRateService.save(rate);
	}

	/**
	 * 用户新增趋势统计
	 */
	private void userIncrement() {
		String hql = "select count(*) from User " ;
		Long count = userService.select(hql, Long.class) ;
		String raHql = " from UserInRate order by datDay desc" ;
		List<UserInRate> rates = userInRateService.list(raHql) ;
		Long lastCount = count ;
		if(rates.size() > 0 ) {
			lastCount = rates.get(0).getUsers() ;
		}
		
		UserInRate rate = new UserInRate() ;
		Double rateRadio = (count.doubleValue()-lastCount)/count.doubleValue() ;
		rate.setRate(rateRadio);
		rate.setUsers(count);
		String now = DateUtil.getNowSimple(DateUtil.COMMON_DATE) ;
		rate.setDatDay(now);
		userInRateService.save(rate);
	}
	
	
	/**
	 * 定期改变新品标签状态
	 */
	

	private void changeLabels() {
		 ProductLabel productLabel = productLabelService.select(" from ProductLabel where  name like '%新品%' ");
	
			String hql3=" from Product where lables like  '%新品%'  order by showSort asc,salesNum desc";
		
			List<Product> productList = productService.list(hql3);
			 Date nowtime=new Date();
					 for(Product pro:productList){
						 Date createDate = pro.getCreateDate();
						  if(null!=createDate){
							  
							  int date = createDate.getDate(); 
							  int day = date+productLabel.getDays();
							  createDate.setDate(day);
							  if(nowtime.after(createDate)){
								  pro.setShowSort(null);
								  if(pro.getLables().contains(",新品")){
										String replace = pro.getLables().replace(",新品","");
										pro.setLables(replace);
										
									}else{
										String replace2 = pro.getLables().replace("新品","");
										pro.setLables(replace2);
									}
							  }
						  }
					 }
				
			 productService.saveBatch(productList, productList.size());
	}
	
	//设置热卖标签
	public void changeLabelsRote() {
		
		//先清除旧的热卖状态
		List<Product> products1list = productService.list(" from Product where lables like ? ","%热卖%");
		
		for(Product p:products1list){
			if(p.getLables().contains(",热卖")){
				String replace = p.getLables().replace(",热卖","");
				p.setLables(replace);
			}else{
				String replace2 = p.getLables().replace("热卖","");
				p.setLables(replace2);
			}
			//p.setLables(p.getLables()+p.getLables().replace(",热卖", " "));
			System.out.println(p.getId()+p.getLables()+p.getShowSort());
		
			//p.setShowSort(null);
		
		}
		
		   productService.saveBatch(products1list, products1list.size());
		
		//设置新的热卖标签
		 ProductLabel productLabel = productLabelService.select(" from ProductLabel where  name='热卖' ");  
		String  hql2=" from Product  where 1=1 order by showSort asc,salesNum desc";
		 TypedQuery<Product>  createQuery = this.entityManager.createQuery(hql2, Product.class);
		 
			createQuery.setFirstResult(0);
			createQuery.setMaxResults(10);
			if(null!=productLabel){	
				if(null!=productLabel.getPresalesNum()){
					createQuery.setMaxResults(productLabel.getPresalesNum());
				 }
			}
			List<Product> resultList = createQuery.getResultList();
			int j=0;
			for(Product p:resultList){
				if(null!=p.getLables()){
					if(!p.getLables().contains("热卖")){
						if(p.getLables().length()>0){
							p.setLables(p.getLables()+",热卖");
						}else{
							p.setLables("热卖");
						}
					}
				}else{
					p.setLables("热卖");
				}
				System.out.println(p.getLables()+p.getPresaleNums()+"j是"+j++);
			}
		productService.saveBatch(resultList, resultList.size());
	}
	
}
