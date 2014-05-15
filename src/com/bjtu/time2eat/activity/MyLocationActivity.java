package com.bjtu.time2eat.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
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
import com.baidu.mapapi.map.LocationData;
import com.baidu.mapapi.map.MapController;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationOverlay;
import com.baidu.mapapi.map.MyLocationOverlay.LocationMode;
import com.baidu.mapapi.map.PopupClickListener;
import com.baidu.mapapi.map.PopupOverlay;
import com.baidu.platform.comapi.basestruct.GeoPoint;
import com.bjtu.time2eat.util.BMapUtil;
import com.bjtu.time2eat.view.MyLocationMapView;
import com.example.time2eat.R;

/**
 * 定位的Activity，调用百度定位API，返回当前的位置信息，并且在地图上展示。
 * 
 * @author licb
 * 
 */
@SuppressLint("ShowToast")
public class MyLocationActivity extends Activity {

	private enum E_BUTTON_TYPE {
		LOC, COMPASS, FOLLOW
	}

	private E_BUTTON_TYPE mCurBtnType;

	// 定位相关
	LocationClient mLocClient;
	LocationData locData = null;
	public MyLocationListenner myListener = new MyLocationListenner();

	// 定位图层
	locationOverlay myLocationOverlay = null;
	// 弹出泡泡图层
	private PopupOverlay pop = null;// 弹出泡泡图层，浏览节点时使用
	private TextView popupText = null;// 泡泡view
	private View viewCache = null;

	// 地图相关，使用继承MapView的MyLocationMapView目的是重写touch事件实现泡泡处理
	// 如果不处理touch事件，则无需继承，直接使用MapView即可
	MyLocationMapView mMapView = null; // 地图View
	private MapController mMapController = null;

	// UI相关
	OnCheckedChangeListener radioButtonListener = null;
	Button requestLocButton = null;
	boolean isRequest = false;// 是否手动触发请求定位
	boolean isFirstLoc = true;// 是否首次定位

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		/**
		 * 使用地图sdk前需先初始化BMapManager. BMapManager是全屿的，可为多个MapView共用，它霿要地图模块创建前创建＿
		 * 并在地图地图模块锿毁后锿毁，只要还有地图模块在使用，BMapManager就不应该锿毿
		 */
		Time2EatApplication app = (Time2EatApplication) this.getApplication();
		if (app.mBMapManager == null) {
			app.mBMapManager = new BMapManager(getApplicationContext());
			/**
			 * 如果BMapManager没有初始化则初始化BMapManager
			 */
			app.mBMapManager.init(new Time2EatApplication.MyGeneralListener());
		}
		setContentView(R.layout.activity_locationoverlay);
		CharSequence titleLable = "定位功能";
		setTitle(titleLable);
		requestLocButton = (Button) findViewById(R.id.button1);
		mCurBtnType = E_BUTTON_TYPE.LOC;
		OnClickListener btnClickListener = new OnClickListener() {
			public void onClick(View v) {
				switch (mCurBtnType) {
				case LOC:
					// 手动定位请求
					requestLocClick();
					break;
				case COMPASS:
					myLocationOverlay.setLocationMode(LocationMode.NORMAL);
					requestLocButton.setText("定位");
					mCurBtnType = E_BUTTON_TYPE.LOC;
					break;
				case FOLLOW:
					myLocationOverlay.setLocationMode(LocationMode.COMPASS);
					requestLocButton.setText("罗盘");
					mCurBtnType = E_BUTTON_TYPE.COMPASS;
					break;
				}
			}
		};
		requestLocButton.setOnClickListener(btnClickListener);

		// 地图初始匿
		mMapView = (MyLocationMapView) findViewById(R.id.bmapView);
		mMapController = mMapView.getController();
		// 地图缩放级别，取值范囿3-19
		mMapController.setZoom(17);
		mMapController.enableClick(true);
		mMapView.setBuiltInZoomControls(true);
		// 创建 弹出泡泡图层
		createPaopao();

