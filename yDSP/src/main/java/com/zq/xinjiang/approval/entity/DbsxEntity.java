package com.zq.xinjiang.approval.entity;

import java.io.Serializable;

public class DbsxEntity implements Serializable {

	private String id;
	private String sncode;
	private String itemname;
	private String username;

	private String endtime;
	private String statedesc;

	private String charge;

	private String iteminstanceid;

	private String issupervised;
	private String flowid;
	private String stepid;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getSncode() {
		return sncode;
	}

	public void setSncode(String sncode) {
		this.sncode = sncode;
	}

	public String getItemname() {
		return itemname;
	}

	public void setItemname(String itemname) {
		this.itemname = itemname;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEndtime() {
		return endtime;
	}

	public void setEndtime(String endtime) {
		this.endtime = endtime;
	}

	public String getStatedesc() {
		return statedesc;
	}

	public void setStatedesc(String statedesc) {
		this.statedesc = statedesc;
	}

	public String getCharge() {
		return charge;
	}

	public void setCharge(String charge) {
		this.charge = charge;
	}

	public String getIteminstanceid() {
		return iteminstanceid;
	}

	public void setIteminstanceid(String iteminstanceid) {
		this.iteminstanceid = iteminstanceid;
	}

	public String getIssupervised() {
		return issupervised;
	}

	public void setIssupervised(String issupervised) {
		this.issupervised = issupervised;
	}

	public String getFlowid() {
		return flowid;
	}

	public void setFlowid(String flowid) {
		this.flowid = flowid;
	}

	public String getStepid() {
		return stepid;
	}

	public void setStepid(String stepid) {
		this.stepid = stepid;
	}

}
