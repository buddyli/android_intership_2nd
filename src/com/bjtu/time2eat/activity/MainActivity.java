package com.bjtu.time2eat.activity;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.time2eat.R;

public class MainActivity extends ListActivity {

	private String[] menuItems = { "商户列表", "地图模式", "预订历史" };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// setContentView(R.layout.activity_main);
		// Use an existing ListAdapter that will map an array
		// of strings to TextViews
		setListAdapter(new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, menuItems));
		getListView().setTextFilterEnabled(true);
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		// TODO Auto-generated method stub
		Intent intent = null;
		switch (position) {
		case 0:
			Toast.makeText(MainActivity.this, menuItems[0], Toast.LENGTH_SHORT)
					.show();
			// new Thread(runnable).start();
			intent = new Intent(this, RestaurantsActivity.class);
			startActivity(intent);
			break;
		case 1:
			Toast.makeText(MainActivity.this, menuItems[1], Toast.LENGTH_SHORT)
					.show();
			intent = new Intent(this, MapActivity.class);
			startActivity(intent);
			break;
		case 2:
			Toast.makeText(MainActivity.this, menuItems[2], Toast.LENGTH_SHORT)
					.show();
			intent = new Intent(this, UserActivity.class);
			startActivity(intent);
			break;
		default:
			super.onListItemClick(l, v, position, id);
			break;
		}

	}

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
