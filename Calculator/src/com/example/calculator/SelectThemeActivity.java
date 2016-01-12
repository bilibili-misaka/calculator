package com.example.calculator;

import com.example.calculator.utils.CacheConfigUtils;
import com.example.calculator.utils.ThemeUtil;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.Gallery.LayoutParams;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class SelectThemeActivity extends Activity {

	private Gallery gallery;
	private TextView colorName;
	public static int selectTheme;

	private int[] colorResId = { R.drawable.red_bg, R.drawable.yellow_bg,
			R.drawable.green_bg, R.drawable.blue_bg, R.drawable.purple_bg,
			R.drawable.orange_bg, R.drawable.lightblue_bg, R.drawable.pink_bg,
			R.drawable.teal_bg };
	private String[] colorNames = { "red", "yellow", "green", "blue", "purple",
			"orange", "lightblue", "pink", "teal" };
	private int[] themes = {R.style.RedTheme,R.style.YellowTheme,
			R.style.GreenTheme,R.style.BlueTheme,R.style.PurpleTheme,
			R.style.OrangeTheme,R.style.LightBlueTheme,R.style.PinkTheme,
			R.style.TealTheme
			};
	
	private Context context;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTheme(App.currentTheme);
		setContentView(R.layout.activity_theme_select);
		initView();
		initData();
		
	}

	private void initView() {
		context = this;
		gallery = (Gallery) findViewById(R.id.gallery);
		colorName = (TextView) findViewById(R.id.colorName);

	}

	private void initData() {
		gallery.setAdapter(new MyAdapter());
		gallery.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View view,
					int position, long arg3) {
				colorName.setText(colorNames[position]);
				switchTheme(position);
			}
		});

		gallery.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View view,
					int position, long arg3) {
				colorName.setText(colorNames[position]);
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}
		});

	}

	public void back(View view){
		finish();
	}
	public void randomTheme(View view){
		switchTheme();
		Toast.makeText(context, "已使用随机颜色", 0).show();
	}
	
	private void switchTheme(){
		App.currentTheme = ThemeUtil.randomTheme();
		CacheConfigUtils.putInt(context, "theme", App.currentTheme);
		finish();
		overridePendingTransition(0, 0);
		Intent intent = new Intent();
		intent.setAction("switchTheme");
		sendBroadcast(intent);
	}
	
	protected void switchTheme(int position) {
		
		App.currentTheme = themes[position];
		CacheConfigUtils.putInt(context, "theme", App.currentTheme);
		finish();
		overridePendingTransition(0, 0);
		Intent intent = new Intent();
		intent.setAction("switchTheme");
		MainActivity.switchTheme = true;
		sendBroadcast(intent);
		
	}
	
	class MyAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return colorResId.length;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			int colorId = colorResId[position];
			ImageView image = new ImageView(context);
			image.setImageResource(colorId);
			image.setAdjustViewBounds(true);
			image.setLayoutParams(new Gallery.LayoutParams(
					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
			return image;
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

	}

}
