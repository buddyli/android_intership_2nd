package com.bjtu.time2eat.activity;

import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.map.ItemizedOverlay;
import com.baidu.mapapi.map.LocationData;
import com.baidu.mapapi.map.MapController;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.OverlayItem;
import com.baidu.mapapi.map.PopupClickListener;
import com.baidu.mapapi.map.PopupOverlay;
import com.baidu.platform.comapi.basestruct.GeoPoint;
import com.bjtu.time2eat.pojo.Merchant;
import com.bjtu.time2eat.pojo.Response;
import com.bjtu.time2eat.pojo.resbody.RestaurantList;
import com.bjtu.time2eat.service.RestaurantService;
import com.bjtu.time2eat.util.BMapUtil;
import com.bjtu.time2eat.view.MyLocationMapView;
import com.example.time2eat.R;

/**
 * 此demo用来展示如何结合定位SDK实现定位，并使用MyLocationOverlay绘制定位位置 同时展示如何使用自定义图标绘制并点击时弹出泡泿
 * 
 */
@SuppressLint("ShowToast")
public class LocationOverlayActivity extends Activity {
	private RestaurantService resService = new RestaurantService();

	// 地图相关，使用继承MapView的MyLocationMapView目的是重写touch事件实现泡泡处理
	// 如果不处理touch事件，则无需继承，直接使用MapView即可
	MyLocationMapView mMapView = null; // 地图View
	private MapController mMapController = null;

	List<String[]> pois = new LinkedList<String[]>();
	List<OverlayItem> overlayItems = new LinkedList<OverlayItem>();
	List<Merchant> merchants = new LinkedList<Merchant>();
	// 弹出泡泡图层
	private PopupOverlay pop = null;
	/** 一堆需要用到的变量 */
	private TextView popupText = null;
	private View viewCache = null;
	private View popupInfo = null;
	private View popupLeft = null;
	private View popupRight = null;
	protected int currentItemIndex = 0;

	// 当前位置的经纬度坐标
	private double currentLat;
	private double currentLon;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		/**
		 * 使用地图sdk前需先初始化BMapManager. BMapManager是全局的，可为多个MapView共用，它霿要地图模块创建前创建＿
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

		// 地图初始匿
		mMapView = (MyLocationMapView) findViewById(R.id.bmapView);
		mMapController = mMapView.getController();
		// 地图缩放级别，取值范囿3-19
		mMapView.getController().setZoom(15);
		// 设置地图是否响应点击事件
		mMapView.getController().enableClick(true);
		// 显示内置缩放控件
		mMapView.setBuiltInZoomControls(true);

		/**
		 * 创建一个popupoverlay，点击图标的响应在这里处理。
		 */
		PopupClickListener popListener = new PopupClickListener() {
			@Override
			public void onClickedPopup(int index) {
				if (index == 0) {
					Intent intent = new Intent(LocationOverlayActivity.this,
							RestaurantDetailActivity.class);
					Merchant mer = merchants.get(currentItemIndex);
					Toast.makeText(LocationOverlayActivity.this,
							"跳转到餐馆: " + mer.getName(), Toast.LENGTH_SHORT)
							.show();
					intent.putExtra("id", mer.getId());
					intent.putExtra("name", mer.getName());
					intent.putExtra("address", mer.getAddress());
					intent.putExtra("telno", mer.getTelno());
					intent.putExtra("price", mer.getPrice());
					startActivity(intent);
					// 点击查看图标，跳转到餐馆详情
				} else if (index == 1) {
					// 点击商户名称，不做响应
				} else if (index == 2) {
					// 点击路线图标，表示当前位置到餐馆的线路
				}
			}
		};
		pop = new PopupOverlay(mMapView, popListener);
		new Thread(runnable).start();

		// 从Application中获取当前位置的坐标。
		LocationData ld = app.getLocData();
		currentLat = ld.latitude;
		currentLon = ld.longitude;

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
		overlayItems.clear();
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

