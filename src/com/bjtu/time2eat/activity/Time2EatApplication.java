package com.bjtu.time2eat.activity;

import android.app.Application;
import android.content.Context;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.MKGeneralListener;
import com.baidu.mapapi.map.LocationData;
import com.baidu.mapapi.map.MKEvent;

/**
 * 自定义Application，存储相关全局变量。
 * 
 * @author licb
 * 
 */
public class Time2EatApplication extends Application {

	private static Time2EatApplication mInstance = null;
	public boolean m_bKeyRight = true;
	BMapManager mBMapManager = null;

	// 定位相关的信息放到这里，方便后面的Activity中调用。
	private LocationClient mLocClient;
	private LocationData locData = null;
	public MyLocationListenner myListener = new MyLocationListenner();

	@Override
	public void onCreate() {
		super.onCreate();
		mInstance = this;
		initEngineManager(this);

		// 定位初始化
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
	}

	public void initEngineManager(Context context) {
		if (mBMapManager == null) {
			mBMapManager = new BMapManager(context);
		}

		if (!mBMapManager.init(new MyGeneralListener())) {
			Toast.makeText(
					Time2EatApplication.getInstance().getApplicationContext(),
					"BMapManager  初始化错误!", Toast.LENGTH_LONG).show();
		}
	}

	public static Time2EatApplication getInstance() {
		return mInstance;
	}

	public LocationData getLocData() {
		return locData;
	}

	public void setLocData(LocationData locData) {
		this.locData = locData;
	}

	// 常用事件监听，用来处理通常的网络错误，授权验证错误等
	static class MyGeneralListener implements MKGeneralListener {

		@Override
		public void onGetNetworkState(int iError) {
			if (iError == MKEvent.ERROR_NETWORK_CONNECT) {
				Toast.makeText(
						Time2EatApplication.getInstance()
								.getApplicationContext(), "您的网络出错啦！",
						Toast.LENGTH_LONG).show();
			} else if (iError == MKEvent.ERROR_NETWORK_DATA) {
				Toast.makeText(
						Time2EatApplication.getInstance()
								.getApplicationContext(), "输入正确的检索条件！",
						Toast.LENGTH_LONG).show();
			}
			// ...
		}

		@Override
		public void onGetPermissionState(int iError) {
			// 非零值表示key验证未通过
			if (iError != 0) {
				// 授权Key错误：
				Toast.makeText(
						Time2EatApplication.getInstance()
								.getApplicationContext(),
						"请在 DemoApplication.java文件输入正确的授权Key,并检查您的网络连接是否正常！error: "
								+ iError, Toast.LENGTH_LONG).show();
				Time2EatApplication.getInstance().m_bKeyRight = false;
			} else {
				Time2EatApplication.getInstance().m_bKeyRight = true;
				Toast.makeText(
						Time2EatApplication.getInstance()
								.getApplicationContext(), "key认证成功",
						Toast.LENGTH_LONG).show();
			}
		}
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

		}
	}
}