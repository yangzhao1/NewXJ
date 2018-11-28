package com.zq.xinjiang.government.activity;

import android.app.Dialog;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.zq.xinjiang.R;
import com.zq.xinjiang.approval.utils.Config;
import com.zq.xinjiang.approval.utils.MSimpleHttpUtil;
import com.zq.xinjiang.government.BaseFragment;
import com.zq.xinjiang.government.adapter.MyFragmentAdapter;
import com.zq.xinjiang.government.entity.ItemDetails;
import com.zq.xinjiang.government.fragment.G_ItemDetailsFragment1;
import com.zq.xinjiang.government.fragment.G_ItemDetailsFragment2;
import com.zq.xinjiang.government.fragment.G_ItemDetailsFragment3;
import com.zq.xinjiang.government.fragment.G_ItemDetailsFragment4;
import com.zq.xinjiang.government.tool.Allports;
import com.zq.xinjiang.government.tool.StatusBar;
import com.zq.xinjiang.government.view.ScrllorTabTextView;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/9/18.
 * 事项详情
 */

public class G_ItemDetailsActivity extends BaseFragment implements View.OnClickListener{

    private ViewPager viewPager;
    private List<Fragment> list;
    private TextView title,titles,back;
    private ScrllorTabTextView scrollTab;
    private TextView conditions_declare,legal,charging,tableDownload;
    private FinalHttp finalHttp;
    private ItemDetails itemDetails = new ItemDetails();
    private TextView itemcode,itemproperty,windowid,orgname,itemtype,limittime,limitlegaltime,orgphone;
    private LinearLayout lin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.g_detailsmain);
