package com.example.calculator;

import com.example.calculator.utils.CacheConfigUtils;
import com.example.calculator.utils.ThemeUtil;

import android.app.Application;
import android.content.Context;

public class App extends Application{

	public static int currentTheme ;
	public static Context context;
	
	@Override
	public void onCreate() {
		super.onCreate();
		context = this;
//		currentTheme = CacheConfigUtils.getInt(context, "theme", R.style.BlueTheme);
		if(CacheConfigUtils.getBoolean(context, "randomTheme")){
			currentTheme = ThemeUtil.randomTheme();
		}else{
			currentTheme = CacheConfigUtils.getInt(context, "theme", R.style.BlueTheme);
		}
		
		setTheme(currentTheme);
	}
}
