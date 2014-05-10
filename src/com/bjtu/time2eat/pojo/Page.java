package com.bjtu.time2eat.pojo;

import java.io.Serializable;

public class Page implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6955461151202329781L;
	private int pn;
	private int ps;
	private int pc;
	private int rc;

	public int getPn() {
		return pn;
	}

	public void setPn(int pn) {
		this.pn = pn;
	}

	public int getPs() {
		return ps;
	}

	public void setPs(int ps) {
		this.ps = ps;
	}

	public int getPc() {
		return pc;
	}

	public void setPc(int pc) {
		this.pc = pc;
	}

	public int getRc() {
		return rc;
	}

	public void setRc(int rc) {
		this.rc = rc;
	}

}