//设置状态栏
        StatusBar.setStatusColor(this);
        init();
    }

    private void init(){
        title = (TextView) findViewById(R.id.titleText);
        titles = (TextView) findViewById(R.id.title);
        back = (TextView) findViewById(R.id.back);
        title.setText("事项详情");

        itemcode = (TextView) findViewById(R.id.itemcode);
        itemproperty = (TextView) findViewById(R.id.itemproperty);
        windowid = (TextView) findViewById(R.id.windowid);
        orgname = (TextView) findViewById(R.id.orgname);
        itemtype = (TextView) findViewById(R.id.itemtype);
        limittime = (TextView) findViewById(R.id.limittime);
        limitlegaltime = (TextView) findViewById(R.id.limitlegaltime);
        orgphone = (TextView) findViewById(R.id.orgphone);
        lin = (LinearLayout) findViewById(R.id.lin);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
//        viewPager.setIsCanScroll(false);

        scrollTab = (ScrllorTabTextView) findViewById(R.id.scrollTab);

        scrollTab.setCurrentNum(0);
        scrollTab.setTabNum(4);

        conditions_declare = (TextView) findViewById(R.id.conditions_declare);
        legal = (TextView) findViewById(R.id.legal);
        charging = (TextView) findViewById(R.id.charging);
        tableDownload = (TextView) findViewById(R.id.tableDownload);

        //获取数据
        String id = getIntent().getStringExtra("id");
        finalHttp = new FinalHttp();
        getUrlData(id);

        conditions_declare.setOnClickListener(this);
        legal.setOnClickListener(this);
        charging.setOnClickListener(this);
        tableDownload.setOnClickListener(this);
        back.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.conditions_declare:
                setTextColorAndTab(conditions_declare);
                scrollTab.setCurrentNum(0);
                viewPager.setCurrentItem(0,false);
                break;
            case R.id.legal:
                setTextColorAndTab(legal);
                scrollTab.setCurrentNum(1);
                viewPager.setCurrentItem(1,false);

                break;
            case R.id.charging:
                setTextColorAndTab(charging);

                scrollTab.setCurrentNum(2);
                viewPager.setCurrentItem(2,false);
                break;
            case R.id.tableDownload:
                setTextColorAndTab(tableDownload);
                scrollTab.setCurrentNum(3);
                viewPager.setCurrentItem(3,false);

                break;
            case R.id.back:

                finish();
                break;
            default:
                break;
        }
    }

    private void setTextColorAndTab(TextView textView){
        conditions_declare.setTextColor(getResources().getColor(R.color.black));
        legal.setTextColor(getResources().getColor(R.color.black));
        charging.setTextColor(getResources().getColor(R.color.black));
        tableDownload.setTextColor(getResources().getColor(R.color.black));

        textView.setTextColor(getResources().getColor(R.color.theme_color));
    }

    private void getUrlData(String id){

        final Dialog dialog;
        String url = Allports.getItemsDetailsUrl("api","getitemdetail",id);
        Log.e("事项详情解析： ",""+url);
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
                            JSONObject ob = jsonObject.getJSONObject("item");
                            itemDetails.setItemcode(ob.get("itemcode").toString());
                            itemDetails.setItemproperty(ob.get("itemproperty").toString());
                            itemDetails.setItemname(ob.getString("itemname"));
                            itemDetails.setWindowid(ob.get("windowid").toString());
                            itemDetails.setOrgname(ob.get("orgname").toString());
                            itemDetails.setItemtype(ob.get("itemtype").toString());
                            itemDetails.setLimittime(ob.get("limittime").toString());
                            itemDetails.setLimitlegaltime(ob.get("limitlegaltime").toString());
                            itemDetails.setOrgphone(ob.get("phone").toString());
                            itemDetails.setRequirements(ob.get("requirements").toString());
                            itemDetails.setApplypursuant(ob.get("applypursuant").toString());
                            itemDetails.setChargepursuant(ob.get("chargequantity").toString());

                            JSONArray array = ob.getJSONArray("docs"); //表格下载
                            List<Map<String,String>> list = new ArrayList<>();
                            Map map;
                            for (int i = 0;i<array.length();i++){
                                map = new HashMap();
                                JSONObject o = (JSONObject) array.get(i);
                                String filename = o.getString("name");//文件名称
                                String path = o.getString("directory");//文件路径
                                String Canonlinedownload = o.getString("canonlinedownload");
                                String Canonlineupload = o.getString("canonlineupload");
                                if (Canonlinedownload.equals("false")){
                                    //不允许下载
                                }else {
                                    map.put("filename",filename);
                                    map.put("path",path);
                                    list.add(map);
                                }
                            }
                            itemDetails.setDocs(list);

                            setData(itemDetails);
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

    private void setData(ItemDetails itemDetails1){
        titles.setText(itemDetails.getItemname());
        itemcode.setText(itemDetails.getItemcode());
        itemproperty.setText(itemDetails.getItemproperty());
        windowid.setText(itemDetails.getWindowid()+"号窗口");
        orgname.setText(itemDetails.getOrgname());
        itemtype.setText(itemDetails.getItemtype());
        limittime.setText(itemDetails.getLimittime()+"个工作日");
        limitlegaltime.setText(itemDetails.getLimitlegaltime()+"个工作日");
        orgphone.setText(itemDetails.getOrgphone());

        //写完数据在写viewpager   itemDetails
        list = new ArrayList<>();
        Fragment fragment = null;
        Bundle bundle =new Bundle();
        bundle.putParcelable("itemDetails", itemDetails1);

        fragment = new G_ItemDetailsFragment1();
        fragment.setArguments(bundle);
        list.add(fragment);
        fragment = new G_ItemDetailsFragment2();
        fragment.setArguments(bundle);
        list.add(fragment);
        fragment = new G_ItemDetailsFragment3();
        fragment.setArguments(bundle);
        list.add(fragment);
        fragment = new G_ItemDetailsFragment4();
        fragment.setArguments(bundle);
        list.add(fragment);

        viewPager.setAdapter(new MyFragmentAdapter(getSupportFragmentManager(),list));
        viewPager.setCurrentItem(0,false);
        viewPager.setOffscreenPageLimit(4);

//        scrollTab.setSelectedColor(R.color.theme_color,R.color.theme_color);
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                scrollTab.setOffset(position,positionOffset);
                switch (position){
                    case 0:
                        setTextColorAndTab(conditions_declare);
                        scrollTab.setCurrentNum(0);
                        break;
                    case 1:
                        setTextColorAndTab(legal);
                        scrollTab.setCurrentNum(1);
                        break;
                    case 2:
                        setTextColorAndTab(charging);
                        scrollTab.setCurrentNum(2);
                        break;
                    case 3:
                        setTextColorAndTab(tableDownload);
                        scrollTab.setCurrentNum(3);
                        break;
                    default:

                        break;
                }
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }
    /**
     * 加载字体，提示用户信息
     *
     * @param toast
     */
    public void initToast(String toast) {
        View toastRoot = getLayoutInflater().inflate(R.layout.toast, null);
        TextView message = (TextView) toastRoot.findViewById(R.id.tv_message);
        // 字体simkai.ttf放置于assets/fonts/路径下
        Typeface face = Typeface.createFromAsset(getAssets(), "simkai.ttf");
        message.setTypeface(face);// 设置字体
        message.setText(toast);

        Toast toastStart = new Toast(this);
        toastStart.setGravity(Gravity.BOTTOM, 0, 80);
        toastStart.setDuration(Toast.LENGTH_LONG);
        toastStart.setView(toastRoot);
        toastStart.show();
    }
    /**
     * 提示网络的错误信息
     *
     * @Title: printError
     * @Description: TODO
     * @param errorNo
     * @return void
     * @throws
     */
    public void printError(int errorNo) {
        System.out.println(errorNo);
        switch (errorNo) {
            case Config.NET_ADDRESS_ERROR:
                initToast("服务器地址错误！");
                break;
            case Config.NET_SERVER_ERROR:
                initToast("服务器已关闭！");
                break;
            default:
                initToast("网络断开，请稍后重试！");
                break;
        }
    }
}
