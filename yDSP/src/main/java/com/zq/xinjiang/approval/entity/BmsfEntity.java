package com.zq.xinjiang.approval.entity;

import java.io.Serializable;

public class BmsfEntity implements Serializable {

	private String orgid;
	private String orgname;
	private String yishou;
	private String charge;

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

	public String getYishou() {
		return yishou;
	}

	public void setYishou(String yishou) {
		this.yishou = yishou;
	}

	public String getCharge() {
		return charge;
	}

	public void setCharge(String charge) {
		this.charge = charge;
	}

}
