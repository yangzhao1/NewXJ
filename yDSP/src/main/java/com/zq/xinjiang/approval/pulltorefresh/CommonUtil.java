package com.zq.xinjiang.approval.pulltorefresh;

import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

/**
 * 放些杂碎的方法
 * @author Administrator
 *
 */
public class CommonUtil {
	/**
	 * 在主线程中更新UI
	 * @param runnable
	 */
   public static void runOnUIThread(Runnable runnable){
	   GooglePlayApplication.getHandler().post(runnable);
   }
   
   /**
    * 获取字符串资源
    * @param resId
    * @return
    */
   public static String getString(int resId){
	   return GooglePlayApplication.getContext().getResources().getString(resId);
   }
   
   /**
    * 获取图片资源
    * @param resId
    * @return
    */
   public static Drawable getDrawable(int resId){
	   return GooglePlayApplication.getContext().getResources().getDrawable(resId);
   }
   
   /**
    * 获取字符串数组资源
    * @param resId
    * @return
    */
   public static String[] getStringArray(int resId){
	   return GooglePlayApplication.getContext().getResources().getStringArray(resId);
   }
   
   /**
    * 获取dp资源,返回的是转换后的像素值
    * @param resId
    * @return
    */
   public static float getDimen(int resId){
	   return GooglePlayApplication.getContext().getResources().getDimension(resId);
   }
   
   /**
    * 将指定childView从它的父View中移除
    * @param child
    */
   public static void removeSelfFromParent(View child){
	   if(child!=null){
		   ViewParent parent = child.getParent();
		   if(parent instanceof ViewGroup){
			   ViewGroup group = (ViewGroup) parent;
			   group.removeView(child);//将child从父view当中移除
		   }
	   }
   }
}
