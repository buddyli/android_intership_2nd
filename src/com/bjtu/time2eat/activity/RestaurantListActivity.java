package com.bjtu.time2eat.activity;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.bjtu.time2eat.pojo.Merchant;
import com.bjtu.time2eat.pojo.Response;
import com.bjtu.time2eat.pojo.resbody.RestaurantList;
import com.bjtu.time2eat.service.RestaurantService;
import com.example.time2eat.R;

@SuppressLint("HandlerLeak") public class RestaurantListActivity extends ListActivity {

	ProgressDialog m_pDialog;
	private List<Map<String, Object>> list = null;
	private RestaurantService resService = new RestaurantService();
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState); 
        initLoadingDialog();
        this.getListView().setOnItemClickListener(new OnItemClickListener() {        	
        	public void onItemClick(AdapterView<?> adapterView, View view,
					int position, long id) {        		
				 Intent intent = new Intent();
				 intent.setClass(RestaurantListActivity.this, RestaurantDetailActivity.class);		
				 intent.putExtra("id", list.get(position).get("id")
				 .toString());
				 intent.putExtra("name", list.get(position).get("name")
				 .toString());
				 startActivity(intent);
        		Toast.makeText(getApplicationContext(), "211212", Toast.LENGTH_SHORT).show();

			}
		});
		new Thread(runnable).start();      
    }    
	
	@SuppressWarnings("deprecation")
	private void initLoadingDialog() {
		// 创建ProgressDialog对象
		m_pDialog = new ProgressDialog(RestaurantListActivity.this);
		// 设置进度条风格，风格为圆形，旋转的
		m_pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		// 设置ProgressDialog 标题
		m_pDialog.setTitle("玩命加载商户列表...");
		// 设置ProgressDialog 提示信息
		// m_pDialog.setMessage("正在加载历史预订记录...");
		// 设置ProgressDialog 标题图标
		// m_pDialog.setIcon(R.drawable.img1);
		// 设置ProgressDialog 的进度条是否不明确
		m_pDialog.setIndeterminate(false);
		// 设置ProgressDialog 是否可以按退回按键取消
		m_pDialog.setCancelable(true);
		// 设置ProgressDialog 的一个Button
		m_pDialog.setButton("确定", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int i) {
				// 点击“确定按钮”取消对话框
				dialog.cancel();
			}
		});
		// 让ProgressDialog显示
		m_pDialog.show();
	}
	
	@SuppressLint("HandlerLeak")
	Handler hander = new Handler() {
		@SuppressLint("HandlerLeak") @Override
		public void handleMessage(Message msg){
			super.handleMessage(msg);
			if (m_pDialog != null) {
				m_pDialog.hide();
			}
			Bundle bundle = msg.getData();			
			String[] txt = (String[]) bundle.get("merchants");			
			list=new ArrayList<Map<String,Object>>();
			String [] restInfo;
			//Toast.makeText(getApplicationContext(),txt[0], Toast.LENGTH_SHORT).show();
			for (int i = 0; i < txt.length; i++) {	
				restInfo=txt[i].split("\\|");//将获得的字符串
				//Toast.makeText(getApplicationContext(),"qqqqq", Toast.LENGTH_SHORT).show();
				Map<String, Object> map = new HashMap<String, Object>();
				map = new HashMap<String, Object>();				
				map.put("id",restInfo[0]);
				map.put("name", restInfo[1]);
				map.put("address", restInfo[2]);
				//map.put("price", restInfo[3]);
    		//
				list.add(map);
			}/**/
			//for (int i = 0; i < txt.length; i++) {	
				//restInfo=txt[i].split("*");			
			
			SimpleAdapter adapter = new SimpleAdapter(RestaurantListActivity.this,list, R.layout.restlistitem,
					new String[] {"id","name","address"},
					new int[] { R.id.restaurantid, R.id.restaurantname,R.id.restaurantaddress});
			setListAdapter(adapter);
//			
//			setListAdapter(new ArrayAdapter<String>(RestListActivity.this,
//					android.R.layout.simple_list_item_1, txt));
//			getListView().setTextFilterEnabled(true);

    	//	Toast.makeText(getApplicationContext(), restInfo[1], Toast.LENGTH_SHORT).show();
			
		}
	};

	Runnable runnable = new Runnable() {

		@Override
		public void run() {
			Response<RestaurantList> list = resService
					.searchRestaurants("", "");
			Message msg = new Message();
			if (list != null && list.getData() != null
					&& list.getData().getResult() != null) {
				Bundle bundle = new Bundle();
				String[] merchants = new String[list.getData().getResult()
						.size()];
				int i = 0;
				for (Merchant merchant : list.getData().getResult()) {
					merchants[i++] = merchant.getId()+"|"+merchant.getName()+"|"+merchant.getAddress()+"|"+merchant.getPrice();
					//merchants[i++] = merchant.getAddress();
				}
				bundle.putCharSequenceArray("merchants", merchants);
				msg.setData(bundle);
			}
			hander.sendMessage(msg);
		}
	};
       
}
