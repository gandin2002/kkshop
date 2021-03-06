package cn.bohoon.userInfo.dao;

import org.springframework.stereotype.Repository;

import cn.bohoon.framework.orm.jpa.AbstractDao;
import cn.bohoon.userInfo.entity.User;

@Repository
public class UserDao extends AbstractDao<User, Integer> {

}
