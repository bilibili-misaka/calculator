package com.example.calculator;

import com.example.calculator.switchbutton.SwitchButton;
import com.example.calculator.utils.CacheConfigUtils;
import com.example.calculator.utils.ThemeUtil;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;

public class SettingActivity extends Activity {

	private SwitchButton switchButton1;
	private SwitchButton switchButton2;
	private boolean colorFlag;
	private boolean UpFlag;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTheme(App.currentTheme);
		setContentView(R.layout.activity_setting);
		switchButton1 = (SwitchButton) findViewById(R.id.switchButton1);
		switchButton2 = (SwitchButton) findViewById(R.id.switchButton2);
		
		colorFlag = CacheConfigUtils.getBoolean(this, "randomTheme");
		UpFlag = CacheConfigUtils.getBoolean(this, "moneyUp");
		
		switchButton1.setChecked(colorFlag);
		switchButton2.setChecked(UpFlag);
		
	}

	public void back(View view) {
		finish();
	}

	public void randomTheme(View view) {
		colorFlag = !switchButton1.isChecked();
		switchButton1.setChecked(colorFlag);
		CacheConfigUtils.putBoolean(App.context, "randomTheme", colorFlag);
	}
	
	public void moneyUp(View view){
		UpFlag = !switchButton2.isChecked();
		switchButton2.setChecked(UpFlag);
		CacheConfigUtils.putBoolean(App.context, "moneyUp", UpFlag);
	}
	
}
