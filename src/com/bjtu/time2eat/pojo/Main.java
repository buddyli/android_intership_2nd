package com.bjtu.time2eat.pojo;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;

import org.apache.commons.io.FileUtils;

import com.bjtu.time2eat.pojo.resbody.RestaurantList;
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
			String json = FileUtils.readFileToString(new File(Main.class.getResource("restaurants.json").getPath()));
			Gson gson = (new GsonBuilder()).disableHtmlEscaping().create();
			Type type = new TypeToken<Response<RestaurantList>>() {
			}.getType();
			Response<RestaurantList> resp = gson.fromJson(json, type);
			System.out.println(resp);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
