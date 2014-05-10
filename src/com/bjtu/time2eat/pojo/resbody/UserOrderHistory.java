package com.bjtu.time2eat.pojo.resbody;

import java.util.LinkedList;
import java.util.List;

import com.bjtu.time2eat.pojo.Order;

/**
 * 用户历史预约订单Response Body
 * 
 * @author licb
 * 
 */
public class UserOrderHistory extends IResponseBody {
	private List<Order> history = new LinkedList<Order>();

	public List<Order> getHistory() {
		return history;
	}

	public void setHistory(List<Order> history) {
		this.history = history;
	}

}
