package com.zq.xinjiang.government.tool;

/**
 * Created by Administrator on 2017/9/19.
 */

public class Allports {

    //get http://192.168.1.103/heyang/webservices/Json.aspx?参数名=参数值
    //Post http://192.168.1.103/heyang/webservices/Json.aspx
//    public static String getPorts = "http://218.200.12.18:8080/webservices/Json.aspx?";//合阳
    public static String getPorts = "http://117.34.72.11/zwfw/webservices/json.aspx?";

//    public static String ipAddress = "http://192.168.1.103/heyang";
//    public static String ipAddress = "http://218.200.12.18:8080";//合阳
    public static String ipAddress = "http://117.34.72.11/zwfw";//文件下载地址

    //通知公告，中心简介，中心动态，政策法规
//    private static final String getPortsL = "http://192.168.1.95:8082/hycms/service/server/getResult.do?";
    private static final String getPortsL = "http://www.hyxbmzx.com/service/server/getResult.do?";//外网
//                                             http://www.hyxbmzx.com/service/server/getResult.do?type=news&classid=1
    //http://192.168.1.95:8082/hycms/service/server/getResult.do?type=news&classid=2
    /**
     * 事项列表
     * @param mod
     * @param act
     * @param pagesize
     * @param pageindex
     * @param id
     * @param orgid
     * @param acceptobjecttype
     * @param acceptobject
     * @param itemproperty
     * @param itemname
     * @param issports
     * @param islocked
     * @param hot
     * @return
     */
    public static String getItemsListPorts(String mod,String act,String pagesize,String pageindex,String id,String orgid ,
                                            String acceptobjecttype,String acceptobject,String itemproperty,String itemname,String issports,
                                            String islocked,String hot){

        String url = getPorts + "mod="+mod +"&act="+act+"&pagesize="+pagesize+"&pageindex="+pageindex+"&id="+id+"&orgid="+orgid+"&acceptobjecttype="+acceptobjecttype+
                        "&acceptobject="+acceptobject+"&itemproperty="+itemproperty+"&itemname="+itemname+"&issports="+issports+"&islocked="+islocked+"&hot="+hot;

        return url;
    }

    /**
     * 热点事项列表
     * @param mod
     * @param act
     * @param pagesize
     * @param pageindex
     * @return
     */
    public static String getHotItemsListPorts(String mod,String act,String pagesize,String pageindex,String acceptobjecttype){

//        String url = getPorts + "mod="+mod +"&act="+act+"&pagesize="+pagesize+"&pageindex="+pageindex +"&hot="+hot+"&userid="+userid;
        String url = getPorts + "mod="+mod +"&act="+act+"&pagesize="+pagesize+"&pageindex="+pageindex +"&acceptobjecttype="+acceptobjecttype;
        return url;
    }

    /**
     * 办件公示列表
     * @param mod
     * @param act
     * @param pagesize
     * @param pageindex
     * @return
     */
    public static String getDoShowListPorts(String mod,String act,String pagesize,String pageindex,String state){
        String url = getPorts + "mod="+mod +"&act="+act+"&pagesize="+pagesize+"&pageindex="+pageindex +"&state="+state;
        return url;
    }

    /**
     * 个人/企业列表
     * @param mod
     * @param act
     * @param pagesize
     * @param pageindex
     * @param acceptobjecttype
     * @param acceptobject
     * @param userid
     * @return
     */
    public static String getItemsListUrl(String mod,String act,String pagesize,String pageindex,
                                           String acceptobjecttype,String acceptobject,String userid){

        String url = getPorts + "mod="+mod +"&act="+act+"&pagesize="+pagesize+"&pageindex="+pageindex+"&acceptobjecttype="+acceptobjecttype+
                "&acceptobject="+acceptobject+  "&userid="+userid;

        return url;
    }

