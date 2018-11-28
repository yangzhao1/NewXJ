package com.zq.xinjiang.unmethod;


import com.google.gson.Gson;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by yangzhao on 2018/4/10.
 */

public class MyOkHttpClient {

    private static MyOkHttpClient netClient;
    private MyOkHttpClient(){
        client = initOkHttpClient();
    }
    public final OkHttpClient client;
    private OkHttpClient initOkHttpClient(){
        //初始化的时候可以自定义一些参数
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .readTimeout(10000, TimeUnit.MILLISECONDS)//设置读取超时为10秒
                .connectTimeout(10000, TimeUnit.MILLISECONDS)//设置链接超时为10秒
                .build();
        return okHttpClient;
    }

    public static MyOkHttpClient getNetClient(){
        if(netClient == null){
            netClient = new MyOkHttpClient();
        }
        return netClient;
    }

    public void getCallNet(String url, final MyCallBack callBack){
        Request request = new Request.Builder().url(url).build();
        Call call = getNetClient().initOkHttpClient().newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callBack.onFailure(-1);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
//请求网络成功说明服务器有响应，但返回的是什么我们无法确定，可以根据响应码判断
                if (response.code() == 200) {
                    //如果是200说明正常，调用MyCallBack的成功方法，传入数据
                    callBack.onSuccess(response.body().string());
                }else{
                    //如果不是200说明异常，调用MyCallBack的失败方法将响应码传入
                    callBack.onFailure(response.code());
                }
            }
        });
    }

    public void postCallNet(String url,Map<String,String> map, final MyCallBack callBack){
        FormBody.Builder builder = new FormBody.Builder();
        if (map != null) {
            //增强for循环遍历
            for (Map.Entry<String, String> entry : map.entrySet()) {
                builder.add(entry.getKey(), entry.getValue());
            }
        }
        FormBody formBody = builder.build();
        Request request = new Request.Builder().post(formBody).url(url).build();
        Call call = getNetClient().initOkHttpClient().newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callBack.onFailure(-1);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
              //请求网络成功说明服务器有响应，但返回的是什么我们无法确定，可以根据响应码判断
                if (response.code() == 200) {
                    //如果是200说明正常，调用MyCallBack的成功方法，传入数据
                    callBack.onSuccess(response.body().string());
                }else{
                    //如果不是200说明异常，调用MyCallBack的失败方法将响应码传入
                    callBack.onFailure(response.code());
                }
            }
        });
    }



    public void post(String url,Map<String,String> map, final MyCallBack callBack){
        String json = new Gson().toJson(map);
        OkHttpClient client = new OkHttpClient();
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(JSON,json);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callBack.onFailure(-1);
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                 //请求网络成功说明服务器有响应，但返回的是什么我们无法确定，可以根据响应码判断
                if (response.code() == 200) {
                    //如果是200说明正常，调用MyCallBack的成功方法，传入数据
                    callBack.onSuccess(response.body().string());
                }else{
                    //如果不是200说明异常，调用MyCallBack的失败方法将响应码传入
                    callBack.onFailure(response.code());
                }
            }
        });
    }

    public void postHaveToken(String url,Map<String,String> map,String token,final MyCallBack callBack){
        String json = new Gson().toJson(map);
        OkHttpClient client = new OkHttpClient();
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(JSON,json);
        Request request = new Request.Builder()
                .url(url)
                .post(body).addHeader("token",token)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callBack.onFailure(-1);
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //请求网络成功说明服务器有响应，但返回的是什么我们无法确定，可以根据响应码判断
                if (response.code() == 200) {
                    //如果是200说明正常，调用MyCallBack的成功方法，传入数据
                    callBack.onSuccess(response.body().string());
                }else{
                    //如果不是200说明异常，调用MyCallBack的失败方法将响应码传入
                    callBack.onFailure(response.code());
                }
            }
        });
    }

    public void getHaveToken(String url,String token,final MyCallBack callBack){
        Request request = new Request.Builder().url(url).addHeader("token",token).build();
        Call call = getNetClient().initOkHttpClient().newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callBack.onFailure(-1);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //请求网络成功说明服务器有响应，但返回的是什么我们无法确定，可以根据响应码判断
                if (response.code() == 200) {
                    //如果是200说明正常，调用MyCallBack的成功方法，传入数据
                    callBack.onSuccess(response.body().string());
                }else{
                    //如果不是200说明异常，调用MyCallBack的失败方法将响应码传入
                    callBack.onFailure(response.code());
                }
            }
        });
    }
}
