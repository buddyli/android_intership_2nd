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
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.bjtu.time2eat.pojo.Order;
import com.bjtu.time2eat.pojo.Response;
import com.bjtu.time2eat.pojo.resbody.UserOrderHistory;
import com.bjtu.time2eat.service.UserService;
import com.example.time2eat.R;

/**
 * 用户预订餐馆、加载预订历史等Activity
 * 
 * @author licb
 * 
 */
public class OrderHistoryActivity extends ListActivity {
	private UserService userService = new UserService();
	private String mobile = null;
	private ProgressDialog m_pDialog;
	private List<Map<String, Object>> list = null;

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initLoadingDialog();
		// 加载用户预订历史记录
		Intent intent=getIntent();
		mobile = intent.getStringExtra("mobile");
		/*if (StringUtils.isBlank(mobile)) {
			mobile = "15210078395";
		}*/
		
		this.getListView().setOnItemClickListener(new OnItemClickListener() {        	
        	public void onItemClick(AdapterView<?> adapterView, View view,
					int position, long id) {        		
				 Intent intent = new Intent();
				 intent.setClass(OrderHistoryActivity.this, OrderHistoryDetailActivity.class);			    
				 intent.putExtra("id", list.get(position).get("orderid").toString());
				 intent.putExtra("orders", list.get(position).get("orders").toString());	
				 intent.putExtra("totalprice", list.get(position).get("cost").toString());
				 intent.putExtra("merchantname", list.get(position).get("name").toString());
				 intent.putExtra("personnum", list.get(position).get("num").toString());
				 intent.putExtra("Restaurant_id", list.get(position).get("Restaurant_id").toString());
				 startActivity(intent);
        		//Toast.makeText(getApplicationContext(), "211212", Toast.LENGTH_SHORT).show();

			}
		});
		new Thread(runnable).start();
	}

	/**
	 * 创建和一个ProgressDialog对象
	 */
	@SuppressWarnings("deprecation")
	private void initLoadingDialog() {
		// 创建ProgressDialog对象
		m_pDialog = new ProgressDialog(OrderHistoryActivity.this);
		// 设置进度条风格，风格为圆形，旋转的
		m_pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		// 设置ProgressDialog 标题
		m_pDialog.setTitle("玩命加载历史记录...");

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

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (m_pDialog != null) {
				m_pDialog.hide();
			}
			Bundle bundle = msg.getData();
			String[] txt = (String[]) bundle.get("orders");
			String[] orders;
			list = new ArrayList<Map<String,Object>>();
			for (int i = 0; i < txt.length; i++) {	
				orders=txt[i].split("\\|");
				Map<String, Object> map = new HashMap<String, Object>();
				map = new HashMap<String, Object>();				
				map.put("datetime",orders[0]);
				map.put("name", orders[1]);
				map.put("cost", "￥"+orders[2]);
				map.put("status", orders[3]);
				map.put("orderid", orders[4]);
				map.put("orders", orders[5]);
				map.put("num", orders[6]);
				map.put("Restaurant_id", orders[7]);
				list.add(map);
			}
			SimpleAdapter adapter = new SimpleAdapter(OrderHistoryActivity.this,list, R.layout.order_history,
					new String[] {"datetime","name","cost","status"},
					new int[] {R.id.datetime,R.id.name,R.id.cost,R.id.status });
			setListAdapter(adapter);
			
/*			//setListAdapter(new ArrayAdapter<String>(UserActivity.this,
					android.R.layout.simple_list_item_1, txt));*/
			getListView().setTextFilterEnabled(true);
		}
	};

	Runnable runnable = new Runnable() {

		@Override
		public void run() {
			Response<UserOrderHistory> list = userService
					.getUserOrderHistory(mobile);
			Message msg = new Message();
			if (list != null && list.getData() != null
					&& list.getData().getHistory() != null) {
				Bundle bundle = new Bundle();
				String[] orders = new String[list.getData().getHistory()
						.size()];
				int i = 0;
				for (Order order : list.getData().getHistory()) {
					orders[i++] = order.getDatetime()+"|"+order.getName()+"|"+order.getCost()
								  +"|"+order.getStatus()+"|"+order.getId()+"|"+order.getOrdered()+"|"+order.getNum()+"|"+order.getRestaurant_id();
				}
				bundle.putCharSequenceArray("orders", orders);
				msg.setData(bundle);
			}
			hander.sendMessage(msg);
		}
	};

}
