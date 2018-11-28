package com.zq.xinjiang.approval.entity;

import java.io.Serializable;

public class UserEntity implements Serializable {

	/*
	{
	    "errno": "0",
	    "id": 810,
	    "username": "郭成军",
	    "loginname": "00201",
	    "phone": "15339052899",
	    "orgid": 834,
	    "orgname": "安监局",
	    "mobilelogintime": "2015-11-25 11:15:07",
	    "mobilelastlogintime": "2015-11-25 11:14:27"
	}
	*/
	
	private String id;
	private String username;
	private String loginname;
	private String phone;
	private String orgid;
	private String orgname;
	private String mobilelogintime;
	private String mobilelastlogintime;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getLoginname() {
		return loginname;
	}

	public void setLoginname(String loginname) {
		this.loginname = loginname;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getOrgid() {
		return orgid;
	}

	public void setOrgid(String orgid) {
		this.orgid = orgid;
	}

	public String getOrgname() {
		return orgname;
	}

	public void setOrgname(String orgname) {
		this.orgname = orgname;
	}

	public String getMobilelogintime() {
		return mobilelogintime;
	}

	public void setMobilelogintime(String mobilelogintime) {
		this.mobilelogintime = mobilelogintime;
	}

	public String getMobilelastlogintime() {
		return mobilelastlogintime;
	}

	public void setMobilelastlogintime(String mobilelastlogintime) {
		this.mobilelastlogintime = mobilelastlogintime;
	}
	
}
