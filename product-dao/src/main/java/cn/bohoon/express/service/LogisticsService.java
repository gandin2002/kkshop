package cn.bohoon.express.service;

import java.text.MessageFormat;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import cn.bohoon.express.dao.LogisticsDao;
import cn.bohoon.express.entity.Express;
import cn.bohoon.express.entity.Logistics;
import cn.bohoon.express.entity.LogisticsUtils;
import cn.bohoon.express.helper.LogisticsHelper;
import cn.bohoon.framework.service.BaseService;

@Service
@Transactional
public class LogisticsService extends BaseService<Logistics,Integer>{

	@Value("${express.key}")
	String token;
	
	@Autowired
	LogisticsDao logisticsDao;
	
	@Autowired
	RestTemplate restTemplate;
	
	@Autowired
	ExpressService expressService;

    @Autowired
    LogisticsService(LogisticsDao logisticsDao){
        super(logisticsDao);
    }
    
    /**
     * 保存 物流公司 与 物流管理模板
     */
    public void saveExpressAddLogistics(Logistics logistics){
    	   logistics = logisticsDao.save(logistics);
    	   Express express=  new Express();
    	   express.setLogisticsid(logistics.getId());
    	   expressService.save(express);
    }
    
    
    public String queryExpress(String com,String num) {
    	String requestUrl = "http://www.kuaidi100.com/applyurl?key={0}&com={1}&nu={2}&show=2&muti=1&order=desc" ;
    	requestUrl = MessageFormat.format(requestUrl, token,com,num) ;
    	String resultUrl = restTemplate.getForObject(requestUrl, String.class) ;
    	
    	return resultUrl ;
    }
    

    // 修改过得
    public List<LogisticsUtils> queryGetExpress(String com,String num) throws Exception {
    	String requestUrl = "https://m.kuaidi100.com/query?id={0}&type={1}&postid={2}&valicode=&temp=0.22577387956347073" ;
    	requestUrl = MessageFormat.format(requestUrl, token,com,num) ;

    	
    	
    	String resultUrl = restTemplate.getForObject(requestUrl, String.class) ;
    	
    	// 将第三方的数据封装成自己想要的数据
    	List<LogisticsUtils> list = LogisticsHelper.toList(resultUrl);
    	if (list == null || list.size() <= 0)
    		return null;
    		
    	List<LogisticsUtils> enTime = LogisticsHelper.enTime(list);
    	
    	return enTime;
    }
    
    

}
