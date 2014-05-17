package com.bjtu.time2eat.activity;

import com.example.time2eat.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

/**
 * 邀请好友
 * 
 * @author Li
 * 
 */
public class UserActivity extends Activity implements OnClickListener {
	private Button btnMyOrder;
	private Button btnInviteFriend;
	private EditText phonetext;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user);
		btnMyOrder = (Button) findViewById(R.id.btn_my_order);
		btnInviteFriend = (Button) findViewById(R.id.btn_invite_friend);
		phonetext = (EditText)findViewById(R.id.phonenumEditText);
		btnMyOrder.setOnClickListener(this);
		btnInviteFriend.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		Intent intent = null;
		switch (v.getId()) {
		case R.id.btn_my_order: // 订餐历史
			intent = new Intent(this, OrderHistoryActivity.class);
			intent.putExtra("mobile", phonetext.getText().toString());
			break;
		case R.id.btn_invite_friend: // 邀请好友
			intent = new Intent(this, WelcomeFriendActivity.class);
			break;
		}
		startActivity(intent);
	}

}
