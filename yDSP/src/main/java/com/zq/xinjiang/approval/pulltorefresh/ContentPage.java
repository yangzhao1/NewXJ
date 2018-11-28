package com.zq.xinjiang.approval.pulltorefresh;

import java.util.List;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import com.zq.xinjiang.R;
import com.zq.xinjiang.approval.progressdialog.CustomProgressDialog;

/**
 * 根据页面的加载状态显示不同的view
 * @author Administrator
 *
 */
public abstract class ContentPage extends FrameLayout{
	private View loadingView;//加载中的View
	private View errorView;//加载失败的View
	private View emptyView;//加载数据为空的View
	private View successView;//加载成功的View
	
	private PageState mState = PageState.STATE_LOADING;//默认的初始化状态,默认是加载中的状态
	public int currValue=0;

	/**
	 * 定义页面状态常量类
	 * @author Administrator
	 *
	 */
	enum PageState{
		STATE_LOADING(0),//加载中的状态
		STATE_SUCCESS(1),//加载成功的状态
		STATE_ERROR(2),//加载失败的状态
		STATE_EMPTY(3);//加载数据为空的状态
		
		private int value;
		PageState(int value){
			this.value = value;
		}

		public int getValue(){
			return this.value;
		}
	}

	public int getValue(){
		return currValue;
	}

	public ContentPage(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		 initContentPage();
	}
	public ContentPage(Context context, AttributeSet attrs) {
		super(context, attrs);
		 initContentPage();
	}
	public ContentPage(Context context) {
		super(context);
		 initContentPage();
	}

	/**
	 * 初始化方法:往ContentPage中添加4个状态对应的View
	 */
	private void initContentPage(){
		LayoutParams params = new  LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);
		//1.添加加载中的view
		if(loadingView==null){
			loadingView = View.inflate(getContext(), R.layout.page_loading, null);
		}
		addView(loadingView, params);
		
		//2.添加加载数据失败的View
		if(errorView==null){
			errorView = View.inflate(getContext(), R.layout.page_error, null);
			Button btn_reload = (Button) errorView.findViewById(R.id.btn_reload);
			btn_reload.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					//1.先显示加载界面
					mState = PageState.STATE_LOADING;
					showPage();
					//2.然后去加载数据，并更新UI
					loadDataAndRefreshView();
				}
			});
		}
		addView(errorView, params);
		//3.添加加载数据为空的View
		if(emptyView==null){
			emptyView = View.inflate(getContext(), R.layout.page_empty, null);
		}
		addView(emptyView, params);
		//4.添加加载成功的View
		if(successView==null){
			successView = createSuccessView();
		}
		if(successView!=null){
			addView(successView, params);
		}else {
			throw new IllegalArgumentException("The method createSuccessView() can not return null!");
		}
		
		//5.根据当前的mState显示一下View
		showPage();
		
		//6.紧接着加载数据，然后刷新View
		loadDataAndRefreshView();
	}
	
	/**
	 * 加载数据然后刷新View
	 */
	public void loadDataAndRefreshView(){
		
		new Thread(){
			@Override
			public void run() {
				super.run();
				//延时2秒获取数据
//				try {
//					Thread.sleep(1000);
//				} catch (InterruptedException e) {
//					e.printStackTrace();
//				}

				//1.获取加载完成之后的数据
				Object result = loadData();//
				
				//2.根据数据判断更新对应的状态
				mState = checkData(result);

				CommonUtil.runOnUIThread(new Runnable() {
					@Override
					public void run() {
						//3.根据最新状态更新View
						showPage();
					}
				});
			}
		}.start();
	}
	
	public CustomProgressDialog dialog;
	/**
	 * 加载数据然后刷新View
	 */
	public void loadDataAndRefreshView_(){
		
		new Thread(){
			@Override
			public void run() {
				super.run();
				//延时2秒获取数据
				try {
					handler.sendEmptyMessage(0);
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				
				//1.获取加载完成之后的数据
				Object result = loadData();//
				
				//2.根据数据判断更新对应的状态
				mState = checkData(result);
				
				CommonUtil.runOnUIThread(new Runnable() {
					@Override
					public void run() {
						//3.根据最新状态更新View
						showPage();
						dialog.dismiss();
					}
				});
				
			}
		}.start();
	}
	
	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			dialog = CustomProgressDialog.createDialog(getContext());
			dialog.setMessage("正在加载中...");
			dialog.show();
			dialog.setCancelable(true);
		}
	};
	
	/**
	 * 根据对应的数据判断对应的状态
	 * @param result
	 * @return
	 */
	
	private PageState checkData(Object result){
		if(result==null){
			this.currValue=0;
			return PageState.STATE_ERROR;//加载失败
		}else {
			if(result instanceof List){
				List list = (List) result;
				if(list.size()==0){
					this.currValue=3;
					return PageState.STATE_SUCCESS;//返回数据为空,在界面另行处理，此处返回正确
				}else {
					this.currValue=0;
					return PageState.STATE_SUCCESS;
				}
			}else {
				this.currValue=0;
				return PageState.STATE_SUCCESS;
			}
		}
	}
	
	/**
	 * 根据mState显示不同的view
	 */
	private void showPage(){
		loadingView.setVisibility(mState==PageState.STATE_LOADING?View.VISIBLE:View.INVISIBLE);
		errorView.setVisibility(mState==PageState.STATE_ERROR?View.VISIBLE:View.INVISIBLE);
		emptyView.setVisibility(mState==PageState.STATE_EMPTY?View.VISIBLE:View.INVISIBLE);
		successView.setVisibility(mState==PageState.STATE_SUCCESS?View.VISIBLE:View.INVISIBLE);
	}
	
	/**
	 * 由于 每个fragment的成功View都不一样，那么这个View应该由每个fragment自己提供
	 * @return
	 */
	public abstract View createSuccessView();
	
	/**
	 * 我不关心每个fragment加载数据的过程，每个fragment只需要给我返回它加载完成之后的数据就行了
	 * @return
	 */
	public abstract Object loadData();
}
