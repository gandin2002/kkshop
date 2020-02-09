package cn.bohoon.page.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import cn.bohoon.framework.service.BaseService;
import cn.bohoon.page.dao.BottomLinkDao;
import cn.bohoon.page.entity.BottomLink;

@Service
@Transactional
public class BottomLinkService extends BaseService<BottomLink,Integer>{

	@Autowired
	BottomLinkDao bottomLinkDao;

    @Autowired
    BottomLinkService(BottomLinkDao bottomLinkDao){
        super(bottomLinkDao);
    }

    public List<BottomLink> selectAll() {
		String hql = " from BottomLink order by id,pid " ;
		return list(hql) ;
	}
    
	public List<BottomLink> selectAllBySort() {
		List<BottomLink> list = selectAll() ;
		List<BottomLink> results = new ArrayList<BottomLink>() ;
		for(BottomLink link1 : list) {
			if(link1.getPid() == 0 ) {
				results.add(link1) ;
				for(BottomLink link2 : list) {
					if(link2.getPid() == link1.getId()) {
						results.add(link2) ;
					}
				}
			}
		}
		return results ;
	}

}