    /**
     * 部门事项列表
     * @param mod
     * @param act
     * @param pagesize
     * @param pageindex
     * @param orgid
     * @param userid
     * @return
     */
    public static String getItemListUrlDepart(String mod,String act,String pagesize,String pageindex,String orgid,String userid){

        String url = getPorts + "mod="+mod +"&act="+act+"&pagesize="+pagesize+"&pageindex="+pageindex+"&orgid="+orgid+"&userid="+userid;

        return url;
    }

    /**
     * 事项详情
     * @param mod
     * @param act
     * @param id
     * @return
     */
    public static String getItemsDetailsUrl(String mod,String act,String id){

        String url = getPorts + "mod="+mod +"&act="+act+ "&id="+id;

        return url;
    }

    /**
     * 添加收藏
     * @param userid
     * @param orgname
     * @param itemdefid
     * @param itemname
     * @param itemcode
     * @return
     */
    public static String getCollectUrl(String userid,String orgname,String itemdefid,String itemname,String itemcode) {

        String url = getPorts + "mod=op&act=collectionofsave" + "&userid=" + userid+"&orgname=" + orgname+"&itemdefid=" +
                itemdefid+"&itemname=" + itemname+"&itemcode=" + itemcode;

        return url;
    }

    /**
     * 取消收藏
     * @param userid
     * @param orgname
     * @param itemdefid
     * @param itemname
     * @param itemcode
     * @param iscollection
     * @return
     */
    public static String getCancelCollectUrl(String userid,String orgname,String itemdefid,String itemname,String itemcode,String iscollection) {

        String url = getPorts + "mod=op&act=collectionofsave" + "&userid=" + userid+"&orgname=" + orgname+"&itemdefid=" +
                itemdefid+"&itemname=" + itemname+"&itemcode=" + itemcode+"&iscollection="+iscollection;

        return url;
    }

    /**
     * 在线办理上报
     * @param mod
     * @param act
     * @param id
     * @param iteminstanceid
     * @param itemdefid
     * @param username
     * @param identitycode
     * @param linkmobile
     * @param address
     * @param docs
     * @return
     */
    public static String getOnlineManageUrl(String mod,String act,String id,String iteminstanceid,String itemdefid,String username,
                                            String identitycode,String linkmobile,String address,String docs) {

        String url = getPorts + "mod=" + mod + "&act=" + act + "&id=" + id+"&iteminstanceid=" + iteminstanceid+"&itemdefid=" +
                itemdefid+"&username=" + username+"&identitycode=" + identitycode+"&linkmobile="+linkmobile+"&address="+address+"&docs="+docs;

        return url;
    }

    /**
     * 部门列表
     * @param mod
     * @param act
     * @return
     */

    public static String getDepartmentUrl(String mod,String act) {

        String url = getPorts + "mod=" + mod + "&act=" + act ;

        return url;
    }

    /**
     * 网上预约
     * @param mod
     * @param act
     * @param orgid
     * @param itemdefid
     * @param starttime
     * @param endtime
     * @param username
     * @param realname
     * @param cardid
     * @param mobilephone
     * @return
     */
    public static String getOnlineBookingUrl(String mod,String act,String orgid,String itemdefid,String starttime,String endtime,
                                          String username,String realname,String cardid,String mobilephone) {

        String url = getPorts + "mod=" + mod + "&act=" + act +"&orgid="+orgid+"&itemdefid="+itemdefid+"&starttime="+starttime+
                "&endtime="+endtime+"&username="+username+"&cardid="+cardid +"&realname="+realname + "&mobilephone="+mobilephone;

        return url;
    }

    /**
     * 192.168.1.103/heyang/webservices/Json.aspx?mod=api&act=getappointed&orgid=2833
     * @return
     */
    public static String getOnlineBookTimeUrl(String mod,String act,String orgid){
        String url = getPorts + "mod=" + mod + "&act=" + act +"&orgid="+orgid;

        return url;
    }

    /**
     * 我的办件
     * @param mod
     * @param act
     * @param id
     * @return
     */
    public static String getMyThingUrl(String mod,String act,String id,String pagesize,String pageindex) {

        String url = getPorts + "mod=" + mod + "&act=" + act + "&userid="+id+ "&pagesize="+pagesize+ "&pageindex="+pageindex;

        return url;
    }

