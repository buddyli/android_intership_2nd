package com.bjtu.time2eat.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.bjtu.time2eat.pojo.Menu;
import com.bjtu.time2eat.pojo.Response;
import com.bjtu.time2eat.pojo.resbody.RestaurantMenu;
import com.bjtu.time2eat.service.RestaurantService;
import com.example.time2eat.R;

/**
 * 
 * @author LiuBao 订菜的动作
 * 
 */
@SuppressLint("HandlerLeak")
public class OrderDishActivity extends ListActivity {

	private String[] dishInfo;
	private String restID;
	private Button yesBtn;
	private ImageView yesimage;
	private ListView dishlist;
	private List<Boolean> mChecked;
	Context mContext;
	private List<String> totalID = new ArrayList<String>();
	private List<String> totalPrice = new ArrayList<String>();

	private List<Integer> selectedID = new ArrayList<Integer>();
	private ProgressDialog m_pDialog;
	private List<Map<String, Object>> list = null;
	private RestaurantService resService = new RestaurantService();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dishlist);

		mChecked = new ArrayList<Boolean>();
		initLoadingDialog();
		Intent intent = getIntent();
		restID = intent.getStringExtra("restID");
		yesBtn = (Button) findViewById(R.id.dishBtn);
		// dishlist=(ListView) findViewById(R.id.dishlist);
		mContext = getApplicationContext();
		new Thread(runnable).start();
		dishlist = getListView();
		// 点菜
		dishlist.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				if (mChecked.get(arg2) == false) {
					int total = 0;
					mChecked.set(arg2, true);
					yesimage = (ImageView) arg1.findViewById(R.id.yesimage);
					yesimage.setImageResource(R.drawable.yeschoose);
					// Toast.makeText(getApplicationContext(),
					// t1.getText().toString(), Toast.LENGTH_SHORT).show();
					for (int i = 0; i < mChecked.size(); i++) {
						if (mChecked.get(i)) {
							total = total + Integer.parseInt(totalPrice.get(i));
						}
					}
					yesBtn.setText("确定(总计" + total + "元)");
					arg1.setBackgroundColor(Color.parseColor("#FF9900"));
				} else {
					mChecked.set(arg2, false);
					yesimage = (ImageView) arg1.findViewById(R.id.yesimage);
					yesimage.setImageResource(R.drawable.nochoose);
					arg1.setBackgroundColor(Color.parseColor("#FFFFFF"));
					int total = 0;
					for (int i = 0; i < mChecked.size(); i++) {
						if (mChecked.get(i)) {
							total = total + Integer.parseInt(totalPrice.get(i));
						}
					}
					yesBtn.setText("确定(总计" + total + "元)");
				}/**/
			}
		});

		yesBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				selectedID.clear();
				for (int i = 0; i < mChecked.size(); i++) {
					if (mChecked.get(i)) {
						selectedID.add(i);// adapter.listPerson;
					}
				}

				if (selectedID.size() == 0) {
					/*AlertDialog.Builder builder1 = new AlertDialog.Builder(
							OrderDishActivity.this);
					builder1.setMessage("没有选中任何记录");
					builder1.show();*/
					Intent intent = new Intent();
					intent.putExtra("totalID", "");//所有菜品ID字符串
					intent.putExtra("totalPrice", "0");//所有菜品总价
					intent.putExtra("totalDishNum", "0");//菜品总个数
					intent.setClass(OrderDishActivity.this,
							RestaurantDetailActivity.class);					
					AlertDialog.Builder builder2 = new AlertDialog.Builder(
							OrderDishActivity.this);
					builder2.setMessage("2121212");
					builder2.show();
					OrderDishActivity.this.setResult(RESULT_OK, intent);
					OrderDishActivity.this.finish();
					
					Toast.makeText(OrderDishActivity.this, "您没有选择菜品", Toast.LENGTH_LONG).show();
					
				} else {

					StringBuilder sb = new StringBuilder();
					String string = new String();
					int totaldish = 0;
					for (int i = 0; i < selectedID.size(); i++) {
						totaldish++;
						sb.append("," + totalID.get(selectedID.get(i)));

					}
					//将菜品的所有ID加上“,”之后赋值给string
					string = sb.toString();
					
					String str = string.substring(1, string.length());// 菜品ID//去掉第一个“，”
					//
					String totalprice = new String();//
					totalprice = yesBtn.getText().toString();
					String str2 = totalprice.substring(5, yesBtn.getText()
							.toString().length() - 2);// 获得所有菜品总价
					Intent intent = new Intent();
					intent.putExtra("totalID", str);//所有菜品ID字符串
					intent.putExtra("totalPrice", str2);//所有菜品总价
					intent.putExtra("totalDishNum", String.valueOf(totaldish));//菜品总个数
					intent.setClass(OrderDishActivity.this,
							RestaurantDetailActivity.class);					
					AlertDialog.Builder builder2 = new AlertDialog.Builder(
							OrderDishActivity.this);
					builder2.setMessage(str2);
					builder2.show();
					OrderDishActivity.this.setResult(RESULT_OK, intent);
					OrderDishActivity.this.finish();
				}

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

			for (int i = 0; i < txt.length; i++) {
				dishInfo = txt[i].split("\\|");// 将获得的字符串
				Map<String, Object> map = new HashMap<String, Object>();
				map = new HashMap<String, Object>();
				map.put("id", dishInfo[0]);
				map.put("name", dishInfo[1]);
				map.put("price", dishInfo[2]);
				// map.put("image", dishInfo[3]);
				totalID.add(dishInfo[0]);
				totalPrice.add(dishInfo[2]);
				mChecked.add(false);
				//
				list.add(map);
			}/**/
			SimpleAdapter adapter = new SimpleAdapter(OrderDishActivity.this,
					list, R.layout.dishlistitem, new String[] { "id", "name",
							"price" }, new int[] { R.id.dishid, R.id.dishname,
							R.id.dishprice });
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
					dishs[i++] = dish.getId() + "|" + dish.getName() + "|"
							+ dish.getPrice();
				}
				bundle.putCharSequenceArray("dishs", dishs);
				msg.setData(bundle);
			}
			hander.sendMessage(msg);
		}
	};

}
