package com.bjtu.time2eat.activity;

import android.app.ActionBar;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.example.time2eat.R;

public class FatherListActivity extends ListActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		ActionBar bars = getActionBar();
		bars.setDisplayOptions(ActionBar.DISPLAY_HOME_AS_UP,
				ActionBar.DISPLAY_HOME_AS_UP);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.action_bars, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Intent intent = null;
		switch (item.getItemId()) {
		case R.id.bar_home:
			intent = new Intent(this, RestaurantListActivity.class);
			break;
		case R.id.bar_map:
			intent = new Intent(this, LocationOverlayActivity.class);
			break;
		case R.id.bar_user:
			intent = new Intent(this, UserActivity.class);
			break;
		case R.id.bar_nfc:
			intent = new Intent(this, NFCReaderActivity.class);
			break;
		default:
			break;
		}

		if (intent != null) {
			startActivity(intent);
		}
		return super.onOptionsItemSelected(item);
	}

}
