package com.zq.xinjiang.government.entity;

/**
 * Created by Administrator on 2017/9/19.
 */

public class ItemsList {

    private String rowid;//列表编号
    private String id;//事项id
    private String orgid;//部门id
    private String itemcode;//事项编码
    private String floor;//楼层
    private String windowid;//窗口
    private String itemName;//事项名称
    private String itemType;//事项类型
    private String acceptObject;//接收对象
    private String acceptObjectType;//接收对象类型
    private String onlineType;//在线类型
    private String orgname;//部门名称
    private String isOnline;//是否在线办理
    private String isAppoint;//是否委托办理
    private String iscollection;//是否收藏


    public String getItemcode() {
        return itemcode;
    }

    public void setItemcode(String itemcode) {
        this.itemcode = itemcode;
    }

    public String getIscollection() {
        return iscollection;
    }

    public void setIscollection(String iscollection) {
        this.iscollection = iscollection;
    }

    public String getRowid() {
        return rowid;
    }

    public void setRowid(String rowid) {
        this.rowid = rowid;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrgid() {
        return orgid;
    }

    public void setOrgid(String orgid) {
        this.orgid = orgid;
    }

    public String getFloor() {
        return floor;
    }

    public void setFloor(String floor) {
        this.floor = floor;
    }

    public String getWindowid() {
        return windowid;
    }

    public void setWindowid(String windowid) {
        this.windowid = windowid;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemType() {
        return itemType;
    }

    public void setItemType(String itemType) {
        this.itemType = itemType;
    }

    public String getAcceptObject() {
        return acceptObject;
    }

    public void setAcceptObject(String acceptObject) {
        this.acceptObject = acceptObject;
    }

    public String getAcceptObjectType() {
        return acceptObjectType;
    }

    public void setAcceptObjectType(String acceptObjectType) {
        this.acceptObjectType = acceptObjectType;
    }

    public String getOnlineType() {
        return onlineType;
    }

    public void setOnlineType(String onlineType) {
        this.onlineType = onlineType;
    }

    public String getOrgname() {
        return orgname;
    }

    public void setOrgname(String orgname) {
        this.orgname = orgname;
    }

    public String getIsOnline() {
        return isOnline;
    }

    public void setIsOnline(String isOnline) {
        this.isOnline = isOnline;
    }

    public String getIsAppoint() {
        return isAppoint;
    }

    public void setIsAppoint(String isAppoint) {
        this.isAppoint = isAppoint;
    }


}
