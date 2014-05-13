package com.bjtu.time2eat.activity;


import com.example.time2eat.R;

import android.app.Activity;
import android.app.LocalActivityManager;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;


@SuppressWarnings("deprecation")
public class MainActivity extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);	
		TabHost tabHost = (TabHost) findViewById(android.R.id.tabhost);  
		//TabHost tabHost=getTabHost();  
       // LayoutInflater.from(this).inflate(R.layout.activity_main,tabHost.getTabContentView(), true);  
		 LocalActivityManager mLocalActivityManager = new LocalActivityManager(this, false);  
         mLocalActivityManager.dispatchCreate(savedInstanceState);  
         tabHost.setup(mLocalActivityManager);  
         Intent intent1=new Intent(this, RestaurantListActivity.class); 
         Intent intent2=new Intent(this, MapActivity.class);         
         Intent intent3=new Intent(this, UserActivity.class);    
        // Intent intent4=new Intent(this, RestaurantListActivity.class); 
        tabHost.addTab(tabHost.newTabSpec("tab1").setIndicator("商户列表").setContent(intent1));
        tabHost.addTab(tabHost.newTabSpec("tab2").setIndicator("地图模式").setContent(intent2));  
        tabHost.addTab(tabHost.newTabSpec("tab3").setIndicator("个人中心").setContent(intent3));
       // tabHost.addTab(tabHost.newTabSpec("tab3").setIndicator("更多").setContent(intent4));
        //tabHost.addTab(tabHost.newTabSpec("tab3").setIndicator("更多", getResources().getDrawable(R.drawable.image3)).setContent(intent3));       
        tabHost.setOnTabChangedListener(new OnTabChangeListener() {			
			@Override
			public void onTabChanged(String tabId) {
				// TODO Auto-generated method stub
				  
				if (tabId.equals("tab1")) {
					
    			  }
				if (tabId.equals("tab2")) {
      			  }
				if (tabId.equals("tab3")) {
      			  }
		         
			}
		}) ; 
	}


}

