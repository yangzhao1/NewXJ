package com.zq.xinjiang.government.entity;

/**
 * Created by Administrator on 2017/9/21.
 * 部门实体类
 */

public class Department {

    private String id;
    private String orgname;
    private String orgpicurl;
    private String norgpicurl;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrgname() {
        return orgname;
    }

    public void setOrgname(String orgname) {
        this.orgname = orgname;
    }

    public String getOrgpicurl() {
        return orgpicurl;
    }

    public void setOrgpicurl(String orgpicurl) {
        this.orgpicurl = orgpicurl;
    }

    public String getNorgpicurl() {
        return norgpicurl;
    }

    public void setNorgpicurl(String norgpicurl) {
        this.norgpicurl = norgpicurl;
    }
}
