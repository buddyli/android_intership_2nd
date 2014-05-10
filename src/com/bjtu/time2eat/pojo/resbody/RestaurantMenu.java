package com.bjtu.time2eat.pojo.resbody;

import java.util.LinkedList;
import java.util.List;

import com.bjtu.time2eat.pojo.Menu;

/**
 * 查询餐馆订单Response Body
 * 
 * @author licb
 * 
 */
public class RestaurantMenu extends IResponseBody {
	private List<Menu> menu = new LinkedList<Menu>();

	public List<Menu> getMenu() {
		return menu;
	}

	public void setMenu(List<Menu> menu) {
		this.menu = menu;
	}

}
