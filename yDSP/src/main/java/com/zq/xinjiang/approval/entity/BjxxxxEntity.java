package com.zq.xinjiang.approval.entity;

import java.io.Serializable;

public class BjxxxxEntity implements Serializable {

	private String stepname;
	private String starttime;
	private String endtime;

	private String logs_stepname;
	private String logs_actorid_name;
	private String logs_remark;

	private String docs_id;
	private String docs_name;
	private String docs_directory;
	private String docs_sizedesc;

	public BjxxxxEntity() {
		super();
	}

	public BjxxxxEntity(String logs_stepname, String logs_actorid_name,
			String logs_remark) {
		super();
		this.logs_stepname = logs_stepname;
		this.logs_actorid_name = logs_actorid_name;
		this.logs_remark = logs_remark;
	}

	public String getStepname() {
		return stepname;
	}

	public void setStepname(String stepname) {
		this.stepname = stepname;
	}

	public String getStarttime() {
		return starttime;
	}

	public void setStarttime(String starttime) {
		this.starttime = starttime;
	}

	public String getEndtime() {
		return endtime;
	}

	public void setEndtime(String endtime) {
		this.endtime = endtime;
	}

	public String getLogs_stepname() {
		return logs_stepname;
	}

	public void setLogs_stepname(String logs_stepname) {
		this.logs_stepname = logs_stepname;
	}

	public String getLogs_actorid_name() {
		return logs_actorid_name;
	}

	public void setLogs_actorid_name(String logs_actorid_name) {
		this.logs_actorid_name = logs_actorid_name;
	}

	public String getLogs_remark() {
		return logs_remark;
	}

	public void setLogs_remark(String logs_remark) {
		this.logs_remark = logs_remark;
	}

	public String getDocs_id() {
		return docs_id;
	}

	public void setDocs_id(String docs_id) {
		this.docs_id = docs_id;
	}

	public String getDocs_name() {
		return docs_name;
	}

	public void setDocs_name(String docs_name) {
		this.docs_name = docs_name;
	}

	public String getDocs_directory() {
		return docs_directory;
	}

	public void setDocs_directory(String docs_directory) {
		this.docs_directory = docs_directory;
	}

	public String getDocs_sizedesc() {
		return docs_sizedesc;
	}

	public void setDocs_sizedesc(String docs_sizedesc) {
		this.docs_sizedesc = docs_sizedesc;
	}

}
