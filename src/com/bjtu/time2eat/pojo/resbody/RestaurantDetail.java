package com.bjtu.time2eat.pojo.resbody;

import java.util.LinkedList;
import java.util.List;

import com.bjtu.time2eat.pojo.Menu;
import com.bjtu.time2eat.pojo.Merchant;

/**
 * 查询餐馆详情Response Body
 * 
 * @author licb
 * 
 */
public class RestaurantDetail extends IResponseBody {
	private Merchant detail = new Merchant();
	private List<Menu> menus = new LinkedList<Menu>();

	public Merchant getDetail() {
		return detail;
	}

	public void setDetail(Merchant detail) {
		this.detail = detail;
	}

	public List<Menu> getMenus() {
		return menus;
	}

	public void setMenus(List<Menu> menus) {
		this.menus = menus;
	}

}
