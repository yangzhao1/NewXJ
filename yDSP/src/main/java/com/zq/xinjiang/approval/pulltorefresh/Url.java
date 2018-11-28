package com.zq.xinjiang.approval.pulltorefresh;

public interface Url {
	
	// 服务器主机
	String SERVER_HOST = "http://192.168.1.117:8080/";

	// home页的接口
	String Dbsx = SERVER_HOST
			+ "wb/webservices/Json.aspx?mod=mp&act=getinstancelist&status=yiban&pagesize=2&orgid=834&pageindex=";
}
