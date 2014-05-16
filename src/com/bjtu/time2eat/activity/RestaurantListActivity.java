package com.bjtu.time2eat.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

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
import android.widget.SimpleAdapter;

import com.bjtu.time2eat.pojo.Merchant;
import com.bjtu.time2eat.pojo.Response;
import com.bjtu.time2eat.pojo.resbody.RestaurantList;
import com.bjtu.time2eat.service.RestaurantService;
import com.example.time2eat.R;

/**
 * 
 * @author LiuBao 商户列表
 * 
 */
@SuppressLint("HandlerLeak")
public class RestaurantListActivity extends ListActivity {

	private String[] restInfo;
	private ProgressDialog m_pDialog;
	private List<Map<String, Object>> list = null;
	private RestaurantService resService = new RestaurantService();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initLoadingDialog();
		this.getListView().setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> adapterView, View view,
					int position, long id) {
				Intent intent = new Intent();
				intent.setClass(RestaurantListActivity.this,
						RestaurantDetailActivity.class);
				
				intent.putExtra("id", list.get(position).get("id").toString());
				intent.putExtra("name", list.get(position).get("name")
						.toString());
				intent.putExtra("address", list.get(position).get("address")
						.toString());
				intent.putExtra("price", list.get(position).get("price")
						.toString());
				intent.putExtra("telno", list.get(position).get("telno")
						.toString());
				intent.putExtra("distance", list.get(position).get("distance")
						.toString());
				intent.putExtra("star", list.get(position).get("star")
						.toString());
				intent.putExtra("trade_name", list.get(position).get("trade_name")
						.toString());
				startActivity(intent);
				// Toast.makeText(getApplicationContext(), "211212",
				// Toast.LENGTH_SHORT).show();

			}
		});
		new Thread(runnable).start();
	}

	@SuppressWarnings("deprecation")
	private void initLoadingDialog() {
		// 创建ProgressDialog对象
		m_pDialog = new ProgressDialog(RestaurantListActivity.this);
		// 设置进度条风格，风格为圆形，旋转的
		m_pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		// 设置ProgressDialog 标题
		m_pDialog.setTitle("玩命加载附近餐厅...");
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
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (m_pDialog != null) {
				m_pDialog.hide();
			}
			Bundle bundle = msg.getData();
			String[] txt = (String[]) bundle.get("merchants");
			list = new ArrayList<Map<String, Object>>();

			for (int i = 0; i < txt.length; i++) {
				restInfo = txt[i].split("\\|");// 将获得的字符串
				Map<String, Object> map = new HashMap<String, Object>();
				map = new HashMap<String, Object>();
				map.put("id", restInfo[0]);
				map.put("name", restInfo[1]);
				map.put("address", restInfo[2]);
				map.put("telno", restInfo[3]);
				map.put("price", restInfo[4]);
				map.put("distance", restInfo[5]);
				map.put("star", restInfo[6]);
				map.put("trade_name", restInfo[7]);/**/

				//
				list.add(map);
			}/**/
			// for (int i = 0; i < txt.length; i++) {
			// restInfo=txt[i].split("*");

			SimpleAdapter adapter = new SimpleAdapter(
					RestaurantListActivity.this, list, R.layout.restlistitem,
					new String[] { "name", "address", "telno", "price" },
					new int[] { R.id.restaurantname, R.id.restaurantaddress,
							R.id.restauranttelno, R.id.restaurantprice });
			setListAdapter(adapter);
		}
	};

	Runnable runnable = new Runnable() {

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
					// merchants[i++] = merchant.getId()+"|"+
					// "商户名称："+merchant.getName()+"|"+
					// "商户地址："+merchant.getAddress()+"|"+
					// "联系电话："+merchant.getTelno()+"|"+
					// "人均消费："+merchant.getPrice();
					StringBuilder item = new StringBuilder();
					item.append(merchant.getId()).append("|");
					item.append(merchant.getName()).append("|");
					if (StringUtils.isNotBlank(merchant.getAddress())) {
						item.append(merchant.getAddress()).append("|");
					} else {
						item.append("--").append("|");
					}

					if (StringUtils.isNotBlank(merchant.getTelno())) {
						item.append(merchant.getTelno()).append("|");
					} else {
						item.append("--").append("|");
					}

					if (StringUtils.isNotBlank(merchant.getPrice())) {
						item.append(merchant.getPrice()).append("|");
					} else {
						item.append("--").append("|");
						;
					}

					if (StringUtils.isNotBlank(merchant.getDistance())) {
						item.append(merchant.getDistance()).append("|");
					} else {
						item.append("--").append("|");
						;
					}
					if (StringUtils.isNotBlank(merchant.getM_star())) {
						item.append(merchant.getM_star()).append("|");
					} else {
						item.append("--").append("|");
						;
					}
					if (StringUtils.isNotBlank(merchant.getTrade_name())) {
						item.append(merchant.getTrade_name());
					} else {
						item.append("--");
					}

					merchants[i++] = item.toString();
				}
				bundle.putCharSequenceArray("merchants", merchants);
				msg.setData(bundle);
			}
			hander.sendMessage(msg);
		}
	};

}
