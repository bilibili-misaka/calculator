package com.example.calculator;

import com.example.calculator.utils.CacheConfigUtils;
import com.example.calculator.utils.ThemeUtil;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

public class InstructionActivity extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setTheme(App.currentTheme);
		setContentView(R.layout.activity_instruction);
	}
	
	public void back(View view){
		finish();
	}
}
