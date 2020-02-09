package cn.bohoon.company.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.bohoon.company.dao.CompanyCataDao;
import cn.bohoon.company.entity.CompanyCata;
import cn.bohoon.framework.service.BaseService;

@Service
@Transactional
public class CompanyCataService extends BaseService<CompanyCata,Integer>{

	@Autowired
	CompanyCataDao companyCataDao;

    @Autowired
    CompanyCataService(CompanyCataDao companyCataDao){
        super(companyCataDao);
    }

	public List<CompanyCata> categorysSorting() {
		List<CompanyCata> clist = list("from CompanyCata order by level,sort");
		List<CompanyCata> list = new ArrayList<CompanyCata>();
		for (CompanyCata cata1 : clist) {
			if (cata1.getLevel() == 1) {
				list.add(cata1);
				for (CompanyCata cata2 : clist) {
					if (cata1.getId().equals(cata2.getPid())) {
						list.add(cata2);
						for (CompanyCata cata3 : clist) {
							if (cata2.getId().equals(cata3.getPid())) {
								list.add(cata3);
								for (CompanyCata cata4 : clist) {
									if (cata3.getId().equals(cata4.getPid())) {
										list.add(cata4);
									}
								}
							}
						}
					}
				}
			}
		}
		return list;
	}

}
