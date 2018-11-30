package com.zq.xinjiang.government.adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.zq.xinjiang.R;
import com.zq.xinjiang.government.activity.G_OnlineManageActivity;
import com.zq.xinjiang.government.tool.ViewHolder;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/9/15.
 * 适配器，在线办结文件上传
 */

public class OnlineManageAdapter extends AutoRVAdapter {

    public List<?> list;

    private Activity context;
    public OnlineManageAdapter(Activity context, List<?> list) {
        super(context, list);
        this.list = list;
        this.context = context;
    }

    @Override
    public int onCreateViewLayoutID(int viewType) {

        return R.layout.g_uploaditem;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        Map<String,String> map = (Map<String, String>) list.get(position);
        //map.get("filename").toString()   文件名称
        //map.get("path").toString()       文件路径

        holder.getTextView(R.id.uploadfile).setText(map.get("filename").toString());

//选择文件
        TextView select = holder.getTextView(R.id.select);
        TextView path = holder.getTextView(R.id.path); // 填写手机上选择的文件路径
        path.setText(map.get("phonePath"));

        select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectFile.setSelectFile(position);
            }
        });
//上传文件
        TextView uploadname = holder.getTextView(R.id.uploadname);
        uploadname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    public interface SelectFile{
        void setSelectFile(int pos);
    }

    private SelectFile selectFile;

    public void setSelectFile(SelectFile selectFile) {
        this.selectFile = selectFile;
    }
}
