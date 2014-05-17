package com.bjtu.time2eat.activity;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Locale;

import android.app.Activity;
import android.nfc.FormatException;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.nfc.tech.NdefFormatable;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.example.time2eat.R;

/**
 * 用来写入NFC标签的Activity
 * 
 * TAG存储的text plain形式的json字符串。
 * 
 * @author licb
 * 
 */
public class NFCWriterActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_nfc);
		NfcAdapter nfcAdapter = NfcAdapter.getDefaultAdapter(this);
		if (nfcAdapter == null) {
			Toast.makeText(NFCWriterActivity.this, "不支持NFC功能...",
					Toast.LENGTH_SHORT).show();
		} else if (!nfcAdapter.isEnabled()) {
			Toast.makeText(NFCWriterActivity.this, "请打开系统设置中的NFC选项",
					Toast.LENGTH_SHORT).show();
		} else {
			TextView view = (TextView) findViewById(R.id.nfcView);
			Toast.makeText(NFCWriterActivity.this, "检测到未初始化的NFC标签...",
					Toast.LENGTH_SHORT).show();
			view.setText("NFC....");
		}

	}

	String json = "{\"status\": {\"code\": \"0\",\"message\": \"SUCCESS\"},\"page\": {\"ps\": \"10\",\"pn\": \"1\",\"pc\": \"20\",\"rc\": \"200\"},\"data\": {\"detail\":{\"price\": \"\", \"address\": \"北京市海淀区交大东路36号楼品阁小区底商105楼2楼\", \"lat\": \"39.952170\", \"telno\": \"010-62250533\", \"id\": \"5365b8c55ce41b27b897e008\", \"distance\": \"\", \"name\": \"小熊之家\", \"lon\": \"116.34848\", \"trade_name\": \"粤菜\", \"landmark\": \"\", \"m_star\": \"3\"}}}";

	@Override
	protected void onResume() {
		super.onResume();
		Toast.makeText(NFCWriterActivity.this, getIntent().getAction(),
				Toast.LENGTH_SHORT).show();
		if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(getIntent().getAction())
				|| NfcAdapter.ACTION_NDEF_DISCOVERED.equals(getIntent()
						.getAction())) {
			Tag tag = getIntent().getParcelableExtra(NfcAdapter.EXTRA_TAG);
			// 标签格式化过和没有被格式化过的时候，写入的格式是不一样的。
			Ndef ndef = Ndef.get(tag);
			NdefFormatable ndefFor = NdefFormatable.get(tag);
			try {
				NdefRecord ndefRecord = createTextRecord(json, Locale.US, true);
				NdefRecord[] records = { ndefRecord };
				NdefMessage ndefMessage = new NdefMessage(records);
				if (ndef != null) {// 标签被格式化过。。。
					ndef.connect();
					ndef.writeNdefMessage(ndefMessage);
				} else if (ndefFor != null) {// 标签没有被格式化过。。。
					ndefFor.connect();
					ndefFor.format(ndefMessage);
				}

			} catch (IOException e1) {
				e1.printStackTrace();
			} catch (FormatException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 官网拷贝的写入NFC TAG格式化数据的方法。
	 * 
	 * @param payload
	 * @param locale
	 * @param encodeInUtf8
	 * @return
	 */
	public NdefRecord createTextRecord(String payload, Locale locale,
			boolean encodeInUtf8) {
		byte[] langBytes = locale.getLanguage().getBytes(
				Charset.forName("US-ASCII"));
		Charset utfEncoding = encodeInUtf8 ? Charset.forName("UTF-8") : Charset
				.forName("UTF-16");
		byte[] textBytes = payload.getBytes(utfEncoding);
		int utfBit = encodeInUtf8 ? 0 : (1 << 7);
		char status = (char) (utfBit + langBytes.length);
		byte[] data = new byte[1 + langBytes.length + textBytes.length];
		data[0] = (byte) status;
		System.arraycopy(langBytes, 0, data, 1, langBytes.length);
		System.arraycopy(textBytes, 0, data, 1 + langBytes.length,
				textBytes.length);
		NdefRecord record = new NdefRecord(NdefRecord.TNF_WELL_KNOWN,
				NdefRecord.RTD_TEXT, new byte[0], data);
		return record;
	}

}
