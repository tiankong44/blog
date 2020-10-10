package com.tiankong44.util;

import net.sf.json.JsonConfig;
import net.sf.json.processors.JsonValueProcessor;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 * TODO:
 * 
 * @author xiaxl
 * @version 1.0, 2015-3-18/下午1:47:34
 * 
 */

/*** 将Bean中的Timestamp转换为json中的日期字符串*/
public class JsonLibTimestampProcessor implements JsonValueProcessor {
	
	public static final String Default_DATE_PATTERN ="yyyy-MM-dd HH:mm:ss";
	private DateFormat dateFormat ;
	/** 供调用的 static 实例 */
	public static final JsonLibTimestampProcessor instance 
						= new JsonLibTimestampProcessor(Default_DATE_PATTERN);
	
	public JsonLibTimestampProcessor(String datePattern){
		try{
			dateFormat  = new SimpleDateFormat(datePattern);}
		catch(Exception e ){
			dateFormat = new SimpleDateFormat(Default_DATE_PATTERN);
		}
	}
	public Object processArrayValue(Object value, JsonConfig jsonConfig) {
		return process(value);
	}
	public Object processObjectValue(String key, Object value, JsonConfig jsonConfig) {
		return process(value);
	}
	private Object process(Object value){
		if (value == null) {
			return "";
		} else if (value instanceof Timestamp){
		return dateFormat.format((Timestamp)value);
		}else {
			return value.toString();
		}
	}
}