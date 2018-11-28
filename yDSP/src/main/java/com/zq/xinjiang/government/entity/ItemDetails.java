package com.zq.xinjiang.government.entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/9/19.
 */

public class ItemDetails implements Parcelable{

    private String itemcode;
    private String itemproperty;
    private String itemname;
    private String windowid;
    private String orgname;
    private String itemtype;
    private String limittime;
    private String limitlegaltime;
    private String orgphone;
    private String requirements;//申报条件
    private String materials;//申报材料
    private String applypursuant;//审批依据
    private String chargepursuant;
    private List<Map<String,String>> docs;

    public ItemDetails() {
    }

    public ItemDetails(Parcel in) {
        itemcode = in.readString();
        itemproperty = in.readString();
        itemname = in.readString();
        windowid = in.readString();
        orgname = in.readString();
        itemtype = in.readString();
        limittime = in.readString();
        limitlegaltime = in.readString();
        orgphone = in.readString();
        requirements = in.readString();
        materials = in.readString();
        applypursuant = in.readString();
        chargepursuant = in.readString();
    }

    public static final Creator<ItemDetails> CREATOR = new Creator<ItemDetails>() {
        @Override
        public ItemDetails createFromParcel(Parcel in) {
            return new ItemDetails(in);
        }

        @Override
        public ItemDetails[] newArray(int size) {
            return new ItemDetails[size];
        }
    };

    public void setDocs(List<Map<String, String>> docs) {
        this.docs = docs;
    }

    public List<Map<String,String>> getDocs(){
        return docs;
    }

    public String getItemname() {
        return itemname;
    }

    public void setItemname(String itemname) {
        this.itemname = itemname;
    }

    public String getItemcode() {
        return itemcode;
    }

    public void setItemcode(String itemcode) {
        this.itemcode = itemcode;
    }

    public String getItemproperty() {
        return itemproperty;
    }

    public void setItemproperty(String itemproperty) {
        this.itemproperty = itemproperty;
    }

    public String getWindowid() {
        return windowid;
    }

    public void setWindowid(String windowid) {
        this.windowid = windowid;
    }

    public String getOrgname() {
        return orgname;
    }

    public void setOrgname(String orgname) {
        this.orgname = orgname;
    }

    public String getItemtype() {
        return itemtype;
    }

    public void setItemtype(String itemtype) {
        this.itemtype = itemtype;
    }

    public String getLimittime() {
        return limittime;
    }

    public void setLimittime(String limittime) {
        this.limittime = limittime;
    }

    public String getLimitlegaltime() {
        return limitlegaltime;
    }

    public void setLimitlegaltime(String limitlegaltime) {
        this.limitlegaltime = limitlegaltime;
    }

    public String getOrgphone() {
        return orgphone;
    }

    public void setOrgphone(String orgphone) {
        this.orgphone = orgphone;
    }

    public String getRequirements() {
        return requirements;
    }

    public void setRequirements(String requirements) {
        this.requirements = requirements;
    }

    public String getMaterials() {
        return materials;
    }

    public void setMaterials(String materials) {
        this.materials = materials;
    }

    public String getApplypursuant() {
        return applypursuant;
    }

    public void setApplypursuant(String applypursuant) {
        this.applypursuant = applypursuant;
    }

    public String getChargepursuant() {
        return chargepursuant;
    }

    public void setChargepursuant(String chargepursuant) {
        this.chargepursuant = chargepursuant;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(itemcode);
        dest.writeString(itemproperty);
        dest.writeString(itemname);
        dest.writeString(windowid);
        dest.writeString(orgname);
        dest.writeString(itemtype);
        dest.writeString(limittime);
        dest.writeString(limitlegaltime);
        dest.writeString(orgphone);
        dest.writeString(requirements);
        dest.writeString(materials);
        dest.writeString(applypursuant);
        dest.writeString(chargepursuant);
    }
}