		// 定位初始匿
		// TODO 这里与V3版本的实现不吿
		mLocClient = new LocationClient(getApplicationContext());
		locData = new LocationData();
		mLocClient.registerLocationListener(myListener);
		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true);// 打开gps
		option.setCoorType("bd09ll"); // 设置坐标类型
		option.setScanSpan(5000);
		option.setIsNeedAddress(true);// 设置是否包含地址位置信息
		option.setNeedDeviceDirect(true);// 设置是否包含机头信息
		mLocClient.setLocOption(option);
		mLocClient.start();

		// 定位图层初始匿
		myLocationOverlay = new locationOverlay(mMapView);
		// 设置定位数据
		myLocationOverlay.setData(locData);
		// 添加定位图层
		mMapView.getOverlays().add(myLocationOverlay);
		myLocationOverlay.enableCompass();
		// 修改定位数据后刷新图层生敿
		mMapView.refresh();
	}

	/**
	 * 手动触1次定位请求
	 */
	public void requestLocClick() {
		isRequest = true;
		mLocClient.requestLocation();
		Toast.makeText(MyLocationActivity.this, "正在定位…", Toast.LENGTH_SHORT)
				.show();
	}

	/**
	 * 修改位置图标
	 * 
	 * @param marker
	 */
	public void modifyLocationOverlayIcon(Drawable marker) {
		// 当传入marker为null时，使用默认图标绘制
		myLocationOverlay.setMarker(marker);
		// 修改图层，需要刷新MapView生效
		mMapView.refresh();
	}

	/**
	 * 创建弹出泡泡图层
	 */
	public void createPaopao() {
		viewCache = getLayoutInflater()
				.inflate(R.layout.custom_text_view, null);
		popupText = (TextView) viewCache.findViewById(R.id.textcache);
		// 泡泡点击响应回调
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
	 * 定位SDK监听函数
	 */
	class MyLocationListenner implements BDLocationListener {

		@Override
		public void onReceiveLocation(BDLocation location) {
			if (location == null)
				return;

			locData.latitude = location.getLatitude();
			locData.longitude = location.getLongitude();
			// 如果不显示定位精度圈，将accuracy赋忼为0即可
			locData.accuracy = location.getRadius();
			// 此处可以设置 locData的方向信恿, 如果定位 SDK
			// 未返回方向信息，用户可以自己实现罗盘功能添加方向信息
			locData.direction = location.getDerect();
			// 更新定位数据
			myLocationOverlay.setData(locData);
			// 更新图层数据执行刷新后生敿
			mMapView.refresh();
			// 是手动触发请求或首次定位时，移动到定位点
			if (isRequest || isFirstLoc) {
				// 移动地图到定位点
				Log.d("LocationOverlay", "receive location, animate to it");
				mMapController.animateTo(new GeoPoint(
						(int) (locData.latitude * 1e6),
						(int) (locData.longitude * 1e6)));
				isRequest = false;
				myLocationOverlay.setLocationMode(LocationMode.FOLLOWING);
				requestLocButton.setText("跟随");
				mCurBtnType = E_BUTTON_TYPE.FOLLOW;
			}
			// 首次定位完成
			isFirstLoc = false;
		}

		public void onReceivePoi(BDLocation poiLocation) {
			// 将在下个版本中去除poi功能
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

			Toast.makeText(MyLocationActivity.this, sb.toString(),
					Toast.LENGTH_SHORT).show();
		}
	}

	// 继承MyLocationOverlay重写dispatchTap实现点击处理
	public class locationOverlay extends MyLocationOverlay {

		public locationOverlay(MapView mapView) {
			super(mapView);
			// TODO Auto-generated constructor stub
		}

		@Override
		protected boolean dispatchTap() {
			// TODO Auto-generated method stub
			// 处理点击事件,弹出泡泡
			popupText.setBackgroundResource(R.drawable.popup);
			popupText.setText("我的位置");
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
		// 逿出时锿毁定使
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

}
