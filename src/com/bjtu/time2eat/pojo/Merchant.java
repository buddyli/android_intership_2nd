package com.bjtu.time2eat.pojo;

import java.io.Serializable;

public class Merchant implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3837896600456765576L;
	private String id;
	private String name;
	private String trade_name;
	private String telno;
	private String address;
	private String landmark;
	private String lon;
	private String lat;
	private String distance;
	private String m_star;
	private String price;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTrade_name() {
		return trade_name;
	}

	public void setTrade_name(String trade_name) {
		this.trade_name = trade_name;
	}

	public String getTelno() {
		return telno;
	}

	public void setTelno(String telno) {
		this.telno = telno;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getLandmark() {
		return landmark;
	}

	public void setLandmark(String landmark) {
		this.landmark = landmark;
	}

	public String getLon() {
		return lon;
	}

	public void setLon(String lon) {
		this.lon = lon;
	}

	public String getLat() {
		return lat;
	}

	public void setLat(String lat) {
		this.lat = lat;
	}

	public String getDistance() {
		return distance;
	}

	public void setDistance(String distance) {
		this.distance = distance;
	}

	public String getM_star() {
		return m_star;
	}

	public void setM_star(String m_star) {
		this.m_star = m_star;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

}
