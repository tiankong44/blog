package com.tiankong44.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONArray;
import net.sf.json.JSONNull;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import net.sf.json.util.PropertyFilter;
import org.apache.commons.lang.StringUtils;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Json工具类.
 * @author miaoyi
 * 
 */
@Slf4j
public class JsonUtils {
	private final static Integer INTEGER = 1;
	private final static Byte BYTE = Byte.valueOf((byte)1);
	private final static Double DOUBLE = Double.valueOf(0.01);
	private final static Long LONG = Long.valueOf(1);
	private final static Float FLOAT = Float.valueOf(1);
	private final static Boolean BOOLEAN = Boolean.valueOf(false);
	private final static Date DATE = new Date();
	/**
	 * 
	 * 获取普通对象的json串.
	 * 
	 * @param obj
	 *            普通对象
	 * @return
	 */
	public static String getObjectString(Object obj) {
		JSONObject json = JSONObject.fromObject(obj, packageConfig(null));
		return json.toString();
	}

	/**
	 * 
	 * 获取普通对象的json串.
	 * 
	 * @param obj
	 *            普通对象
	 * @param configs
	 *            回环控制参数
	 * @return
	 */
	public static String getObjectString(Object obj, final List<String> configs) {
		JSONObject json = JSONObject.fromObject(obj, packageConfig(configs));

		return json.toString();
	}

	/**
	 * 
	 * 获取数组对象的json串.
	 * 
	 * @param obj
	 *            数组对象
	 * @return
	 */
	public static String getArrayString(Object obj) {
		JSONArray json = JSONArray.fromObject(obj, packageConfig(null));

		return json.toString();
	}

	/**
	 * 
	 * 获取数组对象的json串.
	 * 
	 * @param obj
	 *            数组对象
	 * @param configs
	 *            回环控制参数
	 * @return
	 */
	public static String getArrayString(Object obj, final List<String> configs) {
		JSONArray json = JSONArray.fromObject(obj, packageConfig(configs));

		return json.toString();
	}

	/**
	 * 
	 * 把json字符串转换成普通对象.
	 * 
	 * @param jsonString
	 *            json字符串
	 * @param clz
	 *            普通对象类型
	 * 
	 * @return 普通对象
	 */
	public static <T extends Object> T toObject(String jsonString, Class<? extends T> clz) {
		return toObject(jsonString, clz, null);
	}

	/**
	 * 
	 * 把json字符串转换成普通对象.
	 * 
	 * @param jsonString
	 *            json字符串
	 * @param retClssz
	 *            普通对象类型
	 * @param classMap
	 *            复杂属性类型 如果是集合，则为集合中元素类型，例如： List[Driver] drivers = new ArrayList[Driver]()，设置认为：
	 *            classMap.put("drivers", Driver.class)
	 * 
	 * @return 普通对象
	 */
	@SuppressWarnings("unchecked")
	public static <T extends Object> T toObject(String jsonString, Class<? extends T> retClssz, Map<String, Class<?>> classMap) {
		JSONObject jsonObject = JSONObject.fromObject(jsonString);
		return (T) JSONObject.toBean(jsonObject, retClssz, classMap);
	}
	
	/**
	 * 
	 * 把json字符串转换成数组对象.
	 * 
	 * @param jsonString
	 *            json字符串
	 * @param objectClass
	 *            数组元素对象类型
	 *
	 * @return 数组对象
	 */
	public static Object toArray(String jsonString, Class<?> objectClass) {
		return toArray(jsonString, objectClass, null);
	}

	/**
	 * 
	 * 把json字符串转换成数组对象.
	 * 
	 * @param jsonString
	 *            json字符串
	 * @param objectClass
	 *            数组元素对象类型
	 * @param classMap
	 *            复杂属性类型 如果是集合，则为集合中元素类型，例如： List[Driver] drivers = new ArrayList[Driver]()，设置认为：
	 *            classMap.put("drivers", Driver.class)
	 * 
	 * @return 数组对象
	 */
	public static Object toArray(String jsonString, Class<?> objectClass, Map<String, Class<?>> classMap) {
		JSONArray jsonArray = JSONArray.fromObject(jsonString);
		return JSONArray.toArray(jsonArray, objectClass, classMap);
	}

	/**
	 * 
	 * 把json字符串转换成集合对象.
	 * 
	 * @param jsonString
	 *            json字符串
	 * @param objectClass
	 *            集合元素对象类型
	 * 
	 * @return 集合对象
	 */
	public static Object toCollection(String jsonString, Class<?> objectClass) {
		JSONArray jsonArray = JSONArray.fromObject(jsonString);
		return JSONArray.toCollection(jsonArray, objectClass);
	}

	/**
	 * 
	 * 封装参数.
	 * 
	 * @param configs
	 *            外部传入参数
	 * @return jsonconfig
	 */
	private static JsonConfig packageConfig(final List<String> configs) {
		JsonConfig config = new JsonConfig();
		config.setExcludes(new String[] {"handler", "hibernateLazyInitializer"});
		// 添加此句可以解决hibernate查询出的对象转成json串的错误
		config.registerJsonValueProcessor(Date.class, JsonLibDateProcessor.instance);
		config.registerJsonValueProcessor(Timestamp.class, JsonLibTimestampProcessor.instance);
		
		config.setJsonPropertyFilter(new PropertyFilter() {
			public boolean apply(Object source, String name, Object value) {
				if (configs != null && configs.contains(name)) {
					return true;
				}
				return false;
			}

		});

		return config;
	}


