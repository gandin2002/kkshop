package cn.bohoon.interfaces.dao;

import org.springframework.stereotype.Repository;

import cn.bohoon.framework.orm.jpa.AbstractDao;
import cn.bohoon.interfaces.entity.SyncDataJob;


@Repository
public class SyncDataJobDao extends AbstractDao<SyncDataJob,Integer>{

}
