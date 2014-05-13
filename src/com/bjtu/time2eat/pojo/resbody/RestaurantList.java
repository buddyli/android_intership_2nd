package com.bjtu.time2eat.pojo.resbody;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import com.bjtu.time2eat.pojo.Merchant;

/**
 * 查询附近餐馆列表Response Body
 * 
 * @author licb
 * 
 */
public class RestaurantList extends IResponseBody implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8205885785187461542L;
	private List<Merchant> result = new LinkedList<Merchant>();

	public List<Merchant> getResult() {
		return result;
	}

	public void setResult(List<Merchant> result) {
		this.result = result;
	}

}
