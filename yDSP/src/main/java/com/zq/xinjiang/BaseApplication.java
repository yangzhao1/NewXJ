package com.zq.xinjiang;

import java.util.ArrayList;

import android.app.Activity;
import android.app.Application;

import com.zq.xinjiang.approval.utils.CrashHandler;

public class BaseApplication extends Application {

	private static BaseApplication instance;
	private ArrayList<Activity> activities = new ArrayList<Activity>();
	private Activity g_mainActivity;

	@Override
	public void onCreate() {
		super.onCreate();
		CrashHandler mCustomCrashHandler = CrashHandler.getInstance();
		mCustomCrashHandler.setCustomCrashHanler(getApplicationContext());
	}

	public synchronized static BaseApplication getInstance() {
		if (instance == null) {
			instance = new BaseApplication();
		}
		return instance;
	}

	public void addActivity(Activity activity) {
		activities.add(activity);
	}

	public void setG_mainActivity(Activity activity){
		g_mainActivity = activity;
	}

	//主界面关闭
	public void g_MainActivityExit(){
		if (g_mainActivity!=null){
			g_mainActivity.finish();
		}
	}

	public void exit() {
		try {
			for (Activity activity : activities) {
				if (activity != null) {
					activity.finish();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			System.exit(0);
		}
	}

	@Override
	public void onLowMemory() {
		super.onLowMemory();
		System.gc();
	}
}
