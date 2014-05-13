package com.bjtu.time2eat.activity;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.example.time2eat.R;


public class RestaurantDetailActivity extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.restdetail);		
	    
        Intent intent=getIntent();  
        TextView textView=(TextView)findViewById(R.id.restID);
       
        String string=intent.getStringExtra("id");
         textView.setText(string);
        //setText(intent.getStringArrayExtra("ID"));
		

	}


}

