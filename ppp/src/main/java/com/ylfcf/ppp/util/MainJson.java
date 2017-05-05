package com.ylfcf.ppp.util;

import java.lang.reflect.Field;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 通过反射机制解析JSON
 * @author Administrator
 *
 */
public class MainJson {
	/**
	 * 遍历方法
	 * @param object
	 * @param c
	 * @param jsonObject
	 * @return
	 */
	private static Object getField(Object object, Class<?> c, JSONObject jsonObject) {
		Field[] fields = c.getDeclaredFields();
		for(int i=0; i<fields.length; i++) {

			Field filed = fields[i];
			filed.setAccessible(true);
			try {

				filed.set(object, jsonObject.getString(fields[i].getName()));

			} catch (Exception e) {
//				e.printStackTrace();
			}
		}
		
		return object;
	}
	/**
	 * （在调用的时候强制转换成传入的实体类）
	 * 将JSONObject转成实体对象
	 * @param c
	 * @param jsonObject
	 * @return
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws JSONException
	 */
	public static Object fromJson(Class<?> c, JSONObject jsonObject) throws InstantiationException, IllegalAccessException  {

		Object object = c.newInstance();
		object = getField(object, c, jsonObject);
		object = getField(object, c.getSuperclass(), jsonObject);

		return object;
	}

	/**
	 * （在调用的时候强制转换成传入的实体类）
	 * 将String转成实体对象
	 * @param c
	 * @param jsonStr
	 * @return
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws JSONException
	 */
	public static Object fromJson(Class<?> c, String jsonStr) throws JSONException, InstantiationException, IllegalAccessException {

		JSONObject jsonObject = new JSONObject(jsonStr);
		Object object = fromJson(c, jsonObject);
		return object;
	}
}
