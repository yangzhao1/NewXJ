package com.zq.xinjiang.government.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lidroid.xutils.BitmapUtils;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerListener;
import com.youth.banner.loader.ImageLoader;
import com.zq.xinjiang.R;
import com.zq.xinjiang.approval.aactivity.LoginActivity;
import com.zq.xinjiang.approval.utils.LogUtil;
import com.zq.xinjiang.approval.utils.MSimpleHttpUtil;
import com.zq.xinjiang.government.activity.G_ConvenAllActivity;
import com.zq.xinjiang.government.activity.G_NewsDetails;
import com.zq.xinjiang.government.activity.G_NotificateActivity;
import com.zq.xinjiang.government.activity.G_WebActivity;
import com.zq.xinjiang.government.adapter.HomeNotificateAdapter;
import com.zq.xinjiang.government.adapter.MyFragmentAdapter;
import com.zq.xinjiang.government.popupwindow.CommonPopupWindow;
import com.zq.xinjiang.government.tool.Allports;
import com.zq.xinjiang.government.tool.ScrollBanner;
import com.zq.xinjiang.unmethod.SharedPreferencesUtils;

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
 * Created by Administrator on 2017/9/12.
 * 、首页
 */

public class G_HomeFragment extends Fragment implements View.OnClickListener,OnBannerListener{
    private View view;
    private LinearLayout news,express,livingpay,violation,trick;
    private List<View> listViews;
    private List<Fragment> listFrag;
    private ViewPager viewPager;
    private ImageView dot1,dot2;
    private TextView morenews,moreconvens;
    private LinearLayout lin,cut_user;
    private Banner banner;

    private FinalHttp finalHttp;
    //通知公告存数据的map
    private String no_id = null;
    private TextView no_title,no_content;
    private ImageView no_image;
    private Intent intent = null;

    private String titleimgpath="";
    private SharedPreferences preference_app;
    private SharedPreferences.Editor editor_app;
    private ScrollBanner scrollBanner;
    private RecyclerView recyclerView;
    private ScrollView scrollView;
    private int scrollHeightPx = 0;//把80dp转换成px
    private HomeNotificateAdapter adapter = null;
    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.g_homemain,null);

        //保存数据是从政务还是审批
        preference_app = getContext().getSharedPreferences("app_type", Context.MODE_PRIVATE);
        editor_app = preference_app.edit();

        viewPager = (ViewPager) view.findViewById(R.id.viewpager);
        dot1 = (ImageView) view.findViewById(R.id.dot1);
        dot2 = (ImageView) view.findViewById(R.id.dot2);
        banner = (Banner) view.findViewById(R.id.banner);

        news = (LinearLayout) view.findViewById(R.id.news);
        express = (LinearLayout) view.findViewById(R.id.express);
        livingpay = (LinearLayout) view.findViewById(R.id.livingpay);
        violation = (LinearLayout) view.findViewById(R.id.violation);
        trick = (LinearLayout) view.findViewById(R.id.trick);
        lin = (LinearLayout) view.findViewById(R.id.lin);
        scrollView = (ScrollView) view.findViewById(R.id.scrollView);

        morenews = (TextView) view.findViewById(R.id.morenews);
        moreconvens = (TextView) view.findViewById(R.id.moreconven);
        cut_user = (LinearLayout) view.findViewById(R.id.cut_user);
        no_title = (TextView) view.findViewById(R.id.no_title);
        no_content = (TextView) view.findViewById(R.id.no_content);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycleview);

        no_image = (ImageView) view.findViewById(R.id.no_image);
