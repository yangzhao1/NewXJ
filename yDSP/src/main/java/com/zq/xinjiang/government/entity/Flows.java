package com.zq.xinjiang.government.entity;

/**
 * Created by Administrator on 2017/9/27.
 * 我的办件详情 流程
 */

public class Flows {


    private String stepname;
    private String finishtime;
    private String actorid_name;
    private String remark;
    private String isApprove;//是否审批


    public String getIsApprove() {
        return isApprove;
    }

    public void setIsApprove(String isApprove) {
        this.isApprove = isApprove;
    }

    public String getStepname() {
        return stepname;
    }

    public void setStepname(String stepname) {
        this.stepname = stepname;
    }

    public String getFinishtime() {
        return finishtime;
    }

    public void setFinishtime(String finishtime) {
        this.finishtime = finishtime;
    }

    public String getActorid_name() {
        return actorid_name;
    }

    public void setActorid_name(String actorid_name) {
        this.actorid_name = actorid_name;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
