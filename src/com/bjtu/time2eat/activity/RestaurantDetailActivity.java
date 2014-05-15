package com.bjtu.time2eat.activity;

import java.util.Calendar;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.bjtu.time2eat.pojo.Response;
import com.bjtu.time2eat.pojo.resbody.RestaurantOrder;
import com.bjtu.time2eat.service.RestaurantService;
import com.example.time2eat.R;

public class RestaurantDetailActivity extends Activity {
	private EditText date;
	private EditText time;
	private EditText phone;
	private TextView restID;
	private Button yesorderButton;
	private final static int DATE_DIALOG = 0;
	private final static int TIME_DIALOG = 1;
	private Calendar c;
	private RestaurantService resService = new RestaurantService();
	private static final int OTHER = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.restdetail);

		date = (EditText) findViewById(R.id.date);
		time = (EditText) findViewById(R.id.time);
		phone = (EditText) findViewById(R.id.phone);
		date.setFocusable(false);
		time.setFocusable(false);
		// phone.setFocusable(false);
		yesorderButton = (Button) findViewById(R.id.yesOrderBtn);
		restID = (TextView) findViewById(R.id.showRestID);

		Intent intent = getIntent();
		restID.setText(intent.getStringExtra("id"));
		TextView restName = (TextView) findViewById(R.id.showRestName);
		restName.setText(intent.getStringExtra("name"));
		TextView restAddress = (TextView) findViewById(R.id.showRestAddress);
		restAddress.setText(intent.getStringExtra("address"));
		TextView restTelno = (TextView) findViewById(R.id.showRestTelno);
		restTelno.setText(intent.getStringExtra("telno"));
		TextView restPrice = (TextView) findViewById(R.id.showRestPrice);
		restPrice.setText(intent.getStringExtra("price" + "RMB"));
		// intent.getExtras()

		date.setOnClickListener(new View.OnClickListener() {
			@SuppressWarnings("deprecation")
			public void onClick(View v) {
				// date.setInputType(InputType.TYPE_NULL);
				showDialog(DATE_DIALOG);
			}
		});/**/

		time.setOnClickListener(new View.OnClickListener() {
			@SuppressWarnings("deprecation")
			public void onClick(View v) {
				showDialog(TIME_DIALOG);
			}
		});
		phone.setOnClickListener(new View.OnClickListener() {
			@SuppressWarnings("deprecation")
			public void onClick(View v) {
				phone.setText("");
			}
		});

		/*
		 * phone.setOnFocusChangeListener(new View.OnFocusChangeListener() {
		 * 
		 * @Override public void onFocusChange(View v, boolean hasFocus) { //
		 * TODO Auto-generated method stub if(hasFocus) { phone.setText(""); }
		 * else { if(phone.getText().toString().endsWith("请输入手机号码"))
		 * {phone.setText("");} }
		 * 
		 * } });
		 */

		yesorderButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				if (date.getText().toString().endsWith("请选择订餐日期")
						|| time.getText().toString().endsWith("请选择订餐时间")
						|| phone.getText().toString().equals("")
						|| phone.getText().toString().equals("请输入手机号码")) {

					Toast.makeText(getApplicationContext(), "请输入必要的订餐信息",
							Toast.LENGTH_SHORT).show();

					/*
					 * long calID = 3; long startMillis = 0; long endMillis = 0;
					 * Calendar beginTime= Calendar.getInstance();
					 * beginTime.set(2014, 4, 15, 18, 49);
					 * startMillis=beginTime.getTimeInMillis(); Calendar endTime
					 * = Calendar.getInstance(); endTime.set(2014, 4, 15, 18,
					 * 59); endMillis=endTime.getTimeInMillis();
					 * 
					 * ContentResolver cr = getContentResolver(); ContentValues
					 * values = new ContentValues(); values.put(Events.DTSTART,
					 * startMillis); values.put(Events.DTEND, endMillis);
					 * values.put(Events.TITLE, "hello");
					 * values.put(Events.DESCRIPTION, "calendar");
					 * values.put(Events.CALENDAR_ID, calID);
					 * values.put(Events.EVENT_TIMEZONE, "China/Bei_Jing");
					 * //values.put(Events., "hello"); Uri uri =
					 * cr.insert(Events.CONTENT_URI, values); long eventID =
					 * Long.parseLong(uri.getLastPathSegment()); ContentResolver
					 * cd = getContentResolver(); ContentValues value = new
					 * ContentValues(); value.put(Reminders.MINUTES, 5);
					 * value.put(Reminders.EVENT_ID, eventID);
					 * value.put(Reminders.METHOD, Reminders.METHOD_ALERT); Uri
					 * uri1 = cd.insert(Reminders.CONTENT_URI, values);
					 */

				} else {
					try {
						Response<RestaurantOrder> response = resService
								.restaurantOrder(restID.getText().toString(),
										phone.getText().toString(), "精品水煮鱼",
										date.getText().toString(), time
												.getText().toString());
						String statusString = null;
						// if (response != null && response.getStatus() != null)
						// {
						statusString = response.getStatus().getMessage();
						// }
						Toast.makeText(getApplicationContext(), statusString,
								Toast.LENGTH_LONG).show();

					} catch (Exception e) {
						// TODO: handle exception
					}

				}
			}
		});

	}

	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		// 请求码
		switch (requestCode) {
		// other 这个请求 的处理
		case OTHER:
			// 根据请求返回值得结果码 再进行匹配
			switch (resultCode) {
			case RESULT_OK:
				Toast.makeText(this, "返回的数据" + data.getStringExtra("totalID"),
						Toast.LENGTH_LONG).show();
				break;

			default:
				break;
			}

			break;

		default:
			break;
		}
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		Dialog dialog = null;
		switch (id) {
		case DATE_DIALOG:
			c = Calendar.getInstance();
			dialog = new DatePickerDialog(this,
					new DatePickerDialog.OnDateSetListener() {
						public void onDateSet(DatePicker dp, int year,
								int month, int dayOfMonth) {
							date.setText(year + "-" + (month + 1) + "-"
									+ dayOfMonth);
						}
					}, c.get(Calendar.YEAR), // 传入年份
					c.get(Calendar.MONTH), // 传入月份
					c.get(Calendar.DAY_OF_MONTH) // 传入天数
			);
			break;
		case TIME_DIALOG:
			c = Calendar.getInstance();
			dialog = new TimePickerDialog(this,
					new TimePickerDialog.OnTimeSetListener() {
						public void onTimeSet(TimePicker view, int hourOfDay,
								int minute) {
							time.setText(hourOfDay + ":" + minute);
						}
					}, c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE),
					false);
			break;
		}
		return dialog;
	}/* */

	public void openOrderDish(View v) {
		Intent intent = new Intent();
		intent.setClass(RestaurantDetailActivity.this, OrderDishActivity.class);
		// intent.putExtra("name", restInfo.getName());
		intent.putExtra("restID", restID.getText().toString());
		//startActivity(intent);
		startActivityForResult(intent, OTHER);
		
	}
	
	/*public void goOtherActivity(View v) {

		// 返回数据的获取的操作
		Intent intent = new Intent(RestaurantDetailActivity.this,
				OrderDishActivity.class);
		intent.putExtra("totalID", "shasha");
		intent.putExtra("totalPrice", "xxxx");
		// 新打开的activity返回的数据
		startActivityForResult(intent, OTHER);
	}
*/

}
