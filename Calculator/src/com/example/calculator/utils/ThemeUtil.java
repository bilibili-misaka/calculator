package com.example.calculator.utils;


import com.example.calculator.R;

public class ThemeUtil {
	
	private static int[] themes = {R.style.RedTheme,R.style.YellowTheme,
			R.style.GreenTheme,R.style.BlueTheme,R.style.PurpleTheme,
			R.style.OrangeTheme,R.style.LightBlueTheme,R.style.PinkTheme,
			R.style.TealTheme
			};
	
	public static int randomTheme(){
		int n = (int)(Math.random()*9);
		return themes[n];
	}
	
}