	/**
	 * 从请求json中获取想要的参数
	 * @param requestJson
	 * @param params
     * @return
     */
	public static Map<String, Object> getParamMap(JSONObject requestJson, String... params) {
		return getParamMap(requestJson, true, params);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static Map<String, Object> getParamMap(JSONObject requestJson, boolean htmlEncode, String... params) {
		HashMap map = new HashMap();
		String[] arr$ = params;
		int len$ = params.length;
		JSONObject jsonObj =requestJson;
		for(int i$ = 0; i$ < len$; ++i$) {
			String param = arr$[i$];
			if(jsonObj.containsKey(param) && !(jsonObj.get(param) instanceof JSONNull) && jsonObj.getString(param).length() > 0) {

               /* if(htmlEncode) {
                    map.put(param, EncodeUtil.htmlEncode(jsonObj.getString(param).trim()));
                } else {
                    map.put(param, jsonObj.getString(param).trim());
                }*/
				map.put(param,jsonObj.getString(param).trim());
			}
		}

		if(params.length == 0) {
			return jsonObj;
		} else {
			return map;
		}
	}

	/**
	 * 判断jsonObject是否含有params，且不为空，不为null
	 * @param jsonObject
	 * @param params
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static Map noNulls(JSONObject jsonObject, String... params)
	{
		String keys = "";
		for (String str : params)
		{
			String value = "";
			if(jsonObject.containsKey(str))
			{
				value = jsonObject.get(str).toString();
			}

			if (value.trim().length() == 0)
			{
				keys += "," + str;
			}
		}
		if (keys.length() > 0)
		{
			Map retMap =new HashMap();
			retMap.put("status","0");
			retMap.put("desc","入参不正确"+keys+"不能为空");
			return  retMap;
		}
		return null;
	}

	/**
	 * 判断jsonObject是否含有params，且不为空，不为null
	 * @param jsonObject
	 * @param params
	 * @return
	 */
	public static void isNotBlankFileds(JSONObject jsonObject, String... params) throws RequestException {
		String keys = "";
		for (String str : params)
		{
			String value = "";
			if(jsonObject.containsKey(str))
			{
				value = jsonObject.get(str).toString();
			}

			if (value.trim().length() == 0 || "null".equals(value))
			{
				keys += "," + str;
			}
		}
		if (keys.length() > 0)
		{
			throw new RequestException(0,"入参不正确"+keys+"不能为空");
		}
	}

	/**
	 * 注意日期格式
	 * @param cls
	 * @param jsonString
	 * @param <T>
     * @return
     */
	public static <T>T toObject(Class<T> cls ,String jsonString)
	{
		try
		{
			GsonBuilder builder = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss");
			Gson gson = builder.create();
			T t = gson.fromJson(jsonString,cls);
			return t;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			log.error("实体类转换异常");
			log.error(e.getMessage());
		}
		return null;
	}

	/**
	 *
	 * @param cls
	 * @param object json里的字符串字段必须与cls对象里的字段名一致,不支持父类的转换
	 * @param <T>
	 * @return
	 */
	public static <T>T transition(Class<T> cls ,JSONObject object)
	{
		Field [] fields = cls.getDeclaredFields();
		T t = null;
		try
		{
			t = cls.newInstance();
		}
		catch (Exception e)
		{
			log.error("不能实例化"+cls.getName());
		}
		for(Field filed : fields)
		{
			String methodName = null;
			try
			{
				@SuppressWarnings("rawtypes")
				Class filedType = filed.getType();
				String filedName = filed.getName();
				if(object.containsKey(filedName) && object.get(filedName) != null && StringUtils.isNotBlank(object.get(filedName).toString().trim()))
				{
					Object value = object.get(filedName);
					StringBuffer methodBf = new StringBuffer("set");
					char first = filedName.charAt(0);
					char first1 = Character.toUpperCase(first);
					String filedNameNew = first1+filedName.substring(1);
					methodBf.append(filedNameNew);
					methodName = methodBf.toString();
					Method method = cls.getDeclaredMethod(methodBf.toString(),filedType);
					//判断该属性是否是byte类型
					if (filedType.isInstance(JsonUtils.BYTE))
					{
						value = Byte.parseByte(value+"");
					}
					//判断该属性是否是byte类型
					if (filedType.isInstance(JsonUtils.INTEGER))
					{
						value = Integer.parseInt(value+"");
					}
					//判断该属性是否是double类型
					if (filedType.isInstance(JsonUtils.DOUBLE))
					{
						value = Double.parseDouble(value+"");
					}
					//判断该属性是否是Long类型
					if (filedType.isInstance(JsonUtils.LONG))
					{
						value = Long.parseLong(value+"");
					}
					//判断该属性是否是Float类型
					if (filedType.isInstance(JsonUtils.FLOAT))
					{
						value = Float.parseFloat(value+"");
					}
					//判断该属性是否是boolean类型
					if (filedType.isInstance(JsonUtils.BOOLEAN))
					{
						value = Float.parseFloat(value+"");
					}
					//判断该属性是否是Date类型
					if (filedType.isInstance(JsonUtils.DATE))
					{
						SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
						value = format.parse(value+"");
					}

					method.invoke(t,value);
				}
			}
			catch (Exception e)
			{
				log.info("实例化过程中参数："+methodName+"，不能存放至实体类中。");
				e.printStackTrace();
			}
		}
		return t;
	}
}
