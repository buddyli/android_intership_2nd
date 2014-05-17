package com.bjtu.time2eat.activity;

import java.util.Calendar;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.CalendarContract.Events;
import android.provider.CalendarContract.Reminders;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
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
	private String totalID;// 订菜的总ID
	private String lat;// 餐馆纬度
	private String lon;// 餐馆经度
	private TextView totalName;// 订菜的总菜名
	private TextView totalPrice;// 订菜的总价
	private TextView restName;
	// private TextView restAddress;
	private Button yesorderButton;
	private Button orderDishButton;
	private Button inviteFriends;
	private Button restTelno;
	private Button restAddress;
	private CheckBox rem_pw;
	private SharedPreferences sp;
	private static final int OTHER = 1;
	private final static int DATE_DIALOG = 0;
	private final static int TIME_DIALOG = 1;
	private RestaurantService resService = new RestaurantService();

	@SuppressLint("WorldReadableFiles")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.restdetail);

		yesorderButton = (Button) findViewById(R.id.yesOrderBtn);
		orderDishButton = (Button) findViewById(R.id.orderDishBtn);
		inviteFriends = (Button) findViewById(R.id.inviteFriends);
		// restID = (TextView) findViewById(R.id.showRestID);
		totalName = (TextView) findViewById(R.id.totalName);
		totalPrice = (TextView) findViewById(R.id.totalPrice);
		date = (EditText) findViewById(R.id.date);
		time = (EditText) findViewById(R.id.time);
		phone = (EditText) findViewById(R.id.phone);
		peoplenum = (EditText) findViewById(R.id.peoplenum);
		rem_pw = (CheckBox) findViewById(R.id.rem_pw);

		date.setFocusable(false);
		time.setFocusable(false);
		// phone.setFocusable(false);
		// 从商户列表获得商户的信息
		sp = this.getSharedPreferences("userInfo", Context.MODE_WORLD_READABLE);
		Intent intent = getIntent();
		restID = intent.getStringExtra("id");
		restName = (TextView) findViewById(R.id.showRestName);
		restName.setText(intent.getStringExtra("name"));
		restAddress = (Button) findViewById(R.id.showRestAddress);
		restAddress.setText(intent.getStringExtra("address"));
		restTelno = (Button) findViewById(R.id.showRestTelno);
		restTelno.setText(intent.getStringExtra("telno"));
		TextView restPrice = (TextView) findViewById(R.id.showRestPrice);
		restPrice.setText(intent.getStringExtra("price" + "元"));
		TextView restStar = (TextView) findViewById(R.id.star);
		restStar.setText(intent.getStringExtra("star"));
		TextView restDistance = (TextView) findViewById(R.id.showRestDis);
		restDistance.setText(intent.getStringExtra("distance" + "米"));
		TextView restTradeName = (TextView) findViewById(R.id.trade_name);
		restTradeName.setText(intent.getStringExtra("trade_name"));
		lat = intent.getStringExtra("lat");
		lon = intent.getStringExtra("lon");
		// intent.getExtras();
		Time2EatApplication app = (Time2EatApplication) this.getApplication();

		restAddress.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.putExtra("restlat", lat);
				intent.putExtra("restlon", lon);
				intent.setClass(RestaurantDetailActivity.this,
						WalkToRestaurantActivity.class);
				startActivity(intent);
			}
		});

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
		phone.setOnFocusChangeListener(new View.OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				// TODO Auto-generated method stub
				if (!hasFocus) {
					rem_pw.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							if (rem_pw.isChecked()) {
								// 记住用户名、密码、
								Editor editor = sp.edit();
								editor.putString("phone", phone.getText()
										.toString());
								editor.commit();
								Toast.makeText(getApplicationContext(),
										"已经记住手机号码", Toast.LENGTH_LONG).show();
								sp.edit().putBoolean("ISCHECK", true).commit();

							} else {
								Toast.makeText(getApplicationContext(),
										"号码不能被记住", Toast.LENGTH_LONG).show();
								sp.edit().putBoolean("ISCHECK", false).commit();

							}

						}
					});

					if (rem_pw.isChecked()) {
						// 记住用户名、密码、
						Editor editor = sp.edit();
						editor.putString("phone", phone.getText().toString());
						editor.commit();
						Toast.makeText(getApplicationContext(), "已经记住手机号码",
								Toast.LENGTH_LONG).show();
						sp.edit().putBoolean("ISCHECK", true).commit();

					} else {
						Toast.makeText(getApplicationContext(), "号码不能被记住",
								Toast.LENGTH_LONG).show();
						sp.edit().putBoolean("ISCHECK", false).commit();

					}

				}

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
		if (sp.getBoolean("ISCHECK", false)) {
			// 设置默认是记录密码状态
			rem_pw.setChecked(true);
			phone.setText(sp.getString("phone", ""));

		}
		restTelno.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				Intent intent = new Intent();
				intent.setAction("android.intent.action.DIAL");
				intent.setData(Uri.parse("tel:"
						+ restTelno.getText().toString()));
				startActivity(intent);

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

					Toast.makeText(getApplicationContext(), "请输入订餐信息",
							Toast.LENGTH_SHORT).show();

				} else {
					try {
						new Thread(runnable).start();
						// TODO 用餐人数，这里需要根据用户的输入修改
						// new Thread(runnable).start();
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

				totalID = data.getStringExtra("totalID");
				totalName.setText("已点菜品：" + data.getStringExtra("totalName"));
				totalPrice.setText("共计：" + data.getStringExtra("totalPrice")
						+ "元");
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

				if(SettingActivity.option==null)
				{
					SettingActivity.option=false;
				}
				
				if(SettingActivity.option==true){
				  Toast.makeText(RestaurantDetailActivity.this,"a为true!",Toast.LENGTH_LONG).show(); 
				  
				Builder messageBox=new  AlertDialog.Builder(RestaurantDetailActivity.this);
				messageBox.setTitle("设置日历提醒");
				messageBox.setMessage("是否创建日历事件提醒？");
				messageBox.setPositiveButton("是", new DialogInterface.OnClickListener() {
					
					 public void onClick(DialogInterface dialog, int which) {  
			                //确定按钮事件  
						 
						 	long calID = 3;
							long startMillis = 0;
							long endMillis = 0;
							
							Intent intent=getIntent();						
							String s=new String();
							s=date.getText().toString();						
							String[] dateArray=s.split("-");
							String t=new String();
							t=time.getText().toString();
							String[] time=t.split(":");							        
							
							Calendar beginTime= Calendar.getInstance();
							beginTime.set(c.get(Calendar.YEAR),c.get(Calendar.MONTH),c.get(Calendar.DAY_OF_MONTH), c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE));
							startMillis=beginTime.getTimeInMillis();
							Calendar endTime = Calendar.getInstance();
							endTime.set(Integer.parseInt(dateArray[0]), Integer.parseInt(dateArray[1])-1,Integer.parseInt(dateArray[2]),Integer.parseInt(time[0]) , Integer.parseInt(time[1]));
							endMillis=endTime.getTimeInMillis();
							
							ContentResolver cr = getContentResolver();
							ContentValues values = new ContentValues();
							values.put(Events.DTSTART, startMillis);		//设置事件的初始时间
							values.put(Events.DTEND, endMillis);			//设置事件的终止时间
							values.put(Events.TITLE, intent.getStringExtra("name")+" 餐厅的订餐通知！");			//设置事件标题
							values.put(Events.DESCRIPTION, "您在"+ intent.getStringExtra("name") +"定了餐！ 时间为："+s+"  "+t+" 请准时前去就餐哦！亲！");		//设置事件内容
							values.put(Events.CALENDAR_ID, calID);
							values.put(Events.EVENT_TIMEZONE, "China/Bei_Jing");		//设置时区
							values.put(Events.HAS_ALARM, 1);		//设置提醒开关
							Uri uri = cr.insert(Events.CONTENT_URI, values); 		//插入系统日历事件
							
							long eventID = Long.parseLong(uri.getLastPathSegment());
							
							ContentValues otherValues= new ContentValues();
							otherValues.put(Events._ID, eventID);
							otherValues.put(Reminders.EVENT_ID, eventID);
							otherValues.put(Reminders.MINUTES,10);//设置提前10分钟提醒
							otherValues.put(Reminders.METHOD, Reminders.METHOD_ALERT);
							
							cr.insert(Events.CONTENT_URI, otherValues);  //插入提醒
							Toast.makeText(RestaurantDetailActivity.this,"插入日历提醒事件成功!",Toast.LENGTH_LONG).show(); 
							new Thread(runnable).start();
			                setResult(RESULT_OK);  
			               // finish();  
			            }  
				});
				messageBox.setNegativeButton("否", new DialogInterface.OnClickListener() {  
		              
		            public void onClick(DialogInterface dialog, int which) {  
		                //取消按钮事件  
		            	
		            	Intent i1=getIntent();
		            	String s=new String();
						s=date.getText().toString();						
						
						String t=new String();
						t=time.getText().toString();
		            	
		            	Uri smsToUri = Uri.parse("smsto:");

		            	Intent intent = new Intent(Intent.ACTION_SENDTO, smsToUri);

		            	intent.putExtra("sms_body", "我在"+i1.getStringExtra("name")+" 定了餐！  时间是："+ s +"  "+ t +" 请你去吃饭哦！亲！敞开了吃！" );

		            	startActivity(intent);
		            	
		            	
		            	new Thread(runnable).start();
		            }  
		        });
				messageBox.show();
				
				
				 
			   }
			   else if(SettingActivity.option==false)
			   {
				   Toast.makeText(RestaurantDetailActivity.this,"a为false!",Toast.LENGTH_LONG).show(); 
				   new Thread(runnable).start();
			   }
				// 邀请好友
				inviteFriends.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						String s = new String();
						s = date.getText().toString();
						String t = new String();
						t = time.getText().toString();
						Uri smsToUri = Uri.parse("smsto:");
						Intent intent = new Intent(Intent.ACTION_SENDTO,
								smsToUri);
						Toast.makeText(RestaurantDetailActivity.this,
								"您现在可以邀请好友!", Toast.LENGTH_LONG).show();
						setResult(RESULT_OK);
						intent.putExtra("sms_body", "我在"
								+ restName.getText().toString() + "（"
								+ restAddress.getText().toString() + "）"
								+ " 定了餐！  时间是：" + s + "  " + t
								+ " 请你去吃饭哦！亲！敞开了吃！");
						startActivity(intent);
					}
				});

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
					restID, phone.getText().toString(), totalID, date.getText()
							.toString(), time.getText().toString(), peoplen);
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
