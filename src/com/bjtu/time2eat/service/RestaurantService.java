package com.bjtu.time2eat.service;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import com.bjtu.time2eat.pojo.Response;
import com.bjtu.time2eat.pojo.resbody.RestaurantDetail;
import com.bjtu.time2eat.pojo.resbody.RestaurantList;
import com.bjtu.time2eat.pojo.resbody.RestaurantMenu;
import com.bjtu.time2eat.pojo.resbody.RestaurantOrder;
import com.bjtu.time2eat.util.HttpUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

public class RestaurantService {
	private Gson gson = (new GsonBuilder()).disableHtmlEscaping().create();

	/**
	 * 搜索周边餐馆
	 * 
	 * @param lat
	 * @param lon
	 * @return
	 */
	public Response<RestaurantList> searchRestaurants(String lat, String lon) {
		String action = "search_restaurants";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("lat", lat);
		params.put("lon", lon);

		try {
			// String json =
			// HttpUtils.fetchResponseByPostWithUrlEncodedFormEntity(action,
			// params, "UTF-8");
			String json = HttpUtils.fetchResponseByGet(action, params, "UTF-8");
			Type type = new TypeToken<Response<RestaurantList>>() {
			}.getType();
			Response<RestaurantList> resp = gson.fromJson(json, type);

			return resp;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * 加载餐馆详情
	 * 
	 * @param id
	 * @return
	 */
	public Response<RestaurantDetail> restaurantDetail(String id) {
		String action = "restaurant_detail";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("id", id);

		try {
			String json = HttpUtils.fetchResponseByGet(action, params, "UTF-8");

			Type type = new TypeToken<Response<RestaurantDetail>>() {
			}.getType();
			Response<RestaurantDetail> resp = gson.fromJson(json, type);

			return resp;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * 根据餐馆标识加载餐馆菜单
	 * 
	 * @param id
	 * @return
	 */
	public Response<RestaurantMenu> restaurantMenu(String id) {
		String action = "restaurant_menu";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("id", id);

		try {
			String json = HttpUtils.fetchResponseByGet(action, params, "UTF-8");

			Type type = new TypeToken<Response<RestaurantMenu>>() {
			}.getType();
			Response<RestaurantMenu> resp = gson.fromJson(json, type);

			return resp;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * 餐馆预订接口
	 * 
	 * @param id
	 * @param mobile
	 * @param foods
	 *            以英文半角逗号分隔的菜单ID
	 * @param date
	 * @param time
	 * @param num
	 *            用餐人数，默认1
	 * @return
	 */
	public Response<RestaurantOrder> restaurantOrder(String id, String mobile,
			String foods, String date, String time, int num) {
		String action = "restaurant_order";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("id", id);
		params.put("mobile", mobile);
		params.put("foods", foods);
		params.put("date", date);
		params.put("time", time);
		if (num <= 0) {
			num = 1;
		}
		params.put("num", String.valueOf(num));

		try {
			String json = HttpUtils.fetchResponseByGet(action, params, "UTF-8");

			Type type = new TypeToken<Response<RestaurantOrder>>() {
			}.getType();
			Response<RestaurantOrder> resp = gson.fromJson(json, type);

			return resp;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}
}
