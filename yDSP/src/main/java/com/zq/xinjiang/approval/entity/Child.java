package com.zq.xinjiang.approval.entity;

public class Child {

	private String username;
	private String loginname;
	private boolean isChecked;

	public Child(String username, String loginname) {
		this.username = username;
		this.loginname = loginname;
		this.isChecked = false;
	}

	public void setChecked(boolean isChecked) {
		this.isChecked = isChecked;
	}

	public void toggle() {
		this.isChecked = !this.isChecked;
	}

	public boolean getChecked() {
		return this.isChecked;
	}

	public String getUsername() {
		return username;
	}

	public String getLoginname() {
		return loginname;
	}

}
