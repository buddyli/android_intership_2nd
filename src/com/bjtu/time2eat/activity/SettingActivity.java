package com.bjtu.time2eat.activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.example.time2eat.R;

/**
 * 
 * @author LiuBao
 * 
 */
public class SettingActivity extends Activity {
	private Switch switchCalendar;
	public static Boolean option=null;
	public static Boolean checkBox1Option=null;
	public static Boolean checkBox2Option=null;
	public static Boolean checkBox3Option=null;
	public static Boolean checkBox4Option=null;
	private CheckBox cb1;
	private CheckBox cb2;
	private CheckBox cb3;
	private CheckBox cb4;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.setting);
		switchCalendar=(Switch) findViewById(R.id.switch1);
		cb1 = (CheckBox) findViewById(R.id.checkBox1);
		cb2 = (CheckBox) findViewById(R.id.checkBox2);
		cb3 = (CheckBox) findViewById(R.id.checkBox3);
		cb4 = (CheckBox) findViewById(R.id.checkBox4);
		
		
		switchCalendar.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {  
            @Override  
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {  
            	
            	option=isChecked;

            }  
        });  

		cb1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {     
			@Override
			public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
				
				checkBox1Option=isChecked;
			}
		});
		cb2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {     
			@Override
			public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
				
				checkBox2Option=isChecked;
			}
		});
		cb3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {     
			@Override
			public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
				
				checkBox3Option=isChecked;
			}
		});
		cb4.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {     
			@Override
			public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
				
				checkBox4Option=isChecked;
			}
		});
 
	
	
	
	}
	
}

