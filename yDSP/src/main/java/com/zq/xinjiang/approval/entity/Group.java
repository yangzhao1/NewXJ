package com.zq.xinjiang.approval.entity;

import java.util.ArrayList;

public class Group {

	private String orgname;
//	public ArrayList<Child> children ;
	private boolean isChecked;
	public ArrayList<Child> childrens;

	public Group(String orgname) {
		this.orgname = orgname;
//		children = new ArrayList<Child>();
		childrens = new ArrayList<Child>();
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

	public String getOrgname() {
		return orgname;
	}

	public void addChildrenItem(Child childs) {
		childrens.add(childs);
	}
	
//	public ArrayList<Child> getChilds() {
//		return children;
//	}

	public int getChildrenCount() {
		return childrens.size();
	}

	public Child getChildItems(int index) {
		return childrens.get(index);
	}
//	
//	public Child getChildItem(int index) {
//		return children.get(index);
//	}

}
