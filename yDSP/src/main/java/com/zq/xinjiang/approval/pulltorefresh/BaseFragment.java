package com.zq.xinjiang.approval.pulltorefresh;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public abstract class BaseFragment extends Fragment{
	protected ContentPage contentPage;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if(contentPage==null){
//			LogUtil.e(this, "fragment create contentPage: "+this.getClass().getSimpleName());
			contentPage = new ContentPage(getActivity()) {
				
				@Override
				public View createSuccessView() {
					return getSuccessView();
				}
				
				@Override
				public Object loadData() {
					return requestData();
				}
			};
		}else {
//			LogUtil.e(this, this.getClass().getSimpleName()+ " ： 使用了已经创建的ContentPage");
			//将ContentPage从ViewPager中移除
			CommonUtil.removeSelfFromParent(contentPage);
		}
		return contentPage;
	}
	
	/**
	 * 子类去实现获取成功的View
	 * @return
	 */
	protected abstract View getSuccessView();
	
	/**
	 * 子类去实现请求数据的过程
	 * @return
	 */
	protected abstract Object requestData();
}
