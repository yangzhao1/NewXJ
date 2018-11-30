package com.zq.xinjiang.government.adapter;

import android.app.DownloadManager;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.util.LogUtils;
import com.zq.xinjiang.R;
import com.zq.xinjiang.government.tool.Allports;
import com.zq.xinjiang.government.tool.DownloadFile;
import com.zq.xinjiang.government.tool.ViewHolder;
import com.zq.xinjiang.unmethod.FileDownloadManager;

import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/9/15.
 * 事项详情表格下载适配器
 */

public class ItemDetailTableDownAdapter extends AutoRVAdapter {

    private Context context;
    private List<?> list;
    public ItemDetailTableDownAdapter(Context context, List<?> list) {
        super(context, list);
        this.context = context;
        this.list = list;
    }

    @Override
    public int onCreateViewLayoutID(int viewType) {
        return R.layout.g_tabledown;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        TextView filename = holder.getTextView(R.id.downfile);
        final TextView savePath = holder.getTextView(R.id.savepath);
        Map map = (Map) list.get(position);

        final String dfname = map.get("filename").toString();
        String downpath = map.get("path").toString();

        filename.setText(dfname);
        //去掉~字符
        downpath = downpath.substring(1,downpath.length());
//        String paths = downpath.replace("~","");;

        //拼接地址
        final String downFilePath = Allports.ipAddress + downpath;
        final TextView down = holder.getTextView(R.id.down);

        Log.i("下载地址 : ", downFilePath.toString());

        down.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                sampleDownLoadFile.setDownLoadFile(position);


                String text = down.getText().toString();
                if (text.equals("下载")){
                    DownloadFile downloadFile = new DownloadFile(context,down,dfname,savePath);
                    downloadFile.downInfo(downFilePath);
                }else if (text.equals("打开")){

//                    String filepath = context.getFilesDir();
                    File file =context.getExternalFilesDir(DownloadFile.savePath);
                    if(null==file || !file.exists()){
                        return;
                    }

                    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                    intent.setType("*/*");
                    intent.addCategory(Intent.CATEGORY_OPENABLE);

//                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                    intent.setDataAndType(Uri.fromFile(file1), "file/*");

//                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(file.toString()));
                    try {
                        context.startActivity(intent);
//                        startActivity(Intent.createChooser(intent,"选择浏览工具"));
                    } catch (ActivityNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    public interface SampleDownLoadFile{
        void setDownLoadFile(int pos);
    }

    private SampleDownLoadFile sampleDownLoadFile;

    public void sampleDownFile(SampleDownLoadFile sampleDownLoadFile){
        this.sampleDownLoadFile = sampleDownLoadFile;
    }


}
