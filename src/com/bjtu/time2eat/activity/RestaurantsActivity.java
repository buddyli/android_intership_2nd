package com.bjtu.time2eat.activity;

import com.bjtu.time2eat.pojo.Merchant;
import com.bjtu.time2eat.pojo.Response;
import com.bjtu.time2eat.pojo.resbody.RestaurantList;
import com.bjtu.time2eat.service.RestaurantService;

import android.annotation.SuppressLint;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

/**
 * 餐馆查找、菜单获取等操作Activity
 * 
 * @author licb
 * 
 */
public class RestaurantsActivity extends ListActivity {
	private RestaurantService resService = new RestaurantService();
	ProgressDialog m_pDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initLoadingDialog();
		// 加载附件商户列表
		new Thread(runnable).start();
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
	}

	/**
	 * 创建和一个ProgressDialog对象
	 */
	@SuppressWarnings("deprecation")
	private void initLoadingDialog() {
		// 创建ProgressDialog对象
		m_pDialog = new ProgressDialog(RestaurantsActivity.this);
		// 设置进度条风格，风格为圆形，旋转的
		m_pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		// 设置ProgressDialog 标题
		m_pDialog.setTitle("玩命加载历史记录...");

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

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);

			if (m_pDialog != null) {
				m_pDialog.hide();
			}
			Bundle bundle = msg.getData();
			String[] txt = (String[]) bundle.get("merchants");
			setListAdapter(new ArrayAdapter<String>(RestaurantsActivity.this,
					android.R.layout.simple_list_item_1, txt));
			getListView().setTextFilterEnabled(true);
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
					merchants[i++] = merchant.getName();
				}
				bundle.putCharSequenceArray("merchants", merchants);
				msg.setData(bundle);
			}
			hander.sendMessage(msg);
		}
	};

}
