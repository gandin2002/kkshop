package cn.bohoon;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import cn.bohoon.basicSetup.helper.SysParamCache;
import cn.bohoon.interfaces.entity.SyncDataJob;
import cn.bohoon.interfaces.service.SyncDataJobService;
import cn.bohoon.message.service.MessageConfigService;
import cn.bohoon.product.entity.ProductLabel;
import cn.bohoon.product.service.ProductLabelService;
import cn.bohoon.util.SyncDataQuartz;

@Component
public class StartRunner implements CommandLineRunner {

	@Autowired
	SyncDataJobService syncDataJobService ;
	
	@Autowired
	ProductLabelService productLabelService;
	
	@Autowired
	MessageConfigService messageConfigService;
	
	@Override
	public void run(String... args) throws Exception {
		// TODO Auto-generated method stub
		String hql = " select j from SyncDataJob j ,SyncGroup g where j.groupId= g.id and g.status = 1" ;
		List<SyncDataJob> jobs = syncDataJobService.list(hql) ;
		for(SyncDataJob jobModel : jobs) {
			if(!StringUtils.isEmpty(jobModel.getCronExpression())) {
				SyncDataQuartz.addJob(jobModel);
			}
		}
		
		// -----------------------初始化产品标签默认数据 ----------------------------
    	List<String>  defaultData = productLabelService.getDefaultData();
		for (String name : defaultData) {
			ProductLabel  productLabel = productLabelService.select(" from ProductLabel  where name = ? ", name);
			if(null == productLabel){
				productLabel = new ProductLabel();
				productLabel.setName(name);
				productLabel.setCreateTime(new Date());
				productLabelService.save(productLabel);
			}
		}
		
		SysParamCache.getCache().init();
		messageConfigService.initData();
	}

}
