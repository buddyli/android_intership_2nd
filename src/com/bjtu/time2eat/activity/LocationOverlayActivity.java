package com.bjtu.time2eat.activity;

import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.map.ItemizedOverlay;
import com.baidu.mapapi.map.LocationData;
import com.baidu.mapapi.map.MapController;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationOverlay;
import com.baidu.mapapi.map.MyLocationOverlay.LocationMode;
import com.baidu.mapapi.map.OverlayItem;
import com.baidu.mapapi.map.PopupClickListener;
import com.baidu.mapapi.map.PopupOverlay;
import com.baidu.platform.comapi.basestruct.GeoPoint;
import com.bjtu.time2eat.pojo.Merchant;
import com.bjtu.time2eat.pojo.Response;
import com.bjtu.time2eat.pojo.resbody.RestaurantList;
import com.bjtu.time2eat.service.RestaurantService;
import com.bjtu.time2eat.util.BMapUtil;
import com.example.time2eat.R;

/**
 * 姝emo鐢ㄦ潵灞曠ず濡備綍缁撳悎瀹氫綅SDK瀹炵幇瀹氫綅锛屽苟浣跨敤MyLocationOverlay缁樺埗瀹氫綅浣嶇疆
 * 鍚屾椂灞曠ず濡備綍浣跨敤鑷畾涔夊浘鏍囩粯鍒跺苟鐐瑰嚮鏃跺脊鍑烘场娉�
 * 
 */
@SuppressLint("ShowToast")
public class LocationOverlayActivity extends Activity {
	private RestaurantService resService = new RestaurantService();

	private enum E_BUTTON_TYPE {
		LOC, COMPASS, FOLLOW
	}

	private E_BUTTON_TYPE mCurBtnType;

	// 瀹氫綅鐩稿叧
	LocationClient mLocClient;
	LocationData locData = null;
	public MyLocationListenner myListener = new MyLocationListenner();

	// 瀹氫綅鍥惧眰
	locationOverlay myLocationOverlay = null;
	// 寮瑰嚭娉℃场鍥惧眰
	private PopupOverlay pop = null;// 寮瑰嚭娉℃场鍥惧眰锛屾祻瑙堣妭鐐规椂浣跨敤
	private TextView popupText = null;// 娉℃场view
	private View viewCache = null;

	// 鍦板浘鐩稿叧锛屼娇鐢ㄧ户鎵縈apView鐨凪yLocationMapView鐩殑鏄噸鍐檛ouch浜嬩欢瀹炵幇娉℃场澶勭悊
	// 濡傛灉涓嶅鐞唗ouch浜嬩欢锛屽垯鏃犻渶缁ф壙锛岀洿鎺ヤ娇鐢∕apView鍗冲彲
	MyLocationMapView mMapView = null; // 鍦板浘View
	private MapController mMapController = null;

	// UI鐩稿叧
	OnCheckedChangeListener radioButtonListener = null;
	Button requestLocButton = null;
	boolean isRequest = false;// 鏄惁鎵嬪姩瑙﹀彂璇锋眰瀹氫綅
	boolean isFirstLoc = true;// 鏄惁棣栨瀹氫綅

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		/**
		 * 浣跨敤鍦板浘sdk鍓嶉渶鍏堝垵濮嬪寲BMapManager.
		 * BMapManager鏄叏灞�鐨勶紝鍙负澶氫釜MapView鍏辩敤锛屽畠闇�瑕佸湴鍥炬ā鍧楀垱寤哄墠鍒涘缓锛�
		 * 骞跺湪鍦板浘鍦板浘妯″潡閿�姣佸悗閿�姣侊紝鍙杩樻湁鍦板浘妯″潡鍦ㄤ娇鐢紝BMapManager灏变笉搴旇閿�姣�
		 */
		DemoApplication app = (DemoApplication) this.getApplication();
		if (app.mBMapManager == null) {
			app.mBMapManager = new BMapManager(getApplicationContext());
			/**
			 * 濡傛灉BMapManager娌℃湁鍒濆鍖栧垯鍒濆鍖朆MapManager
			 */
			app.mBMapManager.init(new DemoApplication.MyGeneralListener());
		}
		setContentView(R.layout.activity_locationoverlay);
		CharSequence titleLable = "瀹氫綅鍔熻兘";
		setTitle(titleLable);
		requestLocButton = (Button) findViewById(R.id.button1);
		mCurBtnType = E_BUTTON_TYPE.LOC;
		OnClickListener btnClickListener = new OnClickListener() {
			public void onClick(View v) {
				switch (mCurBtnType) {
				case LOC:
					// 鎵嬪姩瀹氫綅璇锋眰
					requestLocClick();

					break;
				case COMPASS:
					myLocationOverlay.setLocationMode(LocationMode.NORMAL);
					requestLocButton.setText("瀹氫綅");
					mCurBtnType = E_BUTTON_TYPE.LOC;
					break;
				case FOLLOW:
					myLocationOverlay.setLocationMode(LocationMode.COMPASS);
					requestLocButton.setText("缃楃洏");
					mCurBtnType = E_BUTTON_TYPE.COMPASS;
					break;
				}
			}
		};
		requestLocButton.setOnClickListener(btnClickListener);

