package com.bjtu.time2eat.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
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
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.bjtu.time2eat.pojo.Menu;
import com.bjtu.time2eat.pojo.Response;
import com.bjtu.time2eat.pojo.resbody.RestaurantMenu;
import com.bjtu.time2eat.service.RestaurantService;
import com.example.time2eat.R;

@SuppressLint("HandlerLeak")
public class OrderDishActivity extends ListActivity {

	private String[] dishInfo;
	private String restID;
	private Button yesBtn;
	private ListView dishlist;
	private List<Boolean> mChecked;		
	//mChecked = new ArrayList<Boolean>();
	private List<Integer> listItemID = new ArrayList<Integer>();
	private CheckBox checkBox;
	private ProgressDialog m_pDialog;
	private List<Map<String, Object>> list = null;
	private RestaurantService resService = new RestaurantService();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dishlist);
		initLoadingDialog();
		Intent intent = getIntent();
		restID = intent.getStringExtra("restID");
		yesBtn= (Button) findViewById(R.id.dishBtn);
		//checkBox = (CheckBox) findViewById(R.id.CheckBox01);
		//dishlist=(ListView) findViewById(R.id.dishlist);
	
		//checkBox.
		/*dishlist.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				Toast.makeText(getApplicationContext(), arg2,Toast.LENGTH_SHORT).show();
			}
			
		});
		
		this.getListView().setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> adapterView, View view,
					int position, long id) {
				//checkBox.setChecked(mChecked.get(position));
				Toast.makeText(getApplicationContext(), position,Toast.LENGTH_SHORT).show();

			}
		});*/
		new Thread(runnable).start();
		
		
		
		/*checkBox.setOnClickListener(new View.OnClickListener() {
			int position;
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				checkBox.setChecked(mChecked.get(position));
			}
		});*/
		yesBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				StringBuilder sb = new StringBuilder();				
				for(int i=0;i<listItemID.size();i++){
					sb.append("ItemID="+listItemID.get(i)+" . ");
				}
				AlertDialog.Builder builder2 = new AlertDialog.Builder(OrderDishActivity.this);
				builder2.setMessage(sb.toString());
				builder2.show();
				
			}
		});
		
	}

	@SuppressWarnings("deprecation")
	private void initLoadingDialog() {
		// 创建ProgressDialog对象
		m_pDialog = new ProgressDialog(OrderDishActivity.this);
		// 设置进度条风格，风格为圆形，旋转的
		m_pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		// 设置ProgressDialog 标题
		m_pDialog.setTitle("玩命加载菜品列表...");
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
		@SuppressLint("HandlerLeak")
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (m_pDialog != null) {
				m_pDialog.hide();
			}
			Bundle bundle = msg.getData();
			String[] txt = (String[]) bundle.get("dishs");
			list = new ArrayList<Map<String, Object>>();
			mChecked = new ArrayList<Boolean>();
			for (int i = 0; i < txt.length; i++) {
				dishInfo = txt[i].split("\\|");// 将获得的字符串
				Map<String, Object> map = new HashMap<String, Object>();
				map = new HashMap<String, Object>();
				//map.put("id",dishInfo[0]);
				map.put("name", dishInfo[1]);
				map.put("price", dishInfo[2]);
	    		mChecked.add(false);
				//
				list.add(map);
			}/**/
			SimpleAdapter adapter = new SimpleAdapter(OrderDishActivity.this,
					list, R.layout.dishlistitem, new String[] { "name",
							"price" }, new int[] { R.id.dishname,
							R.id.dishprice});
			setListAdapter(adapter);
			
		}
	};

	Runnable runnable = new Runnable() {

		public void run() {
			Response<RestaurantMenu> list = resService.restaurantMenu(restID);
			Message msg = new Message();
			if (list != null && list.getData() != null
					&& list.getData().getMenu() != null) {
				Bundle bundle = new Bundle();
				String[] dishs = new String[list.getData().getMenu().size()];
				int i = 0;
				for (Menu dish : list.getData().getMenu()) {
					dishs[i++] = dish.getId() + "|" + "菜品名称：" + dish.getName()
							+ "|" + "菜品价格：" + dish.getPrice();
				}
				bundle.putCharSequenceArray("dishs", dishs);
				msg.setData(bundle);
			}
			hander.sendMessage(msg);
		}
	};
}

