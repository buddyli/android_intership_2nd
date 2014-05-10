package com.bjtu.time2eat.pojo;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			// URL fUrl =
			// Main.class.getClassLoader().getResource("example.json");
			String json = FileUtils.readFileToString(new File(Main.class.getResource("example.json").getPath()));
			Gson gson = (new GsonBuilder()).disableHtmlEscaping().create();
			Response resp = gson.fromJson(json, Response.class);
			System.out.println(resp);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