		// 鍦板浘鍒濆鍖�
		mMapView = (MyLocationMapView) findViewById(R.id.bmapView);
		mMapController = mMapView.getController();
		// 鍦板浘缂╂斁绾у埆锛屽彇鍊艰寖鍥�3-19
		mMapView.getController().setZoom(15);
		mMapView.getController().enableClick(true);
		mMapView.setBuiltInZoomControls(true);
		// 鍒涘缓 寮瑰嚭娉℃场鍥惧眰
		createPaopao();

		// 瀹氫綅鍒濆鍖�
		// TODO 杩欓噷涓嶸3鐗堟湰鐨勫疄鐜颁笉鍚�
		mLocClient = new LocationClient(getApplicationContext());
		locData = new LocationData();
		mLocClient.registerLocationListener(myListener);
		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true);// 鎵撳紑gps
		option.setCoorType("bd09ll"); // 璁剧疆鍧愭爣绫诲瀷
		option.setScanSpan(5000);
		option.setIsNeedAddress(true);// 璁剧疆鏄惁鍖呭惈鍦板潃浣嶇疆淇℃伅
		option.setNeedDeviceDirect(true);// 璁剧疆鏄惁鍖呭惈鏈哄ご淇℃伅
		mLocClient.setLocOption(option);
		// mLocClient.start();

		// 瀹氫綅鍥惧眰鍒濆鍖�
		myLocationOverlay = new locationOverlay(mMapView);
		// 璁剧疆瀹氫綅鏁版嵁
		myLocationOverlay.setData(locData);
		// 娣诲姞瀹氫綅鍥惧眰
		mMapView.getOverlays().add(myLocationOverlay);
		myLocationOverlay.enableCompass();
		// 淇敼瀹氫綅鏁版嵁鍚庡埛鏂板浘灞傜敓鏁�
		mMapView.refresh();

		new Thread(runnable).start();

	}

	/**
	 * 鎵嬪姩瑙﹀彂涓�娆″畾浣嶈姹�
	 */
	public void requestLocClick() {
		isRequest = true;
		mLocClient.requestLocation();
		Toast.makeText(LocationOverlayActivity.this, "姝ｅ湪瀹氫綅鈥︹��",
				Toast.LENGTH_SHORT).show();
	}

	/**
	 * 淇敼浣嶇疆鍥炬爣
	 * 
	 * @param marker
	 */
	public void modifyLocationOverlayIcon(Drawable marker) {
		// 褰撲紶鍏arker涓簄ull鏃讹紝浣跨敤榛樿鍥炬爣缁樺埗
		myLocationOverlay.setMarker(marker);
		// 淇敼鍥惧眰锛岄渶瑕佸埛鏂癕apView鐢熸晥
		mMapView.refresh();
	}

	/**
	 * 鍒涘缓寮瑰嚭娉℃场鍥惧眰
	 */
	public void createPaopao() {
		viewCache = getLayoutInflater()
				.inflate(R.layout.custom_text_view, null);
		popupText = (TextView) viewCache.findViewById(R.id.textcache);
		// 娉℃场鐐瑰嚮鍝嶅簲鍥炶皟
		PopupClickListener popListener = new PopupClickListener() {
			@Override
			public void onClickedPopup(int index) {
				Log.v("click", "clickapoapo");
			}
		};
		pop = new PopupOverlay(mMapView, popListener);
		MyLocationMapView.pop = pop;
	}

	/**
	 * 瀹氫綅SDK鐩戝惉鍑芥暟
	 */
	public class MyLocationListenner implements BDLocationListener {

		@Override
		public void onReceiveLocation(BDLocation location) {
			if (location == null)
				return;

			locData.latitude = location.getLatitude();
			locData.longitude = location.getLongitude();
			// 濡傛灉涓嶆樉绀哄畾浣嶇簿搴﹀湀锛屽皢accuracy璧嬪�间负0鍗冲彲
			locData.accuracy = location.getRadius();
			// 姝ゅ鍙互璁剧疆 locData鐨勬柟鍚戜俊鎭�, 濡傛灉瀹氫綅 SDK
			// 鏈繑鍥炴柟鍚戜俊鎭紝鐢ㄦ埛鍙互鑷繁瀹炵幇缃楃洏鍔熻兘娣诲姞鏂瑰悜淇℃伅銆�
			locData.direction = location.getDerect();
			// 鏇存柊瀹氫綅鏁版嵁
			myLocationOverlay.setData(locData);
			// 鏇存柊鍥惧眰鏁版嵁鎵ц鍒锋柊鍚庣敓鏁�
			mMapView.refresh();
			// 鏄墜鍔ㄨЕ鍙戣姹傛垨棣栨瀹氫綅鏃讹紝绉诲姩鍒板畾浣嶇偣
			if (isRequest || isFirstLoc) {
				// 绉诲姩鍦板浘鍒板畾浣嶇偣
				Log.d("LocationOverlay", "receive location, animate to it");
				mMapController.animateTo(new GeoPoint(
						(int) (locData.latitude * 1e6),
						(int) (locData.longitude * 1e6)));
				isRequest = false;
				myLocationOverlay.setLocationMode(LocationMode.FOLLOWING);
				requestLocButton.setText("璺熼殢");
				mCurBtnType = E_BUTTON_TYPE.FOLLOW;
			}
			// 棣栨瀹氫綅瀹屾垚
			isFirstLoc = false;
		}

		public void onReceivePoi(BDLocation poiLocation) {
			// 灏嗗湪涓嬩釜鐗堟湰涓幓闄oi鍔熻兘
			if (poiLocation == null) {
				return;
			}
			StringBuffer sb = new StringBuffer(256);
			sb.append("Poi time : ");
			sb.append(poiLocation.getTime());
			sb.append("\nerror code : ");
			sb.append(poiLocation.getLocType());
			sb.append("\nlatitude : ");
			sb.append(poiLocation.getLatitude());
			sb.append("\nlontitude : ");
			sb.append(poiLocation.getLongitude());
			sb.append("\nradius : ");
			sb.append(poiLocation.getRadius());
			if (poiLocation.getLocType() == BDLocation.TypeNetWorkLocation) {
				sb.append("\naddr : ");
				sb.append(poiLocation.getAddrStr());
			}
			if (poiLocation.hasPoi()) {
				sb.append("\nPoi:");
				sb.append(poiLocation.getPoi());
			} else {
				sb.append("noPoi information");
			}

			Toast.makeText(LocationOverlayActivity.this, sb.toString(),
					Toast.LENGTH_SHORT).show();
		}
	}

	// 缁ф壙MyLocationOverlay閲嶅啓dispatchTap瀹炵幇鐐瑰嚮澶勭悊
	public class locationOverlay extends MyLocationOverlay {

		public locationOverlay(MapView mapView) {
			super(mapView);
			// TODO Auto-generated constructor stub
		}

		@Override
		protected boolean dispatchTap() {
			// TODO Auto-generated method stub
			// 澶勭悊鐐瑰嚮浜嬩欢,寮瑰嚭娉℃场
			popupText.setBackgroundResource(R.drawable.popup);
			popupText.setText("鎴戠殑浣嶇疆");
			pop.showPopup(BMapUtil.getBitmapFromView(popupText), new GeoPoint(
					(int) (locData.latitude * 1e6),
					(int) (locData.longitude * 1e6)), 8);
			return true;
		}

	}

	@Override
	protected void onPause() {
		mMapView.onPause();
		super.onPause();
	}

	@Override
	protected void onResume() {
		mMapView.onResume();
		super.onResume();
	}

	@Override
	protected void onDestroy() {
		// 閫�鍑烘椂閿�姣佸畾浣�
		if (mLocClient != null)
			mLocClient.stop();
		mMapView.destroy();
		super.onDestroy();
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		mMapView.onSaveInstanceState(outState);

	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		mMapView.onRestoreInstanceState(savedInstanceState);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return true;
	}

	List<String[]> pois = new LinkedList<String[]>();

	private void initOverlay() {
		/**
		 * 鍦ㄦ兂瑕佹坊鍔燨verlay鐨勫湴鏂逛娇鐢ㄤ互涓嬩唬鐮侊紝 姣斿Activity鐨刼nCreate()涓�
		 */
		Drawable mark = getResources().getDrawable(R.drawable.icon_gcoding);
		// 鍒涘缓IteminizedOverlay
		OverlayTest itemOverlay = new OverlayTest(mark, mMapView);
		for (String[] arr : pois) {
			double lat = Double.parseDouble(arr[0]);
			double lon = Double.parseDouble(arr[1]);
			GeoPoint point = new GeoPoint((int) (lat * 1E6), (int) (lon * 1E6));
			OverlayItem item = new OverlayItem(point, arr[2], arr[2]);

			// 鐜板湪鎵�鏈夊噯澶囧伐浣滃凡鍑嗗濂斤紝浣跨敤浠ヤ笅鏂规硶绠＄悊overlay.
			// 娣诲姞overlay, 褰撴壒閲忔坊鍔燨verlay鏃朵娇鐢╝ddItem(List<OverlayItem>)鏁堢巼鏇撮珮
			itemOverlay.addItem(item);

			// 寰堜簩閫肩殑鍐欐硶锛屽厛杩欎箞鍐欑潃
			mMapController.setCenter(point);
		}
		mMapView.getOverlays().clear();
		mMapView.getOverlays().add(itemOverlay);
		mMapView.refresh();

	}

	@SuppressLint("HandlerLeak")
	Handler hander = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);

			Bundle bundle = msg.getData();
			Object obj = bundle.getSerializable("merchants");
			if (obj != null) {
				RestaurantList rList = (RestaurantList) obj;
				for (Merchant merchant : rList.getResult()) {
					if (StringUtils.isNotBlank(merchant.getLat())
							&& StringUtils.isNotBlank(merchant.getLon())) {
						pois.add(new String[] { merchant.getLat(),
								merchant.getLon(), merchant.getName() });
					}

				}
				initOverlay();
			}

		}
	};

	Runnable runnable = new Runnable() {

		@Override
		public void run() {
			Response<RestaurantList> list = resService
					.searchRestaurants("", "");
			Message msg = new Message();
			if (list != null && list.getData() != null
					&& list.getData().getResult() != null) {
				Bundle bundle = new Bundle();
				bundle.putSerializable("merchants", list.getData());
				msg.setData(bundle);
			}
			hander.sendMessage(msg);
		}
	};

	/*
	 * 瑕佸鐞唎verlay鐐瑰嚮浜嬩欢鏃堕渶瑕佺户鎵縄temizedOverlay
	 * 涓嶅鐞嗙偣鍑讳簨浠舵椂鍙洿鎺ョ敓鎴怚temizedOverlay.
	 */
	class OverlayTest extends ItemizedOverlay<OverlayItem> {
		// 鐢∕apView鏋勯�營temizedOverlay
		public OverlayTest(Drawable mark, MapView mapView) {
			super(mark, mapView);
		}

		protected boolean onTap(int index) {
			// 鍦ㄦ澶勭悊item鐐瑰嚮浜嬩欢
			OverlayItem item = getItem(index);
			Toast.makeText(LocationOverlayActivity.this, item.getTitle(),
					Toast.LENGTH_SHORT);
			return true;
		}

		public boolean onTap(GeoPoint pt, MapView mapView) {
			// 鍦ㄦ澶勭悊MapView鐨勭偣鍑讳簨浠讹紝褰撹繑鍥� true鏃�
			super.onTap(pt, mapView);
			return false;
		}
	}
}

/**
 * 缁ф壙MapView閲嶅啓onTouchEvent瀹炵幇娉℃场澶勭悊鎿嶄綔
 * 
 * @author hejin
 * 
 */
class MyLocationMapView extends MapView {
	static PopupOverlay pop = null;// 寮瑰嚭娉℃场鍥惧眰锛岀偣鍑诲浘鏍囦娇鐢�

	public MyLocationMapView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public MyLocationMapView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public MyLocationMapView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (!super.onTouchEvent(event)) {
			// 娑堥殣娉℃场
			if (pop != null && event.getAction() == MotionEvent.ACTION_UP)
				pop.hidePop();
		}
		return true;
	}
}
