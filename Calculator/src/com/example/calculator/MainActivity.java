package com.example.calculator;


import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewAnimationUtils;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.calculator.utils.CacheConfigUtils;
import com.example.calculator.utils.ExpressionUtil;
import com.example.calculator.utils.NumberConvertUtil;
import com.example.calculator.utils.ThemeUtil;
import com.example.view.LayoutRipple;
import com.nineoldandroids.animation.ValueAnimator;
import com.nineoldandroids.animation.ValueAnimator.AnimatorUpdateListener;
import com.nineoldandroids.view.ViewPropertyAnimator;

public class MainActivity extends Activity {

	private static TextView numberText;
	private static TextView numberText2;
	private static String previous = "0";         //记录之前的运算数
	private static String later = "0";            //记录最后的运算数
	private static String result = "0";     //记录运算结果
	private static char c = '0';          //记录运算符
	private static char c2 = '0';       //记录上一个运算符
	private static boolean flag;    //记录运算符输入状态
	private static boolean flag2;   //记录数字输入状态
	private static boolean flag3;   //界面切换状态
	private static boolean isHistoryClear;		//是否清理历史记录
	private static boolean isHistoryClearBefore;	//上一次的记录
	private static boolean isExpression = false;	//是否是表达式求值
	private static String s = "";
	private static String s2 = "";
	private static String st = "";
	private static int count = 0;
    
    private static List<String> resultList;
    private static int index = 0;
	
    private LinearLayout text;
    private LinearLayout numberView;
    private LinearLayout operatorView;
    private FrameLayout frame;
    private RelativeLayout rl;
    private TextView addHistory;
    private RippleView rippleView;
	private int height;
	private BroadcastReceiver receiver;
	public static boolean switchTheme;
	
	
    private Handler handler = new Handler(){
    	@Override
    	public void handleMessage(Message msg) {

    		switch(msg.what){
	    		case 1 :
//	    			rippleView.dispatchTouchEvent(MotionEvent.obtain(SystemClock.uptimeMillis(), 
//	    					 SystemClock.uptimeMillis(), 
//	    					 MotionEvent.ACTION_DOWN, rippleView.getWidth()/2, rippleView.getHeight()/2, 0));
	    			rippleView.dispatchTouchEvent(MotionEvent.obtain(SystemClock.uptimeMillis(), 
	    	        		 SystemClock.uptimeMillis(),
	    	        		 MotionEvent.ACTION_UP, rippleView.getWidth()/2, rippleView.getHeight()/2, 0));
	    			break;
    		}
    		
    	}
    };
    
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(CacheConfigUtils.getBoolean(App.context, "randomTheme") && !switchTheme){
        	App.currentTheme = ThemeUtil.randomTheme();
        }
        setTheme(App.currentTheme);
        
        setContentView(R.layout.activity_main);
        
