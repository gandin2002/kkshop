package cn.bohoon.interfaces.constant;

public class SyncConstants {

	/**
	 * 商城关联字段 json 存放key
	 */
	public static final String MALLLINKCOL_KEY = "mallLinkCol" ;
	
	/**
	 * 同步对接平台 关联字段 json 存放key
	 */
	public static final String THIRDLINKCOL_KEY = "thirdLinkCol" ;
	
	/**
	 * 对接平台为空时候，给商城设置的默认数据
	 */
	public static final String MALL_DEFAULT_VALUE_MAP = "mallValueMap" ;
	
	/**
	 * 商城往对接平台同步数据，对接平台默认字段数据
	 */
	public static final String THIRD_DEFAULT_VALUE_MAP = "thirdValueMap" ;
	
	/**
	 * 需要同步的字段集合  ；格式：key（商城字段），value（对接平台字段）
	 */
	public static final String MALL_THRID_COLMAP = "mallThridColMap" ;
	
	
	/**
	 * 需要同步的字段集合  ；格式：key（对接平台字段），value（商城字段）
	 */
	public static final String THIRD_MALL_COLMAP = "thirdMallColMap" ;
	
	/**
	 * Mall 字段类型
	 */
	public static final String MALL_TYPE_MAP ="mallTypeMap";
	
	/**
	 * erp 字段类型
	 */
	public static final String THIRD_TYPE_MAP ="thirdTypeMap";
	
	/**
	 * 查询语句 SELECT 
	 */
	public static final String _SELECT = " select " ;
	
	/**
	 * SQL语句关键字 from
	 */
	public static final String SQL_FROM  = " from " ;
	
	/**
	 * 逗号
	 */
	public static final String COMMA = "," ;
}