	private void initOverlay() {
		// 清除图层上以前的元素
		mMapView.getOverlays().clear();
		// overlayItems.clear();

		/**
		 * 向地图添加自定义View.
		 */
		viewCache = getLayoutInflater()
				.inflate(R.layout.custom_text_view, null);
		popupInfo = (View) viewCache.findViewById(R.id.popinfo);
		popupLeft = (View) viewCache.findViewById(R.id.popleft);
		popupRight = (View) viewCache.findViewById(R.id.popright);
		popupText = (TextView) viewCache.findViewById(R.id.textcache);

		/**
		 * 在想要添加Overlay的地方使用以下代码， 比如Activity的onCreate()丿
		 */
		Drawable mark = getResources().getDrawable(R.drawable.icon_gcoding);
		Drawable navMark = getResources()
				.getDrawable(R.drawable.bg_map_location);
		// 创建IteminizedOverlay
		MyOverlay itemOverlay = new MyOverlay(mark, mMapView);

		// 设置当前定位位置为中心点
		GeoPoint center = null;
		OverlayItem centerItem = null;
		if (currentLat != 0 && currentLon != 0) {
			center = new GeoPoint((int) (currentLat * 1E6),
					(int) (currentLon * 1E6));
			centerItem = new OverlayItem(center, String.valueOf(currentLat),
					String.valueOf(currentLon));
			centerItem.setMarker(navMark);
			overlayItems.add(0, centerItem);
			itemOverlay.addItem(centerItem);
		}

		for (String[] arr : pois) {
			double lat = Double.parseDouble(arr[0]);
			double lon = Double.parseDouble(arr[1]);

			double[] xy = BMapUtil.bd_encrypt(lat, lon);
			GeoPoint point = new GeoPoint((int) (xy[1] * 1E6),
					(int) (xy[0] * 1E6));
			OverlayItem item = new OverlayItem(point, arr[2], arr[2]);

			itemOverlay.addItem(item);
		}
		// 保存所有item，以便overlay在reset后重新添加
		overlayItems.addAll(itemOverlay.getAllItem());
		// 设置地图的中心点，这里因该是定位获取到得到位置。。。
		if (center != null)
			mMapController.animateTo(center);

		// 将位置加入到地图中
		mMapView.getOverlays().add(itemOverlay);
		// 刷新地图，是刚才的位置生效
		mMapView.refresh();

		// 隐藏所有的弹窗
		// pop.hidePop();
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
				// 保存一下，在响应用户点击地图上商户图标时使用。
				merchants.addAll(rList.getResult());

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
	 * 要处理overlay点击事件时需要继承ItemizedOverlay 不处理点击事件时可直接生成ItemizedOverlay.
	 */
	class MyOverlay extends ItemizedOverlay<OverlayItem> {
		// 用MapView构建ItemizedOverlay
		public MyOverlay(Drawable mark, MapView mapView) {
			super(mark, mapView);
		}

		@Override
		public boolean onTap(int index) {
			if (index == 0) {
				// 第一个坐标是当前的定位位置，因此点击不跳转到商户详情
				popupText.setBackgroundResource(R.drawable.popup);
				popupText.setText("我的位置");
				pop.showPopup(BMapUtil.getBitmapFromView(popupText),
						new GeoPoint((int) (currentLat * 1e6),
								(int) (currentLon * 1e6)), 8);
			} else {
				OverlayItem item = getItem(index);
				// 保存一下这个商户的位置，在跳转到详情和查询路线时可以用到。
				currentItemIndex = index;

				try {
					popupText.setText(getItem(index).getTitle());
					Bitmap[] bitMaps = { BMapUtil.getBitmapFromView(popupLeft),
							BMapUtil.getBitmapFromView(popupInfo),
							BMapUtil.getBitmapFromView(popupRight) };

					pop.showPopup(bitMaps, item.getPoint(), 32);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			return true;
		}

		@Override
		public boolean onTap(GeoPoint pt, MapView mMapView) {
			if (pop != null) {
				pop.hidePop();
			}
			return false;
		}
	}

}
