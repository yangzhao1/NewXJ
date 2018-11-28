package com.zq.xinjiang.government.tool;

import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.zq.xinjiang.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
//import android.content.DialogInterface;
//import android.content.DialogInterface.OnClickListener;

public class DownloadFile {
	/**
	 * 文件下载
	 */
	private Context mContext;
	// 返回的安装包url
	// private String apkUrl =
	// "http://192.168.1.117:8008/webservices/Json.aspx?mod=api&act=getDownload&meth=load&strName=fdzw.apk";
	private String apkUrl;
	private Dialog noticeDialog;
	private Dialog downloadDialog;
	/* 下载包安装路径 */
	public static final String savePath = "/sdcard/政务app/";
	private String saveFileName = "";
	private TextView tv;
	/* 进度条与通知ui刷新的handler和msg常量 */
	private ProgressBar mProgress;
	private static final int DOWN_UPDATE = 1;
	private static final int DOWN_OVER = 2;
	private static final int DOWN_BREAK = 3;
	private int progress;
	private Thread downLoadThread;
	private boolean interceptFlag = false;
	private TextView textView,saveText;
	private String dfname;

	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case DOWN_UPDATE:
				tv.setText(progress + "%");
				mProgress.setProgress(progress);
//				popup_tv.setText(progress + "%");
//				popup_progress.setProgress(progress);
				if (progress == 99) {
					downloadDialog.dismiss();
//					popWindow.dismiss();
				}
				break;
			case DOWN_OVER:
                textView.setText("打开");
				saveText.setText("保存路径："+ saveFileName);
				break;
				case DOWN_BREAK:
					saveText.setText("找不到文件");
					break;
			default:
				break;
			}
		};
	};

	public DownloadFile(Context context,TextView downText,String dfname,TextView saveText) {
		this.mContext = context;
        this.textView = downText;
        this.saveText = saveText;
		this.dfname = dfname;
		view = new View(mContext);
	}

	// 外部接口让主Activity调用
	public void downInfo(String apkUrl) {
		this.apkUrl = apkUrl;
		saveFileName = savePath + dfname;
//		showNoticeDialog(str);
		showDownloadDialog();
//		showPopWindow_jcgx(mContext, view, str);
	}
	private View view;
	private PopupWindow popWindow;
	private LinearLayout popup_no, popup_yes;
	private TextView popup_gx;
	private TextView popup_tv;
	private ProgressBar popup_progress;
	private LinearLayout popup_qx;

	private void showDownloadDialog() {
		Builder builder = new Builder(mContext);
		builder.setTitle("文件下载");

		final LayoutInflater inflater = LayoutInflater.from(mContext);
		View v = inflater.inflate(R.layout.progress, null);
		tv = (TextView) v.findViewById(R.id.tv);
		mProgress = (ProgressBar) v.findViewById(R.id.progress);

		builder.setView(v);
		builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				interceptFlag = true;
			}
		});
		downloadDialog = builder.create();
		downloadDialog.show();

		downloadFile();
	}



	private Runnable mdownApkRunnable = new Runnable() {
		@Override
		public void run() {
			try {
				URL url = new URL(apkUrl);

				HttpURLConnection conn = (HttpURLConnection) url
						.openConnection();
				conn.connect();
				int length = conn.getContentLength();
				InputStream is = conn.getInputStream();

				File file = new File(savePath);
				if (!file.exists()) {
					file.mkdir();
				}
				String apkFile = saveFileName;
				File ApkFile = new File(apkFile);
				FileOutputStream fos = new FileOutputStream(ApkFile);

				int count = 0;
				byte buf[] = new byte[1024];

				do {
					int numread = is.read(buf);
					count += numread;
					progress = (int) (((float) count / length) * 100);
					// 更新进度
					mHandler.sendEmptyMessage(DOWN_UPDATE);
					if (numread <= 0) {
						// 下载完成通知安装
						mHandler.sendEmptyMessage(DOWN_OVER);
						break;
					}
					fos.write(buf, 0, numread);
				} while (!interceptFlag);// 点击取消就停止下载.
				fos.close();
				is.close();
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				downloadDialog.dismiss();
				mHandler.sendEmptyMessage(DOWN_BREAK);

				e.printStackTrace();
			}
		}
	};

	/**
	 * 下载apk
	 * 
	 */
	private void downloadFile() {
		downLoadThread = new Thread(mdownApkRunnable);
		downLoadThread.start();
	}

}