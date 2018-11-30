package com.zq.xinjiang.government.fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.storage.StorageManager;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.util.LogUtils;
import com.zq.xinjiang.R;
import com.zq.xinjiang.government.adapter.ItemDetailTableDownAdapter;
import com.zq.xinjiang.government.entity.ItemDetails;
import com.zq.xinjiang.government.tool.Allports;
import com.zq.xinjiang.unmethod.FileDownloadManager;

import java.io.File;
import java.util.List;
import java.util.Map;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;

/**
 * Created by Administrator on 2017/9/13.
 * 事项详情4
 */
@RuntimePermissions
public class G_ItemDetailsFragment4 extends Fragment {

    private View view;
    private ItemDetails itemDetails;
    private TextView textView;
    private List<Map<String, String>> list;
    private RecyclerView recyclerView;
    private ItemDetailTableDownAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.g_detailsfrag4,null);
        itemDetails = getArguments().getParcelable("itemDetails");

        init();
        return view;
    }
    public G_ItemDetailsFragment4() {
    }
//    public G_ItemDetailsFragment4(ItemDetails itemDetails) {
//        this.itemDetails = itemDetails;
//    }

    private void init(){
//        this.itemDetails = G_ItemDetailsActivity.itemDetails;
        recyclerView = (RecyclerView) view.findViewById(R.id.recycleview);
        if (itemDetails!=null) {
            list = itemDetails.getDocs();
            if (list.size() == 0) {
                recyclerView.setVisibility(View.GONE);
            } else {
                getData();
            }
        }
    }

    private void getData(){
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        // use a linear layout manager
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        // Disabled nested scrolling since Parent scrollview will scroll the content.
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new ItemDetailTableDownAdapter(getContext(),list);
        recyclerView.setAdapter(adapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        adapter.sampleDownFile(new ItemDetailTableDownAdapter.SampleDownLoadFile() {
            @Override
            public void setDownLoadFile(int pos) {

                String downpath = list.get(pos).get("path").toString();
                String dfname = list.get(pos).get("filename").toString();
                //去掉~字符
                downpath = downpath.substring(1,downpath.length());
                //拼接地址
                final String downFilePath = Allports.ipAddress + downpath;

                downFile(downFilePath,dfname);
//                G_ItemDetailsFragment4PermissionsDispatcher.downFileWithCheck(this,downFilePath,dfname);
            }
        });

//        MyLinearLayoutManager layoutManager = new MyLinearLayoutManager(getContext());
//        recyclerView.setLayoutManager(layoutManager);
//        layoutManager.setAutoMeasureEnabled(true);
//        adapter = new ItemDetailTableDownAdapter(getContext(),list);
//        recyclerView.setAdapter(adapter);
//        recyclerView.setItemAnimator(new DefaultItemAnimator());

    }

    @NeedsPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    public void downFile(String version_url,String title) {
        File file = new File("/sdcard/政务APP/"+title);
//        File file = new File("///storage/emulated/0/政务APP/"+title);
        if (!file.exists()){
            file.mkdirs();
        }
//        String new_url = "http://192.168.0.94:8888/sxslfj/" + version_url.substring(0,version_url.length()-1);
//        String new_url = MyFacesUrl.PIC_IP + version_url;
        String new_url = version_url;
        LogUtils.i("表格下载url---"+new_url);
//        String new_url = version_url;
        int endIndex = new_url.lastIndexOf("/");
        String filename = new_url.substring(endIndex+1);
        String path = "/sdcard/政务APP/"+title;
        LogUtils.i("---------------------"+filename);
        File file1 = new File("/sdcard/政务APP/"+title);
//        File file1 = new File("///storage/emulated/0/政务APP/"+title+"/"+filename);

        FileDownloadManager fileDownloadManager = FileDownloadManager.getInstance(getContext().getApplicationContext());
        downid = fileDownloadManager.startDownload(new_url,filename,path,"/sdcard/政务APP/",file1,"nodothing");
        String status = fileDownloadManager.getDownloadStatus(downid);

        Toast.makeText(getContext().getApplicationContext(),status,Toast.LENGTH_SHORT).show();
//        LogUtils.i(downid+"----------");

//        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
//        sp.edit().putLong(DownloadManager.EXTRA_DOWNLOAD_ID,downid).commit();
    }

    private BroadcastReceiver broadcastReceiver = null;

    public void listener() {
        // 注册广播监听系统的下载完成事件。
        IntentFilter intentFilter = new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                long Id = downid;
                long ID = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
                if (ID == Id) {
                    Toast.makeText(getContext().getApplicationContext(),"任务下载完成",Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(getContext().getApplicationContext(),"无效下载链接",Toast.LENGTH_SHORT).show();
                }
            }
        };

        getContext().getApplicationContext().registerReceiver(broadcastReceiver, intentFilter);
    }
    long downid = 1000000000;

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        //释放
        if (broadcastReceiver != null) {
            getContext().getApplicationContext().unregisterReceiver(broadcastReceiver);
        }
        super.onDestroy();
    }

    @SuppressLint("NeedOnRequestPermissionsResult")
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // NOTE: delegate the permission handling to generated method
        G_ItemDetailsFragment4PermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

}
