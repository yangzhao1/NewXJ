package com.zq.xinjiang.approval.entity;

import java.io.Serializable;

public class LoginEntity implements Serializable {

	/*
	{
	    "errno": "0",
	    "item": {
	        "id": 810,
	        "loginname": "00201",
	        "username": "郭成军",
	        "phone": "",
	        "sex": "男",
	        "orgid": 834,
	        "windowid": "8",
	        "mobilelastlogintime": "无",
	        "sessionid": "KCEDX5JNIKTHMS55WACJVDAM",
	        "userpicurl": "",
	        "mobileloginstate": 1,
	        "orgname": "安监局"
	    }
	}
	*/
	private String id;
	private String loginname;
	private String username;
	private String phone;
	private String sex;
	private String orgid;
	private String windowid;
	private String mobilelastlogintime;
	private String sessionid;
	private String userpicurl;
	private String mobileloginstate;
	private String orgname;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getLoginname() {
		return loginname;
	}

	public void setLoginname(String loginname) {
		this.loginname = loginname;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getOrgid() {
		return orgid;
	}

	public void setOrgid(String orgid) {
		this.orgid = orgid;
	}

	public String getWindowid() {
		return windowid;
	}

	public void setWindowid(String windowid) {
		this.windowid = windowid;
	}

	public String getMobilelastlogintime() {
		return mobilelastlogintime;
	}

	public void setMobilelastlogintime(String mobilelastlogintime) {
		this.mobilelastlogintime = mobilelastlogintime;
	}

	public String getSessionid() {
		return sessionid;
	}

	public void setSessionid(String sessionid) {
		this.sessionid = sessionid;
	}

	public String getUserpicurl() {
		return userpicurl;
	}

	public void setUserpicurl(String userpicurl) {
		this.userpicurl = userpicurl;
	}

	public String getMobileloginstate() {
		return mobileloginstate;
	}

	public void setMobileloginstate(String mobileloginstate) {
		this.mobileloginstate = mobileloginstate;
	}

	public String getOrgname() {
		return orgname;
	}

	public void setOrgname(String orgname) {
		this.orgname = orgname;
	}
	
}
