package com.zq.xinjiang.approval.pulltorefresh;

import android.app.Application;
import android.content.Context;
import android.os.Handler;

public class GooglePlayApplication extends Application{
	private static Context context;
	private static Handler mainHandler;//主线程的handler
	@Override
	public void onCreate() {
		super.onCreate();
		
		context = this;
		
		mainHandler = new Handler();
		
//		initImageLoader(context);
		
	}
//	/**
//	 * 初始化ImageLoader
//	 * @param context
//	 */
//	public static void initImageLoader(Context context) {
//		// This configuration tuning is custom. You can tune every option, you may tune some of them,
//		// or you can create default configuration by
//		//  ImageLoaderConfiguration.createDefault(this);
//		// method.
//		ImageLoaderConfiguration.Builder config = new ImageLoaderConfiguration.Builder(context);
//		config.threadPriority(Thread.NORM_PRIORITY - 2);
//		config.denyCacheImageMultipleSizesInMemory();
//		config.diskCacheFileNameGenerator(new Md5FileNameGenerator());
//		config.diskCacheSize(50 * 1024 * 1024); // 50 MiB
//		config.tasksProcessingOrder(QueueProcessingType.LIFO);
//		config.writeDebugLogs(); // Remove for release app
//
//		// Initialize ImageLoader with configuration.
//		ImageLoader.getInstance().init(config.build());
//	}
	
	public static Context getContext(){
		return context;
	}
	
	public static Handler getHandler(){
		return mainHandler;
	}
}
