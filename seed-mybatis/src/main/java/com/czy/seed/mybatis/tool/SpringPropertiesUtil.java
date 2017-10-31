package com.czy.seed.mybatis.tool;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * 全局properties
 */
@Component
public class SpringPropertiesUtil extends PropertyPlaceholderConfigurer {

	private static Map<String, Object> ctxPropertiesMap;

	@Override
	protected void processProperties(ConfigurableListableBeanFactory beanFactory, Properties props) throws BeansException {
		super.processProperties(beanFactory, props);
		ctxPropertiesMap = new HashMap<String, Object>();
		for (Object key : props.keySet()) {
			String keyStr = key.toString();
			String value = props.getProperty(keyStr);
			ctxPropertiesMap.put(keyStr, value);
		}
	}

	/**
	 * 获取spring托管的所有配置项目信息
	 * @return
	 */
	public static Map<String, Object> getCtxPropertiesMap() {
		return ctxPropertiesMap;
	}

	/**
	 * 获取spring托管的properties内容
	 *
	 * @param name
	 * @return Object
	 * @author lilongshun
	 * @date 2013-9-14
	 */
	public static <T> T getProperty(String name) {
		return (T) ctxPropertiesMap.get(name);
	}

	/**
	 * 从spring托管的properties中获取指定key的value
	 *
	 * @param keyName 查询的key值
	 * @return String的属性值
	 * @author lilongshun
	 * @date 2013-9-14
	 */
	public static String getStringProperty(String keyName) {
		Object obj = ctxPropertiesMap.get(keyName);
		return obj == null ? "" : obj.toString();
	}

	/**
	 * 从spring托管的properties中获取指定key的value
	 * @param keyName 查询的key值
	 * @return Integer的属性值
	 * @author panlechun
	 * @date 2013-9-14
	 */
	public static Integer getIntegerProperty(String keyName) {
		Object obj = ctxPropertiesMap.get(keyName);
		if (obj == null) {
			return null;
		} else {
			int res = Integer.parseInt(obj.toString());
			return res;
		}
	}

	/**
	 * 向容器中放入指定的键值对
	 *
	 * @param keyName
	 * @return String
	 * @author lilongshun
	 * @date 2013-9-14
	 */
	public static void setPropertyString(String keyName, String value) {
		ctxPropertiesMap.put(keyName, value);
	}

	/**
	 * spring托管的properties，是否含有指定的key
	 *
	 * @param key 指定键
	 * @return 含有返回：true；不含有返回：false
	 */
	public static boolean containsKey(String key) {
		return ctxPropertiesMap.containsKey(key);

	}

	/**
	 * spring托管的properties，是否含有指定的value
	 *
	 * @param value 指定值
	 * @return 含有返回：true；不含有返回：false
	 */
	public static boolean containsValue(String value) {
		return ctxPropertiesMap.containsValue(value);
	}
}
