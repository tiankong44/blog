package com.tiankong44.util;

import net.sf.json.JsonConfig;
import net.sf.json.processors.JsonValueProcessor;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * json-lib 对 Date 的处理
 * 
 */
public class JsonLibDateProcessor implements JsonValueProcessor {
	/** 供调用的 static 实例 */
	public static final JsonLibDateProcessor instance = new JsonLibDateProcessor();

	private String format = "yyyy-MM-dd HH:mm:ss";
	private SimpleDateFormat sdf = new SimpleDateFormat(format);

	public Object processObjectValue(String key, Object value, JsonConfig jc) {
		if (value == null) {
			return "";
		} else if (value instanceof Date)
			return sdf.format((Date) value);
		else {
			return value.toString();
		}
	}

	public Object processArrayValue(Object value, JsonConfig arg1) {
		return null;
	}
}