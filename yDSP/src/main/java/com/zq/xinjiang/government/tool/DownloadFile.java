package com.zq.xinjiang.government.tool;

import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.storage.StorageManager;
import android.os.storage.StorageVolume;
import android.provider.MediaStore;
import android.util.Log;
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

import static android.os.Environment.MEDIA_MOUNTED;
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
//	public static final String savePath = "/政务app/";
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
	private String dfname = "";

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

	public static String getFilePath(Context context,String dir) {
		String directoryPath="";
		if (MEDIA_MOUNTED.equals(Environment.getExternalStorageState()) ) {//判断外部存储是否可用
			directoryPath =context.getExternalFilesDir(dir).getAbsolutePath();
		}else{//没外部存储就使用内部存储
			directoryPath= Environment.getExternalStorageDirectory()+dir;
		}
		File file = new File(directoryPath);
		if(!file.exists()){//判断文件目录是否存在
			file.mkdirs();
		}
		return directoryPath;
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

//				File fileDir = mContext.getExternalFilesDir(savePath);
//				String path = fileDir.getPath()+savePath;
//				String path3 = Environment.getExternalStoragePublicDirectory(savePath).getPath();
//				String path2 = Environment.getExternalStorageDirectory().getPath();
//				getExternalStorageDirectory
//				Log.i("TAG",path3+"...."+path2);
				String path = "";
//				if (getSDPath()){
					File exFileDir = Environment.getExternalStorageDirectory();
					path = exFileDir+savePath;
//				}else {
//				path = savePath;
//				}
				saveFileName = path +  dfname;
				File file = new File(saveFileName);
				if(!file.exists()){
					file.mkdirs();
				}
//				File file1 = new File(saveFileName);
//				if(!file1.exists()){
//					file1.mkdir();
//				}else {
//					if (file1.delete()){
//						file1.mkdir();
//					}
//				}

				FileOutputStream fos = new FileOutputStream(file);

//				File file = new File(savePath);
//				if (!file.exists()) {
//					file.mkdirs(); //创建多层目录
//				}
//				String apkFile = saveFileName;
//				File ApkFile = new File(apkFile);
//				FileOutputStream fos = new FileOutputStream(ApkFile);

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
	 * file 转换 content
	 * @param context
	 * @param imageFile
	 * @return
	 */
	public static Uri getFileContentUri(Context context, java.io.File imageFile) {
		String filePath = imageFile.getAbsolutePath();
		Cursor cursor = context.getContentResolver().query(
				MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
				new String[] { MediaStore.Images.Media._ID },
				MediaStore.Images.Media.DATA + "=? ",
				new String[] { filePath }, null);
		if (cursor != null && cursor.moveToFirst()) {
			int id = cursor.getInt(cursor
					.getColumnIndex(MediaStore.MediaColumns._ID));
			Uri baseUri = Uri.parse("content://media/external/images/media");
			return Uri.withAppendedPath(baseUri, "" + id);
		} else {
			if (imageFile.exists()) {
				ContentValues values = new ContentValues();
				values.put(MediaStore.Images.Media.DATA, filePath);
				return context.getContentResolver().insert(
						MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
			} else {
				return null;
			}
		}
	}

	/**
	 * Uri 转绝对路径
	 * Gets the corresponding path to a file from the given content:// URI
	 * @param selectedVideoUri The content:// URI to find the file path from
	 * @return the file path as a string
	 */
	public static String getFilePathFromContentUri(Uri selectedVideoUri,
												   Context context) {
		String filePath;
		String[] filePathColumn = {MediaStore.MediaColumns.DATA};

//		Cursor cursor = contentResolver.query(selectedVideoUri, filePathColumn, null, null, null);
//      也可用下面的方法拿到cursor
      	Cursor cursor = context.getContentResolver().query(selectedVideoUri, filePathColumn, null, null, null);

		cursor.moveToFirst();

		int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
		filePath = cursor.getString(columnIndex);
		cursor.close();
		return filePath;
	}


	private boolean getSDPath(){
		boolean sdCardExist = Environment.getExternalStorageState()
				.equals(MEDIA_MOUNTED); //判断sd卡是否存在
		return sdCardExist;
	}

	/**
	 * 下载apk
	 * 
	 */
	private void downloadFile() {
		downLoadThread = new Thread(mdownApkRunnable);
		downLoadThread.start();
	}

}