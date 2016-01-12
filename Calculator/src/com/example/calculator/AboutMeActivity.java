package com.example.calculator;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

public class AboutMeActivity extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTheme(App.currentTheme);
		setContentView(R.layout.activity_aboutme);
	}
	
	public void back(View view){
		finish();
	}
}
