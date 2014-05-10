package com.bjtu.time2eat.pojo.resbody;

import java.util.LinkedList;
import java.util.List;

import com.bjtu.time2eat.pojo.Merchant;

public class RestaurantList implements IResponseBody {
	private List<Merchant> detail = new LinkedList<Merchant>();

	public List<Merchant> getDetail() {
		return detail;
	}

	public void setDetail(List<Merchant> detail) {
		this.detail = detail;
	}

}
