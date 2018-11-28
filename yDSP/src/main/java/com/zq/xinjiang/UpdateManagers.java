package com.zq.xinjiang;

import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class UpdateManagers {

	private Context mContext;
	// 返回的安装包url
	// private String apkUrl =
	// "http://192.168.1.117:8008/webservices/Json.aspx?mod=api&act=getDownload&meth=load&strName=fdzw.apk";
	private String apkUrl;
	private Dialog noticeDialog;
	private Dialog downloadDialog;
	/* 下载包安装路径 */
	private static final String savePath = "/sdcard/移动审批/";
	private static final String saveFileName = savePath +"移动审批.apk";
	private TextView tv;
	/* 进度条与通知ui刷新的handler和msg常量 */
	private ProgressBar mProgress;
	private static final int DOWN_UPDATE = 1;
	private static final int DOWN_OVER = 2;
	private int progress;
	private Thread downLoadThread;
	private boolean interceptFlag = false;

	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case DOWN_UPDATE:
				tv.setText(progress + "%");
				mProgress.setProgress(progress);
				if (progress == 99) {
					downloadDialog.dismiss();
				}
				break;
			case DOWN_OVER:
				installApk();
				break;
			default:
				break;
			}
		};
	};

	public UpdateManagers(Context context) {
		this.mContext = context;
	}

	// 外部接口让主Activity调用
	public void checkUpdateInfo(String str, String apkUrl) {
		this.apkUrl = apkUrl;
		showNoticeDialog(str);
	}

	private void showNoticeDialog(String str) {
		Builder builder = new Builder(mContext);
		builder.setTitle("软件更新");
		builder.setMessage(str);
		builder.setPositiveButton("更新", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				showDownloadDialog();
			}
		});
		builder.setNegativeButton("暂不更新", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		noticeDialog = builder.create();
		noticeDialog.show();
	}

	private void showDownloadDialog() {
		Builder builder = new Builder(mContext);
		builder.setTitle(R.string.app_name);

		final LayoutInflater inflater = LayoutInflater.from(mContext);
		View v = inflater.inflate(R.layout.progress, null);
		tv = (TextView) v.findViewById(R.id.tv);
		mProgress = (ProgressBar) v.findViewById(R.id.progress);

		builder.setView(v);
		builder.setNegativeButton("取消", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				interceptFlag = true;
			}
		});
		downloadDialog = builder.create();
		downloadDialog.show();

		downloadApk();
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
				e.printStackTrace();
			}
		}
	};

	/**
	 * 下载apk
	 * 
	 * @param url
	 */
	private void downloadApk() {
		downLoadThread = new Thread(mdownApkRunnable);
		downLoadThread.start();
	}

	/**
	 * 安装apk
	 * 
	 * @param url
	 */
	private void installApk() {
		File apkfile = new File(saveFileName);
		if (!apkfile.exists()) {
			return;
		}
		Intent i = new Intent(Intent.ACTION_VIEW);// 显示用户数据
		i.setDataAndType(Uri.parse("file://" + apkfile.toString()),
				"application/vnd.android.package-archive");
		mContext.startActivity(i);
	}

}