    //http://192.168.1.103/heyang/webservices/Json.aspx?mod=sp&act=getinstance&iteminstanceid=153709

    /**
     * 我的办件详情
     * @param mod
     * @param act
     * @param iteminstanceid
     * @return
     */
    public static String getMyThingDetailsUrl(String mod,String act,String iteminstanceid) {

        String url = getPorts + "mod=" + mod + "&act=" + act + "&iteminstanceid="+iteminstanceid;

        return url;
    }

    /**
     * 我的收藏查询
     * @param mod
     * @param act
     * @param userid
     * @return
     */
    public static String getMyCollectionUrl(String mod,String act,String userid,String pagesize,String pageindex) {

        String url = getPorts + "mod=" + mod + "&act=" + act + "&userid="+userid+ "&pagesize="+pagesize+ "&pageindex="+pageindex;

        return url;
    }

    /**
     * 我的收藏详细查询
     * @param mod
     * @param act
     * @param id
     * @return
     */
    public static String getMyCollectionDetailsUrl(String mod,String act,String id) {
        String url = getPorts + "mod=" + mod + "&act=" + act + "&id="+id;

        return url;
    }

    /**
     * 我的预约列表
     * @param mod
     * @param act
     * @param userid
     * @return
     */
    public static String getMySubUrl(String mod,String act,String userid,String pagesize,String pageindex,String cardid) {
        String url = getPorts + "mod=" + mod + "&act=" + act + "&userid="+userid + "&pagesize="+pagesize + "&pageindex="+pageindex+"&cardid="+cardid;

        return url;
    }

    /**
     * 取消预约
     * @param mod
     * @param act
     * @param id
     * @return
     */
    public static String getMySubCancelUrl(String mod,String act,String id) {
        String url = getPorts + "mod=" + mod + "&act=" + act + "&id="+id;

        return url;
    }

    /**
     * 咨询投诉结果列表
     * @param mod
     * @param act
     * @param userid
     * @return
     */
    public static String getMyConsultUrl(String mod,String act,String userid,String pagesize,String pageindex,String phone) {
        String url = getPorts + "mod=" + mod + "&act=" + act + "&userid="+userid + "&pagesize="+pagesize + "&pageindex="+pageindex+"&linkmobile="+phone;

        return url;
    }

    /**
     * 评价列表
     * @param mod
     * @param act
     * @param userid
     * @param state
     * @param pingjia
     * @return
     */
    public static String getMyEvaluateUrl(String mod,String act,String userid,String state,String pingjia,String score,String pagesize,String pageindex) {
        String url = getPorts + "mod=" + mod + "&act=" + act + "&userid="+userid+"&state="+state+"&pingjia="+pingjia+"&score="+score+"&pagesize="+
                pagesize+"&pageindex="+pageindex;

        return url;
    }

    /**
     * 我的消息结果列表
     * @param mod
     * @param act
     * @param userid
     * @return
     */
    public static String getMyMessageUrl(String mod,String act,String userid,String pagesize,String pageindex) {
        String url = getPorts + "mod=" + mod + "&act=" + act + "&userid="+userid + "&pagesize="+pagesize + "&pageindex="+pageindex;

        return url;
    }

    /**
     * 咨询投诉提交信息
     * @param mod
     * @param act
     * @param orgid
     * @param orgname
     * @param itemid
     * @param itemname
     * @param username
     * @param phone
     * @param useremail
     * @param biztype
     * @param asktitle
     * @param askcontent
     * @return
     */
    public static String getCCUrl(String mod,String act,String orgid,String orgname,String itemid,String itemname,String username,
                                  String phone,String useremail,String biztype,String asktitle,String askcontent) {
        String url = getPorts + "mod=" + mod + "&act=" + act + "&orgid="+orgid+ "&orgname="+orgname+ "&itemid="+itemid+ "&itemname="+itemname
                + "&username="+username+ "&userphone="+phone+ "&useremail="+useremail+ "&biztype="+biztype+ "&asktitle="+asktitle+ "&askcontent="
                +askcontent;

        return url;
    }

