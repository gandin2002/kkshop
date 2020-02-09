package cn.bohoon.util;

import java.text.MessageFormat;
import java.util.HashMap;

import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.jdbc.core.JdbcTemplate;

import cn.bohoon.interfaces.domain.SourceType;
import cn.bohoon.interfaces.entity.ErpLink;

public class JDBCTemplateUtil {

    public static HashMap<String, JdbcTemplate> templateMap = new HashMap<String, JdbcTemplate>();
    public static final int INITIALSIZE = 40 ;
    public static final int MAXACTIVE = 240 ;
    public static final int CONNECTION_TIME_OUT = 7200 ;

    public static JdbcTemplate createJDBCTemplate(ErpLink link) {
    	
    	JdbcTemplate template = templateMap.get(link.getId()) ;
    	 
	    	if(null != template ) {
	    		return template ;
	    	}
     
    	String jdbcUrl = link.getSourceType().urlFormat() ;
		jdbcUrl = MessageFormat.format(jdbcUrl, link.getHostName(),link.getPort()+"",link.getDbName()) ;
		
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setDriverClassName(link.getSourceType().getDriver());
        dataSource.setUrl(jdbcUrl);
        dataSource.setUsername(link.getUsername());
        dataSource.setPassword(link.getPassword());
        dataSource.setInitialSize(INITIALSIZE);
        dataSource.setMaxActive(MAXACTIVE);
        if(link.getSourceType().equals(SourceType.MySQL)) {
        	dataSource.setTestWhileIdle(true);
        	dataSource.setValidationQuery("select 1");
        	dataSource.setTimeBetweenEvictionRunsMillis(300000);
        	dataSource.setNumTestsPerEvictionRun(MAXACTIVE);
        	dataSource.setMinEvictableIdleTimeMillis(3600000);
        }
        template = new JdbcTemplate(dataSource);
        templateMap.put(link.getId(), template);
        return template;
    }
    
    
    public static JdbcTemplate getJdbcTemplate(String templateName){
        return templateMap.get(templateName);
    }
}
