package com.zq.xinjiang.government.entity;

/**
 * Created by Administrator on 2017/9/26.
 */

public class Things {

    private String id;//事项id
    private String number;//流水号
    private String itemName;
    private String startTime;//申请时间
    private String thingState;//办理状态
    private String orgname;//部门
    private String score;//评分

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getOrgname() {
        return orgname;
    }

    public void setOrgname(String orgname) {
        this.orgname = orgname;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getThingState() {
        return thingState;
    }

    public void setThingState(String thingState) {
        this.thingState = thingState;
    }
}
