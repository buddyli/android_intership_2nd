package com.bjtu.time2eat.activity;

import android.annotation.SuppressLint;
import android.app.ListActivity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.bjtu.time2eat.pojo.Merchant;
import com.bjtu.time2eat.pojo.Response;
import com.bjtu.time2eat.pojo.resbody.RestaurantList;
import com.bjtu.time2eat.service.RestaurantService;
import com.bjtu.time2eat.service.UserService;
import com.example.time2eat.R;

public class MainActivity extends ListActivity {
	private RestaurantService resService = new RestaurantService();
	private UserService userService = new UserService();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// setContentView(R.layout.activity_main);
		// Use an existing ListAdapter that will map an array
		// of strings to TextViews
		setListAdapter(new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, new String[] { "商户列表",
						"地图模式", "预订历史" }));
		getListView().setTextFilterEnabled(true);
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		// TODO Auto-generated method stub
		switch (position) {
		case 0:
			Toast.makeText(MainActivity.this, "商户列表", Toast.LENGTH_SHORT)
					.show();
			new Thread(runnable).start();
			break;
		case 1:
			Toast.makeText(MainActivity.this, "地图模式", Toast.LENGTH_SHORT)
					.show();
			break;
		case 2:
			Toast.makeText(MainActivity.this, "预订历史", Toast.LENGTH_SHORT)
					.show();
			break;
		default:
			super.onListItemClick(l, v, position, id);
			break;
		}

	}

	@SuppressLint("HandlerLeak")
	Handler hander = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			Bundle bundle = msg.getData();
			String[] txt = (String[]) bundle.get("merchants");
			setListAdapter(new ArrayAdapter<String>(MainActivity.this,
					android.R.layout.simple_list_item_1, txt));
			getListView().setTextFilterEnabled(true);
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
					merchants[i++] = merchant.getName();
				}
				bundle.putCharSequenceArray("merchants", merchants);
				msg.setData(bundle);
			}
			hander.sendMessage(msg);
		}
	};

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

}
