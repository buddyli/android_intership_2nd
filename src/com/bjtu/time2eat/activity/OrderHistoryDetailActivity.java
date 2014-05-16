package com.bjtu.time2eat.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.time2eat.R;
import com.bjtu.time2eat.pojo.Menu;
import com.bjtu.time2eat.pojo.Response;
import com.bjtu.time2eat.pojo.resbody.RestaurantMenu;
import com.bjtu.time2eat.service.RestaurantService;;

public class OrderHistoryDetailActivity extends Activity{
	private RestaurantService restaurantservice = new RestaurantService();
	private List<Map<String, Object>> meallist;
	private ListView listview;  
    public MyAdapter adapter; 
    private String orders;
    private String danjia;
    private String restaurantid=null;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		Intent intent=getIntent();
	    orders = intent.getStringExtra("orders");
	    String id = intent.getStringExtra("id");
	    String totalprice = intent.getStringExtra("totalprice");
	    String merchantname = intent.getStringExtra("merchantname");
	    String personnum = intent.getStringExtra("personnum");
	    restaurantid = intent.getStringExtra("Restaurant_id");
		//setContentView(R.layout.orderhistory_detail);			
		setContentView(R.layout.orderhistory_detail);  
        listview = (ListView) findViewById(R.id.mealitem_listview);   
        adapter = new MyAdapter(this);  
        listview.setAdapter(adapter);  
        TextView orderid = (TextView) this.findViewById(R.id.titleorderid);
		orderid.setText(id);
		TextView tn = (TextView) this.findViewById(R.id.totalnum);
		try {
			tn.setText("合计: "+(new JSONArray(orders)).length()+"份");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		TextView tp = (TextView) this.findViewById(R.id.totalprice);
		tp.setText(totalprice);
		TextView mn = (TextView) this.findViewById(R.id.merchantname);
		mn.setText(merchantname);
		TextView pn = (TextView) this.findViewById(R.id.personnum);
		pn.setText(personnum);
		
	}
	
	public final class ViewHolder{
		public TextView mealname;
		public TextView pieceprice;
	}
	
	public class MyAdapter extends BaseAdapter{
		
		

		private LayoutInflater mInflater;
		//Response<RestaurantMenu> list = restaurantservice.restaurantMenu(restaurantid);
		public MyAdapter(Context context){
			this.mInflater = LayoutInflater.from(context);
			meallist = new ArrayList<Map<String,Object>>();
			try {
				JSONArray arr = new JSONArray(orders);
				for(int i=0;i<arr.length();i++){
					Map<String, Object> map = new HashMap<String, Object>();
					map = new HashMap<String, Object>();
					map.put("mealname",arr.getString(i));
					//Menu m = new Menu();
					//String[] menus = new String[list.getData().getMenu().size()];
					//int j = 0;
					/*for(Menu menu : list.getData().getMenu()){
						if(menu.getName()==arr.getString(i)){ danjia=menu.getPrice();}
					}*/
					
					//m.setName(arr.getString(i));
					//map.put("pieceprice", "100");
					meallist.add(map);
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		public int getCount() {
			// TODO Auto-generated method stub
			return meallist.size();
		}

		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return null;
		}

		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return 0;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			
			ViewHolder holder = null;
			if (convertView == null) {
				
				holder=new ViewHolder();  
				
				convertView = mInflater.inflate(R.layout.ordermeal_item, null);
				holder.mealname = (TextView)convertView.findViewById(R.id.mealname);
				holder.pieceprice = (TextView)convertView.findViewById(R.id.pieceprice);
				convertView.setTag(holder);
				
			}else {
				
				holder = (ViewHolder)convertView.getTag();
			}
			
			holder.mealname.setText((String)meallist.get(position).get("mealname"));
			holder.pieceprice.setText((String)meallist.get(position).get("pieceprice"));
			
			
			return convertView;
		}
		
	}
	
	}
