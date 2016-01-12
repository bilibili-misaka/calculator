package com.example.calculator.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * 缓存配置的数据的工具�? 用SharedPreferences缓存
 * 
 * @author lijun
 * 
 */
public class CacheConfigUtils {
	private static final String CONFIG = "config";// SharedPreferences关联的xml文件，config.xml
	public static final String ISFIRST = "isFirst";// 
	private static SharedPreferences mSp;

	private static SharedPreferences getPreference(Context context) {
		if (mSp == null) {
			mSp = context.getSharedPreferences(CONFIG, Context.MODE_PRIVATE);
		}
		return mSp;

	}

	// 存布尔数�?
	public static void putBoolean(Context context, String key, boolean value) {
		SharedPreferences sp = getPreference(context);
		sp.edit().putBoolean(key, value).commit();
	}

	// 取布尔数�?,默认是false
	public static boolean getBoolean(Context context, String key) {
		SharedPreferences sp = getPreference(context);
		return sp.getBoolean(key, false);
	}

	// 取布尔数�?,可以设置缺省�?
	public static boolean getBoolean(Context context, String key,
			boolean defvalue) {
		SharedPreferences sp = getPreference(context);
		return sp.getBoolean(key, defvalue);
	}

	// 存字符串数据
	public static void putString(Context context, String key, String value) {
		SharedPreferences sp = getPreference(context);
		sp.edit().putString(key, value).commit();
	}

	// 取字符串数据,默认是null
	public static String getString(Context context, String key) {
		SharedPreferences sp = getPreference(context);
		return sp.getString(key, null);
	}

	// 取字符串数据,可以设置缺省�?
	public static String getString(Context context, String key,
			String defvalue) {
		SharedPreferences sp = getPreference(context);
		return sp.getString(key, defvalue);
	}
	
	
	
	// 存字整数 
	public static void putInt(Context context, String key, int value) {
		SharedPreferences sp = getPreference(context);
		sp.edit().putInt(key, value).commit();
	}
	
	// 取整�?,默认�?0
	public static int getInt(Context context, String key) {
		SharedPreferences sp = getPreference(context);
		return sp.getInt(key, 0);
	}
	
	// 取整�?,可以设置缺省�?
	public static int getInt(Context context, String key,
			int defvalue) {
		SharedPreferences sp = getPreference(context);
		return sp.getInt(key, defvalue);
	}

}
