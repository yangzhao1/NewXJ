package com.zq.xinjiang.government.activity;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.zq.xinjiang.BaseActivity;
import com.zq.xinjiang.R;

/**
 * Created by Administrator on 2017/10/27.
 * 嵌套网页界面
 */

public class G_WebActivity extends BaseActivity{

    private WebView webView;
    private TextView titleText,back;
    private String title_str = "便民";
    private int code = 0;
    private ProgressBar pg1;

    private String url_kuaidi="http://m.kuaidi100.com/index.jsp?from=openv";
    private String url_shenghuo="http://life.ule.com/";
    private String url_weizhang="http://m.weizhang8.cn/";
    private String url_jipiao="https://m.ceair.com/pages/booking/index.html";
    private String url_tianqi="http://m.hao123.com/a/tianqi";
    private String url_shenfenzheng="http://id.weixingmap.com/";
    private String url_huangli="https://wannianrili.51240.com/";
    private String url_zhaogongzuo="http://www.hao123.com/zhaopin";
    private String url_ditu="http://www.gaode.com/";
    private String url_shangpin="http://www.ancc.org.cn/Service/queryTools/Internal.aspx";
    private String url_shouji="http://m.ip138.com/mobile.html";

    /**
     *
     * 快递查询：https://www.ickd.cn/
     生活缴费：http://life.ule.com/
     违章查询：http://sn.122.gov.cn/views/inquiry.html
     机票预订：https://m.ceair.com/pages/booking/index.html

     天气查询：http://m.weather.com.cn/d/town/index?lat=34.23288&lon=108.90374
     身份证查询：http://id.weixingmap.com/
     万年历/黄历：https://wannianrili.51240.com/
     找工作：http://www.hao123.com/zhaopin
     地图导航：http://www.gaode.com/
     商品价格：http://www.ancc.org.cn/Service/queryTools/Internal.aspx
     手机号码归属地查询：http://m.ip138.com/mobile.html
     *
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.g_webmain);
        setStatusColor();

        init();
    }

    private void init(){
        title_str = getIntent().getStringExtra("title");
        code = getIntent().getIntExtra("code",0);

        webView = (WebView) findViewById(R.id.webview);
        back = (TextView) findViewById(R.id.back);
        titleText = (TextView) findViewById(R.id.titleText);
        pg1 = (ProgressBar) findViewById(R.id.progressBar1);

        titleText.setText(title_str);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        switch (code){
            case 0:
                break;
            case 1://天气查询
                setDataWebView(url_tianqi);
                break;
            case 2://身份证查询
                setDataWebView(url_shenfenzheng);
                break;
            case 3://黄历/万年历
                setDataWebView(url_huangli);
                break;
            case 4://机票
                setDataWebView(url_jipiao);
                break;
            case 5://找工作
                setDataWebView(url_zhaogongzuo);
                break;
            case 6://地图导航
                setDataWebView(url_ditu);
                break;
            case 7://商品查询
                setDataWebView(url_shangpin);
                break;
            case 8://手机号码归属地
                setDataWebView(url_shouji);
                break;
            case 9://快递查询
                setDataWebView(url_kuaidi);
                break;
            case 10://生活缴费
                setDataWebView(url_shenghuo);
                break;
            case 11://违章查询
                setDataWebView(url_weizhang);
                break;
            default:
                break;
        }
    }

    private void setDataWebView(String url){

        //加载服务器上的页面
        webView.loadUrl(url);
        //加载本地中的html
        //myWebView.loadUrl("file:///android_asset/www/test2.html");
        //加上下面这段代码可以使网页中的链接不以浏览器的方式打开
        webView.setWebViewClient(new WebViewClient());
        //得到webview设置
        WebSettings webSettings = webView.getSettings();
        //允许使用javascript
        webSettings.setJavaScriptEnabled(true);
        //将WebAppInterface于javascript绑定
//        webView.addJavascriptInterface(new WebAppInterface(this), "Android");

        webView.setWebViewClient(new WebViewClient(){
            //覆写shouldOverrideUrlLoading实现内部显示网页
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // TODO 自动生成的方法存根
                view.loadUrl(url);
                return true;
            }
        });

        webView.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                // TODO 自动生成的方法存根
                if(newProgress==100){
                    pg1.setVisibility(View.GONE);//加载完网页进度条消失
                }
                else{
                    pg1.setVisibility(View.VISIBLE);//开始加载网页时显示进度条
                    pg1.setProgress(newProgress);//设置进度值
                }

            }
        });
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            if(webView.canGoBack()) {//当webview不是处于第一页面时，返回上一个页面
                webView.goBack();
                return true;
            } else {//当webview处于第一页面时,直接退出程序
                finish();
            }
        }
        return super.onKeyDown(keyCode, event);
    }


}
