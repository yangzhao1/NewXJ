package com.zq.xinjiang.unmethod;

import android.app.DownloadManager;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import java.io.File;

/**
 * Created by ZHAOBAOSHAN on 2018/3/20.
 */

public class FileDownloadManager {
    private DownloadManager downloadManager;
    private Context context;
    private static FileDownloadManager instance;

    private FileDownloadManager(Context context) {
        downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        this.context = context.getApplicationContext();
    }

    public static FileDownloadManager getInstance(Context context) {
        if (instance == null) {
            instance = new FileDownloadManager(context);
        }
        return instance;
    }
    /**
     * @param uri
     * @param title
     * @param description
     * @return download id
     */
    public long startDownload(String uri, String title, String description, String appName, File file, String dothing) {
        DownloadManager.Request req = new DownloadManager.Request(Uri.parse(uri));
//        req.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI);
//        req.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE);
        //req.setAllowedOverRoaming(false);
        req.allowScanningByMediaScanner();//被其他应用扫描到
        req.setVisibleInDownloadsUi(true);//被系统的下载管理扫描到并管理
        req.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        //设置文件的保存的位置[三种方式]
        //第一种
        //file:///storage/emulated/0/Android/data/your-package/files/Download/update.apk
//        req.setDestinationInExternalFilesDir(context, Environment.DIRECTORY_DOWNLOADS, appName+"");
        //第二种
        //file:///storage/emulated/0/Download/update.apk
        //req.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "update.apk");
        //第三种 自定义文件路径
        req.setDestinationUri(Uri.fromFile(file));

        // 设置一些基本显示信息
        req.setTitle(title);
        req.setDescription(description);
        if (dothing.equals("dothing")){
            req.setDestinationInExternalPublicDir(description,title);
        }
        //req.setMimeType("application/vnd.android.package-archive");
        return downloadManager.enqueue(req);//异步
        //dm.openDownloadedFile()
    }
    /**
     * 获取文件保存的路径
     *
     * @param downloadId an ID for the download, unique across the system.
     *                   This ID is used to make future calls related to this download.
     * @return file path
     * @see FileDownloadManager#getDownloadUri(long)
     */
    public String getDownloadPath(long downloadId) {
        DownloadManager.Query query = new DownloadManager.Query().setFilterById(downloadId);
        Cursor c = downloadManager.query(query);
        if (c != null) {
            try {
                if (c.moveToFirst()) {
                    return c.getString(c.getColumnIndexOrThrow(DownloadManager.COLUMN_LOCAL_URI));
                }
            } finally {
                c.close();
            }
        }
        return null;
    }

    /**
     * 获取保存文件的地址
     *
     * @param downloadId an ID for the download, unique across the system.
     *                   This ID is used to make future calls related to this download.
     * @see FileDownloadManager#getDownloadPath(long)
     */
    public Uri getDownloadUri(long downloadId) {
        return downloadManager.getUriForDownloadedFile(downloadId);
    }

    public DownloadManager getDownloadManager() {
        return downloadManager;
    }

    /**
     * 获取下载状态
     *
     * @param downloadId an ID for the download, unique across the system.
     *                   This ID is used to make future calls related to this download.
     * @return int
     * @see DownloadManager#STATUS_PENDING
     * @see DownloadManager#STATUS_PAUSED
     * @see DownloadManager#STATUS_RUNNING
     * @see DownloadManager#STATUS_SUCCESSFUL
     * @see DownloadManager#STATUS_FAILED
     */
    public String getDownloadStatus(long downloadId) {
        DownloadManager.Query query = new DownloadManager.Query().setFilterById(downloadId);
        Cursor cursor = downloadManager.query(query);
        String statusMsg = "无效下载链接";
        if (cursor != null) {
            try {
                if (cursor.moveToFirst()) {
                    int status = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS));
                    switch (status) {
                        case DownloadManager.STATUS_PAUSED:
                            statusMsg = "暂停";
                        case DownloadManager.STATUS_PENDING:
                            statusMsg = "等待中";
                        case DownloadManager.STATUS_RUNNING:
                            statusMsg = "开始下载";
                            break;
                        case DownloadManager.STATUS_SUCCESSFUL:
                            statusMsg = "下载成功";
                            break;
                        case DownloadManager.STATUS_FAILED:
                            statusMsg = "下载失败";
                            break;
                        default:
                            statusMsg = "未知状态";
                            break;
                    }
//                    return c.getInt(c.getColumnIndexOrThrow(DownloadManager.COLUMN_STATUS));
                }
            } finally {
                cursor.close();
            }
        }
        return statusMsg;
    }

}
