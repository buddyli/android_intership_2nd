package com.bjtu.time2eat.pojo;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

/**
 * 对应JSON报文中的Data节点
 * 
 * @author licb
 * 
 * @param <R>
 */
public class Data<R extends Object> implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1316911649312392791L;
	private List<?> result = new LinkedList<Merchant>();

	public List<?> getResult() {
		return result;
	}

	public void setResult(List<?> result) {
		this.result = result;
	}

}
