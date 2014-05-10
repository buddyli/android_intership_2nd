package com.bjtu.time2eat.pojo;

import java.io.Serializable;
import java.util.List;

public class Order implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6324329284522700431L;
	private String id;
	private String cost;
	private String status;
	private String name;
	private String datetime;
	private List<String> ordered;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCost() {
		return cost;
	}

	public void setCost(String cost) {
		this.cost = cost;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDatetime() {
		return datetime;
	}

	public void setDatetime(String datetime) {
		this.datetime = datetime;
	}

	public List<String> getOrdered() {
		return ordered;
	}

	public void setOrdered(List<String> ordered) {
		this.ordered = ordered;
	}

}
