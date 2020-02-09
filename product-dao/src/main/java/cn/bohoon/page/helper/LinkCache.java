package cn.bohoon.page.helper;

import java.util.List;

import cn.bohoon.framework.SpringContextHolder;
import cn.bohoon.page.entity.BottomLink;
import cn.bohoon.page.service.BottomLinkService;

public class LinkCache {

	private static LinkCache cache ;
	
	private List<BottomLink> bLinks = null ;
	
	private LinkCache(){
		init() ;
	}


	
	public static LinkCache getCache(){
		if(null == cache) {
			cache = new LinkCache() ;
		}
		return cache ;
	}
	
	
	public void init() {
		// TODO Auto-generated method stub
		BottomLinkService service = SpringContextHolder.getBean(BottomLinkService.class) ;
		String hql = " from BottomLink where pid = 0  and status = 1" ;
		bLinks = service.list(hql) ;
	}
	
	public List<BottomLink> getLinks() {
		if(null == bLinks) {
			init() ;
		}
		return bLinks ;
	}
}