//        scrollBanner = (ScrollBanner) view.findViewById(R.id.scrollBanner);

        init();
        return view;
    }

    private void init(){
        finalHttp = new FinalHttp();
//        news.setOnClickListener(this);
        express.setOnClickListener(this);
        livingpay.setOnClickListener(this);
        violation.setOnClickListener(this);
        trick.setOnClickListener(this);
        morenews.setOnClickListener(this);
        moreconvens.setOnClickListener(this);
        cut_user.setOnClickListener(this);
        //轮播图片初始化

        listFrag = new ArrayList<Fragment>();
        listFrag.add(new G_TypeFragment1());
//        listFrag.add(new G_TypeFragment2());
        viewPager.setAdapter(new MyFragmentAdapter(getChildFragmentManager(),listFrag));
        viewPager.setCurrentItem(0);
//        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
//            @Override
//            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//                switch (position){
//                    case 0:
//                        dot1.setBackgroundResource(R.drawable.dot_focused);
//                        dot2.setBackgroundResource(R.drawable.dot_normal);
//                        break;
//                    case 1:
//                        dot2.setBackgroundResource(R.drawable.dot_focused);
//                        dot1.setBackgroundResource(R.drawable.dot_normal);
//                        break;
//                    default:
//
//                        break;
//                }
//            }
//
//            @Override
//            public void onPageSelected(int position) {
//
//            }
//
//            @Override
//            public void onPageScrollStateChanged(int state) {
//
//            }
//        });
        //通知公告信息
        getUrlData();
        initBannerData();
        banner.setFocusable(true);
        banner.setFocusableInTouchMode(true);
        banner.requestFocus();
    }

    /**
     * 初始化轮播图片
     */
    private void initBannerData() {
        //放图片地址的集合

//        list_path.add("http://ww4.sinaimg.cn/large/006uZZy8jw1faic21363tj30ci08ct96.jpg");
//        list_path.add("http://ww4.sinaimg.cn/large/006uZZy8jw1faic259ohaj30ci08c74r.jpg");
//        list_path.add("http://ww4.sinaimg.cn/large/006uZZy8jw1faic2b16zuj30ci08cwf4.jpg");
//        list_path.add("http://ww4.sinaimg.cn/large/006uZZy8jw1faic2e7vsaj30ci08cglz.jpg");
        List<Integer> list_path = new ArrayList<>();
        list_path.add(R.drawable.y12);
        list_path.add(R.drawable.y11);
        list_path.add(R.drawable.y13);
        list_path.add(R.drawable.y15);

        //设置内置样式，共有六种可以点入方法内逐一体验使用。
        banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
        //设置图片加载器，图片加载器在下方
        banner.setImageLoader(new MyLoader());
        //设置图片网址或地址的集合
        banner.setImages(list_path);
        //设置轮播的动画效果，内含多种特效，可点入方法内查找后内逐一体验
        banner.setBannerAnimation(Transformer.Stack);
//        //设置轮播图的标题集合
//        banner.setBannerTitles(list_title);
        //设置轮播间隔时间
        banner.setDelayTime(5000);
        //设置是否为自动轮播，默认是“是”。
        banner.isAutoPlay(true);

        //设置指示器的位置，小点点，左中右。
        banner.setIndicatorGravity(BannerConfig.CENTER)
                //以上内容都可写成链式布局，这是轮播图的监听。比较重要。方法在下面。
                .setOnBannerListener(this)
                //必须最后调用的方法，启动轮播图。
                .start();
    }

    @Override
    public void OnBannerClick(int position) {

    }

    //自定义的图片加载器
    public class MyLoader extends ImageLoader {

        @Override
        public void displayImage(Context context, Object path, ImageView imageView) {
            Glide.with(context).load(path).into(imageView);
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
            BitmapUtils bitmapUtils = new BitmapUtils(getContext());
            bitmapUtils.display(no_image,picurl);
        }
    }

    @Override
    public void onResume() {
        if (adapter!=null){
            adapter.stopRunThread();
        }
        super.onResume();
    }

    /**
     *
     * @param view
     *
     */
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.news://通知公告
                intent = new Intent(getContext(),G_NewsDetails.class);
                intent.putExtra("id",no_id);
                intent.putExtra("image",titleimgpath);
                startActivity(intent);
                break;
            case R.id.express://快递查询
                intent = new Intent(getContext(),G_WebActivity.class);
                intent.putExtra("title","快递查询");
                intent.putExtra("code",9);
                startActivity(intent);
                break;
            case R.id.livingpay:
                intent = new Intent(getContext(),G_WebActivity.class);
                intent.putExtra("title","生活缴费");
                intent.putExtra("code",10);
                startActivity(intent);
                break;
            case R.id.violation:
                intent = new Intent(getContext(),G_WebActivity.class);
                intent.putExtra("title","违章查询");
                intent.putExtra("code",11);
                startActivity(intent);
                break;
            case R.id.trick:
                intent = new Intent(getContext(),G_WebActivity.class);
                intent.putExtra("title","机票预订");
                intent.putExtra("code",4);
                startActivity(intent);
                break;
            case R.id.morenews:
                startActivity(new Intent(getContext(),G_NotificateActivity.class));
                break;
            case R.id.moreconven:
                startActivity(new Intent(getContext(),G_ConvenAllActivity.class));
                break;
            case R.id.cut_user://切换用户
                getCutUserPop();

                break;
            default:
                break;
        }
    }

    private List<Map<String,Object>> list_notics = new ArrayList<>();



    //通知公告列表解析
    private void getUrlData(){
        list_notics.clear();
        String url = Allports.getOnlineUrl("0","4","1");
//        String url = "http://192.168.1.95:8082/hycms/service/server/getResult.do?type=news&classid=0&pagesize=4&pageindex=1";
        LogUtil.recordLog("通知公告接口："+url);
        if (MSimpleHttpUtil.isCheckNet(getContext())) {
            finalHttp.get(url, new AjaxCallBack<String>() {
                @Override
                public void onFailure(Throwable t, int errorNo, String strMsg) {
                    super.onFailure(t, errorNo, strMsg);
                }

                @Override
                public void onSuccess(String t) {
                    super.onSuccess(t);
                    try {
                        JSONObject jsonObject = new JSONObject(t);
                        int errno = jsonObject.getInt("errno");
                        if (errno == 0) {
                            JSONArray array = jsonObject.getJSONArray("items");

                            Map map;
                            for (int i =0;i<array.length();i++){
                                JSONObject obj = (JSONObject) array.get(i);
                                map = new HashMap();
                                no_id = obj.getString("id");
                                titleimgpath = obj.getString("titleimgpath");

                                map.put("no_id",no_id);
                                map.put("titleimgpath",titleimgpath);
                                map.put("title",obj.getString("title"));
                                map.put("shortcontent",obj.getString("shortcontent"));
//                                setPic(titleimgpath);
//                                no_title.setText(obj.getString("title"));
//                                no_content.setText(obj.getString("shortcontent"));
                                list_notics.add(map);
                            }
//                            scrollBanner.setList(list_notics);
//                            scrollBanner.startScroll();

                            LinearLayoutManager layoutManager = new LinearLayoutManager(getContext()){
                                @Override
                                public boolean canScrollVertically() {
                                    return false;
                                }
                            };
                            recyclerView.setLayoutManager(layoutManager);
                            adapter = new HomeNotificateAdapter(getContext(),list_notics);
                            LogUtil.recordLog("数据列表size："+list_notics.size());
                            recyclerView.setAdapter(adapter);

                            adapter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                    if (list_notics.size()!=0){
                                        Intent in = new Intent(getContext(),G_NewsDetails.class);
                                        in.putExtra("id",list_notics.get(i).get("no_id").toString());
                                        in.putExtra("image",list_notics.get(i).get("titleimgpath").toString());
                                        startActivity(in);
                                    }
                                }
                            });
                            adapter.startScroll(recyclerView);
                        } else {
                            String errors = jsonObject.getJSONArray("errors").getString(0);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        } else {
//            initToast("请检查网络是否连接！");
        }
    }

    @Override
    public void onDestroy() {
        if (adapter!=null){
            adapter.stopRunThread();
        }
        super.onDestroy();

    }

    private TextView banshi,gongzuo,cancel;
    private CommonPopupWindow commonPopupWindow;

    /**
     * 切换用户popopwindow
     */
    private void getCutUserPop(){

        View upView = LayoutInflater.from(getContext()).inflate(R.layout.cutuser_pop, null);
        banshi = (TextView) upView.findViewById(R.id.banshi);
        gongzuo = (TextView) upView.findViewById(R.id.gongzuo);
        cancel = (TextView) upView.findViewById(R.id.cancel);
        banshi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                type.setText(zixun.getText().toString());
//                startActivity(new Intent(getContext(), G_LoginActivity.class));
//                getActivity().finish();
//                editor_app.putString("apptype","zhengwu");
//                editor_app.commit();
                commonPopupWindow.dismiss();
            }
        });

        gongzuo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                type.setText(tousu.getText().toString());
                startActivity(new Intent(getContext(), LoginActivity.class));
                getActivity().finish();
                editor_app.putString("apptype","shenpi");
                editor_app.commit();
                SharedPreferencesUtils.setParam(getContext().getApplicationContext(),"fragType","0");
                commonPopupWindow.dismiss();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                commonPopupWindow.dismiss();
            }
        });

        commonPopupWindow = new CommonPopupWindow.Builder(getContext())
                .setView(upView)
                .setAnimationStyle(R.anim.push_left_in)
                .setBackGroundLevel(0.5f)
                .setWidthAndHeight(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                .create();

        commonPopupWindow.showAtLocation(lin, Gravity.CENTER,0,0);
    }
}
