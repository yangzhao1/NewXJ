package com.zq.xinjiang.approval.entity;

import java.io.Serializable;

public class SytjEntity implements Serializable {

	/*
	{
	    "errno": "0",
	    "sum": {
	        "actorid": "00201",
	        "daiban": "3",
	        "zhengchang": "3",
	        "yujing": "0",
	        "guoqi": "0",
	        "yichang": "0"
	    },
	    "stats": {
	        "yiban": "3",
	        "xiaoxi": "4"
	    }
	}
	*/
	
	private String actorid;
	private String daiban;
	private String zhengchang;
	private String yujing;
	private String guoqi;
	private String yichang;
	private String yiban;
	private String xiaoxi;

	public String getActorid() {
		return actorid;
	}

	public void setActorid(String actorid) {
		this.actorid = actorid;
	}

	public String getDaiban() {
		return daiban;
	}

	public void setDaiban(String daiban) {
		this.daiban = daiban;
	}

	public String getZhengchang() {
		return zhengchang;
	}

	public void setZhengchang(String zhengchang) {
		this.zhengchang = zhengchang;
	}

	public String getYujing() {
		return yujing;
	}

	public void setYujing(String yujing) {
		this.yujing = yujing;
	}

	public String getGuoqi() {
		return guoqi;
	}

	public void setGuoqi(String guoqi) {
		this.guoqi = guoqi;
	}

	public String getYichang() {
		return yichang;
	}

	public void setYichang(String yichang) {
		this.yichang = yichang;
	}

	public String getYiban() {
		return yiban;
	}

	public void setYiban(String yiban) {
		this.yiban = yiban;
	}

	public String getXiaoxi() {
		return xiaoxi;
	}

	public void setXiaoxi(String xiaoxi) {
		this.xiaoxi = xiaoxi;
	}

}
