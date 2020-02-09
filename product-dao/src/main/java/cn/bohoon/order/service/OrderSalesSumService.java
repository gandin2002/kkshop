package cn.bohoon.order.service;


import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.bohoon.framework.orm.domain.Page;
import cn.bohoon.framework.util.DateUtil;

@Service
@Transactional
public class OrderSalesSumService {
	
	@Autowired
	JdbcTemplate jdbcTemplate;
	
	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
	
	public List<Map<String,Object>> monthList(int year) throws ParseException{
		
		StringBuilder sql = new StringBuilder("select CONCAT(YEAR(o.createDate),'年',DATE_FORMAT(o.createDate,'%m')) as months,"
				+ "sum(o.total) as total,sum(o.deliverFee) as deliverFee,sum(o.couponReduction) as couponReduction from t_order o where 1=1");
		
		Date yearFirstDate = this.getYearFirst(year);
		Date yearLastDate =  this.getYearLast(year);
		
		sql.append(" and o.createDate >='").append(new java.sql.Date(yearFirstDate.getTime())).append("'");
		sql.append(" and o.createDate <'").append(new java.sql.Date(DateUtil.getNDayAfter(yearLastDate, 1).getTime())).append("'");
		sql.append(" group by months order by months asc");
		
		List<Map<String,Object>> list = jdbcTemplate.queryForList(sql.toString());
		return list;
	}
	
	public Page<Map<String,Object>> pageDay(Page<Map<String,Object>> pageDaySum,String startTime,String endTime) throws ParseException{
		
		StringBuilder sql = new StringBuilder("select count(1) from");
		sql.append(" (select DATE_FORMAT(o.createDate,'%Y-%m-%d') as days from t_order o where 1=1");
		sql.append(" and o.createDate >='").append(new java.sql.Date(DateUtil.switchStringToDate(startTime, "yy-MM-dd").getTime())).append("'");
		sql.append(" and o.createDate <'").append(new java.sql.Date(DateUtil.getNDayAfter(endTime, 1).getTime())).append("'");
		sql.append(" group by days) as od");
		
		long totalCount = jdbcTemplate.queryForObject(sql.toString(), Long.class);
		pageDaySum.setTotalCount(totalCount);
		
		sql = new StringBuilder("select DATE_FORMAT(o.createDate,'%Y-%m-%d') as days,"
		+ "sum(o.total) as total,sum(o.deliverFee) as deliverFee,sum(o.couponReduction) as couponReduction from t_order o where 1=1");
		sql.append(" and o.createDate >='").append(new java.sql.Date(DateUtil.switchStringToDate(startTime, "yy-MM-dd").getTime())).append("'");
		sql.append(" and o.createDate <'").append(new java.sql.Date(DateUtil.getNDayAfter(endTime, 1).getTime())).append("'");
		sql.append(" group by days order by days asc limit ");
		sql.append(pageDaySum.getFirst());
		sql.append(",");
		sql.append(pageDaySum.getPageSize());

		List<Map<String,Object>> resultList = jdbcTemplate.queryForList(sql.toString());
		pageDaySum.setResult(resultList);
		return pageDaySum;
	}

	/** 
     * 获取某年第一天日期 
     * @param year 年份 
     * @return Date 
     */  
    private Date getYearFirst(int year){  
        Calendar calendar = Calendar.getInstance();  
        calendar.clear();  
        calendar.set(Calendar.YEAR, year);  
        Date currYearFirst = calendar.getTime();  
        return currYearFirst;  
    }  
      
    /** 
     * 获取某年最后一天日期 
     * @param year 年份 
     * @return Date 
     */  
    private Date getYearLast(int year){  
        Calendar calendar = Calendar.getInstance();  
        calendar.clear();  
        calendar.set(Calendar.YEAR, year);  
        calendar.roll(Calendar.DAY_OF_YEAR, -1);  
        Date currYearLast = calendar.getTime();  
          
        return currYearLast;  
    } 
}
