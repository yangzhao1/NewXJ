package com.zq.xinjiang.government.activity;

import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.lidroid.xutils.BitmapUtils;
import com.zq.xinjiang.BaseActivity;
import com.zq.xinjiang.R;
import com.zq.xinjiang.approval.utils.MSimpleHttpUtil;
import com.zq.xinjiang.government.tool.Allports;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Administrator on 2017/9/14.
 * 新闻详情
 */

public class G_NewsDetails extends BaseActivity {
    private TextView titleText,back;
    private final String title = "新闻详情";
    private String id = null;
    private FinalHttp finalHttp;

    private TextView title_con,content,date;
    private ImageView image;
    private String imageurl="";
    private String centershow = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.g_newsdetails);
        setStatusColor();
        init();
    }

    private void init(){
        finalHttp = new FinalHttp();
        id = getIntent().getStringExtra("id");
        imageurl = getIntent().getStringExtra("image");
        centershow = getIntent().getStringExtra("centershow");


        titleText = (TextView) findViewById(R.id.titleText);

        title_con = (TextView) findViewById(R.id.title_con);
        content = (TextView) findViewById(R.id.content);
        date = (TextView) findViewById(R.id.date);
        image = (ImageView) findViewById(R.id.image);

        back = (TextView) findViewById(R.id.back);

        if (!TextUtils.isEmpty(centershow)){
            titleText.setText("中心简介");
            getCenterUrlData();
        }else {
            titleText.setText(title);
            setPic(imageurl);
            getUrlData();
        }
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    //中心简介列表解析
    private void getCenterUrlData(){

        String url = Allports.getOnlineUrl("3","1" +"",1+"");
        Log.e("中心简介url：",url);
        if (MSimpleHttpUtil.isCheckNet(this)) {

            dialog = showLoadingPop();
            finalHttp.get(url, new AjaxCallBack<String>() {

                @Override
                public void onFailure(Throwable t, int errorNo, String strMsg) {
                    super.onFailure(t, errorNo, strMsg);

                        dialog.dismiss();
                    printError(errorNo);
                }

                @Override
                public void onSuccess(String t) {
                    super.onSuccess(t);

                        dialog.dismiss();
                    try {
                        JSONObject jsonObject = new JSONObject(t);
                        int errno = jsonObject.getInt("errno");
                        if (errno == 0) {
                            JSONObject obj = jsonObject.getJSONObject("item");
                            title_con.setText(obj.getString("title"));
                            content.setText(Html.fromHtml(obj.getString("content")));
                            if (TextUtils.isEmpty(obj.getString("addtime"))){
                                date.setText("信息来源：合阳政务");
                            }else {
                                date.setText("信息来源：合阳政务      发表时间："+obj.getString("addtime"));
                            }
                            setPic(obj.getString("titleimgpath"));
                        } else {
                            String errors = jsonObject.getJSONArray("errors").getString(0);
                            initToast(errors);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        } else {
            initToast("请检查网络是否连接！");
        }
    }


    //通知公告列表解析
    private void getUrlData(){

        String url = Allports.getOnlineDetailsUrl(id);
        Log.e("通知公告详情接口：",url);
        if (MSimpleHttpUtil.isCheckNet(this)) {
            dialog = showLoadingPop();
            finalHttp.get(url, new AjaxCallBack<String>() {

                @Override
                public void onFailure(Throwable t, int errorNo, String strMsg) {
                    super.onFailure(t, errorNo, strMsg);

                    dialog.dismiss();
                    printError(errorNo);
                }

                @Override
                public void onSuccess(String t) {
                    super.onSuccess(t);
                    dialog.dismiss();
                    try {
                        JSONObject jsonObject = new JSONObject(t);
                        int errno = jsonObject.getInt("errno");
                        if (errno == 0) {
                            JSONObject obj = jsonObject.getJSONObject("item");
                            title_con.setText(obj.getString("title"));
                            content.setText(Html.fromHtml(obj.getString("content")));
                            if (TextUtils.isEmpty(obj.getString("addtime"))){
                                date.setText("信息来源：合阳政务");
                            }else {
                                date.setText("信息来源：合阳政务      发表时间："+obj.getString("addtime"));
                            }
//                            setPic(obj.getString("titleimgpath"));
                        } else {
                            String errors = jsonObject.getJSONArray("errors").getString(0);
                            initToast(errors);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        } else {
            initToast("请检查网络是否连接！");
        }
    }

    /**
     * 加载头像
     * @Title: setPic
     * @Description: TODO
     * @return void
     * @throws
     */
    private void setPic(String picurl) {
        if (!"".equals(picurl)) {
            BitmapUtils bitmapUtils = new BitmapUtils(this);
            bitmapUtils.display(image,picurl);
        }
    }
}
