package com.zq.xinjiang.approval.utils;

import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class MSimpleHttpUtil {

	private static HttpClient mHttpClient;
	private static int TimeOut = 5000;
	private static int MaxTotalConnections = 8;

	/**
	 * 测试ConnectivityManager ConnectivityManager主要管理和网络连接相关的操作
	 * 相关的TelephonyManager则管理和手机、运营商等的相关信息；WifiManager则管理和wifi相关的信息。
	 * 想访问网络状态，首先得添加权限<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"/>
	 * NetworkInfo类包含了对wifi和mobile两种网络模式连接的详细描述,通过其getState()方法获取的State对象则代表着连接成功与否等状态。
	 */
	public static boolean isCheckNet(Context context) {
		try {
			// 获取系统的连接服务
			ConnectivityManager connectivity = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			if (connectivity != null) {
				// 获取代表联网状态的NetWorkInfo对象
				NetworkInfo info = connectivity.getActiveNetworkInfo();
				if (info != null && info.isConnected()) {
					// 网络是否可用
					if (info.getState() == NetworkInfo.State.CONNECTED) {
						return true;
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return false;
	}

	/**
	 * 根据手机分辨率从DP转成PX
	 * @param context
	 * @param dpValue
	 * @return
	 */
	public static int dip2px(Context context, float dpValue) {
		float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}

	public static synchronized HttpClient getHttpClient() {
		if (mHttpClient == null) {
			HttpParams params = new BasicHttpParams();
			// HTTP 协议的版本,1.1/1.0/0.9
			HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
			// 字符集
			HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);
			// 这定义了从ConnectionManager管理的连接池中取出连接的超时时间
			ConnManagerParams.setTimeout(params, TimeOut);
			// 设置最大连接数
			ConnManagerParams.setMaxTotalConnections(params,
					MaxTotalConnections);
			// 连接超时,这定义了通过网络与服务器建立连接的超时时间。Httpclient包中通过一个异步线程去创建与服务器的socket连接，这就是该socket连接的超时时间
			HttpConnectionParams.setConnectionTimeout(params, TimeOut);
			// 请求超时,这定义了Socket读数据的超时时间，即从服务器获取响应数据需要等待的时间
			HttpConnectionParams.setSoTimeout(params, TimeOut);
			// 设置访问协议
			SchemeRegistry schReg = new SchemeRegistry();
			schReg.register(new Scheme("http", PlainSocketFactory
					.getSocketFactory(), 80));
			// 使用线程安全的连接管理来创建HttpClient
			ClientConnectionManager conMgr = new ThreadSafeClientConnManager(
					params, schReg);

			mHttpClient = new DefaultHttpClient(conMgr, params);
		}
		return mHttpClient;
	}

	public static HttpResponse httpGet(String url) {
		try {
			HttpClient httpClient = getHttpClient();
			// 设置为get取连接的方式
			HttpGet httpGet = new HttpGet(url);
			// 得到返回的response
			HttpResponse response = httpClient.execute(httpGet);
			return response;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