    /**
     * 注册
     * @param mod
     * @param act
     * @param loginname
     * @param password
     * @param username
     * @param identitycode
     * @param linkmobile
     * @param address
     * @param usertype
     * @return
     */
    public static String getRegisterUrl(String mod,String act,String loginname,String password,String username,String identitycode,String linkmobile,
                                  String address,String usertype,String email) {
        String url = getPorts + "mod=" + mod + "&act=" + act + "&loginname="+loginname+ "&password="+password+ "&username="+username+ "&identitycode="
                +identitycode + "&linkmobile="+linkmobile+ "&address="+address+ "&usertype="+usertype+ "&iscertify=true"+"&email="+email;

        return url;
    }

    /**
     * 登录
     * @param mod
     * @param act
     * @param loginname
     * @param password
     * @return
     */
    public static String getLoginUrl(String mod,String act,String loginname,String password) {
        String url = getPorts + "mod=" + mod + "&act=" + act + "&username="+loginname+ "&password="+password;

        return url;
    }

    /**
     * 修改个人信息
     * @param mod
     * @param act
     * @param loginname
     * @param id
     * @param identitycode
     * @param linkmobile
     * @param address
     * @param username
     * @return
     */
    public static String updateMyInfoUrl(String mod,String act,String loginname,String id,String identitycode,String linkmobile,String address,String username,String usertype,String email) {
        String url = getPorts + "mod=" + mod + "&act=" + act + "&loginname="+loginname+ "&id="+id+ "&identitycode="+identitycode+ "&linkmobile="+linkmobile
                + "&address="+address+ "&username="+username+"&usertype="+usertype+"&email="+ email;

        return url;
    }

    /**
     * 获取用户信息
     * @param mod
     * @param act
     * @param id
     * @return
     */
    public static String getMyInfoUrl(String mod,String act,String id) {
        String url = getPorts + "mod=" + mod + "&act=" + act + "&id="+id;

        return url;
    }

    /**
     * 修改密码
     * @param mod
     * @param act
     * @param id
     * @param spassword
     * @param password
     * @return
     */
    public static String getUpdatePWUrl(String mod,String act,String id,String spassword,String password) {
        String url = getPorts + "mod=" + mod + "&act=" + act + "&id="+id+ "&spassword="+spassword+ "&password="+password;

        return url;
    }

    /**
     * 评价
     * @param mod
     * @param act
     * @param code
     * @param starttime
     * @param score
     * @return
     */
    public static String getEvaluateUrl(String mod,String act,String code,String starttime,String score) {
        String url = getPorts + "mod=" + mod + "&act=" + act + "&code="+code+ "&starttime="+starttime+ "&score="+score;

        return url;
    }

    /**
     * 进度查询
     * @param mod
     * @param act
     * @param field
     * @param content
     * @param pagesize
     * @param pageindex
     * @return
     */
    public static String getProgressUrl(String mod,String act,String field,String content,String pagesize,String pageindex) {
        String url = getPorts + "mod=" + mod + "&act=" + act + "&field="+field+ "&content="+ content + "&pagesize="+pagesize+ "&pageindex="+pageindex;
        return url;
    }

    //http://192.168.1.95:8082/hycms/service/server/getResult.do?type=news&classid=   0通知公告，1政策解读，2中心动态，3中心简介

    /**
     * 0通知公告，1政策解读，2中心动态，3中心简介
     * @param id
     * @return
     */

    public static String getOnlineUrl(String id,String pagesize,String pageindex){
        String url = getPortsL + "type=news&classid="+id+"&pagesize="+pagesize+"&pageindex="+pageindex;
        return url;
    }

    /**
     * 0通知公告，1政策解读，2中心动态，3中心简介   详情
     * @param id
     * @return
     */
    public static String getOnlineDetailsUrl(String id){
        String url = getPortsL + "type=info&id="+id;
        return url;
    }
}
