package com.zq.xinjiang.approval.utils;

import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;

public class PicUtils {

	/**
	 * 加载图片时的回调
	 * 
	 */
	public interface OnLoadImageListener {
		public void onLoadImage(Bitmap bm, String imageUrl);
	}

	/**
	 * 加载图片
	 * 
	 * @param url 图片的url
	 * @param listener 回调监听器
	 */
	public void loadImage(final String url, final OnLoadImageListener listener) {
		if (null == url || null == listener) {
			return;
		}

		final Handler handler = new Handler() {
			public void handleMessage(Message msg) {
				listener.onLoadImage((Bitmap) msg.obj, url);
			}
		};

		// 之前根据url写入本地缓存的路径
		String path = "";
		File file = new File(path);
		if (file.exists()) {
			Bitmap bm = BitmapFactory.decodeFile(path);
			sendMessage(handler, bm);
			return;
		}

		new Thread(new Runnable() {
			public void run() {
				try {
					// 网络加载图片，还可以加入延迟(time out)条件
					URL u = new URL(url);
					HttpURLConnection httpConnection = (HttpURLConnection) u
							.openConnection();
					if (httpConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
						Bitmap bm = BitmapFactory.decodeStream(httpConnection
								.getInputStream());
						sendMessage(handler, bm);
						// 同时对图片进行缓存...
						return;
					}

					// 没有请求到图片
					sendMessage(handler, null);
				} catch (MalformedURLException e) {
					sendMessage(handler, null);
				} catch (IOException e) {
					sendMessage(handler, null);
				}
			}
		}).start();
	}

	/**
	 * 向handler发送处理的消息
	 * 
	 * @param handler
	 * @param bm
	 */
	private void sendMessage(Handler handler, Bitmap bm) {
		// Message有两种获取方法
		// Message msg = new Message();
		// 或者
		// Message msg = handler.obtainMessage();
		// 这两种方法的区别是，前者是new的，需要开辟内存空间；后取者是从global Message pool中，性能消耗相对少
		Message msg = handler.obtainMessage();
		msg.obj = bm;
		handler.sendMessage(msg);
	}

}
