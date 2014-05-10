package com.bjtu.time2eat.pojo;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;

import org.apache.commons.io.FileUtils;

import com.bjtu.time2eat.pojo.resbody.RestaurantDetail;
import com.bjtu.time2eat.pojo.resbody.RestaurantList;
import com.bjtu.time2eat.pojo.resbody.RestaurantMenu;
import com.bjtu.time2eat.pojo.resbody.RestaurantOrder;
import com.bjtu.time2eat.pojo.resbody.UserOrderHistory;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			// URL fUrl =
			// Main.class.getClassLoader().getResource("example.json");
			String json = FileUtils.readFileToString(new File(Main.class
					.getResource("restaurants.json").getPath()));
			Gson gson = (new GsonBuilder()).disableHtmlEscaping().create();
			Type type = new TypeToken<Response<RestaurantList>>() {
			}.getType();
			Response<RestaurantList> resp = gson.fromJson(json, type);
			System.out.println(resp);

			json = FileUtils.readFileToString(new File(Main.class.getResource(
					"restaurant_detail.json").getPath()));
			gson = (new GsonBuilder()).disableHtmlEscaping().create();
			type = new TypeToken<Response<RestaurantDetail>>() {
			}.getType();
			Response<RestaurantDetail> resp1 = gson.fromJson(json, type);
			System.out.println(resp1);

			json = FileUtils.readFileToString(new File(Main.class.getResource(
					"restaurant_menu.json").getPath()));
			gson = (new GsonBuilder()).disableHtmlEscaping().create();
			type = new TypeToken<Response<RestaurantMenu>>() {
			}.getType();
			Response<RestaurantMenu> resp2 = gson.fromJson(json, type);
			System.out.println(resp2);

			json = FileUtils.readFileToString(new File(Main.class.getResource(
					"restaurant_order.json").getPath()));
			gson = (new GsonBuilder()).disableHtmlEscaping().create();
			type = new TypeToken<Response<RestaurantOrder>>() {
			}.getType();
			Response<RestaurantOrder> resp3 = gson.fromJson(json, type);
			System.out.println(resp3);

			json = FileUtils.readFileToString(new File(Main.class.getResource(
					"user_history.json").getPath()));
			gson = (new GsonBuilder()).disableHtmlEscaping().create();
			type = new TypeToken<Response<UserOrderHistory>>() {
			}.getType();
			Response<UserOrderHistory> resp4 = gson.fromJson(json, type);
			System.out.println(resp4);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
