package com.bjtu.time2eat.activity;


import java.util.Calendar;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiConfiguration.Status;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.bjtu.time2eat.pojo.Merchant;
import com.bjtu.time2eat.pojo.Response;
import com.bjtu.time2eat.pojo.resbody.RestaurantList;
import com.bjtu.time2eat.pojo.resbody.RestaurantOrder;
import com.bjtu.time2eat.service.RestaurantService;
import com.example.time2eat.R;
import com.example.time2eat.R.id;


public class RestaurantDetailActivity extends Activity {
		private EditText date;
	    private EditText time;
	    private EditText phone;
	    private TextView restID;
	    private Button yesorderButton;
	    private final static int DATE_DIALOG = 0;
	    private final static int TIME_DIALOG = 1;
	    private Calendar c;
	    private RestaurantService resService = new RestaurantService();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.restdetail);	 
		
		 date=(EditText)findViewById(R.id.date);
	     time=(EditText)findViewById(R.id.time);
		 phone=(EditText)findViewById(R.id.phone);			 
		 date.setFocusable(false);
	     time.setFocusable(false);
	     //phone.setFocusable(false);
         yesorderButton=(Button)findViewById(R.id.yesOrderBtn);
         restID=(TextView)findViewById(R.id.showRestID); 
         Intent intent=getIntent();        
         TextView restName=(TextView)findViewById(R.id.showRestName);   
         restName.setText(intent.getStringExtra("name"));
         TextView restAddress=(TextView)findViewById(R.id.showRestAddress);   
         restAddress.setText(intent.getStringExtra("address"));
         TextView restTelno=(TextView)findViewById(R.id.showRestTelno);   
         restTelno.setText(intent.getStringExtra("telno"));
         TextView restPrice=(TextView)findViewById(R.id.showRestPrice);   
         restPrice.setText(intent.getStringExtra("price"));
         
        
        /* date.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				// TODO Auto-generated method stub
				showDialog(DATE_DIALOG);  
				 InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                 imm.hideSoftInputFromWindow(date.getWindowToken(),0);
				
			}
		});*/
        date.setOnClickListener(new View.OnClickListener(){
            @SuppressWarnings("deprecation")
			public void onClick(View v) {	
            	 //date.setInputType(InputType.TYPE_NULL);            	
            	showDialog(DATE_DIALOG);  
            }
        });/**/
            
        time.setOnClickListener(new View.OnClickListener(){
            @SuppressWarnings("deprecation")
			public void onClick(View v) {
                showDialog(TIME_DIALOG);
            }
        });	 
        phone.setOnClickListener(new View.OnClickListener(){
            @SuppressWarnings("deprecation")
			public void onClick(View v) {
            	phone.setText("");
            }
        });	
    
       /* phone.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				// TODO Auto-generated method stub
				if(hasFocus)
				{
					phone.setText("");
				}
				else 
				{
					if(phone.getText().toString().endsWith("请输入手机号码"))
					{phone.setText("");}
				}
				
			}
		});*/
        
       yesorderButton.setOnClickListener(new OnClickListener() {
		
		public void onClick(View v) {
			if(date.getText().toString().endsWith("请选择订餐日期")||
		       time.getText().toString().endsWith("请选择订餐时间")||
			   phone.getText().toString().equals("")||
			   phone.getText().toString().equals("请输入手机号码")){
			   Toast.makeText(getApplicationContext(), "请输入必要的订餐信息", Toast.LENGTH_SHORT).show();				
			}	
			else
			{
				try {
					Response<RestaurantOrder> response=resService.restaurantOrder( restID.getText().toString() , phone.getText().toString(),
							"3123,1231", date.getText().toString(),time.getText().toString());	
					String statusString = null;
					//if (response != null && response.getStatus() != null)
					//{						
						statusString =response.getStatus().getMessage();						
					//}
					Toast.makeText(getApplicationContext(), statusString, Toast.LENGTH_LONG).show();
					
				} catch (Exception e) {
					// TODO: handle exception
				}
				
				
			}
		}
       });
	    
}	    
	
	
	   @Override
	  protected Dialog onCreateDialog(int id) {	
        	Dialog dialog = null;       	 
	        switch (id) {
	        case DATE_DIALOG:
	            c = Calendar.getInstance();	           
	            dialog = new DatePickerDialog(
	                this,
	                new DatePickerDialog.OnDateSetListener() {
	                    public void onDateSet(DatePicker dp, int year,int month, int dayOfMonth) {
	                        date.setText(year + "-" + (month+1) + "-" + dayOfMonth);
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
	                        time.setText(hourOfDay+":"+minute);
	                    }
	                },
	                c.get(Calendar.HOUR_OF_DAY),
	                c.get(Calendar.MINUTE),
	                false
	            );
	            break;
	        }
	        return dialog;		        
	    }/* */

	public void openOrderdish(View v) {
		Intent intent =new Intent();
		intent.setClass(this, OrderDishActivity.class);		
		//intent.putExtra("name", restInfo.getName());
		startActivity(intent);			
	}
	


}

