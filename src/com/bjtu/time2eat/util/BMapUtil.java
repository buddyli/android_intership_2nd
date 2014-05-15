package com.bjtu.time2eat.util;

import android.graphics.Bitmap;
import android.view.View;

public class BMapUtil {

	/**
	 * 从view 得到图片
	 * 
	 * @param view
	 * @return
	 */
	public static Bitmap getBitmapFromView(View view) {
		view.destroyDrawingCache();
		view.measure(View.MeasureSpec.makeMeasureSpec(0,
				View.MeasureSpec.UNSPECIFIED), View.MeasureSpec
				.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
		view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
		view.setDrawingCacheEnabled(true);
		Bitmap bitmap = view.getDrawingCache(true);
		return bitmap;
	}

	static double x_pi = 3.14159265358979324 * 3000.0 / 180.0;

	/**
	 * 实现02到百度坐标系的转换
	 * 
	 * @param gg_lat
	 * @param gg_lon
	 * @return
	 */
	public static double[] bd_encrypt(double gg_lat, double gg_lon) {

		double x = gg_lon, y = gg_lat;
		double z = Math.sqrt(x * x + y * y) + 0.00002 * Math.sin(y * x_pi);
		double theta = Math.atan2(y, x) + 0.000003 * Math.cos(x * x_pi);
		double bd_lon = z * Math.cos(theta) + 0.0065;
		double bd_lat = z * Math.sin(theta) + 0.006;

		return new double[] { bd_lon, bd_lat };
	}

	/**
	 * 实现百度09坐标系到02坐标系的转换
	 * 
	 * @param bd_lat
	 * @param bd_lon
	 * @return
	 */
	public static double[] bd_decrypt(double bd_lat, double bd_lon) {
		double x = bd_lon - 0.0065, y = bd_lat - 0.006;
		double z = Math.sqrt(x * x + y * y) - 0.00002 * Math.sin(y * x_pi);
		double theta = Math.atan2(y, x) - 0.000003 * Math.cos(x * x_pi);
		double gg_lon = z * Math.cos(theta);
		double gg_lat = z * Math.sin(theta);
		return new double[] { gg_lon, gg_lat };
	}
}
