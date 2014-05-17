package com.bjtu.time2eat.activity;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.os.Parcelable;
import android.widget.Toast;

import com.bjtu.time2eat.pojo.Merchant;
import com.bjtu.time2eat.pojo.Response;
import com.bjtu.time2eat.pojo.resbody.RestaurantDetail;
import com.bjtu.time2eat.service.RestaurantService;
import com.example.time2eat.R;
import com.google.gson.reflect.TypeToken;

/**
 * NFC TAG的读写
 * 
 * TAG存储的text plain形式的json字符串。
 * 
 * @author licb
 * 
 */
public class NFCReaderActivity extends Activity {
	private ProgressDialog m_pDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_nfc);
		NfcAdapter nfcAdapter = NfcAdapter.getDefaultAdapter(this);
		if (nfcAdapter == null) {
			Toast.makeText(NFCReaderActivity.this, "不支持NFC功能...",
					Toast.LENGTH_SHORT).show();
		} else if (!nfcAdapter.isEnabled()) {
			Toast.makeText(NFCReaderActivity.this, "请打开系统设置中的NFC选项",
					Toast.LENGTH_SHORT).show();
		} else {
			 initLoadingDialog();
		}

	}

	@SuppressWarnings({ "deprecation" })
	private void initLoadingDialog() {
		// 创建ProgressDialog对象
		m_pDialog = new ProgressDialog(NFCReaderActivity.this);
		// 设置进度条风格，风格为圆形，旋转的
		m_pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		// 设置ProgressDialog 标题
		m_pDialog.setTitle("请将手机贴近NFC射频设备...");
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

	// 从NFC TAG中读取出的JSON字符串
	String result = null;

	@Override
	protected void onResume() {
		super.onResume();
		if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(getIntent().getAction())) {
			if (readFromTag(getIntent())) {
				if (result != null) {
					Type type = new TypeToken<Response<RestaurantDetail>>() {
					}.getType();
					Response<RestaurantDetail> resp = RestaurantService.gson
							.fromJson(result, type);
					if (resp != null) {
						// 隐藏进度条
						 m_pDialog.dismiss();

						Merchant mer = resp.getData().getDetail();
						Intent intent = new Intent(NFCReaderActivity.this,
								RestaurantDetailActivity.class);

						// 组装跳转到餐馆详情页面的参数
						intent.putExtra("id", mer.getId());
						intent.putExtra("name", "商户名称:" + mer.getName());
						intent.putExtra("address", "商户地址:" + mer.getAddress());
						intent.putExtra("telno", "联系电话:" + mer.getTelno());
						intent.putExtra("price", "平均消费:" + mer.getPrice());
						intent.putExtra("trade_name", mer.getTrade_name());
						intent.putExtra("star", mer.getM_star());
						intent.putExtra("distance", "");
						startActivity(intent);
					}
				}
			} else {
				Toast.makeText(NFCReaderActivity.this, "读取NFC TAG失败",
						Toast.LENGTH_SHORT).show();
			}
		} else {
			Toast.makeText(NFCReaderActivity.this, "NFC TAG还未初始化，请初始化后再读取...",
					Toast.LENGTH_SHORT).show();
		}
	}

	private boolean readFromTag(Intent intent) {
		Parcelable[] rawArray = intent
				.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
		NdefMessage mNdefMsg = (NdefMessage) rawArray[0];
		NdefRecord mNdefRecord = mNdefMsg.getRecords()[0];
		try {
			if (mNdefRecord != null) {
				result = new String(mNdefRecord.getPayload(), "UTF-8");
				if (result != null && result.indexOf("en") != -1) {
					result = result.substring(result.indexOf("en") + 2);
				}
				return true;
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

}
