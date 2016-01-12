package com.example.calculator.utils;

import com.example.calculator.App;

public class NumberConvertUtil {

		private static String str;
		private static char unit;
		private static char ten;
		private static char hundred;
		private static char thousand;
		private static int count;
		private static boolean upFlag;
		
		public static void initData(){
			upFlag = CacheConfigUtils.getBoolean(App.context, "moneyUp");
			if(upFlag){
				unit= '圆';
				ten = '拾';
				hundred = '佰';
				thousand = '仟';
			}else{
				unit = '元';
				ten = '十';
				hundred = '百';
				thousand = '千';
			}
		}
		
		public static String numberConvert (String s) {
			initData();
			str= unit+"";
			count = 0;
			
			convert(s);
			delete0();
			
			return str;
		}
		
		private static void delete0 () {
			for (int i = 1; i < str.length(); i++) {
				if ((str.charAt(i) == '零' && str.charAt(i - 1) == '零')
				        || (str.charAt(i) == '零' && str.charAt(i + 1) == '零')) {
					str = str.substring(0, i) + str.substring(i + 1, str.length());
				}
				if ((str.charAt(i - 1) == '零') && (str.charAt(i) == '亿' || str.charAt(i) == '万' || str.charAt(i) == thousand
				        || str.charAt(i) == hundred || str.charAt(i) == ten || str.charAt(i) == unit)) {
					str = str.substring(0, i - 1) + str.substring(i, str.length());
				}
			}
		}
		
		private static void convert (String s) {
			// 个， 十， 百， 千， 万， 十万， 百万， 千万， 亿
			boolean flag = false;
			int index = s.length();
			
			for (int i = 0; i < s.length(); i++) {
				if (s.charAt(i) == '.') {
					flag = true;
					index = i;
					break;
				}
				
			}
			
			if (flag) {
				if (s.length() - 1 - index == 1) {
					str = unit + method(s.charAt(index + 1)) + "角";
				}else{
					str = unit + method(s.charAt(index + 1)) + "角" + method(s.charAt(index + 2)) + "分";
				}
				
			}
			int length = index;
			if (length > 0) {
				String s1 = s;
				
				if (length > 4) {
					s1 = s.substring(index - 4, index);
				} else {
					s1 = s.substring(0, index);
				}
				int len = s1.length();
				
				if (len == 4) {
					if (s1.charAt(3) != '0') {
						str = method(s1.charAt(3)) + str;
					}
					if (s1.charAt(2) == '0') {
						str = method(s1.charAt(2)) + str;
					} else {
						str = method(s1.charAt(2)) + ten + str;
					}
					if (s1.charAt(1) == '0') {
						str = method(s1.charAt(1)) + str;
					} else {
						str = method(s1.charAt(1)) + hundred + str;
					}
					if (s1.charAt(0) == '0') {
						str = method(s1.charAt(0)) + str;
						
					} else {
						str = method(s1.charAt(0)) + thousand + str;
					}
				} else if (len == 3) {
					if (s1.charAt(2) != '0') {
						str = method(s1.charAt(2)) + str;
					}
					if (s1.charAt(1) == '0') {
						str = method(s1.charAt(1)) + str;
					} else {
						str = method(s1.charAt(1)) + ten + str;
					}
					if (s1.charAt(0) == '0') {
						str = method(s1.charAt(0)) + str;
					} else {
						str = method(s1.charAt(0)) + hundred + str;
					}
					
				} else if (len == 2) {
					if (s1.charAt(1) != '0') {
						str = method(s1.charAt(1)) + str;
					}
					if (s1.charAt(0) == '0') {
						str = method(s1.charAt(0)) + str;
					} else {
						str = method(s1.charAt(0)) + ten + str;
					}
					
				} else if (len == 1) {
					if (s1.charAt(0) != '0') {
						str = method(s1.charAt(0)) + str;
					}
				}
				
			}
			count++;
			if (length > 4) {
				if (count == 1) {
					str = "万" + str;
				} else if (count == 2) {
					str = "亿" + str;
				} else if (count == 3) {
					str = "万" + str;
				}
				String s2 = s.substring(0, index - 4);
				convert(s2);
				
			}
			
		}
		
		private static String method (char c) {
			
			if(!upFlag){
				return convertUp1(c);
			}else{
				return convertUp2(c);
			}
		}
		
		private static String convertUp1(char c){
			switch (c) {
			case '0':
				return "零";
			case '1':
				return "一";
			case '2':
				return "二";
			case '3':
				return "三";
			case '4':
				return "四";
			case '5':
				return "五";
			case '6':
				return "六";
			case '7':
				return "七";
			case '8':
				return "八";
			case '9':
				return "九";
				
		}
		return "";
		}
		
		private static String convertUp2 (char c) {
			switch (c) {
			case '0':
				return "零";
			case '1':
				return "壹";
			case '2':
				return "贰";
			case '3':
				return "叁";
			case '4':
				return "肆";
			case '5':
				return "伍";
			case '6':
				return "陆";
			case '7':
				return "柒";
			case '8':
				return "捌";
			case '9':
				return "玖";
				
			}
			return "";
		}
}
