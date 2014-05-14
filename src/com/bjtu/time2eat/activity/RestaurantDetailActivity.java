package com.bjtu.time2eat.activity;


import java.util.Calendar;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.time2eat.R;

public class RestaurantDetailActivity extends Activity {
		private EditText date;
	    private EditText time;
	    private EditText phone;
	    private final static int DATE_DIALOG = 0;
	    private final static int TIME_DIALOG = 1;
	    private Calendar c;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.restdetail);		 
		
		date=(EditText)findViewById(R.id.date);
	    time=(EditText)findViewById(R.id.time);
		 phone=(EditText)findViewById(R.id.phone);	
		 Intent intent=getIntent();  
		 date.setFocusable(false);
	      time.setFocusable(false);
        
        TextView restID=(TextView)findViewById(R.id.showRestID); 
        //restID.setText(intent.getStringExtra("id"));         
         TextView restName=(TextView)findViewById(R.id.showRestName);   
         restName.setText(intent.getStringExtra("name"));
         TextView restAddress=(TextView)findViewById(R.id.showRestAddress);   
         restAddress.setText(intent.getStringExtra("address"));
         TextView restTelno=(TextView)findViewById(R.id.showRestTelno);   
         restTelno.setText(intent.getStringExtra("telno"));
         TextView restPrice=(TextView)findViewById(R.id.showRestPrice);   
         restPrice.setText(intent.getStringExtra("price"));
       
        date.setOnClickListener(new View.OnClickListener(){
            @SuppressWarnings("deprecation")
			public void onClick(View v) {	
            	
                showDialog(DATE_DIALOG);
            }
        });
        time.setOnClickListener(new View.OnClickListener(){
            @SuppressWarnings("deprecation")
			public void onClick(View v) {
                showDialog(TIME_DIALOG);
            }
        });	    
    
        phone.setOnFocusChangeListener(new android.view.View.OnFocusChangeListener() {  
        	@Override  
        	public void onFocusChange(View v, boolean hasFocus) {  
        		if(hasFocus) {
            	// 此处为得到焦点时的处理内容
            	if(phone.getText().toString().equals("请输入电话号码"))
            	phone.setText("");
        		} else {
        			// 此处为失去焦点时的处理内容
        		}
        	}
        });
	    
}	        @Override
	    protected Dialog onCreateDialog(int id) {	
        	Dialog dialog = null;	        	
	        switch (id) {
	        case DATE_DIALOG:
	            c = Calendar.getInstance();
	            dialog = new DatePickerDialog(
	                this,
	                new DatePickerDialog.OnDateSetListener() {
	                    public void onDateSet(DatePicker dp, int year,int month, int dayOfMonth) {
	                        date.setText("您选择了：" + year + "年" + (month+1) + "月" + dayOfMonth + "日");
	                    }
	                }, 
	                c.get(Calendar.YEAR), // 传入年份
	                c.get(Calendar.MONTH), // 传入月份
	                c.get(Calendar.DAY_OF_MONTH) // 传入天数
	            );
	            break;
	        case TIME_DIALOG:
	            c=Calendar.getInstance();
	            dialog=new TimePickerDialog(
	                this, 
	                new TimePickerDialog.OnTimeSetListener(){
	                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
	                        time.setText("您选择了："+hourOfDay+"时"+minute+"分");
	                    }
	                },
	                c.get(Calendar.HOUR_OF_DAY),
	                c.get(Calendar.MINUTE),
	                false
	            );
	            break;
	        }
	        return dialog;		        
	    }

	public void openOrderdish(View v) {
		Intent intent =new Intent();
		intent.setClass(this, OrderDishActivity.class);		
		//intent.putExtra("name", restInfo.getName());
		startActivity(intent);			
	}


}