        initView();
        
    }

	private void initView() {
		numberText = (TextView) this.findViewById(R.id.text);
        numberText2 = (TextView) this.findViewById(R.id.text2);
        
        addHistory = (TextView) findViewById(R.id.addTimeText);
        
        rl = (RelativeLayout) findViewById(R.id.rl);
        text = (LinearLayout) findViewById(R.id.ll);
        rippleView = (RippleView)findViewById(R.id.rippleView);
        
        resultList = new ArrayList<String>();
        
        frame = (FrameLayout) findViewById(R.id.frame);
        numberView = (LinearLayout) findViewById(R.id.numberView);
        operatorView = (LinearLayout) findViewById(R.id.operatorView);
        
        receiver = new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {
				if("switchTheme".equals(intent.getAction())){
					switchTheme();
				}
			}
		};
		
		IntentFilter filter = new IntentFilter();
		filter.addAction("switchTheme");
		registerReceiver(receiver, filter);
	}
    
	@Override
    protected void onDestroy() {
    	super.onDestroy();
    	unregisterReceiver(receiver);
    }
	
	public void initAnimator(){
		//1.计算高度
		text.measure(0, 0);
		height = text.getMeasuredHeight();
		//2.将高度设置为0
		text.getLayoutParams().height = 10;
		text.requestLayout();
		
		ValueAnimator animator = null;
	
		animator = ValueAnimator.ofInt(10,height);
		
		//监听动画执行流程
		animator.addUpdateListener(new AnimatorUpdateListener() {
			@Override
			public void onAnimationUpdate(ValueAnimator animator) {
				//在动画执行过程中，去获取当前动画的值
				int animatedValue =   (Integer) animator.getAnimatedValue();
				//将当前动画的值设置给rippleView的高度
				text.getLayoutParams().height = animatedValue;
				text.requestLayout();
			}
		});
		animator.setDuration(500);
		animator.start();
	}
	
	public void switchTheme(View view){
    	Intent intent = new Intent(this,SelectThemeActivity.class);
    	startActivityForResult(intent, 0);
    }
    
	public void exit(View view){
		finish();
	}
	
	public void aboutMe(View view){
		startActivity(new Intent(this,AboutMeActivity.class));
	}
	
	public void instruction(View view){
		startActivity(new Intent(this,InstructionActivity.class));
	}
	
	public void setting(View view){
		startActivity(new Intent(this,SettingActivity.class));
	}
	
    protected void switchTheme() {
    	
    	finish();
    	overridePendingTransition(0, 0);
    	Intent intent = new Intent(this,MainActivity.class);
    	startActivity(intent);
    	
	}
	
    public void switchingOperator(View view){
    	frame.setBackgroundColor(Color.parseColor("#ffffff"));
    	revealImageCircular(operatorView);
    	frame.removeAllViews();
    	frame.addView(operatorView, 0);
    	numberView.setVisibility(-1);
        frame.addView(numberView,1);
    	
    }
    
    public void switchingNumber(View view){
    	frame.setBackgroundColor(Color.parseColor("#DD000000"));
		revealImageCircular(numberView);
    	frame.removeAllViews();
    	frame.addView(numberView, 0);
    	operatorView.setVisibility(-1);
        frame.addView(operatorView,1);
    	
    }
    
    private void revealImageCircular(final View view) {
   	 	int x = getX(view);
        int y = getY(view);
        int radius = view.getHeight();
     
       Animator anim =
               ViewAnimationUtils.createCircularReveal(view, x, y, 0, radius);
     
       anim.setDuration( 1200 );
       anim.addListener( new AnimatorListenerAdapter() {
           @Override
           public void onAnimationStart(Animator animation) {
               super.onAnimationStart(animation);
              
               view.setVisibility(1);

           }
       });
     
       anim.start();
   }

   private int getX(View v){
   	int width = v.getWidth();
   	return width/2;
   }
   private int getY(View v){
   	int height = v.getHeight();
   	return height/2;
   }
    

    public void num(View view){
    	//数字
    	numberText.setTextSize(48);
    	com.example.view.LayoutRipple lr = (LayoutRipple) view;
        view = lr.getChildAt(0);
        String str = numberText2.getText().toString();
        String ss = ((TextView) view).getText().toString();
        
        if("(".equals(ss)){
        	isExpression = true;
        }else if(!isExpression && ")".equals(ss)){
        	return;
        }
        
    	if(isExpression){
    		numberText2.setText(str+ss);
    		s = "";
    		return;
    	}
    	
        if (c == '=') {
            clear();
        }
        
        if(s.length()<14){
        	s+= ss;
        }
        
        flag2 = true;
        
        numberText.setText(s);
        
    }

	public void add(View view){
    	 // 加号    
		if(isExpression){
			numberText2.setText(numberText2.getText()+"+");
			return;
		}
		
        if (flag2 && (c == '+' || c == '-' || c == '*' || c == '/'||c=='^')) {
            flag = false;
        } else {
            flag = true;
        }
        if (flag) {
            previous = checkNumber(numberText.getText().toString());
            flag = true;
        } else {
        	
            opera();
            flag = false;
        }
        c2 = c;
        c = '+';
        if(c2!=c){
            if(c2=='0'){
                      s2 += delete0(previous) + "+";
                 }else if(s2.equals(numberText.getText().toString())){
                      s2 += "+";
                 }else {
                     s2 += delete0(later) + "+";
                 }     
        }
        numberText2.setText(s2);
        flag2 = false;
        s = "";
    }
	public void subtract(View view){
		// 减号    
		if(isExpression){
			numberText2.setText(numberText2.getText()+"-");
			return;
		}
		
		
		if (flag2 && (c == '+' || c == '-' || c == '*' || c == '/'||c=='^')) {
			flag = false;
		} else {
			flag = true;
		}
		if (flag) {
			previous = checkNumber(numberText.getText().toString());
			flag = true;
		} else {
			
			opera();
			flag = false;
		}
		c2 = c;
		c = '-';
		if(c2!=c){
			if(c2=='0'){
				s2 += delete0(previous) + "-";
			}else if(s2.equals(numberText.getText().toString())){
				s2 += "-";
			}else {
				s2 += delete0(later) + "-";
			}     
		}
		
		numberText2.setText(s2);
		flag2 = false;
		s = "";
	}
	public void multiply(View view){
		// 乘号    
		if(isExpression){
			numberText2.setText(numberText2.getText()+"*");
			return;
		}
		
		if (flag2 && (c == '+' || c == '-' || c == '*' || c == '/'||c=='^')) {
			flag = false;
		} else {
			flag = true;
		}
		if (flag) {
			previous = checkNumber(numberText.getText().toString());
			flag = true;
		} else {
			
			opera();
			flag = false;
		}
		c2 = c;
		c = '*';
		if(c2!=c){
			if(c2=='0'){
				s2 += delete0(previous) + "*";
			}else if(s2.equals(numberText.getText().toString())){
				s2 += "*";
			}else {
				s2 = "("+s2;
				s2 += delete0(later) + ")"+ "*";
			}     
		}
		numberText2.setText(s2);
		flag2 = false;
		s = "";
	}
	public void divide(View view){
		// 除号    
		if(isExpression){
			numberText2.setText(numberText2.getText()+"/");
			return;
		}
		
		
		if (flag2 && (c == '+' || c == '-' || c == '*' || c == '/'||c=='^')) {
			flag = false;
		} else {
			flag = true;
		}
		if (flag) {
			previous = checkNumber(numberText.getText().toString());
			flag = true;
		} else {
			
			opera();
			flag = false;
		}
		c2 = c;
		c = '/';
		if(c2!=c){
			if(c2=='0'){
				s2 += delete0(previous) + "/";
			}else if(s2.equals(numberText.getText().toString())){
				s2 += "/";
			}else {
				s2 = "("+s2;
				s2 += delete0(later) + ")"+ "/";
			}     
		}
		
		numberText2.setText(s2);
		flag2 = false;
		s = "";
	}

	public void pow(View view){
		 // 幂运算
		if(isExpression){
			numberText2.setText(numberText2.getText()+"^");
			return;
		}
		
		
        if (flag2 && (c == '+' || c == '-' || c == '*' || c == '/' || c == '^')) {
            flag = false;
        } else {
            flag = true;
        }
        if (flag) {
            previous = checkNumber(numberText.getText().toString());
            flag = true;
        } else {
            opera();
            flag = false;
        }
        c2 = c;
        c = '^';
        if (c2 != c) {
            if (c2 == '0') {
                s2 += delete0(previous) + "^";
            } else if (s2.equals(numberText.getText().toString())) {
                s2 += "^";
            } else {
                s2 = "("+s2;
				s2 += delete0(later) + ")"+ "^";
            }
        }

        numberText2.setText(s2);
        flag2 = false;
        s = "";

	}
	
	public void operation(View view){
		// 等号 
		if(isExpression){
			isExpression = false;
			numberText2.setText(numberText2.getText()+"=");
			try {
				ExpressionUtil.getResult(numberText, numberText2);
			} catch (Exception e) {
				numberText.setText("error!");
				numberText2.setText("");
			}
			
			c = '0';
			c2 = '0';
			s = "";
			s2 = "";
			return;
		}
		
		try {
			opera();
		} catch (Exception e) {
			numberText.setText("error!");
			numberText2.setText("");
		}
        
        
        if (c2 != c) {
            s2 += delete0(later) + "=";
        }
        numberText2.setText(s2);
        
        String str = checkNumber(numberText.getText().toString());
        
        if(c2 != '='){
        	if(index<3){
        		index++;
        	}else if(index >=3){
        		resultList.remove(0);
        	}
        	
        	resultList.add(s2+" "+str);
        	
        	if(resultList!=null  && resultList.size()>0){
        		for(int i =0;i<resultList.size();i++){
        			TextView v = (TextView)rl.getChildAt(i);
        			v.setText("⊙"+resultList.get(i));
        		}
        	}
        }
        changeState();
        
        if(checkNumber(str)=="0"){
        	str = "";
        	c = '0';
        }
        
        s2 = str;
	}

	public void clear(View view){
		//清零按钮
		rippleView.dispatchTouchEvent(MotionEvent.obtain(0, 0, 
				MotionEvent.ACTION_DOWN, rippleView.getWidth()/2, rippleView.getHeight()+30, 0));
//		rippleView.dispatchTouchEvent(MotionEvent.obtain(0, 0, 
//				MotionEvent.ACTION_UP, rippleView.getWidth()/2, rippleView.getHeight()/2, 0));
		
//		new Thread(){
//			public void run() {
//				
//				Message message = Message.obtain();
//				message.what = 1;
//				handler.sendMessage(message);
//				
//			};
//		}.start();
		
		clear();
	}
	
	public void point(View view){
		//小数按钮
		flag2 = true;
        s += ".";
        numberText.setText(s);
	}
	
	 private String add() {
	        BigDecimal b1 = new BigDecimal(previous);
	        BigDecimal b2 = new BigDecimal(later);
	        return b1.add(b2).toString();
	    }

	    private String subtract() {
	        BigDecimal b1 = new BigDecimal(previous);
	        BigDecimal b2 = new BigDecimal(later);
	        return b1.subtract(b2).toString();
	    }

	    private String multiply() {
	        BigDecimal b1 = new BigDecimal(previous);
	        BigDecimal b2 = new BigDecimal(later);
	        return b1.multiply(b2).toString();
	    }

	    private String divide() {
	        BigDecimal b1 = new BigDecimal(previous);
	        if ("0".equals(later)) {
	            return "error!";
	        }
	        BigDecimal b2 = new BigDecimal(later);
	        int n = b1.intValue()/b2.intValue();
	        String s = n+"";
	        
	        return b1.divide(b2, 12-s.length(), BigDecimal.ROUND_HALF_UP).toString();
	    }

	    private String pow() {

	        double d1 = Double.parseDouble(previous);
	        double d2 = Double.parseDouble(later);
	        if(!later.contains(".")){
		        BigDecimal b1 = new BigDecimal(previous);
		        BigDecimal b2 = new BigDecimal(later);
		        return b1.pow(b2.intValue()).toString();
	        }

	        BigDecimal b1 = new BigDecimal(Math.pow(d1, d2));
	        BigDecimal b2 = new BigDecimal("1");
	        return b1.divide(b2 , 13, BigDecimal.ROUND_HALF_UP).toString();
	    }

	    private void opera() {
	    	numberText.setTextSize(48);
	        later = checkNumber(numberText.getText().toString());

	        if (previous.charAt(0) == '.' || later.charAt(0) == '.') {
	            numberText.setText("error!");
	            return;
	        }

	        switch (c) {
	            case '+':
	                result = add();
	                break;
	            case '-':
	                result = subtract();
	                break;
	            case '*':
	                result = multiply();
	                break;
	            case '/':
	                result = divide();
	                break;
	            case '^':
	                result = pow();
	                break;
	            case '0':
	                result = later;
	                break;
	            default:
	                break;
	        }
	        if (result.equals("error!")) {
	            previous = "0";
	        } else {
	            previous = result;
	        }

	        c2 = c;
	        c = '=';
	        s = "";
	        if(result.length()>35){
	        	numberText.setText("error!");
	        	return;
	        }
	        int n = 48;
			 if(result.length()>14){
				n=35;
			 }
		    numberText.setTextSize(n);
	        numberText.setText(delete0(result));

	    }

		
		public static String delete0(String s) {
			int n = 12;
	        char[] c = s.toCharArray();
	        boolean flag = false;
	        int end = 0;
	        int count = 0;
	        int length = c.length;

	        for (int i = 0; i < c.length; i++) {
	            if (c[i] == '.') {
	                count = i;
	                if (length - i > n) {
	                    length = i + n + 1;
	                }
	            }
	        }
	        if (count == 0) {
	            return s;
	        }

	        for (int i = length; i < c.length; i++) {
	            c[i] = '0';
	        }
	        while (count < length) {
	            if (!flag && c[count] != '0') {
	                end = count;
	            } else if (c[count] == '0') {

	                flag = false;
	            }
	            count++;
	        }
	        if (c[end] == '.') {
	            end--;
	        }

	        s = "";
	        for (int i = 0; i <= end; i++) {
	            s += c[i];
	        }
	        return s;
		}
	
	
	public void backSpace(View view){
		if(isExpression){
			String str = numberText2.getText().toString();
			if(str!=null && str.length()>1){
				str = str.substring(0, str.length()-1);
				numberText2.setText(str);
			}else{
				str = "";
				numberText2.setText("");
			}
			return;
		}
		
		if(s!=null && s.length()>1){
			s = s.substring(0, s.length()-1);
			numberText.setText(s);
		}else{
			s = "";
			numberText.setText("0");
		}
		
//		s = "";
//		numberText.setText(s);
		
	}
	public void ceClear(View view){
		s = "";
		numberText.setText("0");
	}
    private void clear() {
    	// 清零C
        s = "";
        s2 = "";
        result = "0";
        previous = "0";
        later = "0";
        c = '0';
        c2 = '0';
        flag = false;
        flag2 = false;
        isExpression = false;
        numberText.setTextSize(48);
        numberText.setText("0");
        numberText2.setText("");
	}
    
    public void clearScreen(View view){
    	clear();
    }
    
    public void percent(View view){

    	BigDecimal b = new BigDecimal(checkNumber(numberText.getText().toString()));
    	BigDecimal b2 = new BigDecimal("100");
    	numberText2.setText(b + "%");
    	numberText.setText("" + b.divide(b2));
        s = "";
    }
    
    //改变历史记录清理按钮的状态
    private void changeState(){
    	View view = findViewById(R.id.addTimeText);
    	if(index>0){
    		isHistoryClear = true;
    	}else{
    		isHistoryClear = false;
    	}
    	if(isHistoryClear != isHistoryClearBefore){
    		
    		if(isHistoryClear){
    			ObjectAnimator oa = ObjectAnimator.ofFloat(view, "rotation", 0,45);
    			oa.setDuration(500);
    			oa.start();
    		}else{
    			ObjectAnimator oa = ObjectAnimator.ofFloat(view, "rotation", 45,0);
    			oa.setDuration(500);
    			oa.start();
    		}
    	}
    	isHistoryClearBefore = 	isHistoryClear;
    }
    
    public void clearHistory(View view){
    	
    	if(index>0){
    		
    		for(int i = 0;i<index;i++){
    			TextView v = (TextView)rl.getChildAt(i);
    			v.setText("");
    		}
    		index = 0;
    		resultList.clear();
    	}else{
    		TextView v = (TextView)rl.getChildAt(0);
    		v.setText("⊙"+numberText2.getText()+" "+numberText.getText());
    		index++;
    	}
    	
    	changeState();
    }
    /////////////////////////////////
    public void factorial(View view){
    	Double d = Double.parseDouble(checkNumber(numberText.getText().toString()));
        
    	if(d>20){
    		numberText.setText("error!");
    		s = "";
        	return;
    	}
    	
    	int n = (int) (d + 0);
        long result = 1;
        if (n == 0) {
            result = 0;
        } else {
            for (int i = n; i > 0; i--) {
                result *= i;
            }
        }
        String str = result+"";
   
         int size = 48;
		 if(str.length()>14){
			 size=35;
		 }
	    numberText.setTextSize(size);
        
        numberText2.setText(n + "!");
        numberText.setText(str);
        s = "";
    }
    
    public void getπ(View view){
    	s2 += "π";
    	numberText2.setText(s2);
    	later = Math.PI+"";
    	numberText.setText(later.substring(0, 14));
        s = "";
    }
    
    public void getE(View view){
    	s2 += "e";
    	numberText2.setText(s2);
    	later = Math.E+"";
    	numberText.setText(later.substring(0, 14));
        s = "";
    }
    
   public void sqrt(View view){
	   numberText2.setText("sqrt(" + delete0(checkNumber(numberText.getText().toString()) + ")"));
	   double num = Math.sqrt(Double.parseDouble(checkNumber(numberText.getText().toString())));
	   
	   BigDecimal b1 = new BigDecimal(num);
       BigDecimal b2 = new BigDecimal("1");
       s = b1.divide(b2, 13-((int)num+"").length(), BigDecimal.ROUND_HALF_UP).toString();

       numberText.setText(delete0(s));
       s = "";
   }
   
    public void reciprocal(View view){
    	String num = checkNumber(numberText.getText().toString());
    	if("0".equals(num)){
    		numberText.setText("error!");
    		return;
    	}
    	numberText2.setText("reciproc(" + numberText.getText() + ")");
        BigDecimal b1 = new BigDecimal(num);
        BigDecimal b2 = new BigDecimal("1");
        int n = 1;
        if(b1.intValue()!=0){
        	n = b2.intValue()/b1.intValue();
        }       
        String str = n+"";
        s = b2.divide(b1, 13-str.length(), BigDecimal.ROUND_HALF_UP).toString();

        numberText.setText(delete0(s));
        s = "";
    }
    
    public void log(View view){
    	Double d = Double.parseDouble(delete0(checkNumber(numberText.getText().toString())));
    	if(d==0){
    		numberText.setText("error!");
    		return;
    	}
        BigDecimal b = new BigDecimal(Math.log10(d));
        BigDecimal b2 = new BigDecimal("1");
            
        String str = b.intValue()+"";
        s = b.divide(b2, 13-str.length(), BigDecimal.ROUND_HALF_UP).toString();
        numberText2.setText("log(" + delete0(d+"") + ")");
        numberText.setText(delete0(s));
        s = "";
    }
    
    public void sin(View view){
    	Double d = Double.parseDouble(delete0(checkNumber(numberText.getText().toString())));
        BigDecimal b = new BigDecimal(Math.sin(d*Math.PI/180));
        BigDecimal b2 = new BigDecimal("1");
        numberText2.setText("sin(" + delete0(d+"") + ")");
        numberText.setText(delete0(b.divide(b2, 12, BigDecimal.ROUND_HALF_UP).toString()));
        s = "";
    }
    
    public void cos(View view){
    	 Double d = Double.parseDouble(delete0(checkNumber(numberText.getText().toString())));
         BigDecimal b = new BigDecimal(Math.cos(d*Math.PI/180));
         numberText2.setText("cos(" + delete0(d+"") + ")");
         numberText.setText(delete0(b+""));
         s = "";
    }
    
    public void tan(View view){
    	Double d = Double.parseDouble(delete0(checkNumber(numberText.getText().toString())));
        BigDecimal b = new BigDecimal(Math.tan(d*Math.PI/180));
        numberText2.setText("tan(" + delete0(d+"") + ")");
        numberText.setText(delete0(b+""));
        s = "";
    }
    
    public void sinh(View view){
    	Double d = Double.parseDouble(delete0(checkNumber(numberText.getText().toString())));
        if(d>100){
        	numberText.setText("error!");
        	s = "";
        	return;
        }
        BigDecimal b = new BigDecimal(Math.sinh(d));
        BigDecimal b2 = new BigDecimal("1");
        numberText2.setText("sinh(" + delete0(d+"") + ")");
        String str = b.toString();
        int n = 48-str.length()*2;
    	if(n<32){
    		n=32;
    	}
    	numberText.setTextSize(n);
        numberText.setText(delete0(b.divide(b2, 12, BigDecimal.ROUND_HALF_UP).toString()));
        s = "";
    }
    
    public void cosh(View view){
    	Double d = Double.parseDouble(delete0(checkNumber(numberText.getText().toString())));
        if(d>100){
        	numberText.setText("error!");
        	s = "";
        	return;
        }
        BigDecimal b = new BigDecimal(Math.cosh(d));
        BigDecimal b2 = new BigDecimal("1");
        numberText2.setText("cosh(" + delete0(d+"") + ")");
        String str = b.toString();
        int n = 48-str.length()*2;
    	if(n<32){
    		n=32;
    	}
    	numberText.setTextSize(n);
        numberText.setText(delete0(b.divide(b2, 12, BigDecimal.ROUND_HALF_UP).toString()));
        s = "";
    }
    
    public void tanh(View view){
    	Double d = Double.parseDouble(delete0(checkNumber(numberText.getText().toString())));
        BigDecimal b = new BigDecimal(Math.tanh(d));
        BigDecimal b2 = new BigDecimal("1");
        numberText2.setText("tanh(" + delete0(d+"") + ")");
        numberText.setText(delete0(b.divide(b2, 12, BigDecimal.ROUND_HALF_UP).toString()));
        s = "";
    }
    
    public void square(View view){
    	 String str = checkNumber(numberText.getText().toString());
    	 BigDecimal b1 = new BigDecimal(str);
    	 if((b1.longValue()+"").length()>14){
    		 numberText.setText("error!");
	    	 s = "";
	    	 return;
    	 }
	  
         int n = 48-str.length()*2;
    	 if(n<32){
    		n=32;
    	 }
    	 numberText2.setText("square(" + delete0(str+"") + ")");
    	 numberText.setTextSize(n);
	     numberText.setText(delete0(b1.pow(2).toString()));   
	     s = "";
    }
    
    public void cube(View view){
    	String str = checkNumber(numberText.getText().toString());
   	 	BigDecimal b1 = new BigDecimal(str);
	   	 if((b1.longValue()+"").length()>12){
			 numberText.setText("error!");
	    	 s = "";
	    	 return;
		 }
	  
	     int n = 48-str.length()*2;
		 if(n<32){
			n=32;
		 }
	    numberText2.setText("cube(" + delete0(str+"") + ")");
	    numberText.setTextSize(n);
	    numberText.setText(delete0(b1.pow(3).toString()));   
	    s = "";
    }
    
    public void ln(View view){
    	Double d = Double.parseDouble(delete0(checkNumber(numberText.getText().toString())));
    	if(d==0){
    		numberText.setText("error!");
    		return;
    	}
    	BigDecimal b = new BigDecimal(Math.log(d));
        BigDecimal b2 = new BigDecimal("1");
        String str = b.intValue()+"";
        
        s = b.divide(b2, 13-str.length(), BigDecimal.ROUND_HALF_UP).toString();
        numberText2.setText("ln(" + delete0(d+"") + ")");
        numberText.setText(delete0(s));
        s = "";
    }
    
    public void numberConvert(View view){
    	String str = checkNumber(numberText.getText().toString());
    	numberText2.setText(str+"=");
    	int n = 40-str.length()*2;
    	
    	if(n<22){
    		n=22;
    	}
    	numberText.setTextSize(n);
    	numberText.setText(NumberConvertUtil.numberConvert(str));
    	s = "";
    }
    
    public void random(View view){
    	String str = checkNumber(numberText.getText().toString());
    	if("0".equals(str)){
    		str = (int)(Math.random()*10000)+"";
    		numberText2.setText("random(4)");
    		
    	}else if(str.length()<=8){
    		numberText2.setText("random("+str.length()+")");
    		int n = (int)Math.pow(10, str.length()-1);
    		int num = n+(int)(Math.random()*9*n);
    		str = num+"";
    		
    	}else{
    		numberText2.setText("random(超出范围)");
    	}
    	
	    numberText.setText(str);   
	    s = numberText.getText().toString();
    }
    
    private String checkNumber(String str){
    	if(str.matches("^(-?\\d+)(\\.\\d+)?$")){
    		return str;
    	}
    	return "0";
    }
    
}
