package com.bjtu.time2eat.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.AsyncQueryHandler;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

import com.bjtu.time2eat.pojo.Contact;
import com.bjtu.time2eat.view.QuickAlphabeticBar;
import com.bjtu.time2eat.view.ContactListAdapter;
import com.example.time2eat.R;
import com.bjtu.time2eat.view.ContactListAdapter.ViewHolder;

/**
 * 邀请好友
 * 
 * @author Li
 * 
 */
public class WelcomeFriendActivity extends Activity{
	private ContactListAdapter adapter;
	private ListView contactList;
	private List<Contact> list;
	private AsyncQueryHandler asyncQueryHandler; // 异步查询数据库类对象
	private QuickAlphabeticBar alphabeticBar; // 快速索引条

	private Map<Integer, Contact> contactIdMap = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.contact_list_view);
		contactList = (ListView) findViewById(R.id.contact_list);
		Button btn_add = (Button) findViewById(R.id.btn_add);
        Button btn_back = (Button) findViewById(R.id.btn_back);

		contactList.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				// 取得ViewHolder对象，这样就省去了通过层层的findViewById去实例化我们需要的cb实例的步骤
				ViewHolder holder = (ViewHolder) view.getTag();
				// 改变CheckBox的状态
                holder.cb.toggle();
             // 将CheckBox的选中状况记录下来
                if (holder.cb.isChecked() == true) {
                    list.get(position).isChecked = true;                    
                }
                else {
                    list.get(position).isChecked = false;                 
                }
			}
        	
        });

		btn_add.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				ArrayList<String> phoneNum =new ArrayList<String>();					
				for(int i=0;i<list.size();i++){
					if(list.get(i).isChecked==true){
						phoneNum.add(list.get(i).getPhoneNum());
					}
				}

				StringBuffer numberEvery = new StringBuffer();
				for(int j=0;j<phoneNum.size();j++){
					numberEvery = numberEvery.append(phoneNum.get(j)+",");
				}
				Uri uri = Uri.parse("smsto:"+numberEvery.toString());  
				Intent it = new Intent(Intent.ACTION_SENDTO, uri);  
				startActivity(it);
			}

		});

		btn_back.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				finish();
			}

		});

		alphabeticBar = (QuickAlphabeticBar) findViewById(R.id.fast_scroller);

		// 实例化
		asyncQueryHandler = new MyAsyncQueryHandler(getContentResolver());
		init();

	}

	/**
	 * 初始化数据库查询参数
	 */
	private void init() {
		Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI; // 联系人Uri；
		// 查询的字段
		String[] projection = { ContactsContract.CommonDataKinds.Phone._ID,
				ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
				ContactsContract.CommonDataKinds.Phone.DATA1, "sort_key",
				ContactsContract.CommonDataKinds.Phone.CONTACT_ID,
				ContactsContract.CommonDataKinds.Phone.PHOTO_ID,
				ContactsContract.CommonDataKinds.Phone.LOOKUP_KEY };
		// 按照sort_key升序查詢
		asyncQueryHandler.startQuery(0, null, uri, projection, null, null,
				"sort_key COLLATE LOCALIZED asc");

	}

	private class MyAsyncQueryHandler extends AsyncQueryHandler {

		public MyAsyncQueryHandler(ContentResolver cr) {
			super(cr);
		}

		@Override
		protected void onQueryComplete(int token, Object cookie, Cursor cursor) {
			if (cursor != null && cursor.getCount() > 0) {
				contactIdMap = new HashMap<Integer, Contact>();
				list = new ArrayList<Contact>();
				cursor.moveToFirst(); // 游标移动到第一项
				for (int i = 0; i < cursor.getCount(); i++) {
					cursor.moveToPosition(i);
					String name = cursor.getString(1);
					String number = cursor.getString(2);
					String sortKey = cursor.getString(3);
					int contactId = cursor.getInt(4);
					Long photoId = cursor.getLong(5);
					String lookUpKey = cursor.getString(6);

					if (contactIdMap.containsKey(contactId)) {
						// 无操作
					} else {
						// 创建联系人对象
						Contact contact = new Contact();
						contact.setDesplayName(name);
						contact.setPhoneNum(number);
						contact.setSortKey(sortKey);
						contact.setPhotoId(photoId);
						contact.setLookUpKey(lookUpKey);
						contact.setIsChecked(false);
						list.add(contact);

						contactIdMap.put(contactId, contact);
					}
				}
				if (list.size() > 0) {
					setAdapter(list);
				}
			}

			super.onQueryComplete(token, cookie, cursor);
		}

	}

	private void setAdapter(List<Contact> list) {
		adapter = new ContactListAdapter(this, list, alphabeticBar);
		contactList.setAdapter(adapter);
		alphabeticBar.init(WelcomeFriendActivity.this);
		alphabeticBar.setListView(contactList);
		alphabeticBar.setHight(alphabeticBar.getHeight());
		alphabeticBar.setVisibility(View.VISIBLE);
	}
}