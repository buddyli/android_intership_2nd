package com.bjtu.time2eat.pojo;

import java.io.Serializable;

import com.bjtu.time2eat.pojo.resbody.IResponseBody;

public class Response<R extends IResponseBody> implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8894732207138584616L;
	private Status status;
	private Page page;
	private R data;

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public Page getPage() {
		return page;
	}

	public void setPage(Page page) {
		this.page = page;
	}

	public R getData() {
		return data;
	}

	public void setData(R data) {
		this.data = data;
	}

}
