package com.zq.xinjiang.approval.pulltorefresh;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import com.zq.xinjiang.approval.utils.LogUtil;

public class HttpHelper {
	private static String tag = "HttpHelper";
	/**
	 * get请求
	 * @param url
	 * @return
	 */
	public static String get(String sessionid, String url){
		LogUtil.recordLog("tag：" + "请求的url: "+url);
		String result = "";
		HttpClient httpClient = new DefaultHttpClient();
		HttpGet httpGet = new HttpGet(url);
		httpGet.addHeader("cookie", "ASP.NET_SessionId="
				+ sessionid);
		try {
			HttpResponse httpResponse = httpClient.execute(httpGet);
			if(httpResponse.getStatusLine().getStatusCode()<300){
				//说明服务器响应成功
				HttpEntity entity = httpResponse.getEntity();//获取响应体
				InputStream is = entity.getContent();//获取响应体的流对象
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				
				byte[] buffer = new byte[1024];//1k的缓冲区
				int len = -1;//用来记录每次读取到的长度
				while((len=is.read(buffer))!=-1){
					baos.write(buffer, 0, len);
					baos.flush();//保证缓冲区的字节都刷到输出流
				}
				//关闭流和链接
				is.close();
				baos.close();//其实不用关
				httpClient.getConnectionManager().closeExpiredConnections();//关闭链接
				
				result = new String(baos.toByteArray(), "utf-8");
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		LogUtil.recordLog("tag：" + "响应的response: "+result);
		return result;
	}
}
