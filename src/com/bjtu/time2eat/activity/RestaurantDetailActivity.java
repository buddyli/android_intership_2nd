package com.bjtu.time2eat.activity;

import java.util.Calendar;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.CalendarContract.Events;
import android.provider.CalendarContract.Reminders;
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

/**
 * 
 * @author LiuBao 商户详情
 * 
 */
@SuppressLint("HandlerLeak")
public class RestaurantDetailActivity extends Activity {
	private Calendar c;
	private EditText date; // 就餐日期
	private EditText time;// 就餐时间
	private EditText phone;// 用户手机
	private EditText peoplenum;// 就餐人数
	private String restID;// 餐馆ID
	private TextView totalID;// 订菜的总ID
	private TextView totalPrice;// 订菜的总价
	private Button yesorderButton;
	private Button orderDishButton;
	private static final int OTHER = 1;
	private final static int DATE_DIALOG = 0;
	private final static int TIME_DIALOG = 1;
	private RestaurantService resService = new RestaurantService();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.restdetail);

		yesorderButton = (Button) findViewById(R.id.yesOrderBtn);
		orderDishButton = (Button) findViewById(R.id.orderDishBtn);
		// restID = (TextView) findViewById(R.id.showRestID);
		totalID = (TextView) findViewById(R.id.totalID);
		totalPrice = (TextView) findViewById(R.id.totalPrice);
		date = (EditText) findViewById(R.id.date);
		time = (EditText) findViewById(R.id.time);
		phone = (EditText) findViewById(R.id.phone);
		peoplenum = (EditText) findViewById(R.id.peoplenum);

		date.setFocusable(false);
		time.setFocusable(false);
		// phone.setFocusable(false);
		// 从商户列表获得商户的信息
		Intent intent = getIntent();
		restID = intent.getStringExtra("id");
		TextView restName = (TextView) findViewById(R.id.showRestName);
		restName.setText(intent.getStringExtra("name"));
		TextView restAddress = (TextView) findViewById(R.id.showRestAddress);
		restAddress.setText(intent.getStringExtra("address"));
		TextView restTelno = (TextView) findViewById(R.id.showRestTelno);
		restTelno.setText(intent.getStringExtra("telno"));
		TextView restPrice = (TextView) findViewById(R.id.showRestPrice);
		restPrice.setText(intent.getStringExtra("price" + "元"));
		TextView restStar = (TextView) findViewById(R.id.star);
		restStar.setText(intent.getStringExtra("star"));
		TextView restDistance = (TextView) findViewById(R.id.showRestDis);
		restDistance.setText(intent.getStringExtra("distance"+"米"));
		TextView restTradeName = (TextView) findViewById(R.id.trade_name);
		restTradeName.setText(intent.getStringExtra("trade_name"));
		//intent.getExtras();

		date.setOnClickListener(new View.OnClickListener() {// 日期对话框
			@SuppressWarnings("deprecation")
			public void onClick(View v) {
				// date.setInputType(InputType.TYPE_NULL);
				showDialog(DATE_DIALOG);
			}
		});/**/

		time.setOnClickListener(new View.OnClickListener() {// 时间对话框
			@SuppressWarnings("deprecation")
			public void onClick(View v) {
				showDialog(TIME_DIALOG);
			}
		});
		phone.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				phone.setText("");
			}
		});
		peoplenum.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				peoplenum.setText("");
			}
		});

		yesorderButton.setOnClickListener(new OnClickListener() {
			// 强迫用户填入信息
			public void onClick(View v) {
				if (date.getText().toString().endsWith("请选择订餐日期")
						|| time.getText().toString().endsWith("请选择订餐时间")
						|| phone.getText().toString().equals("")
						|| phone.getText().toString().equals("请输入手机号码")
						|| peoplenum.getText().toString().equals("请输入就餐人数")
						|| peoplenum.getText().toString().equals("")) {

					Toast.makeText(getApplicationContext(), "请输入必要的订餐信息",
							Toast.LENGTH_SHORT).show();

				} else {
					try {
						// TODO 用餐人数，这里需要根据用户的输入修改
						long calID = 3;
						long startMillis = 0;
						long endMillis = 0;
						
						Toast.makeText(getApplicationContext(), date.getText().toString(),
								Toast.LENGTH_SHORT).show();
						
						
						String s=new String();
						s=date.getText().toString();
						
						String[] dateArray=s.split("-");
						
						String t=new String();
						t=time.getText().toString();
						String[] time=t.split(":");
						
						String information=new String("您在"+"定了餐！ 时间为："+s+"  "+t+" 请前去就餐！");
				        
						
						Calendar beginTime= Calendar.getInstance();
						beginTime.set(c.get(Calendar.YEAR),c.get(Calendar.MONTH),c.get(Calendar.DAY_OF_MONTH), c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE));
						startMillis=beginTime.getTimeInMillis();
						Calendar endTime = Calendar.getInstance();
						endTime.set(Integer.parseInt(dateArray[0]), Integer.parseInt(dateArray[1])-1,Integer.parseInt(dateArray[2]),Integer.parseInt(time[0]) , Integer.parseInt(time[1]));
						endMillis=endTime.getTimeInMillis();
						
						ContentResolver cr = getContentResolver();
						ContentValues values = new ContentValues();
						values.put(Events.DTSTART, startMillis);
						values.put(Events.DTEND, endMillis);
						values.put(Events.TITLE, "订餐69");
						values.put(Events.DESCRIPTION, information);
						values.put(Events.CALENDAR_ID, calID);
						values.put(Events.EVENT_TIMEZONE, "China/Bei_Jing");
						values.put(Events.HAS_ALARM, 1);
						Uri uri = cr.insert(Events.CONTENT_URI, values);
						
						long eventID = Long.parseLong(uri.getLastPathSegment());
						
						ContentValues otherValues= new ContentValues();
						otherValues.put(Events._ID, eventID);
						otherValues.put(Reminders.EVENT_ID, eventID);
						otherValues.put(Reminders.MINUTES,10);
						otherValues.put(Reminders.METHOD, Reminders.METHOD_ALERT);
						
						cr.insert(Events.CONTENT_URI, otherValues);
						Toast.makeText(RestaurantDetailActivity.this,"插入日历提醒事件成功!",Toast.LENGTH_LONG).show();  
						
						
						new Thread(runnable).start();
					} catch (Exception e) {

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
				totalID.setText(data.getStringExtra("totalID"));
				totalPrice.setText(data.getStringExtra("totalPrice"));

				orderDishButton.setText("已点"
						+ data.getStringExtra("totalDishNum") + "道菜共"
						+ data.getStringExtra("totalPrice") + "元");

				// Toast.makeText(this, data.getStringExtra("totalID"),
				// Toast.LENGTH_LONG).show();
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
		intent.putExtra("restID", restID);
		startActivityForResult(intent, OTHER);
	}

	@SuppressLint("HandlerLeak")
	Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			int status = msg.getData().getInt("status");
			if (status == 0) {
				Toast.makeText(RestaurantDetailActivity.this, "预订成功！",
						Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(RestaurantDetailActivity.this,
						"网络繁忙，预订失败，请重新预订！", Toast.LENGTH_SHORT).show();
			}

			// TODO，这里可以跳转到订单历史记录页面。
		}

	};
	Runnable runnable = new Runnable() {

		@Override
		public void run() {

			int peoplen = Integer.parseInt(peoplenum.getText().toString());

			if (peoplen > 200) {
				peoplen = 200;
			}

			Response<RestaurantOrder> response = resService.restaurantOrder(
					restID, phone.getText().toString(), totalID.getText()
							.toString(), date.getText().toString(), time
							.getText().toString(), peoplen);
			// String statusString = null;
			// statusString = response.getStatus().getMessage();
			Message msg = new Message();
			Bundle bundle = new Bundle();
			if (response == null) {
				bundle.putInt("status", 1);
			} else {
				bundle.putInt("status", response.getStatus().getCode());
			}
			msg.setData(bundle);
			handler.sendMessage(msg);
		}
	};
}
