package com.bjtu.time2eat.service;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.client.ClientProtocolException;

import com.bjtu.time2eat.pojo.Response;
import com.bjtu.time2eat.pojo.resbody.UserOrderHistory;
import com.bjtu.time2eat.util.HttpUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

public class UserService {
	private Gson gson = (new GsonBuilder()).disableHtmlEscaping().create();

	public Response<UserOrderHistory> getUserOrderHistory(String mobile) {
		String action = "user_book_history";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("mobile", mobile);

		try {
			String json = HttpUtils.fetchResponseByPostWithUrlEncodedFormEntity(action, params, "UTF-8");

			Type type = new TypeToken<Response<UserOrderHistory>>() {
			}.getType();
			Response<UserOrderHistory> resp = gson.fromJson(json, type);

			return resp;
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}
}
